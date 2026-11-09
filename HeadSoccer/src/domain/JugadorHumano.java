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

    public JugadorHumano(ConfiguracionJugador configuracion,
                         Personaje personaje,
                         BarraPoder barraPoder,
                         Lado lado,
                         double posicionX,
                         double posicionY) {
        super(configuracion, personaje, barraPoder, lado, posicionX, posicionY);
    }

    @Override
    public void actualizarControl(Partido partido, double dt) {
        // El control humano se aplica desde la vista/controlador mediante
        // Partido.mover(), Partido.saltar(), Partido.patear() y
        // Partido.activarPoder(). Aquí no hay decisiones automáticas.
    }
}
