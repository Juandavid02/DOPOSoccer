package domain;

import java.io.Serializable;

public interface ReglaPartido extends Serializable {
    /**
     * Indica si el partido ya terminó.
     *
     * @param partido partido que se está evaluando
     * @return true si el partido terminó
     */
    boolean terminado(Partido partido);

    /**
     * Devuelve el motivo por el cual terminó el partido.
     *
     * @return motivo de fin del partido
     */
    MotivoFin getMotivo();
}
