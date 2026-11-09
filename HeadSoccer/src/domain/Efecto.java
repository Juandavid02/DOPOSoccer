package domain;

/**
 * Efecto temporal que sufre un jugador después de recibir un tiro de poder.
 */
public class Efecto {

    private final TipoEfecto tipo;
    private final double duracion;
    private double tiempoRestante;

    public Efecto(TipoEfecto tipo, double duracion) {
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de efecto no puede ser nulo");
        }
        if (duracion <= 0) {
            throw new IllegalArgumentException("La duración del efecto debe ser mayor que cero");
        }
        this.tipo = tipo;
        this.duracion = duracion;
        this.tiempoRestante = duracion;
    }

    public TipoEfecto getTipo() {
        return tipo;
    }

    public double getDuracion() {
        return duracion;
    }

    public double getTiempoRestante() {
        return tiempoRestante;
    }

    public void actualizar(double dt) {
        if (dt <= 0) {
            return;
        }
        tiempoRestante = Math.max(0, tiempoRestante - dt);
    }

    public boolean terminado() {
        return tiempoRestante <= 0;
    }
}
