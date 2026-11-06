package domain;

/**
 * Regla de victoria por tiempo.
 * El partido dura 60 segundos. Si al terminar hay empate, se activa
 * el gol de oro: el primero que anote gana.
 */
public class ReglaTiempo implements ReglaPartido {
    private boolean enGolDeOro;
    private int golesJugador1AlIniciarGolDeOro;
    private int golesJugador2AlIniciarGolDeOro;
    private MotivoFin motivo;

    public ReglaTiempo() {
        this.enGolDeOro = false;
        this.golesJugador1AlIniciarGolDeOro = 0;
        this.golesJugador2AlIniciarGolDeOro = 0;
        this.motivo = null;
    }

    /**
     * Indica si el partido ya terminó según la regla de tiempo.
     *
     * @param partido partido que se está evaluando
     * @return true si el partido terminó
     */
    public boolean terminado(Partido partido) {
        if (partido.getTiempoRestante() > 0) {
            return false;
        }

        Marcador marcador = partido.getMarcador();

        if (enGolDeOro) {
            boolean huboGol = marcador.getGoles(1) != golesJugador1AlIniciarGolDeOro || marcador.getGoles(2) != golesJugador2AlIniciarGolDeOro;
            if (huboGol) {
                motivo = MotivoFin.GOL_DE_ORO;
                return true;
            }
            return false;
        }

        if (!marcador.hayEmpate()) {
            motivo = MotivoFin.TIEMPO;
            return true;
        }

        enGolDeOro = true;
        golesJugador1AlIniciarGolDeOro = marcador.getGoles(1);
        golesJugador2AlIniciarGolDeOro = marcador.getGoles(2);
        return false;
    }

    /**
     * Devuelve el motivo por el cual terminó el partido.
     *
     * @return TIEMPO o GOL_DE_ORO, o null si todavía no terminó
     */
    public MotivoFin getMotivo() {
        return motivo;
    }

    /**
     * Indica si el partido está en gol de oro.
     *
     * @return true si ya se activó el gol de oro
     */
    public boolean estaEnGolDeOro() {
        return enGolDeOro;
    }
}
