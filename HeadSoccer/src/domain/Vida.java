package domain;

public class Vida {
    private double actual;
    private double maxima;

    /**
     * Crea la vida con su valor máximo.
     *
     * @param maxima vida máxima
     * @throws IllegalArgumentException si la vida máxima no es mayor que cero
     */
    public Vida(double maxima) {
        if (maxima <= 0) {
            throw new IllegalArgumentException("La vida máxima debe ser mayor que cero");
        }
        this.maxima = maxima;
        this.actual = maxima;
    }

    /**
     * Obtiene la vida actual.
     *
     * @return vida actual
     */
    public double getActual() {
        return actual;
    }

    /**
     * Obtiene la vida máxima.
     *
     * @return vida máxima
     */
    public double getMaxima() {
        return maxima;
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

    /**
     * Reduce la vida actual por el daño recibido.
     *
     * @param danio daño recibido
     * @throws IllegalArgumentException si el daño es negativo
     */
    public void recibirDanio(double danio) {
        if (danio < 0) {
            throw new IllegalArgumentException("El daño no puede ser negativo");
        }
        actual -= danio;
        if (actual < 0) {
            actual = 0;
        }
    }

    /**
     * Indica si la vida llegó a cero.
     *
     * @return true si la vida actual es cero
     */
    public boolean estaEnCero() {
        return actual <= 0;
    }

    /**
     * Restaura la vida actual a su valor máximo.
     */
    public void reiniciar() {
        actual = maxima;
    }
}
