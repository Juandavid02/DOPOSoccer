package domain;

import java.util.Random;

/**
 * Torneo de eliminación directa con cuatro participantes: el humano y tres
 * máquinas.
 *
 * La inscripción se cobra al crear el torneo y los premios se entregan
 * una sola vez al finalizar. Los partidos del torneo no reparten puntos por
 * sí mismos.
 */
public class Torneo {

    private final PerfilJugador perfilHumano;
    private final ParticipanteTorneo humano;
    private final PartidoTorneo semifinal1;
    private final PartidoTorneo semifinal2;

    private PartidoTorneo finalTorneo;
    private ParticipanteTorneo campeon;
    private ParticipanteTorneo subcampeon;
    private boolean premiosEntregados;

    /**
     * Crea el torneo y descuenta la inscripción del perfil humano.
     *
     * @param perfil perfil que paga la inscripción
     * @param humano participante humano
     * @param ia1 primer rival de la semifinal 1
     * @param ia2 primer rival de la semifinal 2
     * @param ia3 segundo rival de la semifinal 2
     * @throws ExcepcionDOPOSoccer si no hay puntos suficientes
     */
    public Torneo(PerfilJugador perfil, ParticipanteTorneo humano,
                  ParticipanteTorneo ia1, ParticipanteTorneo ia2,
                  ParticipanteTorneo ia3) throws ExcepcionDOPOSoccer {
        this(perfil, humano, ia1, ia2, ia3, true);
    }

    /**
     * Crea el torneo con todos sus participantes.
     *
     * @param perfil perfil que paga la inscripción
     * @param humano participante humano
     * @param ia1 primer rival de la semifinal 1
     * @param ia2 primer rival de la semifinal 2
     * @param ia3 segundo rival de la semifinal 2
     * @param cobrarInscripcion indica si se descuenta la inscripción
     * @throws ExcepcionDOPOSoccer si no hay puntos suficientes
     */
    public Torneo(PerfilJugador perfil, ParticipanteTorneo humano,
                  ParticipanteTorneo ia1, ParticipanteTorneo ia2,
                  ParticipanteTorneo ia3, boolean cobrarInscripcion)
            throws ExcepcionDOPOSoccer {
        if (perfil == null || humano == null || ia1 == null
                || ia2 == null || ia3 == null) {
            throw new IllegalArgumentException(
                    "Los participantes del torneo no pueden ser nulos");
        }
        if (cobrarInscripcion
                && !perfil.getPuntos().gastar(ParametrosJuego.COSTO_TORNEO)) {
            throw new ExcepcionDOPOSoccer(
                    ExcepcionDOPOSoccer.PUNTOS_INSUFICIENTES);
        }

        this.perfilHumano = perfil;
        this.humano = humano;
        this.semifinal1 = new PartidoTorneo(humano, ia1);
        this.semifinal2 = new PartidoTorneo(ia2, ia3);
    }

    /**
     * Devuelve el perfil del jugador humano.
     *
     * @return perfil humano
     */
    public PerfilJugador getPerfilHumano() {
        return perfilHumano;
    }

    /**
     * Devuelve el participante humano.
     *
     * @return participante humano
     */
    public ParticipanteTorneo getHumano() {
        return humano;
    }

    /**
     * Devuelve la primera semifinal del torneo.
     *
     * @return primera semifinal
     */
    public PartidoTorneo getSemifinal1() {
        return semifinal1;
    }

    /**
     * Devuelve la segunda semifinal del torneo.
     *
     * @return segunda semifinal
     */
    public PartidoTorneo getSemifinal2() {
        return semifinal2;
    }

    /**
     * Devuelve la final del torneo.
     *
     * @return final o null si aún no se preparó
     */
    public PartidoTorneo getFinal() {
        return finalTorneo;
    }

    /**
     * Devuelve el campeón del torneo.
     *
     * @return campeón o null si el torneo no terminó
     */
    public ParticipanteTorneo getCampeon() {
        return campeon;
    }

    /**
     * Indica si el torneo ya terminó.
     *
     * @return true si la final ya fue jugada
     */
    public boolean isTerminado() {
        return finalTorneo != null && finalTorneo.isJugado();
    }

    /**
     * Indica si el humano es el campeón.
     *
     * @return true si el humano ganó el torneo
     */
    public boolean isHumanoCampeon() {
        return campeon == humano;
    }

    /**
     * Indica si el humano es el subcampeón.
     *
     * @return true si el humano perdió la final
     */
    public boolean isHumanoSubcampeon() {
        return subcampeon == humano;
    }

    /**
     * Indica si el humano fue eliminado del torneo.
     *
     * @return true si el humano ya no puede seguir jugando
     */
    public boolean isHumanoEliminado() {
        return humano.isEliminado();
    }

    /**
     * Premio en puntos que le corresponde al humano según el estado actual.
     *
     * @return 500 si es campeón, 150 si es subcampeón, 0 en otro caso
     */
    public int getPremioHumano() {
        if (isHumanoCampeon()) {
            return ParametrosJuego.PREMIO_CAMPEON_TORNEO;
        }
        if (isHumanoSubcampeon()) {
            return ParametrosJuego.PREMIO_SUBCAMPEON_TORNEO;
        }
        return 0;
    }

