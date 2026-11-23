package cl.dssm.rrff.gantt;

import cl.dssm.rrff.service.AccessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

/** R05 /api/v1/rrff. Same R04 identifiers, camelCase DTOs, MySQL snake_case database. */
@RestController
@Tag(name="7.2 Carta Gantt", description="Recursos parametrizados R05 y consultas integradas de proyectos y cronogramas")
@RequestMapping("/api/v1/rrff")
public class R72Controller {
  private static final Set<String> ADMIN_ONLY = Set.of("users","profiles","user-profiles","persons", "source-actors");
  private static final Set<String> LOCKED_WRITES = Set.of("users","profiles","user-profiles");
  private final R72Repository repo;
  private final AccessService access;
  private final R72GanttService gantt;
  private final R72Catalog catalog;
  public R72Controller(R72Repository repo,AccessService access,R72GanttService gantt,R72Catalog catalog) {
    this.repo=repo;this.access=access;this.gantt=gantt;this.catalog=catalog;
  }
  private void read(String resource){
    if(ADMIN_ONLY.contains(resource))access.admin();else access.read(7);
  }
  private void write(String resource){
    access.write(7);
    if(LOCKED_WRITES.contains(resource))access.admin();
  }
  @Operation(summary="Listar recurso R05", description="Recurso identificado por el parametro {resource}. Ver contrato R05 para los nombres y campos disponibles.")
  @GetMapping("/{resource}") @Transactional(readOnly=true)
  public Map<String,Object> list(@PathVariable String resource,
    @RequestParam(defaultValue="0") int page,@RequestParam(defaultValue="25") int size,
    @RequestParam Map<String,String> allParams){
    read(resource);
    Map<String,String> filters=new LinkedHashMap<>(allParams);
    filters.remove("page");filters.remove("size");
    return repo.list(resource,page,size,filters);
  }
  @GetMapping("/{resource}/{id}") @Transactional(readOnly=true)
  public Map<String,Object> get(@PathVariable String resource,@PathVariable String id){
    read(resource);return repo.get(resource,id);
  }
  @PostMapping("/{resource}") @Transactional
  public ResponseEntity<Map<String,Object>> create(@PathVariable String resource,@RequestBody JsonNode data){
    write(resource);
    Map<String,Object> item=repo.create(resource,data);
    repo.audit(resource,String.valueOf(item.get(idProperty(resource))),"CREATE",access.username());
    return ResponseEntity.status(HttpStatus.CREATED).body(item);
  }
  @PutMapping("/{resource}/{id}") @Transactional
  public Map<String,Object> update(@PathVariable String resource,@PathVariable String id,@RequestBody JsonNode data){
    write(resource);
    Map<String,Object> item=repo.update(resource,id,data);
    repo.audit(resource,id,"UPDATE",access.username());
    return item;
  }
  private String idProperty(String resource){
    R72Catalog.Spec spec=catalog.require(resource);
    return spec.byDb(spec.id()).json();
  }
  @Operation(summary="Carta Gantt completa de un proyecto y cronograma")
  @GetMapping("/projects/{projectId}/gantt") @Transactional(readOnly=true)
  public Map<String,Object> gantt(@PathVariable String projectId,@RequestParam String scheduleId){
    access.read(7);return gantt.full(projectId,scheduleId);
  }
  @Operation(summary="Obtener avance registrado de un cronograma")
  @GetMapping("/projects/{projectId}/gantt/{scheduleId}/progress") @Transactional(readOnly=true)
  public Map<String,Object> progress(@PathVariable String projectId,@PathVariable String scheduleId){
    access.read(7);return gantt.progress(projectId,scheduleId);
  }
  @Operation(summary="Clonar cronograma para nueva version", description="Escritura bloqueada por defecto; solo usuarios autorizados pueden ejecutarla.")
  @PostMapping("/projects/{projectId}/gantt/{scheduleId}/clone") @Transactional
  public ResponseEntity<Map<String,Object>> cloneSchedule(@PathVariable String projectId,@PathVariable String scheduleId){
    access.write(7);
    Map<String,Object> result=gantt.cloneSchedule(projectId,scheduleId,access.username());
    return ResponseEntity.status(HttpStatus.CREATED).body(result);
  }
}
