package domain;

public class PoderTornado  implements Poder {
    private String nombre;
    private double duracion;
    private double danoTiro;

    public PoderTornado() {
        nombre = "Tornado";
        duracion = 3.0;
        danoTiro = 40.0;
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
