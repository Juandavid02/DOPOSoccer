package domain;

public class ParametrosJuego {

    // Física
    public static final double GRAVEDAD = 1200;   
    public static final double REBOTE = 0.72;
    public static final double FRICCION = 0.995;
    public static final double PERDIDA_REBOTE = 0.8;

    // Balón
    public static final int RADIO_BALON = 12;  

    // Cancha
    public static final int ANCHO_CANCHA = 1280;
    public static final int ALTO_CANCHA = 720;
    public static final int SUELO_Y = 700;

    // Arcos
    public static final int ANCHO_ARCO = 120;
    public static final int ALTO_ARCO = 220;

    private ParametrosJuego() {
    }
}