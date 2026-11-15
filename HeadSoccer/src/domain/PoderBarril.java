package domain;

public class PoderBarril extends PoderBase {

    /**
     * Crea el poder Barril.
     */
    public PoderBarril() {
        super("Barril", 5.0, 35.0);
    }

    /**
     * Obtiene el tipo de efecto que aplica el poder.
     *
     * @return tipo de efecto
     */
    @Override
    public TipoEfecto getTipoEfecto() {
        return TipoEfecto.BARRIL;
    }

    /**
     * Obtiene el impulso horizontal del balón.
     *
     * @return impulso horizontal
     */
    @Override
    public double getImpulsoX() {
        return 1050;
    }

    /**
     * Obtiene el impulso vertical del balón.
     *
     * @return impulso vertical
     */
    @Override
    public double getImpulsoY() {
        return -220;
    }
}
