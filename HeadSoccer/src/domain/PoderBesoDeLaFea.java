package domain;

public class PoderBesoDeLaFea extends PoderBase {

    /**
     * Crea el poder Beso de la Fea.
     */
    public PoderBesoDeLaFea() {
        super("Beso de la Fea", 2.5, 30.0);
    }

    /**
     * Obtiene el tipo de efecto que aplica este poder.
     *
     * @return efecto de controles invertidos
     */
    @Override
    public TipoEfecto getTipoEfecto() {
        return TipoEfecto.CONTROLES_INVERTIDOS;
    }

    /**
     * Obtiene el impulso horizontal del poder.
     *
     * @return impulso horizontal
     */
    @Override
    public double getImpulsoX() {
        return 1250;
    }

    /**
     * Obtiene el impulso vertical del poder.
     *
     * @return impulso vertical
     */
    @Override
    public double getImpulsoY() {
        return -150;
    }
}
