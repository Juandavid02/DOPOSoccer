package presentation;

import javax.swing.*;
import java.awt.*;

/**
 * Botón circular de + / - reutilizable. Lo usan la configuración del partido
 * y el ajuste de estadísticas de la IA.
 */
public class BotonPaso extends JButton {

    private static final int TAMANO = 40;

    /**
     * Crea un botón circular con el texto indicado.
     *
     * @param texto texto que se mostrará en el botón
     */
    public BotonPaso(String texto) {
        super(texto);
        setFont(new Font("SansSerif", Font.BOLD, 22));
        setPreferredSize(new Dimension(TAMANO, TAMANO));
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    /**
     * Dibuja el círculo del botón y su texto.
     *
     * @param g contexto gráfico usado por Swing
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        Color base;
        if (isEnabled()) {
            base = new Color(48, 118, 196);
        } else {
            base = new Color(150, 170, 185);
        }

        g2.setColor(base);
        g2.fillOval(0, 0, getWidth(), getHeight());

        g2.setColor(base.darker());
        g2.setStroke(new BasicStroke(2f));
        g2.drawOval(1, 1, getWidth() - 2, getHeight() - 2);

        g2.setFont(getFont());
        FontMetrics fm = g2.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(getText())) / 2;
        int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
        g2.setColor(Color.WHITE);
        g2.drawString(getText(), x, y);

        g2.dispose();
    }
}
