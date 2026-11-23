package cl.dssm.rrff.api;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
@RestControllerAdvice
public class ApiErrors {
  private ProblemDetail problem(HttpStatus status,String title,String detail){
    ProblemDetail d=ProblemDetail.forStatusAndDetail(status,detail);d.setTitle(title);return d;
  }
  @ExceptionHandler(MethodArgumentNotValidException.class) public ProblemDetail validation(MethodArgumentNotValidException ex){
    return problem(HttpStatus.BAD_REQUEST,"Validación","Campos inválidos: revisar datos obligatorios");
  }
  @ExceptionHandler(DataIntegrityViolationException.class) public ProblemDetail conflict(DataIntegrityViolationException ex){
    return problem(HttpStatus.CONFLICT,"Conflicto de integridad","Registro duplicado o relación inválida");
  }
  @ExceptionHandler(ObjectOptimisticLockingFailureException.class) public ProblemDetail concurrency(ObjectOptimisticLockingFailureException ex){
    return problem(HttpStatus.CONFLICT,"Versión desactualizada","Consultar el registro nuevamente");
  }
  @ExceptionHandler(IllegalArgumentException.class) public ProblemDetail input(IllegalArgumentException ex){
    return problem(HttpStatus.BAD_REQUEST,"Datos inválidos",ex.getMessage());
  }
}
