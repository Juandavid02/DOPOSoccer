package presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Botón de sonido reutilizable. Muestra VolActivo o VolPausado según el
 * estado global del sonido.
 */
public class BotonSonido extends JButton {

    private static final String RUTA_BOTONES = "resources/images/Botones/";

    private final ImageIcon activoNormal;
    private final ImageIcon activoEncima;
    private final ImageIcon activoPresionado;
    private final ImageIcon pausadoNormal;
    private final ImageIcon pausadoEncima;
    private final ImageIcon pausadoPresionado;

    private boolean silenciado;

    /**
     * Crea el botón de sonido con el ancho por defecto.
     */
    public BotonSonido() {
        this(64);
    }

    /**
     * Crea el botón de sonido con el ancho indicado.
     *
     * @param ancho ancho base del icono del botón
     */
    public BotonSonido(int ancho) {
        BufferedImage activo = RecursosImagen.cargar(RUTA_BOTONES + "VolActivo.png");
        BufferedImage pausado = RecursosImagen.cargar(RUTA_BOTONES + "VolPausado.png");
        int anchoEncima = ancho + Math.max(4, ancho * 7 / 100);
        int anchoPresionado = ancho + Math.max(2, ancho * 3 / 100);

        activoNormal = BotonImagen.icono(activo, ancho);
        activoEncima = BotonImagen.icono(activo, anchoEncima);
        activoPresionado = BotonImagen.icono(activo, anchoPresionado);

        pausadoNormal = BotonImagen.icono(pausado, ancho);
        pausadoEncima = BotonImagen.icono(pausado, anchoEncima);
        pausadoPresionado = BotonImagen.icono(pausado, anchoPresionado);

        setRolloverEnabled(true);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        setSilenciado(false);
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
}
