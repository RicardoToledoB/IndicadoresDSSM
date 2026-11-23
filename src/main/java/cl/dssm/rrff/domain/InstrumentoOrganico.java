package cl.dssm.rrff.domain;
import jakarta.persistence.*;
import java.time.LocalDate;
@Entity @Table(name="instrumento_organico")
public class InstrumentoOrganico extends BaseEntity {
  @Column(name="documento_id",length=36) private String documentoId;
  @Column(name="tipo_acto",length=80) private String tipoActo;
  @Column(name="numero",length=80) private String numero;
  @Column(name="anio") private Integer anio;
  @Column(name="fecha") private LocalDate fecha;
  @Column(name="emisor",length=255) private String emisor;
  @Column(name="vigencia",length=60) private String vigencia;
  public String getDocumentoId(){return documentoId;}
  public void setDocumentoId(String value){this.documentoId=value;}
  public String getTipoActo(){return tipoActo;}
  public void setTipoActo(String value){this.tipoActo=value;}
  public String getNumero(){return numero;}
  public void setNumero(String value){this.numero=value;}
  public Integer getAnio(){return anio;}
  public void setAnio(Integer value){this.anio=value;}
  public LocalDate getFecha(){return fecha;}
  public void setFecha(LocalDate value){this.fecha=value;}
  public String getEmisor(){return emisor;}
  public void setEmisor(String value){this.emisor=value;}
  public String getVigencia(){return vigencia;}
  public void setVigencia(String value){this.vigencia=value;}
}
