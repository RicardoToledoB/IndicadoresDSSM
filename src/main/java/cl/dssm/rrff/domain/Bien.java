package cl.dssm.rrff.domain;
import jakarta.persistence.*;
@Entity @Table(name="bien")
public class Bien extends BaseEntity {
  @Column(name="tipo",length=80) private String tipo;
  @Column(name="ubicacion",length=255) private String ubicacion;
  @Column(name="identificador_institucional",length=100) private String identificadorInstitucional;
  public String getTipo(){return tipo;}
  public void setTipo(String value){this.tipo=value;}
  public String getUbicacion(){return ubicacion;}
  public void setUbicacion(String value){this.ubicacion=value;}
  public String getIdentificadorInstitucional(){return identificadorInstitucional;}
  public void setIdentificadorInstitucional(String value){this.identificadorInstitucional=value;}
}
