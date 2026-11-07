package presentation;

import domain.PerfilJugador;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class SeleccionPerfilController {

    private final DOPOSoccerGUI gui;
    private final SeleccionPerfilView view;
    private final List<PerfilJugador> perfiles;

    private PerfilJugador perfil1;
    private PerfilJugador perfil2;
    private boolean listo1;
    private boolean listo2;
    private boolean actualizando;

    public SeleccionPerfilController(DOPOSoccerGUI gui, SeleccionPerfilView view,
                                     List<PerfilJugador> perfiles) {
        this.gui = gui;
        this.view = view;
        this.perfiles = perfiles;
        prepareController();
    }

    private void prepareController() {
        SeleccionPerfilView.PanelJugador panel1 = view.getPanelJugador1();
        SeleccionPerfilView.PanelJugador panel2 = view.getPanelJugador2();

        panel1.getLista().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                alSeleccionar(1);
            }
        });
        panel2.getLista().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                alSeleccionar(2);
            }
        });

        panel1.getBotonContinuar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                continuar(1);
            }
        });
        panel2.getBotonContinuar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                continuar(2);
            }
        });

        panel1.getBotonCambiar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cambiar(1);
            }
        });
        panel2.getBotonCambiar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cambiar(2);
            }
        });

        panel1.getBotonCrear().addActionListener(e -> gui.mostrarPerfilDesdeSeleccion());
        panel2.getBotonCrear().addActionListener(e -> gui.mostrarPerfilDesdeSeleccion());
        panel1.getBotonCrearListo().addActionListener(e -> gui.mostrarPerfilDesdeSeleccion());
        panel2.getBotonCrearListo().addActionListener(e -> gui.mostrarPerfilDesdeSeleccion());

        view.getBotonContinuarCentral().addActionListener(e -> gui.mostrarConfiguracionPartido());
        view.getBotonAtras().addActionListener(e -> gui.mostrarMenu());
    }

    /**
     * Deja la pantalla sin perfiles elegidos para empezar una selección nueva.
     */
    public void reiniciar() {
        perfil1 = null;
        perfil2 = null;
        listo1 = false;
        listo2 = false;
        actualizando = true;
        view.reiniciar();
        actualizando = false;
        refrescar();
    }

    /**
     * Vuelve a cargar las listas de perfiles y el estado de la pantalla.
     */
    public void refrescar() {
        actualizando = true;
        validarPerfilesExistentes();
        view.getPanelJugador1().mostrarPerfiles(perfilesPara(perfil2));
        view.getPanelJugador2().mostrarPerfiles(perfilesPara(perfil1));
        actualizando = false;

        if (!listo1) {
            perfil1 = view.getPanelJugador1().getSeleccionado();
        }
        if (!listo2) {
            perfil2 = view.getPanelJugador2().getSeleccionado();
        }

        if (listo1 && perfil1 != null) {
            view.getPanelJugador1().marcarListo(perfil1.getNombre());
        }
        if (listo2 && perfil2 != null) {
            view.getPanelJugador2().marcarListo(perfil2.getNombre());
        }

        actualizarBotonCentral();
    }

    public PerfilJugador getPerfil1() {
        return perfil1;
    }

    public PerfilJugador getPerfil2() {
        return perfil2;
    }

    private void alSeleccionar(int lado) {
        if (actualizando || (lado == 1 && listo1) || (lado == 2 && listo2)) {
            return;
        }

        if (lado == 1) {
            perfil1 = view.getPanelJugador1().getSeleccionado();
            actualizando = true;
            view.getPanelJugador2().mostrarPerfiles(perfilesPara(perfil1));
            actualizando = false;
            if (!listo2) {
                perfil2 = view.getPanelJugador2().getSeleccionado();
            }
        } else {
            perfil2 = view.getPanelJugador2().getSeleccionado();
            actualizando = true;
            view.getPanelJugador1().mostrarPerfiles(perfilesPara(perfil2));
            actualizando = false;
            if (!listo1) {
                perfil1 = view.getPanelJugador1().getSeleccionado();
            }
        }

        actualizarBotonCentral();
    }

    private void continuar(int lado) {
        PerfilJugador elegido = lado == 1 ? perfil1 : perfil2;
        if (elegido == null) {
            mostrarError("Selecciona un perfil para continuar.");
            return;
        }

        if (lado == 1) {
            listo1 = true;
            view.getPanelJugador1().marcarListo(perfil1.getNombre());
        } else {
            listo2 = true;
            view.getPanelJugador2().marcarListo(perfil2.getNombre());
        }

        actualizarBotonCentral();
    }

    private void cambiar(int lado) {
        actualizando = true;
        if (lado == 1) {
            listo1 = false;
            perfil1 = null;
            view.getPanelJugador1().volverASeleccion();
            view.getPanelJugador1().mostrarPerfiles(perfilesPara(perfil2));
            if (!listo2) {
                view.getPanelJugador2().mostrarPerfiles(perfilesPara(null));
            }
            actualizando = false;

            perfil1 = view.getPanelJugador1().getSeleccionado();
            if (!listo2) {
                perfil2 = view.getPanelJugador2().getSeleccionado();
            }
        } else {
            listo2 = false;
            perfil2 = null;
            view.getPanelJugador2().volverASeleccion();
            view.getPanelJugador2().mostrarPerfiles(perfilesPara(perfil1));
            if (!listo1) {
                view.getPanelJugador1().mostrarPerfiles(perfilesPara(null));
            }
            actualizando = false;

            perfil2 = view.getPanelJugador2().getSeleccionado();
            if (!listo1) {
                perfil1 = view.getPanelJugador1().getSeleccionado();
            }
        }

        actualizarBotonCentral();
    }

    private void validarPerfilesExistentes() {
        if (perfil1 != null && !existe(perfil1)) {
            perfil1 = null;
            listo1 = false;
            view.getPanelJugador1().volverASeleccion();
        }
        if (perfil2 != null && !existe(perfil2)) {
            perfil2 = null;
            listo2 = false;
            view.getPanelJugador2().volverASeleccion();
        }
    }

    private boolean existe(PerfilJugador perfil) {
        for (PerfilJugador actual : perfiles) {
            if (actual.getId().equals(perfil.getId())) {
                return true;
            }
        }
        return false;
    }

    private List<PerfilJugador> perfilesPara(PerfilJugador excluido) {
        List<PerfilJugador> disponibles = new ArrayList<>();
        for (PerfilJugador perfil : perfiles) {
            if (excluido == null || !perfil.getId().equals(excluido.getId())) {
                disponibles.add(perfil);
            }
        }
        return disponibles;
    }

    private void actualizarBotonCentral() {
        boolean ambosListos = listo1 && listo2 && perfil1 != null && perfil2 != null;
        view.setContinuarCentralVisible(ambosListos);
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(
                view,
                mensaje,
                "Seleccionar perfil",
                JOptionPane.WARNING_MESSAGE);
    }
}
