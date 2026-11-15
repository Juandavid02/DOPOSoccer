package domain;

/**
 * Efecto temporal que sufre un jugador después de recibir un tiro de poder.
 */
public class Efecto {

    private final TipoEfecto tipo;
    private double tiempoRestante;

    /**
     * Crea un efecto del tipo indicado con la duración dada.
     *
     * @param tipo tipo de efecto
     * @param duracion duración del efecto en segundos
     */
    public Efecto(TipoEfecto tipo, double duracion) {
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de efecto no puede ser nulo");
        }
        if (duracion <= 0) {
            throw new IllegalArgumentException("La duración del efecto debe ser mayor que cero");
        }
        this.tipo = tipo;
        this.tiempoRestante = duracion;
    }

    /**
     * Obtiene el tipo de efecto.
     *
     * @return tipo de efecto
     */
    public TipoEfecto getTipo() {
        return tipo;
    }

    /**
     * Obtiene el tiempo restante del efecto.
     *
     * @return tiempo restante en segundos
     */
    public double getTiempoRestante() {
        return tiempoRestante;
    }

    /**
     * Reinicia el temporizador del efecto con una nueva duración.
     *
     * @param duracion nueva duración en segundos
     */
    public void renovar(double duracion) {
        if (duracion <= 0) {
            return;
        }
        tiempoRestante = duracion;
    }

    /**
     * Descuenta el tiempo transcurrido del efecto.
     *
     * @param dt tiempo transcurrido
     */
    public void actualizar(double dt) {
        if (dt <= 0) {
            return;
        }
        tiempoRestante = Math.max(0, tiempoRestante - dt);
    }

    /**
     * Indica si el efecto ya terminó.
     *
     * @return true si el efecto terminó
     */
    public boolean terminado() {
        return tiempoRestante <= 0;
    }
}
