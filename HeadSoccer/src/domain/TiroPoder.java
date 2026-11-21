package domain;

import java.io.Serializable;

/**
 * Tiro de poder activo. Se crea cuando un jugador con aura toca el balón y
 * permanece activo hasta que golpea al rival, entra al arco o expira.
 */
public class TiroPoder implements Serializable {

    /** Tiempo máximo que el tiro puede estar activo si no impacta. */
    private static final double DURACION_MAXIMA = 6.0;

    private final Jugador dueno;
    private final Poder poder;
    private double tiempoRestante;
    private boolean activo;

    /**
     * Crea un tiro de poder activo.
     *
     * @param dueno jugador que lanzó el tiro
     * @param poder poder aplicado por el tiro
     * @throws IllegalArgumentException si el dueño o el poder son nulos
     */
    public TiroPoder(Jugador dueno, Poder poder) {
        if (dueno == null) {
            throw new IllegalArgumentException("El dueño del tiro no puede ser nulo");
        }
        if (poder == null) {
            throw new IllegalArgumentException("El poder del tiro no puede ser nulo");
        }
        this.dueno = dueno;
        this.poder = poder;
        this.tiempoRestante = DURACION_MAXIMA;
        this.activo = true;
    }

    /**
     * Obtiene el jugador que lanzó el tiro.
     *
     * @return dueño del tiro
     */
    public Jugador getDueno() {
        return dueno;
    }

    /**
     * Obtiene el poder aplicado por el tiro.
     *
     * @return poder del tiro
     */
    public Poder getPoder() {
        return poder;
    }

    /**
     * Indica si el tiro sigue activo.
     *
     * @return true si está activo
     */
    public boolean isActivo() {
        return activo;
    }

    /**
     * Desactiva el tiro.
     */
    public void desactivar() {
        activo = false;
    }

    /**
     * Actualiza el tiempo restante del tiro.
     *
     * @param dt tiempo transcurrido
     */
    public void actualizar(double dt) {
        if (!activo || dt <= 0) {
            return;
        }
        tiempoRestante -= dt;
        if (tiempoRestante <= 0) {
            activo = false;
        }
    }
}
