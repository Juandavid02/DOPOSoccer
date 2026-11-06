package domain;

public class Personaje {
    private final String nombre;
    private final String pais;
    private final Estadisticas estadisticas;
    private final Poder poder;

    public Personaje(String nombre, String pais, Estadisticas estadisticas, Poder poder){
        if (nombre == null || nombre.trim().isEmpty() ) {
            throw new IllegalArgumentException("El nombre no puede ser nulo o vacío");
        } else {
            this.nombre = nombre;
        }
        if (pais == null || pais.trim().isEmpty() ) {
            throw new IllegalArgumentException("El país no puede ser nulo o vacío");
        } else {
            this.pais = pais;
        }
        if (estadisticas == null) {
            throw new IllegalArgumentException("Las estadísticas no pueden ser nulas");
        } else {
            this.estadisticas = estadisticas;
        }
        if (poder == null) {
            throw new IllegalArgumentException("El poder no puede ser nulo");
        } else {
            this.poder = poder;
        }
    } 

    public String getNombre() {
        return this.nombre;
    }
    public String getPais() {
        return this.pais;
    }
    public Estadisticas getEstadisticas() {
        return this.estadisticas;
    }
    public Poder getPoder() {
        return this.poder;
    }
}
