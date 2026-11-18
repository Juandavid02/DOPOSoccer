package domain;

/**
 * Efectos que puede aplicar un tiro de poder al rival. Cada tipo sabe
 * crear su efecto concreto.
 */
public enum TipoEfecto {
    EMPUJE {
        @Override
        public Efecto crear(double duracion) {
            return new EfectoEmpuje(duracion);
        }
    },
    ELECTROCUTADO {
        @Override
        public Efecto crear(double duracion) {
            return new EfectoElectrocutado(duracion);
        }
    },
    INCONSCIENTE {
        @Override
        public Efecto crear(double duracion) {
            return new EfectoInconsciente(duracion);
        }
    },
    CONFUNDIDO {
        @Override
        public Efecto crear(double duracion) {
            return new EfectoConfundido(duracion);
        }
    },
    CONTROLES_INVERTIDOS {
        @Override
        public Efecto crear(double duracion) {
            return new EfectoControlesInvertidos(duracion);
        }
    },
    BARRIL {
        @Override
        public Efecto crear(double duracion) {
            return new EfectoBarril(duracion);
        }
    };

    /**
     * Crea el efecto que corresponde a este tipo.
     *
     * @param duracion duración del efecto en segundos
     * @return efecto nuevo de este tipo
     */
    public abstract Efecto crear(double duracion);
}
