package presentation;

import javax.swing.*;
import java.awt.*;

/**
 * Título con fondo redondeado, degradado y texto con contorno. Antes cada
 * pantalla tenía su propia copia de esta clase.
 */
public class TituloPantalla extends JComponent {

    private static final int TAMANO_POR_DEFECTO = 28;
    private static final Font FUENTE_BASE =
            new Font("SansSerif", Font.BOLD, TAMANO_POR_DEFECTO);

    private final String texto;
    private final Font fuente;

    /**
     * Crea el título con el texto indicado.
     *
     * @param texto texto que se mostrará
     */
    public TituloPantalla(String texto) {
        this.texto = texto;
        this.fuente = FUENTE_BASE;
    }

    /**
     * Calcula el tamaño preferido según el texto del título.
     *
     * @return tamaño preferido del componente
     */
    @Override
    public Dimension getPreferredSize() {
        FontMetrics fm = getFontMetrics(fuente);
        return new Dimension(fm.stringWidth(texto) + 80, 58);
    }

    /**
     * Dibuja el fondo redondeado y el texto con contorno.
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

        g2.setColor(new Color(0, 0, 0, 90));
        g2.fillRoundRect(4, 6, ancho - 8, alto - 10, arco, arco);

        g2.setPaint(new GradientPaint(
                0, 0, new Color(78, 160, 198, 235),
                0, alto, new Color(26, 82, 122, 235)));
        g2.fillRoundRect(2, 2, ancho - 4, alto - 8, arco, arco);

        g2.setStroke(new BasicStroke(2f));
        g2.setColor(new Color(255, 255, 255, 170));
        g2.drawRoundRect(4, 4, ancho - 9, alto - 13, arco, arco);

        g2.setFont(fuente);
        FontMetrics fm = g2.getFontMetrics();
        int x = (ancho - fm.stringWidth(texto)) / 2;
        int y = (alto - 6 - fm.getHeight()) / 2 + fm.getAscent();

        g2.setColor(new Color(16, 32, 46));
        for (int dx = -2; dx <= 2; dx++) {
            for (int dy = -2; dy <= 2; dy++) {
                if (dx != 0 || dy != 0) {
                    g2.drawString(texto, x + dx, y + dy);
                }
            }
        }
        g2.setColor(Color.WHITE);
        g2.drawString(texto, x, y);

        g2.dispose();
    }
}
