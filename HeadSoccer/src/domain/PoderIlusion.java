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
    
}
