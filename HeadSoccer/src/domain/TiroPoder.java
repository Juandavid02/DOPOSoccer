package domain;

/**
 * Tiro de poder activo. Se crea cuando un jugador con aura toca el balón y
 * permanece activo hasta que golpea al rival, entra al arco o expira.
 */
public class TiroPoder {

    /** Tiempo máximo que el tiro puede estar activo si no impacta. */
    public static final double DURACION_MAXIMA = 6.0;

    private final Jugador dueno;
    private final Poder poder;
    private double tiempoRestante;
    private boolean activo;

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

    public Jugador getDueno() {
        return dueno;
    }

    public Poder getPoder() {
        return poder;
    }

    public boolean isActivo() {
        return activo;
    }

    public void desactivar() {
        activo = false;
    }

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
