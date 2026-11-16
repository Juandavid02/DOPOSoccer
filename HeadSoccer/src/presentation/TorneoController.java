package presentation;

import domain.ConfiguracionIA;
import domain.ConfiguracionPartido;
import domain.ExcepcionDOPOSoccer;
import domain.Modalidad;
import domain.ParametrosJuego;
import domain.ParticipanteTorneo;
import domain.Partido;
import domain.PartidoTorneo;
import domain.PerfilIA;
import domain.PerfilJugador;
import domain.Personaje;
import domain.Resolucion;
import domain.TipoPartido;
import domain.Torneo;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Controlador del torneo: cobra la inscripción, deja elegir personaje y
 * configuración, y administra la llave y los partidos.
 */
public class TorneoController {

    private static final int MINUTOS_POR_DEFECTO = 2;
    private static final int GOLES_POR_DEFECTO = 5;

    private final DOPOSoccerGUI gui;
    private final TorneoView view;
    private final Random random = new Random();

    private Torneo torneo;
    private boolean inscrito;
    private TipoPartido tipoPartido = TipoPartido.TIEMPO;
    private Resolucion resolucion = Resolucion.R1280x720;
    private int tiempo = MINUTOS_POR_DEFECTO;
    private int goles = GOLES_POR_DEFECTO;

    /**
     * Crea el controlador del torneo y registra los listeners de la vista.
     *
     * @param gui ventana principal del juego
     * @param view vista del torneo
     */
    public TorneoController(DOPOSoccerGUI gui, TorneoView view) {
        this.gui = gui;
        this.view = view;
        prepareController();
    }

