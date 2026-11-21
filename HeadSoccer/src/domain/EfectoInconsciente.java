package domain;

/**
 * Efecto Inconsciente: el jugador sale volando y no puede moverse, saltar
 * ni patear mientras dure.
 */
public class EfectoInconsciente extends Efecto {

    /**
     * Crea el efecto Inconsciente.
     *
     * @param duracion duración del efecto en segundos
     */
    public EfectoInconsciente(double duracion) {
        super(TipoEfecto.INCONSCIENTE, duracion);
    }

    /**
     * Deja al jugador quieto en horizontal y lo lanza hacia arriba.
     *
     * @param jugador jugador que queda inconsciente
     */
    @Override
    public void alAplicar(Jugador jugador) {
        jugador.impulsar(0, -ParametrosJuego.IMPULSO_INCONSCIENTE);
    }

    /**
     * Frena el movimiento horizontal cuando se despierta.
     *
     * @param jugador jugador que tenía el efecto
     */
    @Override
    public void alTerminar(Jugador jugador) {
        jugador.detenerHorizontal();
    }

    @Override
    public boolean bloqueaMovimiento() {
        return true;
    }

    @Override
    public boolean bloqueaSalto() {
        return true;
    }

    @Override
    public boolean bloqueaPatada() {
        return true;
    }
}
