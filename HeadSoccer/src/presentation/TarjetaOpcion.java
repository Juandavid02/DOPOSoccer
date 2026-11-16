package presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Tarjeta seleccionable que muestra un título, una descripción y,
 * opcionalmente, una imagen. La usa la pantalla de selección del perfil de
 * la IA.
 *
 * @param <T> valor que representa la tarjeta
 */
public class TarjetaOpcion<T> extends JToggleButton {

    private final T valor;
    private final String titulo;
    private final String descripcion;
    private final BufferedImage imagen;

    /**
     * Crea una tarjeta con una imagen ya cargada.
     *
     * @param valor valor que representa la tarjeta
     * @param titulo título de la tarjeta
     * @param descripcion texto corto de la tarjeta
     * @param imagen imagen superior o null si no tiene
     */
    public TarjetaOpcion(T valor, String titulo, String descripcion,
                         BufferedImage imagen) {
        this.valor = valor;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.imagen = imagen;

        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setRolloverEnabled(true);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setPreferredSize(new Dimension(260, imagen == null ? 150 : 200));
    }

    /**
     * Devuelve el valor que representa la tarjeta.
     *
     * @return valor de la tarjeta
     */
    public T getValor() {
        return valor;
    }

    /**
     * Dibuja la tarjeta con su título, descripción e imagen.
     *
     * @param g gráficos del componente
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int ancho = getWidth();
        int alto = getHeight();
        boolean seleccionada = isSelected();
        boolean encima = getModel().isRollover();

        g2.setColor(new Color(0, 0, 0, 45));
        g2.fillRoundRect(3, 4, ancho - 6, alto - 8, 26, 26);

        Color fondo;
        if (seleccionada) {
            fondo = new Color(112, 82, 190, 240);
        } else if (encima) {
            fondo = new Color(226, 216, 246, 235);
        } else {
            fondo = new Color(228, 240, 248, 220);
        }
        g2.setColor(fondo);
        g2.fillRoundRect(1, 1, ancho - 4, alto - 8, 26, 26);

        g2.setStroke(new BasicStroke(seleccionada ? 3f : 2f));
        if (seleccionada) {
            g2.setColor(new Color(255, 255, 255, 230));
        } else if (encima) {
            g2.setColor(new Color(142, 112, 202, 220));
        } else {
            g2.setColor(new Color(120, 160, 185, 200));
        }
        g2.drawRoundRect(2, 2, ancho - 6, alto - 10, 26, 26);

        g2.setFont(new Font("SansSerif", Font.BOLD, 21));
        g2.setColor(isSelected() ? Color.WHITE : new Color(22, 48, 68));

        int yTitulo = 40;
        if (imagen != null) {
            int margen = 9;
            int anchoImagen = ancho - margen * 2;
            int altoImagen = (int) (alto * 0.50);
            RoundRectangle2D recorte = new RoundRectangle2D.Float(
                    margen, margen, anchoImagen, altoImagen, 18, 18);

            Shape anterior = g2.getClip();
            g2.setClip(recorte);
            double escala = Math.min(
                    (double) anchoImagen / imagen.getWidth(),
                    (double) altoImagen / imagen.getHeight());
            int anchoDibujo = (int) Math.ceil(imagen.getWidth() * escala);
            int altoDibujo = (int) Math.ceil(imagen.getHeight() * escala);
            g2.drawImage(imagen,
                    margen + (anchoImagen - anchoDibujo) / 2,
                    margen + (altoImagen - altoDibujo) / 2,
                    anchoDibujo, altoDibujo, null);
            g2.setClip(anterior);

            g2.setColor(new Color(255, 255, 255, 140));
            g2.setStroke(new BasicStroke(1.5f));
            g2.draw(recorte);

            g2.setColor(isSelected() ? Color.WHITE : new Color(22, 48, 68));
            yTitulo = margen + altoImagen + 26;
        }

        g2.drawString(titulo, 20, yTitulo);

        g2.setFont(new Font("SansSerif", Font.PLAIN, 15));
        FontMetrics fmTexto = g2.getFontMetrics();
        int y = yTitulo + 28;
        for (String linea : envolver(descripcion, fmTexto, ancho - 40)) {
            g2.drawString(linea, 20, y);
            y += fmTexto.getHeight();
        }

        g2.dispose();
    }

    /**
     * Divide un texto en líneas que caben en el ancho indicado.
     *
     * @param texto texto que se quiere envolver
     * @param fm métricas de la fuente usada
     * @param anchoMaximo ancho máximo de cada línea
     * @return líneas resultantes del texto
     */
    private static List<String> envolver(String texto, FontMetrics fm,
                                         int anchoMaximo) {
        List<String> lineas = new ArrayList<>();
        String[] palabras = texto.split(" ");
        StringBuilder linea = new StringBuilder();

        for (String palabra : palabras) {
            String prueba = linea.length() == 0
                    ? palabra : linea + " " + palabra;
            if (fm.stringWidth(prueba) <= anchoMaximo || linea.length() == 0) {
                linea.setLength(0);
                linea.append(prueba);
            } else {
                lineas.add(linea.toString());
                linea.setLength(0);
                linea.append(palabra);
            }
        }

        if (linea.length() > 0) {
            lineas.add(linea.toString());
        }
        return lineas;
    }
}
