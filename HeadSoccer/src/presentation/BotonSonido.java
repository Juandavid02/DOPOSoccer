package presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Botón de sonido reutilizable. Muestra VolActivo o VolPausado según el
 * estado global del sonido.
 */
public class BotonSonido extends JButton {

    private static final String RUTA_BOTONES = "resources/images/Botones.java/";

    private final ImageIcon activoNormal;
    private final ImageIcon activoEncima;
    private final ImageIcon activoPresionado;
    private final ImageIcon pausadoNormal;
    private final ImageIcon pausadoEncima;
    private final ImageIcon pausadoPresionado;

    private boolean silenciado;

    public BotonSonido() {
        this(64);
    }

    public BotonSonido(int ancho) {
        activoNormal = crearIcono(RUTA_BOTONES + "VolActivo.png", ancho);
        activoEncima = crearIcono(RUTA_BOTONES + "VolActivo.png",
                ancho + Math.max(4, ancho * 7 / 100));
        activoPresionado = crearIcono(RUTA_BOTONES + "VolActivo.png",
                ancho + Math.max(2, ancho * 3 / 100));

        pausadoNormal = crearIcono(RUTA_BOTONES + "VolPausado.png", ancho);
        pausadoEncima = crearIcono(RUTA_BOTONES + "VolPausado.png",
                ancho + Math.max(4, ancho * 7 / 100));
        pausadoPresionado = crearIcono(RUTA_BOTONES + "VolPausado.png",
                ancho + Math.max(2, ancho * 3 / 100));

        setRolloverEnabled(true);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        setSilenciado(false);
    }

    public boolean isSilenciado() {
        return silenciado;
    }

    /**
     * Actualiza la imagen y el texto de ayuda del botón.
     *
     * @param silenciado true si el sonido está silenciado
     */
    public void setSilenciado(boolean silenciado) {
        this.silenciado = silenciado;

        ImageIcon normal;
        ImageIcon encima;
        ImageIcon presionado;
        String descripcion;

        if (silenciado) {
            normal = pausadoNormal;
            encima = pausadoEncima;
            presionado = pausadoPresionado;
            descripcion = "Activar sonido";
        } else {
            normal = activoNormal;
            encima = activoEncima;
            presionado = activoPresionado;
            descripcion = "Silenciar";
        }

        setIcon(normal);
        setRolloverIcon(encima);
        setPressedIcon(presionado);
        setPreferredSize(new Dimension(encima.getIconWidth(), encima.getIconHeight()));
        setToolTipText(descripcion);

        revalidate();
        repaint();
    }

    private static ImageIcon crearIcono(String ruta, int ancho) {
        BufferedImage imagen = RecursosImagen.cargar(ruta);
        int alto = ancho * imagen.getHeight() / imagen.getWidth();
        return new ImageIcon(imagen.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH));
    }
}
