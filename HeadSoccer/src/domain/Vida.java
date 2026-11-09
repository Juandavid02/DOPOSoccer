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

    /**
     * Cambia la vida máxima. Si la vida actual supera el nuevo máximo,
     * se ajusta para no quedar por encima.
     *
     * @param maxima nueva vida máxima
     */
    public void setMaxima(double maxima) {
        if (maxima <= 0) {
            throw new IllegalArgumentException("La vida máxima debe ser mayor que cero");
        }
        this.maxima = maxima;
        if (this.actual > maxima) {
            this.actual = maxima;
        }
    }

    /**
     * Cambia la vida máxima conservando el porcentaje de vida actual. Se usa
     * cuando un efecto modifica temporalmente las estadísticas del jugador.
     *
     * @param maxima nueva vida máxima
     */
    public void ajustarMaximaManteniendoProporcion(double maxima) {
        if (maxima <= 0) {
            throw new IllegalArgumentException("La vida máxima debe ser mayor que cero");
        }
        double proporcion = this.maxima > 0 ? this.actual / this.maxima : 1;
        this.maxima = maxima;
        this.actual = Math.max(0, Math.min(maxima, maxima * proporcion));
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
