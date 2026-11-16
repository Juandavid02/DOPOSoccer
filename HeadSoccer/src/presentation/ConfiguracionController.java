package presentation;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Controlador de la pantalla de configuración general.
 */
public class ConfiguracionController {

    private final DOPOSoccerGUI gui;
    private final ConfiguracionView view;

    private ConfiguracionControles borrador;
    private int volumenAnterior;

    /**
     * Crea el controlador de la configuración general.
     *
     * @param gui ventana principal del juego
     * @param view vista de configuración
     */
    public ConfiguracionController(DOPOSoccerGUI gui, ConfiguracionView view) {
        this.gui = gui;
        this.view = view;
        prepareController();
    }

    /**
     * Conecta los botones de la vista con el controlador.
     */
    private void prepareController() {
        view.getBotonAtras().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                atras();
            }
        });
        view.getBotonGuardar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardar();
            }
        });
        view.getBotonReiniciar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reiniciar();
            }
        });
        view.getBotonSonido().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gui.alternarSonido();
            }
        });

        view.setAlCambiarVolumen(gui::setVolumen);

        prepararBotones(1, view.getBotonesJugador1());
        prepararBotones(2, view.getBotonesJugador2());
    }

    /**
     * Asigna el cambio de tecla a los botones de un jugador.
     *
     * @param jugador número del jugador: 1 o 2
     * @param botones botones de tecla del jugador
     */
    private void prepararBotones(int jugador,
                                 java.util.Map<ConfiguracionControles.Accion,
                                         ConfiguracionView.BotonTecla> botones) {
        for (ConfiguracionControles.Accion accion : ConfiguracionControles.Accion.values()) {
            ConfiguracionView.BotonTecla boton = botones.get(accion);
            boton.setAsignador(codigo -> {
                if (borrador == null) {
                    return false;
                }
                if (!borrador.asignar(jugador, accion, codigo)) {
                    JOptionPane.showMessageDialog(
                            view,
                            "Esa tecla ya está asignada a otra acción.",
                            "Configuración",
                            JOptionPane.WARNING_MESSAGE);
                    return false;
                }
                return true;
            });
        }
    }

    /**
     * Prepara la pantalla con los valores que están activos actualmente.
     */
    public void abrir() {
        volumenAnterior = gui.getVolumen();
        borrador = new ConfiguracionControles(gui.getConfiguracionControles());
        view.setTeclas(borrador);
        view.setVolumen(volumenAnterior);
    }

    /**
     * Guarda las teclas y el volumen elegidos.
     */
    private void guardar() {
        view.cancelarCaptura();
        gui.setConfiguracionControles(borrador);
        gui.setVolumen(view.getVolumen());
        gui.volverDeConfiguracion();
    }

    /**
     * Cancela los cambios y vuelve a la pantalla anterior.
     */
    private void atras() {
        view.cancelarCaptura();
        gui.setVolumen(volumenAnterior);
        gui.volverDeConfiguracion();
    }

    /**
     * Restablece el volumen y las teclas por defecto.
     */
    private void reiniciar() {
        int opcion = JOptionPane.showConfirmDialog(
                view,
                "¿Restablecer el volumen y las teclas por defecto?",
                "Configuración",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (opcion != JOptionPane.YES_OPTION) {
            return;
        }

        borrador.restablecer();
        view.setTeclas(borrador);
        view.setVolumen(100);
    }
}
