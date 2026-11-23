package cl.dssm.rrff.repository;
import cl.dssm.rrff.domain.GanttTarea;
import org.springframework.data.jpa.repository.JpaRepository;
public interface GanttTareaRepository extends JpaRepository<GanttTarea,String> {
  java.util.List<GanttTarea> findByGanttIdOrderByCreatedAtAsc(String ganttId);
}
