package test;

import static org.junit.jupiter.api.Assertions.*;

import domain.ConfiguracionJugador;
import domain.ConfiguracionPartido;
import domain.Efecto;
import domain.EfectoBarril;
import domain.EfectoConfundido;
import domain.EfectoControlesInvertidos;
import domain.EfectoElectrocutado;
import domain.EfectoEmpuje;
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
import domain.PoderBesoDeLaFea;
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

    /** Prepara el partido de prueba antes de cada test. */
    @BeforeEach
    public void setUp() {
        partido = crearPartido(
                new Personaje("Goku", "Japón", new PoderRayo()),
                new Personaje("Bob", "Estados Unidos", new PoderIlusion()),
                new Estadisticas(5, 5, 5, 5),
                new Estadisticas(5, 5, 5, 5));
    }

    /** Verifica que no se activa el poder con la barra vacía. */
    @Test
    public void shouldNotActivatePowerWithEmptyBar() {
        assertFalse(partido.activarPoder(1));
        assertFalse(partido.getJugadores().get(0).isAuraActiva());
    }

    /** Verifica que el poder se activa y consume la barra completa. */
    @Test
    public void shouldActivatePowerAndConsumeFullBar() {
        Jugador jugador = partido.getJugadores().get(0);
        jugador.getBarraPoder().sumarCarga(ParametrosJuego.CAPACIDAD_BARRA_PODER);

        assertTrue(partido.activarPoder(1));
        assertTrue(jugador.isAuraActiva());
        assertEquals(0, jugador.getBarraPoder().getCarga(), 0.0001);
    }

    /** Verifica que no se activa otra aura mientras una está activa. */
    @Test
    public void shouldNotActivateASecondAuraWhileOneIsActive() {
        Jugador jugador = partido.getJugadores().get(0);
        jugador.getBarraPoder().sumarCarga(ParametrosJuego.CAPACIDAD_BARRA_PODER);
        partido.activarPoder(1);
        jugador.getBarraPoder().sumarCarga(ParametrosJuego.CAPACIDAD_BARRA_PODER);

        assertFalse(partido.activarPoder(1));
    }

    /** Verifica que el aura al tocar el balón crea un tiro de poder. */
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

    /** Verifica que el tiro de poder daña y aplica el efecto al rival. */
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
        assertTrue(tieneEfecto(rival, TipoEfecto.ELECTROCUTADO));
        assertFalse(rival.puedeSaltar());
    }

    /** Verifica que quien recibe un gol gana un cuarto de barra de poder. */
    @Test
    public void shouldAddQuarterBarToPlayerWhoConcedesGoal() {
        Jugador jugador2 = partido.getJugadores().get(1);

        partido.getBalon().reiniciar(1300, 590);
        partido.actualizar(0.016);

        assertEquals(ParametrosJuego.CAPACIDAD_BARRA_PODER * 0.25,
                jugador2.getBarraPoder().getCarga(), 1.0);
    }

    /** Verifica que se pueden tener varios efectos activos a la vez. */
    @Test
    public void shouldKeepMultipleEffectsAtTheSameTime() {
        Jugador jugador = partido.getJugadores().get(0);

        jugador.aplicarEfecto(new EfectoElectrocutado(3));
        jugador.aplicarEfecto(new EfectoConfundido(2.5));

        assertTrue(tieneEfecto(jugador, TipoEfecto.ELECTROCUTADO));
        assertTrue(tieneEfecto(jugador, TipoEfecto.CONFUNDIDO));
        assertEquals(2, jugador.getEfectosActivos().size());
    }

    /** Verifica que cada efecto expira de forma independiente. */
    @Test
    public void shouldExpireEachEffectIndependently() {
        Jugador jugador = partido.getJugadores().get(0);

        jugador.aplicarEfecto(new EfectoElectrocutado(1));
        jugador.aplicarEfecto(new EfectoConfundido(5));

        jugador.actualizar(1.1, partido.getCancha());

        assertFalse(tieneEfecto(jugador, TipoEfecto.ELECTROCUTADO));
        assertTrue(tieneEfecto(jugador, TipoEfecto.CONFUNDIDO));
        assertEquals(1, jugador.getEfectosActivos().size());
    }

    /** Verifica que al anotar un gol se eliminan todos los efectos. */
    @Test
    public void shouldClearEveryEffectWhenGoalIsScored() {
        Jugador jugador = partido.getJugadores().get(0);

        jugador.aplicarEfecto(new EfectoElectrocutado(3));
        jugador.aplicarEfecto(new EfectoConfundido(2.5));

        partido.reiniciarDespuesDeGol();

        assertFalse(tieneEfecto(jugador, TipoEfecto.ELECTROCUTADO));
        assertFalse(tieneEfecto(jugador, TipoEfecto.CONFUNDIDO));
        assertTrue(jugador.getEfectosActivos().isEmpty());
    }

    /** Verifica que un efecto repetido renueva su duración. */
    @Test
    public void shouldRenewDurationWhenTheSameEffectIsAppliedAgain() {
        Jugador jugador = partido.getJugadores().get(0);

        jugador.aplicarEfecto(new EfectoElectrocutado(1));
        jugador.actualizar(0.6, partido.getCancha());
        jugador.aplicarEfecto(new EfectoElectrocutado(3));

        assertEquals(1, jugador.getEfectosActivos().size());
        assertEquals(3, jugador.getEfectosActivos().get(0).getTiempoRestante(),
                0.0001);
    }

    /**
     * Verifica que al reaplicar un efecto del mismo tipo se ejecuta otra vez
     * su comportamiento inicial, no solo se renueva el tiempo.
     */
    @Test
    public void shouldReapplyEffectBehaviorWhenTheSameTypeIsRenewed() {
        Jugador jugador = partido.getJugadores().get(0);

        jugador.aplicarEfecto(new EfectoEmpuje(1));
        jugador.setVelocidadX(0);
        jugador.aplicarEfecto(new EfectoEmpuje(1));

        assertEquals(ParametrosJuego.IMPULSO_EMPUJE,
                Math.abs(jugador.getVelocidadX()), 0.0001);
    }

    /** Verifica que no se puede aplicar un efecto nulo. */
    @Test
    public void shouldRejectNullEffect() {
        Jugador jugador = partido.getJugadores().get(0);

        assertThrows(IllegalArgumentException.class,
                () -> jugador.aplicarEfecto(null));
    }

    /** Verifica que renovar un efecto con duración no positiva falla. */
    @Test
    public void shouldRejectNonPositiveRenewal() {
        Efecto efecto = new EfectoElectrocutado(3);

        assertThrows(IllegalArgumentException.class, () -> efecto.renovar(0));
    }

    /** Verifica que el efecto del beso de la fea invierte los controles. */
    @Test
    public void shouldInvertControlsWithBesoDeLaFeaEffect() {
        Jugador jugador = partido.getJugadores().get(0);

        jugador.aplicarEfecto(new EfectoControlesInvertidos(2.5));

        assertTrue(partido.tieneControlesInvertidos(1));
        assertFalse(partido.tieneControlesInvertidos(2));
    }

    /** Verifica que el efecto barril reinicia y luego restaura las estadísticas. */
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

        jugador.aplicarEfecto(new EfectoBarril(5));

        assertEquals(ParametrosJuego.ESTADISTICA_INICIAL,
                jugador.getEstadisticas().getSpeed());
        assertEquals(ParametrosJuego.ESTADISTICA_INICIAL,
                jugador.getEstadisticas().getSurvival());
        assertEquals(0, jugador.getBarraPoder().getCarga(), 0.0001);

        jugador.actualizar(5.1, partidoBarril.getCancha());

        assertEquals(8, jugador.getEstadisticas().getSpeed());
        assertEquals(9, jugador.getEstadisticas().getSurvival());
    }

    /** Verifica que cada poder aplica el efecto que le corresponde. */
    @Test
    public void shouldApplyTheCorrectEffectForEveryPower() {
        Poder[] poderes = {
                new PoderAuraAzul(),
                new PoderRayo(),
                new PoderTornado(),
                new PoderIlusion(),
                new PoderBesoDeLaFea(),
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

            assertTrue(tieneEfecto(rival, efectos[i]),
                    "El poder " + poderes[i].getNombre()
                            + " debía aplicar " + efectos[i]);
        }
    }

    /** Verifica que el efecto barril no modifica las estadísticas del perfil. */
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
                .aplicarEfecto(new EfectoBarril(5));

        assertEquals(8, estadisticasPerfil.getSpeed());
        assertEquals(9, estadisticasPerfil.getSurvival());
    }

    /**
     * Indica si el jugador tiene activo un efecto del tipo indicado. Las
     * pruebas lo consultan directamente porque el dominio ya no decide
     * comportamiento a partir de {@link TipoEfecto}.
     *
     * @param jugador jugador consultado
     * @param tipo tipo de efecto buscado
     * @return true si el efecto está activo
     */
    private static boolean tieneEfecto(Jugador jugador, TipoEfecto tipo) {
        for (Efecto efecto : jugador.getEfectosActivos()) {
            if (efecto.getTipo() == tipo) {
                return true;
            }
        }
        return false;
    }

    /**
     * Crea un partido de prueba con los personajes y estadísticas indicados.
     *
     * @param personaje1 personaje del jugador 1
     * @param personaje2 personaje del jugador 2
     * @param estadisticas1 estadísticas del jugador 1
     * @param estadisticas2 estadísticas del jugador 2
     * @return partido configurado para la prueba
     */
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
