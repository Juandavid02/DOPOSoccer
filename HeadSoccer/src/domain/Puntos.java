package domain;

/**
 * Puntos acumulados por un jugador.
 *
 * <p>El ganador de un partido recibe {@link ParametrosJuego#PUNTOS_GANADOR}
 * y el perdedor {@link ParametrosJuego#PUNTOS_PERDEDOR}. Con esos puntos se
 * compran mejoras de estadísticas al inicio de los partidos.</p>
 */
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

    /**
     * Gasta {@code costo} puntos si alcanzan.
     *
     * @return {@code true} si se pudo pagar; {@code false} en caso contrario
     */
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
