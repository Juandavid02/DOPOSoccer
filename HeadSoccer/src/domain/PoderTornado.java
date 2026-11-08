package domain;

public class PoderTornado extends PoderBase {

    public PoderTornado() {
        super("Tornado", 3.0, 40.0);
    }

    @Override
    public TipoEfecto getTipoEfecto() {
        return TipoEfecto.INCONSCIENTE;
    }

    @Override
    public double getImpulsoX() {
        return 1000;
    }

    @Override
    public double getImpulsoY() {
        return -350;
    }
}
