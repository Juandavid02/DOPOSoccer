package presentation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConfiguracionVisualController {

    private final DOPOSoccerGUI gui;
    private final ConfiguracionVisualView view;

    public ConfiguracionVisualController(DOPOSoccerGUI gui, ConfiguracionVisualView view) {
        this.gui = gui;
        this.view = view;
        prepareController();
    }

    private void prepareController() {
        view.getBotonAtras().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gui.mostrarSeleccionPersonaje();
            }
        });
        view.getBotonSonido().addActionListener(e -> gui.alternarSonido());
    }
}
