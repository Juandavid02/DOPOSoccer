package presentation;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Carga imágenes de la carpeta resources directamente del disco.
 */
final class RecursosImagen {

    /**
     * Constructor privado de la clase de utilidades.
     */
    private RecursosImagen() {
    }

    /**
     * Carga una imagen de la carpeta resources.
     *
     * @param ruta la ruta de la imagen, que empieza con "resources/"
     * @return la imagen cargada
     */
    static BufferedImage cargar(String ruta) {
        try {
            BufferedImage imagen = ImageIO.read(new File(ruta));
            if (imagen == null) {
                throw new IllegalStateException(
                        "No se pudo cargar la imagen: " + ruta);
            }
            return imagen;
        } catch (IOException e) {
            throw new IllegalStateException(
                    "No se pudo cargar la imagen: " + ruta, e);
        }
    }

    /**
     * Carga la cadena de bloqueo de la tienda y la selección de personaje.
     *
     * @return imagen de la cadena o null si no se pudo cargar
     */
    static BufferedImage cargarCadena() {
        try {
            return cargar("resources/images/Objetos/CadenaDeBloqueo.png");
        } catch (RuntimeException e) {
            return null;
        }
    }

    /**
     * Recorta los píxeles transparentes que rodean una imagen.
     *
     * @param imagen imagen original
     * @return imagen recortada o null si la imagen es null
     */
    static BufferedImage recortarTransparente(BufferedImage imagen) {
        if (imagen == null) {
            return null;
        }

        int minX = imagen.getWidth();
        int minY = imagen.getHeight();
        int maxX = -1;
        int maxY = -1;

        for (int y = 0; y < imagen.getHeight(); y++) {
            for (int x = 0; x < imagen.getWidth(); x++) {
                int alpha = (imagen.getRGB(x, y) >>> 24) & 0xff;
                if (alpha > 10) {
                    minX = Math.min(minX, x);
                    minY = Math.min(minY, y);
                    maxX = Math.max(maxX, x);
                    maxY = Math.max(maxY, y);
                }
            }
        }

        if (maxX < minX || maxY < minY) {
            return imagen;
        }
        return imagen.getSubimage(minX, minY,
                maxX - minX + 1, maxY - minY + 1);
    }

    /**
     * Recorta el espacio vacío que rodea al contenido de una imagen. Sirve
     * para las ilustraciones de los tipos de IA, que vienen centradas en un
     * lienzo grande con fondo blanco.
     *
     * @param imagen imagen original
     * @return imagen recortada o la original si no se encontró contenido
     */
    static BufferedImage recortarContenido(BufferedImage imagen) {
        if (imagen == null) {
            return null;
        }

        imagen = reducirParaRecorte(imagen);

        int minX = imagen.getWidth();
        int minY = imagen.getHeight();
        int maxX = -1;
        int maxY = -1;

        for (int y = 0; y < imagen.getHeight(); y++) {
            for (int x = 0; x < imagen.getWidth(); x++) {
                if (!esFondo(imagen.getRGB(x, y))) {
                    minX = Math.min(minX, x);
                    minY = Math.min(minY, y);
                    maxX = Math.max(maxX, x);
                    maxY = Math.max(maxY, y);
                }
            }
        }

        if (maxX < minX || maxY < minY) {
            return imagen;
        }

        int margen = 6;
        minX = Math.max(0, minX - margen);
        minY = Math.max(0, minY - margen);
        maxX = Math.min(imagen.getWidth() - 1, maxX + margen);
        maxY = Math.min(imagen.getHeight() - 1, maxY + margen);

        BufferedImage recortada = imagen.getSubimage(minX, minY,
                maxX - minX + 1, maxY - minY + 1);
        return quitarFondoBlanco(recortada);
    }

