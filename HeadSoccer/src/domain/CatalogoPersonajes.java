package domain;

import java.util.List;


public class CatalogoPersonajes {
    /**
     * Constructor privado para evitar instancias del catálogo.
     */
    private CatalogoPersonajes() {
    }

    /**
     * Devuelve la lista de personajes disponibles.
     *
     * @return lista de personajes
     */
    public static List<Personaje> getPersonajes() {
        return List.of(
                new Personaje("Jungkook", "Corea del Sur", PoderConfigurado.auraAzul()),
                new Personaje("Goku", "Japón", PoderConfigurado.rayo()),
                new Personaje("Mario", "Italia", PoderConfigurado.tornado()),
                new Personaje("Bob Esponja", "Estados Unidos", PoderConfigurado.ilusion()),
                new Personaje("Betty la fea", "Colombia", PoderConfigurado.besoDeLaFea()),
                new Personaje("El Chavo del Ocho", "México", PoderConfigurado.barril())
        );
    }
}
