package cl.dssm.rrff.domain;
import jakarta.persistence.*;
@Entity @Table(name="documento_version")
public class DocumentoVersion extends BaseEntity {
  @Column(name="documento_id",length=36,nullable=false) private String documentoId;
  @Column(name="numero",nullable=false) private Integer numero;
  @Column(name="referencia_archivo",length=500,nullable=false) private String referenciaArchivo;
  @Column(name="hash_integridad",length=128) private String hashIntegridad;
  public String getDocumentoId(){return documentoId;}
  public void setDocumentoId(String value){this.documentoId=value;}
  public Integer getNumero(){return numero;}
  public void setNumero(Integer value){this.numero=value;}
  public String getReferenciaArchivo(){return referenciaArchivo;}
  public void setReferenciaArchivo(String value){this.referenciaArchivo=value;}
  public String getHashIntegridad(){return hashIntegridad;}
  public void setHashIntegridad(String value){this.hashIntegridad=value;}
}
