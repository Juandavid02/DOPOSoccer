package domain;

/**
 * Base de los controladores que persiguen una posición objetivo.
 *
 * Comparte el movimiento, el salto, la patada y el uso del poder. Cada
 * perfil concreto solo decide cuál debe ser su posición horizontal.
 */
public abstract class ControladorEstratega extends ControladorIA {

    /**
     * Decide la acción del instante persiguiendo la posición objetivo del
     * perfil concreto.
     *
     * @param partido partido en curso
     * @param yo jugador controlado por la IA
     * @param dt tiempo transcurrido desde la última actualización
     */
    @Override
    protected void decidir(Partido partido, Jugador yo, double dt) {
        Balon balon = partido.getBalon();
        double objetivo = objetivo(partido, yo, balon);
        double diferencia = objetivo - yo.getPosicionX();

        if (Math.abs(diferencia) > 12) {
            mover(partido, yo, Math.signum(diferencia), dt);
        }

        // Salta cuando el balón está por encima de la cabeza y cerca.
        boolean balonArriba = balon.getY() < yo.getPosicionY() - 60;
        boolean cercaHorizontal = Math.abs(balon.getX() - yo.getPosicionX()) < 110;
        if (balonArriba && cercaHorizontal && yo.isEnSuelo() && tiempoSalto <= 0) {
            partido.saltar(numero(partido, yo));
            tiempoSalto = 0.9;
        }

        if (cercaDelBalon(yo, balon)) {
            partido.patear(numero(partido, yo));
        }

        if (yo.getBarraPoder().isFull() && !yo.isAuraActiva()) {
            partido.activarPoder(numero(partido, yo));
        }
    }

    /**
     * Calcula la posición horizontal que debe buscar la IA.
     *
     * @param partido partido en curso
     * @param yo jugador controlado por la IA
     * @param balon balón del partido
     * @return posición horizontal objetivo
     */
    protected abstract double objetivo(Partido partido, Jugador yo, Balon balon);
}
