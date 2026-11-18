package domain;

public class PoderAuraAzul extends PoderBase {

    /**
     * Crea el poder Aura Azul.
     */
    public PoderAuraAzul() {
        super("Aura Azul", 1.5, 40.0);
    }

    /**
     * Crea el empujón que aplica el poder al rival.
     *
     * @return efecto de empuje con la duración del poder
     */
    @Override
    public Efecto crearEfecto() {
        return new EfectoEmpuje(getDuracion());
    }

    /**
     * Obtiene el impulso horizontal del balón.
     *
     * @return impulso horizontal
     */
    @Override
    public double getImpulsoX() {
        return 1350;
    }

    /**
     * Obtiene el impulso vertical del balón.
     *
     * @return impulso vertical
     */
    @Override
    public double getImpulsoY() {
        return -100;
    }
}
