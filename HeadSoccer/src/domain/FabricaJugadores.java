package domain;

import java.util.List;

/**
 * Crea los dos jugadores de un partido y sus barras de poder.
 *
 * Concentra la decisión entre jugador humano y jugador máquina para que
 * Partido no tenga que conocer esos detalles de construcción.
 */
public final class FabricaJugadores {

    private FabricaJugadores() {
    }

    /**
     * Crea el jugador de la izquierda y el de la derecha.
     *
     * @param personaje1 personaje del jugador 1
     * @param personaje2 personaje del jugador 2
     * @param configuracion1 configuración del jugador 1
     * @param configuracion2 configuración del jugador 2
     * @param cancha cancha donde jugarán
     * @param modalidad modalidad del partido
     * @param perfilIA perfil de la máquina, si aplica
     * @return lista con los dos jugadores
     */
    public static List<Jugador> crear(Personaje personaje1, Personaje personaje2,
                                      ConfiguracionJugador configuracion1,
                                      ConfiguracionJugador configuracion2,
                                      Cancha cancha, Modalidad modalidad,
                                      PerfilIA perfilIA) {
        Jugador izquierdo = new JugadorHumano(configuracion1, personaje1,
                crearBarra(), Lado.IZQUIERDO,
                cancha.getAncho() / 4.0,
                cancha.getSuelo() - ParametrosJuego.RADIO_JUGADOR);

        Jugador derecho;
        if (modalidad == Modalidad.PvsM && perfilIA != null) {
            derecho = new JugadorMaquina(configuracion2, personaje2,
                    crearBarra(), Lado.DERECHO,
                    cancha.getAncho() * 3.0 / 4.0,
                    cancha.getSuelo() - ParametrosJuego.RADIO_JUGADOR,
                    perfilIA);
        } else {
            derecho = new JugadorHumano(configuracion2, personaje2,
                    crearBarra(), Lado.DERECHO,
                    cancha.getAncho() * 3.0 / 4.0,
                    cancha.getSuelo() - ParametrosJuego.RADIO_JUGADOR);
        }

        return List.of(izquierdo, derecho);
    }

    private static BarraPoder crearBarra() {
        return new BarraPoder(ParametrosJuego.CAPACIDAD_BARRA_PODER,
                ParametrosJuego.RECARGA_BARRA_PODER);
    }
}
