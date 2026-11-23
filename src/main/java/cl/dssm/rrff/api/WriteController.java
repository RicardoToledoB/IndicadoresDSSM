package cl.dssm.rrff.api;
import cl.dssm.rrff.domain.*;
import cl.dssm.rrff.repository.*;
import cl.dssm.rrff.service.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
/** Escritura de demostración desactivada de fábrica (app.features.writes-enabled=false).
 *  No se publica/formaliza ni reserva un correlativo sin definición institucional. */
@RestController @RequestMapping("/api/v1")
public class WriteController {
 private final ProyectoRepository proyectos; private final MinutaRepository minutas;
 private final GanttRepository gantts; private final GanttTareaRepository tareas;
 private final InversionRepository inversiones; private final ConvenioRepository convenios;
 private final AdendaRepository adendas; private final CorrespondenciaRepository correspondencias;
 private final AccessService access; private final AuditService audit;
 public WriteController(ProyectoRepository proyectos,MinutaRepository minutas,GanttRepository gantts,
   GanttTareaRepository tareas,InversionRepository inversiones,ConvenioRepository convenios,
   AdendaRepository adendas,CorrespondenciaRepository correspondencias,AccessService access,AuditService audit){
   this.proyectos=proyectos;this.minutas=minutas;this.gantts=gantts;this.tareas=tareas;
   this.inversiones=inversiones;this.convenios=convenios;this.adendas=adendas;
   this.correspondencias=correspondencias;this.access=access;this.audit=audit;
 }
 private Proyecto project(String id){return proyectos.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Proyecto inexistente"));}
 private Convenio convenio(String id){return convenios.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Convenio inexistente"));}
 private void checkVersion(Long actual,Long expected){if(expected==null || !expected.equals(actual))throw new ResponseStatusException(HttpStatus.CONFLICT,"Versión desactualizada; consultar ficha nuevamente");}
 private void input(boolean condition,String message){if(!condition)throw new ResponseStatusException(HttpStatus.BAD_REQUEST,message);}

 public record ProyectoIn(@NotBlank String nombre,String codigoBip,String comuna,String etapa,String prioridad,String fuenteReferencia,Integer periodoFuente){}
 @PostMapping("/proyectos") @Transactional
 public Proyecto newProject(@Valid @RequestBody ProyectoIn dto){
  access.write(3);Rules.year(dto.periodoFuente());
  Proyecto p=new Proyecto();p.setNombre(dto.nombre().strip());p.setCodigoBip(dto.codigoBip());
  p.setComuna(dto.comuna());p.setEtapa(dto.etapa());p.setPrioridad(dto.prioridad());
  p.setFuenteReferencia(dto.fuenteReferencia());p.setPeriodoFuente(dto.periodoFuente());
  proyectos.saveAndFlush(p);audit.success("PROYECTO",p.getId(),"CREAR");return p;
 }
 public record ProyectoPatch(@NotNull Long expectedRowVersion,String nombre,String codigoBip,String comuna,String etapa,String prioridad,String fuenteReferencia,Integer periodoFuente){}
 @PatchMapping("/proyectos/{id}") @Transactional
 public Proyecto editProject(@PathVariable String id,@Valid @RequestBody ProyectoPatch dto){
  access.write(3);Proyecto p=project(id);checkVersion(p.getRowVersion(),dto.expectedRowVersion());
  if(dto.nombre()!=null){input(!dto.nombre().isBlank(),"Nombre vacío");p.setNombre(dto.nombre().strip());}
  if(dto.codigoBip()!=null)p.setCodigoBip(dto.codigoBip());
  if(dto.comuna()!=null)p.setComuna(dto.comuna());
  if(dto.etapa()!=null)p.setEtapa(dto.etapa());
  if(dto.prioridad()!=null)p.setPrioridad(dto.prioridad());
  if(dto.fuenteReferencia()!=null)p.setFuenteReferencia(dto.fuenteReferencia());
  if(dto.periodoFuente()!=null){Rules.year(dto.periodoFuente());p.setPeriodoFuente(dto.periodoFuente());}
  proyectos.flush();audit.success("PROYECTO",p.getId(),"RECTIFICAR");return p;
 }
 public record InversionIn(@NotBlank String proyectoId,Integer periodo,String fuente,String moneda,BigDecimal monto){}
 @PostMapping("/inversiones") @Transactional
 public Inversion newInvestment(@Valid @RequestBody InversionIn dto){
  access.write(3);project(dto.proyectoId());Rules.year(dto.periodo());Rules.amountCurrency(dto.monto(),dto.moneda());
  Inversion v=new Inversion();v.setProyectoId(dto.proyectoId());v.setPeriodo(dto.periodo());
  v.setFuente(dto.fuente());v.setMoneda(dto.moneda());v.setMonto(dto.monto());
  inversiones.saveAndFlush(v);audit.success("INVERSION",v.getId(),"REGISTRAR");return v;
 }
 public record ConvenioIn(@NotBlank String titulo,Integer inicioPeriodo,Integer finPeriodo,String referenciaDocumento){}
 @PostMapping("/convenios") @Transactional
 public Convenio newConvenio(@Valid @RequestBody ConvenioIn dto){
  access.write(3);Rules.year(dto.inicioPeriodo());Rules.year(dto.finPeriodo());
  input(dto.inicioPeriodo()==null || dto.finPeriodo()==null || dto.finPeriodo()>=dto.inicioPeriodo(),"Período incoherente");
  Convenio c=new Convenio();c.setTitulo(dto.titulo());c.setInicioPeriodo(dto.inicioPeriodo());
  c.setFinPeriodo(dto.finPeriodo());c.setEstatusFormal("NO_ACREDITADO");c.setReferenciaDocumento(dto.referenciaDocumento());
  convenios.saveAndFlush(c);audit.success("CONVENIO",c.getId(),"CREAR_NO_ACREDITADO");return c;
 }
 public record AdendaIn(@NotBlank String convenioId,LocalDate fechaInstrumento){}
 @PostMapping("/adendas") @Transactional
 public Adenda newAdenda(@Valid @RequestBody AdendaIn dto){
  access.write(3);convenio(dto.convenioId());
  Adenda a=new Adenda();a.setConvenioId(dto.convenioId());a.setFechaInstrumento(dto.fechaInstrumento());a.setEstatusFormal("NO_ACREDITADO");
  adendas.saveAndFlush(a);audit.success("ADENDA",a.getId(),"CREAR_NO_ACREDITADO");return a;
 }
 public record MinutaIn(@NotBlank String proyectoId,LocalDate fechaCorte,String titulo,String resumen,BigDecimal montoInformado){}
 @PostMapping("/minutas") @Transactional
 public Minuta newMinuta(@Valid @RequestBody MinutaIn dto){
  access.write(7);project(dto.proyectoId());
  Minuta m=new Minuta();m.setProyectoId(dto.proyectoId());m.setFechaCorte(dto.fechaCorte());
  m.setTitulo(dto.titulo());m.setResumen(dto.resumen());m.setMontoInformado(dto.montoInformado());
  m.setVersionDocumental(Math.toIntExact(minutas.countByProyectoId(dto.proyectoId())+1));
  minutas.saveAndFlush(m);audit.success("MINUTA",m.getId(),"CREAR_VERSION");return m;
 }
 public record TareaIn(String denominacion,LocalDate inicio,LocalDate fin,BigDecimal avance){}
 public record GanttIn(@NotBlank String proyectoId,@NotBlank String nombre,LocalDate fechaCorte,List<@Valid TareaIn> tareas){}
 @PostMapping("/gantt") @Transactional
 public Gantt newGantt(@Valid @RequestBody GanttIn dto){
  access.write(7);project(dto.proyectoId());
  Gantt g=new Gantt();g.setProyectoId(dto.proyectoId());g.setNombre(dto.nombre());g.setFechaCorte(dto.fechaCorte());
  g.setVersionDocumental(Math.toIntExact(gantts.countByProyectoId(dto.proyectoId())+1));
  gantts.saveAndFlush(g);
  if(dto.tareas()!=null)for(TareaIn item:dto.tareas()){
    input(item.denominacion()!=null && !item.denominacion().isBlank(),"Tarea sin denominación");
    Rules.range(item.inicio(),item.fin());
    GanttTarea t=new GanttTarea();t.setGanttId(g.getId());t.setDenominacion(item.denominacion());
    t.setInicio(item.inicio());t.setFin(item.fin());t.setAvance(item.avance());tareas.save(t);
  }
  tareas.flush();audit.success("GANTT",g.getId(),"CREAR_VERSION");return g;
 }
 public record CorrespondenciaIn(Integer anio,LocalDate fechaRecepcion,LocalTime horaDocumento,
    LocalDate fechaDocumento,String origen,String tipoDocumento,String numeroDocumento,String materia,String responsable){}
 @PostMapping("/correspondencia") @Transactional
 public Correspondencia newCorrespondencia(@RequestBody CorrespondenciaIn dto){
  access.write(1);Rules.year(dto.anio());
  Correspondencia c=new Correspondencia();c.setAnio(dto.anio());c.setFechaRecepcion(dto.fechaRecepcion());
  c.setHoraDocumento(dto.horaDocumento());c.setFechaDocumento(dto.fechaDocumento());c.setOrigen(dto.origen());
  c.setTipoDocumento(dto.tipoDocumento());c.setNumeroDocumento(dto.numeroDocumento());
  c.setMateria(dto.materia());c.setResponsable(dto.responsable());
  correspondencias.saveAndFlush(c);audit.success("CORRESPONDENCIA",c.getId(),"REGISTRAR");return c;
 }
}
