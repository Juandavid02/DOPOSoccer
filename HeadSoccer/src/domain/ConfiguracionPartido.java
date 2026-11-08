package domain;

public class ConfiguracionPartido {
    private final Modalidad modalidad;
    private final TipoPartido tipo;
    private final Resolucion resolucion;
    private final int tiempo;
    private final int goles;

    public ConfiguracionPartido(Modalidad modalidad, TipoPartido tipo, Resolucion resolucion, int tiempo, int goles)
            throws ExcepcionDOPOSoccer {
        if (modalidad == null) {
            throw new ExcepcionDOPOSoccer(ExcepcionDOPOSoccer.MODALIDAD_NULA);
        }
        if (tipo == null) {
            throw new ExcepcionDOPOSoccer(ExcepcionDOPOSoccer.TIPO_PARTIDO_NULO);
        }
        if (resolucion == null) {
            throw new ExcepcionDOPOSoccer(ExcepcionDOPOSoccer.RESOLUCION_NULA);
        }
        if (tiempo <= 0) {
            throw new ExcepcionDOPOSoccer(ExcepcionDOPOSoccer.TIEMPO_INVALIDO);
        }
        if (goles <= 0) {
            throw new ExcepcionDOPOSoccer(ExcepcionDOPOSoccer.GOLES_INVALIDOS);
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
