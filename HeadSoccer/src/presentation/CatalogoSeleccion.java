package presentation;

import domain.CatalogoPersonajes;
import domain.Personaje;

import java.util.ArrayList;
import java.util.List;

/**
 * Relaciona los personajes del dominio con sus imágenes y audios de
 * selección.
 */
final class CatalogoSeleccion {

    private static final String RUTA_PERSONAJES = "resources/images/personajes/";
    private static final String RUTA_BANDERAS = "resources/images/Ventanas/";
    private static final String RUTA_AUDIO = "resources/audio/SonidosPersonajes/";

    private static final List<Opcion> OPCIONES = crear();

    /**
     * Constructor privado de la clase de utilidades.
     */
    private CatalogoSeleccion() {
    }

    /**
     * Devuelve las opciones de personajes disponibles.
     *
     * @return la lista de opciones
     */
    static List<Opcion> getOpciones() {
        return OPCIONES;
    }

    /**
     * Busca la opción de un personaje.
     *
     * @param personaje personaje que se busca
     * @return la opción encontrada o null si no existe
     */
    static Opcion buscar(Personaje personaje) {
        if (personaje == null) {
            return null;
        }
        for (Opcion opcion : OPCIONES) {
            if (opcion.es(personaje)) {
                return opcion;
            }
        }
        return null;
    }

    /**
     * Crea la lista de opciones con las rutas de cada personaje.
     *
     * @return la lista de opciones creada
     */
    private static List<Opcion> crear() {
        List<Opcion> opciones = new ArrayList<>();

        opciones.add(new Opcion(
                buscarPersonaje("Betty la fea"),
                RUTA_PERSONAJES + "BettyCajaDeSeleccion.png",
                RUTA_PERSONAJES + "BettyQuietoDerecha.png",
                RUTA_PERSONAJES + "BettyQuietoIzquierdo.png",
                RUTA_PERSONAJES + "BettyPatadaDerecha.png",
                RUTA_PERSONAJES + "BettyPatadaIzquierdo.png",
                RUTA_PERSONAJES + "TituloBetty.png",
                RUTA_AUDIO + "BettySelecionCaja.wav",
                RUTA_BANDERAS + "BanderaColombia.png"));

        opciones.add(new Opcion(
                buscarPersonaje("Bob Esponja"),
                RUTA_PERSONAJES + "BobEsponjaCajaDeSeleccion.png",
                RUTA_PERSONAJES + "BobQuietoDerecha.png",
                RUTA_PERSONAJES + "BobQuietpIzquierdo.png",
                RUTA_PERSONAJES + "BobPatadaDerecha.png",
                RUTA_PERSONAJES + "BobPatadaIzquierdo.png",
                RUTA_PERSONAJES + "TituloBobEsponja.png",
                RUTA_AUDIO + "BobSelecionCaja.wav",
                RUTA_BANDERAS + "BanderaEstadosUnidos.png"));

        opciones.add(new Opcion(
                buscarPersonaje("El Chavo del Ocho"),
                RUTA_PERSONAJES + "ChavoCajaDeSeleccion.png",
                RUTA_PERSONAJES + "ChavoQuietoDerecha.png",
                RUTA_PERSONAJES + "ChavoQuietoIzquierda.png",
                RUTA_PERSONAJES + "ChavoPatadaIzquierdo.png",
                RUTA_PERSONAJES + "ChavoPatadaDerecha.png",
                RUTA_PERSONAJES + "TituloChavo.png",
                RUTA_AUDIO + "ChavoSelecionCaja.wav",
                RUTA_BANDERAS + "BanderaMexicoSeleccion.png"));

        opciones.add(new Opcion(
                buscarPersonaje("Jungkook"),
                RUTA_PERSONAJES + "BTSCajadeSeleccion.png",
                RUTA_PERSONAJES + "BtsQuietoDerecha.png",
                RUTA_PERSONAJES + "BtsQuietoIzquierda.png",
                RUTA_PERSONAJES + "BtsPatadaDerecha.png",
                RUTA_PERSONAJES + "BtsPatadaIzquierda.png",
                RUTA_PERSONAJES + "TituloBTS.png",
                RUTA_AUDIO + "BTSSelecionCaja.wav",
                RUTA_BANDERAS + "BanderaCorea.png"));

        opciones.add(new Opcion(
                buscarPersonaje("Goku"),
                RUTA_PERSONAJES + "GokuCajadeSeleccion.png",
                RUTA_PERSONAJES + "GokuQuietoDerecha.png",
                RUTA_PERSONAJES + "GokuQuietoIzquierda.png",
                RUTA_PERSONAJES + "GokuPatadaDerecha.png",
                RUTA_PERSONAJES + "GokuPatadaIzquierda.png",
                RUTA_PERSONAJES + "DragonTtilo.png",
                RUTA_AUDIO + "GokuSelecionCaja.wav",
                RUTA_BANDERAS + "BandejeraJapon.png"));

        opciones.add(new Opcion(
                buscarPersonaje("Mario"),
                RUTA_PERSONAJES + "MarioCajaDeSeleccion.jpg",
                RUTA_PERSONAJES + "MarioQuietoDerecha.png",
                RUTA_PERSONAJES + "MarioPatadaQuietoIzquierda.png",
                RUTA_PERSONAJES + "MarioPatadaDerecha.png",
                RUTA_PERSONAJES + "MarioPatadaIzquierda.png",
                RUTA_PERSONAJES + "TitloMario.png",
                RUTA_AUDIO + "MarioSeleccionCaja.wav",
                RUTA_BANDERAS + "BanderaItalia.png"));

        return opciones;
    }

