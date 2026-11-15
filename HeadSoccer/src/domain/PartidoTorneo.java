package domain;

/**
 * Partido de una llave de torneo. Al registrarse, el perdedor queda
 * eliminado.
 */
public class PartidoTorneo {

    private final ParticipanteTorneo primero;
    private final ParticipanteTorneo segundo;

    private int golesPrimero;
    private int golesSegundo;
    private boolean jugado;

    /**
     * Crea un partido de torneo entre dos participantes.
     *
     * @param primero primer participante
     * @param segundo segundo participante
     */
    public PartidoTorneo(ParticipanteTorneo primero,
                         ParticipanteTorneo segundo) {
        if (primero == null || segundo == null) {
            throw new IllegalArgumentException(
                    "Los participantes del partido no pueden ser nulos");
        }
        if (primero == segundo) {
            throw new IllegalArgumentException(
                    "Un participante no puede jugar contra sí mismo");
        }
        this.primero = primero;
        this.segundo = segundo;
    }

    /**
     * Registra el marcador y elimina al perdedor.
     *
     * @param golesPrimero goles del primer participante
     * @param golesSegundo goles del segundo participante
     */
    public void registrar(int golesPrimero, int golesSegundo) {
        if (jugado) {
            throw new IllegalStateException("El partido ya fue registrado");
        }
        if (golesPrimero < 0 || golesSegundo < 0) {
            throw new IllegalArgumentException("Los goles no pueden ser negativos");
        }
        if (golesPrimero == golesSegundo) {
            throw new IllegalArgumentException(
                    "Un partido de torneo no puede terminar empatado");
        }
        this.golesPrimero = golesPrimero;
        this.golesSegundo = golesSegundo;
        this.jugado = true;
        getPerdedor().eliminar();
    }

    /**
     * Devuelve el primer participante del partido.
     *
     * @return primer participante
     */
    public ParticipanteTorneo getPrimero() {
        return primero;
    }

    /**
     * Devuelve el segundo participante del partido.
     *
     * @return segundo participante
     */
    public ParticipanteTorneo getSegundo() {
        return segundo;
    }

    /**
     * Indica si el partido ya fue registrado.
     *
     * @return true si ya se registró el marcador
     */
    public boolean isJugado() {
        return jugado;
    }

    /**
     * Devuelve el ganador del partido.
     *
     * @return ganador o null si aún no se jugó
     */
    public ParticipanteTorneo getGanador() {
        if (!jugado) {
            return null;
        }
        return golesPrimero > golesSegundo ? primero : segundo;
    }

    /**
     * Devuelve el perdedor del partido.
     *
     * @return perdedor o null si aún no se jugó
     */
    public ParticipanteTorneo getPerdedor() {
        if (!jugado) {
            return null;
        }
        return golesPrimero > golesSegundo ? segundo : primero;
    }

}
