package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import domain.Estadisticas;

/**
 * Clase de pruebas EstadisticasTest.
 * Pruebas de las estadísticas de un jugador (speed, jump, kick, survival),
 * sus límites 1–10 y las fórmulas del PDF final.
 */
public class EstadisticasTest
{
    private Estadisticas estadisticas;

    public EstadisticasTest()
    {
    }

    @BeforeEach
    public void setUp()
    {
        estadisticas = new Estadisticas(3, 4, 5, 6);
    }

    /** Se pueden crear estadísticas con valores válidos. */
    @Test
    public void shouldCreateStatisticsWithValidValues()
    {
        assertNotNull(estadisticas);
    }

    /** Se aceptan los valores mínimos permitidos (1, 1, 1, 1). */
    @Test
    public void shouldAcceptMinimumValues()
    {
        Estadisticas e = new Estadisticas(1, 1, 1, 1);
        assertEquals(1, e.getSpeed());
        assertEquals(1, e.getJump());
        assertEquals(1, e.getKick());
        assertEquals(1, e.getSurvival());
    }

    /** Se aceptan los valores máximos permitidos (10, 10, 10, 10). */
    @Test
    public void shouldAcceptMaximumValues()
    {
        Estadisticas e = new Estadisticas(10, 10, 10, 10);
        assertEquals(10, e.getSpeed());
        assertEquals(10, e.getJump());
        assertEquals(10, e.getKick());
        assertEquals(10, e.getSurvival());
    }

    /** Los getters devuelven los mismos valores con los que se creó el objeto. */
    @Test
    public void shouldReturnRequestedValues()
    {
        assertEquals(3, estadisticas.getSpeed());
        assertEquals(4, estadisticas.getJump());
        assertEquals(5, estadisticas.getKick());
        assertEquals(6, estadisticas.getSurvival());
    }

    /** multiplicadorKick = 1 + 0.10 * (kick - 1). */
    @Test
    public void shouldComputeKickMultiplier()
    {
        assertEquals(1.0, new Estadisticas(1, 1, 1, 1).getMultiplicadorKick(), 1e-9);
        assertEquals(1.4, new Estadisticas(1, 1, 5, 1).getMultiplicadorKick(), 1e-9);
        assertEquals(1.9, new Estadisticas(1, 1, 10, 1).getMultiplicadorKick(), 1e-9);
    }

    /** vidaMax = 100 * (1 + 0.10 * (survival - 1)). */
    @Test
    public void shouldComputeMaxLife()
    {
        assertEquals(100.0, new Estadisticas(1, 1, 1, 1).getVidaMax(), 1e-9);
        assertEquals(120.0, new Estadisticas(1, 1, 1, 3).getVidaMax(), 1e-9);
        assertEquals(190.0, new Estadisticas(1, 1, 1, 10).getVidaMax(), 1e-9);
    }

    /** dañoPatada = 10 * multiplicadorKick. */
    @Test
    public void shouldComputeKickDamage()
    {
        assertEquals(10.0, new Estadisticas(1, 1, 1, 1).getDanoPatada(), 1e-9);
        assertEquals(14.0, new Estadisticas(1, 1, 5, 1).getDanoPatada(), 1e-9);
        assertEquals(19.0, new Estadisticas(1, 1, 10, 1).getDanoPatada(), 1e-9);
    }

    /** La velocidad debe estar entre 1 y 10. */
    @Test
    public void shouldNotCreateStatisticsWithInvalidSpeed()
    {
        assertThrows(IllegalArgumentException.class,
                () -> new Estadisticas(0, 3, 3, 3));
        assertThrows(IllegalArgumentException.class,
                () -> new Estadisticas(11, 3, 3, 3));
    }

    /** El salto debe estar entre 1 y 10. */
    @Test
    public void shouldNotCreateStatisticsWithInvalidJump()
    {
        assertThrows(IllegalArgumentException.class,
                () -> new Estadisticas(3, 0, 3, 3));
        assertThrows(IllegalArgumentException.class,
                () -> new Estadisticas(3, 11, 3, 3));
    }

    /** El kick debe estar entre 1 y 10. */
    @Test
    public void shouldNotCreateStatisticsWithInvalidKick()
    {
        assertThrows(IllegalArgumentException.class,
                () -> new Estadisticas(3, 3, 0, 3));
        assertThrows(IllegalArgumentException.class,
                () -> new Estadisticas(3, 3, 11, 3));
    }

    /** La supervivencia debe estar entre 1 y 10. */
    @Test
    public void shouldNotCreateStatisticsWithInvalidSurvival()
    {
        assertThrows(IllegalArgumentException.class,
                () -> new Estadisticas(3, 3, 3, 0));
        assertThrows(IllegalArgumentException.class,
                () -> new Estadisticas(3, 3, 3, 11));
    }

    @AfterEach
    public void tearDown()
    {
    }
}
