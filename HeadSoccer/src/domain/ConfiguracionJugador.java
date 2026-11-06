package domain;

/**
 * Datos de configuración de un jugador antes de iniciar un partido.
 *
 * <p>Agrupa el nombre, las estadísticas propias del jugador (speed, jump,
 * kick y survival) y sus puntos acumulados. Las estadísticas pertenecen al
 * jugador, no al personaje, tal como pide el PDF final.</p>
 */
public class ConfiguracionJugador {
    private final String nombre;
    private final Estadisticas estadisticas;
    private final Puntos puntos;

    public ConfiguracionJugador(String nombre, Estadisticas estadisticas) {
        this(nombre, estadisticas, new Puntos());
    }

    public ConfiguracionJugador(String nombre, Estadisticas estadisticas, Puntos puntos) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede ser nulo o vacío");
        }
        if (estadisticas == null) {
            throw new IllegalArgumentException("Las estadísticas no pueden ser nulas");
        }
        if (puntos == null) {
            throw new IllegalArgumentException("Los puntos no pueden ser nulos");
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
