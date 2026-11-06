package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import domain.Estadisticas;
import domain.Personaje;
import domain.Poder;
import domain.Jugador;

/**
 * Clase de pruebas PersonajeTest.
 * Pruebas de los datos de un personaje: creación, getters y
 * validaciones del constructor.
 *
 * @author Juan David Rojas Heredia
 * @version 05-11-2026
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
     * Se ejecuta antes de cada método de prueba.
     */
    @BeforeEach
    public void setUp()
    {
        personaje = new Personaje("Polar", "Canadá",
                new Estadisticas(3, 3, 4), new PoderFake());
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

    /** Las estadísticas son las mismas que se recibieron. */
    @Test
    public void shouldReturnRequestedStatistics()
    {
        assertEquals(3, personaje.getEstadisticas().getVelocidad());
        assertEquals(3, personaje.getEstadisticas().getSalto());
        assertEquals(4, personaje.getEstadisticas().getFuerza());
    }

    /** El poder es el mismo que se recibió. */
    @Test
    public void shouldReturnRequestedPower()
    {
        assertNotNull(personaje.getPoder());
        assertEquals("Prueba", personaje.getPoder().getNombre());
    }

    /** El nombre no puede ser nulo. */
    @Test
    public void shouldNotCreateCharacterWithNullName()
    {
        try {
            new Personaje(null, "Canadá", new Estadisticas(3, 3, 4), new PoderFake());
            fail("Debía lanzar IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("El nombre no puede ser nulo o vacío", e.getMessage());
        }
    }

    /** El nombre no puede estar vacío. */
    @Test
    public void shouldNotCreateCharacterWithEmptyName()
    {
        try {
            new Personaje("", "Canadá", new Estadisticas(3, 3, 4), new PoderFake());
            fail("Debía lanzar IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("El nombre no puede ser nulo o vacío", e.getMessage());
        }
    }

    /** El nombre no puede estar formado solo por espacios. */
    @Test
    public void shouldNotCreateCharacterWithBlankName()
    {
        try {
            new Personaje("   ", "Canadá", new Estadisticas(3, 3, 4), new PoderFake());
            fail("Debía lanzar IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("El nombre no puede ser nulo o vacío", e.getMessage());
        }
    }

    /** El país no puede ser nulo. */
    @Test
    public void shouldNotCreateCharacterWithNullCountry()
    {
        try {
            new Personaje("Polar", null, new Estadisticas(3, 3, 4), new PoderFake());
            fail("Debía lanzar IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("El país no puede ser nulo o vacío", e.getMessage());
        }
    }

    /** El país no puede estar vacío. */
    @Test
    public void shouldNotCreateCharacterWithEmptyCountry()
    {
        try {
            new Personaje("Polar", "", new Estadisticas(3, 3, 4), new PoderFake());
            fail("Debía lanzar IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("El país no puede ser nulo o vacío", e.getMessage());
        }
    }

    /** Las estadísticas no pueden ser nulas. */
    @Test
    public void shouldNotCreateCharacterWithNullStatistics()
    {
        try {
            new Personaje("Polar", "Canadá", null, new PoderFake());
            fail("Debía lanzar IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Las estadísticas no pueden ser nulas", e.getMessage());
        }
    }

    /** El poder no puede ser nulo. */
    @Test
    public void shouldNotCreateCharacterWithNullPower()
    {
        try {
            new Personaje("Polar", "Canadá", new Estadisticas(3, 3, 4), null);
            fail("Debía lanzar IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("El poder no puede ser nulo", e.getMessage());
        }
    }

    /**
     * Poder falso para las pruebas de Personaje.
     */
    private static class PoderFake implements Poder
    {
        @Override
        public String getNombre()
        {
            return "Prueba";
        }

        @Override
        public double getDuracion()
        {
            return 0.0;
        }

        @Override
        public void aplicarPoder(Jugador jugador)
        {
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
