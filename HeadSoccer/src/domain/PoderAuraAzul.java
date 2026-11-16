package domain;

public class PoderAuraAzul extends PoderBase {

    /**
     * Crea el poder Aura Azul.
     */
    public PoderAuraAzul() {
        super("Aura Azul", 1.5, 40.0);
    }

    /**
     * Obtiene el tipo de efecto que aplica el poder.
     *
     * @return tipo de efecto
     */
    @Override
    public TipoEfecto getTipoEfecto() {
        return TipoEfecto.EMPUJE;
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
