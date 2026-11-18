package presentation;

import domain.ParametrosJuego;
import domain.PerfilJugador;
import domain.Personaje;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SeleccionPersonajeController {

    private final DOPOSoccerGUI gui;
    private final SeleccionPersonajeView view;

    private Personaje personaje1;
    private Personaje personaje2;
    private int turno;
    private boolean soloHumano;
    private final Random random = new Random();

    /**
     * Crea el controlador de la selección de personajes.
     *
     * @param gui ventana principal del juego
     * @param view vista de selección de personajes
     */
    public SeleccionPersonajeController(DOPOSoccerGUI gui, SeleccionPersonajeView view) {
        this.gui = gui;
        this.view = view;
        prepareController();
    }

    /**
     * Conecta los botones de la vista con las acciones del controlador.
     */
    private void prepareController() {
        for (SeleccionPersonajeView.BotonCaja boton : view.getBotonesCaja()) {
            boton.addActionListener(new ActionListener() {
                /**
                 * Selecciona el personaje de la caja pulsada.
                 *
                 * @param e evento de acción
                 */
                @Override
                public void actionPerformed(ActionEvent e) {
                    seleccionar(boton.getOpcion());
                }
            });
        }

        view.getBotonCambiar1().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cambiar(1);
            }
        });
        view.getBotonCambiar2().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cambiar(2);
            }
        });
        view.getBotonAleatorio().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                elegirAleatorio();
            }
        });

        view.getBotonReady().addActionListener(new ActionListener() {
            /**
             * Confirma la selección y pasa a la siguiente pantalla.
             *
             * @param e evento de acción
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                continuar();
            }
        });

        view.getBotonAtras().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gui.detenerSonido();
                if (soloHumano) {
                    int opcion = JOptionPane.showConfirmDialog(
                            view,
                            "Si vuelves perderás los "
                                    + ParametrosJuego.COSTO_TORNEO
                                    + " puntos de inscripción y quedarás "
                                    + "descalificado del torneo.\n"
                                    + "¿Quieres volver a la inscripción?",
                            "Volver a la inscripción",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE);
                    if (opcion == JOptionPane.YES_OPTION) {
                        gui.volverAInscripcionTorneo();
                    }
                    return;
                }
                if (gui.isTorneoPendiente()) {
                    gui.mostrarSeleccionPerfil();
                } else {
                    gui.mostrarConfiguracionPartido();
                }
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
     * Indica si la partida es contra la máquina con un solo humano.
     *
     * @param soloHumano true si solo juega un humano
     */
    public void setSoloHumano(boolean soloHumano) {
        this.soloHumano = soloHumano;
        view.setSoloHumano(soloHumano);
    }

    /**
     * Reinicia la selección de personajes.
     */
    public void reiniciar() {
        personaje1 = null;
        personaje2 = null;
        turno = 1;
        view.reiniciar();
        actualizarVista();
    }

    /**
     * Actualiza el turno y redibuja la vista.
     */
    public void refrescar() {
        actualizarTurno();
        actualizarVista();
    }

    /**
     * Selecciona el personaje de la opción indicada.
     *
     * @param opcion opción elegida
     */
    private void seleccionar(CatalogoSeleccion.Opcion opcion) {
        if (opcion == null || turno == 0) {
            return;
        }

        if (!view.estaDesbloqueado(opcion.getPersonaje(), perfilDeTurno())) {
            JOptionPane.showMessageDialog(
                    view,
                    "Ese personaje no está desbloqueado para este perfil.",
                    "Selección de personaje",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (turno == 1) {
            if (personaje2 != null && opcion.es(personaje2)) {
                return;
            }
            personaje1 = opcion.getPersonaje();
            if (soloHumano) {
                personaje2 = personajeAleatorio();
            }
        } else {
            if (personaje1 != null && opcion.es(personaje1)) {
                return;
            }
            personaje2 = opcion.getPersonaje();
        }

        gui.reproducirEfecto(opcion.getAudio());
        actualizarTurno();
        actualizarVista();
    }

    /**
     * Quita el personaje del jugador indicado.
     *
     * @param jugador número del jugador: 1 o 2
     */
    private void cambiar(int jugador) {
        gui.detenerSonido();

        if (jugador == 1) {
            personaje1 = null;
            if (soloHumano) {
                personaje2 = null;
            }
        } else {
            personaje2 = null;
        }

        actualizarTurno();
        actualizarVista();
    }

    /**
     * Valida la selección y avanza a la siguiente pantalla.
     */
    private void continuar() {
        if (personaje1 == null || (!soloHumano && personaje2 == null)) {
            String mensaje;
            if (soloHumano) {
                mensaje = "Selecciona un personaje para continuar.";
            } else {
                mensaje = "Cada jugador debe elegir un personaje.";
            }
            JOptionPane.showMessageDialog(
                    view,
                    mensaje,
                    "Selección de personaje",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (personaje2 == null) {
            personaje2 = personajeAleatorio();
        }

        gui.detenerSonido();
        gui.setPersonajes(personaje1, personaje2);

        if (soloHumano) {
            gui.mostrarConfiguracionTorneo();
            return;
        }

        // Siempre se pasa por la selección de balón y escenario, incluso
        // cuando el cambio de personaje se hizo desde una partida en curso.
        if (gui.isCambiarPersonajeDesdeJuego()) {
            gui.terminarCambioPersonaje();
        }
        gui.mostrarConfiguracionVisual();
    }

    /**
     * Calcula a qué jugador le toca elegir.
     */
    private void actualizarTurno() {
        if (soloHumano) {
            if (personaje1 == null) {
                turno = 1;
            } else {
                turno = 0;
            }
            return;
        }
        if (personaje1 == null) {
            turno = 1;
        } else if (personaje2 == null) {
            turno = 2;
        } else {
            turno = 0;
        }
    }

    /**
     * Refresca los perfiles y personajes que muestra la vista.
     */
    private void actualizarVista() {
        if (gui.esPvsM()) {
            view.setPerfilHumano(gui.getPerfilPartido1());
            if (!soloHumano) {
                view.setLadoIA(gui.getConfiguracionIA());
            }
        } else {
            view.setPerfiles(gui.getPerfilPartido1(), gui.getPerfilPartido2());
        }
        view.actualizar(personaje1, personaje2, turno, perfilDeTurno());
    }

    /**
     * Perfil al que le toca elegir. En PvsP cada jugador solo puede usar lo
     * que desbloqueó su propio perfil; la máquina no tiene perfil y puede
     * usar cualquier personaje.
     *
     * @return perfil que elige, o null si elige la máquina
     */
    private PerfilJugador perfilDeTurno() {
        if (gui.esPvsM()) {
            if (soloHumano || turno != 2) {
                return gui.getPerfilPartido1();
            }
            return null;
        }
        if (turno == 2) {
            return gui.getPerfilPartido2();
        }
        return gui.getPerfilPartido1();
    }

    /**
     * Elige un personaje al azar del catálogo.
     *
     * @return el personaje elegido
     */
    private Personaje personajeAleatorio() {
        List<CatalogoSeleccion.Opcion> opciones = CatalogoSeleccion.getOpciones();
        return opciones.get(random.nextInt(opciones.size())).getPersonaje();
    }

    /**
     * Elige al azar el personaje de la IA entre los personajes desbloqueados
     * y evitando repetir el de J1.
     */
    private void elegirAleatorio() {
        if (!gui.esPvsM()) {
            return;
        }

        List<CatalogoSeleccion.Opcion> disponibles = new ArrayList<>();
        for (CatalogoSeleccion.Opcion opcion : CatalogoSeleccion.getOpciones()) {
            if (!view.estaDesbloqueado(opcion.getPersonaje(), null)) {
                continue;
            }
            if (personaje1 != null && opcion.es(personaje1)) {
                continue;
            }
            if (personaje2 != null && opcion.es(personaje2)) {
                continue;
            }
            disponibles.add(opcion);
        }

        if (disponibles.isEmpty()) {
            JOptionPane.showMessageDialog(
                    view,
                    "No hay otro personaje desbloqueado disponible para la IA.",
                    "Selección aleatoria",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        CatalogoSeleccion.Opcion elegida =
                disponibles.get(random.nextInt(disponibles.size()));
        personaje2 = elegida.getPersonaje();
        gui.reproducirEfecto(elegida.getAudio());
        actualizarTurno();
        actualizarVista();
    }
}
