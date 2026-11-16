package presentation;

import domain.ConfiguracionIA;
import domain.PerfilIA;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Controlador de la pantalla donde se elige el perfil de la máquina y se
 * ajustan sus estadísticas.
 */
public class SeleccionPerfilIAController {

    private final DOPOSoccerGUI gui;
    private final SeleccionPerfilIAView view;

    /**
     * Crea el controlador y registra los listeners de la vista.
     *
     * @param gui ventana principal del juego
     * @param view vista de selección del perfil de la IA
     */
    public SeleccionPerfilIAController(DOPOSoccerGUI gui,
                                       SeleccionPerfilIAView view) {
        this.gui = gui;
        this.view = view;
        prepareController();
    }

    /**
     * Conecta los botones de la vista con las acciones del controlador.
     */
    private void prepareController() {
        view.getBotonReady().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                continuar();
            }
        });
        view.getBotonAtras().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gui.mostrarSeleccionPerfil();
            }
        });
        view.getBotonSonido().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gui.alternarSonido();
            }
        });
    }

    /**
     * Guarda el perfil de la IA elegido y pasa a la configuración del partido.
     */
    private void continuar() {
        PerfilIA perfil = view.getPerfilSeleccionado();
        if (perfil == null) {
            JOptionPane.showMessageDialog(
                    view,
                    "Selecciona el tipo de IA para continuar.",
                    "Perfil de la IA",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        gui.setConfiguracionIA(new ConfiguracionIA(perfil,
                view.getEstadisticasIA()));
        gui.mostrarConfiguracionPartido();
    }
}
