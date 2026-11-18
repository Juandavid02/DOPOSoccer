package domain;

/**
 * Efecto Electrocutado: el jugador se mueve a la mitad de velocidad y no
 * puede saltar mientras dure.
 */
public class EfectoElectrocutado extends Efecto {

    private static final double FACTOR_VELOCIDAD = 0.5;

    /**
     * Crea el efecto Electrocutado.
     *
     * @param duracion duración del efecto en segundos
     */
    public EfectoElectrocutado(double duracion) {
        super(TipoEfecto.ELECTROCUTADO, duracion);
    }

    @Override
    public boolean bloqueaSalto() {
        return true;
    }

    @Override
    public double getMultiplicadorVelocidad() {
        return FACTOR_VELOCIDAD;
    }
}
