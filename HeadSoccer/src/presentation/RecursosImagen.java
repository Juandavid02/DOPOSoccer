package presentation;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Carga imágenes de la carpeta resources directamente del disco.
 */
final class RecursosImagen {

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
            return ImageIO.read(new File(ruta));
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo cargar la imagen: " + ruta, e);
        }
    }
}
