package cl.dssm.rrff.domain;
import jakarta.persistence.*;
import java.time.LocalDate;
@Entity @Table(name="derivacion")
public class Derivacion extends BaseEntity {
  @Column(name="correspondencia_id",length=36,nullable=false) private String correspondenciaId;
  @Column(name="destino",length=255) private String destino;
  @Column(name="fecha") private LocalDate fecha;
  @Column(name="observacion",columnDefinition="TEXT") private String observacion;
  public String getCorrespondenciaId(){return correspondenciaId;}
  public void setCorrespondenciaId(String value){this.correspondenciaId=value;}
  public String getDestino(){return destino;}
  public void setDestino(String value){this.destino=value;}
  public LocalDate getFecha(){return fecha;}
  public void setFecha(LocalDate value){this.fecha=value;}
  public String getObservacion(){return observacion;}
  public void setObservacion(String value){this.observacion=value;}
}
