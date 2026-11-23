package cl.dssm.rrff.domain;
import jakarta.persistence.*;
import java.time.LocalDate;
@Entity @Table(name="correlativo")
public class Correlativo extends BaseEntity {
  @Column(name="tipo",length=40,nullable=false) private String tipo;
  @Column(name="serie",length=80) private String serie;
  @Column(name="unidad",length=120) private String unidad;
  @Column(name="anio") private Integer anio;
  @Column(name="numero",length=60) private String numero;
  @Column(name="responsable",length=200) private String responsable;
  @Column(name="fecha") private LocalDate fecha;
  @Column(name="materia",columnDefinition="TEXT") private String materia;
  @Column(name="destino",length=255) private String destino;
  public String getTipo(){return tipo;}
  public void setTipo(String value){this.tipo=value;}
  public String getSerie(){return serie;}
  public void setSerie(String value){this.serie=value;}
  public String getUnidad(){return unidad;}
  public void setUnidad(String value){this.unidad=value;}
  public Integer getAnio(){return anio;}
  public void setAnio(Integer value){this.anio=value;}
  public String getNumero(){return numero;}
  public void setNumero(String value){this.numero=value;}
  public String getResponsable(){return responsable;}
  public void setResponsable(String value){this.responsable=value;}
  public LocalDate getFecha(){return fecha;}
  public void setFecha(LocalDate value){this.fecha=value;}
  public String getMateria(){return materia;}
  public void setMateria(String value){this.materia=value;}
  public String getDestino(){return destino;}
  public void setDestino(String value){this.destino=value;}
}
