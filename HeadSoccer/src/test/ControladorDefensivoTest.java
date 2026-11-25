package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import domain.Balon;
import domain.CatalogoPersonajes;
import domain.ConfiguracionJugador;
import domain.ConfiguracionPartido;
import domain.ControladorDefensivo;
import domain.Estadisticas;
import domain.Jugador;
import domain.Modalidad;
import domain.Partido;
import domain.Personaje;
import domain.Resolucion;
import domain.TipoPartido;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Pruebas de la posición objetivo de la IA defensiva.
 */
class ControladorDefensivoTest {

    private static final double EPSILON = 0.0001;

    /**
     * Expone el método protegido para poder probarlo desde el paquete test.
     */
    private static class DefensivoExpuesto extends ControladorDefensivo {
        /**
         * Calcula la posición objetivo del perfil defensivo.
         *
         * @param partido partido en curso
         * @param yo jugador controlado
         * @param balon balón del partido
         * @return posición horizontal objetivo
         */
        double calcularObjetivo(Partido partido, Jugador yo, Balon balon) {
            return objetivo(partido, yo, balon);
        }
    }

    /**
     * Crea una configuración de jugador de prueba.
     *
     * @param nombre nombre del jugador
     * @return configuración válida
     * @throws Exception si la configuración no es válida
     */
    private static ConfiguracionJugador configuracion(String nombre)
            throws Exception {
        return new ConfiguracionJugador(nombre, new Estadisticas(5, 5, 5, 5));
    }

    /**
     * Crea un partido PvsP para usar su cancha y sus jugadores.
     *
     * @return partido de prueba
     * @throws Exception si la configuración no es válida
     */
    private static Partido partido() throws Exception {
        List<Personaje> personajes = CatalogoPersonajes.getPersonajes();
        ConfiguracionPartido configuracion = new ConfiguracionPartido(
                Modalidad.PvsP, TipoPartido.TIEMPO, Resolucion.R1280x720, 3, 5);
        return new Partido(configuracion,
                personajes.get(0),
                personajes.get(1),
                configuracion("Uno"),
                configuracion("Dos"));
    }

    /**
     * Verifica el desplazamiento normal hacia ambos lados de la cancha.
     *
     * @throws Exception si la configuración no es válida
     */
    @Test
    void elObjetivoSeDesplazaRespectoAlBalon() throws Exception {
        Partido partido = partido();
        Balon balon = partido.getBalon();
        Jugador izquierdo = partido.getJugadores().get(0);
        Jugador derecho = partido.getJugadores().get(1);
        DefensivoExpuesto controlador = new DefensivoExpuesto();

        double centro = partido.getCancha().getAncho() / 2.0;
        balon.reubicar(centro, balon.getY());

        assertEquals(centro + 150,
                controlador.calcularObjetivo(partido, derecho, balon),
                EPSILON);
        assertEquals(centro - 150,
                controlador.calcularObjetivo(partido, izquierdo, balon),
                EPSILON);
    }

    /**
     * Verifica los límites cuando el balón está pegado a cada arco.
     *
     * @throws Exception si la configuración no es válida
     */
    @Test
    void elObjetivoRespetaLosLimitesDeLaCancha() throws Exception {
        Partido partido = partido();
        Balon balon = partido.getBalon();
        Jugador izquierdo = partido.getJugadores().get(0);
        Jugador derecho = partido.getJugadores().get(1);
        DefensivoExpuesto controlador = new DefensivoExpuesto();
        double ancho = partido.getCancha().getAncho();

        balon.reubicar(0, balon.getY());
        assertEquals(ancho * 0.55,
                controlador.calcularObjetivo(partido, derecho, balon),
                EPSILON);
        assertEquals(80,
                controlador.calcularObjetivo(partido, izquierdo, balon),
                EPSILON);

        balon.reubicar(ancho, balon.getY());
        assertEquals(ancho - 80,
                controlador.calcularObjetivo(partido, derecho, balon),
                EPSILON);
        assertEquals(ancho * 0.45,
                controlador.calcularObjetivo(partido, izquierdo, balon),
                EPSILON);
    }
}
