package domain;

public class PoderRayo implements Poder {
    private String nombre;
    private double duracion;
    private double danoTiro;

    public PoderRayo() {
        nombre = "Rayo";
        duracion = 3.0;
        danoTiro = 60.0;
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
