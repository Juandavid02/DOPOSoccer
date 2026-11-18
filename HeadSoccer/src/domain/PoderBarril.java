package domain;

public class PoderBarril extends PoderBase {

    /**
     * Crea el poder Barril.
     */
    public PoderBarril() {
        super("Barril", 5.0, 35.0);
    }

    /**
     * Crea el efecto Barril que aplica el poder al rival.
     *
     * @return efecto Barril con la duración del poder
     */
    @Override
    public Efecto crearEfecto() {
        return new EfectoBarril(getDuracion());
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
