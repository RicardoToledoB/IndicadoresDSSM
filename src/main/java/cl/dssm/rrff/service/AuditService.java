package cl.dssm.rrff.service;
import cl.dssm.rrff.domain.AuditoriaEvento;
import cl.dssm.rrff.repository.AuditoriaEventoRepository;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.springframework.stereotype.Service;
@Service
public class AuditService {
  private final AuditoriaEventoRepository repo;
  private final AccessService access;
  public AuditService(AuditoriaEventoRepository repo,AccessService access){this.repo=repo;this.access=access;}
  public void success(String recurso,String id,String accion){
    AuditoriaEvento e=new AuditoriaEvento();
    e.setRecursoId(recurso+":"+id);e.setAccion(accion);e.setFechaHora(LocalDateTime.now(ZoneOffset.UTC));
    e.setResultado("OK");e.setDetalle("usuario="+access.username());repo.save(e);
  }
}
