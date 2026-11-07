package domain;

public class Vida {
    private double actual;
    private double maxima;

    public Vida(double maxima) {
        if (maxima <= 0) {
            throw new IllegalArgumentException("La vida máxima debe ser mayor que cero");
        }
        this.maxima = maxima;
        this.actual = maxima;
    }

    public double getActual() {
        return actual;
    }

    public double getMaxima() {
        return maxima;
    }

    public void recibirDanio(double danio) {
        if (danio < 0) {
            throw new IllegalArgumentException("El daño no puede ser negativo");
        }
        actual -= danio;
        if (actual < 0) {
            actual = 0;
        }
    }

    public boolean estaEnCero() {
        return actual <= 0;
    }

    public void reiniciar() {
        actual = maxima;
    }
}
