package presentation;

import domain.PerfilJugador;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Renderer compartido para las listas de perfiles. Muestra el nombre y,
 * opcionalmente, los puntos del perfil.
 */
public class PerfilRenderer extends DefaultListCellRenderer {

    private static final Color TEXTO = new Color(30, 62, 86);
    private static final Color FONDO = new Color(214, 232, 242);
    private static final Color SELECCION = new Color(38, 108, 148);

    private final int tamanoFuente;
    private final int padding;
    private final boolean mostrarPuntos;

    /**
     * Crea el renderer.
     *
     * @param tamanoFuente tamaño de la fuente
     * @param padding espacio interior de la celda
     * @param mostrarPuntos true para mostrar los puntos junto al nombre
     */
    public PerfilRenderer(int tamanoFuente, int padding,
                          boolean mostrarPuntos) {
        this.tamanoFuente = tamanoFuente;
        this.padding = padding;
        this.mostrarPuntos = mostrarPuntos;
    }

    /**
     * Configura la etiqueta de una celda de la lista de perfiles.
     *
     * @param lista lista que dibuja la celda
     * @param valor perfil de la celda
     * @param indice índice de la celda
     * @param seleccionado si la celda está seleccionada
     * @param tieneFoco si la celda tiene el foco
     * @return componente de la celda
     */
    @Override
    public Component getListCellRendererComponent(JList<?> lista, Object valor,
                                                  int indice, boolean seleccionado,
                                                  boolean tieneFoco) {
        JLabel etiqueta = (JLabel) super.getListCellRendererComponent(
                lista, valor, indice, seleccionado, tieneFoco);

        String texto = "";
        if (valor instanceof PerfilJugador) {
            PerfilJugador perfil = (PerfilJugador) valor;
            texto = perfil.getNombre();
            if (mostrarPuntos) {
                texto += "  ·  " + perfil.getPuntos().getTotal() + " pts";
            }
        } else if (valor != null) {
            texto = valor.toString();
        }
        etiqueta.setText(texto);
        etiqueta.setOpaque(true);
        etiqueta.setFont(new Font("SansSerif", Font.BOLD, tamanoFuente));
        etiqueta.setBorder(new EmptyBorder(padding, 14, padding, 14));

        if (seleccionado) {
            etiqueta.setForeground(Color.WHITE);
            etiqueta.setBackground(SELECCION);
        } else {
            etiqueta.setForeground(TEXTO);
            etiqueta.setBackground(FONDO);
        }
        return etiqueta;
    }
}
