package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import domain.Balon;
import domain.ConfiguracionJugador;
import domain.ConfiguracionPartido;
import domain.EstadoPartido;
import domain.Estadisticas;
import domain.ExcepcionDOPOSoccer;
import domain.Jugador;
import domain.Lado;
import domain.Modalidad;
import domain.MotivoFin;
import domain.ParametrosJuego;
import domain.Partido;
import domain.Personaje;
import domain.PoderConfigurado;
import domain.Resolucion;
import domain.TipoPartido;

/**
 * Clase de pruebas PartidoTest.
 * Pruebas del partido: estado inicial, jugadores, balón, goles,
 * reinicio, pausa, tiempo y resultado final.
 */
public class PartidoTest
{
    private Partido partido;

    /** Constructor de la clase de pruebas PartidoTest. */
    public PartidoTest()
    {
    }

    /** Prepara el partido de prueba antes de cada test. */
    @BeforeEach
    public void setUp()
    {
        partido = crearPartido(TipoPartido.TIEMPO, 5);
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

        assertEquals(Resolucion.R1280x720.getAncho() / 4.0, jugador1.getPosicionX(), 0.0001);
        assertEquals(Resolucion.R1280x720.getAncho() * 3.0 / 4.0, jugador2.getPosicionX(), 0.0001);
        assertEquals(Resolucion.R1280x720.getSuelo() - ParametrosJuego.RADIO_JUGADOR,
                jugador1.getPosicionY(), 0.0001);
        assertEquals(Lado.IZQUIERDO, jugador1.getLado());
        assertEquals(Lado.DERECHO, jugador2.getLado());
    }

