package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import domain.MotivoFin;
import domain.ResultadoPartido;

/**
 * Clase de pruebas ResultadoPartidoTest.
 * Pruebas del resultado final: goles, motivo, tiempo, empate y ganador.
 *
 * @author Juan David Rojas Heredia
 * @version 06-11-2026
 */
public class ResultadoPartidoTest
{
    private ResultadoPartido resultado;

    /**
     * Constructor de la clase de pruebas ResultadoPartidoTest.
     */
    public ResultadoPartidoTest()
    {
    }

    /**
     * Prepara el escenario de pruebas.
     * Se ejecuta antes de cada método de prueba.
     */
    @BeforeEach
    public void setUp()
    {
        resultado = new ResultadoPartido(2, 1, MotivoFin.TIEMPO, 60);
    }

    /** El resultado conserva los datos con los que fue creado. */
    @Test
    public void shouldReturnRequestedValues()
    {
        assertEquals(2, resultado.getGolesJugador1());
        assertEquals(1, resultado.getGolesJugador2());
        assertEquals(MotivoFin.TIEMPO, resultado.getMotivoFin());
        assertEquals(60, resultado.getTiempoTotal());
    }

    /** El resultado detecta al ganador. */
    @Test
    public void shouldDetectWinner()
    {
        assertTrue(resultado.hayGanador());
        assertFalse(resultado.hayEmpate());
        assertEquals(1, resultado.getGanador());
    }

    /** El resultado detecta al jugador 2 como ganador. */
    @Test
    public void shouldDetectPlayerTwoAsWinner()
    {
        ResultadoPartido r = new ResultadoPartido(0, 3, MotivoFin.GOLES, 45);

        assertTrue(r.hayGanador());
        assertEquals(2, r.getGanador());
    }

    /** El resultado detecta el empate. */
    @Test
    public void shouldDetectTie()
    {
        ResultadoPartido r = new ResultadoPartido(1, 1, MotivoFin.TIEMPO, 60);

        assertTrue(r.hayEmpate());
        assertFalse(r.hayGanador());
        assertEquals(0, r.getGanador());
    }

    /** No se puede crear un resultado con goles negativos. */
    @Test
    public void shouldNotCreateWithNegativeGoals()
    {
        try {
            new ResultadoPartido(-1, 0, MotivoFin.TIEMPO, 60);
            fail("Debía lanzar IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Los goles no pueden ser negativos", e.getMessage());
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
