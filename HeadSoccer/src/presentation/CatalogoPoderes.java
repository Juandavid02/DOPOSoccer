package presentation;

import domain.Poder;
import domain.TipoEfecto;

import java.awt.Color;

/**
 * Relaciona cada poder del dominio con sus imágenes y colores.
 */
final class CatalogoPoderes {

    private static final String RUTA_PODERES = "resources/images/Poderes/";
    private static final String RUTA_EFECTOS = "resources/images/Efectos/";

    private CatalogoPoderes() {
    }

    static String rutaAura(Poder poder) {
        if (poder == null) {
            return null;
        }
        switch (poder.getNombre()) {
            case "Aura Azul":
                return RUTA_PODERES + "AuraAzul.png";
            case "Rayo":
                return RUTA_PODERES + "AuraRayo.png";
            case "Tornado":
                return RUTA_PODERES + "AuraTornado.png";
            case "Ilusión":
                return RUTA_PODERES + "AuraIlusion.png";
            case "Flechazo de Amor":
                return RUTA_PODERES + "AuradeAmor.png";
            case "Barril":
                return RUTA_PODERES + "AuraBarril.png";
            default:
                return null;
        }
    }

    static String rutaTiro(Poder poder) {
        if (poder == null) {
            return null;
        }
        switch (poder.getNombre()) {
            case "Aura Azul":
                return RUTA_PODERES + "PoderAuraAzul.png";
            case "Rayo":
                return RUTA_PODERES + "PoerdRayo.png";
            case "Tornado":
                return RUTA_PODERES + "PoderTornado.png";
            case "Ilusión":
                return RUTA_PODERES + "PoderIlusion.png";
            case "Flechazo de Amor":
                return RUTA_PODERES + "PoderAmor.png";
            case "Barril":
                return RUTA_PODERES + "PoderBarril.png";
            default:
                return null;
        }
    }

    static String rutaEfecto(TipoEfecto tipo) {
        if (tipo == null) {
            return null;
        }
        switch (tipo) {
            case EMPUJE:
                return RUTA_EFECTOS + "EfectoAuraAzul.png";
            case ELECTROCUTADO:
                return RUTA_EFECTOS + "EfectoRayo.png";
            case INCONSCIENTE:
                return RUTA_EFECTOS + "EfectoTornado.png";
            case CONFUNDIDO:
                return RUTA_EFECTOS + "EfectoIlusion.png";
            case CONTROLES_INVERTIDOS:
                return RUTA_EFECTOS + "EfectoAmor.png";
            case BARRIL:
                return RUTA_EFECTOS + "EfectoBarril.png";
            default:
                return null;
        }
    }

    static Color colorEfecto(TipoEfecto tipo) {
        if (tipo == null) {
            return Color.WHITE;
        }
        switch (tipo) {
            case EMPUJE:
                return new Color(70, 150, 255);
            case ELECTROCUTADO:
                return new Color(255, 225, 70);
            case INCONSCIENTE:
                return new Color(160, 220, 120);
            case CONFUNDIDO:
                return new Color(200, 120, 255);
            case CONTROLES_INVERTIDOS:
                return new Color(255, 110, 160);
            case BARRIL:
                return new Color(205, 145, 70);
            default:
                return Color.WHITE;
        }
    }

    static Color colorPoder(Poder poder) {
        if (poder == null) {
            return Color.WHITE;
        }
        switch (poder.getNombre()) {
            case "Aura Azul":
                return new Color(70, 150, 255);
            case "Rayo":
                return new Color(255, 225, 70);
            case "Tornado":
                return new Color(160, 220, 120);
            case "Ilusión":
                return new Color(200, 120, 255);
            case "Flechazo de Amor":
                return new Color(255, 110, 160);
            case "Barril":
                return new Color(205, 145, 70);
            default:
                return Color.WHITE;
        }
    }
}
