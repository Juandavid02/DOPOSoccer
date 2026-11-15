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
                new Personaje("Jungkook", "Corea del Sur", new PoderAuraAzul()),
                new Personaje("Goku", "Japón", new PoderRayo()),
                new Personaje("Mario", "Italia", new PoderTornado()),
                new Personaje("Bob Esponja", "Estados Unidos", new PoderIlusion()),
                new Personaje("Betty la fea", "Colombia", new PoderBesoDeLaFea()),
                new Personaje("El Chavo del Ocho", "México", new PoderBarril())
        );
    }
}
