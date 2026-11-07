package domain;

import java.util.List;


public class CatalogoPersonajes {
    private CatalogoPersonajes() {
    }

    public static List<Personaje> getPersonajes() {
        return List.of(
                new Personaje("Jungkook", "Corea del Sur", new PoderAuraAzul()),
                new Personaje("Goku", "Japón", new PoderRayo()),
                new Personaje("Mario", "Italia", new PoderTornado()),
                new Personaje("Mickey Mouse", "Estados Unidos", new PoderIlusion()),
                new Personaje("Betty la fea", "Colombia", new PoderFlechazoDeAmor()),
                new Personaje("El Chavo del Ocho", "México", new PoderBarril())
        );
    }
}
