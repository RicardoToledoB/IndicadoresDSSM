package cl.dssm.rrff.domain;
import jakarta.persistence.*;
import java.time.LocalDate;
@Entity @Table(name="hito")
public class Hito extends BaseEntity {
  @Column(name="proyecto_id",length=36,nullable=false) private String proyectoId;
  @Column(name="denominacion",length=255) private String denominacion;
  @Column(name="inicio") private LocalDate inicio;
  @Column(name="fin") private LocalDate fin;
  @Column(name="estado",length=60) private String estado;
  public String getProyectoId(){return proyectoId;}
  public void setProyectoId(String value){this.proyectoId=value;}
  public String getDenominacion(){return denominacion;}
  public void setDenominacion(String value){this.denominacion=value;}
  public LocalDate getInicio(){return inicio;}
  public void setInicio(LocalDate value){this.inicio=value;}
  public LocalDate getFin(){return fin;}
  public void setFin(LocalDate value){this.fin=value;}
  public String getEstado(){return estado;}
  public void setEstado(String value){this.estado=value;}
}
