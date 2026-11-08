package domain;

public class Estadisticas {
    private int speed;
    private int jump;
    private int kick;
    private int survival;

    public Estadisticas(int speed, int jump, int kick, int survival) {
        this.speed = validar("speed", speed);
        this.jump = validar("jump", jump);
        this.kick = validar("kick", kick);
        this.survival = validar("survival", survival);
    }

    private static int validar(String nombre, int valor) {
        if (valor < ParametrosJuego.MIN_ESTADISTICA || valor > ParametrosJuego.MAX_ESTADISTICA) {
            throw new IllegalArgumentException(nombre + " debe estar entre " + ParametrosJuego.MIN_ESTADISTICA + " y "+ ParametrosJuego.MAX_ESTADISTICA);
        }
        return valor;
    }

    public int getSpeed() {
        return speed;
    }

    public int getJump() {
        return jump;
    }

    public int getKick() {
        return kick;
    }

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


    public double getMultiplicadorKick() {
        return 1 + ParametrosJuego.INCREMENTO_POR_PUNTO * (kick - 1);
    }

    public double getVidaMax() {
        return ParametrosJuego.VIDA_BASE * (1 + ParametrosJuego.INCREMENTO_POR_PUNTO * (survival - 1));
    }

    public double getDanoPatada() {
        return ParametrosJuego.DANO_PATADA_BASE * getMultiplicadorKick();
    }
}
