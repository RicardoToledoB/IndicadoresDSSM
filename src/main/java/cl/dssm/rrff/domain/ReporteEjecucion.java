package cl.dssm.rrff.domain;
import jakarta.persistence.*;
import java.time.LocalDate;
@Entity @Table(name="reporte_ejecucion")
public class ReporteEjecucion extends BaseEntity {
  @Column(name="tipo",length=80,nullable=false) private String tipo;
  @Column(name="fecha_corte") private LocalDate fechaCorte;
  @Column(name="proyecto_id",length=36) private String proyectoId;
  @Column(name="filtros",columnDefinition="TEXT") private String filtros;
  @Column(name="referencia_fuente",length=255) private String referenciaFuente;
  @Column(name="documento_id",length=36) private String documentoId;
  @Column(name="estado",length=40) private String estado;
  @Column(name="creado_por",length=100) private String creadoPor;
  public String getTipo(){return tipo;}
  public void setTipo(String value){this.tipo=value;}
  public LocalDate getFechaCorte(){return fechaCorte;}
  public void setFechaCorte(LocalDate value){this.fechaCorte=value;}
  public String getProyectoId(){return proyectoId;}
  public void setProyectoId(String value){this.proyectoId=value;}
  public String getFiltros(){return filtros;}
  public void setFiltros(String value){this.filtros=value;}
  public String getReferenciaFuente(){return referenciaFuente;}
  public void setReferenciaFuente(String value){this.referenciaFuente=value;}
  public String getDocumentoId(){return documentoId;}
  public void setDocumentoId(String value){this.documentoId=value;}
  public String getEstado(){return estado;}
  public void setEstado(String value){this.estado=value;}
  public String getCreadoPor(){return creadoPor;}
  public void setCreadoPor(String value){this.creadoPor=value;}
}
