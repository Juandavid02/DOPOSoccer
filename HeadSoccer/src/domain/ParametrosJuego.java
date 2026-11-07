package domain;

/**
 * Constantes ajustables de DOPOSoccer.
 *
 * Todos los números del juego (física, vida, daño, poderes, puntos y mejoras)
 * viven aquí para no quedar hardcodeados en la lógica.
 */
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

    // Estadísticas (rango y crecimiento)
    public static final int MIN_ESTADISTICA = 1;
    public static final int MAX_ESTADISTICA = 10;
    /** Cada punto de estadística suma un 10 % sobre el valor base. */
    public static final double INCREMENTO_POR_PUNTO = 0.10;

    // Vida y daño
    public static final int VIDA_BASE = 100;
    public static final int DANO_PATADA_BASE = 10;

    // Barra de poder
    public static final double CAPACIDAD_BARRA_PODER = 100;
    /** La barra se llena en 10 s: 100 / 10 = 10 unidades por segundo. */
    public static final double RECARGA_BARRA_PODER = 10;
    public static final double TIEMPO_LLENADO_BARRA_PODER = 10.0;
    /** Al recibir un gol, la barra sube inmediatamente un cuarto. */
    public static final double FRACCION_BARRA_POR_GOL = 0.25;

    // Puntos y mejoras
    public static final int PUNTOS_GANADOR = 100;
    public static final int PUNTOS_PERDEDOR = 25;
    public static final int COSTO_MEJORA_INICIAL = 500;
    /** El precio de cada punto de mejora se duplica: 500, 1000, 2000... */
    public static final int FACTOR_COSTO_MEJORA = 2;

    // Juego
    public static final int TIEMPO_PARTIDO = 60;
    public static final int TIEMPO_DESCANSO = 15;

    private ParametrosJuego() {
    }
}
