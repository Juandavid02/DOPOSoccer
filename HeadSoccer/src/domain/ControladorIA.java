package domain;

import java.io.Serializable;

/**
 * Base de los controladores de la máquina.
 *
 * Define el flujo común de actualización y deja en cada subclase la
 * decisión concreta según el perfil de IA. Aplica el patrón Template
 * Method.
 */
public abstract class ControladorIA implements Serializable {

    /** Tiempo que debe esperar la IA antes de volver a saltar. */
    protected double tiempoSalto;

    /**
     * Actualiza las decisiones de la máquina para este instante.
     *
     * @param partido partido en curso
     * @param yo jugador controlado por la IA
     * @param dt tiempo transcurrido desde la última actualización
     */
    public final void actualizar(Partido partido, Jugador yo, double dt) {
        if (dt <= 0 || yo.isAturdido()) {
            return;
        }
        tiempoSalto -= dt;
        decidir(partido, yo, dt);
    }

    /**
     * Decide y ejecuta las acciones de la máquina para este instante.
     *
     * @param partido partido en curso
     * @param yo jugador controlado por la IA
     * @param dt tiempo transcurrido desde la última actualización
     */
    protected abstract void decidir(Partido partido, Jugador yo, double dt);

    /**
     * Mueve al jugador en la dirección indicada a su velocidad actual.
     *
     * @param partido partido en curso
     * @param yo jugador controlado por la IA
     * @param direccion dirección del movimiento
     * @param dt tiempo transcurrido desde la última actualización
     */
    protected void mover(Partido partido, Jugador yo, double direccion, double dt) {
        double velocidad = ParametrosJuego.VELOCIDAD_BASE_JUGADOR * yo.getEstadisticas().getFactorVelocidad() * yo.getMultiplicadorVelocidad();
        partido.mover(numero(partido, yo), direccion * velocidad * dt);
    }

    /**
     * Indica si el jugador está cerca del balón.
     *
     * @param yo jugador controlado por la IA
     * @param balon balón del partido
     * @return true si el balón está dentro del alcance
     */
    protected static boolean cercaDelBalon(Jugador yo, Balon balon) {
        double dx = Math.abs(balon.getX() - yo.getPosicionX());
        double dy = Math.abs(balon.getY() - yo.getPosicionY());
        return dx <= ParametrosJuego.ALCANCE_PATADA + 15 && dy <= 95;
    }

    /**
     * Devuelve el número del jugador dentro del partido.
     *
     * @param partido partido en curso
     * @param jugador jugador consultado
     * @return número del jugador: 1 o 2
     */
    protected static int numero(Partido partido, Jugador jugador) {
        return partido.getJugadores().indexOf(jugador) + 1;
    }
}
