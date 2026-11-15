package presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class InicioController {

    private final DOPOSoccerGUI gui;
    private final InicioView view;

    /**
     * Crea el controlador de la pantalla de inicio y conecta sus botones.
     *
     * @param gui la ventana principal
     * @param view la vista de inicio
     */
    public InicioController(DOPOSoccerGUI gui, InicioView view) {
        this.gui = gui;
        this.view = view;
        prepareController();
    }

    /**
     * Asigna una acción a cada botón de la vista.
     */
    private void prepareController() {
        view.getBotonPlay().addActionListener(new ActionListener() {
            @Override
            /**
             * Ejecuta la acción del botón cuando se presiona.
             *
             * @param e evento de acción
             */
            public void actionPerformed(ActionEvent e) {
                gui.mostrarMenu();
            }
        });
        view.getBotonSalir().addActionListener(new ActionListener() {
            @Override
            /**
             * Ejecuta la acción del botón cuando se presiona.
             *
             * @param e evento de acción
             */
            public void actionPerformed(ActionEvent e) {
                confirmarSalida();
            }
        });
        view.getBotonSonido().addActionListener(new ActionListener() {
            @Override
            /**
             * Ejecuta la acción del botón cuando se presiona.
             *
             * @param e evento de acción
             */
            public void actionPerformed(ActionEvent e) {
                gui.alternarSonido();
            }
        });
    }

    /**
     * Pregunta al usuario si desea salir del juego y cierra la aplicación.
     */
    private void confirmarSalida() {
        int opcion = JOptionPane.showConfirmDialog(
                view,
                "¿Seguro que quieres salir de DOPOSoccer?",
                "Salir",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        if (opcion == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}