package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import domain.Marcador;

/**
 * Clase de pruebas MarcadorTest.
 * Pruebas del marcador: estado inicial, suma de goles, empate,
 * ganador, reinicio y validación de jugadores.
 *
 * @author Juan David Rojas Heredia
 * @version 06-11-2026
 */
public class MarcadorTest
{
    private Marcador marcador;

    /**
     * Constructor de la clase de pruebas MarcadorTest.
     */
    public MarcadorTest()
    {
    }

    /**
     * Prepara el escenario de pruebas.
     * Se ejecuta antes de cada método de prueba.
     */
    @BeforeEach
    public void setUp()
    {
        marcador = new Marcador();
    }

    /** El marcador inicia 0–0. */
    @Test
    public void shouldStartAtZero()
    {
        assertEquals(0, marcador.getGoles(1));
        assertEquals(0, marcador.getGoles(2));
        assertTrue(marcador.hayEmpate());
        assertEquals(0, marcador.getGanador());
    }

    /** Un gol al jugador 1 suma solo a ese jugador. */
    @Test
    public void shouldAddGoalToPlayerOne()
    {
        marcador.sumarGol(1);

        assertEquals(1, marcador.getGoles(1));
        assertEquals(0, marcador.getGoles(2));
        assertFalse(marcador.hayEmpate());
        assertEquals(1, marcador.getGanador());
    }

    /** Un gol al jugador 2 suma solo a ese jugador. */
    @Test
    public void shouldAddGoalToPlayerTwo()
    {
        marcador.sumarGol(2);

        assertEquals(0, marcador.getGoles(1));
        assertEquals(1, marcador.getGoles(2));
        assertEquals(2, marcador.getGanador());
    }

    /** El marcador detecta el empate. */
    @Test
    public void shouldDetectTie()
    {
        marcador.sumarGol(1);
        marcador.sumarGol(2);

        assertTrue(marcador.hayEmpate());
        assertEquals(0, marcador.getGanador());
    }

    /** Reiniciar vuelve a 0–0. */
    @Test
    public void shouldResetScore()
    {
        marcador.sumarGol(1);
        marcador.sumarGol(2);
        marcador.sumarGol(2);

        marcador.reiniciar();

        assertEquals(0, marcador.getGoles(1));
        assertEquals(0, marcador.getGoles(2));
        assertTrue(marcador.hayEmpate());
    }

    /** No se puede consultar un jugador que no sea 1 o 2. */
    @Test
    public void shouldNotAcceptInvalidPlayerOnGetGoles()
    {
        try {
            marcador.getGoles(0);
            fail("Debía lanzar IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("El número de jugador debe ser 1 o 2", e.getMessage());
        }
    }

    /** No se puede sumar un gol a un jugador que no sea 1 o 2. */
    @Test
    public void shouldNotAcceptInvalidPlayerOnSumarGol()
    {
        try {
            marcador.sumarGol(3);
            fail("Debía lanzar IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("El número de jugador debe ser 1 o 2", e.getMessage());
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
