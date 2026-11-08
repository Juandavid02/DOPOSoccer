package presentation;

import domain.Personaje;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SeleccionPersonajeController {

    private final DOPOSoccerGUI gui;
    private final SeleccionPersonajeView view;

    private Personaje personaje1;
    private Personaje personaje2;
    private int turno;

    public SeleccionPersonajeController(DOPOSoccerGUI gui, SeleccionPersonajeView view) {
        this.gui = gui;
        this.view = view;
        prepareController();
    }

    private void prepareController() {
        for (SeleccionPersonajeView.BotonCaja boton : view.getBotonesCaja()) {
            boton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    seleccionar(boton.getOpcion());
                }
            });
        }

        view.getBotonCambiar1().addActionListener(e -> cambiar(1));
        view.getBotonCambiar2().addActionListener(e -> cambiar(2));

        view.getBotonReady().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                continuar();
            }
        });

        view.getBotonAtras().addActionListener(e -> {
            gui.detenerSonido();
            gui.mostrarConfiguracionPartido();
        });
        view.getBotonSonido().addActionListener(e -> gui.alternarSonido());
    }

    public void reiniciar() {
        personaje1 = null;
        personaje2 = null;
        turno = 1;
        view.reiniciar();
        actualizarVista();
    }

    public void refrescar() {
        actualizarTurno();
        actualizarVista();
    }

    private void seleccionar(CatalogoSeleccion.Opcion opcion) {
        if (opcion == null || turno == 0) {
            return;
        }

        if (turno == 1) {
            if (personaje2 != null && opcion.es(personaje2)) {
                return;
            }
            personaje1 = opcion.getPersonaje();
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

    private void cambiar(int jugador) {
        gui.detenerSonido();

        if (jugador == 1) {
            personaje1 = null;
        } else {
            personaje2 = null;
        }

        actualizarTurno();
        actualizarVista();
    }

    private void continuar() {
        if (personaje1 == null || personaje2 == null) {
            return;
        }

        gui.detenerSonido();
        gui.setPersonajes(personaje1, personaje2);

        // Siempre se pasa por la selección de balón y escenario, incluso
        // cuando el cambio de personaje se hizo desde una partida en curso.
        if (gui.isCambiarPersonajeDesdeJuego()) {
            gui.terminarCambioPersonaje();
        }
        gui.mostrarConfiguracionVisual();
    }

    private void actualizarTurno() {
        if (personaje1 == null) {
            turno = 1;
        } else if (personaje2 == null) {
            turno = 2;
        } else {
            turno = 0;
        }
    }

    private void actualizarVista() {
        view.actualizar(personaje1, personaje2, turno);
    }
}
