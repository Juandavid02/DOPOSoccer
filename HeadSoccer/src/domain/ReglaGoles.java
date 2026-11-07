package domain;

public class ReglaGoles implements ReglaPartido {
    private final int golesParaGanar;

    public ReglaGoles(int golesParaGanar) {
        if (golesParaGanar <= 0) {
            throw new IllegalArgumentException("El número de goles para ganar debe ser mayor que cero");
        }
        this.golesParaGanar = golesParaGanar;
    }

    public boolean terminado(Partido partido) {
        Marcador marcador = partido.getMarcador();
        return marcador.getGoles(1) >= golesParaGanar || marcador.getGoles(2) >= golesParaGanar;
    }

    public MotivoFin getMotivo() {
        return MotivoFin.GOLES;
    }
    
}
