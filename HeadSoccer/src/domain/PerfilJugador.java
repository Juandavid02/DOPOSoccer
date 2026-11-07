package domain;

import java.util.UUID;

/**
 * Perfil de una persona. No tiene lado fijo ni personaje: en una partida
 * puede ocupar cualquiera de los dos lados y el personaje se elige después.
 */
public class PerfilJugador {

    private static final int ESTADISTICA_INICIAL = 5;

    private final String id;
    private String nombre;
    private ConfiguracionJugador configuracion;

    /**
     * Crea un perfil nuevo con estadísticas iniciales y cero puntos.
     *
     * @param nombre el nombre del perfil
     */
    public PerfilJugador(String nombre) {
        this(UUID.randomUUID().toString(), nombre, estadisticasIniciales(nombre));
    }

    /**
     * Crea un perfil con todos sus datos.
     *
     * @param id identificador único del perfil
     * @param nombre nombre del perfil
     * @param configuracion configuración con estadísticas y puntos
     */
    public PerfilJugador(String id, String nombre, ConfiguracionJugador configuracion) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("El id no puede ser nulo o vacío");
        }
        validarNombre(nombre);
        if (configuracion == null) {
            throw new IllegalArgumentException("La configuración no puede ser nula");
        }
        this.id = id;
        this.nombre = nombre;
        this.configuracion = configuracion;
    }

    private static ConfiguracionJugador estadisticasIniciales(String nombre) {
        validarNombre(nombre);
        Estadisticas estadisticas = new Estadisticas(
                ESTADISTICA_INICIAL,
                ESTADISTICA_INICIAL,
                ESTADISTICA_INICIAL,
                ESTADISTICA_INICIAL);
        return new ConfiguracionJugador(nombre, estadisticas);
    }

    private static void validarNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede ser nulo o vacío");
        }
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    /**
     * Cambia el nombre del perfil conservando sus estadísticas y puntos.
     *
     * @param nombre el nuevo nombre
     */
    public void setNombre(String nombre) {
        validarNombre(nombre);
        this.nombre = nombre;
        this.configuracion = new ConfiguracionJugador(
                nombre,
                configuracion.getEstadisticas(),
                configuracion.getPuntos());
    }

    public ConfiguracionJugador getConfiguracion() {
        return configuracion;
    }

    public Estadisticas getEstadisticas() {
        return configuracion.getEstadisticas();
    }

    public Puntos getPuntos() {
        return configuracion.getPuntos();
    }
}
