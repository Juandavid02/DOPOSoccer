package domain;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Perfil de una persona. No tiene lado fijo ni personaje: en una partida
 * puede ocupar cualquiera de los dos lados y el personaje se elige después.
 *
 * Cada perfil guarda sus propios personajes, balones y escenarios
 * desbloqueados. Lo que compra un perfil en la tienda no lo desbloquea para
 * los demás perfiles.
 */
public class PerfilJugador implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Personajes disponibles para todo perfil nuevo. */
    private static final List<String> PERSONAJES_INICIALES = List.of(
            "Betty la fea",
            "El Chavo del Ocho",
            "Goku",
            "Bob Esponja");

    /** Balones disponibles para todo perfil nuevo. */
    private static final List<String> BALONES_INICIALES = List.of(
            "Mario",
            "Betty");

    /** Escenarios disponibles para todo perfil nuevo. */
    private static final List<String> ESCENARIOS_INICIALES = List.of(
            "Fondo de Bikini",
            "Tenkaichi Budokai",
            "Vecindad del Chavo");

    private final String id;
    private String nombre;
    private ConfiguracionJugador configuracion;
    private final Set<String> personajesDesbloqueados =
            new HashSet<>(PERSONAJES_INICIALES);
    private final Set<String> balonesDesbloqueados =
            new HashSet<>(BALONES_INICIALES);
    private final Set<String> escenariosDesbloqueados =
            new HashSet<>(ESCENARIOS_INICIALES);

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

    /**
     * Crea la configuración inicial de un perfil con estadísticas base.
     *
     * @param nombre nombre del perfil
     * @return configuración inicial del perfil
     * @throws ExcepcionDOPOSoccer si el nombre no es válido
     */
    private static ConfiguracionJugador estadisticasIniciales(String nombre)
            throws ExcepcionDOPOSoccer {
        validarNombre(nombre);
        int inicial = ParametrosJuego.ESTADISTICA_INICIAL;
        Estadisticas estadisticas = new Estadisticas(
                inicial, inicial, inicial, inicial);
        return new ConfiguracionJugador(nombre, estadisticas);
    }

    /**
     * Comprueba que el nombre del perfil sea válido.
     *
     * @param nombre nombre a validar
     * @throws ExcepcionDOPOSoccer si el nombre no es válido
     */
    private static void validarNombre(String nombre) throws ExcepcionDOPOSoccer {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ExcepcionDOPOSoccer(ExcepcionDOPOSoccer.NOMBRE_INVALIDO);
        }
    }

    /**
     * Devuelve el identificador del perfil.
     *
     * @return identificador único
     */
    public String getId() {
        return id;
    }

    /**
     * Devuelve el nombre del perfil.
     *
     * @return nombre del perfil
     */
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

    /**
     * Devuelve la configuración del perfil.
     *
     * @return configuración con estadísticas y puntos
     */
    public ConfiguracionJugador getConfiguracion() {
        return configuracion;
    }

    /**
     * Devuelve las estadísticas del perfil.
     *
     * @return estadísticas del perfil
     */
    public Estadisticas getEstadisticas() {
        return configuracion.getEstadisticas();
    }

    /**
     * Devuelve los puntos del perfil.
     *
     * @return puntos acumulados
     */
    public Puntos getPuntos() {
        return configuracion.getPuntos();
    }

    /**
     * Indica si este perfil desbloqueó un personaje.
     *
     * @param nombre nombre del personaje
     * @return true si el perfil puede usar ese personaje
     */
    public boolean estaDesbloqueadoPersonaje(String nombre) {
        return nombre != null && personajesDesbloqueados.contains(nombre);
    }

    /**
     * Indica si este perfil desbloqueó un balón.
     *
     * @param nombre nombre del balón
     * @return true si el perfil puede usar ese balón
     */
    public boolean estaDesbloqueadoBalon(String nombre) {
        return nombre != null && balonesDesbloqueados.contains(nombre);
    }

    /**
     * Indica si este perfil desbloqueó un escenario.
     *
     * @param nombre nombre del escenario
     * @return true si el perfil puede usar ese escenario
     */
    public boolean estaDesbloqueadoEscenario(String nombre) {
        return nombre != null && escenariosDesbloqueados.contains(nombre);
    }

    /**
     * Marca un personaje como desbloqueado para este perfil.
     *
     * @param nombre nombre del personaje
     * @return true si el personaje no estaba desbloqueado
     */
    public boolean desbloquearPersonaje(String nombre) {
        return nombre != null && personajesDesbloqueados.add(nombre);
    }

    /**
     * Marca un balón como desbloqueado para este perfil.
     *
     * @param nombre nombre del balón
     * @return true si el balón no estaba desbloqueado
     */
    public boolean desbloquearBalon(String nombre) {
        return nombre != null && balonesDesbloqueados.add(nombre);
    }

    /**
     * Marca un escenario como desbloqueado para este perfil.
     *
     * @param nombre nombre del escenario
     * @return true si el escenario no estaba desbloqueado
     */
    public boolean desbloquearEscenario(String nombre) {
        return nombre != null && escenariosDesbloqueados.add(nombre);
    }

    /**
     * Devuelve los personajes desbloqueados del perfil.
     *
     * @return conjunto de personajes desbloqueados
     */
    public Set<String> getPersonajesDesbloqueados() {
        return Collections.unmodifiableSet(personajesDesbloqueados);
    }

    /**
     * Devuelve los balones desbloqueados del perfil.
     *
     * @return conjunto de balones desbloqueados
     */
    public Set<String> getBalonesDesbloqueados() {
        return Collections.unmodifiableSet(balonesDesbloqueados);
    }

    /**
     * Devuelve los escenarios desbloqueados del perfil.
     *
     * @return conjunto de escenarios desbloqueados
     */
    public Set<String> getEscenariosDesbloqueados() {
        return Collections.unmodifiableSet(escenariosDesbloqueados);
    }

    /**
     * Calcula cuántas mejoras se han comprado, tomando como base las
     * estadísticas iniciales.
     *
     * @return cantidad de puntos de estadística mejorados
     */
    public int getMejorasCompradas() {
        Estadisticas estadisticas = configuracion.getEstadisticas();
        int inicial = ParametrosJuego.ESTADISTICA_INICIAL;
        int total = (estadisticas.getSpeed() - inicial)
                + (estadisticas.getJump() - inicial)
                + (estadisticas.getKick() - inicial)
                + (estadisticas.getSurvival() - inicial);
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
