package domain;

public class PoderIlusion extends PoderBase {

    public PoderIlusion() {
        super("Ilusión", 2.5, 30.0);
    }

    @Override
    public TipoEfecto getTipoEfecto() {
        return TipoEfecto.CONFUNDIDO;
    }

    @Override
    public double getImpulsoX() {
        return 1200;
    }

    @Override
    public double getImpulsoY() {
        return -180;
    }
}
