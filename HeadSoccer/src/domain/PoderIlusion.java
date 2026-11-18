package domain;

public class PoderIlusion extends PoderBase {

    /**
     * Crea el poder Ilusión.
     */
    public PoderIlusion() {
        super("Ilusión", 2.5, 30.0);
    }

    /**
     * Crea el efecto de confusión que aplica este poder.
     *
     * @return efecto con la duración del poder
     */
    @Override
    public Efecto crearEfecto() {
        return new EfectoConfundido(getDuracion());
    }

    /**
     * Obtiene el impulso horizontal del poder.
     *
     * @return impulso horizontal
     */
    @Override
    public double getImpulsoX() {
        return 1200;
    }

    /**
     * Obtiene el impulso vertical del poder.
     *
     * @return impulso vertical
     */
    @Override
    public double getImpulsoY() {
        return -180;
    }
}
