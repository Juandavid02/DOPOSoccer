package domain;

/**
 * IA experta: ataca el balón si está en su campo y, si no, se adelanta
 * para esperarlo.
 */
public class ControladorExperto extends ControladorEstratega {

    /**
     * Decide si perseguir el balón o esperarlo en una posición adelantada.
     *
     * @param partido partido en curso
     * @param yo jugador controlado por la IA
     * @param balon balón del partido
     * @return posición horizontal objetivo
     */
    @Override
    protected double objetivo(Partido partido, Jugador yo, Balon balon) {
        Cancha cancha = partido.getCancha();
        double ancho = cancha.getAncho();
        double medio = ancho / 2;
        boolean ladoDerecho = yo.getLado() == Lado.DERECHO;
        boolean balonEnMiCampo;
        if (ladoDerecho) {
            balonEnMiCampo = balon.getX() > medio;
        } else {
            balonEnMiCampo = balon.getX() < medio;
        }

        if (balonEnMiCampo) {
            return balon.getX();
        }
        if (ladoDerecho) {
            return ancho * 0.68;
        }
        return ancho * 0.32;
    }
}
