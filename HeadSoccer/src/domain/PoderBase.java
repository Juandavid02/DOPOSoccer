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

    /**
     * Crea un poder con nombre, duración y daño del tiro.
     *
     * @param nombre nombre del poder
     * @param duracion duración del poder en segundos
     * @param danoTiro daño que produce el tiro
     */
    protected PoderBase(String nombre, double duracion, double danoTiro) {
        this.nombre = nombre;
        this.duracion = duracion;
        this.danoTiro = danoTiro;
    }

    /**
     * Obtiene el nombre del poder.
     *
     * @return nombre del poder
     */
    @Override
    public final String getNombre() {
        return nombre;
    }

    /**
     * Obtiene la duración del poder.
     *
     * @return duración en segundos
     */
    @Override
    public final double getDuracion() {
        return duracion;
    }

    /**
     * Obtiene el daño del tiro del poder.
     *
     * @param jugador jugador que ejecuta el tiro
     * @return daño del tiro
     */
    @Override
    public double getDanoTiro(Jugador jugador) {
        return danoTiro;
    }

    /**
     * Crea el efecto que aplica el poder al rival.
     *
     * @return efecto nuevo con la duración del poder
     */
    @Override
    public abstract Efecto crearEfecto();

    /**
     * Obtiene el impulso horizontal del balón.
     *
     * @return impulso horizontal
     */
    @Override
    public abstract double getImpulsoX();

    /**
     * Obtiene el impulso vertical del balón.
     *
     * @return impulso vertical
     */
    @Override
    public abstract double getImpulsoY();
}
