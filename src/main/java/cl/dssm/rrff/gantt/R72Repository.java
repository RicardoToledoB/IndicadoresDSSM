package cl.dssm.rrff.gantt;

import cl.dssm.rrff.gantt.R72Catalog.Field;
import cl.dssm.rrff.gantt.R72Catalog.Spec;
import com.fasterxml.jackson.databind.JsonNode;
import java.math.BigDecimal;
import java.sql.*;
import java.time.*;
import java.util.*;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

/** SQL identifiers ONLY originate in the static R05 allowlist. User input is always bound. */
@Repository
public class R72Repository {
  private final NamedParameterJdbcTemplate db;
  private final R72Catalog registry;
  public R72Repository(NamedParameterJdbcTemplate db, R72Catalog registry){this.db=db; this.registry=registry;}
  private static String q(String col){return "`"+col+"`";}
  private RowMapper<Map<String,Object>> mapper(Spec spec){
    return (rs,index)->{
      Map<String,Object> result=new LinkedHashMap<>();
      for (Field f:spec.columns()) {
        Object value=rs.getObject(f.db());
        if(value instanceof java.sql.Date d)value=d.toLocalDate().toString();
        if(value instanceof Timestamp ts)value=ts.toInstant().toString();
        if("BOOLEAN".equals(f.type()) && value instanceof Number n)value=n.intValue()!=0;
        result.put(f.json(),value);
      }
      return result;
    };
  }
  public Map<String,Object> get(String slug,String id){
    Spec spec=registry.require(slug);
    List<Map<String,Object>> matches=db.query("SELECT * FROM "+q(spec.table())+" WHERE "+q(spec.id())+"=:id",
       Map.of("id",id), mapper(spec));
    if(matches.isEmpty())throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Registro inexistente");
    return matches.get(0);
  }
  public Map<String,Object> list(String slug,int page,int size,Map<String,String> filters) {
    if(page<0 || page>100000 || size<1 || size>200)throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Paginación inválida (size 1..200)");
    Spec s=registry.require(slug);
    MapSqlParameterSource params=new MapSqlParameterSource();
    StringBuilder where=new StringBuilder(" WHERE 1=1");
    int i=0;
    for(var entry:filters.entrySet()){
      Field f=s.byJson(entry.getKey());
      if(f==null || !(f.db().endsWith("_id") || Set.of("active","status_id","record_type").contains(f.db())))
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Filtro no permitido: "+entry.getKey());
      if(entry.getValue().length()>180)throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Filtro demasiado largo");
      String key="f"+(i++);
      where.append(" AND ").append(q(f.db())).append("=:").append(key);
      params.addValue(key,entry.getValue());
    }
    String table=q(s.table());
    Long count=db.queryForObject("SELECT COUNT(*) FROM "+table+where,params,Long.class);
    params.addValue("lim",size).addValue("off",(long)page*size);
    String sql="SELECT * FROM "+table+where+" ORDER BY "+q(s.id())+" LIMIT :lim OFFSET :off";
    List<Map<String,Object>> items=db.query(sql,params,mapper(s));
    Map<String,Object> result=new LinkedHashMap<>();
    result.put("items",items);result.put("total",count==null?0:count);result.put("page",page);result.put("size",size);
    return result;
  }
  public List<Map<String,Object>> by(String slug,String dbField,String value){
    Spec s=registry.require(slug);
    if(s.byDb(dbField)==null)throw new IllegalArgumentException("Invalid field");
    return db.query("SELECT * FROM "+q(s.table())+" WHERE "+q(dbField)+"=:ref ORDER BY "+q(s.id()),Map.of("ref",value),mapper(s));
  }
  public List<Map<String,Object>> all(String slug){
    Spec s=registry.require(slug);
    return db.query("SELECT * FROM "+q(s.table())+" ORDER BY "+q(s.id()),new MapSqlParameterSource(),mapper(s));
  }
  public List<Map<String,Object>> taskAssignments(String scheduleId){
    Spec spec=registry.require("task-people");
    return db.query("SELECT a.* FROM `r72_task_people` a INNER JOIN `r72_schedule_tasks` t ON t.task_id=a.task_id WHERE t.schedule_id=:id ORDER BY a.relation_id",
       Map.of("id",scheduleId),mapper(spec));
  }
  public List<Map<String,Object>> assignedPeople(String scheduleId){
    Spec spec=registry.require("persons");
    return db.query("SELECT DISTINCT p.* FROM `r72_persons` p JOIN `r72_task_people` a ON a.person_id=p.person_id JOIN `r72_schedule_tasks` t ON t.task_id=a.task_id WHERE t.schedule_id=:id ORDER BY p.person_id",
      Map.of("id",scheduleId), mapper(spec));
  }
  public boolean published(String scheduleId) {
    Boolean published=db.query("SELECT version_published FROM `r72_schedules` WHERE schedule_id=:id",Map.of("id",scheduleId),rs->rs.next() ? (rs.getObject(1)==null ? null : rs.getBoolean(1)) : null);
    return Boolean.TRUE.equals(published);
  }
  public void guardHistorical(String slug,String id,Map<String,Object> changes){
    // Imported R04 cuts and source tasks remain immutable even when version_published was blank.
    if ((slug.equals("schedules") && id.matches("CR[0-9]+-[0-9]+")) ||
        (slug.equals("schedule-tasks") && id.matches("T[0-9]+")))
      throw new ResponseStatusException(HttpStatus.CONFLICT,"Registro original R04 inmutable: clonar antes de editar");
    if (slug.equals("task-updates"))
      throw new ResponseStatusException(HttpStatus.CONFLICT,"Las actualizaciones de avance son append-only");
    if (slug.equals("schedules")) {
      if(published(id))throw new ResponseStatusException(HttpStatus.CONFLICT,"Versión publicada: clonar para rectificar");
    } else if (slug.equals("schedule-tasks")) {
      Map<String,Object> row=get(slug,id);
      String schedule=String.valueOf(row.get("scheduleId"));
      if(published(schedule))throw new ResponseStatusException(HttpStatus.CONFLICT,"Tareas de versión publicada: clonar");
      if(changes.containsKey("scheduleId") && !schedule.equals(String.valueOf(changes.get("scheduleId"))))
        throw new ResponseStatusException(HttpStatus.CONFLICT,"No mover una tarea entre versiones");
    }
  }
  private Object fromJson(Field f,JsonNode node){
    if(node==null || node.isNull())return null;
    return switch(f.type()){
      case "TEXT" -> {
        if(!node.isTextual())throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Texto requerido: "+f.json());
        String s=node.asText();
        if (f.db().endsWith("_id") && s.length()>80)throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"ID demasiado largo");
        if(s.length()>40000)throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Texto excede límite");
        yield s;
      }
      case "INTEGER" -> {if(!node.isIntegralNumber())throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Entero requerido");yield node.intValue();}
      case "BOOLEAN" -> {if(!node.isBoolean())throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Booleano requerido");yield node.booleanValue();}
      case "NUMERIC(18,6)" -> {if(!node.isNumber())throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Número requerido");yield node.decimalValue();}
      case "DATE" -> {
        try {yield LocalDate.parse(node.asText());}catch(Exception ex){throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Fecha ISO inválida: "+f.json());}
      }
      case "TIMESTAMPTZ" -> {
        try {yield Timestamp.from(OffsetDateTime.parse(node.asText()).toInstant());}
        catch(Exception ex){throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Fecha/hora con zona horaria requerida");}
      }
      default -> throw new IllegalStateException("Type unsupported");
    };
  }
  private Map<String,Object> fields(Spec s,JsonNode data){
    if(!data.isObject() || data.size()==0)throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Objeto JSON requerido");
    Map<String,Object> values=new LinkedHashMap<>();
    Iterator<String> keys=data.fieldNames();
    while(keys.hasNext()){
      String key=keys.next(); Field f=s.byJson(key);
      if(f==null)throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Campo no reconocido: "+key);
      values.put(f.db(),fromJson(f,data.get(key)));
    }
    return values;
  }
  public Map<String,Object> create(String slug, JsonNode data){
    Spec s=registry.require(slug);Map<String,Object> row=fields(s,data);
    if(row.get(s.id())==null || row.get(s.id()).toString().isBlank())throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"ID obligatorio");
    String cols=String.join(",",row.keySet().stream().map(R72Repository::q).toList());
    String placeholders=String.join(",",row.keySet().stream().map(x->":"+x).toList());
    try {db.update("INSERT INTO "+q(s.table())+" ("+cols+") VALUES ("+placeholders+")",new MapSqlParameterSource(row));}
    catch(DuplicateKeyException e){throw new ResponseStatusException(HttpStatus.CONFLICT,"Registro duplicado");}
    return get(slug,String.valueOf(row.get(s.id())));
  }
  public Map<String,Object> update(String slug,String id,JsonNode data){
    Spec s=registry.require(slug);
    get(slug,id);
    Map<String,Object> row=fields(s,data);
    if(row.containsKey(s.id()) && !id.equals(String.valueOf(row.get(s.id()))))
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"El ID no puede modificarse");
    row.remove(s.id());
    if(row.isEmpty())throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Sin campos para actualizar");
    guardHistorical(slug,id,row);
    String sets=String.join(",",row.keySet().stream().map(x->q(x)+"=:"+x).toList());
    row.put("_id",id);
    db.update("UPDATE "+q(s.table())+" SET "+sets+" WHERE "+q(s.id())+"=:_id",new MapSqlParameterSource(row));
    return get(slug,id);
  }
  public void audit(String resource,String id,String operation,String actor){
    db.update("INSERT INTO `r72_audit_log` (`audit_id`,`table`,`record_id`,`operation`,`user_id`,`date_time`,`detail`) "+
        "VALUES (:id,:t,:rid,:op,NULL,:now,:detail)",new MapSqlParameterSource()
        .addValue("id",UUID.randomUUID().toString()).addValue("t",resource).addValue("rid",id)
        .addValue("op",operation).addValue("now",Timestamp.from(Instant.now())).addValue("detail","jwtActor="+actor));
  }
  public NamedParameterJdbcTemplate jdbc(){return db;}
}