    /**
     * Busca un personaje por su nombre.
     *
     * @param nombre nombre del personaje
     * @return el personaje encontrado
     */
    private static Personaje buscarPersonaje(String nombre) {
        for (Personaje personaje : CatalogoPersonajes.getPersonajes()) {
            if (personaje.getNombre().equalsIgnoreCase(nombre)) {
                return personaje;
            }
        }
        throw new IllegalStateException("No está en el catálogo el personaje: " + nombre);
    }

    static final class Opcion {

        private final Personaje personaje;
        private final String caja;
        private final String derecha;
        private final String izquierda;
        private final String patadaDerecha;
        private final String patadaIzquierda;
        private final String titulo;
        private final String audio;
        private final String bandera;

        /**
         * Crea una opción con las rutas de un personaje.
         *
         * @param personaje personaje de la opción
         * @param caja imagen de la caja de selección
         * @param derecha imagen quieto mirando a la derecha
         * @param izquierda imagen quieto mirando a la izquierda
         * @param patadaDerecha imagen de patada a la derecha
         * @param patadaIzquierda imagen de patada a la izquierda
         * @param titulo imagen del título
         * @param audio ruta del audio de selección
         * @param bandera ruta de la bandera
         */
        private Opcion(Personaje personaje, String caja, String derecha,
                       String izquierda, String patadaDerecha,
                       String patadaIzquierda, String titulo,
                       String audio, String bandera) {
            this.personaje = personaje;
            this.caja = caja;
            this.derecha = derecha;
            this.izquierda = izquierda;
            this.patadaDerecha = patadaDerecha;
            this.patadaIzquierda = patadaIzquierda;
            this.titulo = titulo;
            this.audio = audio;
            this.bandera = bandera;
        }

        /**
         * Devuelve el personaje de la opción.
         *
         * @return el personaje
         */
        Personaje getPersonaje() {
            return personaje;
        }

        /**
         * Devuelve la imagen de la caja de selección.
         *
         * @return la ruta de la caja
         */
        String getCaja() {
            return caja;
        }

        /**
         * Devuelve la imagen quieto mirando a la derecha.
         *
         * @return la ruta de la imagen
         */
        String getDerecha() {
            return derecha;
        }

        /**
         * Devuelve la imagen quieto mirando a la izquierda.
         *
         * @return la ruta de la imagen
         */
        String getIzquierda() {
            return izquierda;
        }

        /**
         * Devuelve la imagen de patada a la derecha.
         *
         * @return la ruta de la imagen
         */
        String getPatadaDerecha() {
            return patadaDerecha;
        }

        /**
         * Devuelve la imagen de patada a la izquierda.
         *
         * @return la ruta de la imagen
         */
        String getPatadaIzquierda() {
            return patadaIzquierda;
        }

        /**
         * Devuelve la imagen del título del personaje.
         *
         * @return la ruta del título
         */
        String getTitulo() {
            return titulo;
        }

        /**
         * Devuelve la ruta del audio de selección.
         *
         * @return la ruta del audio
         */
        String getAudio() {
            return audio;
        }

        /**
         * Devuelve la ruta de la bandera.
         *
         * @return la ruta de la bandera
         */
        String getBandera() {
            return bandera;
        }

        /**
         * Indica si la opción corresponde a otro personaje.
         *
         * @param otro personaje que se compara
         * @return true si es el mismo personaje
         */
        boolean es(Personaje otro) {
            return personaje.getNombre().equalsIgnoreCase(otro.getNombre());
        }
    }
}
