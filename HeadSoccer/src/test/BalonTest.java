package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import domain.Balon;
import domain.Cancha;
import domain.ParametrosJuego;
import domain.Resolucion;

/**
 * Clase de pruebas BalonTest.
 * Pruebas de la física del balón: estado inicial, gravedad, fricción,
 * rebotes en suelo, techo y paredes, y reinicio de posición.
 *
 * @author Juan David Rojas Heredia
 * @version 05-11-2026
 */
public class BalonTest
{
    private Cancha cancha;
    private Balon balon;

    /**
     * Constructor de la clase de pruebas BalonTest.
     */
    public BalonTest()
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
        balon = new Balon(640, 300, ParametrosJuego.RADIO_BALON);
    }

    /** El balón inicia sin velocidad. */
    @Test
    public void shouldStartWithZeroVelocity()
    {
        assertEquals(0, balon.getVelocidadX(), 0.0001);
        assertEquals(0, balon.getVelocidadY(), 0.0001);
    }

    /** Un impulso cambia las velocidades del balón. */
    @Test
    public void shouldApplyImpulse()
    {
        balon.aplicarImpulso(100, -50);
        assertEquals(100, balon.getVelocidadX(), 0.0001);
        assertEquals(-50, balon.getVelocidadY(), 0.0001);
    }

    /** La gravedad hace que el balón caiga. */
    @Test
    public void shouldFallByGravity()
    {
        double yInicial = balon.getY();
        balon.actualizar(0.1, cancha);

        assertTrue(balon.getY() > yInicial);
        assertTrue(balon.getVelocidadY() > 0);
    }

    /** La velocidad vertical aumenta con el tiempo por la gravedad. */
    @Test
    public void shouldIncreaseVerticalSpeedOverTime()
    {
        balon.actualizar(0.1, cancha);
        double primeraVelocidad = balon.getVelocidadY();
        balon.actualizar(0.1, cancha);

        assertTrue(balon.getVelocidadY() > primeraVelocidad);
    }

    /** La fricción reduce la velocidad horizontal. */
    @Test
    public void shouldReduceHorizontalSpeedByFriction()
    {
        balon.aplicarImpulso(100, 0);
        balon.actualizar(0.1, cancha);

        assertTrue(balon.getVelocidadX() < 100);
        assertTrue(balon.getVelocidadX() > 0);
    }

    /** Al tocar el suelo, el balón rebota hacia arriba. */
    @Test
    public void shouldBounceOnFloor()
    {
        Balon b = new Balon(640, 690, ParametrosJuego.RADIO_BALON);
        b.actualizar(0.02, cancha);

        assertTrue(b.getVelocidadY() < 0);
        assertEquals(cancha.getSuelo() - b.getRadio(), b.getY(), 0.0001);
    }

    /** El balón no queda por debajo del suelo. */
    @Test
    public void shouldNotExceedFloor()
    {
        Balon b = new Balon(640, 690, ParametrosJuego.RADIO_BALON);
        b.actualizar(0.02, cancha);

        assertTrue(b.getY() + b.getRadio() <= cancha.getSuelo());
    }

    /** Después de rebotar en el suelo, el balón pierde energía. */
    @Test
    public void shouldLoseEnergyAfterBounce()
    {
        Balon b = new Balon(640, 690, ParametrosJuego.RADIO_BALON);
        b.aplicarImpulso(0, 100);
        b.actualizar(0.01, cancha);

        assertTrue(Math.abs(b.getVelocidadY()) < 112);
    }

    /** Al tocar el techo, el balón rebota hacia abajo. */
    @Test
    public void shouldBounceOnCeiling()
    {
        Balon b = new Balon(640, 12, ParametrosJuego.RADIO_BALON);
        b.aplicarImpulso(0, -100);
        b.actualizar(0.01, cancha);

        assertTrue(b.getVelocidadY() > 0);
        assertEquals(b.getRadio(), b.getY(), 0.0001);
    }

    /** Al tocar la pared izquierda, el balón rebota hacia la derecha. */
    @Test
    public void shouldBounceOnLeftWall()
    {
        Balon b = new Balon(13, 300, ParametrosJuego.RADIO_BALON);
        b.aplicarImpulso(-100, 0);
        b.actualizar(0.02, cancha);

        assertEquals(b.getRadio(), b.getX(), 0.0001);
        assertTrue(b.getVelocidadX() > 0);
    }

    /** Al tocar la pared derecha, el balón rebota hacia la izquierda. */
    @Test
    public void shouldBounceOnRightWall()
    {
        Balon b = new Balon(1267, 300, ParametrosJuego.RADIO_BALON);
        b.aplicarImpulso(100, 0);
        b.actualizar(0.02, cancha);

        assertEquals(cancha.getAncho() - b.getRadio(), b.getX(), 0.0001);
        assertTrue(b.getVelocidadX() < 0);
    }

    /** En la boca del arco izquierdo no hay rebote de pared. */
    @Test
    public void shouldNotBounceOnLeftGoalMouth()
    {
        Balon b = new Balon(13, 590, ParametrosJuego.RADIO_BALON);
        b.aplicarImpulso(-100, 0);
        b.actualizar(0.02, cancha);

        assertTrue(b.getX() < b.getRadio());
        assertTrue(b.getVelocidadX() < 0);
    }

    /** En la boca del arco derecho no hay rebote de pared. */
    @Test
    public void shouldNotBounceOnRightGoalMouth()
    {
        Balon b = new Balon(1267, 590, ParametrosJuego.RADIO_BALON);
        b.aplicarImpulso(100, 0);
        b.actualizar(0.02, cancha);

        assertTrue(b.getX() > cancha.getAncho() - b.getRadio());
        assertTrue(b.getVelocidadX() > 0);
    }

    /** Reiniciar devuelve el balón a la posición indicada y sin velocidad. */
    @Test
    public void shouldResetToInitialPosition()
    {
        balon.aplicarImpulso(100, -100);
        balon.actualizar(0.1, cancha);

        balon.reiniciar(640, 300);

        assertEquals(640, balon.getX(), 0.0001);
        assertEquals(300, balon.getY(), 0.0001);
        assertEquals(0, balon.getVelocidadX(), 0.0001);
        assertEquals(0, balon.getVelocidadY(), 0.0001);
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
