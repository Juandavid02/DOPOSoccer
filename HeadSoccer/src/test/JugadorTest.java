package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import domain.Balon;
import domain.BarraPoder;
import domain.Cancha;
import domain.ConfiguracionJugador;
import domain.Estadisticas;
import domain.Jugador;
import domain.Lado;
import domain.ParametrosJuego;
import domain.Personaje;
import domain.PoderAuraAzul;
import domain.Resolucion;

/**
 * Clase de pruebas JugadorTest.
 * Pruebas del jugador: posición, movimiento, salto, gravedad, patada y uso
 * de sus propias estadísticas (speed, jump, kick, survival).
 *
 * @author Juan David Rojas Heredia
 * @version 06-11-2026
 */
public class JugadorTest
{
    private Cancha cancha;
    private Jugador jugador;

    public JugadorTest()
    {
    }

    @BeforeEach
    public void setUp()
    {
        cancha = new Cancha(Resolucion.R1280x720);
        jugador = crearJugador("Juan", 3, 4, 5, 6, Lado.IZQUIERDO, 100,
                Resolucion.R1280x720.getSuelo() - ParametrosJuego.RADIO_JUGADOR);
    }

    /** El jugador inicia en la posición indicada y sin velocidad. */
    @Test
    public void shouldStartAtRequestedPosition()
    {
        assertEquals(100, jugador.getPosicionX(), 0.0001);
        assertEquals(Resolucion.R1280x720.getSuelo() - ParametrosJuego.RADIO_JUGADOR,
                jugador.getPosicionY(), 0.0001);
        assertEquals(0, jugador.getVelocidadX(), 0.0001);
        assertEquals(0, jugador.getVelocidadY(), 0.0001);
        assertTrue(jugador.isEnSuelo());
    }

    /** El jugador se mueve a la derecha. */
    @Test
    public void shouldMoveRight()
    {
        jugador.mover(50, cancha);
        assertEquals(150, jugador.getPosicionX(), 0.0001);
    }

    /** El jugador se mueve a la izquierda. */
    @Test
    public void shouldMoveLeft()
    {
        jugador.mover(-50, cancha);
        assertEquals(50, jugador.getPosicionX(), 0.0001);
    }

    /** El jugador no puede salir por la izquierda. */
    @Test
    public void shouldNotMoveOutsideLeft()
    {
        jugador.mover(-1000, cancha);
        assertEquals(ParametrosJuego.RADIO_JUGADOR, jugador.getPosicionX(), 0.0001);
    }

    /** El jugador no puede salir por la derecha. */
    @Test
    public void shouldNotMoveOutsideRight()
    {
        jugador.mover(2000, cancha);
        assertEquals(Resolucion.R1280x720.getAncho() - ParametrosJuego.RADIO_JUGADOR,
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

        assertEquals(Resolucion.R1280x720.getSuelo() - ParametrosJuego.RADIO_JUGADOR,
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

    /** Un jugador con más jump alcanza mayor velocidad vertical. */
    @Test
    public void shouldUsePlayerJumpStat()
    {
        Jugador saltadorBajo = crearJugador("Bajo", 3, 1, 3, 1,
                Lado.IZQUIERDO, 100, 680);
        Jugador saltadorAlto = crearJugador("Alto", 3, 5, 3, 1,
                Lado.IZQUIERDO, 100, 680);

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

    /** El kick no cambia la fuerza con la que sale el balón (PDF final). */
    @Test
    public void shouldNotChangeBallPowerWithKickStat()
    {
        Jugador kickBajo = crearJugador("Bajo", 3, 3, 1, 1,
                Lado.IZQUIERDO, 100, 680);
        Jugador kickAlto = crearJugador("Alto", 3, 3, 10, 1,
                Lado.IZQUIERDO, 100, 680);

        Balon balonKickBajo = new Balon(110, 680, ParametrosJuego.RADIO_BALON);
        Balon balonKickAlto = new Balon(110, 680, ParametrosJuego.RADIO_BALON);

        kickBajo.patear(balonKickBajo);
        kickAlto.patear(balonKickAlto);

        assertEquals(balonKickBajo.getVelocidadX(), balonKickAlto.getVelocidadX(), 0.0001);
    }

    /** El jugador expone sus estadísticas propias y sus puntos. */
    @Test
    public void shouldExposePlayerStatisticsAndPoints()
    {
        assertEquals(3, jugador.getEstadisticas().getSpeed());
        assertEquals(4, jugador.getEstadisticas().getJump());
        assertEquals(5, jugador.getEstadisticas().getKick());
        assertEquals(6, jugador.getEstadisticas().getSurvival());
        assertEquals(0, jugador.getPuntos().getTotal());
    }

    /** La vida inicia llena y coincide con la vida máxima de sus estadísticas. */
    @Test
    public void shouldStartWithLifeAtMaximum()
    {
        double vidaMaxima = jugador.getEstadisticas().getVidaMax();

        assertEquals(vidaMaxima, jugador.getVida().getMaxima(), 0.0001);
        assertEquals(vidaMaxima, jugador.getVida().getActual(), 0.0001);
        assertFalse(jugador.getVida().estaEnCero());
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
     * Crea un jugador de prueba con sus propias estadísticas.
     */
    private Jugador crearJugador(String nombre, int speed, int jump, int kick, int survival,
                                 Lado lado, double x, double y)
    {
        ConfiguracionJugador configuracion = new ConfiguracionJugador(
                nombre, new Estadisticas(speed, jump, kick, survival));

        return new Jugador(
                configuracion,
                crearPersonaje(),
                new BarraPoder(ParametrosJuego.CAPACIDAD_BARRA_PODER,
                        ParametrosJuego.RECARGA_BARRA_PODER),
                lado,
                x,
                y
        );
    }

    /**
     * Crea un personaje de prueba con un poder real.
     */
    private Personaje crearPersonaje()
    {
        return new Personaje("Polar", "Canadá", new PoderAuraAzul());
    }

    @AfterEach
    public void tearDown()
    {
    }
}