    /** El balón inicia en el centro de la cancha. */
    @Test
    public void shouldPlaceBallInCenter()
    {
        Balon balon = partido.getBalon();

        assertEquals(Resolucion.R1280x720.getAncho() / 2.0, balon.getX(), 0.0001);
        assertEquals(Resolucion.R1280x720.getSuelo() - ParametrosJuego.RADIO_BALON,
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

        assertEquals(Resolucion.R1280x720.getAncho() / 2.0,
                partido.getBalon().getX(), 0.0001);
        assertEquals(Resolucion.R1280x720.getAlto() / 2.0,
                partido.getBalon().getY(), 0.0001);
        assertEquals(0, partido.getBalon().getVelocidadX(), 0.0001);
        assertEquals(0, partido.getBalon().getVelocidadY(), 0.0001);

        assertEquals(Resolucion.R1280x720.getAncho() / 4.0,
                partido.getJugadores().get(0).getPosicionX(), 0.0001);
        assertEquals(Resolucion.R1280x720.getAncho() * 3.0 / 4.0,
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

    /** Cuando el tiempo llega a cero sin empate, el partido termina. */
    @Test
    public void shouldFinishWhenTimeRunsOut()
    {
        partido.sumarGol(1);
        partido.actualizar(60);

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

    /**
     * Un jugador que salta por encima del rival puede pasar al otro lado:
     * su bota queda por encima de la cabeza del otro y no se separan.
     */
    @Test
    public void shouldPassOverOpponentWhenJumping()
    {
        Jugador jugador1 = partido.getJugadores().get(0);
        Jugador jugador2 = partido.getJugadores().get(1);
        partido.getBalon().reiniciar(400, 400);

        // El jugador 1 queda 180 px por encima del jugador 2.
        jugador1.reiniciar(600, 500);
        jugador2.reiniciar(600, 680);

        partido.actualizar(0.001);

        assertEquals(600, jugador1.getPosicionX(), 0.0001);
        assertEquals(600, jugador2.getPosicionX(), 0.0001);
    }

    /**
     * En una partida real, con el salto base (estadística 1) el jugador
     * alcanza altura suficiente para pasar por encima del rival.
     */
    @Test
    public void shouldPassOverOpponentWithBaseJump()
    {
        Partido conSaltoBase = crearPartido(TipoPartido.TIEMPO, 5,
                new Estadisticas(1, 1, 1, 1),
                new Estadisticas(1, 1, 1, 1));
        Jugador atacante = conSaltoBase.getJugadores().get(0);
        Jugador defensor = conSaltoBase.getJugadores().get(1);
        double suelo = conSaltoBase.getCancha().getSuelo()
                - ParametrosJuego.RADIO_JUGADOR;

        conSaltoBase.getBalon().reiniciar(400, 400);
        defensor.reiniciar(640, suelo);
        atacante.reiniciar(540, suelo);
        atacante.saltar();

        double paso = ParametrosJuego.VELOCIDAD_BASE_JUGADOR * 0.016;
        for (int i = 0; i < 200
                && atacante.getPosicionX() <= defensor.getPosicionX(); i++) {
            atacante.mover(paso, conSaltoBase.getCancha());
            conSaltoBase.actualizar(0.016);
        }

        assertTrue(atacante.getPosicionX() > defensor.getPosicionX(),
                "El jugador debía pasar al otro lado saltando; x atacante: "
                        + atacante.getPosicionX() + ", x defensor: "
                        + defensor.getPosicionX());
    }

    /** A la misma altura los jugadores siguen chocando y se separan. */
    @Test
    public void shouldKeepPlayerCollisionWhenAtSameHeight()
    {
        Jugador jugador1 = partido.getJugadores().get(0);
        Jugador jugador2 = partido.getJugadores().get(1);
        partido.getBalon().reiniciar(400, 400);

        jugador1.reiniciar(600, 680);
        jugador2.reiniciar(610, 680);

        partido.actualizar(0.001);

        double separacion = Math.abs(jugador1.getPosicionX() - jugador2.getPosicionX());
        assertTrue(separacion > 60,
                "Los jugadores debían separarse; separación: " + separacion);
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

    /** Un partido por goles termina al alcanzar el objetivo. */
    @Test
    public void shouldFinishByGoals()
    {
        Partido porGoles = crearPartido(TipoPartido.GOLES, 3);

        porGoles.sumarGol(1);
        porGoles.sumarGol(1);
        porGoles.actualizar(0.016);
        assertEquals(EstadoPartido.EN_CURSO, porGoles.getEstado());

        porGoles.sumarGol(1);
        porGoles.actualizar(0.016);
        assertEquals(EstadoPartido.TERMINADO, porGoles.getEstado());
        assertEquals(MotivoFin.GOLES, porGoles.getResultado().getMotivoFin());
    }

    /** Con empate al agotarse el tiempo, se juega muerte súbita. */
    @Test
    public void shouldStartSuddenDeathWhenTied()
    {
        partido.actualizar(60);

        assertEquals(EstadoPartido.EN_CURSO, partido.getEstado());
    }

    /** El primer gol en muerte súbita termina el partido. */
    @Test
    public void shouldFinishByGoldenGoal()
    {
        partido.actualizar(60);
        partido.sumarGol(1);
        partido.actualizar(0.016);

        assertEquals(EstadoPartido.TERMINADO, partido.getEstado());
        assertEquals(MotivoFin.GOL_DE_ORO, partido.getResultado().getMotivoFin());
    }

    /**
     * El aviso de patada se consume una sola vez.
     */
    @Test
    public void shouldConsumeKickNotificationOnce()
    {
        partido.actualizar(0.016);
        assertFalse(partido.consumirPatada(1));

        Jugador jugador1 = partido.getJugadores().get(0);
        partido.getBalon().reiniciar(jugador1.getPosicionX() + 10,
                jugador1.getPosicionY());
        partido.patear(1);

        assertTrue(partido.consumirPatada(1));
        assertFalse(partido.consumirPatada(1));
        assertFalse(partido.consumirPatada(2));
    }

    /**
     * Una patada sin balón daña al rival si está al alcance.
     */
    @Test
    public void shouldDamageRivalWhenKickReachesIt()
    {
        partido.actualizar(0.016);
        Jugador jugador1 = partido.getJugadores().get(0);
        Jugador jugador2 = partido.getJugadores().get(1);
        partido.getBalon().reiniciar(400, 400);

        double suelo = partido.getCancha().getSuelo()
                - ParametrosJuego.RADIO_JUGADOR;
        jugador1.reiniciar(600, suelo);
        jugador2.reiniciar(640, suelo);
        double vidaAntes = jugador2.getVida().getActual();

        partido.patear(1);

        assertTrue(jugador2.getVida().getActual() < vidaAntes);
    }

    /**
     * Una patada sin balón no daña al rival si está lejos.
     */
    @Test
    public void shouldNotDamageRivalWhenKickDoesNotReachIt()
    {
        partido.actualizar(0.016);
        Jugador jugador1 = partido.getJugadores().get(0);
        Jugador jugador2 = partido.getJugadores().get(1);
        partido.getBalon().reiniciar(400, 400);

        double suelo = partido.getCancha().getSuelo()
                - ParametrosJuego.RADIO_JUGADOR;
        jugador1.reiniciar(600, suelo);
        jugador2.reiniciar(900, suelo);
        double vidaAntes = jugador2.getVida().getActual();

        partido.patear(1);

        assertEquals(vidaAntes, jugador2.getVida().getActual(), 0.0001);
    }

    /**
     * Un partido pausado no registra patadas.
     */
    @Test
    public void shouldNotRegisterKickWhenPaused()
    {
        partido.pausar();
        partido.patear(1);

        assertFalse(partido.consumirPatada(1));
    }

    /**
     * Un jugador aturdido no puede registrar una patada.
     */
    @Test
    public void shouldNotRegisterKickWhenStunned()
    {
        partido.actualizar(0.016);
        partido.getJugadores().get(0).aturdir();

        partido.patear(1);

        assertFalse(partido.consumirPatada(1));
    }

    /**
     * El ganador recibe cien puntos y el perdedor veinticinco.
     */
    @Test
    public void shouldAwardPointsToWinnerAndLoser()
    {
        partido.sumarGol(1);
        partido.terminar(MotivoFin.TIEMPO);

        assertEquals(ParametrosJuego.PUNTOS_GANADOR,
                partido.getPuntosJugador1());
        assertEquals(ParametrosJuego.PUNTOS_PERDEDOR,
                partido.getPuntosJugador2());
        assertEquals(ParametrosJuego.PUNTOS_GANADOR,
                partido.getJugadores().get(0).getPuntos().getTotal());
        assertEquals(ParametrosJuego.PUNTOS_PERDEDOR,
                partido.getJugadores().get(1).getPuntos().getTotal());
    }

    /**
     * El ganador del lado derecho también recibe los cien puntos.
     */
    @Test
    public void shouldAwardPointsWhenPlayerTwoWins()
    {
        partido.sumarGol(2);
        partido.terminar(MotivoFin.GOLES);

        assertEquals(ParametrosJuego.PUNTOS_PERDEDOR,
                partido.getPuntosJugador1());
        assertEquals(ParametrosJuego.PUNTOS_GANADOR,
                partido.getPuntosJugador2());
    }

    /**
     * En un empate ambos reciben los puntos del perdedor.
     */
    @Test
    public void shouldAwardLoserPointsWhenDraw()
    {
        partido.terminar(MotivoFin.TIEMPO);

        assertEquals(ParametrosJuego.PUNTOS_PERDEDOR,
                partido.getPuntosJugador1());
        assertEquals(ParametrosJuego.PUNTOS_PERDEDOR,
                partido.getPuntosJugador2());
    }

    /**
     * Crea un partido de prueba con el tipo y los goles indicados.
     */
    private Partido crearPartido(TipoPartido tipo, int goles)
    {
        return crearPartido(tipo, goles,
                new Estadisticas(3, 4, 5, 6),
                new Estadisticas(4, 3, 4, 5));
    }

    /**
     * Crea un partido de prueba con las estadísticas indicadas.
     */
    private Partido crearPartido(TipoPartido tipo, int goles,
                                 Estadisticas estadisticas1,
                                 Estadisticas estadisticas2)
    {
        try {
            ConfiguracionPartido configuracion = new ConfiguracionPartido(
                    Modalidad.PvsP,
                    tipo,
                    Resolucion.R1280x720,
                    1,
                    goles
            );

            return new Partido(
                    configuracion,
                    crearPersonaje(),
                    crearPersonaje(),
                    new ConfiguracionJugador("Ana", estadisticas1),
                    new ConfiguracionJugador("Luis", estadisticas2)
            );
        } catch (ExcepcionDOPOSoccer e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Crea un personaje de prueba con un poder real.
     */
    private Personaje crearPersonaje()
    {
        return new Personaje("Polar", "Canadá", PoderConfigurado.auraAzul());
    }

    /** Libera el escenario de pruebas después de cada test. */
    @AfterEach
    public void tearDown()
    {
    }
}
