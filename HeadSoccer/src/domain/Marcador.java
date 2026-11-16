package domain;

public class Marcador {
    private int golesJugador1;
    private int golesJugador2;

    /**
     * Crea un marcador en cero.
     */
    public Marcador() {
        this.golesJugador1 = 0;
        this.golesJugador2 = 0;
    }

    /**
     * Obtiene los goles de un jugador.
     *
     * @param jugador número de jugador, 1 o 2
     * @return goles del jugador indicado
     * @throws IllegalArgumentException si el número de jugador no es 1 ni 2
     */
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

    /**
     * Suma un gol al jugador indicado.
     *
     * @param jugador número de jugador, 1 o 2
     * @throws IllegalArgumentException si el número de jugador no es 1 ni 2
     */
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

    /**
     * Indica si el marcador está empatado.
     *
     * @return true si ambos jugadores tienen los mismos goles
     */
    public boolean hayEmpate() {
        return golesJugador1 == golesJugador2;
    }

    /**
     * Obtiene el número del jugador con más goles.
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

    /**
     * Reinicia los goles de ambos jugadores a cero.
     */
    public void reiniciar(){
        this.golesJugador1 = 0;
        this.golesJugador2 = 0;
    }

}
