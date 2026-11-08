package presentation;

import domain.ConfiguracionPartido;
import domain.Modalidad;

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
        view.getBotonSonido().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gui.alternarSonido();
            }
        });
        view.getBotonReady().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                continuar();
            }
        });
    }

    private void continuar() {
        ConfiguracionPartido configuracion = new ConfiguracionPartido(
                Modalidad.PvsP,
                view.getTipoPartido(),
                view.getResolucion(),
                view.getTiempo(),
                view.getGoles());

        gui.setConfiguracionPartido(configuracion);
        gui.iniciarSeleccionPersonaje();
    }
}
