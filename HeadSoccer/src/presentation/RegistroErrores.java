package presentation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Registro de errores para los programadores. Guarda los detalles técnicos
 * en logs/errores.log mientras el usuario ve un mensaje claro en pantalla.
 */
public final class RegistroErrores {

    private static final String CARPETA = "logs";
    private static final String ARCHIVO = "logs/errores.log";
    private static final Logger REGISTRO = Logger.getLogger("DOPOSoccer");

    static {
        try {
            Path carpeta = Paths.get(CARPETA);
            Files.createDirectories(carpeta);
            FileHandler manejador = new FileHandler(ARCHIVO, true);
            manejador.setEncoding("UTF-8");
            manejador.setFormatter(new SimpleFormatter());
            REGISTRO.addHandler(manejador);
            REGISTRO.setUseParentHandlers(false);
        } catch (IOException | SecurityException e) {
            System.err.println("No se pudo inicializar el log de errores: "
                    + e.getMessage());
        }
    }

    private RegistroErrores() {
    }

    /**
     * Registra un error con su traza.
     *
     * @param contexto operación que falló
     * @param error error original
     */
    public static void registrar(String contexto, Throwable error) {
        if (error == null) {
            registrar(contexto, "error desconocido");
            return;
        }
        REGISTRO.log(Level.SEVERE, contexto, error);
    }

    /**
     * Registra un mensaje de error sin traza.
     *
     * @param contexto operación que falló
     * @param detalle detalle del error
     */
    public static void registrar(String contexto, String detalle) {
        REGISTRO.log(Level.SEVERE, contexto + ": " + detalle);
    }

    /**
     * Devuelve la ruta del archivo de log.
     *
     * @return ruta del archivo de log
     */
    public static String getRutaArchivo() {
        return ARCHIVO;
    }
}
