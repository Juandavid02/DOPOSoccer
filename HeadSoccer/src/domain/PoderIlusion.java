package domain;

public class PoderIlusion implements Poder {
    private final String nombre;
    private final double duracion;
    private final double danoTiro;

    public PoderIlusion() {
        nombre = "Ilusión";
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
