package domain;

/**
 * Efecto Barril: mientras dure, el jugador queda con las estadísticas en su
 * valor inicial y la barra de poder en cero; al terminar recupera sus
 * estadísticas normales.
 */
public class EfectoBarril extends Efecto {

    /**
     * Crea el efecto Barril.
     *
     * @param duracion duración del efecto en segundos
     */
    public EfectoBarril(double duracion) {
        super(TipoEfecto.BARRIL, duracion);
    }

    /**
     * Reinicia las estadísticas y la barra de poder del jugador.
     *
     * @param jugador jugador que queda dentro del barril
     */
    @Override
    public void alAplicar(Jugador jugador) {
        jugador.restablecerEstadisticas();
        jugador.getBarraPoder().reiniciar();
    }

    /**
     * Le devuelve al jugador sus estadísticas normales.
     *
     * @param jugador jugador que tenía el efecto
     */
    @Override
    public void alTerminar(Jugador jugador) {
        jugador.restaurarEstadisticas();
    }
}
