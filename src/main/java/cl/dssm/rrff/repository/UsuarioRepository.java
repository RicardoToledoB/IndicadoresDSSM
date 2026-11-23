package cl.dssm.rrff.repository;
import cl.dssm.rrff.domain.Usuario;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
public interface UsuarioRepository extends JpaRepository<Usuario,String> { Optional<Usuario> findByUsername(String username); }
