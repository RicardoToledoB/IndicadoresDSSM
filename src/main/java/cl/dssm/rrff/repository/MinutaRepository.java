package cl.dssm.rrff.repository;
import cl.dssm.rrff.domain.Minuta;
import org.springframework.data.jpa.repository.JpaRepository;
public interface MinutaRepository extends JpaRepository<Minuta,String> {
  java.util.List<Minuta> findByProyectoIdOrderByCreatedAtDesc(String proyectoId);
  long countByProyectoId(String proyectoId);
}
