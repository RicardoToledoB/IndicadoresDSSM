package cl.dssm.rrff.gantt;

import java.time.Instant;
import java.util.Map;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/** Avoid exposing raw SQL when referential or unique constraints reject a request. */
@RestControllerAdvice(assignableTypes={R72Controller.class})
public class R72Exceptions {
  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<Map<String,Object>> integrity(DataIntegrityViolationException ex){
    return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
      "status",409,"error","Conflicto de integridad relacional o valor duplicado",
      "time",Instant.now().toString()));
  }
}
