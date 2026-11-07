package domain;

public class Puntos {
    private int total;

    public Puntos() {
        this(0);
    }

    public Puntos(int total) {
        if (total < 0) {
            throw new IllegalArgumentException("Los puntos no pueden ser negativos");
        }
        this.total = total;
    }

    public int getTotal() {
        return total;
    }

    public void sumar(int puntos) {
        if (puntos < 0) {
            throw new IllegalArgumentException("No se pueden sumar puntos negativos");
        }
        this.total += puntos;
    }

    public boolean puedePagar(int costo) {
        if (costo < 0) {
            throw new IllegalArgumentException("El costo no puede ser negativo");
        }
        return total >= costo;
    }

    public boolean gastar(int costo) {
        if (!puedePagar(costo)) {
            return false;
        }
        this.total -= costo;
        return true;
    }

    public void reiniciar() {
        this.total = 0;
    }
}