    /**
     * Convierte en transparente el fondo blanco conectado con los bordes.
     * Los blancos internos (ojos, balón) se conservan porque no están
     * conectados con el exterior.
     */
    /**
     * Quita el fondo blanco conectado con los bordes de la imagen.
     *
     * @param imagen imagen original
     * @return imagen con el fondo transparente
     */
    private static BufferedImage quitarFondoBlanco(BufferedImage imagen) {
        int ancho = imagen.getWidth();
        int alto = imagen.getHeight();
        BufferedImage salida = new BufferedImage(ancho, alto,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = salida.createGraphics();
        g2.drawImage(imagen, 0, 0, null);
        g2.dispose();

        boolean[] visitado = new boolean[ancho * alto];
        int[] pila = new int[ancho * alto];
        int cima = 0;

        for (int x = 0; x < ancho; x++) {
            cima = empujarFondo(salida, visitado, pila, cima, x, 0);
            cima = empujarFondo(salida, visitado, pila, cima, x, alto - 1);
        }
        for (int y = 0; y < alto; y++) {
            cima = empujarFondo(salida, visitado, pila, cima, 0, y);
            cima = empujarFondo(salida, visitado, pila, cima, ancho - 1, y);
        }

        while (cima > 0) {
            int posicion = pila[--cima];
            int x = posicion % ancho;
            int y = posicion / ancho;
            int argb = salida.getRGB(x, y);
            salida.setRGB(x, y, argb & 0x00FFFFFF);

            if (x > 0) {
                cima = empujarFondo(salida, visitado, pila, cima, x - 1, y);
            }
            if (x < ancho - 1) {
                cima = empujarFondo(salida, visitado, pila, cima, x + 1, y);
            }
            if (y > 0) {
                cima = empujarFondo(salida, visitado, pila, cima, x, y - 1);
            }
            if (y < alto - 1) {
                cima = empujarFondo(salida, visitado, pila, cima, x, y + 1);
            }
        }

        return salida;
    }

    /**
     * Agrega un píxel de fondo a la pila si corresponde.
     *
     * @param imagen imagen que se analiza
     * @param visitado marcas de píxeles ya visitados
     * @param pila pila de posiciones pendientes
     * @param cima posición actual de la cima de la pila
     * @param x coordenada horizontal
     * @param y coordenada vertical
     * @return la nueva cima de la pila
     */
    private static int empujarFondo(BufferedImage imagen, boolean[] visitado,
                                    int[] pila, int cima, int x, int y) {
        int ancho = imagen.getWidth();
        int posicion = y * ancho + x;
        if (visitado[posicion]) {
            return cima;
        }
        visitado[posicion] = true;
        if (!esFondo(imagen.getRGB(x, y))) {
            return cima;
        }
        pila[cima] = posicion;
        return cima + 1;
    }

    /**
     * Indica si un color se considera fondo.
     *
     * @param argb color en formato ARGB
     * @return true si el color es transparente o casi blanco
     */
    private static boolean esFondo(int argb) {
        int alfa = (argb >>> 24) & 0xff;
        if (alfa < 16) {
            return true;
        }
        int rojo = (argb >> 16) & 0xff;
        int verde = (argb >> 8) & 0xff;
        int azul = argb & 0xff;
        return rojo >= 244 && verde >= 244 && azul >= 244;
    }

    /**
     * Reduce la imagen antes de analizarla. El recorte y el quitado de fondo
     * se hacen sobre una versión pequeña para no recorrer millones de
     * píxeles al abrir la pantalla.
     *
     * @param imagen imagen original
     * @return la imagen reducida
     */
    private static BufferedImage reducirParaRecorte(BufferedImage imagen) {
        int ladoMaximo = Math.max(imagen.getWidth(), imagen.getHeight());
        if (ladoMaximo <= 360) {
            return imagen;
        }

        double escala = 360.0 / ladoMaximo;
        int ancho = Math.max(1, (int) Math.round(imagen.getWidth() * escala));
        int alto = Math.max(1, (int) Math.round(imagen.getHeight() * escala));
        BufferedImage reducida = new BufferedImage(ancho, alto,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = reducida.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(imagen, 0, 0, ancho, alto, null);
        g2.dispose();
        return reducida;
    }
}
