package domain;

/**
 * Efecto Controles invertidos: izquierda y derecha se cambian mientras dure.
 */
public class EfectoControlesInvertidos extends Efecto {

    /**
     * Crea el efecto Controles invertidos.
     *
     * @param duracion duración del efecto en segundos
     */
    public EfectoControlesInvertidos(double duracion) {
        super(TipoEfecto.CONTROLES_INVERTIDOS, duracion);
    }

    @Override
    public boolean invierteControles() {
        return true;
    }
}
