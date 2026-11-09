package domain;

/**
 * Geometría de la cancha: dimensiones, suelo y arcos.
 *
 * La cancha se construye a partir de una. Los arcos se
 * ubican justo fuera de las líneas de gol, apoyados sobre el suelo.</p>
 */
public class Cancha {
    private final Resolucion resolucion;
    private final double ancho;
    private final double alto;
    private final double suelo;
    private final Arco arcoIzquierdo;
    private final Arco arcoDerecho;

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

    public Resolucion getResolucion() {
        return resolucion;
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
