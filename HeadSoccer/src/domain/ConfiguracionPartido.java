package domain;

public class ConfiguracionPartido {
    private final Modalidad modalidad;
    private final TipoPartido tipo;
    private final Resolucion resolucion;
    private final int tiempo;
    private final int goles;

    public ConfiguracionPartido(Modalidad modalidad, TipoPartido tipo, Resolucion resolucion, int tiempo, int goles) {
        if (modalidad == null) {
            throw new IllegalArgumentException("La modalidad no puede ser nula");
        }
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de partido no puede ser nulo");
        }
        if (resolucion == null) {
            throw new IllegalArgumentException("La resolución no puede ser nula");
        }
        if (tiempo <= 0) {
            throw new IllegalArgumentException("El tiempo debe ser mayor que cero");
        }
        if (goles <= 0) {
            throw new IllegalArgumentException("Los goles deben ser mayores que cero");
        }

        this.modalidad = modalidad;
        this.tipo = tipo;
        this.resolucion = resolucion;
        this.tiempo = tiempo;
        this.goles = goles;
    }
    
    public Modalidad getModalidad() {
        return modalidad;
    }

    public TipoPartido getTipo() {
        return tipo;
    }

    public Resolucion getResolucion() {
        return resolucion;
    }

    public int getTiempo() {
        return tiempo;
    }

    public int getGoles() {
        return goles;
    }
}
