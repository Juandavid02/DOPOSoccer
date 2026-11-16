package domain;

/**
 * Geometría de la cancha: dimensiones, suelo y arcos.
 *
 * La cancha se construye a partir de una resolución. Los arcos se ubican
 * justo fuera de las líneas de gol, apoyados sobre el suelo.
 */
public class Cancha {
    private final Resolucion resolucion;
    private final double ancho;
    private final double alto;
    private final double suelo;
    private final Arco arcoIzquierdo;
    private final Arco arcoDerecho;

    /**
     * Crea la cancha a partir de la resolución indicada.
     *
     * @param resolucion resolución que define las dimensiones
     */
    public Cancha(Resolucion resolucion) {
        if (resolucion == null) {
            throw new IllegalArgumentException("La resolución no puede ser nula");
        }

        this.resolucion = resolucion;
        this.ancho = resolucion.getAncho();
        this.alto = resolucion.getAlto();
        this.suelo = resolucion.getSuelo();

        double yArco = suelo - ParametrosJuego.ALTO_ARCO;
        this.arcoIzquierdo = new Arco(0, yArco);
        this.arcoDerecho = new Arco(ancho - ParametrosJuego.ANCHO_ARCO, yArco);
    }

    /**
     * Obtiene la resolución de la cancha.
     *
     * @return resolución
     */
    public Resolucion getResolucion() {
        return resolucion;
    }

    /**
     * Obtiene el ancho de la cancha.
     *
     * @return ancho
     */
    public double getAncho() {
        return ancho;
    }

    /**
     * Obtiene el alto de la cancha.
     *
     * @return alto
     */
    public double getAlto() {
        return alto;
    }

    /**
     * Obtiene la coordenada del suelo de la cancha.
     *
     * @return coordenada del suelo
     */
    public double getSuelo() {
        return suelo;
    }

    /**
     * Obtiene el arco izquierdo.
     *
     * @return arco izquierdo
     */
    public Arco getArcoIzquierdo() {
        return arcoIzquierdo;
    }

    /**
     * Obtiene el arco derecho.
     *
     * @return arco derecho
     */
    public Arco getArcoDerecho() {
        return arcoDerecho;
    }

    /**
     * Indica si el balón está a la altura de la boca del arco izquierdo.
     *
     * @param yBalon coordenada vertical del balón
     * @param radio radio del balón
     * @return true si el balón está en la boca del arco izquierdo
     */
    public boolean estaEnBocaArcoIzquierdo(double yBalon, double radio) {
        return arcoIzquierdo.estaEnBoca(yBalon, radio);
    }

    /**
     * Indica si el balón está a la altura de la boca del arco derecho.
     *
     * @param yBalon coordenada vertical del balón
     * @param radio radio del balón
     * @return true si el balón está en la boca del arco derecho
     */
    public boolean estaEnBocaArcoDerecho(double yBalon, double radio) {
        return arcoDerecho.estaEnBoca(yBalon, radio);
    }
}
