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
}
