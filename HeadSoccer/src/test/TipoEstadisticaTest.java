package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import domain.Estadisticas;
import domain.TipoEstadistica;
import org.junit.jupiter.api.Test;

/**
 * Pruebas de las estadísticas mejorables.
 */
class TipoEstadisticaTest {

    /** Verifica que cada estadística tenga su etiqueta legible. */
    @Test
    void cadaEstadisticaTieneSuEtiqueta() {
        assertEquals("Velocidad", TipoEstadistica.VELOCIDAD.getEtiqueta());
        assertEquals("Salto", TipoEstadistica.SALTO.getEtiqueta());
        assertEquals("Patada", TipoEstadistica.PATADA.getEtiqueta());
        assertEquals("Supervivencia",
                TipoEstadistica.SUPERVIVENCIA.getEtiqueta());
    }

    /** Verifica que cada estadística lea el campo correcto. */
    @Test
    void cadaEstadisticaDevuelveSuValor() {
        Estadisticas estadisticas = new Estadisticas(3, 4, 5, 6);

        assertEquals(3, TipoEstadistica.VELOCIDAD.getValor(estadisticas));
        assertEquals(4, TipoEstadistica.SALTO.getValor(estadisticas));
        assertEquals(5, TipoEstadistica.PATADA.getValor(estadisticas));
        assertEquals(6, TipoEstadistica.SUPERVIVENCIA.getValor(estadisticas));
    }

    /** Verifica que existan exactamente las cuatro estadísticas esperadas. */
    @Test
    void existenExactamenteCuatroEstadisticas() {
        assertEquals(4, TipoEstadistica.values().length);
    }
}
