package cl.dssm.rrff;
import cl.dssm.rrff.service.Rules;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class RulesTest {
 @Test void evitaFinAntesInicio(){assertThrows(IllegalArgumentException.class,
    ()->Rules.range(LocalDate.of(2026,11,19),LocalDate.of(2026,11,18)));}
 @Test void permiteFechasNoEjecutadas(){assertDoesNotThrow(()->Rules.range(LocalDate.of(2026,11,18),null));}
 @Test void noPresuponeMoneda(){assertThrows(IllegalArgumentException.class,
    ()->Rules.amountCurrency(new BigDecimal("5000"),null));}
 @Test void permiteImporteSinSuposiciones(){assertDoesNotThrow(()->Rules.amountCurrency(new BigDecimal("-50"),"CLP"));}
}
