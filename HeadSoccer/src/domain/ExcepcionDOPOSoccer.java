package domain;

/**
 * Excepción propia de DOPOSoccer para errores de configuración y validación.
 */
public class ExcepcionDOPOSoccer extends Exception {

    private static final long serialVersionUID = 1L;

    public static final String MODALIDAD_NULA = "La modalidad no puede ser nula";
    public static final String TIPO_PARTIDO_NULO = "El tipo de partido no puede ser nulo";
    public static final String RESOLUCION_NULA = "La resolución no puede ser nula";
    public static final String TIEMPO_INVALIDO = "El tiempo debe ser mayor que cero";
    public static final String GOLES_INVALIDOS = "Los goles deben ser mayores que cero";

    public static final String NOMBRE_INVALIDO = "El nombre no puede ser nulo o vacío";
    public static final String ID_INVALIDO = "El id no puede ser nulo o vacío";
    public static final String CONFIGURACION_NULA = "La configuración no puede ser nula";
    public static final String ESTADISTICAS_NULAS = "Las estadísticas no pueden ser nulas";
    public static final String PUNTOS_NULOS = "Los puntos no pueden ser nulos";
    public static final String PUNTOS_INSUFICIENTES = "No hay puntos suficientes para inscribirse en el torneo";

    public static final String SAVE_ARCHIVO_NULO =
            "No se indicó el archivo donde guardar el progreso";
    public static final String SAVE_ES_CARPETA =
            "No se puede guardar: la ruta indicada es una carpeta";
    public static final String SAVE_EXTENSION_INVALIDA =
            "El archivo para guardar debe tener extensión .dat";
    public static final String SAVE_CARPETA_NO_EXISTE =
            "La carpeta donde se quiere guardar no existe";
    public static final String SAVE_SIN_PERMISO =
            "No hay permiso de escritura en el archivo o en su carpeta";
    public static final String SAVE_NO_SERIALIZABLE =
            "El progreso contiene datos que no se pueden guardar";
    public static final String SAVE_ERROR =
            "Ocurrió un error al guardar el progreso";

    public static final String OPEN_ARCHIVO_NULO =
            "No se indicó el archivo que se quiere abrir";
    public static final String OPEN_ES_CARPETA =
            "No se puede abrir: la ruta indicada es una carpeta";
    public static final String OPEN_NO_EXISTE =
            "El archivo que se quiere abrir no existe";
    public static final String OPEN_EXTENSION_INVALIDA =
            "El archivo para abrir debe tener extensión .dat";
    public static final String OPEN_SIN_PERMISO =
            "No hay permiso de lectura en el archivo";
    public static final String OPEN_NO_ES_PROGRESO =
            "El archivo no contiene un progreso de DOPOSoccer";
    public static final String OPEN_VERSION_INCOMPATIBLE =
            "El archivo fue guardado con una versión incompatible";
    public static final String OPEN_CLASE_DESCONOCIDA =
            "El archivo contiene clases que esta versión no reconoce";
    public static final String OPEN_ERROR =
            "Ocurrió un error al abrir el progreso";

    public static final String EXPORT_ARCHIVO_NULO =
            "No se indicó el archivo donde exportar el progreso";
    public static final String EXPORT_ES_CARPETA =
            "No se puede exportar: la ruta indicada es una carpeta";
    public static final String EXPORT_EXTENSION_INVALIDA =
            "El archivo para exportar debe tener extensión .txt";
    public static final String EXPORT_CARPETA_NO_EXISTE =
            "La carpeta donde se quiere exportar no existe";
    public static final String EXPORT_SIN_PERMISO =
            "No hay permiso de escritura en el archivo o en su carpeta";
    public static final String EXPORT_ERROR =
            "Ocurrió un error al exportar el progreso";

    public static final String IMPORT_ARCHIVO_NULO =
            "No se indicó el archivo que se quiere importar";
    public static final String IMPORT_ES_CARPETA =
            "No se puede importar: la ruta indicada es una carpeta";
    public static final String IMPORT_NO_EXISTE =
            "El archivo que se quiere importar no existe";
    public static final String IMPORT_EXTENSION_INVALIDA =
            "El archivo para importar debe tener extensión .txt";
    public static final String IMPORT_SIN_PERMISO =
            "No hay permiso de lectura en el archivo";
    public static final String IMPORT_ERROR =
            "Ocurrió un error al importar el progreso";

    public static final String PARTIDA_INVALIDA =
            "La partida guardada no tiene todos los datos necesarios";
    public static final String PARTIDA_SAVE_ARCHIVO_NULO =
            "No se indicó el archivo donde guardar la partida";
    public static final String PARTIDA_SAVE_ES_CARPETA =
            "No se puede guardar la partida: la ruta indicada es una carpeta";
    public static final String PARTIDA_SAVE_EXTENSION_INVALIDA =
            "La partida debe guardarse con extensión .dat";
    public static final String PARTIDA_SAVE_CARPETA_NO_EXISTE =
            "La carpeta donde se quiere guardar la partida no existe";
    public static final String PARTIDA_SAVE_SIN_PERMISO =
            "No hay permiso de escritura en el archivo o en su carpeta";
    public static final String PARTIDA_SAVE_NO_SERIALIZABLE =
            "La partida contiene datos que no se pueden guardar";
    public static final String PARTIDA_SAVE_ERROR =
            "Ocurrió un error al guardar la partida";

    public static final String PARTIDA_OPEN_ARCHIVO_NULO =
            "No se indicó el archivo de la partida que se quiere abrir";
    public static final String PARTIDA_OPEN_ES_CARPETA =
            "No se puede abrir la partida: la ruta indicada es una carpeta";
    public static final String PARTIDA_OPEN_NO_EXISTE =
            "El archivo de la partida no existe";
    public static final String PARTIDA_OPEN_EXTENSION_INVALIDA =
            "La partida debe tener extensión .dat";
    public static final String PARTIDA_OPEN_SIN_PERMISO =
            "No hay permiso de lectura en el archivo de la partida";
    public static final String PARTIDA_OPEN_NO_ES_PARTIDA =
            "El archivo no contiene una partida de DOPOSoccer";
    public static final String PARTIDA_OPEN_VERSION_INCOMPATIBLE =
            "La partida fue guardada con una versión incompatible";
    public static final String PARTIDA_OPEN_CLASE_DESCONOCIDA =
            "La partida contiene clases que esta versión no reconoce";
    public static final String PARTIDA_OPEN_ERROR =
            "Ocurrió un error al abrir la partida";

    /**
     * Crea la excepción con el mensaje indicado.
     *
     * @param mensaje mensaje de la excepción
     */
    public ExcepcionDOPOSoccer(String mensaje) {
        super(mensaje);
    }

    /**
     * Crea la excepción con el mensaje y la causa indicados.
     *
     * @param mensaje mensaje de la excepción
     * @param causa causa original del error
     */
    public ExcepcionDOPOSoccer(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
