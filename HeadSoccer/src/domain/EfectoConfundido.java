package domain;

/**
 * Efecto Confundido: el jugador ve balones de ilusión. No cambia nada del
 * jugador por sí mismo; el Partido revisa si lo tiene para manejar los
 * balones de ilusión.
 */
public class EfectoConfundido extends Efecto {

    /**
     * Crea el efecto Confundido.
     *
     * @param duracion duración del efecto en segundos
     */
    public EfectoConfundido(double duracion) {
        super(TipoEfecto.CONFUNDIDO, duracion);
    }
}
