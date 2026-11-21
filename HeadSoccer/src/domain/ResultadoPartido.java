package domain;

import java.io.Serializable;

public class ResultadoPartido implements Serializable {
    private final int golesJugador1;
    private final int golesJugador2;
    private final MotivoFin motivoFin;
    private final double tiempoTotal;

    /**
     * Crea el resultado de un partido.
     *
     * @param golesJugador1 goles del jugador 1
     * @param golesJugador2 goles del jugador 2
     * @param motivoFin motivo por el cual terminó el partido
     * @param tiempoTotal tiempo total jugado
     * @throws IllegalArgumentException si algún marcador de goles es negativo
     */
    public ResultadoPartido(int golesJugador1, int golesJugador2, MotivoFin motivoFin, double tiempoTotal) {
        if (golesJugador1 < 0 || golesJugador2 < 0) {
            throw new IllegalArgumentException("Los goles no pueden ser negativos");
        }
        this.golesJugador1 = golesJugador1;
        this.golesJugador2 = golesJugador2;
        this.motivoFin = motivoFin;
        this.tiempoTotal = tiempoTotal;
    }
    /**
     * Obtiene los goles del jugador 1.
     *
     * @return goles del jugador 1
     */
    public int getGolesJugador1() {
        return golesJugador1;
    }

    /**
     * Obtiene los goles del jugador 2.
     *
     * @return goles del jugador 2
     */
    public int getGolesJugador2() {
        return golesJugador2;
    }

    /**
     * Obtiene el motivo por el cual terminó el partido.
     *
     * @return motivo de fin
     */
    public MotivoFin getMotivoFin() {
        return motivoFin;
    }

    /**
     * Obtiene el tiempo total jugado.
     *
     * @return tiempo total
     */
    public double getTiempoTotal() {
        return tiempoTotal;
    }

    /**
     * Indica si el resultado es un empate.
     *
     * @return true si ambos jugadores tienen los mismos goles
     */
    public boolean hayEmpate() {
        return golesJugador1 == golesJugador2;
    }

    /**
     * Indica si el resultado tiene un ganador.
     *
     * @return true si no hay empate
     */
    public boolean hayGanador() {
        return !hayEmpate();
    }

    /**
     * Obtiene el número del jugador ganador.
     *
     * @return 1 o 2 según el ganador, o 0 si hay empate
     */
    public int getGanador() {
        if (golesJugador1 > golesJugador2) {
            return 1;
        } else if (golesJugador2 > golesJugador1) {
            return 2;
        } else {
            return 0;
        }
    }
}
