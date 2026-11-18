package domain;

/**
 * Jugador controlado por una persona.
 *
 * Las acciones del jugador humano llegan desde la capa de presentación a
 * través de los métodos de Partido (mover, saltar,
 * patear y activarPoder), por lo que este tipo de jugador no
 * necesita tomar decisiones automáticas.
 */
public class JugadorHumano extends Jugador {

    /**
     * Crea un jugador controlado por una persona.
     *
     * @param configuracion configuración del jugador
     * @param personaje personaje elegido
     * @param barraPoder barra de poder del jugador
     * @param lado lado de la cancha que defiende
     * @param posicionX posición inicial en el eje X
     * @param posicionY posición inicial en el eje Y
     */
    public JugadorHumano(ConfiguracionJugador configuracion, Personaje personaje, BarraPoder barraPoder, Lado lado, double posicionX, double posicionY) {
        super(configuracion, personaje, barraPoder, lado, posicionX, posicionY);
    }

    /**
     * Actualiza el control del jugador humano.
     *
     * @param partido partido en curso
     * @param dt tiempo transcurrido
     */
    @Override
    public void actualizarControl(Partido partido, double dt) {
        // El control humano se aplica desde la vista/controlador mediante
        // Partido.mover(), Partido.saltar(), Partido.patear() y
        // Partido.activarPoder(). Aquí no hay decisiones automáticas.
    }
}
