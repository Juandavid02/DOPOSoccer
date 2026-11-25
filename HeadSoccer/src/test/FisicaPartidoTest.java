package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import domain.Balon;
import domain.Cancha;
import domain.CatalogoPersonajes;
import domain.ConfiguracionJugador;
import domain.ConfiguracionPartido;
import domain.Estadisticas;
import domain.FisicaPartido;
import domain.Jugador;
import domain.Modalidad;
import domain.ParametrosJuego;
import domain.Partido;
import domain.Personaje;
import domain.Resolucion;
import domain.TipoPartido;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Pruebas de las colisiones del balón con los jugadores.
 */
class FisicaPartidoTest {

    private Cancha cancha;
    private Jugador jugador;
    private Partido partido;

    /**
     * Crea un partido PvsP para reutilizar su cancha y sus jugadores.
     *
     * @throws Exception si la configuración no es válida
     */
    @BeforeEach
    void setUp() throws Exception {
        List<Personaje> personajes = CatalogoPersonajes.getPersonajes();
        ConfiguracionPartido configuracion = new ConfiguracionPartido(
                Modalidad.PvsP, TipoPartido.TIEMPO, Resolucion.R1280x720, 3, 5);
        partido = new Partido(configuracion,
                personajes.get(0),
                personajes.get(1),
                configuracion("Ana"),
                configuracion("Luis"));
        cancha = partido.getCancha();
        jugador = partido.getJugadores().get(0);
    }

    /**
     * Crea una configuración de jugador de prueba.
     *
     * @param nombre nombre del jugador
     * @return configuración válida
     * @throws Exception si los datos no son válidos
     */
    private static ConfiguracionJugador configuracion(String nombre)
            throws Exception {
        return new ConfiguracionJugador(nombre, new Estadisticas(5, 5, 5, 5));
    }

    /** Un balón lejos del círculo no se modifica. */
    @Test
    void unBalonLejosNoColisiona() {
        Balon balon = new Balon(0, 0, ParametrosJuego.RADIO_BALON);
        balon.establecerVelocidad(10, 20);

        boolean toco = FisicaPartido.resolverBalonCirculo(
                balon, jugador, cancha, 100, 100, 10);

        assertFalse(toco);
        assertEquals(0, balon.getX(), 0.0001);
        assertEquals(0, balon.getY(), 0.0001);
        assertEquals(10, balon.getVelocidadX(), 0.0001);
        assertEquals(20, balon.getVelocidadY(), 0.0001);
    }

    /** Un balón solapado se coloca fuera del círculo y recibe impulso. */
    @Test
    void unBalonSolapadoSeSepara() {
        Balon balon = new Balon(120, 100, ParametrosJuego.RADIO_BALON);

        boolean toco = FisicaPartido.resolverBalonCirculo(
                balon, jugador, cancha, 100, 100, 10);

        assertTrue(toco);
        assertEquals(140, balon.getX(), 0.0001);
        assertEquals(100, balon.getY(), 0.0001);
        assertEquals(30, balon.getVelocidadX(), 0.0001);
    }

    /** Si el balón está exactamente en el centro se usa la normal horizontal. */
    @Test
    void unBalonEnElMismoCentroUsaNormalHorizontal() {
        Balon balon = new Balon(100, 100, ParametrosJuego.RADIO_BALON);

        boolean toco = FisicaPartido.resolverBalonCirculo(
                balon, jugador, cancha, 100, 100, 10);

        assertTrue(toco);
        assertEquals(140, balon.getX(), 0.0001);
        assertEquals(100, balon.getY(), 0.0001);
    }

    /** Si el balón va hacia el jugador, rebota con restitución. */
    @Test
    void elBalonRebotaSiVaHaciaElJugador() {
        Balon balon = new Balon(120, 100, ParametrosJuego.RADIO_BALON);
        balon.establecerVelocidad(-100, 0);

        FisicaPartido.resolverBalonCirculo(balon, jugador, cancha, 100, 100, 10);

        assertEquals(60, balon.getVelocidadX(), 0.0001);
    }

    /** Si el balón se aleja, solo recibe un pequeño empuje. */
    @Test
    void elBalonSoloRecibeEmpujeSiSeAleja() {
        Balon balon = new Balon(120, 100, ParametrosJuego.RADIO_BALON);
        balon.establecerVelocidad(50, 0);

        FisicaPartido.resolverBalonCirculo(balon, jugador, cancha, 100, 100, 10);

        assertEquals(80, balon.getVelocidadX(), 0.0001);
    }

    /** Un choque contra el techo se resuelve en horizontal. */
    @Test
    void unChoqueContraElTechoSeResuelveEnHorizontal() {
        Balon balon = new Balon(130, -25, ParametrosJuego.RADIO_BALON);

        boolean toco = FisicaPartido.resolverBalonCirculo(
                balon, jugador, cancha, 100, -20, 10);

        assertTrue(toco);
        assertEquals(12, balon.getY(), 0.0001);
        assertEquals(124, balon.getX(), 0.0001);
        assertEquals(30, balon.getVelocidadX(), 0.0001);
    }

