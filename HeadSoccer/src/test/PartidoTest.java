package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import domain.Balon;
import domain.ConfiguracionJugador;
import domain.EstadoPartido;
import domain.Estadisticas;
import domain.Jugador;
import domain.Lado;
import domain.Modalidad;
import domain.MotivoFin;
import domain.ParametrosJuego;
import domain.Partido;
import domain.Personaje;
import domain.PoderAuraAzul;
import domain.TipoPartido;

/**
 * Clase de pruebas PartidoTest.
 * Pruebas del partido: estado inicial, jugadores, balón, goles,
 * reinicio, pausa, tiempo y resultado final.
 *
 * @author Juan David Rojas Heredia
 * @version 06-11-2026
 */
public class PartidoTest
{
    private Partido partido;

    public PartidoTest()
    {
    }

    @BeforeEach
    public void setUp()
    {
        partido = new Partido(
                Modalidad.PvsP,
                TipoPartido.TIEMPO,
                crearPersonaje(),
                crearPersonaje(),
                new ConfiguracionJugador("Ana", new Estadisticas(3, 4, 5, 6)),
                new ConfiguracionJugador("Luis", new Estadisticas(4, 3, 4, 5))
        );
    }

    /** El partido inicia 0–0. */
    @Test
    public void shouldStartWithZeroScore()
    {
        assertEquals(0, partido.getMarcador().getGoles(1));
        assertEquals(0, partido.getMarcador().getGoles(2));
        assertTrue(partido.getMarcador().hayEmpate());
    }

    /** Las barras de poder inician vacías. */
    @Test
    public void shouldStartWithEmptyPowerBars()
    {
        assertEquals(0, partido.getJugadores().get(0).getBarraPoder().getCarga(), 0.0001);
        assertEquals(0, partido.getJugadores().get(1).getBarraPoder().getCarga(), 0.0001);
    }

    /** Cada jugador queda en su lado de la cancha. */
    @Test
    public void shouldPlacePlayersOnTheirSides()
    {
        Jugador jugador1 = partido.getJugadores().get(0);
        Jugador jugador2 = partido.getJugadores().get(1);

        assertEquals(ParametrosJuego.ANCHO_CANCHA / 4.0, jugador1.getPosicionX(), 0.0001);
        assertEquals(ParametrosJuego.ANCHO_CANCHA * 3.0 / 4.0, jugador2.getPosicionX(), 0.0001);
        assertEquals(ParametrosJuego.SUELO_Y - ParametrosJuego.RADIO_JUGADOR,
                jugador1.getPosicionY(), 0.0001);
        assertEquals(Lado.IZQUIERDO, jugador1.getLado());
        assertEquals(Lado.DERECHO, jugador2.getLado());
    }

    /** El balón inicia en el centro de la cancha. */
    @Test
    public void shouldPlaceBallInCenter()
    {
        Balon balon = partido.getBalon();

        assertEquals(ParametrosJuego.ANCHO_CANCHA / 2.0, balon.getX(), 0.0001);
        assertEquals(ParametrosJuego.SUELO_Y - ParametrosJuego.RADIO_BALON,
                balon.getY(), 0.0001);
    }

    /** Los nombres de los jugadores son los de la configuración. */
    @Test
    public void shouldKeepPlayerNames()
    {
        assertEquals("Ana", partido.getJugadores().get(0).getNombre());
        assertEquals("Luis", partido.getJugadores().get(1).getNombre());
    }

    /** Un gol en el arco derecho suma al jugador 1. */
    @Test
    public void shouldScoreGoalInRightGoalForPlayerOne()
    {
        partido.getBalon().reiniciar(1300, 590);
        partido.actualizar(0.016);

        assertEquals(1, partido.getMarcador().getGoles(1));
        assertEquals(0, partido.getMarcador().getGoles(2));
    }

    /** Un gol en el arco izquierdo suma al jugador 2. */
    @Test
    public void shouldScoreGoalInLeftGoalForPlayerTwo()
    {
        partido.getBalon().reiniciar(-60, 590);
        partido.actualizar(0.016);

        assertEquals(0, partido.getMarcador().getGoles(1));
        assertEquals(1, partido.getMarcador().getGoles(2));
    }

