package domain;

/**
 * Constantes ajustables de DOPOSoccer.
 *
 * Todos los números del juego (física, vida, daño, barra de poder y puntos)
 * viven aquí para no quedar hardcodeados en la lógica. Las dimensiones de la
 * cancha ya no están aquí: las define {@link Resolucion}.
 */
public class ParametrosJuego {

    // Física
    public static final double GRAVEDAD = 1200;
    public static final double REBOTE = 0.92;
    public static final double FRICCION = 0.995;
    public static final double PERDIDA_REBOTE = 0.9;

    // Balón
    public static final int RADIO_BALON = 12;

    // Arcos
    public static final int ANCHO_ARCO = 120;
    public static final int ALTO_ARCO = 220;

    // Jugador
    public static final double RADIO_JUGADOR = 20;

    // Círculos de colisión del personaje: cabeza y bota.
    public static final double RADIO_CABEZA_JUGADOR = 45;
    public static final double RADIO_BOTA_JUGADOR = 18;
    public static final double OFFSET_CABEZA_JUGADOR = -55;
    public static final double OFFSET_BOTA_X_JUGADOR = 10;
    public static final double OFFSET_BOTA_Y_JUGADOR = 8;

    /** Radio visual del balón para calcular sus choques. */
    public static final double RADIO_BALON_COLISION = 30;

    public static final double RESTITUCION_COLISION = 0.8;
    /** Distancia a la que se puede patear el balón: pie, balón y tolerancia visual. */
    public static final double ALCANCE_PATADA = RADIO_BOTA_JUGADOR + RADIO_BALON_COLISION + 17;
    public static final double IMPULSO_SALTO_BASE = 300;
    public static final double IMPULSO_SALTO_POR_NIVEL = 80;
    public static final double POTENCIA_PATADA_BASE = 650;
    /** Componente hacia arriba de la patada para que el balón haga curva. */
    public static final double POTENCIA_LEVANTE_PATADA = 520;

    // Estadísticas (rango y crecimiento)
    public static final int MIN_ESTADISTICA = 1;
    public static final int MAX_ESTADISTICA = 10;
    /** Valor de cada estadística en un perfil sin mejoras. */
    public static final int ESTADISTICA_INICIAL = 5;
    /** Cada punto de estadística suma un 10 % sobre el valor base. */
    public static final double INCREMENTO_POR_PUNTO = 0.10;

    // Vida y daño
    public static final int VIDA_BASE = 100;
    public static final int DANO_PATADA_BASE = 10;
    /** Tiempo que el jugador queda aturdido al quedarse sin vida. */
    public static final double TIEMPO_ATURDIMIENTO = 3.0;
    /** Tolerancia extra para golpear al rival con la bota. */
    public static final double TOLERANCIA_GOLPE_PATADA = 10;

    // Efectos de los tiros de poder
    /** Velocidad horizontal con la que el Aura Azul empuja al rival. */
    public static final double IMPULSO_EMPUJE = 900;
    /** Componente vertical del empuje. */
    public static final double IMPULSO_EMPUJE_VERTICAL = 300;
    /** Impulso vertical del Tornado para lanzar al rival al aire. */
    public static final double IMPULSO_INCONSCIENTE = 650;

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

    private ParametrosJuego() {
    }
}
