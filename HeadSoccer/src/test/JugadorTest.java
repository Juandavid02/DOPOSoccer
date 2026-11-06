package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import domain.Balon;
import domain.BarraPoder;
import domain.Cancha;
import domain.Estadisticas;
import domain.Jugador;
import domain.Lado;
import domain.ParametrosJuego;
import domain.Personaje;
import domain.Poder;

/**
 * Clase de pruebas JugadorTest.
 * Pruebas del jugador: posición, movimiento, salto, gravedad,
 * patada y uso de las estadísticas del personaje.
 *
 * @author Juan David Rojas Heredia
 * @version 06-11-2026
 */
public class JugadorTest
{
    private Cancha cancha;
    private Jugador jugador;

    /**
     * Constructor de la clase de pruebas JugadorTest.
     */
    public JugadorTest()
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
        jugador = new Jugador(
                "Juan",
                crearPersonaje(3, 4, 5),
                new BarraPoder(ParametrosJuego.CAPACIDAD_BARRA_PODER,
                        ParametrosJuego.RECARGA_BARRA_PODER),
                Lado.IZQUIERDO,
                100,
                ParametrosJuego.SUELO_Y - ParametrosJuego.RADIO_JUGADOR
        );
    }

    /** El jugador inicia en la posición indicada y sin velocidad. */
    @Test
    public void shouldStartAtRequestedPosition()
    {
        assertEquals(100, jugador.getPosicionX(), 0.0001);
        assertEquals(ParametrosJuego.SUELO_Y - ParametrosJuego.RADIO_JUGADOR,
                jugador.getPosicionY(), 0.0001);
        assertEquals(0, jugador.getVelocidadX(), 0.0001);
        assertEquals(0, jugador.getVelocidadY(), 0.0001);
        assertTrue(jugador.isEnSuelo());
    }

    /** El jugador se mueve a la derecha. */
    @Test
    public void shouldMoveRight()
    {
        jugador.mover(50);
        assertEquals(150, jugador.getPosicionX(), 0.0001);
    }

    /** El jugador se mueve a la izquierda. */
    @Test
    public void shouldMoveLeft()
    {
        jugador.mover(-50);
        assertEquals(50, jugador.getPosicionX(), 0.0001);
    }

    /** El jugador no puede salir por la izquierda. */
    @Test
    public void shouldNotMoveOutsideLeft()
    {
        jugador.mover(-1000);
        assertEquals(ParametrosJuego.RADIO_JUGADOR, jugador.getPosicionX(), 0.0001);
    }

    /** El jugador no puede salir por la derecha. */
    @Test
    public void shouldNotMoveOutsideRight()
    {
        jugador.mover(2000);
        assertEquals(ParametrosJuego.ANCHO_CANCHA - ParametrosJuego.RADIO_JUGADOR,
                jugador.getPosicionX(), 0.0001);
    }

    /** El jugador salta si está en el suelo. */
    @Test
    public void shouldJumpWhenOnGround()
    {
        jugador.saltar();
        assertTrue(jugador.getVelocidadY() < 0);
        assertFalse(jugador.isEnSuelo());
    }

    /** El jugador no puede saltar en el aire. */
    @Test
    public void shouldNotJumpInAir()
    {
        jugador.saltar();
        double velocidadSalto = jugador.getVelocidadY();
        jugador.saltar();
        assertEquals(velocidadSalto, jugador.getVelocidadY(), 0.0001);
    }

    /** La gravedad hace caer al jugador. */
    @Test
    public void shouldFallByGravity()
    {
        jugador.reiniciar(100, 600);
        jugador.actualizar(0.1, cancha);

        assertTrue(jugador.getPosicionY() > 600);
        assertTrue(jugador.getVelocidadY() > 0);
    }

    /** El jugador vuelve al suelo después de caer. */
    @Test
    public void shouldReturnToGround()
    {
        jugador.reiniciar(100, 600);

        for (int i = 0; i < 200; i++) {
            jugador.actualizar(0.05, cancha);
        }

        assertEquals(ParametrosJuego.SUELO_Y - ParametrosJuego.RADIO_JUGADOR,
                jugador.getPosicionY(), 0.0001);
        assertEquals(0, jugador.getVelocidadY(), 0.0001);
        assertTrue(jugador.isEnSuelo());
    }

    /** La barra de poder inicia vacía. */
    @Test
    public void shouldStartWithEmptyPowerBar()
    {
        assertEquals(0, jugador.getBarraPoder().getCarga(), 0.0001);
    }

    /** Un personaje con más salto alcanza mayor velocidad vertical. */
    @Test
    public void shouldUseCharacterJumpStat()
    {
        Jugador saltadorBajo = new Jugador(
                "Bajo", crearPersonaje(3, 1, 3),
                new BarraPoder(100, 10), Lado.IZQUIERDO, 100, 680);
        Jugador saltadorAlto = new Jugador(
                "Alto", crearPersonaje(3, 5, 3),
                new BarraPoder(100, 10), Lado.IZQUIERDO, 100, 680);

        saltadorBajo.saltar();
        saltadorAlto.saltar();

        assertTrue(Math.abs(saltadorAlto.getVelocidadY())
                > Math.abs(saltadorBajo.getVelocidadY()));
    }

    /** El jugador patea el balón si está dentro del alcance. */
    @Test
    public void shouldKickBallWhenClose()
    {
        Balon balon = new Balon(110, 680, ParametrosJuego.RADIO_BALON);

        jugador.patear(balon);

        assertTrue(balon.getVelocidadX() > 0);
    }

    /** El jugador no patea el balón si está lejos. */
    @Test
    public void shouldNotKickBallWhenFar()
    {
        Balon balon = new Balon(500, 680, ParametrosJuego.RADIO_BALON);

        jugador.patear(balon);

        assertEquals(0, balon.getVelocidadX(), 0.0001);
        assertEquals(0, balon.getVelocidadY(), 0.0001);
    }

    /** Un personaje con más fuerza patea más fuerte. */
    @Test
    public void shouldUseCharacterStrengthStat()
    {
        Jugador debil = new Jugador(
                "Debil", crearPersonaje(3, 3, 1),
                new BarraPoder(100, 10), Lado.IZQUIERDO, 100, 680);
        Jugador fuerte = new Jugador(
                "Fuerte", crearPersonaje(3, 3, 5),
                new BarraPoder(100, 10), Lado.IZQUIERDO, 100, 680);

        Balon balonDebil = new Balon(110, 680, ParametrosJuego.RADIO_BALON);
        Balon balonFuerte = new Balon(110, 680, ParametrosJuego.RADIO_BALON);

        debil.patear(balonDebil);
        fuerte.patear(balonFuerte);

        assertTrue(balonFuerte.getVelocidadX() > balonDebil.getVelocidadX());
    }

    /** Reiniciar devuelve al jugador a la posición indicada. */
    @Test
    public void shouldResetToRequestedPosition()
    {
        jugador.setVelocidadX(100);
        jugador.setVelocidadY(-200);

        jugador.reiniciar(200, 300);

        assertEquals(200, jugador.getPosicionX(), 0.0001);
        assertEquals(300, jugador.getPosicionY(), 0.0001);
        assertEquals(0, jugador.getVelocidadX(), 0.0001);
        assertEquals(0, jugador.getVelocidadY(), 0.0001);
        assertTrue(jugador.isEnSuelo());
    }

    /**
     * Crea un personaje de prueba con las estadísticas indicadas.
     */
    private Personaje crearPersonaje(int velocidad, int salto, int fuerza)
    {
        return new Personaje(
                "Polar", "Canadá",
                new Estadisticas(velocidad, salto, fuerza),
                new PoderFake()
        );
    }

    /**
     * Poder falso para las pruebas.
     */
    private static class PoderFake implements Poder
    {
        @Override
        public String getNombre()
        {
            return "Prueba";
        }

        @Override
        public double getDuracion()
        {
            return 1.0;
        }

        @Override
        public void aplicarPoder(Jugador jugador)
        {
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
