package presentation;

import domain.ExcepcionDOPOSoccer;
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

    /**
     * Crea el controlador y conecta los botones de la vista.
     *
     * @param gui ventana principal del juego
     * @param view vista de mejoras
     * @param perfiles perfiles disponibles
     */
    public MejorasController(DOPOSoccerGUI gui, MejorasView view,
                             List<PerfilJugador> perfiles) {
        this.gui = gui;
        this.view = view;
        this.perfiles = perfiles;
        prepareController();
    }

    /**
     * Conecta los listeners de la vista con las acciones del controlador.
     */
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

        view.setAccionCompra(this::comprar);
    }

    /**
     * Carga los perfiles y deja seleccionado el primero.
     */
    public void abrir() {
        view.setPerfiles(perfiles);
    }

    /**
     * Compra un personaje, balón o escenario de la tienda.
     *
     * @param tarjeta tarjeta del artículo que se intenta comprar
     */
    private void comprar(MejorasView.TarjetaDesbloqueo tarjeta) {
        PerfilJugador perfil = view.getListaPerfiles().getSelectedValue();
        if (perfil == null) {
            JOptionPane.showMessageDialog(
                    view,
                    "Selecciona un perfil para usar sus puntos.",
                    "Tienda",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (tarjeta.isDesbloqueado()) {
            return;
        }

        Desbloqueos desbloqueos = gui.getDesbloqueos();
        boolean comprado;
        switch (tarjeta.getTipo()) {
            case PERSONAJE:
                comprado = desbloqueos.desbloquearPersonaje(
                        tarjeta.getNombre(), perfil);
                break;
            case BALON:
                comprado = desbloqueos.desbloquearBalon(
                        tarjeta.getNombre(), perfil);
                break;
            case ESCENARIO:
                comprado = desbloqueos.desbloquearEscenario(
                        tarjeta.getNombre(), perfil);
                break;
            default:
                comprado = false;
        }

        if (!comprado) {
            JOptionPane.showMessageDialog(
                    view,
                    "No tienes puntos suficientes. Necesitas "
                            + tarjeta.getPrecio() + " puntos.",
                    "Tienda",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        view.mostrarPerfil(perfil);
    }

    /**
     * Aplica una mejora a la estadística indicada del perfil seleccionado.
     *
     * @param tipo estadística que se desea mejorar
     */
    private void mejorar(TipoEstadistica tipo) {
        PerfilJugador perfil = view.getListaPerfiles().getSelectedValue();
        if (perfil == null) {
            JOptionPane.showMessageDialog(
                    view,
                    "Selecciona un perfil para mejorar sus estadísticas.",
                    "Mejoras",
                    JOptionPane.WARNING_MESSAGE);
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

        try {
            if (perfil.mejorar(tipo)) {
                view.mostrarPerfil(perfil);
                view.getListaPerfiles().repaint();
            }
        } catch (ExcepcionDOPOSoccer e) {
            JOptionPane.showMessageDialog(
                    view,
                    e.getMessage(),
                    "Mejoras",
                    JOptionPane.WARNING_MESSAGE);
        }
    }
}
