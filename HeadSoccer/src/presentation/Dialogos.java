package presentation;

import javax.swing.*;
import java.awt.*;

/**
 * Diálogos compartidos de confirmación, aviso, error e información.
 */
final class Dialogos {

    private Dialogos() {
    }

    /**
     * Pregunta al usuario y devuelve si eligió sí.
     *
     * @param padre componente padre del diálogo
     * @param titulo título de la ventana
     * @param mensaje texto de la pregunta
     * @param tipoMensaje tipo de icono de {@link JOptionPane}
     * @return true si el usuario eligió sí
     */
    static boolean confirmar(Component padre, String titulo, String mensaje,
                             int tipoMensaje) {
        int opcion = JOptionPane.showConfirmDialog(
                padre,
                mensaje,
                titulo,
                JOptionPane.YES_NO_OPTION,
                tipoMensaje);
        return opcion == JOptionPane.YES_OPTION;
    }

    /**
     * Muestra un aviso con el tipo de icono indicado.
     *
     * @param padre componente padre del diálogo
     * @param titulo título de la ventana
     * @param mensaje texto del aviso
     * @param tipoMensaje tipo de icono de {@link JOptionPane}
     */
    static void aviso(Component padre, String titulo, String mensaje,
                      int tipoMensaje) {
        JOptionPane.showMessageDialog(padre, mensaje, titulo, tipoMensaje);
    }

    /**
     * Muestra un error.
     *
     * @param padre componente padre del diálogo
     * @param titulo título de la ventana
     * @param mensaje texto del error
     */
    static void error(Component padre, String titulo, String mensaje) {
        aviso(padre, titulo, mensaje, JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Muestra un mensaje informativo.
     *
     * @param padre componente padre del diálogo
     * @param titulo título de la ventana
     * @param mensaje texto informativo
     */
    static void informacion(Component padre, String titulo, String mensaje) {
        aviso(padre, titulo, mensaje, JOptionPane.INFORMATION_MESSAGE);
    }
}
