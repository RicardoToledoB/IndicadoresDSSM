package cl.dssm.rrff.api;
import cl.dssm.rrff.service.AccessService;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/v1/catalogos")
public class ModuleController {
 private final AccessService access;
 public ModuleController(AccessService access){this.access=access;}
 @GetMapping("/modulos") public List<Map<String,Object>> modules(){
  return List.of(
   Map.of("numero",7,"nombre","Reportabilidad","fase",1,"definicion","DI/DT"),
   Map.of("numero",3,"nombre","Inversiones","fase",2,"definicion","DI/DT"),
   Map.of("numero",1,"nombre","Secretaría","fase",3,"definicion","DI/DT"),
   Map.of("numero",2,"nombre","Bienes Fiscales SSM","fase",4,"definicion","DT/PD"),
   Map.of("numero",4,"nombre","Plan Anual de Mantención","fase",4,"definicion","DT/PD"),
   Map.of("numero",5,"nombre","Plan Anual de Compras","fase",4,"definicion","DT/PD"),
   Map.of("numero",6,"nombre","Indicadores","fase",4,"definicion","DT/PD"));
 }
}
