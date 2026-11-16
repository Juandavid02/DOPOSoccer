package domain;

/**
 * Resoluciones de cancha disponibles en DOPOSoccer.
 *
 * Cada resolución define el ancho, el alto y la altura del suelo (la línea
 * donde apoyan los jugadores y el balón). Se mantiene un margen inferior de
 * 20 píxeles, igual que en la resolución base de 1280×720.
 */
public enum Resolucion {
    R1280x720(1280, 720, 700),
    R1600x900(1600, 900, 880),
    R1920x1080(1920, 1080, 1060);

    private final double ancho;
    private final double alto;
    private final double suelo;

    /**
     * Crea una resolución con sus dimensiones.
     *
     * @param ancho ancho de la cancha
     * @param alto alto de la cancha
     * @param suelo altura de la línea del suelo
     */
    Resolucion(double ancho, double alto, double suelo) {
        this.ancho = ancho;
        this.alto = alto;
        this.suelo = suelo;
    }

    /**
     * Obtiene el ancho de la cancha.
     *
     * @return ancho en píxeles
     */
    public double getAncho() {
        return ancho;
    }

    /**
     * Obtiene el alto de la cancha.
     *
     * @return alto en píxeles
     */
    public double getAlto() {
        return alto;
    }

    /**
     * Obtiene la altura de la línea del suelo.
     *
     * @return altura del suelo en píxeles
     */
    public double getSuelo() {
        return suelo;
    }
}
