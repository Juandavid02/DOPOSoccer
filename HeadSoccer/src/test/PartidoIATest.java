package test;

import domain.CatalogoPersonajes;
import domain.ConfiguracionIA;
import domain.ConfiguracionJugador;
import domain.ConfiguracionPartido;
import domain.ControladorDefensivo;
import domain.ControladorExperto;
import domain.ControladorOfensivo;
import domain.ControladorTorpe;
import domain.Estadisticas;
import domain.FabricaControladorIA;
import domain.JugadorMaquina;
import domain.Modalidad;
import domain.Partido;
import domain.PerfilIA;
import domain.Personaje;
import domain.Resolucion;
import domain.TipoPartido;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PartidoIATest {

    /**
     * Crea una configuración de jugador de prueba.
     *
     * @return configuración de jugador con estadísticas de prueba
     * @throws Exception si la configuración del jugador no es válida
     */
    private static ConfiguracionJugador configuracion() throws Exception {
        return new ConfiguracionJugador("Prueba",
                new Estadisticas(5, 5, 5, 5));
    }

    /**
     * Crea un partido de prueba contra la máquina con el perfil indicado.
     *
     * @param perfil perfil de la inteligencia artificial
     * @return partido configurado para las pruebas
     * @throws Exception si la configuración del partido no es válida
     */
    private static Partido crearPartido(PerfilIA perfil) throws Exception {
        List<Personaje> personajes = CatalogoPersonajes.getPersonajes();
        ConfiguracionPartido configuracion = new ConfiguracionPartido(
                Modalidad.PvsM,
                TipoPartido.TIEMPO,
                Resolucion.R1280x720,
                3,
                5,
                perfil);
        return new Partido(configuracion,
                personajes.get(0),
                personajes.get(1),
                configuracion(),
                configuracion(),
                perfil);
    }

    /**
     * Verifica que la máquina se mueva hacia el balón.
     *
     * @throws Exception si la configuración del partido no es válida
     */
    @Test
    void laMaquinaSeMueveHaciaElBalon() throws Exception {
        Partido partido = crearPartido(PerfilIA.EXPERTA);
        double posicionInicial = partido.getJugadores().get(1).getPosicionX();

        for (int i = 0; i < 60; i++) {
            partido.actualizar(1.0 / 60.0);
        }

        double posicionFinal = partido.getJugadores().get(1).getPosicionX();
        assertTrue(posicionFinal < posicionInicial,
                "La IA debe acercarse al balón que está en el centro");
    }

    /**
     * Verifica que el constructor corto use el perfil de IA de la configuración.
     *
     * @throws Exception si la configuración del partido no es válida
     */
    @Test
    void elConstructorCortoUsaElPerfilIAdeLaConfiguracion()
            throws Exception {
        List<Personaje> personajes = CatalogoPersonajes.getPersonajes();
        ConfiguracionPartido configuracion = new ConfiguracionPartido(
                Modalidad.PvsM,
                TipoPartido.TIEMPO,
                Resolucion.R1280x720,
                3,
                5,
                PerfilIA.EXPERTA);

        Partido partido = new Partido(configuracion,
                personajes.get(0),
                personajes.get(1),
                configuracion(),
                configuracion());

        assertTrue(partido.getJugadores().get(1)
                instanceof JugadorMaquina);
    }

    /**
     * Verifica que la configuración de IA copie sus estadísticas.
     *
     * @throws Exception si la configuración del partido no es válida
     */
    @Test
    void laConfiguracionIACopiaSusEstadisticas() throws Exception {
        Estadisticas originales = new Estadisticas(8, 2, 7, 9);
        ConfiguracionIA configuracion = new ConfiguracionIA(
                PerfilIA.DEFENSIVA, originales);

        originales.copiarDesde(new Estadisticas(1, 1, 1, 1));

        assertEquals(8, configuracion.getEstadisticas().getSpeed());
        assertEquals(2, configuracion.getEstadisticas().getJump());
        assertEquals("IA · DEFENSIVA",
                configuracion.crearConfiguracionJugador().getNombre());
    }

    /** Verifica que la fábrica cree el controlador de cada perfil. */
    @Test
    void laFabricaCreaElControladorDeCadaPerfil() {
        assertTrue(FabricaControladorIA.crear(PerfilIA.TORPE)
                instanceof ControladorTorpe);
        assertTrue(FabricaControladorIA.crear(PerfilIA.OFENSIVA)
                instanceof ControladorOfensivo);
        assertTrue(FabricaControladorIA.crear(PerfilIA.DEFENSIVA)
                instanceof ControladorDefensivo);
        assertTrue(FabricaControladorIA.crear(PerfilIA.EXPERTA)
                instanceof ControladorExperto);
        assertThrows(IllegalArgumentException.class,
                () -> FabricaControladorIA.crear(null));
    }

    /**
     * Verifica que la IA defensiva retroceda desde su posición inicial.
     *
     * @throws Exception si la configuración del partido no es válida
     */
    @Test
    void laIADefensivaRetrocedeHaciaSuArco() throws Exception {
        Partido partido = crearPartido(PerfilIA.DEFENSIVA);
        double posicionInicial = partido.getJugadores().get(1).getPosicionX();

        for (int i = 0; i < 60; i++) {
            partido.actualizar(1.0 / 60.0);
        }

        assertTrue(partido.getJugadores().get(1).getPosicionX()
                < posicionInicial);
    }
}
