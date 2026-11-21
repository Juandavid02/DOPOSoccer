package presentation;

import domain.Modalidad;
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

    private Modalidad modo = Modalidad.PvsP;
    private PerfilJugador perfil1;
    private PerfilJugador perfil2;
    private boolean listo1;
    private boolean listo2;
    private boolean actualizando;

    /**
     * Crea el controlador de la selección de perfiles.
     *
     * @param gui ventana principal del juego
     * @param view vista de selección de perfiles
     * @param perfiles perfiles disponibles
     */
    public SeleccionPerfilController(DOPOSoccerGUI gui, SeleccionPerfilView view,
                                     List<PerfilJugador> perfiles) {
        this.gui = gui;
        this.view = view;
        this.perfiles = perfiles;
        prepareController();
    }

    /**
     * Conecta los botones y listas de la vista con el controlador.
     */
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
            /**
             * Marca como listo el perfil del jugador 1.
             *
             * @param e evento de acción
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                continuar(1);
            }
        });
        panel2.getBotonContinuar().addActionListener(new ActionListener() {
            /**
             * Marca como listo el perfil del jugador 2.
             *
             * @param e evento de acción
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                continuar(2);
            }
        });

        panel1.getBotonCambiar().addActionListener(new ActionListener() {
            /**
             * Permite cambiar el perfil del jugador 1.
             *
             * @param e evento de acción
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                cambiar(1);
            }
        });
        panel2.getBotonCambiar().addActionListener(new ActionListener() {
            /**
             * Permite cambiar el perfil del jugador 2.
             *
             * @param e evento de acción
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                cambiar(2);
            }
        });

        panel1.getBotonCrear().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gui.mostrarPerfilDesdeSeleccion();
            }
        });
        panel2.getBotonCrear().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gui.mostrarPerfilDesdeSeleccion();
            }
        });
        panel1.getBotonCrearListo().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gui.mostrarPerfilDesdeSeleccion();
            }
        });
        panel2.getBotonCrearListo().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gui.mostrarPerfilDesdeSeleccion();
            }
        });

        view.getBotonContinuarCentral().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (modo == Modalidad.PvsM) {
                    gui.setPerfilesPartido(perfil1, null);
                    if (gui.isTorneoPendiente()) {
                        gui.mostrarInscripcionTorneo();
                    } else {
                        gui.iniciarSeleccionPerfilIA();
                    }
                    return;
                }
                gui.setPerfilesPartido(perfil1, perfil2);
                gui.mostrarConfiguracionPartido();
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
     * Cambia entre la selección de dos humanos (PvsP) y un solo humano
     * (PvsM). En PvsM la máquina se configura en su propia pantalla.
     *
     * @param modo modalidad elegida
     */
    public void setModo(Modalidad modo) {
        if (modo == null) {
            this.modo = Modalidad.PvsP;
        } else {
            this.modo = modo;
        }
        if (this.modo == Modalidad.PvsM) {
            perfil2 = null;
            listo2 = false;
        }
        view.setModo(this.modo);
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

    /**
     * Actualiza el perfil elegido y el del otro lado al seleccionar uno.
     *
     * @param lado número del jugador: 1 o 2
     */
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

    /**
     * Confirma el perfil del jugador indicado.
     *
     * @param lado número del jugador: 1 o 2
     */
    private void continuar(int lado) {
        PerfilJugador elegido;
        if (lado == 1) {
            elegido = perfil1;
        } else {
            elegido = perfil2;
        }

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

    /**
     * Vuelve al estado de selección del jugador indicado.
     *
     * @param lado número del jugador: 1 o 2
     */
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

    /**
     * Quita los perfiles que ya no existen de la selección.
     */
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

    /**
     * Indica si un perfil está en la lista de perfiles disponibles.
     *
     * @param perfil perfil que se busca
     * @return true si el perfil existe
     */
    private boolean existe(PerfilJugador perfil) {
        for (PerfilJugador actual : perfiles) {
            if (actual.getId().equals(perfil.getId())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Devuelve los perfiles que puede elegir un jugador.
     *
     * @param excluido perfil que se excluye de la lista
     * @return los perfiles disponibles
     */
    private List<PerfilJugador> perfilesPara(PerfilJugador excluido) {
        List<PerfilJugador> disponibles = new ArrayList<>();
        for (PerfilJugador perfil : perfiles) {
            if (excluido == null || !perfil.getId().equals(excluido.getId())) {
                disponibles.add(perfil);
            }
        }
        return disponibles;
    }

    /**
     * Muestra el botón central solo si ambos perfiles están listos.
     */
    private void actualizarBotonCentral() {
        boolean listo;
        if (modo == Modalidad.PvsM) {
            listo = listo1 && perfil1 != null;
        } else {
            listo = listo1 && listo2 && perfil1 != null && perfil2 != null;
        }
        view.setContinuarCentralVisible(listo);
    }

    /**
     * Muestra un mensaje de advertencia en la pantalla.
     *
     * @param mensaje texto del mensaje
     */
    private void mostrarError(String mensaje) {
        Dialogos.aviso(view, "Seleccionar perfil", mensaje,
                JOptionPane.WARNING_MESSAGE);
    }
}
