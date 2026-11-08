package domain;
import domain.Jugador;

public interface Poder {
    String getNombre();
    double getDuracion();
    double getDanoTiro(Jugador jugador);

    /** Efecto que se aplica al rival cuando el tiro lo golpea. */
    TipoEfecto getTipoEfecto();

    /** Impulso horizontal del balón al ejecutar el tiro. */
    double getImpulsoX();

    /** Impulso vertical del balón al ejecutar el tiro. */
    double getImpulsoY();
}
