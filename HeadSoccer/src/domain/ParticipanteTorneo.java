package domain;

/**
 * Participante de un torneo: el perfil humano con su personaje o una máquina
 * con su perfil de comportamiento y sus estadísticas.
 */
public class ParticipanteTorneo {

    private final PerfilJugador perfilHumano;
    private final PerfilIA perfilIA;
    private final Personaje personaje;
    private Estadisticas estadisticas;
    private boolean eliminado;

    /**
     * Crea un participante de torneo con todos sus datos.
     *
     * @param perfilHumano perfil humano o null si es máquina
     * @param perfilIA perfil de IA o null si es humano
     * @param personaje personaje del participante
     * @param estadisticas estadísticas iniciales
     */
    private ParticipanteTorneo(PerfilJugador perfilHumano,
                               PerfilIA perfilIA,
                               Personaje personaje,
                               Estadisticas estadisticas) {
        if (personaje == null) {
            throw new IllegalArgumentException("El personaje no puede ser nulo");
        }
        if (estadisticas == null) {
            throw new IllegalArgumentException("Las estadísticas no pueden ser nulas");
        }
        this.perfilHumano = perfilHumano;
        this.perfilIA = perfilIA;
        this.personaje = personaje;
        this.estadisticas = estadisticas.copiar();
    }

    /**
     * Crea un participante humano.
     *
     * @param perfil perfil del jugador humano
     * @param personaje personaje elegido
     * @return participante humano
     */
    public static ParticipanteTorneo humano(PerfilJugador perfil,
                                            Personaje personaje) {
        if (perfil == null) {
            throw new IllegalArgumentException("El perfil humano no puede ser nulo");
        }
        return new ParticipanteTorneo(perfil, null, personaje,
                perfil.getEstadisticas());
    }

    /**
     * Crea un participante controlado por la máquina.
     *
     * @param perfil perfil de comportamiento de la IA
     * @param personaje personaje elegido
     * @return participante máquina
     */
    public static ParticipanteTorneo maquina(PerfilIA perfil,
                                             Personaje personaje) {
        if (perfil == null) {
            throw new IllegalArgumentException("El perfil de IA no puede ser nulo");
        }
        return new ParticipanteTorneo(null, perfil, personaje,
                estadisticasBase(perfil));
    }

    /**
     * Devuelve las estadísticas base de un perfil de IA.
     *
     * @param perfil perfil de IA
     * @return estadísticas base del perfil
     */
    private static Estadisticas estadisticasBase(PerfilIA perfil) {
        int valor;
        switch (perfil) {
            case TORPE:
                valor = ParametrosJuego.ESTADISTICAS_IA_TORPE;
                break;
            case OFENSIVA:
                valor = ParametrosJuego.ESTADISTICAS_IA_OFENSIVA;
                break;
            case DEFENSIVA:
                valor = ParametrosJuego.ESTADISTICAS_IA_DEFENSIVA;
                break;
            case EXPERTA:
                valor = ParametrosJuego.ESTADISTICAS_IA_EXPERTA;
                break;
            default:
                valor = ParametrosJuego.ESTADISTICA_INICIAL;
                break;
        }
        return new Estadisticas(valor, valor, valor, valor);
    }

    /**
     * Indica si el participante es humano.
     *
     * @return true si es humano
     */
    public boolean isHumano() {
        return perfilHumano != null;
    }

    /**
     * Devuelve el nombre visible del participante.
     *
     * @return nombre del participante
     */
    public String getNombre() {
        if (isHumano()) {
            return perfilHumano.getNombre();
        }
        return "IA · " + perfilIA.name();
    }

    /**
     * Devuelve el perfil de IA del participante.
     *
     * @return perfil de IA o null si es humano
     */
    public PerfilIA getPerfilIA() {
        return perfilIA;
    }

    /**
     * Devuelve el personaje del participante.
     *
     * @return personaje elegido
     */
    public Personaje getPersonaje() {
        return personaje;
    }

    /**
     * Devuelve una copia de las estadísticas del participante.
     *
     * @return estadísticas del participante
     */
    public Estadisticas getEstadisticas() {
        return estadisticas.copiar();
    }

    /**
     * Suma la mejora de ronda a todas las estadísticas de una IA. El humano
     * no se mejora automáticamente.
     */
    public void mejorar() {
        if (isHumano()) {
            return;
        }
        int maximo = ParametrosJuego.MAX_ESTADISTICA;
        int mejora = ParametrosJuego.MEJORA_IA_TORNEO;
        estadisticas = new Estadisticas(
                Math.min(maximo, estadisticas.getSpeed() + mejora),
                Math.min(maximo, estadisticas.getJump() + mejora),
                Math.min(maximo, estadisticas.getKick() + mejora),
                Math.min(maximo, estadisticas.getSurvival() + mejora));
    }

    /**
     * Indica si el participante está eliminado.
     *
     * @return true si fue eliminado
     */
    public boolean isEliminado() {
        return eliminado;
    }

    /**
     * Marca al participante como eliminado.
     */
    public void eliminar() {
        this.eliminado = true;
    }

    /**
     * Configuración de IA para el partido, o null si el participante es
     * humano.
     *
     * @return configuración de máquina
     */
    public ConfiguracionIA crearConfiguracionIA() {
        if (isHumano()) {
            return null;
        }
        return new ConfiguracionIA(perfilIA, estadisticas);
    }
}
