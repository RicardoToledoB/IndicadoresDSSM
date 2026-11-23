package cl.dssm.rrff.domain;
import jakarta.persistence.*;
@Entity @Table(name="pac_item")
public class PacItem extends BaseEntity {
  @Column(name="area",length=100) private String area;
  @Column(name="periodo") private Integer periodo;
  @Column(name="descripcion",length=255) private String descripcion;
  public String getArea(){return area;}
  public void setArea(String value){this.area=value;}
  public Integer getPeriodo(){return periodo;}
  public void setPeriodo(Integer value){this.periodo=value;}
  public String getDescripcion(){return descripcion;}
  public void setDescripcion(String value){this.descripcion=value;}
}