    /** Después de un gol, el balón vuelve al centro y los jugadores a su lado. */
    @Test
    public void shouldResetAfterGoal()
    {
        partido.getBalon().reiniciar(1300, 590);
        partido.actualizar(0.016);

        assertEquals(ParametrosJuego.ANCHO_CANCHA / 2.0,
                partido.getBalon().getX(), 0.0001);
        assertEquals(ParametrosJuego.ALTO_CANCHA / 2.0,
                partido.getBalon().getY(), 0.0001);
        assertEquals(0, partido.getBalon().getVelocidadX(), 0.0001);
        assertEquals(0, partido.getBalon().getVelocidadY(), 0.0001);

        assertEquals(ParametrosJuego.ANCHO_CANCHA / 4.0,
                partido.getJugadores().get(0).getPosicionX(), 0.0001);
        assertEquals(ParametrosJuego.ANCHO_CANCHA * 3.0 / 4.0,
                partido.getJugadores().get(1).getPosicionX(), 0.0001);
    }

    /** Pausar y reanudar cambian el estado del partido. */
    @Test
    public void shouldPauseAndResume()
    {
        partido.pausar();
        assertEquals(EstadoPartido.PAUSADO, partido.getEstado());

        partido.reanudar();
        assertEquals(EstadoPartido.EN_CURSO, partido.getEstado());
    }

    /** Mientras está pausado, el tiempo y el balón no cambian. */
    @Test
    public void shouldNotUpdateWhenPaused()
    {
        partido.pausar();
        double tiempo = partido.getTiempoRestante();
        double balonX = partido.getBalon().getX();
        double balonY = partido.getBalon().getY();

        partido.actualizar(1);

        assertEquals(tiempo, partido.getTiempoRestante(), 0.0001);
        assertEquals(balonX, partido.getBalon().getX(), 0.0001);
        assertEquals(balonY, partido.getBalon().getY(), 0.0001);
        assertEquals(EstadoPartido.PAUSADO, partido.getEstado());
    }

    /** Mientras está pausado, los jugadores no se mueven. */
    @Test
    public void shouldNotMoveWhenPaused()
    {
        partido.pausar();
        double posicionInicial = partido.getJugadores().get(0).getPosicionX();

        partido.mover(1, 100);

        assertEquals(posicionInicial,
                partido.getJugadores().get(0).getPosicionX(), 0.0001);
    }

    /** El tiempo disminuye cuando el partido está en curso. */
    @Test
    public void shouldAdvanceTime()
    {
        partido.actualizar(1);

        assertTrue(partido.getTiempoRestante() < ParametrosJuego.TIEMPO_PARTIDO);
        assertEquals(EstadoPartido.EN_CURSO, partido.getEstado());
    }

    /** Cuando el tiempo llega a cero, el partido termina. */
    @Test
    public void shouldFinishWhenTimeRunsOut()
    {
        partido.actualizar(61);

        assertEquals(EstadoPartido.TERMINADO, partido.getEstado());
        assertNotNull(partido.getResultado());
        assertEquals(MotivoFin.TIEMPO, partido.getResultado().getMotivoFin());
        assertEquals(ParametrosJuego.TIEMPO_PARTIDO,
                partido.getResultado().getTiempoTotal());
    }

    /** El jugador 1 se mueve cuando el partido está en curso. */
    @Test
    public void shouldMovePlayerWhenRunning()
    {
        partido.actualizar(0.016);
        double posicionInicial = partido.getJugadores().get(0).getPosicionX();

        partido.mover(1, 50);

        assertEquals(posicionInicial + 50,
                partido.getJugadores().get(0).getPosicionX(), 0.0001);
    }

    /** El jugador 1 patea el balón si está cerca. */
    @Test
    public void shouldKickBallWhenClose()
    {
        partido.actualizar(0.016);
        Jugador jugador1 = partido.getJugadores().get(0);
        partido.getBalon().reiniciar(jugador1.getPosicionX() + 10,
                jugador1.getPosicionY());

        partido.patear(1);

        assertTrue(partido.getBalon().getVelocidadX() > 0);
    }

    /** No se puede usar un número de jugador distinto de 1 o 2. */
    @Test
    public void shouldNotAcceptInvalidPlayer()
    {
        try {
            partido.mover(3, 10);
            fail("Debía lanzar IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("El número de jugador debe ser 1 o 2", e.getMessage());
        }
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
