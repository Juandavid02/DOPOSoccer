package domain;

import java.util.Random;

/**
 * Decide las acciones de un jugador controlado por la máquina.
 *
 * El controlador no mueve al jugador directamente: usa los mismos métodos
 * que un jugador humano (mover, saltar, patear y activarPoder), de modo que
 * las estadísticas y las reglas del partido se aplican igual para ambos.
 */
public class ControladorIA {

    private final PerfilIA perfil;
    private final Random random = new Random();

    private double tiempoDecision;
    private double tiempoSalto;
    private int direccionAleatoria;
    private boolean saltarAleatorio;

    /**
     * Crea el controlador de una máquina con el perfil indicado.
     *
     * @param perfil perfil de comportamiento de la IA
     */
    public ControladorIA(PerfilIA perfil) {
        if (perfil == null) {
            throw new IllegalArgumentException("El perfil de la IA no puede ser nulo");
        }
        this.perfil = perfil;
    }

    /**
     * Actualiza las decisiones de la máquina para este instante.
     *
     * @param partido partido en curso
     * @param yo jugador controlado por la IA
     * @param dt tiempo transcurrido desde la última actualización
     */
    public void actualizar(Partido partido, Jugador yo, double dt) {
        if (dt <= 0 || yo.isAturdido()) {
            return;
        }

        tiempoDecision -= dt;
        tiempoSalto -= dt;

        if (perfil == PerfilIA.TORPE) {
            decidirTorpe(partido, yo, dt);
        } else {
            decidirEstrategia(partido, yo, dt);
        }
    }

    /**
     * Decide acciones aleatorias para el perfil Torpe.
     *
     * @param partido partido en curso
     * @param yo jugador controlado por la IA
     * @param dt tiempo transcurrido desde la última actualización
     */
    private void decidirTorpe(Partido partido, Jugador yo, double dt) {
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

    /**
     * Aplica la estrategia del perfil para mover y patear.
     *
     * @param partido partido en curso
     * @param yo jugador controlado por la IA
     * @param dt tiempo transcurrido desde la última actualización
     */
    private void decidirEstrategia(Partido partido, Jugador yo, double dt) {
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
    private double objetivo(Partido partido, Jugador yo, Balon balon) {
        Cancha cancha = partido.getCancha();
        boolean ladoDerecho = yo.getLado() == Lado.DERECHO;
        double ancho = cancha.getAncho();

        switch (perfil) {
            case OFENSIVA:
                return balon.getX();
            case DEFENSIVA: {
                // Se queda entre el balón y su propio arco.
                double desplazamiento = -150;
                if (ladoDerecho) {
                    desplazamiento = 150;
                }
                double objetivo = balon.getX() + desplazamiento;
                if (ladoDerecho) {
                    return Math.max(ancho * 0.55,
                            Math.min(ancho - 80, objetivo));
                }
                return Math.min(ancho * 0.45,
                        Math.max(80, objetivo));
            }
            case EXPERTA: {
                double medio = ancho / 2;
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
            default:
                return balon.getX();
        }
    }

    /**
     * Mueve al jugador en la dirección indicada a su velocidad actual.
     *
     * @param partido partido en curso
     * @param yo jugador controlado por la IA
     * @param direccion dirección del movimiento
     * @param dt tiempo transcurrido desde la última actualización
     */
    private void mover(Partido partido, Jugador yo, double direccion, double dt) {
        double velocidad = ParametrosJuego.VELOCIDAD_BASE_JUGADOR
                * yo.getEstadisticas().getFactorVelocidad()
                * yo.getMultiplicadorVelocidad();
        partido.mover(numero(partido, yo), direccion * velocidad * dt);
    }

    /**
     * Indica si el jugador está cerca del balón.
     *
     * @param yo jugador controlado por la IA
     * @param balon balón del partido
     * @return true si el balón está dentro del alcance
     */
    private static boolean cercaDelBalon(Jugador yo, Balon balon) {
        double dx = Math.abs(balon.getX() - yo.getPosicionX());
        double dy = Math.abs(balon.getY() - yo.getPosicionY());
        return dx <= ParametrosJuego.ALCANCE_PATADA + 15 && dy <= 95;
    }

    /**
     * Devuelve el número del jugador dentro del partido.
     *
     * @param partido partido en curso
     * @param jugador jugador consultado
     * @return número del jugador: 1 o 2
     */
    private static int numero(Partido partido, Jugador jugador) {
        return partido.getJugadores().indexOf(jugador) + 1;
    }
}
