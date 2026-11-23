package cl.dssm.rrff.domain;
import jakarta.persistence.*;
@Entity @Table(name="proyecto")
public class Proyecto extends BaseEntity {
  @Column(name="codigo_bip",length=40) private String codigoBip;
  @Column(name="nombre",length=300,nullable=false) private String nombre;
  @Column(name="comuna",length=120) private String comuna;
  @Column(name="etapa",length=80) private String etapa;
  @Column(name="prioridad",length=80) private String prioridad;
  @Column(name="fuente_referencia",length=255) private String fuenteReferencia;
  @Column(name="periodo_fuente") private Integer periodoFuente;
  public String getCodigoBip(){return codigoBip;}
  public void setCodigoBip(String value){this.codigoBip=value;}
  public String getNombre(){return nombre;}
  public void setNombre(String value){this.nombre=value;}
  public String getComuna(){return comuna;}
  public void setComuna(String value){this.comuna=value;}
  public String getEtapa(){return etapa;}
  public void setEtapa(String value){this.etapa=value;}
  public String getPrioridad(){return prioridad;}
  public void setPrioridad(String value){this.prioridad=value;}
  public String getFuenteReferencia(){return fuenteReferencia;}
  public void setFuenteReferencia(String value){this.fuenteReferencia=value;}
  public Integer getPeriodoFuente(){return periodoFuente;}
  public void setPeriodoFuente(Integer value){this.periodoFuente=value;}
}
