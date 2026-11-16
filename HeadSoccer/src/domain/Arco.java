package domain;

public class Arco {
    private final double x;
    private final double y;
    private final double ancho;
    private final double alto;

    /**
     * Crea un arco en la posición indicada.
     *
     * @param x coordenada horizontal del arco
     * @param y coordenada vertical del arco
     */
    public Arco(double x, double y) {
        this.x = x;
        this.y = y;
        this.ancho = ParametrosJuego.ANCHO_ARCO;
        this.alto = ParametrosJuego.ALTO_ARCO;
    }

    /**
     * Obtiene la coordenada horizontal del arco.
     *
     * @return coordenada horizontal
     */
    public double getX() {
        return x;
    }

    /**
     * Obtiene la coordenada vertical del arco.
     *
     * @return coordenada vertical
     */
    public double getY() {
        return y;
    }

    /**
     * Obtiene el ancho del arco.
     *
     * @return ancho del arco
     */
    public double getAncho() {
        return ancho;
    }

    /**
     * Obtiene el alto del arco.
     *
     * @return alto del arco
     */
    public double getAlto() {
        return alto;
    }

    /**
     * Indica si el balón está a la altura de la boca del arco.
     *
     * @param yBalon coordenada vertical del balón
     * @param radio radio del balón
     * @return true si el balón está en la boca del arco
     */
    public boolean estaEnBoca(double yBalon, double radio) {
        return yBalon + radio >= y && yBalon - radio <= y + alto;
    }

    /**
     * Indica si el balón está completamente dentro del arco.
     *
     * @param balon balón que se evalúa
     * @return true si el balón está dentro del arco
     */
    public boolean estaDentro(Balon balon) {
        return balon.getX() - balon.getRadio() >= x &&
            balon.getX() + balon.getRadio() <= x + ancho &&
            balon.getY() - balon.getRadio() >= y &&
            balon.getY() + balon.getRadio() <= y + alto;
    }

}
