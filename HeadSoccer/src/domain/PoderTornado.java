package domain;

public class PoderTornado extends PoderBase {

    /**
     * Crea el poder Tornado.
     */
    public PoderTornado() {
        super("Tornado", 3.0, 40.0);
    }

    /**
     * Obtiene el tipo de efecto que aplica este poder.
     *
     * @return efecto de inconsciencia
     */
    @Override
    public TipoEfecto getTipoEfecto() {
        return TipoEfecto.INCONSCIENTE;
    }

    /**
     * Obtiene el impulso horizontal del poder.
     *
     * @return impulso horizontal
     */
    @Override
    public double getImpulsoX() {
        return 1000;
    }

    /**
     * Obtiene el impulso vertical del poder.
     *
     * @return impulso vertical
     */
    @Override
    public double getImpulsoY() {
        return -350;
    }
}
