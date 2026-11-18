package domain;

/**
 * IA defensiva: se coloca entre el balón y su propio arco.
 */
public class ControladorDefensivo extends ControladorEstratega {

    /**
     * Calcula una posición retrasada respecto al balón, sin alejarse
     * demasiado de su propio arco.
     *
     * @param partido partido en curso
     * @param yo jugador controlado por la IA
     * @param balon balón del partido
     * @return posición horizontal defensiva
     */
    @Override
    protected double objetivo(Partido partido, Jugador yo, Balon balon) {
        Cancha cancha = partido.getCancha();
        boolean ladoDerecho = yo.getLado() == Lado.DERECHO;
        double ancho = cancha.getAncho();

        double desplazamiento = -150;
        if (ladoDerecho) {
            desplazamiento = 150;
        }

        double objetivo = balon.getX() + desplazamiento;
        if (ladoDerecho) {
            return Math.max(ancho * 0.55, Math.min(ancho - 80, objetivo));
        }
        return Math.min(ancho * 0.45, Math.max(80, objetivo));
    }
}
