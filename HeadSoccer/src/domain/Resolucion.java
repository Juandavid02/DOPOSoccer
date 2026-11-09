package domain;

/**
 * Resoluciones de cancha disponibles en DOPOSoccer.
 *
 * Cada resolución define el ancho, el alto y la altura del suelo (la línea
 * donde apoyan los jugadores y el balón). Se mantiene un margen inferior de
 * 20 píxeles, igual que en la resolución base de 1280×720.</p>
 */
public enum Resolucion {
    R1280x720(1280, 720, 700),
    R1600x900(1600, 900, 880),
    R1920x1080(1920, 1080, 1060);

    private final double ancho;
    private final double alto;
    private final double suelo;

    Resolucion(double ancho, double alto, double suelo) {
        this.ancho = ancho;
        this.alto = alto;
        this.suelo = suelo;
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
}
