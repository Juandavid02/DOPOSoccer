package domain;

public class Marcador {
    private int golesJugador1;
    private int golesJugador2;
    
    public Marcador() {
        this.golesJugador1 = 0;
        this.golesJugador2 = 0;
    }

    public int getGoles(int jugador) {
        if (jugador != 1 && jugador != 2) {
            throw new IllegalArgumentException("El número de jugador debe ser 1 o 2");
        }
        if (jugador == 1) {
            return golesJugador1;
        } else {
            return golesJugador2;
        }
    }

    public void sumarGol(int jugador) {
        if (jugador != 1 && jugador != 2) {
            throw new IllegalArgumentException("El número de jugador debe ser 1 o 2");
        }
        if (jugador == 1) {
            golesJugador1++;
        } else if (jugador == 2) {
            golesJugador2++;
        }
    }

    public boolean hayEmpate() {
        return golesJugador1 == golesJugador2;
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

    public void reiniciar(){
        this.golesJugador1 = 0;
        this.golesJugador2 = 0;
    } 

}
