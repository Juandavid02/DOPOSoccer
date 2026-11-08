package domain;

/**
 * Base para los jugadores controlados por la máquina.
 *
 * Por ahora solo queda preparada la jerarquía. Las versiones concretas de la
 * IA (torpe, ofensiva, defensiva y experta) se implementarán más adelante,
 * cuando se active el modo PvsM.
 */
public abstract class JugadorIA extends Jugador {

    protected JugadorIA(ConfiguracionJugador configuracion,
                        Personaje personaje,
                        BarraPoder barraPoder,
                        Lado lado,
                        double posicionX,
                        double posicionY) {
        super(configuracion, personaje, barraPoder, lado, posicionX, posicionY);
    }
}
