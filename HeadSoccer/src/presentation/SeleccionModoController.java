package presentation;

import domain.Modalidad;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Controlador de la selección de modo dentro de Arcade.
 */
public class SeleccionModoController {

    private final DOPOSoccerGUI gui;
    private final SeleccionModoView view;

    /**
     * Crea el controlador y registra los listeners de la vista.
     *
     * @param gui ventana principal del juego
     * @param view vista de selección de modo
     */
    public SeleccionModoController(DOPOSoccerGUI gui, SeleccionModoView view) {
        this.gui = gui;
        this.view = view;
        prepareController();
    }

    /**
     * Conecta los botones de la vista con las acciones del controlador.
     */
    private void prepareController() {
        view.getBotonPvsP().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iniciarModo(Modalidad.PvsP);
            }
        });
        view.getBotonPvsM().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iniciarModo(Modalidad.PvsM);
            }
        });
        view.getBotonAtras().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gui.mostrarMenu();
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
     * Inicia la selección de perfil con el modo elegido.
     *
     * @param modalidad modalidad de juego elegida
     */
    private void iniciarModo(Modalidad modalidad) {
        gui.setTorneoPendiente(false);
        gui.iniciarSeleccionPerfil(modalidad);
    }
}
