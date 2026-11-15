package domain;

/**
 * Barra de poder de un jugador. Se llena con el tiempo y se vacía cuando el
 * jugador activa su tiro de poder.
 */
public class BarraPoder {
    private final double capacidad;
    private final double recargaPorSegundo;
    private double carga;

    /**
     * Crea una barra de poder con la capacidad y la recarga indicadas.
     *
     * @param capacidad capacidad máxima de la barra
     * @param recargaPorSegundo carga que se suma por segundo
     */
    public BarraPoder(double capacidad, double recargaPorSegundo) {
        if (capacidad <= 0) {
            throw new IllegalArgumentException("La capacidad debe ser mayor que 0");
        }
        if (recargaPorSegundo < 0) {
            throw new IllegalArgumentException("La recarga por segundo no puede ser negativa");
        }
        this.capacidad = capacidad;
        this.recargaPorSegundo = recargaPorSegundo;
        this.carga = 0;
    }

    /**
     * Obtiene la carga actual de la barra.
     *
     * @return carga actual
     */
    public double getCarga() {
        return carga;
    }

    /**
     * Obtiene la capacidad máxima de la barra.
     *
     * @return capacidad máxima
     */
    public double getCapacidad() {
        return capacidad;
    }

    /**
     * Obtiene la recarga por segundo de la barra.
     *
     * @return recarga por segundo
     */
    public double getRecargaPorSegundo() {
        return recargaPorSegundo;
    }

    /**
     * Actualiza la carga de la barra según el tiempo transcurrido.
     *
     * @param dt tiempo transcurrido
     */
    public void actualizarBarra(double dt) {
        if (dt <= 0) {
            throw new IllegalArgumentException("El tiempo delta no puede ser negativo ni cero");
        }
        this.carga += recargaPorSegundo * dt;
        if (this.carga > capacidad) {
            this.carga = capacidad;
        }
    }

    /**
     * Indica si la barra está completamente cargada.
     *
     * @return true si la barra está llena
     */
    public boolean isFull() {
        return carga >= capacidad;
    }

    /**
     * Consume la barra si está llena y la reinicia.
     *
     * @return true si se pudo consumir
     */
    public boolean consumir() {
        if (!isFull()) {
            return false;
        }
        reiniciar();
        return true;
    }

    /**
     * Reinicia la carga de la barra a cero.
     */
    public void reiniciar() {
        this.carga = 0;
    }

    /**
     * Suma carga de forma inmediata, sin pasar por la recarga por segundo.
     * Se usa cuando el jugador recibe un gol.
     *
     * @param cantidad unidades de carga que se suman
     */
    public void sumarCarga(double cantidad) {
        if (cantidad < 0) {
            throw new IllegalArgumentException("La carga a sumar no puede ser negativa");
        }
        this.carga = Math.min(capacidad, this.carga + cantidad);
    }

    /**
     * Suma una fracción de la capacidad total.
     *
     * @param fraccion fracción entre 0 y 1, por ejemplo 0.25 para un cuarto
     */
    public void sumarFraccion(double fraccion) {
        if (fraccion < 0) {
            throw new IllegalArgumentException("La fracción no puede ser negativa");
        }
        sumarCarga(capacidad * fraccion);
    }
}
