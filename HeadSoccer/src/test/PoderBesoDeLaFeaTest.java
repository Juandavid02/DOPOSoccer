package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import domain.PoderBesoDeLaFea;

/**
 * Clase de pruebas PoderBesoDeLaFeaTest.
 * Verifica los datos del poder de Betty la fea (Colombia).
 */
public class PoderBesoDeLaFeaTest
{
    private PoderBesoDeLaFea poder;

    /**
     * Constructor de la clase de pruebas PoderBesoDeLaFeaTest.
     */
    public PoderBesoDeLaFeaTest()
    {
    }

    /**
     * Prepara el escenario de pruebas.
     */
    @BeforeEach
    public void setUp()
    {
        poder = new PoderBesoDeLaFea();
    }

    /** El nombre visible del poder es "Beso de la Fea". */
    @Test
    public void shouldHaveExpectedName()
    {
        assertEquals("Beso de la Fea", poder.getNombre());
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

    /**
     * Libera el escenario de pruebas.
     */
    @AfterEach
    public void tearDown()
    {
    }
}
