package domain;

public class PoderAuraAzul implements Poder {
    private String nombre;
    private double duracion;
    private double danoTiro;


    public PoderAuraAzul() {
        nombre = "Aura Azul";
        duracion = 1.5;
        danoTiro = 40;
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
