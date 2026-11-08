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
        for (ConfiguracionVisualView.TarjetaVisual tarjeta : view.getTarjetasBalon()) {
            tarjeta.addActionListener(e -> view.seleccionarBalon(tarjeta));
        }

        for (ConfiguracionVisualView.TarjetaVisual tarjeta : view.getTarjetasEscenario()) {
            tarjeta.addActionListener(e -> view.seleccionarEscenario(tarjeta));
        }

        view.getBotonAtras().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gui.detenerSonido();
                gui.mostrarSeleccionPersonaje();
            }
        });
        view.getBotonSonido().addActionListener(e -> gui.alternarSonido());
        view.getBotonReady().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                continuar();
            }
        });

        view.seleccionarIniciales();
    }

    private void continuar() {
        CatalogoVisual.Opcion balon = view.getBalonSeleccionado();
        CatalogoVisual.Opcion escenario = view.getEscenarioSeleccionado();

        if (balon == null || escenario == null) {
            return;
        }

        gui.setConfiguracionVisual(balon.getRuta(), escenario.getRuta());
        gui.mostrarProximamente("Juego");
    }
}
