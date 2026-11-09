package domain;

public class ReglaFactory {
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