package domain;

import java.io.Serializable;

public class Personaje implements Serializable {
    private final String nombre;
    private final String pais;
    private final Poder poder;

    /**
     * Crea un personaje con nombre, país y poder.
     *
     * @param nombre nombre del personaje
     * @param pais país del personaje
     * @param poder poder del personaje
     */
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

    /**
     * Obtiene el nombre del personaje.
     *
     * @return nombre del personaje
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Obtiene el país del personaje.
     *
     * @return país del personaje
     */
    public String getPais() {
        return pais;
    }

    /**
     * Obtiene el poder del personaje.
     *
     * @return poder del personaje
     */
    public Poder getPoder() {
        return poder;
    }
}
