package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import domain.Estadisticas;

/**
 * Clase de pruebas EstadisticasTest.
 * Pruebas de las estadísticas de un personaje: valores válidos,
 * límites permitidos y validaciones del constructor.
 *
 * @author Juan David Rojas Heredia
 * @version 05-11-2026
 */
public class EstadisticasTest
{
    private Estadisticas estadisticas;

    /**
     * Constructor de la clase de pruebas EstadisticasTest.
     */
    public EstadisticasTest()
    {
    }

    /**
     * Prepara el escenario de pruebas.
     * Se ejecuta antes de cada método de prueba.
     */
    @BeforeEach
    public void setUp()
    {
        estadisticas = new Estadisticas(3, 4, 5);
    }

    /** Se pueden crear estadísticas con valores válidos. */
    @Test
    public void shouldCreateStatisticsWithValidValues()
    {
        assertNotNull(estadisticas);
    }

    /** Se aceptan los valores mínimos permitidos (1, 1, 1). */
    @Test
    public void shouldAcceptMinimumValues()
    {
        Estadisticas e = new Estadisticas(1, 1, 1);
        assertEquals(1, e.getVelocidad());
        assertEquals(1, e.getSalto());
        assertEquals(1, e.getFuerza());
    }

    /** Se aceptan los valores máximos permitidos (5, 5, 5). */
    @Test
    public void shouldAcceptMaximumValues()
    {
        Estadisticas e = new Estadisticas(5, 5, 5);
        assertEquals(5, e.getVelocidad());
        assertEquals(5, e.getSalto());
        assertEquals(5, e.getFuerza());
    }

    /** Los getters devuelven los mismos valores con los que se creó el objeto. */
    @Test
    public void shouldReturnRequestedValues()
    {
        assertEquals(3, estadisticas.getVelocidad());
        assertEquals(4, estadisticas.getSalto());
        assertEquals(5, estadisticas.getFuerza());
    }

    /** La velocidad debe estar entre 1 y 5. */
    @Test
    public void shouldNotCreateStatisticsWithInvalidSpeed()
    {
        try {
            new Estadisticas(0, 3, 3);
            fail("Debía lanzar IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("La velocidad debe estar entre 1 y 5", e.getMessage());
        }

        try {
            new Estadisticas(6, 3, 3);
            fail("Debía lanzar IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("La velocidad debe estar entre 1 y 5", e.getMessage());
        }
    }

    /** El salto debe estar entre 1 y 5. */
    @Test
    public void shouldNotCreateStatisticsWithInvalidJump()
    {
        try {
            new Estadisticas(3, 0, 3);
            fail("Debía lanzar IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("El salto debe estar entre 1 y 5", e.getMessage());
        }

        try {
            new Estadisticas(3, 6, 3);
            fail("Debía lanzar IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("El salto debe estar entre 1 y 5", e.getMessage());
        }
    }

    /** La fuerza debe estar entre 1 y 5. */
    @Test
    public void shouldNotCreateStatisticsWithInvalidStrength()
    {
        try {
            new Estadisticas(3, 3, 0);
            fail("Debía lanzar IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("La fuerza debe estar entre 1 y 5", e.getMessage());
        }

        try {
            new Estadisticas(3, 3, 6);
            fail("Debía lanzar IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("La fuerza debe estar entre 1 y 5", e.getMessage());
        }
    }

    /**
     * Libera el escenario de pruebas.
     * Se ejecuta después de cada método de prueba.
     */
    @AfterEach
    public void tearDown()
    {
    }
}
