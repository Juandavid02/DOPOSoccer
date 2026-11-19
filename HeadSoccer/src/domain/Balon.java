package domain;

import java.io.Serializable;
public class Balon implements Serializable {
    private double x;
    private double y;
    private double velocidadX;
    private double velocidadY;
    private double radio;

    /**
     * Crea un balón en la posición indicada y sin velocidad.
     *
     * @param x coordenada horizontal inicial
     * @param y coordenada vertical inicial
     * @param radio radio del balón
     */
    public Balon(double x, double y, double radio) {
        this.x = x;
        this.y = y;
        this.radio = radio;
        this.velocidadX = 0;
        this.velocidadY = 0;
    }

    /**
     * Obtiene la coordenada horizontal del balón.
     *
     * @return coordenada horizontal
     */
    public double getX() {
        return x;
    }

    /**
     * Obtiene la coordenada vertical del balón.
     *
     * @return coordenada vertical
     */
    public double getY() {
        return y;
    }

    /**
     * Obtiene el radio del balón.
     *
     * @return radio del balón
     */
    public double getRadio() {
        return radio;
    }

    /**
     * Obtiene la velocidad horizontal del balón.
     *
     * @return velocidad horizontal
     */
    public double getVelocidadX() {
        return velocidadX;
    }

    /**
     * Obtiene la velocidad vertical del balón.
     *
     * @return velocidad vertical
     */
    public double getVelocidadY() {
        return velocidadY;
    }

    /**
     * Suma un impulso a la velocidad del balón.
     *
     * @param impulsoX impulso horizontal
     * @param impulsoY impulso vertical
     */
    public void aplicarImpulso(double impulsoX, double impulsoY) {
        this.velocidadX += impulsoX;
        this.velocidadY += impulsoY;
    }

    /**
     * Fija la velocidad del balón. Se usa al ejecutar un tiro de poder para
     * que salga con la trayectoria exacta del poder.
     *
     * @param velocidadX nueva velocidad horizontal
     * @param velocidadY nueva velocidad vertical
     */
    public void establecerVelocidad(double velocidadX, double velocidadY) {
        this.velocidadX = velocidadX;
        this.velocidadY = velocidadY;
    }

    /**
     * Actualiza la posición del balón aplicando gravedad, fricción y rebotes.
     *
     * @param dt tiempo transcurrido
     * @param cancha cancha donde se mueve el balón
     */
    public void actualizar(double dt, Cancha cancha) {
        velocidadY += ParametrosJuego.GRAVEDAD * dt;
        // IA Generativa para que el juego no dependa de la velocidad de fotogramas y se comporte igual en cualquier máquina.
        velocidadX *= Math.pow(ParametrosJuego.FRICCION, dt*60);
        x += velocidadX * dt;
        y += velocidadY * dt;

        double suelo = cancha.getSuelo();
        if (y + radio >= suelo) {
            y = suelo - radio;
            if (velocidadY > 0) {
                velocidadY = -velocidadY * ParametrosJuego.REBOTE;
                velocidadX *= ParametrosJuego.PERDIDA_REBOTE;
            }
        }
        if (y - radio <= 0) {
            y = radio;
            if (velocidadY < 0) {
                velocidadY = -velocidadY * ParametrosJuego.REBOTE;
                velocidadX *= ParametrosJuego.PERDIDA_REBOTE;
            }
        }

        if (x - radio <= 0) {
            x = radio;
            if (velocidadX < 0) {
                velocidadX = -velocidadX * ParametrosJuego.PERDIDA_REBOTE;
            }
        }

        if (x + radio > cancha.getAncho()) {
            x = cancha.getAncho() - radio;
            if (velocidadX > 0) {
                velocidadX = -velocidadX * ParametrosJuego.PERDIDA_REBOTE;
            }
        }
    }

    /**
     * Reinicia la posición del balón y detiene su movimiento.
     *
     * @param x nueva coordenada horizontal
     * @param y nueva coordenada vertical
     */
    public void reiniciar(double x, double y) {
        this.x = x;
        this.y = y;
        this.velocidadX = 0;
        this.velocidadY = 0;
    }

    /**
     * Mueve el balón sin cambiar su velocidad. Se usa para sacarlo de un
     * personaje cuando hay una colisión.
     *
     * @param x nueva posición horizontal
     * @param y nueva posición vertical
     */
    public void reubicar(double x, double y) {
        this.x = x;
        this.y = y;
    }
}