    /**
     * Conecta los botones de la vista con las acciones del controlador.
     */
    private void prepareController() {
        view.getBotonInscribirse().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inscribir();
            }
        });
        view.getBotonJugar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jugarOSiguiente();
            }
        });
        view.getBotonAtras().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salir();
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
     * Muestra la pantalla de inscripción con el perfil humano ya elegido.
     */
    public void mostrarInscripcion() {
        torneo = null;
        inscrito = false;
        view.mostrarInscripcion(gui.getPerfilPartido1());
        gui.mostrarTorneo();
    }

    /**
     * Cobra la inscripción y pasa a la selección de personaje.
     */
    private void inscribir() {
        PerfilJugador perfil = gui.getPerfilPartido1();

        if (perfil == null) {
            mostrarAviso("Falta el perfil para el torneo.");
            return;
        }
        if (!perfil.getPuntos().puedePagar(ParametrosJuego.COSTO_TORNEO)) {
            mostrarAviso(ExcepcionDOPOSoccer.PUNTOS_INSUFICIENTES);
            view.mostrarInscripcion(perfil);
            return;
        }

        int opcion = JOptionPane.showConfirmDialog(
                view,
                "Inscribirse cuesta " + ParametrosJuego.COSTO_TORNEO
                        + " puntos y no es reembolsable.\n¿Quieres continuar?",
                "Inscripción al torneo",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        if (opcion != JOptionPane.YES_OPTION) {
            return;
        }

        if (!perfil.getPuntos().gastar(ParametrosJuego.COSTO_TORNEO)) {
            mostrarAviso(ExcepcionDOPOSoccer.PUNTOS_INSUFICIENTES);
            return;
        }

        inscrito = true;
        gui.iniciarSeleccionPersonajeTorneo();
    }

    /**
     * Crea la llave después de elegir personaje y configuración.
     *
     * @param tipoPartido tipo de partido elegido
     * @param resolucion resolución elegida
     * @param tiempo minutos si es por tiempo
     * @param goles goles objetivo si es por goles
     */
    public void crearTorneo(TipoPartido tipoPartido, Resolucion resolucion,
                            int tiempo, int goles) {
        PerfilJugador perfil = gui.getPerfilPartido1();
        Personaje personajeHumano = gui.getPersonajeJugador1();

        if (!inscrito) {
            mostrarAviso("Primero debes pagar la inscripción.");
            return;
        }
        if (perfil == null || personajeHumano == null) {
            mostrarAviso("Falta el perfil o el personaje para el torneo.");
            return;
        }

        this.tipoPartido = tipoPartido == null
                ? TipoPartido.TIEMPO : tipoPartido;
        this.resolucion = resolucion == null
                ? Resolucion.R1280x720 : resolucion;
        this.tiempo = Math.max(1, tiempo);
        this.goles = Math.max(1, goles);

        List<Personaje> rivales = personajesRivales(personajeHumano);
        if (rivales.size() < 3) {
            mostrarAviso("No hay suficientes personajes para el torneo.");
            return;
        }

        ParticipanteTorneo humano = ParticipanteTorneo.humano(
                perfil, personajeHumano);
        ParticipanteTorneo ia1 = ParticipanteTorneo.maquina(
                PerfilIA.EXPERTA, rivales.get(0));
        ParticipanteTorneo ia2 = ParticipanteTorneo.maquina(
                PerfilIA.EXPERTA, rivales.get(1));
        ParticipanteTorneo ia3 = ParticipanteTorneo.maquina(
                PerfilIA.EXPERTA, rivales.get(2));

        try {
            torneo = new Torneo(perfil, humano, ia1, ia2, ia3, false);
        } catch (ExcepcionDOPOSoccer e) {
            mostrarAviso(e.getMessage());
            return;
        }

        view.mostrarTorneo(torneo);
        gui.mostrarTorneo();
    }

    /**
     * Inicia el partido del siguiente cruce del jugador humano.
     */
    private void jugarOSiguiente() {
        if (torneo == null) {
            return;
        }

        PartidoTorneo siguiente = torneo.getSiguientePartidoHumano();
        if (siguiente == null) {
            return;
        }

        ParticipanteTorneo rival = siguiente.getPrimero()
                == torneo.getHumano()
                ? siguiente.getSegundo() : siguiente.getPrimero();

        ConfiguracionIA configuracionIA = rival.crearConfiguracionIA();
        ConfiguracionPartido configuracion = null;
        try {
            configuracion = new ConfiguracionPartido(
                    Modalidad.PvsM,
                    tipoPartido,
                    resolucion,
                    tiempo,
                    goles,
                    rival.getPerfilIA());
        } catch (ExcepcionDOPOSoccer e) {
            mostrarAviso(e.getMessage());
            return;
        }

        gui.setModalidadSeleccionada(Modalidad.PvsM);
        gui.setPerfilesPartido(torneo.getPerfilHumano(), null);
        gui.setPersonajes(torneo.getHumano().getPersonaje(),
                rival.getPersonaje());
        gui.setConfiguracionIA(configuracionIA);
        gui.setConfiguracionPartido(configuracion);
        gui.setConfiguracionVisual(rutaBalon(), rutaEscenario());
        gui.iniciarJuegoTorneo(this::alVolverDelPartido);
    }

    /**
     * Registra el resultado del partido y actualiza la llave.
     *
     * @param partido partido terminado
     */
    private void alVolverDelPartido(Partido partido) {
        if (torneo == null || partido == null
                || partido.getResultado() == null) {
            return;
        }

        int golesHumano = partido.getResultado().getGolesJugador1();
        int golesRival = partido.getResultado().getGolesJugador2();
        torneo.registrarResultadoHumano(golesHumano, golesRival, random);

        view.mostrarTorneo(torneo);
        gui.mostrarTorneo();
    }

    /**
     * Vuelve a la inscripción después de que el jugador abandone la
     * selección de personaje. La inscripción ya pagada no se devuelve.
     */
    public void volverAInscripcion() {
        torneo = null;
        inscrito = false;
        view.mostrarInscripcion(gui.getPerfilPartido1());
        gui.mostrarTorneo();
    }

    /**
     * Sale del torneo y vuelve al menú principal.
     */
    private void salir() {
        boolean torneoActivo = torneo != null && !torneo.isTerminado();
        boolean inscripcionSinLlave = inscrito && torneo == null;

        if (torneoActivo || inscripcionSinLlave) {
            if (!confirmarVolverAInscripcion()) {
                return;
            }
            volverAInscripcion();
            return;
        }

        cancelar();
        gui.mostrarMenu();
    }

    /**
     * Pide confirmación para volver a pagar la inscripción.
     *
     * @return true si el jugador confirma, false en caso contrario
     */
    private boolean confirmarVolverAInscripcion() {
        int opcion = JOptionPane.showConfirmDialog(
                view,
                "Si vuelves ahora perderás el dinero de la inscripción "
                        + "y quedarás descalificado del torneo.\n"
                        + "¿Quieres volver a pagar la inscripción?",
                "Volver a la inscripción",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        return opcion == JOptionPane.YES_OPTION;
    }

    /**
     * Limpia el estado del torneo sin mostrar ninguna pantalla.
     */
    public void cancelar() {
        torneo = null;
        inscrito = false;
    }

    /**
     * Elige tres personajes distintos para las IAs, sin repetir el del
     * humano. Así ningún país aparece dos veces en la llave.
     *
     * @param humano personaje elegido por el jugador
     * @return personajes disponibles mezclados
     */
    private List<Personaje> personajesRivales(Personaje humano) {
        List<Personaje> disponibles = new ArrayList<>();
        for (CatalogoSeleccion.Opcion opcion : CatalogoSeleccion.getOpciones()) {
            if (humano == null || !opcion.es(humano)) {
                disponibles.add(opcion.getPersonaje());
            }
        }
        Collections.shuffle(disponibles, random);
        return disponibles;
    }

    /**
     * Devuelve la ruta del balón seleccionado o la del primero por defecto.
     *
     * @return ruta del balón
     */
    private String rutaBalon() {
        String ruta = gui.getRutaBalonSeleccionado();
        return ruta != null ? ruta
                : CatalogoVisual.getBalones().get(0).getRuta();
    }

    /**
     * Devuelve la ruta del escenario elegido o el primero por defecto.
     *
     * @return ruta del escenario
     */
    private String rutaEscenario() {
        String ruta = gui.getRutaEscenarioSeleccionado();
        return ruta != null ? ruta
                : CatalogoVisual.getEscenarios().get(0).getRuta();
    }

    /**
     * Muestra un aviso al jugador con el mensaje indicado.
     *
     * @param mensaje texto que se va a mostrar
     */
    private void mostrarAviso(String mensaje) {
        JOptionPane.showMessageDialog(
                view,
                mensaje,
                "Torneo",
                JOptionPane.WARNING_MESSAGE);
    }
}
