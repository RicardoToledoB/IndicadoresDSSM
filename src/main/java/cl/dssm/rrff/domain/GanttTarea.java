package cl.dssm.rrff.domain;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
@Entity @Table(name="gantt_tarea")
public class GanttTarea extends BaseEntity {
  @Column(name="gantt_id",length=36,nullable=false) private String ganttId;
  @Column(name="denominacion",length=255,nullable=false) private String denominacion;
  @Column(name="inicio") private LocalDate inicio;
  @Column(name="fin") private LocalDate fin;
  @Column(name="avance") private BigDecimal avance;
  public String getGanttId(){return ganttId;}
  public void setGanttId(String value){this.ganttId=value;}
  public String getDenominacion(){return denominacion;}
  public void setDenominacion(String value){this.denominacion=value;}
  public LocalDate getInicio(){return inicio;}
  public void setInicio(LocalDate value){this.inicio=value;}
  public LocalDate getFin(){return fin;}
  public void setFin(LocalDate value){this.fin=value;}
  public BigDecimal getAvance(){return avance;}
  public void setAvance(BigDecimal value){this.avance=value;}
}
