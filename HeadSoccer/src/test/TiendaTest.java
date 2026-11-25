package test;

import static org.junit.jupiter.api.Assertions.*;

import domain.ExcepcionDOPOSoccer;
import domain.PerfilJugador;
import domain.Tienda;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Pruebas de la tienda: cada perfil debe tener sus propios desbloqueos.
 */
public class TiendaTest {

    private PerfilJugador perfilConPuntos;
    private PerfilJugador perfilSinPuntos;
    private Tienda tienda;

    /**
     * Prepara los perfiles y la tienda de desbloqueos.
     *
     * @throws ExcepcionDOPOSoccer si la configuración de un perfil no es válida
     */
    @BeforeEach
    public void setUp() throws ExcepcionDOPOSoccer {
        perfilConPuntos = new PerfilJugador("Con puntos");
        perfilConPuntos.getPuntos().sumar(1000);
        perfilSinPuntos = new PerfilJugador("Sin puntos");
        tienda = new Tienda();
    }

    /**
     * Verifica que los personajes iniciales estén disponibles para todos.
     */
    @Test
    public void losPersonajesInicialesEstanDisponiblesParaTodos() {
        assertTrue(perfilConPuntos.estaDesbloqueadoPersonaje("Betty la fea"));
        assertTrue(perfilSinPuntos.estaDesbloqueadoPersonaje("Betty la fea"));
        assertFalse(perfilConPuntos.estaDesbloqueadoPersonaje("Mario"));
    }

    /**
     * Verifica que un desbloqueo no se comparta entre perfiles.
     */
    @Test
    public void unDesbloqueoNoSeComparteEntrePerfiles() {
        assertTrue(tienda.desbloquearPersonaje("Mario", perfilConPuntos));

        assertTrue(perfilConPuntos.estaDesbloqueadoPersonaje("Mario"));
        assertFalse(perfilSinPuntos.estaDesbloqueadoPersonaje("Mario"));
    }

    /**
     * Verifica que la compra descuente solo los puntos del perfil que compra.
     */
    @Test
    public void laCompraDescuentaSoloLosPuntosDelPerfilQueCompra() {
        tienda.desbloquearPersonaje("Mario", perfilConPuntos);

        assertEquals(500, perfilConPuntos.getPuntos().getTotal());
        assertEquals(0, perfilSinPuntos.getPuntos().getTotal());
    }

    /**
     * Verifica que no se pueda comprar dos veces el mismo personaje.
     */
    @Test
    public void noSePuedeComprarDosVecesElMismoPersonaje() {
        assertTrue(tienda.desbloquearPersonaje("Mario", perfilConPuntos));
        assertFalse(tienda.desbloquearPersonaje("Mario", perfilConPuntos));
        assertEquals(500, perfilConPuntos.getPuntos().getTotal());
    }

    /**
     * Verifica que sin puntos no se pueda desbloquear un personaje.
     */
    @Test
    public void sinPuntosNoSePuedeDesbloquear() {
        assertFalse(tienda.desbloquearPersonaje("Mario", perfilSinPuntos));
        assertFalse(perfilSinPuntos.estaDesbloqueadoPersonaje("Mario"));
    }

    /**
     * Verifica los casos inválidos al desbloquear un personaje.
     */
    @Test
    public void desbloquearPersonajeConDatosInvalidosDevuelveFalse() {
        assertFalse(tienda.desbloquearPersonaje(null, perfilConPuntos));
        assertFalse(tienda.desbloquearPersonaje("Mario", null));
        assertFalse(tienda.desbloquearPersonaje("Betty la fea",
                perfilConPuntos));
        assertEquals(1000, perfilConPuntos.getPuntos().getTotal());
    }

    /**
     * Verifica que un balón se cobre y se desbloquee solo para su perfil.
     */
    @Test
    public void desbloquearBalonCobraYDesbloqueaSoloEsePerfil() {
        assertTrue(tienda.desbloquearBalon("BTS", perfilConPuntos));

        assertTrue(perfilConPuntos.estaDesbloqueadoBalon("BTS"));
        assertFalse(perfilSinPuntos.estaDesbloqueadoBalon("BTS"));
        assertEquals(700, perfilConPuntos.getPuntos().getTotal());
        assertEquals(0, perfilSinPuntos.getPuntos().getTotal());
    }

    /**
     * Verifica que no se pueda comprar dos veces el mismo balón.
     */
    @Test
    public void noSePuedeComprarDosVecesElMismoBalon() {
        assertTrue(tienda.desbloquearBalon("BTS", perfilConPuntos));
        assertFalse(tienda.desbloquearBalon("BTS", perfilConPuntos));

        assertEquals(700, perfilConPuntos.getPuntos().getTotal());
    }

    /**
     * Verifica los casos inválidos al desbloquear un balón.
     */
    @Test
    public void desbloquearBalonConDatosInvalidosDevuelveFalse() {
        assertFalse(tienda.desbloquearBalon(null, perfilConPuntos));
        assertFalse(tienda.desbloquearBalon("BTS", null));
        assertFalse(tienda.desbloquearBalon("Mario", perfilConPuntos));
        assertEquals(1000, perfilConPuntos.getPuntos().getTotal());
    }

    /**
     * Verifica que un escenario se cobre y se desbloquee solo para su perfil.
     */
    @Test
    public void desbloquearEscenarioCobraYDesbloqueaSoloEsePerfil() {
        assertTrue(tienda.desbloquearEscenario("Estadio Nuevo", perfilConPuntos));

        assertTrue(perfilConPuntos.estaDesbloqueadoEscenario("Estadio Nuevo"));
        assertFalse(perfilSinPuntos.estaDesbloqueadoEscenario("Estadio Nuevo"));
        assertEquals(600, perfilConPuntos.getPuntos().getTotal());
        assertEquals(0, perfilSinPuntos.getPuntos().getTotal());
    }

    /**
     * Verifica que no se pueda comprar dos veces el mismo escenario.
     */
    @Test
    public void noSePuedeComprarDosVecesElMismoEscenario() {
        assertTrue(tienda.desbloquearEscenario("Estadio Nuevo", perfilConPuntos));
        assertFalse(tienda.desbloquearEscenario("Estadio Nuevo", perfilConPuntos));

        assertEquals(600, perfilConPuntos.getPuntos().getTotal());
    }

    /**
     * Verifica los casos inválidos al desbloquear un escenario.
     */
    @Test
    public void desbloquearEscenarioConDatosInvalidosDevuelveFalse() {
        assertFalse(tienda.desbloquearEscenario(null, perfilConPuntos));
        assertFalse(tienda.desbloquearEscenario("Estadio Nuevo", null));
        assertFalse(tienda.desbloquearEscenario("Fondo de Bikini",
                perfilConPuntos));
        assertEquals(1000, perfilConPuntos.getPuntos().getTotal());
    }
}
