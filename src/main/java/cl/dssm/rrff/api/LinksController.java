package cl.dssm.rrff.api;
import cl.dssm.rrff.domain.*;
import cl.dssm.rrff.repository.*;
import cl.dssm.rrff.service.*;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
/** Operaciones DT de enlace; controladas por app.features.writes-enabled. */
@RestController @RequestMapping("/api/v1")
public class LinksController {
  private final EntityManager em;private final AccessService access;private final AuditService audit;
  public LinksController(EntityManager em,AccessService a,AuditService audit){this.em=em;this.access=a;this.audit=audit;}
  private void exists(Class<?> entity,String id){
    if(id==null || em.find(entity,id)==null)throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Referencia inexistente");
  }
  public record HitoIn(@NotBlank String proyectoId,String denominacion,LocalDate inicio,LocalDate fin,String estado){}
  @PostMapping("/hitos") @Transactional public Hito hito(@Valid @RequestBody HitoIn request){
    access.write(3);exists(Proyecto.class,request.proyectoId());
    Hito h=new Hito();h.setProyectoId(request.proyectoId());h.setDenominacion(request.denominacion());
    Rules.range(request.inicio(),request.fin());h.setInicio(request.inicio());h.setFin(request.fin());h.setEstado(request.estado());
    em.persist(h);em.flush();audit.success("HITO",h.getId(),"REGISTRAR");return h;
  }
  public record DerivacionIn(@NotBlank String correspondenciaId,String destino,LocalDate fecha,String observacion){}
  @PostMapping("/derivaciones") @Transactional public Derivacion derivar(@Valid @RequestBody DerivacionIn request){
    access.write(1);exists(Correspondencia.class,request.correspondenciaId());
    Derivacion d=new Derivacion();d.setCorrespondenciaId(request.correspondenciaId());
    d.setDestino(request.destino());d.setFecha(request.fecha());d.setObservacion(request.observacion());
    em.persist(d);em.flush();audit.success("DERIVACION",d.getId(),"REGISTRAR");return d;
  }
  public record ConvenioProyectoIn(@NotBlank String convenioId,@NotBlank String proyectoId,String tipoVinculo){}
  @PostMapping("/convenios-proyectos") @Transactional public ConvenioProyecto link(@Valid @RequestBody ConvenioProyectoIn request){
    access.write(3);exists(Convenio.class,request.convenioId());exists(Proyecto.class,request.proyectoId());
    ConvenioProyecto link=new ConvenioProyecto();link.setConvenioId(request.convenioId());link.setProyectoId(request.proyectoId());link.setTipoVinculo(request.tipoVinculo());
    em.persist(link);em.flush();audit.success("CONVENIO_PROYECTO",link.getId(),"ASOCIAR");return link;
  }
}
