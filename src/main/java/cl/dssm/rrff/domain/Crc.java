package cl.dssm.rrff.domain;
import jakarta.persistence.*;
import java.time.LocalDate;
@Entity @Table(name="crc")
public class Crc extends BaseEntity {
  @Column(name="numero",length=60) private String numero;
  @Column(name="fecha") private LocalDate fecha;
  @Column(name="proveedor",length=255) private String proveedor;
  @Column(name="materia",columnDefinition="TEXT") private String materia;
  @Column(name="documento_factura_id",length=36) private String documentoFacturaId;
  @Column(name="documento_certificado_id",length=36) private String documentoCertificadoId;
  public String getNumero(){return numero;}
  public void setNumero(String value){this.numero=value;}
  public LocalDate getFecha(){return fecha;}
  public void setFecha(LocalDate value){this.fecha=value;}
  public String getProveedor(){return proveedor;}
  public void setProveedor(String value){this.proveedor=value;}
  public String getMateria(){return materia;}
  public void setMateria(String value){this.materia=value;}
  public String getDocumentoFacturaId(){return documentoFacturaId;}
  public void setDocumentoFacturaId(String value){this.documentoFacturaId=value;}
  public String getDocumentoCertificadoId(){return documentoCertificadoId;}
  public void setDocumentoCertificadoId(String value){this.documentoCertificadoId=value;}
}
