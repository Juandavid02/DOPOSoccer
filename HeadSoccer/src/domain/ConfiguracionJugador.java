package domain;

public class ConfiguracionJugador {
    private final String nombre;
    private final Estadisticas estadisticas;
    private final Puntos puntos;

    public ConfiguracionJugador(String nombre, Estadisticas estadisticas)
            throws ExcepcionDOPOSoccer {
        this(nombre, estadisticas, new Puntos());
    }

    public ConfiguracionJugador(String nombre, Estadisticas estadisticas, Puntos puntos)
            throws ExcepcionDOPOSoccer {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ExcepcionDOPOSoccer(ExcepcionDOPOSoccer.NOMBRE_INVALIDO);
        }
        if (estadisticas == null) {
            throw new ExcepcionDOPOSoccer(ExcepcionDOPOSoccer.ESTADISTICAS_NULAS);
        }
        if (puntos == null) {
            throw new ExcepcionDOPOSoccer(ExcepcionDOPOSoccer.PUNTOS_NULOS);
        }
        this.nombre = nombre;
        this.estadisticas = estadisticas;
        this.puntos = puntos;
    }

    public String getNombre() {
        return nombre;
    }

    public Estadisticas getEstadisticas() {
        return estadisticas;
    }

    public Puntos getPuntos() {
        return puntos;
    }
}
