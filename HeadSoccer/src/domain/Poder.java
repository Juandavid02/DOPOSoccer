package domain;
import domain.Jugador;

public interface Poder {
    String getNombre();
    double getDuracion();
    void aplicarPoder(Jugador jugador);
}
