package cl.dssm.rrff.repository;
import cl.dssm.rrff.domain.Adenda;
import org.springframework.data.jpa.repository.JpaRepository;
public interface AdendaRepository extends JpaRepository<Adenda,String> {
  java.util.List<Adenda> findByConvenioIdOrderByCreatedAtDesc(String convenioId);
}
