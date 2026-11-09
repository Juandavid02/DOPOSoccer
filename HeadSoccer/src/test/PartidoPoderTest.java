package test;

import static org.junit.jupiter.api.Assertions.*;

import domain.ConfiguracionJugador;
import domain.ConfiguracionPartido;
import domain.Estadisticas;
import domain.ExcepcionDOPOSoccer;
import domain.Jugador;
import domain.Modalidad;
import domain.ParametrosJuego;
import domain.Partido;
import domain.Personaje;
import domain.Poder;
import domain.PoderAuraAzul;
import domain.PoderBarril;
import domain.PoderFlechazoDeAmor;
import domain.PoderIlusion;
import domain.PoderRayo;
import domain.PoderTornado;
import domain.Resolucion;
import domain.TipoEfecto;
import domain.TipoPartido;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Pruebas de la activación de poderes, los tiros y sus efectos.
 */
public class PartidoPoderTest {

    private Partido partido;

    @BeforeEach
    public void setUp() {
        partido = crearPartido(
                new Personaje("Goku", "Japón", new PoderRayo()),
                new Personaje("Bob", "Estados Unidos", new PoderIlusion()),
                new Estadisticas(5, 5, 5, 5),
                new Estadisticas(5, 5, 5, 5));
    }

    @Test
    public void shouldNotActivatePowerWithEmptyBar() {
        assertFalse(partido.activarPoder(1));
        assertFalse(partido.getJugadores().get(0).isAuraActiva());
    }

    @Test
    public void shouldActivatePowerAndConsumeFullBar() {
        Jugador jugador = partido.getJugadores().get(0);
        jugador.getBarraPoder().sumarCarga(ParametrosJuego.CAPACIDAD_BARRA_PODER);

        assertTrue(partido.activarPoder(1));
        assertTrue(jugador.isAuraActiva());
        assertEquals(0, jugador.getBarraPoder().getCarga(), 0.0001);
    }

    @Test
    public void shouldNotActivateASecondAuraWhileOneIsActive() {
        Jugador jugador = partido.getJugadores().get(0);
        jugador.getBarraPoder().sumarCarga(ParametrosJuego.CAPACIDAD_BARRA_PODER);
        partido.activarPoder(1);
        jugador.getBarraPoder().sumarCarga(ParametrosJuego.CAPACIDAD_BARRA_PODER);

        assertFalse(partido.activarPoder(1));
    }

    @Test
    public void shouldCreatePowerShotWhenAuraTouchesBall() {
        Jugador jugador = partido.getJugadores().get(0);
        jugador.getBarraPoder().sumarCarga(ParametrosJuego.CAPACIDAD_BARRA_PODER);
        partido.activarPoder(1);

        partido.getBalon().reiniciar(
                jugador.getPosicionX() + 10,
                jugador.getPosicionY());
        partido.actualizar(0.016);

        assertFalse(jugador.isAuraActiva());
        assertNotNull(partido.getTiroPoder());
        assertTrue(partido.getTiroPoder().isActivo());
    }

    @Test
    public void shouldApplyDamageAndEffectWhenShotHitsRival() {
        Jugador atacante = partido.getJugadores().get(0);
        Jugador rival = partido.getJugadores().get(1);

        atacante.getBarraPoder().sumarCarga(ParametrosJuego.CAPACIDAD_BARRA_PODER);
        partido.activarPoder(1);
        partido.getBalon().reiniciar(
                atacante.getPosicionX() + 10,
                atacante.getPosicionY());
        partido.actualizar(0.016);

        double vidaAntes = rival.getVida().getActual();
        partido.getBalon().reiniciar(
                rival.getPosicionX(),
                rival.getPosicionY());
        partido.actualizar(0.0001);

        assertTrue(rival.getVida().getActual() < vidaAntes);
        assertTrue(rival.tieneEfecto(TipoEfecto.ELECTROCUTADO));
        assertFalse(rival.puedeSaltar());
    }

    @Test
    public void shouldAddQuarterBarToPlayerWhoConcedesGoal() {
        Jugador jugador2 = partido.getJugadores().get(1);

        partido.getBalon().reiniciar(1300, 590);
        partido.actualizar(0.016);

        assertEquals(ParametrosJuego.CAPACIDAD_BARRA_PODER * 0.25,
                jugador2.getBarraPoder().getCarga(), 1.0);
    }

    @Test
    public void shouldReplacePreviousEffectWithNewOne() {
        Jugador jugador = partido.getJugadores().get(0);

        jugador.aplicarEfecto(TipoEfecto.ELECTROCUTADO, 3);
        jugador.aplicarEfecto(TipoEfecto.CONFUNDIDO, 2.5);

        assertTrue(jugador.tieneEfecto(TipoEfecto.CONFUNDIDO));
        assertFalse(jugador.tieneEfecto(TipoEfecto.ELECTROCUTADO));
        assertEquals(2.5, jugador.getEfectoActivo().getTiempoRestante(), 0.0001);
    }

