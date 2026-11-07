package domain;

public class PoderBarril implements Poder {
    private final String nombre;
    private final double duracion;
    private final double danoTiro;

    public PoderBarril() {
        nombre = "Barril";
        duracion = 5.0;
        danoTiro = 35.0;
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
}
