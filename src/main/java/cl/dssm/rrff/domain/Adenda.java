package cl.dssm.rrff.domain;
import jakarta.persistence.*;
import java.time.LocalDate;
@Entity @Table(name="adenda")
public class Adenda extends BaseEntity {
  @Column(name="convenio_id",length=36,nullable=false) private String convenioId;
  @Column(name="fecha_instrumento") private LocalDate fechaInstrumento;
  @Column(name="estatus_formal",length=40) private String estatusFormal;
  @Column(name="documento_id",length=36) private String documentoId;
  public String getConvenioId(){return convenioId;}
  public void setConvenioId(String value){this.convenioId=value;}
  public LocalDate getFechaInstrumento(){return fechaInstrumento;}
  public void setFechaInstrumento(LocalDate value){this.fechaInstrumento=value;}
  public String getEstatusFormal(){return estatusFormal;}
  public void setEstatusFormal(String value){this.estatusFormal=value;}
  public String getDocumentoId(){return documentoId;}
  public void setDocumentoId(String value){this.documentoId=value;}
}
