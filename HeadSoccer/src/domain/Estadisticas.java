package domain;

import java.io.Serializable;

/**
 * Estadísticas físicas de un jugador: velocidad, salto, patada y resistencia.
 */
public class Estadisticas implements Serializable {

    private static final long serialVersionUID = 1L;

    private int speed;
    private int jump;
    private int kick;
    private int survival;

    /**
     * Crea las estadísticas con los valores indicados.
     *
     * @param speed valor de velocidad
     * @param jump valor de salto
     * @param kick valor de patada
     * @param survival valor de resistencia
     */
    public Estadisticas(int speed, int jump, int kick, int survival) {
        this.speed = validar("speed", speed);
        this.jump = validar("jump", jump);
        this.kick = validar("kick", kick);
        this.survival = validar("survival", survival);
    }

    /**
     * Valida que un valor esté dentro del rango permitido.
     *
     * @param nombre nombre de la estadística
     * @param valor valor que se valida
     * @return el valor validado
     */
    private static int validar(String nombre, int valor) {
        if (valor < ParametrosJuego.MIN_ESTADISTICA || valor > ParametrosJuego.MAX_ESTADISTICA) {
            throw new IllegalArgumentException(nombre + " debe estar entre " + ParametrosJuego.MIN_ESTADISTICA + " y " + ParametrosJuego.MAX_ESTADISTICA);
        }
        return valor;
    }

    /**
     * Obtiene la estadística de velocidad.
     *
     * @return valor de velocidad
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * Obtiene la estadística de salto.
     *
     * @return valor de salto
     */
    public int getJump() {
        return jump;
    }

    /**
     * Obtiene la estadística de patada.
     *
     * @return valor de patada
     */
    public int getKick() {
        return kick;
    }

    /**
     * Obtiene la estadística de resistencia.
     *
     * @return valor de resistencia
     */
    public int getSurvival() {
        return survival;
    }

    /**
     * Devuelve una copia independiente de estas estadísticas.
     *
     * @return copia con los mismos valores
     */
    public Estadisticas copiar() {
        return new Estadisticas(speed, jump, kick, survival);
    }

    /**
     * Copia los valores de otras estadísticas en esta instancia.
     *
     * @param otras estadísticas de origen
     */
    public void copiarDesde(Estadisticas otras) {
        if (otras == null) {
            throw new IllegalArgumentException("Las estadísticas de origen no pueden ser nulas");
        }
        this.speed = otras.speed;
        this.jump = otras.jump;
        this.kick = otras.kick;
        this.survival = otras.survival;
    }

    /**
     * Vuelve a los valores básicos de un perfil sin mejoras.
     */
    public void restablecer() {
        int inicial = ParametrosJuego.ESTADISTICA_INICIAL;
        this.speed = inicial;
        this.jump = inicial;
        this.kick = inicial;
        this.survival = inicial;
    }

    /**
     * Obtiene el multiplicador de patada según la estadística.
     *
     * @return multiplicador de patada
     */
    public double getMultiplicadorKick() {
        return 1 + ParametrosJuego.INCREMENTO_POR_PUNTO * (kick - 1);
    }

    /**
     * Factor que aplica la estadística de velocidad sobre la velocidad base.
     *
     * @return multiplicador de velocidad
     */
    public double getFactorVelocidad() {
        return 1 + ParametrosJuego.INCREMENTO_POR_PUNTO * (speed - 1);
    }

    /**
     * Obtiene la vida máxima según la estadística de resistencia.
     *
     * @return vida máxima
     */
    public double getVidaMax() {
        return ParametrosJuego.VIDA_BASE * (1 + ParametrosJuego.INCREMENTO_POR_PUNTO * (survival - 1));
    }

    /**
     * Obtiene el daño de patada según la estadística.
     *
     * @return daño de patada
     */
    public double getDanoPatada() {
        return ParametrosJuego.DANO_PATADA_BASE * getMultiplicadorKick();
    }
}
