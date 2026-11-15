package presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Fábrica de botones con imagen. Centraliza el efecto de agrandar la imagen
 * al pasar el mouse y encogerla al presionar, que antes estaba repetido en
 * casi todas las vistas.
 */
public final class BotonImagen {

    private static final int PORCENTAJE_ENCIMA = 7;
    private static final int PORCENTAJE_PRESIONADO = 3;

    /**
     * Constructor privado para evitar instancias de la fábrica.
     */
    private BotonImagen() {
    }

    /**
     * Crea un botón cargando la imagen desde la carpeta resources.
     *
     * @param ruta ruta de la imagen
     * @param ancho ancho normal del botón
     * @return el botón listo para usar
     */
    public static JButton crear(String ruta, int ancho) {
        return crear(RecursosImagen.cargar(ruta), ancho);
    }

    /**
     * Crea un botón a partir de una imagen ya cargada.
     *
     * @param imagen imagen del botón
     * @param ancho ancho normal del botón
     * @return el botón listo para usar
     */
    public static JButton crear(BufferedImage imagen, int ancho) {
        return crear(imagen, ancho, PORCENTAJE_ENCIMA, PORCENTAJE_PRESIONADO);
    }

    /**
     * Crea un botón indicando cuánto crece y cuánto se encoge la imagen.
     *
     * @param imagen imagen del botón
     * @param ancho ancho normal del botón
     * @param porcentajeEncima porcentaje que crece al pasar el mouse
     * @param porcentajePresiona porcentaje que se encoge al presionar
     * @return el botón listo para usar
     */
    public static JButton crear(BufferedImage imagen, int ancho,
                                int porcentajeEncima, int porcentajePresiona) {
        ImageIcon normal = icono(imagen, ancho);
        ImageIcon encima = icono(imagen, ancho + extra(ancho, porcentajeEncima));
        ImageIcon presionado = icono(imagen, ancho + extra(ancho, porcentajePresiona));

        JButton boton = new JButton(normal);
        boton.setRolloverIcon(encima);
        boton.setPressedIcon(presionado);
        boton.setRolloverEnabled(true);
        boton.setPreferredSize(new Dimension(
                encima.getIconWidth(), encima.getIconHeight()));
        boton.setContentAreaFilled(false);
        boton.setBorderPainted(false);
        boton.setFocusPainted(false);
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return boton;
    }

    /**
     * Cambia la imagen de un botón ya creado sin perder sus acciones.
     *
     * @param boton botón a actualizar
     * @param imagen nueva imagen
     * @param ancho ancho normal
     */
    public static void cambiarImagen(JButton boton, BufferedImage imagen,
                                     int ancho) {
        cambiarImagen(boton, imagen, ancho,
                PORCENTAJE_ENCIMA, PORCENTAJE_PRESIONADO);
    }

    /**
     * Cambia la imagen de un botón ya creado indicando cuánto crece y cuánto
     * se encoge.
     *
     * @param boton botón a actualizar
     * @param imagen nueva imagen
     * @param ancho ancho normal
     * @param porcentajeEncima porcentaje que crece al pasar el mouse
     * @param porcentajePresiona porcentaje que se encoge al presionar
     */
    public static void cambiarImagen(JButton boton, BufferedImage imagen,
                                     int ancho, int porcentajeEncima,
                                     int porcentajePresiona) {
        if (boton == null || imagen == null) {
            return;
        }

        ImageIcon normal = icono(imagen, ancho);
        ImageIcon encima = icono(imagen,
                ancho + extra(ancho, porcentajeEncima));
        ImageIcon presionado = icono(imagen,
                ancho + extra(ancho, porcentajePresiona));

        boton.setIcon(normal);
        boton.setRolloverIcon(encima);
        boton.setPressedIcon(presionado);
        boton.setPreferredSize(new Dimension(
                encima.getIconWidth(), encima.getIconHeight()));
        boton.revalidate();
        boton.repaint();
    }

    /**
     * Escala una imagen manteniendo su proporción.
     *
     * @param imagen imagen original
     * @param ancho ancho deseado
     * @return el ícono escalado
     */
    public static ImageIcon icono(BufferedImage imagen, int ancho) {
        int alto = ancho * imagen.getHeight() / imagen.getWidth();
        return new ImageIcon(imagen.getScaledInstance(ancho, alto,
                Image.SCALE_SMOOTH));
    }

    /**
     * Calcula el tamaño extra del botón según un porcentaje.
     *
     * @param ancho ancho normal del botón
     * @param porcentaje porcentaje de crecimiento
     * @return el tamaño adicional en píxeles
     */
    private static int extra(int ancho, int porcentaje) {
        return Math.max(2, ancho * porcentaje / 100);
    }
}
