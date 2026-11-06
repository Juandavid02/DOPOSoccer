package domain;

public class Estadisticas {
    private int velocidad;
    private int salto;
    private int fuerza;

    public Estadisticas(int velocidad, int salto, int fuerza){
        if (velocidad < 1 || velocidad > 5) {
            throw new IllegalArgumentException("La velocidad debe estar entre 1 y 5");
        } else {
            this.velocidad = velocidad;
        }
        if (salto < 1 || salto > 5) {
            throw new IllegalArgumentException("El salto debe estar entre 1 y 5");
        } else {
            this.salto = salto;
        }
        if (fuerza < 1 || fuerza > 5) {
            throw new IllegalArgumentException("La fuerza debe estar entre 1 y 5");
        } else {
            this.fuerza = fuerza;
        }
    } 

    public int getVelocidad() {
        return this.velocidad;
    }

    public int getSalto() {
        return this.salto;
    }

    public int getFuerza() {
        return this.fuerza;
    }

}
