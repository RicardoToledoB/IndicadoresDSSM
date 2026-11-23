package cl.dssm.rrff.repository;
import cl.dssm.rrff.domain.AuditoriaEvento;
import org.springframework.data.jpa.repository.JpaRepository;
public interface AuditoriaEventoRepository extends JpaRepository<AuditoriaEvento,String> {
  org.springframework.data.domain.Page<AuditoriaEvento> findAllByOrderByFechaHoraDesc(org.springframework.data.domain.Pageable pageable);
}
