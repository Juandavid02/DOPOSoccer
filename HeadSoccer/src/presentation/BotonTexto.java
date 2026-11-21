package presentation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Botón de texto para acciones que no tienen una imagen propia. Es un botón
 * normal de Swing con color de fondo y texto blanco.
 */
public class BotonTexto extends JButton {

    private static final Color COLOR_POR_DEFECTO = new Color(112, 82, 190);

    /**
     * Crea un botón de texto con el color morado por defecto.
     *
     * @param texto texto que se mostrará en el botón
     */
    public BotonTexto(String texto) {
        this(texto, COLOR_POR_DEFECTO);
    }

    /**
     * Crea un botón de texto con el color indicado.
     *
     * @param texto texto que se mostrará en el botón
     * @param color color base del botón
     */
    public BotonTexto(String texto, Color color) {
        super(texto);
        Color base = color == null ? COLOR_POR_DEFECTO : color;

        setFont(new Font("SansSerif", Font.BOLD, 21));
        setPreferredSize(new Dimension(340, 62));
        setMaximumSize(new Dimension(340, 62));
        setOpaque(true);
        setBackground(base);
        setForeground(Color.WHITE);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(base.darker(), 2, true),
                new EmptyBorder(8, 18, 8, 18)));
        setFocusPainted(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
}
