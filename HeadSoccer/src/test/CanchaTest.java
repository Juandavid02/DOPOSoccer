package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import domain.Cancha;
import domain.ParametrosJuego;
import domain.Resolucion;

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
        cancha = new Cancha(Resolucion.R1280x720);
    }

    /** La cancha informa el ancho, alto y suelo con los que fue creada. */
    @Test
    public void shouldReturnRequestedDimensions()
    {
        assertEquals(Resolucion.R1280x720.getAncho(), cancha.getAncho(), 0.0001);
        assertEquals(Resolucion.R1280x720.getAlto(), cancha.getAlto(), 0.0001);
        assertEquals(Resolucion.R1280x720.getSuelo(), cancha.getSuelo(), 0.0001);
    }

    /** El arco izquierdo queda fuera de la cancha, pegado a la línea de gol. */
    @Test
    public void shouldPlaceLeftGoalOutsideField()
    {
        assertEquals(-ParametrosJuego.ANCHO_ARCO, cancha.getArcoIzquierdo().getX(), 0.0001);
        assertEquals(Resolucion.R1280x720.getSuelo() - ParametrosJuego.ALTO_ARCO,
                cancha.getArcoIzquierdo().getY(), 0.0001);
    }

    /** El arco derecho queda fuera de la cancha, pegado a la línea de gol. */
    @Test
    public void shouldPlaceRightGoalOutsideField()
    {
        assertEquals(Resolucion.R1280x720.getAncho(), cancha.getArcoDerecho().getX(), 0.0001);
        assertEquals(Resolucion.R1280x720.getSuelo() - ParametrosJuego.ALTO_ARCO,
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

    /** La cancha conserva la resolución con la que fue creada. */
    @Test
    public void shouldKeepRequestedResolution()
    {
        assertEquals(Resolucion.R1280x720, cancha.getResolucion());
    }

    /** La cancha se adapta a la resolución 1600×900. */
    @Test
    public void shouldBuildFieldFor1600x900()
    {
        Cancha hd = new Cancha(Resolucion.R1600x900);

        assertEquals(1600, hd.getAncho(), 0.0001);
        assertEquals(900, hd.getAlto(), 0.0001);
        assertEquals(880, hd.getSuelo(), 0.0001);
        assertEquals(1600, hd.getArcoDerecho().getX(), 0.0001);
        assertEquals(880 - ParametrosJuego.ALTO_ARCO,
                hd.getArcoDerecho().getY(), 0.0001);
    }

    /** La cancha se adapta a la resolución 1920×1080. */
    @Test
    public void shouldBuildFieldFor1920x1080()
    {
        Cancha fullHd = new Cancha(Resolucion.R1920x1080);

        assertEquals(1920, fullHd.getAncho(), 0.0001);
        assertEquals(1080, fullHd.getAlto(), 0.0001);
        assertEquals(1060, fullHd.getSuelo(), 0.0001);
        assertEquals(-ParametrosJuego.ANCHO_ARCO,
                fullHd.getArcoIzquierdo().getX(), 0.0001);
        assertEquals(1060 - ParametrosJuego.ALTO_ARCO,
                fullHd.getArcoIzquierdo().getY(), 0.0001);
    }

    /** No se puede crear una cancha sin resolución. */
    @Test
    public void shouldNotCreateFieldWithNullResolution()
    {
        assertThrows(IllegalArgumentException.class, () -> new Cancha(null));
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
