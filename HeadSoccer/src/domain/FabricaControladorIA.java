package domain;

/**
 * Crea el controlador de IA que corresponde a cada perfil.
 */
public final class FabricaControladorIA {

    private FabricaControladorIA() {
    }

    /**
     * Crea el controlador del perfil indicado.
     *
     * @param perfil perfil de inteligencia artificial
     * @return controlador concreto
     * @throws IllegalArgumentException si el perfil es nulo
     */
    public static ControladorIA crear(PerfilIA perfil) {
        if (perfil == null) {
            throw new IllegalArgumentException("El perfil de la IA no puede ser nulo");
        }

        switch (perfil) {
            case TORPE:
                return new ControladorTorpe();
            case OFENSIVA:
                return new ControladorOfensivo();
            case DEFENSIVA:
                return new ControladorDefensivo();
            case EXPERTA:
                return new ControladorExperto();
            default:
                throw new IllegalArgumentException("Perfil de IA desconocido: " + perfil);
        }
    }
}
