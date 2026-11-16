package domain;

public class ReglaGoles implements ReglaPartido {
    private final int golesParaGanar;

    /**
     * Crea la regla de victoria por cantidad de goles.
     *
     * @param golesParaGanar goles necesarios para ganar
     * @throws IllegalArgumentException si el número de goles no es mayor que cero
     */
    public ReglaGoles(int golesParaGanar) {
        if (golesParaGanar <= 0) {
            throw new IllegalArgumentException("El número de goles para ganar debe ser mayor que cero");
        }
        this.golesParaGanar = golesParaGanar;
    }

    /**
     * Indica si el partido terminó según la cantidad de goles.
     *
     * @param partido partido que se está evaluando
     * @return true si algún jugador alcanzó los goles para ganar
     */
    public boolean terminado(Partido partido) {
        Marcador marcador = partido.getMarcador();
        return marcador.getGoles(1) >= golesParaGanar || marcador.getGoles(2) >= golesParaGanar;
    }

    /**
     * Devuelve el motivo por el cual terminó el partido.
     *
     * @return motivo de fin por goles
     */
    public MotivoFin getMotivo() {
        return MotivoFin.GOLES;
    }

}
