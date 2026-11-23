package cl.dssm.rrff.api;
import cl.dssm.rrff.domain.Usuario;
import cl.dssm.rrff.repository.UsuarioRepository;
import cl.dssm.rrff.service.AccessService;
import cl.dssm.rrff.service.AuditService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
@RestController @RequestMapping("/api/v1/admin/usuarios")
public class AdminController {
 private final UsuarioRepository users;private final PasswordEncoder encoder;
 private final AccessService access;private final AuditService audit;
 private static final Set<String> ALLOWED=Set.of("ROLE_ADMIN","ROLE_DIRECTOR","ROLE_SUBDIRECTOR_RRFF",
    "ROLE_JEFE_RRFF","ROLE_ARTICULACION","ROLE_SECRETARIA","ROLE_EDITOR_1","ROLE_EDITOR_3","ROLE_EDITOR_7");
 public AdminController(UsuarioRepository users,PasswordEncoder encoder,AccessService access,AuditService audit){
   this.users=users;this.encoder=encoder;this.access=access;this.audit=audit;
 }
 public record UserView(String id,String username,boolean activo,Set<String> roles){}
 public record UserIn(@NotBlank String username,@NotBlank String password,Set<String> roles){}
 @GetMapping public List<UserView> list(){access.admin();return users.findAll().stream().map(this::view).toList();}
 private UserView view(Usuario u){return new UserView(u.getId(),u.getUsername(),Boolean.TRUE.equals(u.getActivo()),u.getRoles());}
 @PostMapping @Transactional public UserView create(@Valid @RequestBody UserIn input){
  access.admin();
  String login=input.username().strip().toLowerCase(java.util.Locale.ROOT);
  if(!login.matches("[a-z0-9._-]{3,100}"))throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Usuario inválido");
  if(input.password().length()<14)throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Contraseña debe tener 14 caracteres o más");
  if(input.roles()==null || input.roles().isEmpty() || !ALLOWED.containsAll(input.roles()))
    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Roles desconocidos");
  if(users.findByUsername(login).isPresent())throw new ResponseStatusException(HttpStatus.CONFLICT,"Usuario existente");
  Usuario u=new Usuario();u.setUsername(login);u.setPasswordHash(encoder.encode(input.password()));
  u.setActivo(true);u.setRoles(Set.copyOf(input.roles()));users.saveAndFlush(u);
  audit.success("USUARIO",u.getId(),"CREAR");return view(u);
 }
 public record ActiveRequest(boolean activo){}
 @PatchMapping("/{id}/activo") @Transactional public UserView active(@PathVariable String id,@RequestBody ActiveRequest input){
  access.admin();Usuario u=users.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
  if(u.getUsername().equals(access.username()) && !input.activo())throw new ResponseStatusException(HttpStatus.CONFLICT,"No desactivar el propio usuario admin");
  u.setActivo(input.activo());users.flush();audit.success("USUARIO",u.getId(),input.activo()?"ACTIVAR":"DESACTIVAR");return view(u);
 }
}
