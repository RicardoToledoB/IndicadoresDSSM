package cl.dssm.rrff.service;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
@Service
public class AccessService {
    private final boolean writesEnabled;
    public AccessService(@Value("${app.features.writes-enabled:false}") boolean writesEnabled){this.writesEnabled=writesEnabled;}
    private static final Map<Integer,Set<String>> READ = Map.of(
      7, Set.of("ROLE_DIRECTOR","ROLE_SUBDIRECTOR_RRFF","ROLE_ARTICULACION"),
      3, Set.of("ROLE_DIRECTOR","ROLE_SUBDIRECTOR_RRFF","ROLE_JEFE_RRFF","ROLE_ARTICULACION"),
      1, Set.of("ROLE_SUBDIRECTOR_RRFF","ROLE_JEFE_RRFF","ROLE_ARTICULACION","ROLE_SECRETARIA")
    );
    private Set<String> authorities(){
      Authentication a=SecurityContextHolder.getContext().getAuthentication();
      if(a==null || !a.isAuthenticated())throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
      return a.getAuthorities().stream().map(x->x.getAuthority()).collect(java.util.stream.Collectors.toSet());
    }
    public void read(int module){
      Set<String> auth=authorities();
      if(auth.contains("ROLE_ADMIN"))return;
      if(READ.getOrDefault(module,Set.of()).stream().noneMatch(auth::contains))
        throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Sin permiso de consulta para el módulo");
    }
    public void write(int module){
      if(!writesEnabled)throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Escritura no habilitada: requiere autorización institucional");
      Set<String> auth=authorities();
      if(!auth.contains("ROLE_ADMIN") && !auth.contains("ROLE_EDITOR_"+module))
        throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Sin permiso de escritura");
    }
    public void admin(){if(!authorities().contains("ROLE_ADMIN"))throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Solo administración técnica");}
    public boolean writesEnabled(){return writesEnabled;}
    public String username(){Authentication a=SecurityContextHolder.getContext().getAuthentication();return a==null?"sistema":a.getName();}
}
