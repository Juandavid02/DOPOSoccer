package domain;

/**
 * Personaje (país) que elige un jugador.
 *
 * <p>Cada personaje representa un país y tiene un tiro de poder único
 * (Anexo 1 del PDF). Las estadísticas NO van aquí: son del jugador y viven
 * en {@link ConfiguracionJugador} / {@link Estadisticas}.</p>
 */
public class Personaje {
    private final String nombre;
    private final String pais;
    private final Poder poder;

    public Personaje(String nombre, String pais, Poder poder) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede ser nulo o vacío");
        }
        if (pais == null || pais.trim().isEmpty()) {
            throw new IllegalArgumentException("El país no puede ser nulo o vacío");
        }
        if (poder == null) {
            throw new IllegalArgumentException("El poder no puede ser nulo");
        }
        this.nombre = nombre;
        this.pais = pais;
        this.poder = poder;
    }

    public String getNombre() {
        return nombre;
    }

    public String getPais() {
        return pais;
    }

    public Poder getPoder() {
        return poder;
    }
}
