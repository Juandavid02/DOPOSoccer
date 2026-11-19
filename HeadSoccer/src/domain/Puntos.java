package domain;

import java.io.Serializable;

public class Puntos implements Serializable {

    private static final long serialVersionUID = 1L;

    private int total;

    /**
     * Crea un contador de puntos en cero.
     */
    public Puntos() {
        this.total = 0;
    }

    /**
     * Obtiene el total de puntos acumulados.
     *
     * @return total de puntos
     */
    public int getTotal() {
        return total;
    }

    /**
     * Suma puntos al total.
     *
     * @param puntos puntos a sumar
     * @throws IllegalArgumentException si los puntos son negativos
     */
    public void sumar(int puntos) {
        if (puntos < 0) {
            throw new IllegalArgumentException("No se pueden sumar puntos negativos");
        }
        this.total += puntos;
    }

    /**
     * Indica si hay puntos suficientes para pagar un costo.
     *
     * @param costo costo a pagar
     * @return true si se puede pagar
     * @throws IllegalArgumentException si el costo es negativo
     */
    public boolean puedePagar(int costo) {
        if (costo < 0) {
            throw new IllegalArgumentException("El costo no puede ser negativo");
        }
        return total >= costo;
    }

    /**
     * Gasta puntos del total si hay suficientes.
     *
     * @param costo costo a gastar
     * @return true si se realizó el gasto
     */
    public boolean gastar(int costo) {
        if (!puedePagar(costo)) {
            return false;
        }
        this.total -= costo;
        return true;
    }

}
