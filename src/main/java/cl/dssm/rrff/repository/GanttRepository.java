package cl.dssm.rrff.repository;
import cl.dssm.rrff.domain.Gantt;
import org.springframework.data.jpa.repository.JpaRepository;
public interface GanttRepository extends JpaRepository<Gantt,String> {
  java.util.List<Gantt> findByProyectoIdOrderByCreatedAtDesc(String proyectoId);
  long countByProyectoId(String proyectoId);
}
