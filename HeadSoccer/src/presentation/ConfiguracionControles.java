package presentation;

import java.awt.event.KeyEvent;

/**
 * Teclas asignadas a las acciones de cada jugador.
 *
 * Las teclas se guardan como códigos de KeyEvent. La configuración es
 * independiente de la pantalla que la edita.
 */
public final class ConfiguracionControles {

    public enum Accion {
        IZQUIERDA("Izquierda"),
        DERECHA("Derecha"),
        SALTAR("Saltar"),
        PATEAR("Patear"),
        PODER("Poder");

        private final String etiqueta;

        /**
         * Crea la acción con su etiqueta.
         *
         * @param etiqueta nombre visible de la acción
         */
        Accion(String etiqueta) {
            this.etiqueta = etiqueta;
        }

        /**
         * Devuelve el nombre visible de la acción.
         *
         * @return la etiqueta de la acción
         */
        public String getEtiqueta() {
            return etiqueta;
        }
    }

    private static final int JUGADORES = 2;
    private static final int ACCIONES = Accion.values().length;

    private final int[][] teclas;

    /**
     * Crea la configuración con las teclas por defecto.
     */
    public ConfiguracionControles() {
        teclas = new int[JUGADORES][ACCIONES];
        restablecer();
    }

    /**
     * Crea una copia independiente de otra configuración.
     *
     * @param otra configuración que se copia
     */
    public ConfiguracionControles(ConfiguracionControles otra) {
        teclas = new int[JUGADORES][ACCIONES];
        for (int jugador = 0; jugador < JUGADORES; jugador++) {
            System.arraycopy(otra.teclas[jugador], 0,
                    teclas[jugador], 0, ACCIONES);
        }
    }

    /**
     * Devuelve el código de tecla de una acción.
     *
     * @param jugador número del jugador: 1 o 2
     * @param accion acción consultada
     * @return código de KeyEvent
     */
    public int getTecla(int jugador, Accion accion) {
        validarJugador(jugador);
        return teclas[jugador - 1][accion.ordinal()];
    }

    /**
     * Asigna una tecla a una acción si no está ocupada por otra.
     *
     * @param jugador número del jugador: 1 o 2
     * @param accion acción que se cambia
     * @param tecla nuevo código de tecla
     * @return true si la asignación se realizó
     */
    public boolean asignar(int jugador, Accion accion, int tecla) {
        validarJugador(jugador);
        if (accion == null || tecla == KeyEvent.VK_UNDEFINED
                || tecla == KeyEvent.VK_ESCAPE) {
            return false;
        }

        int jugadorIndice = jugador - 1;
        for (int j = 0; j < JUGADORES; j++) {
            for (Accion a : Accion.values()) {
                if (j == jugadorIndice && a == accion) {
                    continue;
                }
                if (teclas[j][a.ordinal()] == tecla) {
                    return false;
                }
            }
        }

        teclas[jugadorIndice][accion.ordinal()] = tecla;
        return true;
    }

    /**
     * Vuelve a las teclas originales del juego.
     */
    public void restablecer() {
        teclas[0][Accion.IZQUIERDA.ordinal()] = KeyEvent.VK_A;
        teclas[0][Accion.DERECHA.ordinal()] = KeyEvent.VK_D;
        teclas[0][Accion.SALTAR.ordinal()] = KeyEvent.VK_W;
        teclas[0][Accion.PATEAR.ordinal()] = KeyEvent.VK_S;
        teclas[0][Accion.PODER.ordinal()] = KeyEvent.VK_Q;

        teclas[1][Accion.IZQUIERDA.ordinal()] = KeyEvent.VK_LEFT;
        teclas[1][Accion.DERECHA.ordinal()] = KeyEvent.VK_RIGHT;
        teclas[1][Accion.SALTAR.ordinal()] = KeyEvent.VK_UP;
        teclas[1][Accion.PATEAR.ordinal()] = KeyEvent.VK_DOWN;
        teclas[1][Accion.PODER.ordinal()] = KeyEvent.VK_CONTROL;
    }

    /**
     * Convierte un código de tecla en un nombre legible.
     *
     * @param tecla código de KeyEvent
     * @return nombre corto de la tecla
     */
    public static String nombreTecla(int tecla) {
        switch (tecla) {
            case KeyEvent.VK_LEFT:
                return "←";
            case KeyEvent.VK_RIGHT:
                return "→";
            case KeyEvent.VK_UP:
                return "↑";
            case KeyEvent.VK_DOWN:
                return "↓";
            case KeyEvent.VK_CONTROL:
                return "Ctrl";
            case KeyEvent.VK_SHIFT:
                return "Shift";
            case KeyEvent.VK_ALT:
                return "Alt";
            case KeyEvent.VK_SPACE:
                return "Espacio";
            case KeyEvent.VK_ENTER:
                return "Enter";
            default:
                return KeyEvent.getKeyText(tecla);
        }
    }

    /**
     * Comprueba que el número de jugador sea válido.
     *
     * @param jugador número del jugador: 1 o 2
     */
    private static void validarJugador(int jugador) {
        if (jugador != 1 && jugador != 2) {
            throw new IllegalArgumentException("El jugador debe ser 1 o 2");
        }
    }
}
