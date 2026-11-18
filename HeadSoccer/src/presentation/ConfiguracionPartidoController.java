package presentation;

import domain.ConfiguracionIA;
import domain.ConfiguracionPartido;
import domain.ExcepcionDOPOSoccer;
import domain.Modalidad;
import domain.PerfilIA;

import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConfiguracionPartidoController {

    private final DOPOSoccerGUI gui;
    private final ConfiguracionPartidoView view;

    /**
     * Crea el controlador de la configuración del partido.
     *
     * @param gui ventana principal del juego
     * @param view vista de configuración del partido
     */
    public ConfiguracionPartidoController(DOPOSoccerGUI gui, ConfiguracionPartidoView view) {
        this.gui = gui;
        this.view = view;
        prepareController();
    }

    /**
     * Conecta los botones de la vista con el controlador.
     */
    private void prepareController() {
        view.getBotonAtras().addActionListener(new ActionListener() {
            /**
             * Vuelve a la pantalla anterior.
             *
             * @param e evento de acción
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gui.isConfiguracionTorneo()) {
                    gui.mostrarSeleccionPersonajeTorneo();
                } else if (gui.esPvsM()) {
                    gui.mostrarSeleccionPerfilIA();
                } else {
                    gui.mostrarSeleccionPerfil();
                }
            }
        });
        view.getBotonSonido().addActionListener(new ActionListener() {
            /**
             * Alterna el sonido del juego.
             *
             * @param e evento de acción
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                gui.alternarSonido();
            }
        });
        view.getBotonReady().addActionListener(new ActionListener() {
            /**
             * Confirma la configuración y continúa.
             *
             * @param e evento de acción
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                continuar();
            }
        });
    }

    /**
     * Valida la configuración y avanza a la siguiente pantalla.
     */
    private void continuar() {
        try {
            Modalidad modalidad = gui.getModalidadSeleccionada();
            ConfiguracionIA configuracionIA = gui.getConfiguracionIA();
            PerfilIA perfilIA = null;
            if (modalidad == Modalidad.PvsM && configuracionIA != null) {
                perfilIA = configuracionIA.getPerfil();
            }

            ConfiguracionPartido configuracion = new ConfiguracionPartido(
                    modalidad,
                    view.getTipoPartido(),
                    view.getResolucion(),
                    view.getTiempo(),
                    view.getGoles(),
                    perfilIA);

            gui.setConfiguracionPartido(configuracion);
            if (gui.isConfiguracionTorneo()) {
                gui.crearTorneoDesdeConfiguracion(configuracion);
            } else {
                gui.iniciarSeleccionPersonaje();
            }
        } catch (ExcepcionDOPOSoccer e) {
            JOptionPane.showMessageDialog(
                    view,
                    e.getMessage(),
                    "Configuración del partido",
                    JOptionPane.WARNING_MESSAGE);
        }
    }
}
