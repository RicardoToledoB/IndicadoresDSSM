package cl.dssm.rrff.gantt;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;
class R72CatalogTest {
 @Test void exposesR05ResourcesAndExactColumnNames() throws Exception {
   var catalog=new R72Catalog(new ObjectMapper());
   assertEquals(25,catalog.names().size());
   assertEquals("r72_schedule_tasks",catalog.require("schedule-tasks").table());
   assertEquals("activity_o_substage",catalog.require("schedule-tasks").byJson("activityOSubstage").db());
   assertEquals("schedule_id",catalog.require("schedules").id());
 }
 @Test void rejectsUnlistedResources() throws Exception {
   var catalog=new R72Catalog(new ObjectMapper());
   assertThrows(ResponseStatusException.class,()->catalog.require("random_table"));
 }
}
