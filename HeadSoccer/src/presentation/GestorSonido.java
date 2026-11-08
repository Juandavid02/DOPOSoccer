package presentation;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Estado global del sonido del juego: música de fondo y efectos.
 *
 * La música suena en bucle. Cuando se reproduce el audio de un personaje,
 * la música se pausa y se reanuda al terminar.
 */
public class GestorSonido {

    private boolean silenciado;
    private final List<Consumer<Boolean>> oyentes = new ArrayList<>();

    private Clip musicaFondo;
    private String rutaMusicaFondo;
    private Clip efectoActual;
    private boolean musicaPausada;

    public boolean isSilenciado() {
        return silenciado;
    }

    /**
     * Cambia entre sonido activo y silenciado, y avisa a todas las pantallas.
     * Al silenciar también detiene el audio que esté sonando.
     */
    public void alternarSilencio() {
        silenciado = !silenciado;

        if (silenciado) {
            detenerEfectoActual(false);
            pausarMusicaFondo();
        } else {
            reanudarMusicaFondo();
        }

        for (Consumer<Boolean> oyente : oyentes) {
            oyente.accept(silenciado);
        }
    }

    /**
     * Registra un componente que debe actualizarse cuando cambie el sonido.
     *
     * @param oyente recibe true cuando el sonido está silenciado
     */
    public void agregarOyente(Consumer<Boolean> oyente) {
        oyentes.add(oyente);
        oyente.accept(silenciado);
    }

    /**
     * Inicia la música de fondo en bucle.
     *
     * @param ruta la ruta del archivo de música
     */
    public void iniciarMusicaFondo(String ruta) {
        rutaMusicaFondo = ruta;

        if (silenciado) {
            return;
        }

        if (musicaFondo != null) {
            musicaPausada = false;
            if (!musicaFondo.isRunning()) {
                if (musicaFondo.getFramePosition() >= musicaFondo.getFrameLength()) {
                    musicaFondo.setFramePosition(0);
                }
                musicaFondo.start();
            }
            return;
        }

        File archivo = new File(ruta);
        if (!archivo.isFile()) {
            return;
        }

        try {
            AudioInputStream audio = AudioSystem.getAudioInputStream(archivo);
            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            musicaPausada = false;

            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP
                        && clip == musicaFondo
                        && !musicaPausada
                        && !silenciado) {
                    // Reinicia el bucle si el Clip dejó de reproducir al final.
                    clip.setFramePosition(0);
                    clip.start();
                }
            });

            clip.setFramePosition(0);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            if (!clip.isRunning()) {
                clip.start();
            }
            musicaFondo = clip;
        } catch (Exception e) {
            System.err.println("No se pudo reproducir la música: " + ruta
                    + " (" + e.getMessage() + ")");
        }
    }

    /**
     * Pausa la música de fondo sin perder la posición actual.
     */
    public void pausarMusicaFondo() {
        musicaPausada = true;
        if (musicaFondo != null && musicaFondo.isRunning()) {
            musicaFondo.stop();
        }
    }

    /**
     * Reanuda la música de fondo desde donde se quedó.
     */
    public void reanudarMusicaFondo() {
        if (silenciado) {
            return;
        }

        if (musicaFondo == null) {
            if (rutaMusicaFondo != null) {
                iniciarMusicaFondo(rutaMusicaFondo);
            }
            return;
        }

        musicaPausada = false;
        if (!musicaFondo.isRunning()) {
            if (musicaFondo.getFramePosition() >= musicaFondo.getFrameLength()) {
                musicaFondo.setFramePosition(0);
            }
            musicaFondo.start();
        }
    }

    /**
     * Detiene el efecto actual y reanuda la música de fondo.
     */
    public void detenerEfectos() {
        detenerEfectoActual(true);
    }

    /**
     * Reproduce el audio de un personaje. Pausa la música de fondo mientras
     * dura el efecto.
     *
     * @param ruta la ruta del archivo de audio
     */
    public void reproducirEfecto(String ruta) {
        if (silenciado) {
            return;
        }

        File archivo = new File(ruta);
        if (!archivo.isFile()) {
            return;
        }

        detenerEfectoActual(false);
        pausarMusicaFondo();

        try {
            AudioInputStream audio = AudioSystem.getAudioInputStream(archivo);
            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            efectoActual = clip;

            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP && clip == efectoActual) {
                    efectoActual = null;
                    try {
                        clip.close();
                    } catch (Exception e) {
                        // El clip pudo haberse cerrado antes.
                    }
                    reanudarMusicaFondo();
                }
            });

            clip.start();
        } catch (Exception e) {
            reanudarMusicaFondo();
            System.err.println("No se pudo reproducir el audio: " + ruta
                    + " (" + e.getMessage() + ")");
        }
    }

    private void detenerEfectoActual(boolean reanudarMusica) {
        Clip clip = efectoActual;
        efectoActual = null;

        if (clip != null) {
            try {
                clip.stop();
            } catch (Exception e) {
                // El clip pudo haberse cerrado antes.
            }
            try {
                clip.close();
            } catch (Exception e) {
                // El clip pudo haberse cerrado antes.
            }
        }

        if (reanudarMusica) {
            reanudarMusicaFondo();
        }
    }
}
