package cl.dssm.rrff.domain;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity @Table(name="auditoria_evento")
public class AuditoriaEvento extends BaseEntity {
  @Column(name="actor_id",length=36) private String actorId;
  @Column(name="recurso_id",length=120,nullable=false) private String recursoId;
  @Column(name="accion",length=60,nullable=false) private String accion;
  @Column(name="fecha_hora",nullable=false) private LocalDateTime fechaHora;
  @Column(name="resultado",length=40,nullable=false) private String resultado;
  @Column(name="detalle",length=500) private String detalle;
  public String getActorId(){return actorId;}
  public void setActorId(String value){this.actorId=value;}
  public String getRecursoId(){return recursoId;}
  public void setRecursoId(String value){this.recursoId=value;}
  public String getAccion(){return accion;}
  public void setAccion(String value){this.accion=value;}
  public LocalDateTime getFechaHora(){return fechaHora;}
  public void setFechaHora(LocalDateTime value){this.fechaHora=value;}
  public String getResultado(){return resultado;}
  public void setResultado(String value){this.resultado=value;}
  public String getDetalle(){return detalle;}
  public void setDetalle(String value){this.detalle=value;}
}
