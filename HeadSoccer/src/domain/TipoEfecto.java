package domain;

/**
 * Clasificación de los efectos que puede aplicar un tiro de poder.
 *
 * La lógica de cada efecto vive en sus subclases de {@link Efecto}. Este
 * enum se conserva únicamente como clave para elegir los recursos gráficos
 * y para identificar el efecto en las pruebas; el dominio no decide
 * comportamiento a partir de él.
 */
public enum TipoEfecto {
    EMPUJE,
    ELECTROCUTADO,
    INCONSCIENTE,
    CONFUNDIDO,
    CONTROLES_INVERTIDOS,
    BARRIL
}
