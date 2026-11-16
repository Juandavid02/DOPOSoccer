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

    /**
     * Constructor privado de la clase de utilidades.
     */
    private CatalogoPoderes() {
    }

    /**
     * Devuelve la imagen del aura de un poder.
     *
     * @param poder poder consultado
     * @return la ruta del aura o null si no tiene
     */
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
            case "Beso de la Fea":
                return RUTA_PODERES + "AuradeAmor.png";
            case "Barril":
                return RUTA_PODERES + "AuraBarril.png";
            default:
                return null;
        }
    }

    /**
     * Devuelve la imagen del disparo de un poder.
     *
     * @param poder poder consultado
     * @return la ruta del disparo o null si no tiene
     */
    static String rutaTiro(Poder poder) {
        if (poder == null) {
            return null;
        }
        switch (poder.getNombre()) {
            case "Aura Azul":
                return RUTA_PODERES + "PoderAuraAzul.png";
            case "Rayo":
                return RUTA_PODERES + "PoderRayo.png";
            case "Tornado":
                return RUTA_PODERES + "PoderTornado.png";
            case "Ilusión":
                return RUTA_PODERES + "PoderIlusion.png";
            case "Beso de la Fea":
                return RUTA_PODERES + "PoderAmor.png";
            case "Barril":
                return RUTA_PODERES + "PoderBarril.png";
            default:
                return null;
        }
    }

    /**
     * Imagen que se muestra dentro del círculo de poder del marcador.
     *
     * @param poder poder del que se obtiene la imagen
     * @return la ruta de la imagen del poder
     */
    static String rutaEmoji(Poder poder) {
        if (poder == null) {
            return null;
        }
        switch (poder.getNombre()) {
            case "Aura Azul":
                return RUTA_PODERES + "EmojiAuraAzul.png";
            case "Rayo":
                return RUTA_PODERES + "EmojiRayo.png";
            case "Tornado":
                return RUTA_PODERES + "EmojiTornado.png";
            case "Ilusión":
                return RUTA_PODERES + "EmojiIlusion.png";
            case "Beso de la Fea":
                return RUTA_PODERES + "EmojiAmor.png";
            case "Barril":
                return RUTA_PODERES + "EmojiBarril.png";
            default:
                return null;
        }
    }

    /**
     * Devuelve la imagen del efecto de un tipo de efecto.
     *
     * @param tipo tipo de efecto consultado
     * @return la ruta del efecto o null si no tiene
     */
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

    /**
     * Devuelve el color asociado a un tipo de efecto.
     *
     * @param tipo tipo de efecto consultado
     * @return el color del efecto
     */
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
                return new Color(165, 190, 215);
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

    /**
     * Devuelve el color asociado a un poder.
     *
     * @param poder poder consultado
     * @return el color del poder
     */
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
                return new Color(165, 190, 215);
            case "Ilusión":
                return new Color(200, 120, 255);
            case "Beso de la Fea":
                return new Color(255, 110, 160);
            case "Barril":
                return new Color(205, 145, 70);
            default:
                return Color.WHITE;
        }
    }
}
