package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import domain.Vida;

/**
 * Clase de pruebas VidaTest.
 * Pruebas de la vida de un jugador: inicio, daño, límite inferior y reinicio.
 */
public class VidaTest
{
    private Vida vida;

    public VidaTest()
    {
    }

    @BeforeEach
    public void setUp()
    {
        vida = new Vida(120);
    }

    /** La vida inicia llena y con el máximo solicitado. */
    @Test
    public void shouldStartWithFullLife()
    {
        assertEquals(120, vida.getActual(), 0.0001);
        assertEquals(120, vida.getMaxima(), 0.0001);
        assertFalse(vida.estaEnCero());
    }

    /** La vida máxima debe ser mayor que cero. */
    @Test
    public void shouldNotCreateWithNonPositiveMaxLife()
    {
        assertThrows(IllegalArgumentException.class, () -> new Vida(0));
        assertThrows(IllegalArgumentException.class, () -> new Vida(-10));
    }

    /** Recibir daño descuenta la vida actual. */
    @Test
    public void shouldReduceLifeWhenReceivingDamage()
    {
        vida.recibirDanio(30);

        assertEquals(90, vida.getActual(), 0.0001);
        assertEquals(120, vida.getMaxima(), 0.0001);
        assertFalse(vida.estaEnCero());
    }

    /** La vida nunca puede quedar por debajo de cero. */
    @Test
    public void shouldNeverGoBelowZero()
    {
        vida.recibirDanio(500);

        assertEquals(0, vida.getActual(), 0.0001);
        assertTrue(vida.estaEnCero());
    }

    /** El daño negativo no es válido. */
    @Test
    public void shouldNotAcceptNegativeDamage()
    {
        assertThrows(IllegalArgumentException.class, () -> vida.recibirDanio(-1));
    }

    /** Reiniciar devuelve la vida a su máximo. */
    @Test
    public void shouldResetToMaxLife()
    {
        vida.recibirDanio(80);
        vida.reiniciar();

        assertEquals(120, vida.getActual(), 0.0001);
        assertEquals(120, vida.getMaxima(), 0.0001);
        assertFalse(vida.estaEnCero());
    }

    @AfterEach
    public void tearDown()
    {
    }
}
