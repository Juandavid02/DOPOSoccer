package domain;

public class PoderAuraAzul extends PoderBase {

    public PoderAuraAzul() {
        super("Aura Azul", 1.5, 40.0);
    }

    @Override
    public TipoEfecto getTipoEfecto() {
        return TipoEfecto.EMPUJE;
    }

    @Override
    public double getImpulsoX() {
        return 1350;
    }

    @Override
    public double getImpulsoY() {
        return -100;
    }
}
