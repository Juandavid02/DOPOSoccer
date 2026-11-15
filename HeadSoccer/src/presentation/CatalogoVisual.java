package presentation;

import java.util.List;

/**
 * Catálogo de balones y escenarios disponibles para una partida.
 */
final class CatalogoVisual {

    private static final String RUTA_BALONES = "resources/images/Balones/";
    private static final String RUTA_ESCENARIOS = "resources/images/Escenarios/";

    private static final List<Opcion> BALONES = List.of(
            new Opcion("Mario", RUTA_BALONES + "MarioBalon.png"),
            new Opcion("BTS", RUTA_BALONES + "BTSBalon.png"),
            new Opcion("Betty", RUTA_BALONES + "BettyBalon.png"));

    private static final List<Opcion> ESCENARIOS = List.of(
            new Opcion("Reino Champiñón", RUTA_ESCENARIOS + "EscennarioReinoChampiñon.jpg"),
            new Opcion("Fondo de Bikini", RUTA_ESCENARIOS + "EscennarioFondoDeBikini.jpg"),
            new Opcion("Tenkaichi Budokai", RUTA_ESCENARIOS + "EscennarioTenkaichiBudokai.jpg"),
            new Opcion("Vecindad del Chavo", RUTA_ESCENARIOS + "EscennarioVecindadDelCHavo.jpg"));

    /**
     * Constructor privado para evitar instancias del catálogo.
     */
    private CatalogoVisual() {
    }

    /**
     * Devuelve los balones disponibles.
     *
     * @return lista de balones
     */
    static List<Opcion> getBalones() {
        return BALONES;
    }

    /**
     * Devuelve los escenarios disponibles.
     *
     * @return lista de escenarios
     */
    static List<Opcion> getEscenarios() {
        return ESCENARIOS;
    }

    static final class Opcion {

        private final String nombre;
        private final String ruta;

        /**
         * Construye una opción con su nombre y su ruta.
         *
         * @param nombre nombre visible de la opción
         * @param ruta ruta de la imagen
         */
        private Opcion(String nombre, String ruta) {
            this.nombre = nombre;
            this.ruta = ruta;
        }

        /**
         * Devuelve el nombre de la opción.
         *
         * @return el nombre
         */
        String getNombre() {
            return nombre;
        }

        /**
         * Devuelve la ruta de la imagen de la opción.
         *
         * @return la ruta
         */
        String getRuta() {
            return ruta;
        }
    }
}
