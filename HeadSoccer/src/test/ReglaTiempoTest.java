package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import domain.ConfiguracionJugador;
import domain.ConfiguracionPartido;
import domain.Estadisticas;
import domain.EstadoPartido;
import domain.ExcepcionDOPOSoccer;
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
 * Pruebas de la regla de victoria por tiempo y muerte súbita.
 */
public class ReglaTiempoTest
{
    private Partido partido;

    /**
     * Constructor de la clase de pruebas ReglaTiempoTest.
     */
    public ReglaTiempoTest()
    {
    }

    /**
     * Prepara el escenario de pruebas.
     */
    @BeforeEach
    public void setUp()
    {
        partido = crearPartido();
    }

    /** Mientras quede tiempo, el partido no termina. */
    @Test
    public void shouldNotFinishWithTimeRemaining()
    {
        ReglaTiempo regla = new ReglaTiempo();

        assertFalse(regla.terminado(partido));
    }

    /** Sin empate, el partido termina cuando se agota el tiempo. */
    @Test
    public void shouldFinishWithoutTieWhenTimeRunsOut()
    {
        partido.sumarGol(1);
        partido.actualizar(60);

        assertEquals(EstadoPartido.TERMINADO, partido.getEstado());
        assertEquals(MotivoFin.TIEMPO, partido.getResultado().getMotivoFin());
    }

    /** Con empate al agotarse el tiempo, se activa la muerte súbita. */
    @Test
    public void shouldEnterSuddenDeathWhenTied()
    {
        partido.actualizar(60);

        assertEquals(EstadoPartido.EN_CURSO, partido.getEstado());
        assertTrue(((ReglaTiempo) partido.getRegla()).estaEnGolDeOro());
    }

    /** El primer gol en muerte súbita termina el partido. */
    @Test
    public void shouldFinishByGoldenGoal()
    {
        partido.actualizar(60);
        partido.sumarGol(1);
        partido.actualizar(0.016);

        assertEquals(EstadoPartido.TERMINADO, partido.getEstado());
        assertEquals(MotivoFin.GOL_DE_ORO, partido.getResultado().getMotivoFin());
    }

    /**
     * Crea un partido de prueba con la regla de tiempo.
     *
     * @return partido configurado para las pruebas
     */
    private Partido crearPartido()
    {
        try {
            ConfiguracionPartido configuracion = new ConfiguracionPartido(
                    Modalidad.PvsP,
                    TipoPartido.TIEMPO,
                    Resolucion.R1280x720,
                    1,
                    5
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
        return new Personaje("Prueba", "Prueba", new PoderAuraAzul());
    }

    /**
     * Libera el escenario de pruebas.
     */
    @AfterEach
    public void tearDown()
    {
    }
}
