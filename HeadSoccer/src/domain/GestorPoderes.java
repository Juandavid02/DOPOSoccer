package domain;

import java.io.Serializable;

/**
 * Administra el tiro de poder activo de un partido: lo crea cuando un aura
 * toca el balón y lo aplica al rival cuando el balón lo alcanza.
 */
public class GestorPoderes implements Serializable {

    private static final long serialVersionUID = 1L;

    private TiroPoder tiroPoder;

    /**
     * Actualiza el temporizador del tiro de poder activo.
     *
     * @param dt tiempo transcurrido en segundos
     */
    public void actualizar(double dt) {
        if (tiroPoder != null) {
            tiroPoder.actualizar(dt);
        }
    }

    /**
     * Reacciona a un contacto entre un jugador y el balón.
     *
     * @param jugador jugador que tocó el balón
     * @param balon balón del partido
     */
    public void alContactarBalon(Jugador jugador, Balon balon) {
        if (jugador.isAuraActiva()) {
            disparar(jugador, balon);
        }

        // Un tiro de poder activo golpea al rival (nunca a su dueño).
        if (tiroPoder != null && tiroPoder.isActivo() && tiroPoder.getDueno() != jugador) {
            impactar(jugador);
        }
    }

    /**
     * Devuelve el tiro de poder activo.
     *
     * @return tiro de poder o null si no hay ninguno
     */
    public TiroPoder getTiroPoder() {
        return tiroPoder;
    }

    /**
     * Descarta el tiro de poder activo.
     */
    public void limpiar() {
        tiroPoder = null;
    }

    /**
     * Consume el aura del jugador y lanza su tiro de poder.
     */
    private void disparar(Jugador jugador, Balon balon) {
        Poder poder = jugador.getPersonaje().getPoder();
        jugador.consumirAura();

        double direccion = -1;
        if (jugador.getLado() == Lado.IZQUIERDO) {
            direccion = 1;
        }
        balon.establecerVelocidad(direccion * poder.getImpulsoX(), poder.getImpulsoY());

        tiroPoder = new TiroPoder(jugador, poder);
    }

    /**
     * Aplica al rival el daño y el efecto del tiro de poder activo.
     */
    private void impactar(Jugador rival) {
        Jugador dueno = tiroPoder.getDueno();
        Poder poder = tiroPoder.getPoder();

        double dano = poder.getDanoTiro(dueno) * dueno.getEstadisticas().getMultiplicadorKick();
        rival.recibirDanio(dano);
        rival.aplicarEfecto(poder.crearEfecto());

        tiroPoder.desactivar();
    }
}
