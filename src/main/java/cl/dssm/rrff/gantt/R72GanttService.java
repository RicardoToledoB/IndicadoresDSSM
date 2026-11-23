package cl.dssm.rrff.gantt;

import cl.dssm.rrff.service.AccessService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/** Integrated Gantt view, source progress without invented computation, and version-safe cloning. */
@Service
public class R72GanttService {
  private final R72Repository repo;
  private final ObjectMapper mapper;
  public R72GanttService(R72Repository repo,ObjectMapper mapper){this.repo=repo;this.mapper=mapper;}
  private Map<String,Object> checked(String projectId,String scheduleId){
    Map<String,Object> schedule=repo.get("schedules",scheduleId);
    if(!projectId.equals(schedule.get("projectId")))
      throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Cronograma no pertenece al proyecto");
    return schedule;
  }
  public Map<String,Object> full(String projectId,String scheduleId){
    Map<String,Object> project=repo.get("projects",projectId);
    Map<String,Object> schedule=checked(projectId,scheduleId);
    Map<String,Object> out=new LinkedHashMap<>();
    out.put("project",project);out.put("schedule",schedule);
    out.put("tasks",repo.by("schedule-tasks","schedule_id",scheduleId));
    out.put("taskAssignments",repo.taskAssignments(scheduleId));
    out.put("people",repo.assignedPeople(scheduleId));
    out.put("stages",repo.all("stages"));out.put("statuses",repo.all("statuses"));
    out.put("dependencies",repo.by("task-dependencies","schedule_id",scheduleId));
    return out;
  }
  public Map<String,Object> progress(String projectId,String scheduleId){
    Map<String,Object> schedule=checked(projectId,scheduleId);
    List<Map<String,Object>> tasks=repo.by("schedule-tasks","schedule_id",scheduleId);
    // task_updates are counted with a relational join rather than iterating all tasks.
    Long updateCount=repo.jdbc().queryForObject("SELECT COUNT(*) FROM `r72_task_updates` u JOIN `r72_schedule_tasks` t ON t.task_id=u.task_id WHERE t.schedule_id=:scheduleId",
      Map.of("scheduleId",scheduleId), Long.class);
    long withProgress=tasks.stream().filter(t -> t.get("actualProgressPercent")!=null).count();
    Map<String,Object> out=new LinkedHashMap<>();
    out.put("projectId",projectId);out.put("scheduleId",scheduleId);
    out.put("sourceOverallProgressRaw",schedule.get("sourceOverallProgress"));
    out.put("taskCount",tasks.size());out.put("tasksWithSourceProgress",withProgress);
    out.put("recordedUpdates",updateCount==null?0:updateCount);
    // Do not manufacture percentage from 0..1 decimals, empty weights, or unapproved calendar sign.
    out.put("computedProgressPercent",null);
    out.put("calculationStatus","PENDIENTE_REGLA_INSTITUCIONAL");
    return out;
  }
  private String gen(String prefix){return prefix+"-"+UUID.randomUUID();}
  private Map<String,Object> cloneRow(Map<String,Object> source, String idField, String newId){
    Map<String,Object> copy=new LinkedHashMap<>(source);
    copy.put(idField,newId);
    copy.remove("createdAt");copy.remove("updatedAt");copy.remove("updatedByUserId");
    copy.remove("createdByUserId");
    return copy;
  }
  private JsonNode node(Map<String,Object> data){return mapper.valueToTree(data);}
  private void copyRows(String resource,String scheduleId,String newScheduleId,Map<String,String> taskMap){
    String idProperty=resource.equals("task-people")?"relationId": resource.equals("task-actors")?"relationId":
      resource.equals("task-dependencies")?"dependencyId":"milestoneId";
    List<Map<String,Object>> rows;
    if(resource.equals("task-people"))rows=repo.taskAssignments(scheduleId);
    else rows=repo.by(resource,"schedule_id",scheduleId);
    for(Map<String,Object> src:rows){
      Map<String,Object> c=cloneRow(src,idProperty,gen("R"));
      if(c.containsKey("scheduleId"))c.put("scheduleId",newScheduleId);
      for (String f:List.of("taskId","predecessorTaskId","successorTaskId"))
        if(c.get(f)!=null)c.put(f,taskMap.getOrDefault(String.valueOf(c.get(f)),String.valueOf(c.get(f))));
      repo.create(resource,node(c));
    }
  }
  public Map<String,Object> cloneSchedule(String projectId,String scheduleId,String jwtActor){
    repo.get("projects",projectId);
    Map<String,Object> original=checked(projectId,scheduleId);
    String newScheduleId=gen("CR");
    Map<String,Object> next=cloneRow(original,"scheduleId",newScheduleId);
    next.put("versionPublished",false);
    next.put("sourceSheetName",String.valueOf(original.getOrDefault("sourceSheetName",""))+" - nueva versión");
    next.put("cutoffDate",null); // Cutoff determined by authorized user; never invent it
    next.put("sourceOverallProgress",null); // New cut requires real reporting
    Map<String,Object> created=repo.create("schedules",node(next));
    List<Map<String,Object>> sourceTasks=repo.by("schedule-tasks","schedule_id",scheduleId);
    Map<String,String> taskMap=new LinkedHashMap<>();
    for(Map<String,Object> old:sourceTasks)taskMap.put(String.valueOf(old.get("taskId")),gen("T"));
    // First pass creates tasks without parents; second pass reattaches hierarchy.
    for(Map<String,Object> old:sourceTasks){
      String oldId=String.valueOf(old.get("taskId"));
      Map<String,Object> c=cloneRow(old,"taskId",taskMap.get(oldId));
      c.put("scheduleId",newScheduleId);
      c.put("parentTaskId",null);
      c.put("actualProgressPercent",null);
      repo.create("schedule-tasks",node(c));
    }
    for(Map<String,Object> old:sourceTasks){
      if(old.get("parentTaskId")!=null){
        String mappedParent=taskMap.get(String.valueOf(old.get("parentTaskId")));
        if(mappedParent!=null)repo.update("schedule-tasks",taskMap.get(String.valueOf(old.get("taskId"))),node(Map.of("parentTaskId",mappedParent)));
      }
    }
    copyRows("task-people",scheduleId,newScheduleId,taskMap);
    copyRows("task-actors",scheduleId,newScheduleId,taskMap);
    copyRows("task-dependencies",scheduleId,newScheduleId,taskMap);
    copyRows("milestones",scheduleId,newScheduleId,taskMap);
    repo.audit("schedules",newScheduleId,"CLONE_FROM:"+scheduleId,jwtActor);
    Map<String,Object> result=new LinkedHashMap<>();
    result.put("projectId",projectId);result.put("previousScheduleId",scheduleId);
    result.put("schedule",created);result.put("taskCount",sourceTasks.size());
    return result;
  }
}
