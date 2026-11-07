package domain;

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
