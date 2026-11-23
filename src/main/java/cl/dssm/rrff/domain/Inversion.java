package cl.dssm.rrff.domain;
import jakarta.persistence.*;
import java.math.BigDecimal;
@Entity @Table(name="inversion")
public class Inversion extends BaseEntity {
  @Column(name="proyecto_id",length=36,nullable=false) private String proyectoId;
  @Column(name="periodo") private Integer periodo;
  @Column(name="fuente",length=150) private String fuente;
  @Column(name="moneda",length=10) private String moneda;
  @Column(name="monto") private BigDecimal monto;
  public String getProyectoId(){return proyectoId;}
  public void setProyectoId(String value){this.proyectoId=value;}
  public Integer getPeriodo(){return periodo;}
  public void setPeriodo(Integer value){this.periodo=value;}
  public String getFuente(){return fuente;}
  public void setFuente(String value){this.fuente=value;}
  public String getMoneda(){return moneda;}
  public void setMoneda(String value){this.moneda=value;}
  public BigDecimal getMonto(){return monto;}
  public void setMonto(BigDecimal value){this.monto=value;}
}
