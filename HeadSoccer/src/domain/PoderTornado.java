package domain;

public class PoderTornado extends PoderBase {

    /**
     * Crea el poder Tornado.
     */
    public PoderTornado() {
        super("Tornado", 3.0, 40.0);
    }

    /**
     * Crea el efecto de inconsciencia que aplica el poder.
     *
     * @return efecto con la duración del poder
     */
    @Override
    public Efecto crearEfecto() {
        return new EfectoInconsciente(getDuracion());
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
