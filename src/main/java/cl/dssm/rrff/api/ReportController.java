package cl.dssm.rrff.api;
import cl.dssm.rrff.domain.*;
import cl.dssm.rrff.repository.*;
import cl.dssm.rrff.service.AccessService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
/** Vista JSON, NO reportes oficiales ni certificación de cifras. */
@RestController @RequestMapping("/api/v1/reportabilidad")
public class ReportController {
 private final ProyectoRepository proyectos;private final MinutaRepository minutas;
 private final GanttRepository gantts;private final GanttTareaRepository tareas;
 private final InversionRepository inversiones;private final AccessService access;
 public ReportController(ProyectoRepository p,MinutaRepository m,GanttRepository g,GanttTareaRepository t,
   InversionRepository i,AccessService a){proyectos=p;minutas=m;gantts=g;tareas=t;inversiones=i;access=a;}
 @GetMapping("/proyectos/{id}/vista-ejecutiva") @Transactional(readOnly=true)
 public Map<String,Object> vista(@PathVariable String id,@RequestParam(required=false) LocalDate fechaCorte){
   access.read(7);
   Proyecto p=proyectos.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Proyecto no encontrado"));
   List<Minuta> ms=minutas.findByProyectoIdOrderByCreatedAtDesc(id).stream()
     .filter(m->fechaCorte==null || m.getFechaCorte()!=null && !m.getFechaCorte().isAfter(fechaCorte)).toList();
   List<Gantt> gs=gantts.findByProyectoIdOrderByCreatedAtDesc(id).stream()
     .filter(g->fechaCorte==null || g.getFechaCorte()!=null && !g.getFechaCorte().isAfter(fechaCorte)).toList();
   List<Map<String,Object>> cronogramas=new ArrayList<>();
   for(Gantt g:gs){Map<String,Object> c=new LinkedHashMap<>();c.put("gantt",g);c.put("tareas",tareas.findByGanttIdOrderByCreatedAtAsc(g.getId()));cronogramas.add(c);}
   List<String> advertencias=new ArrayList<>();
   if(ms.isEmpty())advertencias.add("Sin minuta: dato ausente, no equivale a cero");
   if(gs.isEmpty())advertencias.add("Sin carta Gantt validada para consulta");
   List<Inversion> partidas=inversiones.findByProyectoIdOrderByCreatedAtDesc(id);
   if(partidas.isEmpty())advertencias.add("Sin partidas de inversión: no se certifican montos");
   Map<String,Object> r=new LinkedHashMap<>();
   r.put("tipo","PREVISUALIZACION_NO_OFICIAL");r.put("fechaCorteSolicitada",fechaCorte);
   r.put("proyecto",p);r.put("minutasVersionadas",ms);r.put("ganttVersionados",cronogramas);
   r.put("partidasSinSumatoria",partidas);r.put("advertencias",advertencias);
   r.put("exportacionAutorizada",false);
   return r;
 }
}
