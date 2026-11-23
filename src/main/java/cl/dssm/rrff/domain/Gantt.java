package cl.dssm.rrff.domain;
import jakarta.persistence.*;
import java.time.LocalDate;
@Entity @Table(name="gantt")
public class Gantt extends BaseEntity {
  @Column(name="proyecto_id",length=36,nullable=false) private String proyectoId;
  @Column(name="nombre",length=255,nullable=false) private String nombre;
  @Column(name="version_documental",nullable=false) private Integer versionDocumental;
  @Column(name="fecha_corte") private LocalDate fechaCorte;
  public String getProyectoId(){return proyectoId;}
  public void setProyectoId(String value){this.proyectoId=value;}
  public String getNombre(){return nombre;}
  public void setNombre(String value){this.nombre=value;}
  public Integer getVersionDocumental(){return versionDocumental;}
  public void setVersionDocumental(Integer value){this.versionDocumental=value;}
  public LocalDate getFechaCorte(){return fechaCorte;}
  public void setFechaCorte(LocalDate value){this.fechaCorte=value;}
}
