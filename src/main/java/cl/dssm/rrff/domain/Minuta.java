package cl.dssm.rrff.domain;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
@Entity @Table(name="minuta")
public class Minuta extends BaseEntity {
  @Column(name="proyecto_id",length=36,nullable=false) private String proyectoId;
  @Column(name="fecha_corte") private LocalDate fechaCorte;
  @Column(name="titulo",length=300) private String titulo;
  @Column(name="resumen",columnDefinition="TEXT") private String resumen;
  @Column(name="monto_informado") private BigDecimal montoInformado;
  @Column(name="version_documental",nullable=false) private Integer versionDocumental;
  public String getProyectoId(){return proyectoId;}
  public void setProyectoId(String value){this.proyectoId=value;}
  public LocalDate getFechaCorte(){return fechaCorte;}
  public void setFechaCorte(LocalDate value){this.fechaCorte=value;}
  public String getTitulo(){return titulo;}
  public void setTitulo(String value){this.titulo=value;}
  public String getResumen(){return resumen;}
  public void setResumen(String value){this.resumen=value;}
  public BigDecimal getMontoInformado(){return montoInformado;}
  public void setMontoInformado(BigDecimal value){this.montoInformado=value;}
  public Integer getVersionDocumental(){return versionDocumental;}
  public void setVersionDocumental(Integer value){this.versionDocumental=value;}
}
