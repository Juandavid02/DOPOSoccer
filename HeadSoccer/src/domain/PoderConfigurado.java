package domain;

/**
 * Poder configurable del juego.
 *
 * Concentra los datos que comparten todos los poderes (nombre, duración,
 * daño del tiro e impulsos del balón) y crea el efecto que corresponde al
 * tipo configurado. Los poderes del juego se obtienen con los métodos de
 * fábrica de esta clase.
 */
public final class PoderConfigurado implements Poder {

    private final String nombre;
    private final double duracion;
    private final double danoTiro;
    private final double impulsoX;
    private final double impulsoY;
    private final TipoEfecto tipoEfecto;

    /**
     * Crea un poder con sus datos y el efecto que aplica.
     *
     * @param nombre nombre del poder
     * @param duracion duración del poder en segundos
     * @param danoTiro daño que produce el tiro
     * @param impulsoX impulso horizontal del balón
     * @param impulsoY impulso vertical del balón
     * @param tipoEfecto efecto que aplica al rival
     */
    public PoderConfigurado(String nombre, double duracion, double danoTiro,
                            double impulsoX, double impulsoY,
                            TipoEfecto tipoEfecto) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del poder no puede estar vacío");
        }
        if (duracion <= 0) {
            throw new IllegalArgumentException("La duración del poder debe ser positiva");
        }
        if (danoTiro < 0) {
            throw new IllegalArgumentException("El daño del tiro no puede ser negativo");
        }
        if (tipoEfecto == null) {
            throw new IllegalArgumentException("El tipo de efecto no puede ser nulo");
        }
        this.nombre = nombre;
        this.duracion = duracion;
        this.danoTiro = danoTiro;
        this.impulsoX = impulsoX;
        this.impulsoY = impulsoY;
        this.tipoEfecto = tipoEfecto;
    }

    /**
     * Obtiene el nombre del poder.
     *
     * @return nombre del poder
     */
    @Override
    public String getNombre() {
        return nombre;
    }

    /**
     * Obtiene la duración del poder.
     *
     * @return duración en segundos
     */
    @Override
    public double getDuracion() {
        return duracion;
    }

    /**
     * Obtiene el daño del tiro del poder.
     *
     * @param jugador jugador que ejecuta el tiro
     * @return daño del tiro
     */
    @Override
    public double getDanoTiro(Jugador jugador) {
        return danoTiro;
    }

    /**
     * Crea el efecto que aplica el poder al rival.
     *
     * @return efecto nuevo con la duración del poder
     */
    @Override
    public Efecto crearEfecto() {
        switch (tipoEfecto) {
            case EMPUJE:
                return new EfectoEmpuje(duracion);
            case ELECTROCUTADO:
                return new EfectoElectrocutado(duracion);
            case INCONSCIENTE:
                return new EfectoInconsciente(duracion);
            case CONFUNDIDO:
                return new EfectoConfundido(duracion);
            case CONTROLES_INVERTIDOS:
                return new EfectoControlesInvertidos(duracion);
            case BARRIL:
                return new EfectoBarril(duracion);
            default:
                throw new IllegalStateException(
                        "Tipo de efecto desconocido: " + tipoEfecto);
        }
    }

    /**
     * Obtiene el impulso horizontal del balón.
     *
     * @return impulso horizontal
     */
    @Override
    public double getImpulsoX() {
        return impulsoX;
    }

    /**
     * Obtiene el impulso vertical del balón.
     *
     * @return impulso vertical
     */
    @Override
    public double getImpulsoY() {
        return impulsoY;
    }

    /**
     * Poder Aura Azul: empuja al rival hacia su propio arco.
     *
     * @return poder configurado
     */
    public static Poder auraAzul() {
        return new PoderConfigurado("Aura Azul", 1.5, 40.0,
                1350, -100, TipoEfecto.EMPUJE);
    }

    /**
     * Poder Rayo: electrocuta al rival y lo frena.
     *
     * @return poder configurado
     */
    public static Poder rayo() {
        return new PoderConfigurado("Rayo", 3.0, 60.0,
                1450, -120, TipoEfecto.ELECTROCUTADO);
    }

    /**
     * Poder Tornado: deja inconsciente al rival.
     *
     * @return poder configurado
     */
    public static Poder tornado() {
        return new PoderConfigurado("Tornado", 3.0, 40.0,
                1000, -350, TipoEfecto.INCONSCIENTE);
    }

    /**
     * Poder Ilusión: confunde al rival con balones falsos.
     *
     * @return poder configurado
     */
    public static Poder ilusion() {
        return new PoderConfigurado("Ilusión", 2.5, 30.0,
                1200, -180, TipoEfecto.CONFUNDIDO);
    }

    /**
     * Poder Beso de la Fea: invierte los controles del rival.
     *
     * @return poder configurado
     */
    public static Poder besoDeLaFea() {
        return new PoderConfigurado("Beso de la Fea", 2.5, 30.0,
                1250, -150, TipoEfecto.CONTROLES_INVERTIDOS);
    }

    /**
     * Poder Barril: devuelve al rival a sus estadísticas iniciales.
     *
     * @return poder configurado
     */
    public static Poder barril() {
        return new PoderConfigurado("Barril", 5.0, 35.0,
                1050, -220, TipoEfecto.BARRIL);
    }
}