    /**
     * Devuelve el partido que le toca jugar al humano, o null si ya no tiene
     * partidos pendientes.
     *
     * @return semifinal 1, final o null
     */
    public PartidoTorneo getSiguientePartidoHumano() {
        if (!semifinal1.isJugado()) {
            return semifinal1;
        }
        if (!humano.isEliminado() && semifinal2.isJugado()
                && finalTorneo != null && !finalTorneo.isJugado()) {
            return finalTorneo;
        }
        return null;
    }

    /**
     * Registra el resultado del partido actual del humano y avanza la llave.
     *
     * @param golesHumano goles del humano
     * @param golesRival goles de la máquina
     * @param random generador para simular los partidos de IA
     * @return true si había un partido pendiente y se registró
     */
    public boolean registrarResultadoHumano(int golesHumano, int golesRival,
                                            Random random) {
        PartidoTorneo partido = getSiguientePartidoHumano();
        if (partido == null) {
            return false;
        }
        int golesPrimero = partido.getPrimero().isHumano()
                ? golesHumano : golesRival;
        int golesSegundo = partido.getPrimero().isHumano()
                ? golesRival : golesHumano;
        partido.registrar(golesPrimero, golesSegundo);
        simularPendientes(random);
        return true;
    }

    /**
     * Simula la otra semifinal si hace falta y, cuando el humano ya está
     * eliminado, también simula la final para completar la llave.
     *
     * @param random generador de resultados
     */
    public void simularPendientes(Random random) {
        if (random == null) {
            random = new Random();
        }
        if (!semifinal2.isJugado()) {
            simular(semifinal2, random);
        }
        prepararFinal();

        if (finalTorneo != null && !finalTorneo.isJugado()
                && humano.isEliminado()) {
            simular(finalTorneo, random);
        }
        finalizar();
    }

    /**
     * Prepara la final cuando ambas semifinales ya se jugaron.
     */
    private void prepararFinal() {
        if (finalTorneo != null || !semifinal1.isJugado()
                || !semifinal2.isJugado()) {
            return;
        }

        ParticipanteTorneo primero = semifinal1.getGanador();
        ParticipanteTorneo segundo = semifinal2.getGanador();

        // Las IAs mejoran al avanzar de ronda; el humano no.
        primero.mejorar();
        segundo.mejorar();

        finalTorneo = new PartidoTorneo(primero, segundo);
    }

    /**
     * Simula un partido del torneo y registra su resultado.
     *
     * @param partido partido a simular
     * @param random generador de resultados
     */
    private void simular(PartidoTorneo partido, Random random) {
        int[] goles = simularGoles(partido.getPrimero(),
                partido.getSegundo(), random);
        partido.registrar(goles[0], goles[1]);
    }

    /**
     * Genera un marcador aleatorio entre dos participantes.
     *
     * @param primero primer participante
     * @param segundo segundo participante
     * @param random generador de resultados
     * @return goles del primero y del segundo
     */
    private int[] simularGoles(ParticipanteTorneo primero,
                               ParticipanteTorneo segundo,
                               Random random) {
        int fuerzaPrimero = fuerza(primero);
        int fuerzaSegundo = fuerza(segundo);
        double probabilidadPrimero = (double) fuerzaPrimero
                / (fuerzaPrimero + fuerzaSegundo);

        int golesPrimero = random.nextInt(4)
                + (random.nextDouble() < probabilidadPrimero ? 1 : 0);
        int golesSegundo = random.nextInt(4)
                + (random.nextDouble() < 1 - probabilidadPrimero ? 1 : 0);

        while (golesPrimero == golesSegundo) {
            if (random.nextDouble() < probabilidadPrimero) {
                golesPrimero++;
            } else {
                golesSegundo++;
            }
        }
        return new int[]{golesPrimero, golesSegundo};
    }

    /**
     * Calcula la fuerza total de un participante.
     *
     * @param participante participante consultado
     * @return suma de sus estadísticas
     */
    private static int fuerza(ParticipanteTorneo participante) {
        Estadisticas estadisticas = participante.getEstadisticas();
        return estadisticas.getSpeed() + estadisticas.getJump()
                + estadisticas.getKick() + estadisticas.getSurvival();
    }

    /**
     * Entrega los premios al finalizar el torneo.
     */
    private void finalizar() {
        if (premiosEntregados || finalTorneo == null
                || !finalTorneo.isJugado()) {
            return;
        }
        premiosEntregados = true;
        campeon = finalTorneo.getGanador();
        subcampeon = finalTorneo.getPerdedor();

        if (isHumanoCampeon()) {
            perfilHumano.getPuntos().sumar(
                    ParametrosJuego.PREMIO_CAMPEON_TORNEO);
        } else if (isHumanoSubcampeon()) {
            perfilHumano.getPuntos().sumar(
                    ParametrosJuego.PREMIO_SUBCAMPEON_TORNEO);
        }
    }
}
