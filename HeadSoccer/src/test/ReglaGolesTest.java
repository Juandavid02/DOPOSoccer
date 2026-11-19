package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import domain.ConfiguracionJugador;
import domain.ConfiguracionPartido;
import domain.Estadisticas;
import domain.ExcepcionDOPOSoccer;
import domain.Modalidad;
import domain.MotivoFin;
import domain.Partido;
import domain.Personaje;
import domain.PoderConfigurado;
import domain.ReglaGoles;
import domain.Resolucion;
import domain.TipoPartido;

/**
 * Clase de pruebas ReglaGolesTest.
 * Pruebas de la regla de victoria por goles.
 */
public class ReglaGolesTest
{
    private Partido partido;
    private ReglaGoles regla;

    /**
     * Constructor de la clase de pruebas ReglaGolesTest.
     */
    public ReglaGolesTest()
    {
    }

    /**
     * Prepara el escenario de pruebas.
     */
    @BeforeEach
    public void setUp()
    {
        partido = crearPartido(3);
        regla = new ReglaGoles(3);
    }

    /** El partido no termina antes de alcanzar el objetivo. */
    @Test
    public void shouldNotFinishBeforeTarget()
    {
        partido.sumarGol(1);
        partido.sumarGol(1);

        assertFalse(regla.terminado(partido));
    }

    /** El partido termina cuando el jugador 1 llega al objetivo. */
    @Test
    public void shouldFinishWhenPlayerOneReachesTarget()
    {
        partido.sumarGol(1);
        partido.sumarGol(1);
        partido.sumarGol(1);

        assertTrue(regla.terminado(partido));
        assertEquals(MotivoFin.GOLES, regla.getMotivo());
    }

    /** El partido termina cuando el jugador 2 llega al objetivo. */
    @Test
    public void shouldFinishWhenPlayerTwoReachesTarget()
    {
        partido.sumarGol(2);
        partido.sumarGol(2);
        partido.sumarGol(2);

        assertTrue(regla.terminado(partido));
        assertEquals(MotivoFin.GOLES, regla.getMotivo());
    }

    /** El objetivo de goles debe ser mayor que cero. */
    @Test
    public void shouldRejectNonPositiveTarget()
    {
        assertThrows(IllegalArgumentException.class, () -> new ReglaGoles(0));
        assertThrows(IllegalArgumentException.class, () -> new ReglaGoles(-1));
    }

    /**
     * Crea un partido de prueba con el objetivo de goles indicado.
     *
     * @param goles goles necesarios para ganar
     * @return partido configurado para las pruebas
     */
    private Partido crearPartido(int goles)
    {
        try {
            ConfiguracionPartido configuracion = new ConfiguracionPartido(
                    Modalidad.PvsP,
                    TipoPartido.GOLES,
                    Resolucion.R1280x720,
                    1,
                    goles
            );

            return new Partido(
                    configuracion,
                    crearPersonaje(),
                    crearPersonaje(),
                    new ConfiguracionJugador("A", new Estadisticas(1, 1, 1, 1)),
                    new ConfiguracionJugador("B", new Estadisticas(1, 1, 1, 1))
            );
        } catch (ExcepcionDOPOSoccer e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Crea un personaje de prueba.
     *
     * @return personaje de prueba
     */
    private Personaje crearPersonaje()
    {
        return new Personaje("Prueba", "Prueba", PoderConfigurado.auraAzul());
    }

    /**
     * Libera el escenario de pruebas.
     */
    @AfterEach
    public void tearDown()
    {
    }
}
