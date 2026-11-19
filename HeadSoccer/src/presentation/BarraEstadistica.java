package presentation;

import domain.ParametrosJuego;

import javax.swing.*;
import java.awt.*;

/**
 * Barra de estadística segmentada de 1 a 10. Se usa en el perfil, en la
 * tienda y en la selección del perfil de la IA.
 */
public class BarraEstadistica extends JComponent {

    /**
     * Estilo del relleno de los segmentos.
     */
    public enum Estilo {
        DEGRADADO,
        PLANO
    }

    private final int separacion;
    private final boolean mostrarValor;
    private final Estilo estilo;
    private int valor;

    /**
     * Crea una barra con relleno degradado.
     *
     * @param ancho ancho preferido
     * @param alto alto preferido
     * @param separacion separación entre segmentos
     * @param mostrarValor true para dibujar el valor numérico a la derecha
     */
    public BarraEstadistica(int ancho, int alto, int separacion,
                            boolean mostrarValor) {
        this(ancho, alto, separacion, mostrarValor, Estilo.DEGRADADO);
    }

    /**
     * Crea una barra con el estilo indicado.
     *
     * @param ancho ancho preferido
     * @param alto alto preferido
     * @param separacion separación entre segmentos
     * @param mostrarValor true para dibujar el valor numérico a la derecha
     * @param estilo estilo del relleno
     */
    public BarraEstadistica(int ancho, int alto, int separacion,
                            boolean mostrarValor, Estilo estilo) {
        this.separacion = Math.max(1, separacion);
        this.mostrarValor = mostrarValor;
        this.estilo = estilo == null ? Estilo.DEGRADADO : estilo;
        setPreferredSize(new Dimension(ancho, alto));
    }

    /**
     * Asigna el valor que muestra la barra.
     *
     * @param valor valor entre 0 y el máximo permitido
     */
    public void setValor(int valor) {
        this.valor = Math.max(0,
                Math.min(ParametrosJuego.MAX_ESTADISTICA, valor));
        repaint();
    }

    /**
     * Dibuja los segmentos llenos y vacíos.
     *
     * @param g contexto gráfico
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int anchoTexto = mostrarValor ? 30 : 0;
        int anchoBarra = Math.max(40, getWidth() - anchoTexto);
        pintar(g2, 0, 2, anchoBarra, Math.max(4, getHeight() - 4),
                separacion, valor,
                new Color(52, 170, 110), new Color(30, 70, 95, 55),
                estilo == Estilo.DEGRADADO);

        if (mostrarValor) {
            g2.setFont(new Font("SansSerif", Font.BOLD, 15));
            g2.setColor(new Color(22, 48, 68));
            String texto = String.valueOf(valor);
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(texto,
                    getWidth() - fm.stringWidth(texto),
                    (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
        }

        g2.dispose();
    }

    /**
     * Dibuja una barra segmentada en el contexto y posición indicados.
     * También la usa la tarjeta de estadísticas de la selección de personaje.
     *
     * @param g2 contexto gráfico
     * @param x posición horizontal
     * @param y posición vertical
     * @param ancho ancho disponible
     * @param alto alto de los segmentos
     * @param separacion separación entre segmentos
     * @param valor cantidad de segmentos llenos
     * @param colorLleno color de los segmentos llenos
     * @param colorVacio color de los segmentos vacíos
     * @param degradado true para rellenar con degradado
     */
    public static void pintar(Graphics2D g2, int x, int y, int ancho,
                              int alto, int separacion, int valor,
                              Color colorLleno, Color colorVacio,
                              boolean degradado) {
        int segmentos = ParametrosJuego.MAX_ESTADISTICA;
        int anchoSegmento = Math.max(3,
                (ancho - separacion * (segmentos - 1)) / segmentos);
        int altoSegmento = Math.max(4, alto);

        for (int i = 0; i < segmentos; i++) {
            int xSegmento = x + i * (anchoSegmento + separacion);
            if (i < valor) {
                if (degradado) {
                    g2.setPaint(new GradientPaint(
                            xSegmento, y, colorLleno.brighter(),
                            xSegmento, y + altoSegmento, colorLleno.darker()));
                } else {
                    g2.setColor(colorLleno);
                }
            } else {
                g2.setColor(colorVacio);
            }
            g2.fillRoundRect(xSegmento, y, anchoSegmento, altoSegmento, 6, 6);
        }
    }
}
