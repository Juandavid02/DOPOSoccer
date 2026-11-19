package presentation;

import javax.swing.*;
import java.awt.*;

/**
 * Botón de paso para subir o bajar un valor. Es un botón normal de Swing
 * con dos estilos de color.
 */
public class BotonPaso extends JButton {

    /**
     * Estilo visual del botón.
     */
    public enum Estilo {
        AZUL,
        DORADO
    }

    private static final int TAMANO_POR_DEFECTO = 40;

    /**
     * Crea un botón azul con el texto indicado.
     *
     * @param texto texto que se mostrará en el botón
     */
    public BotonPaso(String texto) {
        this(texto, Estilo.AZUL, TAMANO_POR_DEFECTO);
    }

    /**
     * Crea un botón con el estilo y tamaño indicados.
     *
     * @param texto texto que se mostrará en el botón
     * @param estilo estilo visual del botón
     * @param tamano tamaño del botón en píxeles
     */
    public BotonPaso(String texto, Estilo estilo, int tamano) {
        super(texto);
        Estilo elegido = estilo == null ? Estilo.AZUL : estilo;

        setFont(new Font("SansSerif", Font.BOLD, 22));
        setPreferredSize(new Dimension(tamano, tamano));
        setMinimumSize(new Dimension(tamano, tamano));
        setOpaque(true);
        setFocusPainted(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setMargin(new Insets(0, 0, 0, 0));

        if (elegido == Estilo.DORADO) {
            setBackground(new Color(10, 32, 54));
            setForeground(new Color(255, 226, 130));
            setBorder(BorderFactory.createLineBorder(
                    new Color(212, 175, 55), 2, true));
        } else {
            setBackground(new Color(48, 118, 196));
            setForeground(Color.WHITE);
            setBorder(BorderFactory.createLineBorder(
                    new Color(28, 78, 136), 2, true));
        }
    }
}
