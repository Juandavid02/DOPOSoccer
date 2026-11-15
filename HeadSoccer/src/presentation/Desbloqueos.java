package presentation;

import domain.PerfilJugador;

/**
 * Precios y compras de la tienda. El estado de lo desbloqueado pertenece a
 * cada perfil: un personaje comprado por un perfil no queda disponible para
 * los demás.
 */
public final class Desbloqueos {

    static final int PRECIO_PERSONAJE = 500;
    static final int PRECIO_BALON = 300;
    static final int PRECIO_ESCENARIO = 400;

    /**
     * Indica si un personaje está desbloqueado para el perfil.
     *
     * @param nombre nombre del personaje
     * @param perfil perfil del jugador
     * @return true si el personaje está desbloqueado
     */
    public boolean estaDesbloqueadoPersonaje(String nombre, PerfilJugador perfil) {
        return nombre != null && perfil != null
                && perfil.estaDesbloqueadoPersonaje(nombre);
    }

    /**
     * Indica si un balón está desbloqueado para el perfil.
     *
     * @param nombre nombre del balón
     * @param perfil perfil del jugador
     * @return true si el balón está desbloqueado
     */
    public boolean estaDesbloqueadoBalon(String nombre, PerfilJugador perfil) {
        return nombre != null && perfil != null
                && perfil.estaDesbloqueadoBalon(nombre);
    }

    /**
     * Indica si un escenario está desbloqueado para el perfil.
     *
     * @param nombre nombre del escenario
     * @param perfil perfil del jugador
     * @return true si el escenario está desbloqueado
     */
    public boolean estaDesbloqueadoEscenario(String nombre, PerfilJugador perfil) {
        return nombre != null && perfil != null
                && perfil.estaDesbloqueadoEscenario(nombre);
    }

    /**
     * Compra un personaje para el perfil si tiene puntos suficientes.
     *
     * @param nombre nombre del personaje
     * @param perfil perfil que paga la compra
     * @return true si la compra se realizó
     */
    public boolean desbloquearPersonaje(String nombre, PerfilJugador perfil) {
        if (nombre == null || perfil == null
                || perfil.estaDesbloqueadoPersonaje(nombre)) {
            return false;
        }
        if (!perfil.getPuntos().gastar(PRECIO_PERSONAJE)) {
            return false;
        }
        return perfil.desbloquearPersonaje(nombre);
    }

    /**
     * Compra un balón para el perfil si tiene puntos suficientes.
     *
     * @param nombre nombre del balón
     * @param perfil perfil que paga la compra
     * @return true si la compra se realizó
     */
    public boolean desbloquearBalon(String nombre, PerfilJugador perfil) {
        if (nombre == null || perfil == null
                || perfil.estaDesbloqueadoBalon(nombre)) {
            return false;
        }
        if (!perfil.getPuntos().gastar(PRECIO_BALON)) {
            return false;
        }
        return perfil.desbloquearBalon(nombre);
    }

    /**
     * Compra un escenario para el perfil si tiene puntos suficientes.
     *
     * @param nombre nombre del escenario
     * @param perfil perfil que paga la compra
     * @return true si la compra se realizó
     */
    public boolean desbloquearEscenario(String nombre, PerfilJugador perfil) {
        if (nombre == null || perfil == null
                || perfil.estaDesbloqueadoEscenario(nombre)) {
            return false;
        }
        if (!perfil.getPuntos().gastar(PRECIO_ESCENARIO)) {
            return false;
        }
        return perfil.desbloquearEscenario(nombre);
    }
}
