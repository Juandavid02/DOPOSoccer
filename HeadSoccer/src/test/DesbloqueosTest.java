package test;

import static org.junit.jupiter.api.Assertions.*;

import domain.ExcepcionDOPOSoccer;
import domain.PerfilJugador;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import presentation.Desbloqueos;

/**
 * Pruebas de la tienda: cada perfil debe tener sus propios desbloqueos.
 */
public class DesbloqueosTest {

    private PerfilJugador perfilConPuntos;
    private PerfilJugador perfilSinPuntos;
    private Desbloqueos tienda;

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
        tienda = new Desbloqueos();
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
}
