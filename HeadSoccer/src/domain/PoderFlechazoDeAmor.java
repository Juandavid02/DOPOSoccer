package domain;

public class PoderFlechazoDeAmor extends PoderBase {

    public PoderFlechazoDeAmor() {
        super("Flechazo de Amor", 2.5, 30.0);
    }

    @Override
    public TipoEfecto getTipoEfecto() {
        return TipoEfecto.CONTROLES_INVERTIDOS;
    }

    @Override
    public double getImpulsoX() {
        return 1250;
    }

    @Override
    public double getImpulsoY() {
        return -150;
    }
}
