package domain;

/**
 * Efecto temporal que sufre un jugador después de recibir un tiro de poder.
 * Cada efecto concreto (Empuje, Barril, etc.) sabe qué le hace al jugador
 * cuando empieza, mientras dura y cuando termina.
 */
public abstract class Efecto {

    private final TipoEfecto tipo;
    private double tiempoRestante;

    /**
     * Crea un efecto del tipo indicado con la duración dada.
     *
     * @param tipo tipo de efecto
     * @param duracion duración del efecto en segundos
     */
    protected Efecto(TipoEfecto tipo, double duracion) {
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de efecto no puede ser nulo");
        }
        if (duracion <= 0) {
            throw new IllegalArgumentException("La duración del efecto debe ser mayor que cero");
        }
        this.tipo = tipo;
        this.tiempoRestante = duracion;
    }

    /**
     * Obtiene el tipo de efecto.
     *
     * @return tipo de efecto
     */
    public TipoEfecto getTipo() {
        return tipo;
    }

    /**
     * Obtiene el tiempo restante del efecto.
     *
     * @return tiempo restante en segundos
     */
    public double getTiempoRestante() {
        return tiempoRestante;
    }

    /**
     * Reinicia el temporizador del efecto con una nueva duración.
     *
     * @param duracion nueva duración en segundos
     */
    public void renovar(double duracion) {
        if (duracion <= 0) {
            throw new IllegalArgumentException("La duración debe ser mayor que cero");
        }
        tiempoRestante = duracion;
    }

    /**
     * Descuenta el tiempo transcurrido del efecto.
     *
     * @param dt tiempo transcurrido
     */
    public void actualizar(double dt) {
        // Un dt no positivo no representa tiempo transcurrido.
        if (dt <= 0) {
            return;
        }
        tiempoRestante = Math.max(0, tiempoRestante - dt);
    }

    /**
     * Indica si el efecto ya terminó.
     *
     * @return true si el efecto terminó
     */
    public boolean terminado() {
        return tiempoRestante <= 0;
    }

    /**
     * Se llama cuando el efecto le llega al jugador. Por defecto no hace
     * nada; los efectos que cambian algo al empezar lo sobrescriben.
     *
     * @param jugador jugador que recibe el efecto
     */
    public void alAplicar(Jugador jugador) {
    }

    /**
     * Se llama cuando el efecto se acaba (o se borra por un gol). Por
     * defecto no hace nada; sirve para devolver al jugador a la normalidad.
     *
     * @param jugador jugador que tenía el efecto
     */
    public void alTerminar(Jugador jugador) {
    }

    /**
     * Indica si este efecto le impide moverse al jugador.
     *
     * @return true si bloquea el movimiento
     */
    public boolean bloqueaMovimiento() {
        return false;
    }

    /**
     * Indica si este efecto le impide saltar al jugador.
     *
     * @return true si bloquea el salto
     */
    public boolean bloqueaSalto() {
        return false;
    }

    /**
     * Indica si este efecto le impide patear al jugador.
     *
     * @return true si bloquea la patada
     */
    public boolean bloqueaPatada() {
        return false;
    }

    /**
     * Indica si este efecto invierte los controles del jugador.
     *
     * @return true si los controles quedan invertidos
     */
    public boolean invierteControles() {
        return false;
    }

    /**
     * Indica si este efecto hace que el jugador vea balones falsos.
     *
     * @return true si el efecto confunde al jugador
     */
    public boolean confunde() {
        return false;
    }

    /**
     * Devuelve cuánto frena este efecto al jugador. 1 es velocidad normal.
     *
     * @return factor que multiplica la velocidad normal
     */
    public double getMultiplicadorVelocidad() {
        return 1.0;
    }
}
