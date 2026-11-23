package cl.dssm.rrff.api;
import cl.dssm.rrff.domain.*;
import cl.dssm.rrff.service.AccessService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
/** Consultas de entidades catalogadas. Los documentos solo son visibles por admin
 *  mientras no exista clasificación + ACL por objeto autorizada. */
@RestController @RequestMapping("/api/v1")
public class ReadController {
  public record Resource(Class<? extends BaseEntity> type,int module,String searchField,String filterField){}
  private static final Map<String,Resource> TYPES=Map.ofEntries(
    Map.entry("proyectos",new Resource(Proyecto.class,3,"nombre",null)),
    Map.entry("minutas",new Resource(Minuta.class,7,null,"proyectoId")),
    Map.entry("gantt",new Resource(Gantt.class,7,"nombre","proyectoId")),
    Map.entry("gantt-tareas",new Resource(GanttTarea.class,7,"denominacion","ganttId")),
    Map.entry("inversiones",new Resource(Inversion.class,3,null,"proyectoId")),
    Map.entry("convenios",new Resource(Convenio.class,3,"titulo",null)),
    Map.entry("adendas",new Resource(Adenda.class,3,null,"convenioId")),
    Map.entry("correspondencia",new Resource(Correspondencia.class,1,"materia",null)),
    Map.entry("correlativos",new Resource(Correlativo.class,1,null,null)),
    Map.entry("crc",new Resource(Crc.class,1,"materia",null)),
    Map.entry("hitos",new Resource(Hito.class,3,"denominacion","proyectoId")),
    Map.entry("derivaciones",new Resource(Derivacion.class,1,null,"correspondenciaId")),
    Map.entry("convenios-proyectos",new Resource(ConvenioProyecto.class,3,null,"convenioId")),
    Map.entry("instrumentos-organicos",new Resource(InstrumentoOrganico.class,1,"emisor",null)),
    Map.entry("reportes-ejecucion",new Resource(ReporteEjecucion.class,7,null,"proyectoId")),
    Map.entry("documentos",new Resource(Documento.class,0,"titulo",null)),
    Map.entry("documentos-versiones",new Resource(DocumentoVersion.class,0,null,"documentoId")),
    Map.entry("permisos",new Resource(PerfilPermiso.class,0,null,"actorId")),
    Map.entry("auditoria",new Resource(AuditoriaEvento.class,0,null,null)),
    Map.entry("bienes",new Resource(Bien.class,2,null,null)),
    Map.entry("mantenciones",new Resource(Mantencion.class,4,"actividad","bienId")),
    Map.entry("pac-items",new Resource(PacItem.class,5,"descripcion",null)),
    Map.entry("indicadores",new Resource(Indicador.class,6,"nombre",null))
  );
  private final EntityManager em; private final AccessService access;
  public ReadController(EntityManager em,AccessService access){this.em=em;this.access=access;}
  private Resource resource(String key){
    Resource r=TYPES.get(key);
    if(r==null)throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Recurso inexistente");
    if(r.module()==0) access.admin(); else access.read(r.module());
    return r;
  }
  @GetMapping("/{resource}") @Transactional(readOnly=true)
  public Map<String,Object> list(@PathVariable String resource,@RequestParam(defaultValue="0") int page,
    @RequestParam(defaultValue="20") int size,@RequestParam(required=false) String q,
    @RequestParam(required=false) String ref){
    Resource r=resource(resource);
    if(page<0 || size<1 || size>100 || page>100000)throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Paginación inválida");
    if(q!=null && q.length()>180)throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Filtro demasiado largo");
    if(ref!=null && ref.length()>36)throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Referencia inválida");
    StringBuilder jpql=new StringBuilder("SELECT e FROM ").append(r.type().getSimpleName()).append(" e WHERE 1=1");
    if(q!=null && !q.isBlank() && r.searchField()!=null)jpql.append(" AND lower(e.").append(r.searchField()).append(") LIKE :q");
    if(ref!=null && !ref.isBlank() && r.filterField()!=null)jpql.append(" AND e.").append(r.filterField()).append(" = :ref");
    jpql.append(" ORDER BY e.createdAt DESC");
    TypedQuery<? extends BaseEntity> query=em.createQuery(jpql.toString(),r.type());
    if(jpql.indexOf(":q")>=0)query.setParameter("q","%"+q.toLowerCase(java.util.Locale.ROOT)+"%");
    if(jpql.indexOf(":ref")>=0)query.setParameter("ref",ref);
    List<? extends BaseEntity> result=query.setFirstResult(page*size).setMaxResults(size+1).getResultList();
    boolean more=result.size()>size;
    return Map.of("items",more?result.subList(0,size):result,"page",page,"size",size,"hasMore",more);
  }
  @GetMapping("/{resource}/{id}") @Transactional(readOnly=true)
  public BaseEntity get(@PathVariable String resource,@PathVariable String id){
    Resource r=resource(resource);
    BaseEntity item=em.find(r.type(),id);
    if(item==null)throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Registro no encontrado");
    return item;
  }
}
