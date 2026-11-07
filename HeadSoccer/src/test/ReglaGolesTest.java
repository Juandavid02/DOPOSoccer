package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import domain.ConfiguracionJugador;
import domain.ConfiguracionPartido;
import domain.Estadisticas;
import domain.Modalidad;
import domain.MotivoFin;
import domain.Partido;
import domain.Personaje;
import domain.PoderAuraAzul;
import domain.ReglaGoles;
import domain.Resolucion;
import domain.TipoPartido;

/**
 * Clase de pruebas ReglaGolesTest.
 * Pruebas de la regla de victoria por goles.
 *
 * @author Juan David Rojas Heredia
 * @version 06-11-2026
 */
public class ReglaGolesTest
{
    private Partido partido;
    private ReglaGoles regla;

    public ReglaGolesTest()
    {
    }

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

    private Partido crearPartido(int goles)
    {
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
    }

    private Personaje crearPersonaje()
    {
        return new Personaje("Prueba", "Prueba", new PoderAuraAzul());
    }

    @AfterEach
    public void tearDown()
    {
    }
}
