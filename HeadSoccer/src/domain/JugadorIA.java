package domain;

/**
 * Base para los jugadores controlados por la máquina. La implementación
 * concreta vive en JugadorMaquina, que delega las decisiones en
 * ControladorIA.
 */
public abstract class JugadorIA extends Jugador {

    /**
     * Crea un jugador controlado por la máquina.
     *
     * @param configuracion configuración del jugador
     * @param personaje personaje elegido
     * @param barraPoder barra de poder del jugador
     * @param lado lado de la cancha que defiende
     * @param posicionX posición inicial en el eje X
     * @param posicionY posición inicial en el eje Y
     */
    protected JugadorIA(ConfiguracionJugador configuracion,
                        Personaje personaje,
                        BarraPoder barraPoder,
                        Lado lado,
                        double posicionX,
                        double posicionY) {
        super(configuracion, personaje, barraPoder, lado, posicionX, posicionY);
    }
}
