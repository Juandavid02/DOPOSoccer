package presentation;

import domain.ConfiguracionIA;
import domain.ConfiguracionJugador;
import domain.ConfiguracionPartido;
import domain.EstadoPartido;
import domain.Jugador;
import domain.Modalidad;
import domain.ParametrosJuego;
import domain.Partido;
import domain.PerfilIA;
import domain.PerfilJugador;
import domain.Personaje;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class JuegoController {

    private static final int INTERVALO = 16;
    private static final String SONIDO_GOL =
            "resources/audio/SonidosAmbiente/SonidoGol.wav";
    private static final String SONIDO_PITO_FINAL =
            "resources/audio/SonidosAmbiente/PitoFinal.wav";
    private static final String SONIDO_GOLPE =
            "resources/audio/SonidosAmbiente/GolpeEnLaCara.wav";
    private static final int RETRASO_PITO_GOL = 1100;
    private static final int RETRASO_MUSICA_MENU = 1500;

    private final DOPOSoccerGUI gui;
    private final JuegoView view;
    private final Set<Integer> teclas = new HashSet<>();

    private ConfiguracionPartido configuracion;
    private Personaje personaje1;
    private Personaje personaje2;
    private PerfilJugador perfil1;
    private PerfilJugador perfil2;
    private ConfiguracionIA configuracionIA;
    private String rutaEscenario;
    private String rutaBalon;

    private Partido partido;
    private Timer timer;
    private Timer pitoFinal;
    private Timer musicaMenuFinal;
    private long ultimoTiempo;
    private KeyAdapter escuchaTeclas;
    private boolean otorgarPuntos = true;
    private boolean modoTorneo;
    private Consumer<Partido> alVolverTorneo;
    private int golesAnteriores;
    private double vidaAnterior1;
    private double vidaAnterior2;
    private EstadoPartido estadoAnterior;

    /**
     * Crea el controlador del partido y conecta los botones con sus acciones.
     *
     * @param gui la ventana principal
     * @param view la vista del partido
     */
    public JuegoController(DOPOSoccerGUI gui, JuegoView view) {
        this.gui = gui;
        this.view = view;
        prepareController();
    }

    /**
     * Asigna una acción a cada botón de la vista.
     */
    private void prepareController() {
        view.getBotonAtras().addActionListener(e -> volverASeleccionVisual());
        view.getBotonSonido().addActionListener(e -> gui.alternarSonido());

        view.getBotonPausaContinuar().addActionListener(e -> reanudarPartido());
        view.getBotonPausaReiniciar().addActionListener(e -> reiniciarPartido());
        view.getBotonPausaCambiarPersonaje().addActionListener(e -> cambiarPersonaje());
        view.getBotonPausaConfiguracion().addActionListener(
                e -> gui.mostrarConfiguracionDesdePausa());
        view.getBotonPausaSalir().addActionListener(e -> salirAlMenu());

        view.getBotonResultadoRevancha().addActionListener(e -> revancha());
        view.getBotonResultadoCambiarPersonaje().addActionListener(
                e -> cambiarPersonajeDesdeResultado());
        view.getBotonResultadoMenu().addActionListener(
                e -> volverAlMenuDesdeResultado());
        view.getBotonResultadoTorneo().addActionListener(
                e -> volverAlTorneo());
    }

    /**
     * Inicia un partido con los datos recibidos y arranca su ciclo de
     * actualización.
     *
     * @param configuracion configuración del partido
     * @param personaje1 personaje del jugador 1
     * @param personaje2 personaje del jugador 2
     * @param perfil1 perfil del jugador 1
     * @param perfil2 perfil del jugador 2
     * @param configuracionIA configuración de la máquina
     * @param rutaEscenario ruta de la imagen del escenario
     * @param rutaBalon ruta de la imagen del balón
     * @param otorgarPuntos true si se otorgan puntos al ganar
     * @param alVolverTorneo acción a ejecutar al terminar, o null
     * @return true si el partido se pudo iniciar
     */
    public boolean iniciar(ConfiguracionPartido configuracion,
                           Personaje personaje1, Personaje personaje2,
                           PerfilJugador perfil1, PerfilJugador perfil2,
                           ConfiguracionIA configuracionIA,
                           String rutaEscenario, String rutaBalon,
                           boolean otorgarPuntos,
                           Consumer<Partido> alVolverTorneo) {
        boolean pvsM = configuracion != null
                && configuracion.getModalidad() == Modalidad.PvsM;
        boolean faltanPerfiles = pvsM
                ? configuracionIA == null
                : perfil2 == null;

        if (configuracion == null || personaje1 == null || personaje2 == null
                || perfil1 == null || faltanPerfiles
                || rutaEscenario == null || rutaBalon == null) {
            JOptionPane.showMessageDialog(
                    view,
                    "Faltan datos para iniciar el partido.",
                    "DOPOSoccer",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        this.configuracion = configuracion;
        this.personaje1 = personaje1;
        this.personaje2 = personaje2;
        this.perfil1 = perfil1;
        this.perfil2 = perfil2;
        this.configuracionIA = configuracionIA;
        this.rutaEscenario = rutaEscenario;
        this.rutaBalon = rutaBalon;
        this.otorgarPuntos = otorgarPuntos;
        this.modoTorneo = alVolverTorneo != null;
        this.alVolverTorneo = alVolverTorneo;

        crearPartido();
        prepararTeclas();

        ultimoTiempo = System.nanoTime();
        timer = new Timer(INTERVALO, e -> actualizar());
        timer.start();
        view.requestFocusInWindow();

        return true;
    }

    /**
     * Crea el partido con la configuración actual y lo muestra en la vista.
     */
    private void crearPartido() {
        if (pitoFinal != null) {
            pitoFinal.stop();
            pitoFinal = null;
        }
        if (musicaMenuFinal != null) {
            musicaMenuFinal.stop();
            musicaMenuFinal = null;
        }
        partido = new Partido(
                configuracion,
                personaje1,
                personaje2,
                perfil1.getConfiguracion(),
                configuracionJugador2(),
                perfilIA(),
                otorgarPuntos);
        golesAnteriores = 0;
        vidaAnterior1 = partido.getJugadores().get(0).getVida().getActual();
        vidaAnterior2 = partido.getJugadores().get(1).getVida().getActual();
        estadoAnterior = partido.getEstado();
        view.iniciar(partido, rutaEscenario, rutaBalon);
        view.setModoTorneo(modoTorneo);
    }

    /**
     * Devuelve la configuración del jugador 2 según la modalidad.
     *
     * @return la configuración del jugador 2
     */
    private ConfiguracionJugador configuracionJugador2() {
        if (esPvsM()) {
            return configuracionIA.crearConfiguracionJugador();
        }
        return perfil2.getConfiguracion();
    }

    /**
     * Devuelve el perfil de la máquina, o null si no juega la máquina.
     *
     * @return el perfil de la máquina, o null
     */
    private PerfilIA perfilIA() {
        return esPvsM() ? configuracionIA.getPerfil() : null;
    }

    /**
     * Indica si el partido es jugador contra máquina.
     *
     * @return true si se juega contra la máquina
     */
    private boolean esPvsM() {
        return configuracion != null
                && configuracion.getModalidad() == Modalidad.PvsM;
    }

    /**
     * Detiene el partido y libera sus temporizadores y oyentes.
     */
    public void detener() {
        if (pitoFinal != null) {
            pitoFinal.stop();
            pitoFinal = null;
        }
        if (musicaMenuFinal != null) {
            musicaMenuFinal.stop();
            musicaMenuFinal = null;
        }
        if (timer != null) {
            timer.stop();
            timer = null;
        }
        if (escuchaTeclas != null) {
            view.removeKeyListener(escuchaTeclas);
            escuchaTeclas = null;
        }
        teclas.clear();
        gui.restaurarMusicaMenu();
    }

    /**
     * Registra el oyente de teclado que controla a los jugadores.
     */
    private void prepararTeclas() {
        escuchaTeclas = new KeyAdapter() {
            @Override
            /**
             * Procesa las teclas presionadas para mover y hacer actuar a los
             * jugadores.
             *
             * @param e evento de teclado
             */
            public void keyPressed(KeyEvent e) {
                int codigo = e.getKeyCode();

                if (codigo == KeyEvent.VK_ESCAPE) {
                    alternarPausa();
                    return;
                }

                if (teclas.add(codigo)) {
                    if (codigo == tecla(1, ConfiguracionControles.Accion.SALTAR)) {
                        partido.saltar(1);
                    } else if (codigo == tecla(1, ConfiguracionControles.Accion.PATEAR)) {
                        partido.patear(1);
                        view.mostrarPatada(1);
                    } else if (!esPvsM() && codigo == tecla(2, ConfiguracionControles.Accion.SALTAR)) {
                        partido.saltar(2);
                    } else if (!esPvsM() && codigo == tecla(2, ConfiguracionControles.Accion.PATEAR)) {
                        partido.patear(2);
                        view.mostrarPatada(2);
                    } else if (codigo == teclaPoder(1)) {
                        partido.activarPoder(1);
                    } else if (!esPvsM() && codigo == teclaPoder(2)) {
                        partido.activarPoder(2);
                    }
                }
            }

            @Override
            /**
             * Quita la tecla liberada del conjunto de teclas activas.
             *
             * @param e evento de teclado
             */
            public void keyReleased(KeyEvent e) {
                teclas.remove(e.getKeyCode());
            }
        };

        view.addKeyListener(escuchaTeclas);
    }

    /**
     * Actualiza el partido en cada ciclo del temporizador.
     */
    private void actualizar() {
        if (partido == null) {
            return;
        }

        long ahora = System.nanoTime();
        double dt = (ahora - ultimoTiempo) / 1_000_000_000.0;
        ultimoTiempo = ahora;

        if (dt <= 0) {
            return;
        }
        if (dt > 0.05) {
            dt = 0.05;
        }

        // Mientras se muestra la imagen de gol, el partido queda congelado.
        if (view.isGolVisible()) {
            view.repaint();
            return;
        }

        if (partido.getEstado() != EstadoPartido.PAUSADO) {
            double paso1 = velocidadJugador(1) * dt;
            double paso2 = velocidadJugador(2) * dt;

            if (teclas.contains(tecla(1, ConfiguracionControles.Accion.IZQUIERDA))) {
                partido.mover(1, -paso1);
            }
            if (teclas.contains(tecla(1, ConfiguracionControles.Accion.DERECHA))) {
                partido.mover(1, paso1);
            }
            if (!esPvsM() && teclas.contains(tecla(2, ConfiguracionControles.Accion.IZQUIERDA))) {
                partido.mover(2, -paso2);
            }
            if (!esPvsM() && teclas.contains(tecla(2, ConfiguracionControles.Accion.DERECHA))) {
                partido.mover(2, paso2);
            }
        }

        partido.actualizar(dt);
        if (esPvsM() && partido.consumirPatada(2)) {
            view.mostrarPatada(2);
        }
        detectarEventosPartido();
        view.repaint();

        if (partido.getEstado() == EstadoPartido.TERMINADO) {
            timer.stop();
            mostrarResultado();
        }
    }

    /**
     * Reproduce los sonidos del partido y muestra la imagen de gol cuando
     * cambia el marcador, la vida de un jugador o termina el partido.
     */
    private void detectarEventosPartido() {
        Jugador jugador1 = partido.getJugadores().get(0);
        Jugador jugador2 = partido.getJugadores().get(1);

        int goles = partido.getMarcador().getGoles(1)
                + partido.getMarcador().getGoles(2);
        boolean huboGol = goles > golesAnteriores;
        if (huboGol) {
            gui.reproducirSonidoCorto(SONIDO_GOL);
            view.mostrarGol();
        }
        golesAnteriores = goles;

        double vida1 = jugador1.getVida().getActual();
        double vida2 = jugador2.getVida().getActual();
        if (vida1 < vidaAnterior1 - 0.01 || vida2 < vidaAnterior2 - 0.01) {
            gui.reproducirSonidoCorto(SONIDO_GOLPE);
        }
        vidaAnterior1 = vida1;
        vidaAnterior2 = vida2;

        EstadoPartido estado = partido.getEstado();
        if (estado == EstadoPartido.TERMINADO
                && estadoAnterior != EstadoPartido.TERMINADO) {
            if (huboGol) {
                // Deja que suene el gol antes del pito final.
                if (pitoFinal != null) {
                    pitoFinal.stop();
                }
                pitoFinal = new Timer(RETRASO_PITO_GOL, e -> {
                    pitoFinal = null;
                    gui.reproducirSonidoCorto(SONIDO_PITO_FINAL);
                });
                pitoFinal.setRepeats(false);
                pitoFinal.start();
                programarMusicaMenu(RETRASO_PITO_GOL + RETRASO_MUSICA_MENU);
            } else {
                gui.reproducirSonidoCorto(SONIDO_PITO_FINAL);
                programarMusicaMenu(RETRASO_MUSICA_MENU);
            }
        }
        estadoAnterior = estado;
    }

    /**
     * Cambia a la música de los menús cuando termina de sonar el pito final.
     *
     * @param retraso tiempo de espera en milisegundos
     */
    private void programarMusicaMenu(int retraso) {
        if (musicaMenuFinal != null) {
            musicaMenuFinal.stop();
        }
        musicaMenuFinal = new Timer(retraso, e -> {
            musicaMenuFinal = null;
            if (partido != null
                    && partido.getEstado() == EstadoPartido.TERMINADO) {
                gui.restaurarMusicaMenu();
            }
        });
        musicaMenuFinal.setRepeats(false);
        musicaMenuFinal.start();
    }

    /**
     * Calcula la velocidad actual de un jugador.
     *
     * @param jugador número del jugador
     * @return la velocidad del jugador
     */
    private double velocidadJugador(int jugador) {
        Jugador jugadorActual = partido.getJugadores().get(jugador - 1);
        return ParametrosJuego.VELOCIDAD_BASE_JUGADOR
                * jugadorActual.getEstadisticas().getFactorVelocidad()
                * jugadorActual.getMultiplicadorVelocidad();
    }

    /**
     * Devuelve la tecla asignada a una acción, invirtiéndola si el jugador
     * está bajo el efecto del Beso de la Fea.
     *
     * @param jugador número del jugador
     * @param accion acción que se quiere consultar
     * @return el código de la tecla asignada
     */
    private int tecla(int jugador, ConfiguracionControles.Accion accion) {
        ConfiguracionControles controles = gui.getConfiguracionControles();
        if (partido != null && partido.tieneControlesInvertidos(jugador)) {
            accion = invertirAccion(accion);
        }
        return controles.getTecla(jugador, accion);
    }

    /**
     * Devuelve la tecla asignada a la acción de poder de un jugador.
     *
     * @param jugador número del jugador
     * @return el código de la tecla de poder
     */
    private int teclaPoder(int jugador) {
        return gui.getConfiguracionControles().getTecla(
                jugador, ConfiguracionControles.Accion.PODER);
    }

    /**
     * Devuelve la acción contraria a la indicada.
     *
     * @param accion acción original
     * @return la acción invertida
     */
    private static ConfiguracionControles.Accion invertirAccion(
            ConfiguracionControles.Accion accion) {
        switch (accion) {
            case IZQUIERDA:
                return ConfiguracionControles.Accion.DERECHA;
            case DERECHA:
                return ConfiguracionControles.Accion.IZQUIERDA;
            case SALTAR:
                return ConfiguracionControles.Accion.PATEAR;
            case PATEAR:
                return ConfiguracionControles.Accion.SALTAR;
            default:
                return accion;
        }
    }

    /**
     * Pausa o reanuda el partido según su estado actual.
     */
    private void alternarPausa() {
        if (partido.getEstado() == EstadoPartido.PAUSADO) {
            reanudarPartido();
        } else if (partido.getEstado() == EstadoPartido.EN_CURSO
                || partido.getEstado() == EstadoPartido.LISTO) {
            pausarPartido();
        }
    }

    /**
     * Pausa el partido y muestra el menú de pausa.
     */
    private void pausarPartido() {
        partido.pausar();
        view.setPausaVisible(true);
        gui.restaurarMusicaMenu();
        view.repaint();
    }

    /**
     * Reanuda el partido y le devuelve el foco a la vista.
     */
    private void reanudarPartido() {
        partido.reanudar();
        view.setPausaVisible(false);
        gui.restaurarMusicaPartido();
        view.requestFocusInWindow();
        view.repaint();
    }

    /**
     * Reinicia el partido actual después de pedir confirmación.
     */
    private void reiniciarPartido() {
        if (modoTorneo) {
            return;
        }
        if (!confirmar("¿Reiniciar el partido?")) {
            return;
        }

        iniciarPartidoNuevo();
    }

    /**
     * Juega una revancha con la misma configuración.
     */
    private void revancha() {
        if (modoTorneo) {
            return;
        }
        iniciarPartidoNuevo();
    }

    /**
     * Crea un partido nuevo con la misma configuración y reinicia el ciclo.
     */
    private void iniciarPartidoNuevo() {
        if (modoTorneo) {
            return;
        }
        crearPartido();
        teclas.clear();
        ultimoTiempo = System.nanoTime();
        gui.restaurarMusicaPartido();
        if (timer != null) {
            timer.restart();
        }
        view.setPausaVisible(false);
        view.requestFocusInWindow();
        view.repaint();
    }

    /**
     * Abre la selección de personaje desde el resultado del partido.
     */
    private void cambiarPersonajeDesdeResultado() {
        if (modoTorneo) {
            return;
        }
        detener();
        gui.cambiarPersonajeDesdeJuego();
    }

    /**
     * Vuelve al menú desde el resultado del partido.
     */
    private void volverAlMenuDesdeResultado() {
        if (modoTorneo) {
            salirAlMenu();
            return;
        }
        detener();
        gui.volverAlMenuDesdeJuego();
    }

    /**
     * Cambia de personaje desde la pausa después de pedir confirmación.
     */
    private void cambiarPersonaje() {
        if (modoTorneo) {
            return;
        }
        if (!confirmar("¿Cambiar de personaje? La partida actual se reiniciará.")) {
            return;
        }

        detener();
        gui.cambiarPersonajeDesdeJuego();
    }

    /**
     * Pide confirmación y sale al menú principal.
     */
    private void salirAlMenu() {
        String mensaje = modoTorneo
                ? "Si sales al menú perderás el progreso del torneo, "
                        + "quedarás descalificado y perderás los puntos "
                        + "de inscripción.\n¿Salir al menú?"
                : "¿Salir al menú principal?";
        if (!confirmar(mensaje)) {
            return;
        }

        detener();
        if (modoTorneo) {
            gui.cancelarTorneo();
        }
        gui.volverAlMenuDesdeJuego();
    }

    /**
     * Vuelve a la selección visual desde el partido.
     */
    private void volverASeleccionVisual() {
        if (modoTorneo) {
            salirAlMenu();
            return;
        }
        detener();
        gui.mostrarConfiguracionVisual();
    }

    /**
     * Muestra un diálogo de confirmación con el mensaje indicado.
     *
     * @param mensaje texto de la pregunta
     * @return true si el usuario eligió sí
     */
    private boolean confirmar(String mensaje) {
        int opcion = JOptionPane.showConfirmDialog(
                view,
                mensaje,
                "Pausa",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        return opcion == JOptionPane.YES_OPTION;
    }

    /**
     * Muestra el resultado final del partido en la vista.
     */
    private void mostrarResultado() {
        view.mostrarResultado(partido, modoTorneo);
    }

    /**
     * Avisa al torneo del resultado del partido y vuelve a la llave.
     */
    private void volverAlTorneo() {
        if (alVolverTorneo == null) {
            return;
        }

        // Se limpia antes de llamar para que un doble clic no registre el
        // mismo resultado dos veces en la llave.
        Consumer<Partido> accion = alVolverTorneo;
        alVolverTorneo = null;

        Partido terminado = partido;
        detener();
        accion.accept(terminado);
    }
}
