package domain;

import java.io.Serializable;

/**
 * Comportamiento de un tiro de poder de un personaje.
 */
public interface Poder extends Serializable {
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
     * Crea el efecto que se aplica al rival cuando el tiro lo golpea.
     *
     * @return efecto nuevo con la duración de este poder
     */
    Efecto crearEfecto();

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
