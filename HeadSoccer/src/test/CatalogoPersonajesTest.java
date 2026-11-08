package test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import domain.CatalogoPersonajes;
import domain.Personaje;

/**
 * Clase de pruebas CatalogoPersonajesTest.
 * Verifica los seis personajes disponibles, sus países y sus poderes.
 *
 * @author Juan David Rojas Heredia
 * @version 06-11-2026
 */
public class CatalogoPersonajesTest
{
    private List<Personaje> personajes;

    public CatalogoPersonajesTest()
    {
    }

    @BeforeEach
    public void setUp()
    {
        personajes = CatalogoPersonajes.getPersonajes();
    }

    /** El catálogo devuelve exactamente seis personajes. */
    @Test
    public void shouldReturnSixCharacters()
    {
        assertEquals(6, personajes.size());
    }

    /** No puede haber dos personajes del mismo país. */
    @Test
    public void shouldHaveUniqueCountries()
    {
        Set<String> paises = new HashSet<>();
        for (Personaje personaje : personajes) {
            assertTrue(paises.add(personaje.getPais()),
                    "País duplicado: " + personaje.getPais());
        }
        assertEquals(6, paises.size());
    }

    /** Cada personaje tiene el país y el poder esperados. */
    @Test
    public void shouldContainExpectedCharactersAndPowers()
    {
        assertCharacter("Jungkook", "Corea del Sur", "Aura Azul");
        assertCharacter("Goku", "Japón", "Rayo");
        assertCharacter("Mario", "Italia", "Tornado");
        assertCharacter("Bob Esponja", "Estados Unidos", "Ilusión");
        assertCharacter("Betty la fea", "Colombia", "Flechazo de Amor");
        assertCharacter("El Chavo del Ocho", "México", "Barril");
    }

    /** El catálogo entrega una lista nueva en cada llamada. */
    @Test
    public void shouldReturnANewListOnEachCall()
    {
        assertNotSame(CatalogoPersonajes.getPersonajes(),
                CatalogoPersonajes.getPersonajes());
    }

    /** La lista del catálogo no puede modificarse desde afuera. */
    @Test
    public void shouldReturnAnImmutableList()
    {
        assertThrows(UnsupportedOperationException.class,
                () -> CatalogoPersonajes.getPersonajes().clear());
    }

    private void assertCharacter(String nombre, String pais, String poder)
    {
        Personaje encontrado = buscarPorNombre(nombre);
        assertNotNull(encontrado, "No se encontró el personaje: " + nombre);
        assertEquals(pais, encontrado.getPais());
        assertEquals(poder, encontrado.getPoder().getNombre());
    }

    private Personaje buscarPorNombre(String nombre)
    {
        for (Personaje personaje : personajes) {
            if (personaje.getNombre().equals(nombre)) {
                return personaje;
            }
        }
        return null;
    }

    @AfterEach
    public void tearDown()
    {
    }
}
