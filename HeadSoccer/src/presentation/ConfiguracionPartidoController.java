package presentation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConfiguracionPartidoController {

    private final DOPOSoccerGUI gui;
    private final ConfiguracionPartidoView view;

    public ConfiguracionPartidoController(DOPOSoccerGUI gui, ConfiguracionPartidoView view) {
        this.gui = gui;
        this.view = view;
        prepareController();
    }

    private void prepareController() {
        view.getBotonAtras().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gui.mostrarSeleccionPerfil();
            }
        });
    }
}
