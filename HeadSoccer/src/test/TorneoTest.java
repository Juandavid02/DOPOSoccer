package test;

import domain.CatalogoPersonajes;
import domain.ConfiguracionJugador;
import domain.ConfiguracionPartido;
import domain.Estadisticas;
import domain.ExcepcionDOPOSoccer;
import domain.Modalidad;
import domain.ParametrosJuego;
import domain.ParticipanteTorneo;
import domain.Partido;
import domain.PerfilIA;
import domain.PerfilJugador;
import domain.Personaje;
import domain.Resolucion;
import domain.TipoPartido;
import domain.Torneo;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TorneoTest {

    private static final Random RANDOM = new Random(7);

    /**
     * Devuelve la lista de personajes del catálogo.
     *
     * @return lista de personajes disponibles
     */
    private static List<Personaje> personajes() {
        return CatalogoPersonajes.getPersonajes();
    }

    /**
     * Crea un torneo de prueba con el perfil indicado.
     *
     * @param perfil perfil del jugador humano
     * @return torneo configurado para la prueba
     * @throws Exception si falla la creación del torneo
     */
    private static Torneo crearTorneo(PerfilJugador perfil) throws Exception {
        List<Personaje> p = personajes();
        ParticipanteTorneo humano = ParticipanteTorneo.humano(perfil, p.get(0));
        ParticipanteTorneo ia1 = ParticipanteTorneo.maquina(
                PerfilIA.TORPE, p.get(1));
        ParticipanteTorneo ia2 = ParticipanteTorneo.maquina(
                PerfilIA.OFENSIVA, p.get(2));
        ParticipanteTorneo ia3 = ParticipanteTorneo.maquina(
                PerfilIA.EXPERTA, p.get(3));
        return new Torneo(perfil, humano, ia1, ia2, ia3);
    }

    /**
     * Crea un perfil de jugador con la cantidad de puntos indicada.
     *
     * @param puntos puntos iniciales del perfil
     * @return perfil con los puntos sumados
     * @throws Exception si falla la creación del perfil
     */
    private static PerfilJugador perfilConPuntos(int puntos) throws Exception {
        PerfilJugador perfil = new PerfilJugador("Jugador");
        perfil.getPuntos().sumar(puntos);
        return perfil;
    }

    /**
     * Verifica que la inscripción descuenta el costo del torneo.
     *
     * @throws Exception si falla la inscripción en el torneo
     */
    @Test
    void laInscripcionDescuentaElCosto() throws Exception {
        PerfilJugador perfil = perfilConPuntos(300);
        crearTorneo(perfil);
        assertEquals(200, perfil.getPuntos().getTotal());
    }

    /**
     * Verifica que sin puntos suficientes no se permite la inscripción.
     *
     * @throws Exception si falla la creación del perfil
     */
    @Test
    void sinPuntosNoSePuedeInscribir() throws Exception {
        PerfilJugador perfil = perfilConPuntos(99);
        assertThrows(ExcepcionDOPOSoccer.class, () -> crearTorneo(perfil));
        assertEquals(99, perfil.getPuntos().getTotal());
    }

    /**
     * Verifica que el campeón del torneo recibe el premio.
     *
     * @throws Exception si falla la simulación del torneo
     */
    @Test
    void elCampeonRecibeElPremio() throws Exception {
        PerfilJugador perfil = perfilConPuntos(300);
        Torneo torneo = crearTorneo(perfil);

        torneo.registrarResultadoHumano(3, 1, RANDOM);
        torneo.registrarResultadoHumano(2, 0, RANDOM);

        assertTrue(torneo.isHumanoCampeon());
        assertEquals(ParametrosJuego.PREMIO_CAMPEON_TORNEO,
                torneo.getPremioHumano());
        assertEquals(700, perfil.getPuntos().getTotal());
    }

    /**
     * Verifica que el subcampeón del torneo recibe su premio.
     *
     * @throws Exception si falla la simulación del torneo
     */
    @Test
    void elSubcampeonRecibeSuPremio() throws Exception {
        PerfilJugador perfil = perfilConPuntos(300);
        Torneo torneo = crearTorneo(perfil);

        torneo.registrarResultadoHumano(2, 1, RANDOM);
        torneo.registrarResultadoHumano(1, 2, RANDOM);

        assertTrue(torneo.isHumanoSubcampeon());
        assertEquals(ParametrosJuego.PREMIO_SUBCAMPEON_TORNEO,
                torneo.getPremioHumano());
        assertEquals(350, perfil.getPuntos().getTotal());
    }

    /**
     * Verifica que el eliminado en semifinal no recibe premio.
     *
     * @throws Exception si falla la simulación del torneo
     */
    @Test
    void elEliminadoEnSemifinalNoRecibePremio() throws Exception {
        PerfilJugador perfil = perfilConPuntos(300);
        Torneo torneo = crearTorneo(perfil);

        torneo.registrarResultadoHumano(0, 2, RANDOM);

        assertTrue(torneo.isHumanoEliminado());
        assertFalse(torneo.isHumanoCampeon());
        assertEquals(0, torneo.getPremioHumano());
        assertEquals(200, perfil.getPuntos().getTotal());
        assertTrue(torneo.getFinal().isJugado());
    }

    /**
     * Verifica que no se cobra dos veces la inscripción al torneo.
     *
     * @throws Exception si falla la creación del perfil
     */
    @Test
    void noCobraDosVecesCuandoLaInscripcionYaSePago() throws Exception {
        PerfilJugador perfil = perfilConPuntos(300);
        List<Personaje> p = personajes();
        ParticipanteTorneo humano = ParticipanteTorneo.humano(perfil, p.get(0));
        ParticipanteTorneo ia1 = ParticipanteTorneo.maquina(
                PerfilIA.TORPE, p.get(1));
        ParticipanteTorneo ia2 = ParticipanteTorneo.maquina(
                PerfilIA.OFENSIVA, p.get(2));
        ParticipanteTorneo ia3 = ParticipanteTorneo.maquina(
                PerfilIA.EXPERTA, p.get(3));

        new Torneo(perfil, humano, ia1, ia2, ia3, false);

        assertEquals(300, perfil.getPuntos().getTotal());
    }

    /**
     * Verifica que un partido de torneo no reparte puntos.
     *
     * @throws Exception si falla la creación del partido
     */
    @Test
    void unPartidoDeTorneoNoRepartePuntos() throws Exception {
        List<Personaje> personajes = personajes();
        ConfiguracionJugador jugador1 = new ConfiguracionJugador(
                "Humano", new Estadisticas(5, 5, 5, 5));
        ConfiguracionJugador jugador2 = new ConfiguracionJugador(
                "IA", new Estadisticas(5, 5, 5, 5));
        ConfiguracionPartido configuracion = new ConfiguracionPartido(
                Modalidad.PvsM,
                TipoPartido.TIEMPO,
                Resolucion.R1280x720,
                2,
                5,
                PerfilIA.EXPERTA);

        Partido partido = new Partido(configuracion,
                personajes.get(0), personajes.get(1),
                jugador1, jugador2, PerfilIA.EXPERTA, false);

        partido.terminar();

        assertEquals(0, partido.getPuntosJugador1());
        assertEquals(0, partido.getPuntosJugador2());
        assertEquals(0, jugador1.getPuntos().getTotal());
    }
}
