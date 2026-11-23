package cl.dssm.rrff.config;
import cl.dssm.rrff.domain.Usuario;
import cl.dssm.rrff.repository.UsuarioRepository;
import java.util.Set;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
@Configuration
public class BootstrapConfig {
  @Bean CommandLineRunner bootstrapAdmin(UsuarioRepository repo,PasswordEncoder passwords,
    @Value("${app.bootstrap.enabled:false}") boolean enabled,
    @Value("${app.bootstrap.username:admin}") String username,
    @Value("${app.bootstrap.password:REEMPLAZAR}") String password) {
      return args -> {
        if(!enabled)return;
        if(password.length()<14 || password.startsWith("REEMPLAZAR") || username.isBlank())
           throw new IllegalStateException("Cambiar app.bootstrap.password por una clave privada fuerte (14+ caracteres)");
        if(repo.findByUsername(username).isPresent())return;
        Usuario u=new Usuario();u.setUsername(username);u.setPasswordHash(passwords.encode(password));
        u.setActivo(true);u.setRoles(Set.of("ROLE_ADMIN"));repo.save(u);
      };
  }
}
