package domain;

public class Cancha {
    private final double ancho;
    private final double alto;
    private final double suelo;
    private final Arco arcoIzquierdo;
    private final Arco arcoDerecho;

    public Cancha() {
        this.ancho = ParametrosJuego.ANCHO_CANCHA;
        this.alto = ParametrosJuego.ALTO_CANCHA;
        this.suelo = ParametrosJuego.SUELO_Y;
        
        double yArco = ParametrosJuego.SUELO_Y - ParametrosJuego.ALTO_ARCO;
        this.arcoIzquierdo = new Arco(-ParametrosJuego.ANCHO_ARCO, yArco);
        this.arcoDerecho = new Arco(ParametrosJuego.ANCHO_CANCHA, yArco);
    }

    public double getAncho() {
        return ancho;
    }

    public double getAlto() {
        return alto;
    }

    public double getSuelo() {
        return suelo;
    }

    public Arco getArcoIzquierdo() {
        return arcoIzquierdo;
    }

    public Arco getArcoDerecho() {
        return arcoDerecho;
    }

    public boolean estaEnBocaArcoIzquierdo(double yBalon, double radio) {
        return arcoIzquierdo.estaEnBoca(yBalon, radio);
    }

    public boolean estaEnBocaArcoDerecho(double yBalon, double radio) {
        return arcoDerecho.estaEnBoca(yBalon, radio);
    }

}
