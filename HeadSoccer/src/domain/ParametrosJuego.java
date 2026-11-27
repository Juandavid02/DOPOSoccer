package domain;

/**
 * Constantes ajustables de DOPOSoccer.
 *
 * Todos los números del juego (física, vida, daño, barra de poder y puntos)
 * viven aquí para no quedar hardcodeados en la lógica. Las dimensiones de la
 * cancha ya no están aquí: las define Resolucion.
 */
public final class ParametrosJuego {

    // Física
    public static final double GRAVEDAD = 1200;
    public static final double REBOTE = 0.70;
    public static final double FRICCION = 0.995;
    public static final double PERDIDA_REBOTE = 0.9;

    // Balón
    public static final int RADIO_BALON = 12;
    public static final double RADIO_BALON_COLISION = 30;
    public static final double RESTITUCION_COLISION = 0.6;

    // Arcos
    public static final int ANCHO_ARCO = 120;
    public static final int ALTO_ARCO = 220;
    public static final double ALTO_RAMPA_ARCO = 45;
    // Si el balón cae con menos velocidad que esta, no rebota y se resbala.
    public static final double UMBRAL_REBOTE_RAMPA = 90;

    // Jugador
    public static final double RADIO_JUGADOR = 20;
    /** Velocidad base de desplazamiento; las estadísticas la modifican. */
    public static final double VELOCIDAD_BASE_JUGADOR = 260;
    public static final double RADIO_CABEZA_JUGADOR = 45;
    public static final double RADIO_BOTA_JUGADOR = 18;
    public static final double OFFSET_CABEZA_JUGADOR = -55;
    public static final double OFFSET_BOTA_X_JUGADOR = 10;
    public static final double OFFSET_BOTA_Y_JUGADOR = 8;

    // Golpe
    /** Distancia a la que se puede patear el balón: pie, balón y tolerancia visual. */
    public static final double ALCANCE_PATADA = RADIO_BOTA_JUGADOR + RADIO_BALON_COLISION + 17;
    public static final double IMPULSO_SALTO_BASE = 600;
    public static final double POTENCIA_PATADA_BASE = 650;
    /** Componente hacia arriba de la patada para que el balón haga curva. */
    public static final double POTENCIA_LEVANTE_PATADA = 520;

    // Estadísticas
    public static final int MIN_ESTADISTICA = 1;
    public static final int MAX_ESTADISTICA = 10;
    /** Valor de cada estadística en un perfil nuevo y en la IA de PvsM. */
    public static final int ESTADISTICA_INICIAL = 1;
    /** Cada punto de estadística suma un 10 % sobre el valor base. */
    public static final double INCREMENTO_POR_PUNTO = 0.10;

    // Vida y daño
    public static final int VIDA_BASE = 100;
    public static final int DANO_PATADA_BASE = 10;
    public static final double TIEMPO_ATURDIMIENTO = 3.0;
    /** Tolerancia extra para golpear al rival con la bota. */
    public static final double TOLERANCIA_GOLPE_PATADA = 10;

    // Efectos
    /** Velocidad horizontal con la que el Aura Azul empuja al rival. */
    public static final double IMPULSO_EMPUJE = 900;
    /** Componente vertical del empuje. */
    public static final double IMPULSO_EMPUJE_VERTICAL = 300;
    /** Impulso vertical del Tornado para lanzar al rival al aire. */
    public static final double IMPULSO_INCONSCIENTE = 650;

    // Ilusión: siete balones falsos que distraen al rival.
    public static final int CANTIDAD_BALONES_ILUSION = 7;

    // Barra de poder
    public static final double CAPACIDAD_BARRA_PODER = 100;
    /** La barra se llena en 10 s: 100 / 10 = 10 unidades por segundo. */
    public static final double RECARGA_BARRA_PODER = 10;
    /** Al recibir un gol, la barra sube inmediatamente un cuarto. */
    public static final double FRACCION_BARRA_POR_GOL = 0.25;

    // Puntos y mejoras
    public static final int PUNTOS_GANADOR = 100;
    public static final int PUNTOS_PERDEDOR = 25;
    public static final int COSTO_MEJORA_INICIAL = 500;
    /** El precio de cada punto de mejora se duplica: 500, 1000, 2000... */
    public static final int FACTOR_COSTO_MEJORA = 2;

    // Torneo
    /** Puntos que cuesta inscribirse en un torneo. */
    public static final int COSTO_TORNEO = 100;
    /** Premio en puntos para el campeón del torneo. */
    public static final int PREMIO_CAMPEON_TORNEO = 500;
    /** Premio en puntos para el subcampeón del torneo. */
    public static final int PREMIO_SUBCAMPEON_TORNEO = 150;
    /** Estadísticas base de las IAs del torneo. */
    public static final int ESTADISTICAS_IA_TORPE = 4;
    public static final int ESTADISTICAS_IA_OFENSIVA = 6;
    public static final int ESTADISTICAS_IA_DEFENSIVA = 6;
    public static final int ESTADISTICAS_IA_EXPERTA = 7;
    /** Mejora que recibe una IA por avanzar de ronda. */
    public static final int MEJORA_IA_TORNEO = 1;

    // Juego
    public static final int TIEMPO_PARTIDO = 60;

    /**
     * Constructor privado para evitar instancias de la clase.
     */
    private ParametrosJuego() {
    }
}