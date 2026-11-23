package cl.dssm.rrff.service;
import java.math.BigDecimal;
import java.time.LocalDate;
/** Reglas técnicas conservadoras; no representan aprobación institucional. */
public final class Rules {
 private Rules(){}
 public static void range(LocalDate start,LocalDate end){
   if(start!=null && end!=null && end.isBefore(start))throw new IllegalArgumentException("La fecha fin no puede ser anterior a inicio");
 }
 public static void year(Integer value){
   if(value!=null && (value<1900 || value>2200))throw new IllegalArgumentException("Año fuera de rango");
 }
 public static void amountCurrency(BigDecimal amount,String currency){
   if(amount!=null && (currency==null || currency.isBlank()))throw new IllegalArgumentException("Si informa monto, indique moneda; no se asume CLP");
 }
}
