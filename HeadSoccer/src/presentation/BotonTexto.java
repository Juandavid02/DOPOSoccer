package presentation;

import javax.swing.*;
import java.awt.*;

/**
 * Botón de texto con forma de píldora. Se usa para acciones que no tienen
 * una imagen propia, como inscribirse o jugar la siguiente ronda.
 */
public class BotonTexto extends JButton {

    private final Color color;

    /**
     * Crea un botón de texto con el color morado por defecto.
     *
     * @param texto texto que se mostrará en el botón
     */
    public BotonTexto(String texto) {
        this(texto, new Color(112, 82, 190));
    }

    /**
     * Crea un botón de texto con el color indicado.
     *
     * @param texto texto que se mostrará en el botón
     * @param color color base del botón
     */
    public BotonTexto(String texto, Color color) {
        super(texto);
        if (color == null) {
            this.color = new Color(112, 82, 190);
        } else {
            this.color = color;
        }
        setFont(new Font("SansSerif", Font.BOLD, 21));
        setPreferredSize(new Dimension(340, 62));
        setMaximumSize(new Dimension(340, 62));
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setRolloverEnabled(true);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    /**
     * Dibuja la píldora del botón y su texto.
     *
     * @param g contexto gráfico usado por Swing
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int ancho = getWidth();
        int alto = getHeight();
        int arco = alto;
        boolean encima = isEnabled() && getModel().isRollover();

        g2.setColor(new Color(0, 0, 0, 80));
        g2.fillRoundRect(3, 5, ancho - 6, alto - 6, arco, arco);

        Color base;
        if (isEnabled()) {
            base = color;
        } else {
            base = new Color(140, 140, 150);
        }
        Color superior = base;
        if (encima) {
            superior = base.brighter();
        }
        g2.setPaint(new GradientPaint(
                0, 0, superior,
                0, alto, base.darker()));
        g2.fillRoundRect(1, 1, ancho - 3, alto - 6, arco, arco);

        g2.setStroke(new BasicStroke(2f));
        g2.setColor(new Color(255, 255, 255, 180));
        g2.drawRoundRect(2, 2, ancho - 5, alto - 8, arco, arco);

        g2.setFont(getFont());
        FontMetrics fm = g2.getFontMetrics();
        int x = (ancho - fm.stringWidth(getText())) / 2;
        int y = (alto - 5 + fm.getAscent() - fm.getDescent()) / 2;

        g2.setColor(new Color(20, 12, 32, 150));
        g2.drawString(getText(), x + 1, y + 2);
        g2.setColor(Color.WHITE);
        g2.drawString(getText(), x, y);

        g2.dispose();
    }
}
