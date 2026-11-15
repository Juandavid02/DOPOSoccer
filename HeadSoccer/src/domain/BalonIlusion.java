package domain;

import java.util.Random;

/**
 * Balón falso creado por el poder de Ilusión. Se mueve de forma aleatoria
 * por la cancha y empuja levemente al jugador confundido cuando lo toca.
 */
public class BalonIlusion {

    private static final double VELOCIDAD_MINIMA = 150;
    private static final double VELOCIDAD_MAXIMA = 330;
    private static final double RADIO = ParametrosJuego.RADIO_BALON_COLISION;

    private final Random random;

    private double x;
    private double y;
    private double velocidadX;
    private double velocidadY;
    private double tiempoCambioDireccion;
    private double tiempoEmpuje;

    /**
     * Crea un balón de ilusión en la posición indicada.
     *
     * @param x coordenada horizontal inicial
     * @param y coordenada vertical inicial
     * @param random generador aleatorio para los cambios de dirección
     */
    public BalonIlusion(double x, double y, Random random) {
        if (random == null) {
            throw new IllegalArgumentException("El generador aleatorio no puede ser nulo");
        }
        this.x = x;
        this.y = y;
        this.random = random;
        this.tiempoEmpuje = 0.4;
        cambiarDireccion();
    }

    /**
     * Mueve el balón y lo hace rebotar con los bordes de la cancha.
     *
     * @param dt tiempo transcurrido desde la última actualización
     * @param cancha cancha sobre la que se mueve el balón
     */
    public void actualizar(double dt, Cancha cancha) {
        if (dt <= 0 || cancha == null) {
            return;
        }

        tiempoCambioDireccion -= dt;
        if (tiempoCambioDireccion <= 0) {
            cambiarDireccion();
        }
        tiempoEmpuje = Math.max(0, tiempoEmpuje - dt);

        x += velocidadX * dt;
        y += velocidadY * dt;

        double minX = RADIO;
        double maxX = cancha.getAncho() - RADIO;
        double minY = RADIO;
        double maxY = cancha.getSuelo() - RADIO;

        if (x < minX) {
            x = minX;
            velocidadX = Math.abs(velocidadX);
        } else if (x > maxX) {
            x = maxX;
            velocidadX = -Math.abs(velocidadX);
        }

        if (y < minY) {
            y = minY;
            velocidadY = Math.abs(velocidadY);
        } else if (y > maxY) {
            y = maxY;
            velocidadY = -Math.abs(velocidadY);
        }
    }

    /**
     * Indica si el balón falso está tocando al jugador.
     *
     * @param jugador jugador que se comprueba
     * @return true si el balón está tocando al jugador
     */
    public boolean tocaA(Jugador jugador) {
        if (jugador == null) {
            return false;
        }
        double dx = jugador.getPosicionX() - x;
        double dy = jugador.getPosicionY() - y;
        double distancia = Math.sqrt(dx * dx + dy * dy);
        return distancia <= RADIO + ParametrosJuego.RADIO_JUGADOR;
    }

    /**
     * Indica si el balón puede volver a empujar al jugador.
     *
     * @return true si ya puede empujar de nuevo
     */
    public boolean puedeEmpujar() {
        return tiempoEmpuje <= 0;
    }

    /**
     * Reinicia el tiempo de espera entre empujes.
     */
    public void reiniciarEmpuje() {
        tiempoEmpuje = ParametrosJuego.TIEMPO_ENTRE_EMPUJES_ILUSION;
        // Al empujar cambia de dirección para no quedarse pegado al jugador.
        cambiarDireccion();
    }

    /**
     * Obtiene la coordenada horizontal del balón de ilusión.
     *
     * @return coordenada horizontal
     */
    public double getX() {
        return x;
    }

    /**
     * Obtiene la coordenada vertical del balón de ilusión.
     *
     * @return coordenada vertical
     */
    public double getY() {
        return y;
    }

    /**
     * Cambia la dirección y la velocidad del balón de forma aleatoria.
     */
    private void cambiarDireccion() {
        double angulo = random.nextDouble() * Math.PI * 2;
        double velocidad = VELOCIDAD_MINIMA
                + random.nextDouble() * (VELOCIDAD_MAXIMA - VELOCIDAD_MINIMA);
        velocidadX = Math.cos(angulo) * velocidad;
        velocidadY = Math.sin(angulo) * velocidad;
        tiempoCambioDireccion = 0.45 + random.nextDouble() * 0.9;
    }
}
