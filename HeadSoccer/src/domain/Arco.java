package domain;

public class Arco {
    private final double x;
    private final double y;
    private final double ancho;
    private final double alto;

    public Arco(double x, double y) {
        this.x = x;
        this.y = y;
        this.ancho = ParametrosJuego.ANCHO_ARCO;
        this.alto = ParametrosJuego.ALTO_ARCO;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getAncho() {
        return ancho;
    }

    public double getAlto() {
        return alto;
    }

    public boolean estaEnBoca(double yBalon, double radio) {
        return yBalon + radio >= y && yBalon - radio <= y + alto;
    }

    public boolean estaDentro(Balon balon) {
        return balon.getX() - balon.getRadio() >= x &&
            balon.getX() + balon.getRadio() <= x + ancho &&
            balon.getY() - balon.getRadio() >= y &&
            balon.getY() + balon.getRadio() <= y + alto;
    }

}
