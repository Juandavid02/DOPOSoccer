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
        view.getBotonContinuar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                continuarPartida();
            }
        });
        view.getBotonPvsP().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gui.iniciarSeleccionPerfil();
            }
        });
        view.getBotonPractica().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gui.mostrarProximamente("Modo Práctica (PvsM)");
            }
        });
        view.getBotonConfiguracion().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gui.mostrarProximamente("Configuración");
            }
        });
        view.getBotonMejoras().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gui.mostrarProximamente("Mejoras");
            }
        });
        view.getBotonPerfil().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gui.mostrarPerfil();
            }
        });
        view.getBotonSalir().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmarSalida();
            }
        });
        view.getBotonAtras().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gui.mostrarInicio();
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
     * Intenta cargar la última partida guardada. La persistencia todavía
     * no está implementada.
     */
    private void continuarPartida() {
        // Cuando exista RepositorioPartidas, aquí se cargará la última partida.
        JOptionPane.showMessageDialog(
                view,
                "No hay una partida guardada todavía.",
                "Continuar partida",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Pregunta al usuario si desea salir del juego y cierra la ventana.
     */
    private void confirmarSalida() {
        int opcion = JOptionPane.showConfirmDialog(view,"¿Seguro que quieres salir de DOPOSoccer?","Salir",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (opcion == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}
