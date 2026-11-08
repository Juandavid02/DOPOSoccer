package presentation;

import domain.PerfilJugador;
import domain.TipoEstadistica;

import javax.swing.*;
import java.util.List;

/**
 * Controlador de la pantalla de mejoras de estadísticas.
 */
public class MejorasController {

    private final DOPOSoccerGUI gui;
    private final MejorasView view;
    private final List<PerfilJugador> perfiles;

    public MejorasController(DOPOSoccerGUI gui, MejorasView view,
                             List<PerfilJugador> perfiles) {
        this.gui = gui;
        this.view = view;
        this.perfiles = perfiles;
        prepareController();
    }

    private void prepareController() {
        view.getBotonAtras().addActionListener(e -> gui.mostrarMenu());
        view.getBotonSonido().addActionListener(e -> gui.alternarSonido());

        view.getListaPerfiles().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                view.mostrarPerfil(view.getListaPerfiles().getSelectedValue());
            }
        });

        for (TipoEstadistica tipo : TipoEstadistica.values()) {
            view.getBotonMejorar(tipo).addActionListener(e -> mejorar(tipo));
        }
    }

    /**
     * Carga los perfiles y deja seleccionado el primero.
     */
    public void abrir() {
        view.setPerfiles(perfiles);
    }

    private void mejorar(TipoEstadistica tipo) {
        PerfilJugador perfil = view.getListaPerfiles().getSelectedValue();
        if (perfil == null) {
            return;
        }

        int costo = perfil.getCostoSiguienteMejora();
        if (!perfil.puedeMejorar(tipo)) {
            JOptionPane.showMessageDialog(
                    view,
                    "Esa estadística ya está al máximo.",
                    "Mejoras",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        if (!perfil.getPuntos().puedePagar(costo)) {
            JOptionPane.showMessageDialog(
                    view,
                    "No tienes puntos suficientes. Necesitas " + costo + " puntos.",
                    "Mejoras",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (perfil.mejorar(tipo)) {
            view.mostrarPerfil(perfil);
            view.getListaPerfiles().repaint();
        }
    }
}
