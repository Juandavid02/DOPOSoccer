package domain;

/**
 * Estadísticas que se pueden mejorar con puntos.
 */
public enum TipoEstadistica {
    VELOCIDAD("Velocidad"),
    SALTO("Salto"),
    PATADA("Patada"),
    SUPERVIVENCIA("Supervivencia");

    private final String etiqueta;

    /**
     * Crea la estadística con su etiqueta.
     *
     * @param etiqueta nombre legible de la estadística
     */
    TipoEstadistica(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    /**
     * Obtiene la etiqueta de la estadística.
     *
     * @return nombre legible de la estadística
     */
    public String getEtiqueta() {
        return etiqueta;
    }

    /**
     * Devuelve el valor actual de esta estadística.
     *
     * @param estadisticas estadísticas del jugador
     * @return valor entre 1 y 10
     */
    public int getValor(Estadisticas estadisticas) {
        switch (this) {
            case VELOCIDAD:
                return estadisticas.getSpeed();
            case SALTO:
                return estadisticas.getJump();
            case PATADA:
                return estadisticas.getKick();
            case SUPERVIVENCIA:
                return estadisticas.getSurvival();
            default:
                throw new IllegalStateException("Estadística desconocida: " + this);
        }
    }
}
