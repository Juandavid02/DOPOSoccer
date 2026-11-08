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
            new Opcion("Reino Champiñón", RUTA_ESCENARIOS + "EscennarioReinoChampiñon.png"),
            new Opcion("Fondo de Bikini", RUTA_ESCENARIOS + "EscennarioFondoDeBikini.png"),
            new Opcion("Tenkaichi Budokai", RUTA_ESCENARIOS + "EscennarioTenkaichiBudokai.jpg"),
            new Opcion("Vecindad del Chavo", RUTA_ESCENARIOS + "EscennarioVecindadDelCHavo.jpg"));

    private CatalogoVisual() {
    }

    static List<Opcion> getBalones() {
        return BALONES;
    }

    static List<Opcion> getEscenarios() {
        return ESCENARIOS;
    }

    static final class Opcion {

        private final String nombre;
        private final String ruta;

        private Opcion(String nombre, String ruta) {
            this.nombre = nombre;
            this.ruta = ruta;
        }

        String getNombre() {
            return nombre;
        }

        String getRuta() {
            return ruta;
        }
    }
}