    /** El choque contra el techo elige el lado izquierdo si el balón está allí. */
    @Test
    void unChoqueContraElTechoEligeElLadoCorrecto() {
        Balon balon = new Balon(70, -25, ParametrosJuego.RADIO_BALON);

        FisicaPartido.resolverBalonCirculo(balon, jugador, cancha, 100, -20, 10);

        assertEquals(12, balon.getY(), 0.0001);
        assertEquals(76, balon.getX(), 0.0001);
    }

    /**
     * Configura dos jugadores con el balón atrapado entre ellos.
     *
     * @return balón en disputa
     */
    private Balon balonAtrapado() {
        Jugador izquierdo = partido.getJugadores().get(0);
        Jugador derecho = partido.getJugadores().get(1);
        double y = cancha.getSuelo() - ParametrosJuego.RADIO_JUGADOR;
        izquierdo.reiniciar(500, y);
        derecho.reiniciar(540, y);
        Balon balon = new Balon(520,
                cancha.getSuelo() - ParametrosJuego.RADIO_BALON,
                ParametrosJuego.RADIO_BALON);
        balon.establecerVelocidad(0, 0);
        return balon;
    }

    /** Un balón atrapado separa a los jugadores y sale hacia arriba. */
    @Test
    void unBalonAtrapadoSeparaLosJugadoresYSube() {
        Jugador izquierdo = partido.getJugadores().get(0);
        Jugador derecho = partido.getJugadores().get(1);
        Balon balon = balonAtrapado();

        FisicaPartido.resolverBalonAtrapado(izquierdo, derecho, balon,
                cancha, 1.0 / 60.0);

        assertTrue(izquierdo.getPosicionX() < 500);
        assertTrue(derecho.getPosicionX() > 540);
        assertEquals(-ParametrosJuego.POTENCIA_LEVANTE_PATADA,
                balon.getVelocidadY(), 0.0001);
    }

    /** El orden de los jugadores no altera la separación. */
    @Test
    void elOrdenDeLosJugadoresNoAlteraLaSeparacion() {
        Jugador izquierdo = partido.getJugadores().get(0);
        Jugador derecho = partido.getJugadores().get(1);
        Balon balon = balonAtrapado();

        FisicaPartido.resolverBalonAtrapado(derecho, izquierdo, balon,
                cancha, 1.0 / 60.0);

        assertTrue(izquierdo.getPosicionX() < 500);
        assertTrue(derecho.getPosicionX() > 540);
    }

    /** Si el balón no está entre los jugadores no se resuelve nada. */
    @Test
    void unBalonFueraDeLosJugadoresNoSeResuelve() {
        Jugador izquierdo = partido.getJugadores().get(0);
        Jugador derecho = partido.getJugadores().get(1);
        Balon balon = balonAtrapado();
        balon.reubicar(700, balon.getY());
        double xIzquierdo = izquierdo.getPosicionX();
        double xDerecho = derecho.getPosicionX();

        FisicaPartido.resolverBalonAtrapado(izquierdo, derecho, balon,
                cancha, 1.0 / 60.0);

        assertEquals(xIzquierdo, izquierdo.getPosicionX(), 0.0001);
        assertEquals(xDerecho, derecho.getPosicionX(), 0.0001);
        assertEquals(0, balon.getVelocidadY(), 0.0001);
    }

    /** Si el balón no toca a ambos jugadores no se resuelve nada. */
    @Test
    void unBalonQueNoTocaAambosJugadoresNoSeResuelve() {
        Jugador izquierdo = partido.getJugadores().get(0);
        Jugador derecho = partido.getJugadores().get(1);
        Balon balon = balonAtrapado();
        balon.reubicar(520, 0);
        double xIzquierdo = izquierdo.getPosicionX();
        double xDerecho = derecho.getPosicionX();

        FisicaPartido.resolverBalonAtrapado(izquierdo, derecho, balon,
                cancha, 1.0 / 60.0);

        assertEquals(xIzquierdo, izquierdo.getPosicionX(), 0.0001);
        assertEquals(xDerecho, derecho.getPosicionX(), 0.0001);
        assertEquals(0, balon.getVelocidadY(), 0.0001);
    }

    /** Si el balón ya sube más rápido, no se le cambia la velocidad. */
    @Test
    void unBalonQueYaSubeRapidoConservaSuVelocidad() {
        Jugador izquierdo = partido.getJugadores().get(0);
        Jugador derecho = partido.getJugadores().get(1);
        Balon balon = balonAtrapado();
        balon.establecerVelocidad(0, -600);

        FisicaPartido.resolverBalonAtrapado(izquierdo, derecho, balon,
                cancha, 1.0 / 60.0);

        assertEquals(-600, balon.getVelocidadY(), 0.0001);
    }
}
