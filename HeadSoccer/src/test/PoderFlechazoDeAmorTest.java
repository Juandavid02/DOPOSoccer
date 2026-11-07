package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import domain.PoderFlechazoDeAmor;

/**
 * Clase de pruebas PoderFlechazoDeAmorTest.
 * Verifica los datos del poder de Betty la fea (Colombia).
 *
 * @author Juan David Rojas Heredia
 * @version 06-11-2026
 */
public class PoderFlechazoDeAmorTest
{
    private PoderFlechazoDeAmor poder;

    public PoderFlechazoDeAmorTest()
    {
    }

    @BeforeEach
    public void setUp()
    {
        poder = new PoderFlechazoDeAmor();
    }

    /** El nombre visible del poder es "Flechazo de Amor". */
    @Test
    public void shouldHaveExpectedName()
    {
        assertEquals("Flechazo de Amor", poder.getNombre());
    }

    /** La duración del efecto es de 2.5 segundos. */
    @Test
    public void shouldHaveExpectedDuration()
    {
        assertEquals(2.5, poder.getDuracion(), 0.0001);
    }

    /** El daño base del tiro es 30. */
    @Test
    public void shouldHaveExpectedBaseDamage()
    {
        // El daño base todavía no depende del jugador.
        assertEquals(30.0, poder.getDanoTiro(null), 0.0001);
    }

    @AfterEach
    public void tearDown()
    {
    }
}
