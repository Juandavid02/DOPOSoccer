package domain;

public class BarraPoder {
    private final double capacidad;
    private final double recargaPorSegundo;
    private double carga;

    public BarraPoder(double capacidad, double recargaPorSegundo){
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

    public double getCarga() {
        return carga;
    }

    public double getCapacidad() {
        return capacidad;
    }

    public double getRecargaPorSegundo() {
        return recargaPorSegundo;
    }

    public void actualizarBarra(double dt){
        if (dt <= 0) {
            throw new IllegalArgumentException("El tiempo delta no puede ser negativo ni cero");
        }
        this.carga += recargaPorSegundo * dt;
        if (this.carga > capacidad) {
            this.carga = capacidad;
        }
    }
    
    public boolean isfull() {
        return carga >= capacidad;
    }

    public boolean consumir() {
        if (isfull()) {
            reiniciar();
            return true;
        } else {
            return false;
        }
    }

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
