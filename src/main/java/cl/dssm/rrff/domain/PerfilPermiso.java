package cl.dssm.rrff.domain;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity @Table(name="perfil_permiso")
public class PerfilPermiso extends BaseEntity {
  @Column(name="actor_id",length=36,nullable=false) private String actorId;
  @Column(name="recurso",length=100,nullable=false) private String recurso;
  @Column(name="accion",length=50,nullable=false) private String accion;
  @Column(name="ambito",length=255) private String ambito;
  @Column(name="vigente_hasta") private LocalDateTime vigenteHasta;
  public String getActorId(){return actorId;}
  public void setActorId(String value){this.actorId=value;}
  public String getRecurso(){return recurso;}
  public void setRecurso(String value){this.recurso=value;}
  public String getAccion(){return accion;}
  public void setAccion(String value){this.accion=value;}
  public String getAmbito(){return ambito;}
  public void setAmbito(String value){this.ambito=value;}
  public LocalDateTime getVigenteHasta(){return vigenteHasta;}
  public void setVigenteHasta(LocalDateTime value){this.vigenteHasta=value;}
}
