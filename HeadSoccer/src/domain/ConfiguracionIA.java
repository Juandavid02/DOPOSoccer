package domain;

import java.io.Serializable;

/**
 * Configuración de la máquina: perfil de comportamiento y estadísticas
 * físicas ajustables por el jugador humano.
 */
public class ConfiguracionIA implements Serializable {

    private static final String NOMBRE = "IA";

    private final PerfilIA perfil;
    private final Estadisticas estadisticas;

    /**
     * Crea la configuración de la IA con el perfil y las estadísticas indicados.
     *
     * @param perfil perfil de comportamiento de la IA
     * @param estadisticas estadísticas físicas de la IA
     */
    public ConfiguracionIA(PerfilIA perfil, Estadisticas estadisticas) {
        if (perfil == null) {
            throw new IllegalArgumentException("El perfil de la IA no puede ser nulo");
        }
        if (estadisticas == null) {
            throw new IllegalArgumentException("Las estadísticas de la IA no pueden ser nulas");
        }
        this.perfil = perfil;
        this.estadisticas = estadisticas.copiar();
    }

    /**
     * Obtiene el perfil de comportamiento de la IA.
     *
     * @return perfil de la IA
     */
    public PerfilIA getPerfil() {
        return perfil;
    }

    /**
     * Devuelve una copia de las estadísticas para que nadie modifique la
     * configuración guardada.
     *
     * @return estadísticas de la IA
     */
    public Estadisticas getEstadisticas() {
        return estadisticas.copiar();
    }

    /**
     * Crea la configuración de jugador que usará la máquina en el partido.
     *
     * @return configuración con el nombre "IA" y sus estadísticas
     */
    public ConfiguracionJugador crearConfiguracionJugador() {
        try {
            return new ConfiguracionJugador(
                    NOMBRE + " · " + perfil.name(),
                    estadisticas.copiar(),
                    new Puntos());
        } catch (ExcepcionDOPOSoccer e) {
            throw new IllegalStateException(
                    "No se pudo crear la configuración de la IA", e);
        }
    }
}
