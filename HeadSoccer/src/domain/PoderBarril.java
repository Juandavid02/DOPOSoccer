package domain;

public class PoderBarril extends PoderBase {

    public PoderBarril() {
        super("Barril", 5.0, 35.0);
    }

    @Override
    public TipoEfecto getTipoEfecto() {
        return TipoEfecto.BARRIL;
    }

    @Override
    public double getImpulsoX() {
        return 1050;
    }

    @Override
    public double getImpulsoY() {
        return -220;
    }
}
