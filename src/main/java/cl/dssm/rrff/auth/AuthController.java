package cl.dssm.rrff.auth;
import cl.dssm.rrff.domain.Usuario;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.Operation;
import cl.dssm.rrff.repository.UsuarioRepository;
import cl.dssm.rrff.service.AccessService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
@RestController @RequestMapping("/api/v1/auth")
public class AuthController {
  private final UsuarioRepository users; private final PasswordEncoder passwords;
  private final JwtEncoder jwt; private final AccessService access;
  @Value("${app.jwt.issuer}") private String issuer;
  @Value("${app.jwt.expiration-minutes:30}") private long minutes;
  public AuthController(UsuarioRepository u,PasswordEncoder p,JwtEncoder jwt,AccessService a){users=u;passwords=p;this.jwt=jwt;access=a;}
  public record Login(@NotBlank String username,@NotBlank String password){}
  @SecurityRequirements
  @Operation(summary="Autenticarse y obtener token JWT")
  @PostMapping("/login") public Map<String,Object> login(@Valid @RequestBody Login req){
    Usuario user=users.findByUsername(req.username().strip()).orElse(null);
    if(user==null || !Boolean.TRUE.equals(user.getActivo()) || !passwords.matches(req.password(),user.getPasswordHash()))
       throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Credenciales inválidas");
    Instant now=Instant.now();
    JwtClaimsSet claims=JwtClaimsSet.builder().issuer(issuer).issuedAt(now).expiresAt(now.plus(minutes,ChronoUnit.MINUTES))
       .subject(user.getUsername()).id(java.util.UUID.randomUUID().toString()).claim("roles",user.getRoles().stream().sorted().toList()).build();
    String token=jwt.encode(JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(),claims)).getTokenValue();
    return Map.of("accessToken",token,"tokenType","Bearer","expiresInSeconds",minutes*60,"roles",user.getRoles());
  }
  @GetMapping("/me") public Map<String,Object> me(Authentication authentication){
    return Map.of("username",authentication.getName(),"roles",authentication.getAuthorities().stream().map(x->x.getAuthority()).sorted().toList(),"writesEnabled",access.writesEnabled());
  }
}
