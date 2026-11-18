package domain;

/**
 * Jugador concreto controlado por la máquina. Delega sus decisiones en un
 * ControladorIA según el perfil elegido.
 */
public class JugadorMaquina extends Jugador {

    private final ControladorIA controlador;

    /**
     * Crea un jugador controlado por la máquina con el perfil indicado.
     *
     * @param configuracion configuración del jugador
     * @param personaje personaje elegido
     * @param barraPoder barra de poder del jugador
     * @param lado lado de la cancha que defiende
     * @param posicionX posición inicial en el eje X
     * @param posicionY posición inicial en el eje YQQ
     * @param perfil perfil de inteligencia artificial
     */
    public JugadorMaquina(ConfiguracionJugador configuracion, Personaje personaje,  BarraPoder barraPoder, Lado lado, double posicionX, double posicionY, PerfilIA perfil) {
        super(configuracion, personaje, barraPoder, lado, posicionX, posicionY);
        this.controlador = FabricaControladorIA.crear(perfil);
    }

    /**
     * Actualiza el control del jugador delegando en su controlador de IA.
     *
     * @param partido partido en curso
     * @param dt tiempo transcurrido
     */
    @Override
    public void actualizarControl(Partido partido, double dt) {
        controlador.actualizar(partido, this, dt);
    }
}
