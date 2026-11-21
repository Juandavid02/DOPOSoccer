package domain;

/**
 * Efecto Confundido: el jugador ve balones de ilusión. No cambia nada del
 * jugador por sí mismo; la vista dibuja los balones falsos mientras el
 * efecto esté activo.
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

    /**
     * Indica que este efecto confunde al jugador y le hace ver balones falsos.
     *
     * @return siempre true
     */
    @Override
    public boolean confunde() {
        return true;
    }
}
