package cl.dssm.rrff.domain;
import jakarta.persistence.*;
import java.time.LocalDate;
@Entity @Table(name="mantencion")
public class Mantencion extends BaseEntity {
  @Column(name="bien_id",length=36) private String bienId;
  @Column(name="fecha_programada") private LocalDate fechaProgramada;
  @Column(name="actividad",length=255) private String actividad;
  public String getBienId(){return bienId;}
  public void setBienId(String value){this.bienId=value;}
  public LocalDate getFechaProgramada(){return fechaProgramada;}
  public void setFechaProgramada(LocalDate value){this.fechaProgramada=value;}
  public String getActividad(){return actividad;}
  public void setActividad(String value){this.actividad=value;}
}
