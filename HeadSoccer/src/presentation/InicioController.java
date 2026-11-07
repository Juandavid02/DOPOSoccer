package presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class InicioController {

    private final DOPOSoccerGUI gui;
    private final InicioView view;

    public InicioController(DOPOSoccerGUI gui, InicioView view) {
        this.gui = gui;
        this.view = view;
        prepareController();
    }

    private void prepareController() {
        view.getBotonPlay().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gui.mostrarMenu();
            }
        });
        view.getBotonSalir().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmarSalida();
            }
        });
    }

    private void confirmarSalida() {
        int opcion = JOptionPane.showConfirmDialog(view, "¿Seguro que quieres salir de DOPOSoccer?", "Salir", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (opcion == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}