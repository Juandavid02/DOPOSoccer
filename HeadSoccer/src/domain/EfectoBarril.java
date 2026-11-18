package domain;

/**
 * Efecto Barril: el jugador queda con las estadísticas y la barra de poder
 * en cero mientras dure; al terminar recupera sus estadísticas normales.
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
        jugador.reiniciarPorBarril();
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
