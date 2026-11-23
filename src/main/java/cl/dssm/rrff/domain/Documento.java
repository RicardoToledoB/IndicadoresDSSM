package cl.dssm.rrff.domain;
import jakarta.persistence.*;
@Entity @Table(name="documento")
public class Documento extends BaseEntity {
  @Column(name="titulo",length=300,nullable=false) private String titulo;
  @Column(name="tipo",length=80) private String tipo;
  @Column(name="clasificacion",length=60) private String clasificacion;
  @Column(name="origen",length=255) private String origen;
  public String getTitulo(){return titulo;}
  public void setTitulo(String value){this.titulo=value;}
  public String getTipo(){return tipo;}
  public void setTipo(String value){this.tipo=value;}
  public String getClasificacion(){return clasificacion;}
  public void setClasificacion(String value){this.clasificacion=value;}
  public String getOrigen(){return origen;}
  public void setOrigen(String value){this.origen=value;}
}
