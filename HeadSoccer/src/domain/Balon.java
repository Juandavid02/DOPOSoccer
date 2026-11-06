package domain;
public class Balon {
    private double x;
    private double y;
    private double velocidadX;
    private double velocidadY;
    private double radio;

    public Balon(double x, double y, double radio) {
        this.x = x;
        this.y = y;
        this.radio = radio;
        this.velocidadX = 0;
        this.velocidadY = 0;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getRadio() {
        return radio;
    }

    public double getVelocidadX() {
        return velocidadX;
    }

    public double getVelocidadY() {
        return velocidadY;
    }
    
    public void aplicarImpulso(double impulsoX, double impulsoY) {
        this.velocidadX += impulsoX;
        this.velocidadY += impulsoY;
    }

    public void actualizar(double dt, Cancha cancha) {
        velocidadY += ParametrosJuego.GRAVEDAD * dt;
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

        if (x - radio <= 0 && !cancha.estaEnBocaArcoIzquierdo(y, radio)) {
            x = radio;
            if (velocidadX < 0) {
                velocidadX = -velocidadX * ParametrosJuego.PERDIDA_REBOTE;
            }
        }

        if (x + radio > cancha.getAncho() && !cancha.estaEnBocaArcoDerecho(y, radio)) {
            x = cancha.getAncho() - radio;
            if (velocidadX > 0) {
                velocidadX = -velocidadX * ParametrosJuego.PERDIDA_REBOTE;
            }
        }
    }

    public void reiniciar(double x, double y) {
        this.x = x;
        this.y = y;
        this.velocidadX = 0;
        this.velocidadY = 0;
    }
}
