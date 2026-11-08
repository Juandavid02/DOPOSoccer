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
    private static final String RUTA_AUDIO = "resources/audio/";

    private static final List<Opcion> OPCIONES = crear();

    private CatalogoSeleccion() {
    }

    static List<Opcion> getOpciones() {
        return OPCIONES;
    }

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
                RUTA_PERSONAJES + "ChavoPatadaDerecha.png",
                RUTA_PERSONAJES + "ChavoPatadaIzquierdo.png",
                RUTA_PERSONAJES + "TituloChavo.png",
                RUTA_AUDIO + "ChavoSelecionCaja.wav",
                RUTA_BANDERAS + "BanderaMexicoSeleccion.png"));

        return opciones;
    }

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

        Personaje getPersonaje() {
            return personaje;
        }

        String getCaja() {
            return caja;
        }

        String getDerecha() {
            return derecha;
        }

        String getIzquierda() {
            return izquierda;
        }

        String getPatadaDerecha() {
            return patadaDerecha;
        }

        String getPatadaIzquierda() {
            return patadaIzquierda;
        }

        String getTitulo() {
            return titulo;
        }

        String getAudio() {
            return audio;
        }

        String getBandera() {
            return bandera;
        }

        boolean es(Personaje otro) {
            return personaje.getNombre().equalsIgnoreCase(otro.getNombre());
        }
    }
}
