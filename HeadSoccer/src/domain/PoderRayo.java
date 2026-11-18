package domain;

public class PoderRayo extends PoderBase {

    /**
     * Crea el poder Rayo.
     */
    public PoderRayo() {
        super("Rayo", 3.0, 60.0);
    }

    /**
     * Crea el efecto de electrocución que aplica el poder.
     *
     * @return efecto con la duración del poder
     */
    @Override
    public Efecto crearEfecto() {
        return new EfectoElectrocutado(getDuracion());
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
