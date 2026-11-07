package domain;
import domain.Jugador;

public interface Poder {
    String getNombre();
    double getDuracion();
    double getDanoTiro(Jugador jugador);
}
