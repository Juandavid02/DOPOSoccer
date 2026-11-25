package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import domain.ConfiguracionJugador;
import domain.Estadisticas;
import domain.ExcepcionDOPOSoccer;
import domain.ParametrosJuego;
import domain.PerfilJugador;
import domain.TipoEstadistica;
import org.junit.jupiter.api.Test;

/**
 * Pruebas de las mejoras compradas con puntos en un perfil.
 */
class PerfilJugadorTest {

    /**
     * Crea un perfil con estadísticas y puntos iniciales.
     *
     * @param speed valor de velocidad
     * @param jump valor de salto
     * @param kick valor de patada
     * @param survival valor de supervivencia
     * @param puntos puntos iniciales
     * @return perfil listo para probar
     * @throws ExcepcionDOPOSoccer si los datos no son válidos
     */
    private static PerfilJugador perfil(int speed, int jump, int kick,
            int survival, int puntos) throws ExcepcionDOPOSoccer {
        Estadisticas estadisticas = new Estadisticas(speed, jump, kick, survival);
        ConfiguracionJugador configuracion =
                new ConfiguracionJugador("Ana", estadisticas);
        PerfilJugador perfil =
                new PerfilJugador("id-ana", "Ana", configuracion);
        perfil.getPuntos().sumar(puntos);
        return perfil;
    }

    /**
     * Verifica que mejorar suba solo la estadística elegida y cobre el costo.
     *
     * @throws ExcepcionDOPOSoccer si la mejora no es válida
     */
    @Test
    void mejorarCadaEstadisticaSubeSoloLaCorrespondiente()
            throws ExcepcionDOPOSoccer {
        int inicial = ParametrosJuego.ESTADISTICA_INICIAL;
        PerfilJugador perfil = perfil(inicial, inicial, inicial, inicial, 10000);

        assertEquals(0, perfil.getMejorasCompradas());
        assertEquals(ParametrosJuego.COSTO_MEJORA_INICIAL,
                perfil.getCostoSiguienteMejora());

        assertTrue(perfil.mejorar(TipoEstadistica.VELOCIDAD));
        assertEquals(inicial + 1, perfil.getEstadisticas().getSpeed());
        assertEquals(inicial, perfil.getEstadisticas().getJump());
        assertEquals(inicial, perfil.getEstadisticas().getKick());
        assertEquals(inicial, perfil.getEstadisticas().getSurvival());
        assertEquals(9500, perfil.getPuntos().getTotal());
        assertEquals(1, perfil.getMejorasCompradas());
        assertEquals(1000, perfil.getCostoSiguienteMejora());

        assertTrue(perfil.mejorar(TipoEstadistica.SALTO));
        assertEquals(inicial + 1, perfil.getEstadisticas().getJump());
        assertEquals(8500, perfil.getPuntos().getTotal());
        assertEquals(2000, perfil.getCostoSiguienteMejora());

        assertTrue(perfil.mejorar(TipoEstadistica.PATADA));
        assertEquals(inicial + 1, perfil.getEstadisticas().getKick());
        assertEquals(6500, perfil.getPuntos().getTotal());
        assertEquals(4000, perfil.getCostoSiguienteMejora());

        assertTrue(perfil.mejorar(TipoEstadistica.SUPERVIVENCIA));
        assertEquals(inicial + 1, perfil.getEstadisticas().getSurvival());
        assertEquals(2500, perfil.getPuntos().getTotal());
        assertEquals(4, perfil.getMejorasCompradas());
        assertEquals(8000, perfil.getCostoSiguienteMejora());
    }

    /**
     * Verifica que sin puntos no se modifique el perfil.
     *
     * @throws ExcepcionDOPOSoccer si la mejora no es válida
     */
    @Test
    void mejorarSinPuntosDevuelveFalseYNoCambiaNada()
            throws ExcepcionDOPOSoccer {
        int inicial = ParametrosJuego.ESTADISTICA_INICIAL;
        PerfilJugador perfil = perfil(inicial, inicial, inicial, inicial, 0);

        assertFalse(perfil.mejorar(TipoEstadistica.VELOCIDAD));
        assertEquals(inicial, perfil.getEstadisticas().getSpeed());
        assertEquals(0, perfil.getPuntos().getTotal());
        assertEquals(0, perfil.getMejorasCompradas());
    }

    /**
     * Verifica que una estadística al máximo no se pueda mejorar.
     *
     * @throws ExcepcionDOPOSoccer si la mejora no es válida
     */
    @Test
    void noSePuedeMejorarUnaEstadisticaAlMaximo() throws ExcepcionDOPOSoccer {
        int maximo = ParametrosJuego.MAX_ESTADISTICA;
        PerfilJugador perfil = perfil(maximo, 5, 5, 5, 10000);

        assertFalse(perfil.puedeMejorar(TipoEstadistica.VELOCIDAD));
        assertFalse(perfil.mejorar(TipoEstadistica.VELOCIDAD));
        assertEquals(maximo, perfil.getEstadisticas().getSpeed());
        assertEquals(10000, perfil.getPuntos().getTotal());
    }

    /**
     * Verifica que una estadística nula no se pueda consultar ni mejorar.
     *
     * @throws ExcepcionDOPOSoccer si la mejora no es válida
     */
    @Test
    void unaEstadisticaNulaNoSePuedeMejorar() throws ExcepcionDOPOSoccer {
        PerfilJugador perfil = perfil(5, 5, 5, 5, 10000);

        assertFalse(perfil.puedeMejorar(null));
        assertFalse(perfil.mejorar(null));
        assertEquals(10000, perfil.getPuntos().getTotal());
    }

    /**
     * Verifica que el contador de mejoras nunca sea negativo.
     *
     * @throws ExcepcionDOPOSoccer si el perfil no es válido
     */
    @Test
    void elContadorDeMejorasNuncaEsNegativo() throws ExcepcionDOPOSoccer {
        PerfilJugador perfil = perfil(1, 1, 1, 1, 0);

        assertEquals(0, perfil.getMejorasCompradas());
    }

    /**
     * Verifica que los desbloqueos con nombre nulo no existan.
     *
     * @throws ExcepcionDOPOSoccer si el perfil no es válido
     */
    @Test
    void losDesbloqueosConNombreNuloNoExisten() throws ExcepcionDOPOSoccer {
        PerfilJugador perfil = perfil(1, 1, 1, 1, 0);

        assertFalse(perfil.estaDesbloqueadoPersonaje(null));
        assertFalse(perfil.estaDesbloqueadoBalon(null));
        assertFalse(perfil.estaDesbloqueadoEscenario(null));
        assertFalse(perfil.desbloquearPersonaje(null));
        assertFalse(perfil.desbloquearBalon(null));
        assertFalse(perfil.desbloquearEscenario(null));
    }

    /**
     * Verifica que cambiar el nombre conserve puntos y estadísticas.
     *
     * @throws ExcepcionDOPOSoccer si el nombre no es válido
     */
    @Test
    void cambiarElNombreConservaPuntosYEstadisticas()
            throws ExcepcionDOPOSoccer {
        PerfilJugador perfil = perfil(1, 1, 1, 1, 600);
        assertTrue(perfil.mejorar(TipoEstadistica.VELOCIDAD));

        perfil.setNombre("Beatriz");

        assertEquals("Beatriz", perfil.getNombre());
        assertEquals("Beatriz", perfil.getConfiguracion().getNombre());
        assertEquals(2, perfil.getEstadisticas().getSpeed());
        assertEquals(100, perfil.getPuntos().getTotal());
        assertThrows(ExcepcionDOPOSoccer.class,
                () -> perfil.setNombre("   "));
    }
}