    @Test
    public void shouldInvertControlsWithFlechazoEffect() {
        Jugador jugador = partido.getJugadores().get(0);

        jugador.aplicarEfecto(TipoEfecto.CONTROLES_INVERTIDOS, 2.5);

        assertTrue(partido.tieneControlesInvertidos(1));
        assertFalse(partido.tieneControlesInvertidos(2));
    }

    @Test
    public void shouldResetAndRestoreStatsWithBarrelEffect() {
        Estadisticas estadisticas = new Estadisticas(8, 7, 6, 9);
        Personaje chavo = new Personaje("Chavo", "México", new PoderBarril());
        Partido partidoBarril = crearPartido(
                chavo,
                new Personaje("Betty", "Colombia", new PoderAuraAzul()),
                estadisticas,
                new Estadisticas(5, 5, 5, 5));
        Jugador jugador = partidoBarril.getJugadores().get(0);
        jugador.getBarraPoder().sumarCarga(100);

        jugador.aplicarEfecto(TipoEfecto.BARRIL, 5);

        assertEquals(ParametrosJuego.ESTADISTICA_INICIAL,
                jugador.getEstadisticas().getSpeed());
        assertEquals(ParametrosJuego.ESTADISTICA_INICIAL,
                jugador.getEstadisticas().getSurvival());
        assertEquals(0, jugador.getBarraPoder().getCarga(), 0.0001);

        jugador.actualizar(5.1, partidoBarril.getCancha());

        assertEquals(8, jugador.getEstadisticas().getSpeed());
        assertEquals(9, jugador.getEstadisticas().getSurvival());
    }

    @Test
    public void shouldApplyTheCorrectEffectForEveryPower() {
        Poder[] poderes = {
                new PoderAuraAzul(),
                new PoderRayo(),
                new PoderTornado(),
                new PoderIlusion(),
                new PoderFlechazoDeAmor(),
                new PoderBarril()
        };
        TipoEfecto[] efectos = {
                TipoEfecto.EMPUJE,
                TipoEfecto.ELECTROCUTADO,
                TipoEfecto.INCONSCIENTE,
                TipoEfecto.CONFUNDIDO,
                TipoEfecto.CONTROLES_INVERTIDOS,
                TipoEfecto.BARRIL
        };

        for (int i = 0; i < poderes.length; i++) {
            Partido partidoPoder = crearPartido(
                    new Personaje("Atacante", "País", poderes[i]),
                    new Personaje("Rival", "Otro país", new PoderIlusion()),
                    new Estadisticas(5, 5, 5, 5),
                    new Estadisticas(5, 5, 5, 5));
            Jugador atacante = partidoPoder.getJugadores().get(0);
            Jugador rival = partidoPoder.getJugadores().get(1);

            atacante.getBarraPoder().sumarCarga(100);
            partidoPoder.activarPoder(1);
            partidoPoder.getBalon().reiniciar(
                    atacante.getPosicionX() + 10,
                    atacante.getPosicionY());
            partidoPoder.actualizar(0.016);

            partidoPoder.getBalon().reiniciar(
                    rival.getPosicionX(),
                    rival.getPosicionY());
            partidoPoder.actualizar(0.0001);

            assertTrue(rival.tieneEfecto(efectos[i]),
                    "El poder " + poderes[i].getNombre()
                            + " debía aplicar " + efectos[i]);
        }
    }

    @Test
    public void shouldNotModifyProfileStatsWhenBarrelIsApplied() {
        Estadisticas estadisticasPerfil = new Estadisticas(8, 7, 6, 9);
        Personaje chavo = new Personaje("Chavo", "México", new PoderBarril());
        Partido partidoBarril = crearPartido(
                chavo,
                new Personaje("Betty", "Colombia", new PoderAuraAzul()),
                estadisticasPerfil,
                new Estadisticas(5, 5, 5, 5));

        partidoBarril.getJugadores().get(0)
                .aplicarEfecto(TipoEfecto.BARRIL, 5);

        assertEquals(8, estadisticasPerfil.getSpeed());
        assertEquals(9, estadisticasPerfil.getSurvival());
    }

    private Partido crearPartido(Personaje personaje1, Personaje personaje2,
                                 Estadisticas estadisticas1,
                                 Estadisticas estadisticas2) {
        try {
            ConfiguracionPartido configuracion = new ConfiguracionPartido(
                    Modalidad.PvsP,
                    TipoPartido.TIEMPO,
                    Resolucion.R1280x720,
                    5,
                    3);

            return new Partido(
                    configuracion,
                    personaje1,
                    personaje2,
                    new ConfiguracionJugador("Ana", estadisticas1),
                    new ConfiguracionJugador("Luis", estadisticas2));
        } catch (ExcepcionDOPOSoccer e) {
            throw new RuntimeException(e);
        }
    }
}
