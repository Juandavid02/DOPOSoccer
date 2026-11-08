package domain;

public class PoderFlechazoDeAmor implements Poder {
    private final String nombre;
    private final double duracion;
    private final double danoTiro;

    public PoderFlechazoDeAmor() {
        nombre = "Flechazo de Amor";
        duracion = 2.5;
        danoTiro = 30.0;
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public double getDuracion() {
        return duracion;
    }

    @Override
    public double getDanoTiro(Jugador jugador) {
        return danoTiro;
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
