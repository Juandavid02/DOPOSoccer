package domain;

public class PoderRayo extends PoderBase {

    public PoderRayo() {
        super("Rayo", 3.0, 60.0);
    }

    @Override
    public TipoEfecto getTipoEfecto() {
        return TipoEfecto.ELECTROCUTADO;
    }

    @Override
    public double getImpulsoX() {
        return 1450;
    }

    @Override
    public double getImpulsoY() {
        return -120;
    }
}
