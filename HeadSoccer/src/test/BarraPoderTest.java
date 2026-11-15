package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import domain.BarraPoder;

/**
 * Clase de pruebas BarraPoderTest.
 * Pruebas de la barra de poder: estado inicial, recarga, tope,
 * consumo y validaciones del constructor.
 */
public class BarraPoderTest
{
    private BarraPoder barra;

    /**
     * Constructor de la clase de pruebas BarraPoderTest.
     */
    public BarraPoderTest()
    {
    }

    /**
     * Prepara el escenario de pruebas.
     * Se ejecuta antes de cada método de prueba.
     */
    @BeforeEach
    public void setUp()
    {
        barra = new BarraPoder(100, 10);
    }

    /** La barra inicia vacía. */
    @Test
    public void shouldStartEmpty()
    {
        assertEquals(0, barra.getCarga(), 0.0001);
    }

    /** El modelo informa la capacidad y la recarga con las que se creó. */
    @Test
    public void shouldReturnCapacityAndRechargeRate()
    {
        assertEquals(100, barra.getCapacidad(), 0.0001);
        assertEquals(10, barra.getRecargaPorSegundo(), 0.0001);
    }

    /** Con 5 segundos y recarga de 10 por segundo, la carga es 50. */
    @Test
    public void shouldIncreaseChargeOverTime()
    {
        barra.actualizarBarra(5);
        assertEquals(50, barra.getCarga(), 0.0001);
    }

    /** La barra queda llena exactamente al alcanzar la capacidad. */
    @Test
    public void shouldReachFullCharge()
    {
        barra.actualizarBarra(10);
        assertTrue(barra.isFull());
    }

    /** La barra no está llena antes de alcanzar la capacidad. */
    @Test
    public void shouldNotBeFullBeforeCapacity()
    {
        barra.actualizarBarra(9.9);
        assertFalse(barra.isFull());
    }

    /** La carga nunca supera la capacidad. */
    @Test
    public void shouldNotExceedCapacity()
    {
        barra.actualizarBarra(20);
        assertEquals(100, barra.getCarga(), 0.0001);
    }

    /** No se puede consumir si la barra no está llena. */
    @Test
    public void shouldNotConsumeWhenNotFull()
    {
        barra.actualizarBarra(5);
        assertFalse(barra.consumir());
        assertEquals(50, barra.getCarga(), 0.0001);
    }

    /** Al consumir una barra llena, la carga vuelve a cero. */
    @Test
    public void shouldConsumeWhenFull()
    {
        barra.actualizarBarra(10);
        assertTrue(barra.consumir());
        assertEquals(0, barra.getCarga(), 0.0001);
    }

    /** Reiniciar deja la carga en cero. */
    @Test
    public void shouldResetCharge()
    {
        barra.actualizarBarra(5);
        barra.reiniciar();
        assertEquals(0, barra.getCarga(), 0.0001);
    }

    /** La capacidad debe ser mayor que cero. */
    @Test
    public void shouldNotCreateWithInvalidCapacity()
    {
        try {
            new BarraPoder(0, 10);
            fail("Debía lanzar IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("La capacidad debe ser mayor que 0", e.getMessage());
        }

        try {
            new BarraPoder(-1, 10);
            fail("Debía lanzar IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("La capacidad debe ser mayor que 0", e.getMessage());
        }
    }

    /** La recarga por segundo no puede ser negativa. */
    @Test
    public void shouldNotCreateWithNegativeRecharge()
    {
        try {
            new BarraPoder(100, -1);
            fail("Debía lanzar IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("La recarga por segundo no puede ser negativa", e.getMessage());
        }
    }

    /** El delta de tiempo debe ser mayor que cero. */
    @Test
    public void shouldNotUpdateWithNonPositiveDelta()
    {
        try {
            barra.actualizarBarra(0);
            fail("Debía lanzar IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("El tiempo delta no puede ser negativo ni cero", e.getMessage());
        }

        try {
            barra.actualizarBarra(-1);
            fail("Debía lanzar IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("El tiempo delta no puede ser negativo ni cero", e.getMessage());
        }
    }

    /**
     * Libera el escenario de pruebas.
     * Se ejecuta después de cada método de prueba.
     */
    @AfterEach
    public void tearDown()
    {
    }
}
