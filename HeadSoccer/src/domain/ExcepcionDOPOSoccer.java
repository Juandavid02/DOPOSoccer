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

    public ExcepcionDOPOSoccer(String mensaje) {
        super(mensaje);
    }

    public ExcepcionDOPOSoccer(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
