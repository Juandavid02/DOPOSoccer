package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import domain.ConfiguracionJugador;
import domain.ConfiguracionPartido;
import domain.Estadisticas;
import domain.EstadoPartido;
import domain.Modalidad;
import domain.MotivoFin;
import domain.Partido;
import domain.Personaje;
import domain.PoderAuraAzul;
import domain.ReglaTiempo;
import domain.Resolucion;
import domain.TipoPartido;

/**
 * Clase de pruebas ReglaTiempoTest.
 * Verifica la regla de victoria por tiempo y la muerte súbita.
 *
 * @author Juan David Rojas Heredia
 * @version 06-11-2026
 */
public class ReglaTiempoTest
{
    private Partido partido;
    private ReglaTiempo regla;

    public ReglaTiempoTest()
    {
    }

    @BeforeEach
    public void setUp()
    {
        partido = crearPartido();
        regla = new ReglaTiempo();
    }

    /** Mientras quede tiempo, el partido no termina. */
    @Test
    public void shouldNotFinishWhileTimeRemains()
    {
        assertFalse(regla.terminado(partido));
    }

    /** Sin empate, termina al agotarse el tiempo. */
    @Test
    public void shouldFinishWithoutTieWhenTimeRunsOut()
    {
        partido.sumarGol(1);
        partido.actualizar(60);

        assertTrue(regla.terminado(partido));
        assertEquals(MotivoFin.TIEMPO, regla.getMotivo());
    }

    /** Con empate, se activa la muerte súbita y el partido sigue. */
    @Test
    public void shouldEnterSuddenDeathWhenTied()
    {
        partido.actualizar(60);

        assertEquals(EstadoPartido.EN_CURSO, partido.getEstado());
        assertFalse(regla.terminado(partido));
        assertTrue(regla.estaEnGolDeOro());
    }

    /** Un gol en muerte súbita termina el partido con GOL_DE_ORO. */
    @Test
    public void shouldFinishByGoldenGoal()
    {
        partido.actualizar(60);
        regla.terminado(partido); // activa la muerte súbita en la regla de prueba

        partido.sumarGol(1);

        assertTrue(regla.terminado(partido));
        assertEquals(MotivoFin.GOL_DE_ORO, regla.getMotivo());
    }

    private Partido crearPartido()
    {
        ConfiguracionPartido configuracion = new ConfiguracionPartido(
                Modalidad.PvsP, TipoPartido.TIEMPO, Resolucion.R1280x720, 1, 5);

        return new Partido(
                configuracion,
                crearPersonaje(),
                crearPersonaje(),
                new ConfiguracionJugador("Ana", new Estadisticas(1, 1, 1, 1)),
                new ConfiguracionJugador("Luis", new Estadisticas(1, 1, 1, 1))
        );
    }

    private Personaje crearPersonaje()
    {
        return new Personaje("Polar", "Canadá", new PoderAuraAzul());
    }

    @AfterEach
    public void tearDown()
    {
    }
}
