package cl.dssm.rrff.domain;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
@Entity @Table(name="correspondencia")
public class Correspondencia extends BaseEntity {
  @Column(name="anio") private Integer anio;
  @Column(name="fecha_recepcion") private LocalDate fechaRecepcion;
  @Column(name="hora_documento") private LocalTime horaDocumento;
  @Column(name="fecha_documento") private LocalDate fechaDocumento;
  @Column(name="origen",length=255) private String origen;
  @Column(name="tipo_documento",length=60) private String tipoDocumento;
  @Column(name="numero_documento",length=60) private String numeroDocumento;
  @Column(name="materia",columnDefinition="TEXT") private String materia;
  @Column(name="responsable",length=200) private String responsable;
  public Integer getAnio(){return anio;}
  public void setAnio(Integer value){this.anio=value;}
  public LocalDate getFechaRecepcion(){return fechaRecepcion;}
  public void setFechaRecepcion(LocalDate value){this.fechaRecepcion=value;}
  public LocalTime getHoraDocumento(){return horaDocumento;}
  public void setHoraDocumento(LocalTime value){this.horaDocumento=value;}
  public LocalDate getFechaDocumento(){return fechaDocumento;}
  public void setFechaDocumento(LocalDate value){this.fechaDocumento=value;}
  public String getOrigen(){return origen;}
  public void setOrigen(String value){this.origen=value;}
  public String getTipoDocumento(){return tipoDocumento;}
  public void setTipoDocumento(String value){this.tipoDocumento=value;}
  public String getNumeroDocumento(){return numeroDocumento;}
  public void setNumeroDocumento(String value){this.numeroDocumento=value;}
  public String getMateria(){return materia;}
  public void setMateria(String value){this.materia=value;}
  public String getResponsable(){return responsable;}
  public void setResponsable(String value){this.responsable=value;}
}
