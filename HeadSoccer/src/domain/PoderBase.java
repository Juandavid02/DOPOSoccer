package domain;

/**
 * Base común para todos los poderes del juego.
 *
 * Concentra los datos que comparten todos los poderes (nombre, duración y
 * daño del tiro) y deja como responsabilidad de cada subclase el efecto que
 * aplica y los impulsos con los que sale el balón.
 */
public abstract class PoderBase implements Poder {

    private final String nombre;
    private final double duracion;
    private final double danoTiro;

    protected PoderBase(String nombre, double duracion, double danoTiro) {
        this.nombre = nombre;
        this.duracion = duracion;
        this.danoTiro = danoTiro;
    }

    @Override
    public final String getNombre() {
        return nombre;
    }

    @Override
    public final double getDuracion() {
        return duracion;
    }

    @Override
    public double getDanoTiro(Jugador jugador) {
        return danoTiro;
    }

    @Override
    public abstract TipoEfecto getTipoEfecto();

    @Override
    public abstract double getImpulsoX();

    @Override
    public abstract double getImpulsoY();
}
