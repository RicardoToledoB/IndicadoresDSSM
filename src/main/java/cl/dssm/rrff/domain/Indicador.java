package cl.dssm.rrff.domain;
import jakarta.persistence.*;
import java.math.BigDecimal;
@Entity @Table(name="indicador")
public class Indicador extends BaseEntity {
  @Column(name="nombre",length=255) private String nombre;
  @Column(name="periodo",length=30) private String periodo;
  @Column(name="meta") private BigDecimal meta;
  public String getNombre(){return nombre;}
  public void setNombre(String value){this.nombre=value;}
  public String getPeriodo(){return periodo;}
  public void setPeriodo(String value){this.periodo=value;}
  public BigDecimal getMeta(){return meta;}
  public void setMeta(BigDecimal value){this.meta=value;}
}
