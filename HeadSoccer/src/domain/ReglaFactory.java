package domain;

public class ReglaFactory {
    /**
     * Crea la regla de partido correspondiente a la configuración.
     *
     * @param configuracion configuración del partido
     * @return regla de partido según el tipo configurado
     * @throws IllegalArgumentException si la configuración es nula o de tipo desconocido
     */
    public static ReglaPartido crearRegla(ConfiguracionPartido configuracion) {
        if (configuracion == null) {
            throw new IllegalArgumentException("La configuración no puede ser nula");
        }
        if (configuracion.getTipo() == TipoPartido.TIEMPO) {
            return new ReglaTiempo();
        } else if (configuracion.getTipo() == TipoPartido.GOLES) {
            return new ReglaGoles(configuracion.getGoles());
        } else {
            throw new IllegalArgumentException("Tipo de partido desconocido: " + configuracion.getTipo());
        }

    }

}