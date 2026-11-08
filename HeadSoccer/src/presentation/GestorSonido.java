package presentation;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

/**
 * Estado global del sonido del juego. Por ahora solo maneja si está
 * silenciado; más adelante aquí se agregará la reproducción de música y
 * efectos con Java Sound.
 */
public class GestorSonido {

    private boolean silenciado;
    private final List<Consumer<Boolean>> oyentes = new ArrayList<>();
    private final List<Clip> clipsActivos = new CopyOnWriteArrayList<>();

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
            detenerTodos();
        }

        for (Consumer<Boolean> oyente : oyentes) {
            oyente.accept(silenciado);
        }
    }

    /**
     * Detiene y cierra todos los audios que estén sonando.
     */
    public void detenerTodos() {
        List<Clip> clips = new ArrayList<>(clipsActivos);
        clipsActivos.clear();

        for (Clip clip : clips) {
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
     * Registra un componente que debe actualizarse cuando cambie el sonido.
     *
     * @param oyente recibe true cuando el sonido está silenciado
     */
    public void agregarOyente(Consumer<Boolean> oyente) {
        oyentes.add(oyente);
        oyente.accept(silenciado);
    }

    /**
     * Reproduce un efecto de sonido corto. Si el sonido está silenciado o el
     * archivo no existe, no hace nada.
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

        // Solo un efecto a la vez: si otro personaje estaba sonando, se corta.
        detenerTodos();

        try {
            AudioInputStream audio = AudioSystem.getAudioInputStream(archivo);
            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            clipsActivos.add(clip);
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    clipsActivos.remove(clip);
                    try {
                        clip.close();
                    } catch (Exception e) {
                        // El clip pudo haberse cerrado antes.
                    }
                }
            });
            clip.start();
        } catch (Exception e) {
            System.err.println("No se pudo reproducir el audio: " + ruta
                    + " (" + e.getMessage() + ")");
        }
    }
}
