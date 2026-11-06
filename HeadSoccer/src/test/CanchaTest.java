package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import domain.Cancha;
import domain.ParametrosJuego;

/**
 * Clase de pruebas CanchaTest.
 * Pruebas de la geometría de la cancha: dimensiones, posición de los
 * arcos y detección de la boca de cada arco.
 *
 * @author Juan David Rojas Heredia
 * @version 05-11-2026
 */
public class CanchaTest
{
    private Cancha cancha;

    /**
     * Constructor de la clase de pruebas CanchaTest.
     */
    public CanchaTest()
    {
    }

    /**
     * Prepara el escenario de pruebas.
     * Se ejecuta antes de cada método de prueba.
     */
    @BeforeEach
    public void setUp()
    {
        cancha = new Cancha();
    }

    /** La cancha informa el ancho, alto y suelo con los que fue creada. */
    @Test
    public void shouldReturnRequestedDimensions()
    {
        assertEquals(ParametrosJuego.ANCHO_CANCHA, cancha.getAncho(), 0.0001);
        assertEquals(ParametrosJuego.ALTO_CANCHA, cancha.getAlto(), 0.0001);
        assertEquals(ParametrosJuego.SUELO_Y, cancha.getSuelo(), 0.0001);
    }

    /** El arco izquierdo queda fuera de la cancha, pegado a la línea de gol. */
    @Test
    public void shouldPlaceLeftGoalOutsideField()
    {
        assertEquals(-ParametrosJuego.ANCHO_ARCO, cancha.getArcoIzquierdo().getX(), 0.0001);
        assertEquals(ParametrosJuego.SUELO_Y - ParametrosJuego.ALTO_ARCO,
                cancha.getArcoIzquierdo().getY(), 0.0001);
    }

    /** El arco derecho queda fuera de la cancha, pegado a la línea de gol. */
    @Test
    public void shouldPlaceRightGoalOutsideField()
    {
        assertEquals(ParametrosJuego.ANCHO_CANCHA, cancha.getArcoDerecho().getX(), 0.0001);
        assertEquals(ParametrosJuego.SUELO_Y - ParametrosJuego.ALTO_ARCO,
                cancha.getArcoDerecho().getY(), 0.0001);
    }

    /** La boca del arco izquierdo está a la altura de la red. */
    @Test
    public void shouldDetectLeftGoalMouth()
    {
        assertTrue(cancha.estaEnBocaArcoIzquierdo(590, ParametrosJuego.RADIO_BALON));
    }

    /** Un balón por encima del travesaño no está en la boca del arco izquierdo. */
    @Test
    public void shouldNotDetectLeftGoalMouthAboveCrossbar()
    {
        assertFalse(cancha.estaEnBocaArcoIzquierdo(460, ParametrosJuego.RADIO_BALON));
    }

    /** La boca del arco derecho está a la altura de la red. */
    @Test
    public void shouldDetectRightGoalMouth()
    {
        assertTrue(cancha.estaEnBocaArcoDerecho(590, ParametrosJuego.RADIO_BALON));
    }

    /** Un balón por encima del travesaño no está en la boca del arco derecho. */
    @Test
    public void shouldNotDetectRightGoalMouthAboveCrossbar()
    {
        assertFalse(cancha.estaEnBocaArcoDerecho(460, ParametrosJuego.RADIO_BALON));
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
