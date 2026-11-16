package presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MenuController {

    private final DOPOSoccerGUI gui;
    private final MenuView view;

    /**
     * Crea el controlador del menú y conecta los botones con sus acciones.
     *
     * @param gui la ventana principal que cambia entre pantallas
     * @param view la vista del menú
     */
    public MenuController(DOPOSoccerGUI gui, MenuView view) {
        this.gui = gui;
        this.view = view;
        prepareController();
    }

    /**
     * Asigna una acción a cada botón de la vista.
     */
    private void prepareController() {
        view.getBotonGestionarProgreso().addActionListener(new ActionListener() {
            @Override
            /**
             * Ejecuta la acción del botón cuando se presiona.
             *
             * @param e evento de acción
             */
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(
                        view,
                        "Persistencia en construcción",
                        "Gestionar progreso",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
        view.getBotonArcade().addActionListener(new ActionListener() {
            @Override
            /**
             * Ejecuta la acción del botón cuando se presiona.
             *
             * @param e evento de acción
             */
            public void actionPerformed(ActionEvent e) {
                gui.mostrarSeleccionModo();
            }
        });
        view.getBotonTorneo().addActionListener(new ActionListener() {
            @Override
            /**
             * Ejecuta la acción del botón cuando se presiona.
             *
             * @param e evento de acción
             */
            public void actionPerformed(ActionEvent e) {
                gui.iniciarTorneo();
            }
        });
        view.getBotonConfiguracion().addActionListener(new ActionListener() {
            @Override
            /**
             * Ejecuta la acción del botón cuando se presiona.
             *
             * @param e evento de acción
             */
            public void actionPerformed(ActionEvent e) {
                gui.mostrarConfiguracion();
            }
        });
        view.getBotonTienda().addActionListener(new ActionListener() {
            @Override
            /**
             * Ejecuta la acción del botón cuando se presiona.
             *
             * @param e evento de acción
             */
            public void actionPerformed(ActionEvent e) {
                gui.mostrarMejoras();
            }
        });
        view.getBotonPerfil().addActionListener(new ActionListener() {
            @Override
            /**
             * Ejecuta la acción del botón cuando se presiona.
             *
             * @param e evento de acción
             */
            public void actionPerformed(ActionEvent e) {
                gui.mostrarPerfil();
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
        view.getBotonAtras().addActionListener(new ActionListener() {
            @Override
            /**
             * Ejecuta la acción del botón cuando se presiona.
             *
             * @param e evento de acción
             */
            public void actionPerformed(ActionEvent e) {
                gui.mostrarInicio();
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
     * Pregunta al usuario si desea salir del juego y cierra la ventana.
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
