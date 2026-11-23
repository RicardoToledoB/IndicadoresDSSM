package cl.dssm.rrff.domain;
import jakarta.persistence.*;
@Entity @Table(name="convenio_proyecto")
public class ConvenioProyecto extends BaseEntity {
  @Column(name="convenio_id",length=36,nullable=false) private String convenioId;
  @Column(name="proyecto_id",length=36,nullable=false) private String proyectoId;
  @Column(name="tipo_vinculo",length=80) private String tipoVinculo;
  public String getConvenioId(){return convenioId;}
  public void setConvenioId(String value){this.convenioId=value;}
  public String getProyectoId(){return proyectoId;}
  public void setProyectoId(String value){this.proyectoId=value;}
  public String getTipoVinculo(){return tipoVinculo;}
  public void setTipoVinculo(String value){this.tipoVinculo=value;}
}
