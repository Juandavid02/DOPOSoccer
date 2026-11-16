package domain;

/**
 * Comportamiento de un tiro de poder de un personaje.
 */
public interface Poder {
    /**
     * Obtiene el nombre del poder.
     *
     * @return nombre del poder
     */
    String getNombre();
    /**
     * Obtiene la duración del efecto del poder.
     *
     * @return duración en segundos
     */
    double getDuracion();
    /**
     * Calcula el daño del tiro para el jugador indicado.
     *
     * @param jugador jugador que ejecuta el tiro
     * @return daño del tiro
     */
    double getDanoTiro(Jugador jugador);

    /**
     * Efecto que se aplica al rival cuando el tiro lo golpea.
     *
     * @return tipo de efecto
     */
    TipoEfecto getTipoEfecto();

    /**
     * Impulso horizontal del balón al ejecutar el tiro.
     *
     * @return impulso horizontal
     */
    double getImpulsoX();

    /**
     * Impulso vertical del balón al ejecutar el tiro.
     *
     * @return impulso vertical
     */
    double getImpulsoY();
}
