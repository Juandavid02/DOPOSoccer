package domain;

public class ReglaGoles implements ReglaPartido {

    public boolean terminado(Partido partido) {
        Marcador marcador = partido.getMarcador();
        return marcador.getGoles(1) >= 5 || marcador.getGoles(2) >= 5;
    }

    public MotivoFin getMotivo() {
        return MotivoFin.GOLES;
    }
    
}
