package domain;

import java.util.UUID;

/**
 * Perfil de una persona. No tiene lado fijo ni personaje: en una partida
 * puede ocupar cualquiera de los dos lados y el personaje se elige después.
 */
public class PerfilJugador {

    private static final int ESTADISTICA_INICIAL = 5;

    private final String id;
    private String nombre;
    private ConfiguracionJugador configuracion;

    /**
     * Crea un perfil nuevo con estadísticas iniciales y cero puntos.
     *
     * @param nombre el nombre del perfil
     * @throws ExcepcionDOPOSoccer si el nombre no es válido
     */
    public PerfilJugador(String nombre) throws ExcepcionDOPOSoccer {
        this(UUID.randomUUID().toString(), nombre, estadisticasIniciales(nombre));
    }

    /**
     * Crea un perfil con todos sus datos.
     *
     * @param id identificador único del perfil
     * @param nombre nombre del perfil
     * @param configuracion configuración con estadísticas y puntos
     * @throws ExcepcionDOPOSoccer si algún dato no es válido
     */
    public PerfilJugador(String id, String nombre, ConfiguracionJugador configuracion)
            throws ExcepcionDOPOSoccer {
        if (id == null || id.trim().isEmpty()) {
            throw new ExcepcionDOPOSoccer(ExcepcionDOPOSoccer.ID_INVALIDO);
        }
        validarNombre(nombre);
        if (configuracion == null) {
            throw new ExcepcionDOPOSoccer(ExcepcionDOPOSoccer.CONFIGURACION_NULA);
        }
        this.id = id;
        this.nombre = nombre;
        this.configuracion = configuracion;
    }

    private static ConfiguracionJugador estadisticasIniciales(String nombre)
            throws ExcepcionDOPOSoccer {
        validarNombre(nombre);
        Estadisticas estadisticas = new Estadisticas(
                ESTADISTICA_INICIAL,
                ESTADISTICA_INICIAL,
                ESTADISTICA_INICIAL,
                ESTADISTICA_INICIAL);
        return new ConfiguracionJugador(nombre, estadisticas);
    }

    private static void validarNombre(String nombre) throws ExcepcionDOPOSoccer {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ExcepcionDOPOSoccer(ExcepcionDOPOSoccer.NOMBRE_INVALIDO);
        }
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    /**
     * Cambia el nombre del perfil conservando sus estadísticas y puntos.
     *
     * @param nombre el nuevo nombre
     * @throws ExcepcionDOPOSoccer si el nombre no es válido
     */
    public void setNombre(String nombre) throws ExcepcionDOPOSoccer {
        validarNombre(nombre);
        this.nombre = nombre;
        this.configuracion = new ConfiguracionJugador(
                nombre,
                configuracion.getEstadisticas(),
                configuracion.getPuntos());
    }

    public ConfiguracionJugador getConfiguracion() {
        return configuracion;
    }

    public Estadisticas getEstadisticas() {
        return configuracion.getEstadisticas();
    }

    public Puntos getPuntos() {
        return configuracion.getPuntos();
    }

    /**
     * Calcula cuántas mejoras se han comprado, tomando como base las
     * estadísticas iniciales.
     *
     * @return cantidad de puntos de estadística mejorados
     */
    public int getMejorasCompradas() {
        Estadisticas estadisticas = configuracion.getEstadisticas();
        int total = (estadisticas.getSpeed() - ESTADISTICA_INICIAL)
                + (estadisticas.getJump() - ESTADISTICA_INICIAL)
                + (estadisticas.getKick() - ESTADISTICA_INICIAL)
                + (estadisticas.getSurvival() - ESTADISTICA_INICIAL);
        return Math.max(0, total);
    }

    /**
     * Costo de la siguiente mejora. El precio se duplica después de cada
     * compra: 500, 1000, 2000, 4000...
     *
     * @return costo en puntos
     */
    public int getCostoSiguienteMejora() {
        long costo = ParametrosJuego.COSTO_MEJORA_INICIAL;
        for (int i = 0; i < getMejorasCompradas(); i++) {
            costo *= ParametrosJuego.FACTOR_COSTO_MEJORA;
        }
        return (int) Math.min(Integer.MAX_VALUE, costo);
    }

    /**
     * Indica si una estadística todavía puede subir.
     *
     * @param tipo estadística consultada
     * @return true si está por debajo del máximo
     */
    public boolean puedeMejorar(TipoEstadistica tipo) {
        if (tipo == null) {
            return false;
        }
        return tipo.getValor(configuracion.getEstadisticas())
                < ParametrosJuego.MAX_ESTADISTICA;
    }

    /**
     * Compra un punto de mejora para una estadística y descuenta los puntos
     * del perfil.
     *
     * @param tipo estadística que se mejora
     * @return true si la compra se realizó
     * @throws ExcepcionDOPOSoccer si no se puede crear la nueva configuración
     */
    public boolean mejorar(TipoEstadistica tipo) throws ExcepcionDOPOSoccer {
        if (!puedeMejorar(tipo)) {
            return false;
        }

        int costo = getCostoSiguienteMejora();
        if (!configuracion.getPuntos().gastar(costo)) {
            return false;
        }

        Estadisticas actuales = configuracion.getEstadisticas();
        int speed = actuales.getSpeed();
        int jump = actuales.getJump();
        int kick = actuales.getKick();
        int survival = actuales.getSurvival();

        switch (tipo) {
            case VELOCIDAD:
                speed++;
                break;
            case SALTO:
                jump++;
                break;
            case PATADA:
                kick++;
                break;
            case SUPERVIVENCIA:
                survival++;
                break;
            default:
                throw new IllegalStateException("Estadística desconocida: " + tipo);
        }

        configuracion = new ConfiguracionJugador(
                nombre,
                new Estadisticas(speed, jump, kick, survival),
                configuracion.getPuntos());
        return true;
    }
}
