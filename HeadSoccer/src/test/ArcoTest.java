package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import domain.Arco;
import domain.Balon;
import domain.ParametrosJuego;
import domain.Resolucion;

/**
 * Clase de pruebas ArcoTest.
 * Pruebas de la geometría de un arco: posición, tamaño, boca y
 * detección de que el balón entró completamente.
 */
public class ArcoTest
{
    private Arco arco;

    /**
     * Constructor de la clase de pruebas ArcoTest.
     */
    public ArcoTest()
    {
    }

    /**
     * Prepara el escenario de pruebas.
     * Se ejecuta antes de cada método de prueba.
     */
    @BeforeEach
    public void setUp()
    {
        arco = new Arco(-ParametrosJuego.ANCHO_ARCO,
                Resolucion.R1280x720.getSuelo() - ParametrosJuego.ALTO_ARCO);
    }

    /** El arco informa la posición y el tamaño con los que fue creado. */
    @Test
    public void shouldReturnRequestedPositionAndSize()
    {
        assertEquals(-ParametrosJuego.ANCHO_ARCO, arco.getX(), 0.0001);
        assertEquals(Resolucion.R1280x720.getSuelo() - ParametrosJuego.ALTO_ARCO,
                arco.getY(), 0.0001);
        assertEquals(ParametrosJuego.ANCHO_ARCO, arco.getAncho(), 0.0001);
        assertEquals(ParametrosJuego.ALTO_ARCO, arco.getAlto(), 0.0001);
    }

    /** Un balón completamente dentro de la red cuenta como gol. */
    @Test
    public void shouldDetectBallCompletelyInside()
    {
        Balon balon = new Balon(-12, 590, ParametrosJuego.RADIO_BALON);
        assertTrue(arco.estaDentro(balon));
    }

    /** Un balón que solo toca la red no cuenta como gol. */
    @Test
    public void shouldNotDetectBallPartiallyInside()
    {
        Balon balon = new Balon(-5, 590, ParametrosJuego.RADIO_BALON);
        assertFalse(arco.estaDentro(balon));
    }

    /** Un balón que está en el campo, fuera de la red, no cuenta como gol. */
    @Test
    public void shouldNotDetectBallOutside()
    {
        Balon balon = new Balon(60, 590, ParametrosJuego.RADIO_BALON);
        assertFalse(arco.estaDentro(balon));
    }

    /** Un balón por encima del travesaño no cuenta como gol. */
    @Test
    public void shouldNotDetectBallAboveGoal()
    {
        Balon balon = new Balon(-12, 470, ParametrosJuego.RADIO_BALON);
        assertFalse(arco.estaDentro(balon));
    }

    /** La boca del arco incluye la altura del travesaño. */
    @Test
    public void shouldDetectGoalMouthAtCrossbar()
    {
        assertTrue(arco.estaEnBoca(480, ParametrosJuego.RADIO_BALON));
    }

    /** Un balón por encima del travesaño no está en la boca del arco. */
    @Test
    public void shouldNotDetectGoalMouthAboveCrossbar()
    {
        assertFalse(arco.estaEnBoca(460, ParametrosJuego.RADIO_BALON));
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
