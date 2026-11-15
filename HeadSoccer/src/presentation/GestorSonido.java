package presentation;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
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
    private int volumen = 100;
    private final List<Consumer<Boolean>> oyentes = new ArrayList<>();

    private Clip musicaFondo;
    private String rutaMusicaFondo;
    private Clip efectoActual;
    private boolean musicaPausada;

    /**
     * Volumen general del juego: afecta música y efectos.
     *
     * @return volumen entre 0 y 100
     */
    public int getVolumen() {
        return volumen;
    }

    /**
     * Cambia el volumen general y lo aplica al audio que esté sonando.
     *
     * @param volumen nuevo volumen entre 0 y 100
     */
    public void setVolumen(int volumen) {
        this.volumen = Math.max(0, Math.min(100, volumen));
        aplicarVolumen(musicaFondo);
        aplicarVolumen(efectoActual);
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
     * Deja de notificar a un componente que ya no está en pantalla.
     *
     * @param oyente oyente registrado previamente
     */
    public void quitarOyente(Consumer<Boolean> oyente) {
        oyentes.remove(oyente);
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
            System.err.println("No se encontró la música: " + ruta);
            return;
        }

        try (AudioInputStream audio =
                     AudioSystem.getAudioInputStream(archivo)) {
            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            musicaPausada = false;
            aplicarVolumen(clip);

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
            e.printStackTrace();
        }
    }

    /**
     * Cambia la música de fondo por otra pista, deteniendo la anterior.
     * Si la pista solicitada ya es la actual, simplemente se reanuda.
     *
     * @param ruta la ruta del nuevo archivo de música
     */
    public void cambiarMusicaFondo(String ruta) {
        if (ruta == null) {
            return;
        }

        if (ruta.equals(rutaMusicaFondo) && musicaFondo != null) {
            reanudarMusicaFondo();
            return;
        }

        detenerMusicaFondo();
        rutaMusicaFondo = ruta;

        if (silenciado) {
            return;
        }

        iniciarMusicaFondo(ruta);
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
     * Detiene y libera la música de fondo actual.
     */
    private void detenerMusicaFondo() {
        Clip clip = musicaFondo;
        musicaFondo = null;
        musicaPausada = false;

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
            System.err.println("No se encontró el audio: " + ruta);
            return;
        }

        detenerEfectoActual(false);
        pausarMusicaFondo();

        try (AudioInputStream audio =
                     AudioSystem.getAudioInputStream(archivo)) {
            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            efectoActual = clip;
            aplicarVolumen(clip);

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
            e.printStackTrace();
        }
    }

    /**
     * Reproduce un sonido corto del partido (gol, golpe, pito) sin detener
     * la música de fondo. Varios sonidos cortos pueden sonar a la vez.
     *
     * @param ruta la ruta del archivo de audio
     */
    public void reproducirSonidoCorto(String ruta) {
        if (silenciado) {
            return;
        }

        File archivo = new File(ruta);
        if (!archivo.isFile()) {
            System.err.println("No se encontró el sonido: " + ruta);
            return;
        }

        try (AudioInputStream audio = AudioSystem.getAudioInputStream(archivo)) {
            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            aplicarVolumen(clip);

            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    try {
                        clip.close();
                    } catch (Exception e) {
                        // El clip pudo haberse cerrado antes.
                    }
                }
            });

            clip.start();
        } catch (Exception e) {
            System.err.println("No se pudo reproducir el sonido: " + ruta
                    + " (" + e.getMessage() + ")");
            e.printStackTrace();
        }
    }

    /**
     * Detiene y libera el efecto actual, reanudando la música si se pide.
     *
     * @param reanudarMusica true para reanudar la música de fondo
     */
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

    /**
     * Aplica el volumen general a un clip. Si el sistema no soporta control
     * de ganancia, el clip se reproduce con su volumen normal.
     *
     * @param clip clip al que se aplica el volumen
     */
    private void aplicarVolumen(Clip clip) {
        if (clip == null) {
            return;
        }

        try {
            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                FloatControl control = (FloatControl) clip.getControl(
                        FloatControl.Type.MASTER_GAIN);

                float decibelios;
                if (volumen <= 0) {
                    decibelios = control.getMinimum();
                } else if (volumen >= 100) {
                    decibelios = 0f;
                } else {
                    decibelios = (float) (20.0 * Math.log10(volumen / 100.0));
                }

                decibelios = Math.max(control.getMinimum(),
                        Math.min(control.getMaximum(), decibelios));
                control.setValue(decibelios);
            } else if (clip.isControlSupported(FloatControl.Type.VOLUME)) {
                FloatControl control = (FloatControl) clip.getControl(
                        FloatControl.Type.VOLUME);
                control.setValue(volumen / 100f);
            }
        } catch (Exception e) {
            // Algunos dispositivos no permiten cambiar el volumen.
        }
    }
}
