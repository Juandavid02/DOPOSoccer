package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import domain.Personaje;
import domain.PoderConfigurado;

/**
 * Clase de pruebas PersonajeTest.
 * Pruebas de los datos de un personaje: nombre, país y poder.
 * Las estadísticas ya no pertenecen al personaje, sino al jugador.
 */
public class PersonajeTest
{
    private Personaje personaje;

    /**
     * Constructor de la clase de pruebas PersonajeTest.
     */
    public PersonajeTest()
    {
    }

    /**
     * Prepara el escenario de pruebas.
     */
    @BeforeEach
    public void setUp()
    {
        personaje = new Personaje("Polar", "Canadá", PoderConfigurado.auraAzul());
    }

    /** Se puede crear un personaje con todos los datos válidos. */
    @Test
    public void shouldCreateCharacterWithValidData()
    {
        assertNotNull(personaje);
    }

    /** El nombre se conserva tal como se recibió. */
    @Test
    public void shouldReturnRequestedName()
    {
        assertEquals("Polar", personaje.getNombre());
    }

    /** El país se conserva tal como se recibió. */
    @Test
    public void shouldReturnRequestedCountry()
    {
        assertEquals("Canadá", personaje.getPais());
    }

    /** El poder es el mismo que se recibió. */
    @Test
    public void shouldReturnRequestedPower()
    {
        assertNotNull(personaje.getPoder());
        assertEquals("Aura Azul", personaje.getPoder().getNombre());
    }

    /** El nombre no puede ser nulo. */
    @Test
    public void shouldNotCreateCharacterWithNullName()
    {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> new Personaje(null, "Canadá", PoderConfigurado.auraAzul()));
        assertEquals("El nombre no puede ser nulo o vacío", e.getMessage());
    }

    /** El nombre no puede estar vacío. */
    @Test
    public void shouldNotCreateCharacterWithEmptyName()
    {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> new Personaje("", "Canadá", PoderConfigurado.auraAzul()));
        assertEquals("El nombre no puede ser nulo o vacío", e.getMessage());
    }

    /** El nombre no puede estar formado solo por espacios. */
    @Test
    public void shouldNotCreateCharacterWithBlankName()
    {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> new Personaje("   ", "Canadá", PoderConfigurado.auraAzul()));
        assertEquals("El nombre no puede ser nulo o vacío", e.getMessage());
    }

    /** El país no puede ser nulo. */
    @Test
    public void shouldNotCreateCharacterWithNullCountry()
    {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> new Personaje("Polar", null, PoderConfigurado.auraAzul()));
        assertEquals("El país no puede ser nulo o vacío", e.getMessage());
    }

    /** El país no puede estar vacío. */
    @Test
    public void shouldNotCreateCharacterWithEmptyCountry()
    {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> new Personaje("Polar", "", PoderConfigurado.auraAzul()));
        assertEquals("El país no puede ser nulo o vacío", e.getMessage());
    }

    /** El poder no puede ser nulo. */
    @Test
    public void shouldNotCreateCharacterWithNullPower()
    {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> new Personaje("Polar", "Canadá", null));
        assertEquals("El poder no puede ser nulo", e.getMessage());
    }

    /**
     * Libera el escenario de pruebas.
     */
    @AfterEach
    public void tearDown()
    {
    }
}
