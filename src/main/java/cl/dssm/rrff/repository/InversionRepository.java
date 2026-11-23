package cl.dssm.rrff.repository;
import cl.dssm.rrff.domain.Inversion;
import org.springframework.data.jpa.repository.JpaRepository;
public interface InversionRepository extends JpaRepository<Inversion,String> {
  java.util.List<Inversion> findByProyectoIdOrderByCreatedAtDesc(String proyectoId);
}
