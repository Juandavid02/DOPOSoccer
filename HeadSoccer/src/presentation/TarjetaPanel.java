package presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Panel con forma de tarjeta redondeada. Tiene dos estilos: el claro que
 * usan las pantallas de configuración y el oscuro con borde dorado.
 */
public class TarjetaPanel extends JPanel {

    public enum Estilo {
        CLARA,
        OSCURA
    }

    private final Estilo estilo;

    /**
     * Crea una tarjeta con el estilo claro.
     */
    public TarjetaPanel() {
        this(Estilo.CLARA);
    }

    /**
     * Crea una tarjeta con el estilo indicado.
     *
     * @param estilo estilo visual de la tarjeta
     */
    public TarjetaPanel(Estilo estilo) {
        if (estilo == null) {
            this.estilo = Estilo.CLARA;
        } else {
            this.estilo = estilo;
        }
        setOpaque(false);
    }

    /**
     * Crea una tarjeta oscura vacía con un signo de interrogación centrado.
     *
     * @return tarjeta vacía lista para usar
     */
    public static TarjetaPanel vacia() {
        return vacia(40);
    }

    /**
     * Crea una tarjeta oscura vacía con un signo de interrogación centrado.
     *
     * @param tamanoFuente tamaño del signo de interrogación
     * @return tarjeta vacía lista para usar
     */
    public static TarjetaPanel vacia(int tamanoFuente) {
        TarjetaPanel tarjeta = new TarjetaPanel(Estilo.OSCURA);
        tarjeta.setLayout(new BorderLayout());

        JLabel signo = new JLabel("?", SwingConstants.CENTER);
        signo.setFont(new Font("SansSerif", Font.BOLD, tamanoFuente));
        signo.setForeground(new Color(242, 205, 98));
        tarjeta.add(signo, BorderLayout.CENTER);
        return tarjeta;
    }

    /**
     * Dibuja la tarjeta según su estilo.
     *
     * @param g gráficos del componente
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        if (estilo == Estilo.OSCURA) {
            pintarOscura(g2);
        } else {
            pintarClara(g2);
        }

        g2.dispose();
    }

    /**
     * Pinta el fondo claro de la tarjeta.
     *
     * @param g2 gráficos del componente
     */
    private void pintarClara(Graphics2D g2) {
        int ancho = getWidth();
        int alto = getHeight();
        RoundRectangle2D forma = new RoundRectangle2D.Float(
                1, 1, ancho - 3, alto - 3, 38, 38);

        g2.setPaint(new GradientPaint(
                0, 0, new Color(236, 246, 252, 130),
                0, alto, new Color(204, 228, 242, 100)));
        g2.fill(forma);

        g2.setStroke(new BasicStroke(2f));
        g2.setColor(new Color(255, 255, 255, 120));
        g2.draw(forma);
    }

    /**
     * Pinta el fondo oscuro de la tarjeta con borde dorado.
     *
     * @param g2 gráficos del componente
     */
    private void pintarOscura(Graphics2D g2) {
        int ancho = getWidth();
        int alto = getHeight();
        RoundRectangle2D forma = new RoundRectangle2D.Float(
                1, 1, ancho - 3, alto - 3, 32, 32);

        g2.setPaint(new GradientPaint(
                0, 0, new Color(9, 30, 52, 225),
                0, alto, new Color(5, 20, 38, 225)));
        g2.fill(forma);

        g2.setStroke(new BasicStroke(3f));
        g2.setColor(new Color(212, 175, 55));
        g2.draw(forma);

        g2.setStroke(new BasicStroke(1f));
        g2.setColor(new Color(255, 225, 140, 95));
        g2.draw(new RoundRectangle2D.Float(
                6, 6, ancho - 13, alto - 13, 26, 26));
    }
}
