package domain;

/**
 * IA ofensiva: persigue el balón para atacar.
 */
public class ControladorOfensivo extends ControladorEstratega {

    /**
     * Fija como objetivo la posición del balón.
     *
     * @param partido partido en curso
     * @param yo jugador controlado por la IA
     * @param balon balón del partido
     * @return posición horizontal del balón
     */
    @Override
    protected double objetivo(Partido partido, Jugador yo, Balon balon) {
        return balon.getX();
    }
}
