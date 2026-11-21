package domain;

import java.util.Random;

/**
 * IA torpe: se mueve, salta y patea de forma aleatoria.
 */
public class ControladorTorpe extends ControladorIA {

    private final Random random = new Random();
    private double tiempoDecision;
    private int direccionAleatoria;
    private boolean saltarAleatorio;

    /**
     * Decide acciones aleatorias para este instante.
     *
     * @param partido partido en curso
     * @param yo jugador controlado por la IA
     * @param dt tiempo transcurrido desde la última actualización
     */
    @Override
    protected void decidir(Partido partido, Jugador yo, double dt) {
        tiempoDecision -= dt;
        if (tiempoDecision <= 0) {
            tiempoDecision = 0.5 + random.nextDouble() * 0.6;
            direccionAleatoria = random.nextInt(3) - 1;
            saltarAleatorio = random.nextDouble() < 0.25;
        }

        if (direccionAleatoria != 0) {
            mover(partido, yo, direccionAleatoria, dt);
        }

        if (saltarAleatorio && yo.isEnSuelo() && tiempoSalto <= 0) {
            partido.saltar(numero(partido, yo));
            tiempoSalto = 1.2;
        }

        Balon balon = partido.getBalon();
        if (cercaDelBalon(yo, balon) && random.nextDouble() < 0.4) {
            partido.patear(numero(partido, yo));
        }
    }
}
