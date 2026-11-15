package presentation;

import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConfiguracionVisualController {

    private final DOPOSoccerGUI gui;
    private final ConfiguracionVisualView view;

    /**
     * Crea el controlador de la configuración visual.
     *
     * @param gui ventana principal del juego
     * @param view vista de configuración visual
     */
    public ConfiguracionVisualController(DOPOSoccerGUI gui, ConfiguracionVisualView view) {
        this.gui = gui;
        this.view = view;
        prepareController();
    }

    /**
     * Conecta los botones y tarjetas de la vista con el controlador.
     */
    private void prepareController() {
        for (ConfiguracionVisualView.TarjetaVisual tarjeta : view.getTarjetasBalon()) {
            tarjeta.addActionListener(e -> view.seleccionarBalon(tarjeta));
        }

        for (ConfiguracionVisualView.TarjetaVisual tarjeta : view.getTarjetasEscenario()) {
            tarjeta.addActionListener(e -> view.seleccionarEscenario(tarjeta));
        }

        view.getBotonAtras().addActionListener(new ActionListener() {
            /**
             * Vuelve a la selección de personajes.
             *
             * @param e evento de acción
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                gui.detenerSonido();
                gui.mostrarSeleccionPersonaje();
            }
        });
        view.getBotonSonido().addActionListener(e -> gui.alternarSonido());
        view.getBotonReady().addActionListener(new ActionListener() {
            /**
             * Confirma la selección e inicia el juego.
             *
             * @param e evento de acción
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                continuar();
            }
        });

        view.seleccionarIniciales();
    }

    /**
     * Guarda el balón y el escenario elegidos e inicia el juego.
     */
    private void continuar() {
        CatalogoVisual.Opcion balon = view.getBalonSeleccionado();
        CatalogoVisual.Opcion escenario = view.getEscenarioSeleccionado();

        if (balon == null || escenario == null) {
            JOptionPane.showMessageDialog(
                    view,
                    "Selecciona un balón y un escenario para continuar.",
                    "Balón y escenario",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        gui.setConfiguracionVisual(balon.getRuta(), escenario.getRuta());
        gui.iniciarJuego();
    }
}
