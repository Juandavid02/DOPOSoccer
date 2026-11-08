package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import domain.PoderBarril;

/**
 * Clase de pruebas PoderBarrilTest.
 * Verifica los datos del poder de El Chavo del Ocho (México).
 */
public class PoderBarrilTest
{
    private PoderBarril poder;

    public PoderBarrilTest()
    {
    }

    @BeforeEach
    public void setUp()
    {
        poder = new PoderBarril();
    }

    /** El nombre visible del poder es "Barril". */
    @Test
    public void shouldHaveExpectedName()
    {
        assertEquals("Barril", poder.getNombre());
    }

    /** La duración del bloqueo es de 5 segundos. */
    @Test
    public void shouldHaveExpectedDuration()
    {
        assertEquals(5.0, poder.getDuracion(), 0.0001);
    }

    /** El daño base del tiro es 35. */
    @Test
    public void shouldHaveExpectedBaseDamage()
    {
        // El daño base todavía no depende del jugador.
        assertEquals(35.0, poder.getDanoTiro(null), 0.0001);
    }

    @AfterEach
    public void tearDown()
    {
    }
}
