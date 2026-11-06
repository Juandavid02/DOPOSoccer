package domain;

public class ResultadoPartido {
    private final int golesJugador1;
    private final int golesJugador2;
    private final MotivoFin motivoFin;
    private final double tiempoTotal;

    public ResultadoPartido(int golesJugador1, int golesJugador2, MotivoFin motivoFin, double tiempoTotal) {
        if (golesJugador1 < 0 || golesJugador2 < 0) {
            throw new IllegalArgumentException("Los goles no pueden ser negativos");
        }
        this.golesJugador1 = golesJugador1;
        this.golesJugador2 = golesJugador2;
        this.motivoFin = motivoFin;
        this.tiempoTotal = tiempoTotal;
    }
    public int getGolesJugador1() {
        return golesJugador1;
    }

    public int getGolesJugador2() {
        return golesJugador2;
    }

    public MotivoFin getMotivoFin() {
        return motivoFin;
    }

    public double getTiempoTotal() {
        return tiempoTotal;
    }

    public boolean hayEmpate() {
        return golesJugador1 == golesJugador2;
    }

    public boolean hayGanador() {
        return !hayEmpate();
    }

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
