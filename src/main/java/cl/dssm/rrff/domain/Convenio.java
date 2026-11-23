package cl.dssm.rrff.domain;
import jakarta.persistence.*;
@Entity @Table(name="convenio")
public class Convenio extends BaseEntity {
  @Column(name="titulo",length=300,nullable=false) private String titulo;
  @Column(name="inicio_periodo") private Integer inicioPeriodo;
  @Column(name="fin_periodo") private Integer finPeriodo;
  @Column(name="estatus_formal",length=40) private String estatusFormal;
  @Column(name="referencia_documento",length=255) private String referenciaDocumento;
  public String getTitulo(){return titulo;}
  public void setTitulo(String value){this.titulo=value;}
  public Integer getInicioPeriodo(){return inicioPeriodo;}
  public void setInicioPeriodo(Integer value){this.inicioPeriodo=value;}
  public Integer getFinPeriodo(){return finPeriodo;}
  public void setFinPeriodo(Integer value){this.finPeriodo=value;}
  public String getEstatusFormal(){return estatusFormal;}
  public void setEstatusFormal(String value){this.estatusFormal=value;}
  public String getReferenciaDocumento(){return referenciaDocumento;}
  public void setReferenciaDocumento(String value){this.referenciaDocumento=value;}
}
