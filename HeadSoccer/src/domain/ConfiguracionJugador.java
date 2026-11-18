package domain;

public class ConfiguracionJugador {
    private final String nombre;
    private final Estadisticas estadisticas;
    private final Puntos puntos;

    /**
     * Crea la configuración de un jugador con puntos nuevos.
     *
     * @param nombre nombre del jugador
     * @param estadisticas estadísticas físicas del jugador
     * @throws ExcepcionDOPOSoccer si el nombre o las estadísticas no son válidos
     */
    public ConfiguracionJugador(String nombre, Estadisticas estadisticas) throws ExcepcionDOPOSoccer {
        this(nombre, estadisticas, new Puntos());
    }

    /**
     * Crea la configuración de un jugador con los datos indicados.
     *
     * @param nombre nombre del jugador
     * @param estadisticas estadísticas físicas del jugador
     * @param puntos puntos del jugador
     * @throws ExcepcionDOPOSoccer si algún dato no es válido
     */
    public ConfiguracionJugador(String nombre, Estadisticas estadisticas, Puntos puntos) throws ExcepcionDOPOSoccer {
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

    /**
     * Obtiene el nombre del jugador.
     *
     * @return nombre del jugador
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Obtiene las estadísticas del jugador.
     *
     * @return estadísticas del jugador
     */
    public Estadisticas getEstadisticas() {
        return estadisticas;
    }

    /**
     * Obtiene los puntos del jugador.
     *
     * @return puntos del jugador
     */
    public Puntos getPuntos() {
        return puntos;
    }
}
