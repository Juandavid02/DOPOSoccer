package domain;

public class PoderRayo extends PoderBase {

    /**
     * Crea el poder Rayo.
     */
    public PoderRayo() {
        super("Rayo", 3.0, 60.0);
    }

    /**
     * Obtiene el tipo de efecto que aplica este poder.
     *
     * @return efecto de electrocución
     */
    @Override
    public TipoEfecto getTipoEfecto() {
        return TipoEfecto.ELECTROCUTADO;
    }

    /**
     * Obtiene el impulso horizontal del poder.
     *
     * @return impulso horizontal
     */
    @Override
    public double getImpulsoX() {
        return 1450;
    }

    /**
     * Obtiene el impulso vertical del poder.
     *
     * @return impulso vertical
     */
    @Override
    public double getImpulsoY() {
        return -120;
    }
}
