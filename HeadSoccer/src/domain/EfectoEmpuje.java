package domain;

/**
 * Efecto Empuje: lanza al jugador en la dirección en la que mira y lo deja
 * en el aire.
 */
public class EfectoEmpuje extends Efecto {

    /**
     * Crea el efecto Empuje.
     *
     * @param duracion duración del efecto en segundos
     */
    public EfectoEmpuje(double duracion) {
        super(TipoEfecto.EMPUJE, duracion);
    }

    /**
     * Empuja al jugador hacia atrás, hacia su propio arco.
     *
     * @param jugador jugador que recibe el empujón
     */
    @Override
    public void alAplicar(Jugador jugador) {
        double direccion = -1;
        if (jugador.getLado() != Lado.IZQUIERDO) {
            direccion = 1;
        }
        jugador.impulsar(direccion * ParametrosJuego.IMPULSO_EMPUJE,-ParametrosJuego.IMPULSO_EMPUJE_VERTICAL);
    }

    /**
     * Frena el movimiento horizontal cuando termina el empujón.
     *
     * @param jugador jugador que tenía el efecto
     */
    @Override
    public void alTerminar(Jugador jugador) {
        jugador.detenerHorizontal();
    }
}
