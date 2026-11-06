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

    // Jugador
    public static final double RADIO_JUGADOR = 20;
    public static final double IMPULSO_SALTO_BASE = 300;
    public static final double IMPULSO_SALTO_POR_NIVEL = 80;
    public static final double POTENCIA_PATADA_BASE = 300;
    public static final double POTENCIA_PATADA_POR_NIVEL = 100;

    // Barra de poder
    public static final double CAPACIDAD_BARRA_PODER = 100;
    public static final double RECARGA_BARRA_PODER = 10;
    
    // Juego
    public static final int TIEMPO_PARTIDO = 60;
    public static final int TIEMPO_DESCANSO = 15;
    private ParametrosJuego() {
    }
}