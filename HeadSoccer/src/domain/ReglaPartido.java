package domain;

public interface ReglaPartido {
    boolean terminado(Partido partido);
    MotivoFin getMotivo();
}
