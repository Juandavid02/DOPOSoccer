package domain;

public class ConfiguracionPartido {
    private final Modalidad modalidad;
    private final TipoPartido tipo;
    private final Resolucion resolucion;
    private final int tiempo;
    private final int goles;
    private final PerfilIA perfilIA;

    /**
     * Crea la configuración de un partido sin perfil de IA.
     *
     * @param modalidad modalidad del partido
     * @param tipo tipo de partido
     * @param resolucion resolución de la cancha
     * @param tiempo tiempo del partido
     * @param goles goles necesarios para ganar
     * @throws ExcepcionDOPOSoccer si algún dato no es válido
     */
    public ConfiguracionPartido(Modalidad modalidad, TipoPartido tipo, Resolucion resolucion, int tiempo, int goles)
            throws ExcepcionDOPOSoccer {
        this(modalidad, tipo, resolucion, tiempo, goles, null);
    }

    /**
     * Crea la configuración de un partido con el perfil de IA indicado.
     *
     * @param modalidad modalidad del partido
     * @param tipo tipo de partido
     * @param resolucion resolución de la cancha
     * @param tiempo tiempo del partido
     * @param goles goles necesarios para ganar
     * @param perfilIA perfil de comportamiento de la IA
     * @throws ExcepcionDOPOSoccer si algún dato no es válido
     */
    public ConfiguracionPartido(Modalidad modalidad, TipoPartido tipo,
                                Resolucion resolucion, int tiempo, int goles,
                                PerfilIA perfilIA)
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
        this.perfilIA = perfilIA;
    }

    /**
     * Obtiene la modalidad del partido.
     *
     * @return modalidad del partido
     */
    public Modalidad getModalidad() {
        return modalidad;
    }

    /**
     * Obtiene el tipo de partido.
     *
     * @return tipo de partido
     */
    public TipoPartido getTipo() {
        return tipo;
    }

    /**
     * Obtiene la resolución de la cancha.
     *
     * @return resolución de la cancha
     */
    public Resolucion getResolucion() {
        return resolucion;
    }

    /**
     * Obtiene el tiempo del partido.
     *
     * @return tiempo del partido
     */
    public int getTiempo() {
        return tiempo;
    }

    /**
     * Obtiene los goles necesarios para ganar.
     *
     * @return goles necesarios para ganar
     */
    public int getGoles() {
        return goles;
    }

    /**
     * Obtiene el perfil de IA del partido.
     *
     * @return perfil de IA
     */
    public PerfilIA getPerfilIA() {
        return perfilIA;
    }
}
