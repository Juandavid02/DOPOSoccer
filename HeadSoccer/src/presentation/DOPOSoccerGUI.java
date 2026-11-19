package presentation;

import domain.ConfiguracionIA;
import domain.ConfiguracionPartido;
import domain.ExcepcionDOPOSoccer;
import domain.Modalidad;
import domain.PartidaGuardada;
import domain.Partido;
import domain.PerfilJugador;
import domain.Personaje;
import domain.Tienda;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class DOPOSoccerGUI extends JFrame {

    private static final String CARTA_INICIO = "inicio";
    private static final String CARTA_MENU = "menu";
    private static final String CARTA_SELECCION_MODO = "seleccionModo";
    private static final String CARTA_PERFIL = "perfil";
    private static final String CARTA_GESTION_PROGRESO = "gestionProgreso";
    private static final String CARTA_SELECCION_PERFIL = "seleccionPerfil";
    private static final String CARTA_SELECCION_PERFIL_IA = "seleccionPerfilIA";
    private static final String CARTA_CONFIGURACION = "configuracionPartido";
    private static final String CARTA_SELECCION_PERSONAJE = "seleccionPersonaje";
    private static final String CARTA_CONFIGURACION_VISUAL = "configuracionVisual";
    private static final String CARTA_CONFIGURACION_JUEGO = "configuracionJuego";
    private static final String CARTA_TORNEO = "torneo";
    private static final String CARTA_MEJORAS = "mejoras";
    private static final String CARTA_JUEGO = "juego";
    private static final String MUSICA_MENU =
            "resources/audio/SonidosAmbiente/CancionDeFondo.wav";
    private static final String AMBIENTE_PARTIDO =
            "resources/audio/SonidosAmbiente/AmbienteEstadio.wav";

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private InicioView inicioView;
    private MenuView menuView;
    private SeleccionModoView seleccionModoView;
    private PerfilView perfilView;
    private PerfilController perfilController;
    private GestionProgresoView gestionProgresoView;
    private GestionProgresoController gestionProgresoController;
    private SeleccionPerfilView seleccionPerfilView;
    private SeleccionPerfilController seleccionPerfilController;
    private SeleccionPerfilIAView seleccionPerfilIAView;
    private ConfiguracionPartidoView configuracionPartidoView;
    private SeleccionPersonajeView seleccionPersonajeView;
    private SeleccionPersonajeController seleccionPersonajeController;
    private ConfiguracionVisualView configuracionVisualView;
    private ConfiguracionView configuracionView;
    private ConfiguracionController configuracionController;
    private MejorasView mejorasView;
    private MejorasController mejorasController;
    private TorneoView torneoView;
    private TorneoController torneoController;
    private boolean torneoPendiente;
    private boolean configuracionTorneo;
    private ConfiguracionControles configuracionControles = new ConfiguracionControles();
    private boolean configuracionVuelveAPausa;
    private final List<PerfilJugador> perfiles = new ArrayList<>();
    private boolean perfilVuelveASeleccion;
    private final GestorSonido gestorSonido = new GestorSonido();
    private final Tienda desbloqueos = new Tienda();
    private boolean cambiarPersonajeDesdeJuego;
    private ConfiguracionPartido configuracionPartido;
    private ConfiguracionIA configuracionIA;
    private Modalidad modalidadSeleccionada = Modalidad.PvsP;
    private Personaje personajeJugador1;
    private Personaje personajeJugador2;
    private PerfilJugador perfilPartido1;
    private PerfilJugador perfilPartido2;
    private String rutaBalonSeleccionado;
    private String rutaEscenarioSeleccionado;
    private JuegoView juegoView;
    private JuegoController juegoController;
    private Consumer<Boolean> oyenteSonidoJuego;

    /**
     * Crea la ventana principal, prepara las vistas y los controladores e
     * inicia la música de fondo.
     */
    public DOPOSoccerGUI() {
        super("DOPOSoccer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setSize(screenSize.width, screenSize.height);
        prepareView();
        prepareController();
        gestorSonido.iniciarMusicaFondo(MUSICA_MENU);
    }

    /**
     * Inicia la aplicación y muestra la ventana principal.
     *
     * @param args argumentos de la línea de comandos
     */
    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler((hilo, error) -> {
            error.printStackTrace();
            RegistroErrores.registrar("Error no controlado en la interfaz", error);
            String detalle = error.getMessage();
            if (detalle == null || detalle.isBlank()) {
                detalle = error.getClass().getSimpleName();
            }
            String mensaje = "Ocurrió un error inesperado: " + detalle;
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(
                    null,
                    mensaje,
                    "DOPOSoccer",
                    JOptionPane.ERROR_MESSAGE));
        });

        SwingUtilities.invokeLater(() -> {
            DOPOSoccerGUI gui = new DOPOSoccerGUI();
            gui.setVisible(true);
        });
    }

    /**
     * Prepara todas las vistas del juego y muestra la pantalla de inicio.
     */
    private void prepareView() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        add(mainPanel);

        prepareViewInicio();
        prepareViewMenu();
        prepareViewSeleccionModo();
        prepareViewPerfil();
        prepareViewGestionProgreso();
        prepareViewSeleccionPerfil();
        prepareViewSeleccionPerfilIA();
        prepareViewConfiguracionPartido();
        prepareViewSeleccionPersonaje();
        prepareViewConfiguracionVisual();
        prepareViewConfiguracionJuego();
        prepareViewMejoras();
        prepareViewTorneo();
        mostrarInicio();
    }

    /**
     * Crea la vista de inicio y la agrega al panel principal.
     */
    private void prepareViewInicio() {
        inicioView = new InicioView();
        mainPanel.add(inicioView, CARTA_INICIO);
    }

    /**
     * Crea la vista del menú y la agrega al panel principal.
     */
    private void prepareViewMenu() {
        menuView = new MenuView();
        mainPanel.add(menuView, CARTA_MENU);
    }

    /**
     * Crea la vista de selección de modo y la agrega al panel principal.
     */
    private void prepareViewSeleccionModo() {
        seleccionModoView = new SeleccionModoView();
        mainPanel.add(seleccionModoView, CARTA_SELECCION_MODO);
    }

    /**
     * Crea la vista de perfil y la agrega al panel principal.
     */
    private void prepareViewPerfil() {
        perfilView = new PerfilView();
        mainPanel.add(perfilView, CARTA_PERFIL);
    }

    /**
     * Crea la vista de gestión de progreso y la agrega al panel principal.
     */
    private void prepareViewGestionProgreso() {
        gestionProgresoView = new GestionProgresoView();
        mainPanel.add(gestionProgresoView, CARTA_GESTION_PROGRESO);
    }

    /**
     * Crea la vista de selección de perfil y la agrega al panel principal.
     */
    private void prepareViewSeleccionPerfil() {
        seleccionPerfilView = new SeleccionPerfilView();
        mainPanel.add(seleccionPerfilView, CARTA_SELECCION_PERFIL);
    }

    /**
     * Crea la vista de selección del perfil de la máquina y la agrega al
     * panel principal.
     */
    private void prepareViewSeleccionPerfilIA() {
        seleccionPerfilIAView = new SeleccionPerfilIAView();
        mainPanel.add(seleccionPerfilIAView, CARTA_SELECCION_PERFIL_IA);
    }

    /**
     * Crea la vista de configuración de partido y la agrega al panel
     * principal.
     */
    private void prepareViewConfiguracionPartido() {
        configuracionPartidoView = new ConfiguracionPartidoView();
        mainPanel.add(configuracionPartidoView, CARTA_CONFIGURACION);
    }

    /**
     * Crea la vista de selección de personaje y la agrega al panel principal.
     */
    private void prepareViewSeleccionPersonaje() {
        seleccionPersonajeView = new SeleccionPersonajeView();
        seleccionPersonajeView.setDesbloqueos(desbloqueos);
        mainPanel.add(seleccionPersonajeView, CARTA_SELECCION_PERSONAJE);
    }

    /**
     * Crea la vista de configuración visual y la agrega al panel principal.
     */
    private void prepareViewConfiguracionVisual() {
        configuracionVisualView = new ConfiguracionVisualView();
        configuracionVisualView.setDesbloqueos(desbloqueos);
        mainPanel.add(configuracionVisualView, CARTA_CONFIGURACION_VISUAL);
    }

    /**
     * Crea la vista de configuración del juego y la agrega al panel
     * principal.
     */
    private void prepareViewConfiguracionJuego() {
        configuracionView = new ConfiguracionView();
        mainPanel.add(configuracionView, CARTA_CONFIGURACION_JUEGO);
    }

    /**
     * Crea la vista de mejoras y la agrega al panel principal.
     */
    private void prepareViewMejoras() {
        mejorasView = new MejorasView();
        mejorasView.setDesbloqueos(desbloqueos);
        mainPanel.add(mejorasView, CARTA_MEJORAS);
    }

    /**
     * Crea la vista del torneo y la agrega al panel principal.
     */
    private void prepareViewTorneo() {
        torneoView = new TorneoView();
        mainPanel.add(torneoView, CARTA_TORNEO);
    }

    /**
     * Crea los controladores de cada vista y registra los oyentes de sonido.
     */
    private void prepareController() {
        new InicioController(this, inicioView);
        new MenuController(this, menuView);
        new SeleccionModoController(this, seleccionModoView);
        seleccionPerfilController = new SeleccionPerfilController(this, seleccionPerfilView, perfiles);
        new SeleccionPerfilIAController(this, seleccionPerfilIAView);
        perfilController = new PerfilController(this, perfilView, perfiles);
        gestionProgresoController = new GestionProgresoController(
                this, gestionProgresoView, perfiles);
        new ConfiguracionPartidoController(this, configuracionPartidoView);
        seleccionPersonajeController = new SeleccionPersonajeController(this, seleccionPersonajeView);
        new ConfiguracionVisualController(this, configuracionVisualView);
        configuracionController = new ConfiguracionController(this, configuracionView);
        mejorasController = new MejorasController(this, mejorasView, perfiles);
        torneoController = new TorneoController(this, torneoView);

        gestorSonido.agregarOyente(inicioView.getBotonSonido()::setSilenciado);
        gestorSonido.agregarOyente(menuView.getBotonSonido()::setSilenciado);
        gestorSonido.agregarOyente(seleccionModoView.getBotonSonido()::setSilenciado);
        gestorSonido.agregarOyente(perfilView.getBotonSonido()::setSilenciado);
        gestorSonido.agregarOyente(gestionProgresoView.getBotonSonido()::setSilenciado);
        gestorSonido.agregarOyente(seleccionPerfilView.getBotonSonido()::setSilenciado);
        gestorSonido.agregarOyente(seleccionPerfilIAView.getBotonSonido()::setSilenciado);
        gestorSonido.agregarOyente(configuracionPartidoView.getBotonSonido()::setSilenciado);
        gestorSonido.agregarOyente(seleccionPersonajeView.getBotonSonido()::setSilenciado);
        gestorSonido.agregarOyente(configuracionVisualView.getBotonSonido()::setSilenciado);
        gestorSonido.agregarOyente(configuracionView.getBotonSonido()::setSilenciado);
        gestorSonido.agregarOyente(mejorasView.getBotonSonido()::setSilenciado);
        gestorSonido.agregarOyente(torneoView.getBotonSonido()::setSilenciado);
    }

    /**
     * Muestra la pantalla de inicio y enfoca el botón de jugar.
     */
    public void mostrarInicio() {
        cardLayout.show(mainPanel, CARTA_INICIO);
        inicioView.getBotonPlay().requestFocusInWindow();
    }

    /**
     * Libera el partido anterior y muestra el menú principal.
     */
    public void mostrarMenu() {
        liberarJuegoAnterior();
        torneoPendiente = false;
        configuracionTorneo = false;
        if (seleccionPersonajeController != null) {
            seleccionPersonajeController.setSoloHumano(false);
        }
        if (torneoController != null) {
            torneoController.cancelar();
        }
        cardLayout.show(mainPanel, CARTA_MENU);
        menuView.getBotonPrincipal().requestFocusInWindow();
    }

    /**
     * Muestra la pantalla de selección de modo de juego.
     */
    public void mostrarSeleccionModo() {
        cardLayout.show(mainPanel, CARTA_SELECCION_MODO);
        seleccionModoView.repaint();
    }

    /**
     * Inicia la selección de perfiles para un partido entre dos jugadores.
     */
    public void iniciarSeleccionPerfil() {
        iniciarSeleccionPerfil(Modalidad.PvsP);
    }

    /**
     * Abre la selección de perfiles para la modalidad indicada. En PvsM solo
     * se elige el perfil del humano; la máquina se configura después.
     *
     * @param modalidad modalidad del partido
     */
    public void iniciarSeleccionPerfil(Modalidad modalidad) {
        if (modalidad == null) {
            modalidadSeleccionada = Modalidad.PvsP;
        } else {
            modalidadSeleccionada = modalidad;
        }
        configuracionIA = null;
        configuracionTorneo = false;
        seleccionPersonajeController.setSoloHumano(false);
        seleccionPerfilController.setModo(modalidadSeleccionada);
        seleccionPerfilController.reiniciar();
        cardLayout.show(mainPanel, CARTA_SELECCION_PERFIL);
        seleccionPerfilView.repaint();
    }

    /**
     * Inicia el flujo del torneo: perfil, inscripción, personaje y
     * configuración de los partidos.
     */
    public void iniciarTorneo() {
        torneoPendiente = true;
        configuracionIA = null;
        configuracionTorneo = false;
        seleccionPersonajeController.setSoloHumano(false);
        iniciarSeleccionPerfil(Modalidad.PvsM);
        torneoPendiente = true;
    }

    /**
     * Inicia la selección de personaje para el torneo.
     */
    public void iniciarSeleccionPersonajeTorneo() {
        cambiarPersonajeDesdeJuego = false;
        configuracionTorneo = false;
        seleccionPersonajeController.setSoloHumano(true);
        seleccionPersonajeController.reiniciar();
        cardLayout.show(mainPanel, CARTA_SELECCION_PERSONAJE);
        seleccionPersonajeView.repaint();
    }

    /**
     * Muestra la pantalla de inscripción del torneo.
     */
    public void mostrarInscripcionTorneo() {
        torneoPendiente = false;
        configuracionTorneo = false;
        torneoController.mostrarInscripcion();
        cardLayout.show(mainPanel, CARTA_TORNEO);
        torneoView.repaint();
    }

    /**
     * Muestra la pantalla de selección de personaje del torneo.
     */
    public void mostrarSeleccionPersonajeTorneo() {
        configuracionTorneo = false;
        cardLayout.show(mainPanel, CARTA_SELECCION_PERSONAJE);
        seleccionPersonajeView.repaint();
    }

    /**
     * Abre la configuración del torneo reutilizando la pantalla de
     * configuración de partido.
     */
    public void mostrarConfiguracionTorneo() {
        configuracionTorneo = true;
        configuracionPartidoView.setTituloPantalla("CONFIGURACIÓN DEL TORNEO");
        configuracionPartidoView.setModalidadTexto("TORNEO");
        configuracionPartidoView.setBotonReadySolo(true);
        cardLayout.show(mainPanel, CARTA_CONFIGURACION);
        configuracionPartidoView.repaint();
    }

    /**
     * Crea la llave del torneo con la configuración elegida y muestra la
     * pantalla de llaves.
     *
     * @param configuracion configuración elegida en la pantalla
     */
    public void crearTorneoDesdeConfiguracion(
            ConfiguracionPartido configuracion) {
        if (configuracion == null) {
            return;
        }
        torneoController.crearTorneo(
                configuracion.getTipo(),
                configuracion.getResolucion(),
                configuracion.getTiempo(),
                configuracion.getGoles());
    }

    /**
     * Indica si se está configurando un torneo.
     *
     * @return true si se está configurando un torneo
     */
    public boolean isConfiguracionTorneo() {
        return configuracionTorneo;
    }

    /**
     * Vuelve a la pantalla de inscripción del torneo.
     */
    public void volverAInscripcionTorneo() {
        configuracionTorneo = false;
        torneoController.volverAInscripcion();
    }

    /**
     * Cancela el torneo actual.
     */
    public void cancelarTorneo() {
        configuracionTorneo = false;
        if (torneoController != null) {
            torneoController.cancelar();
        }
    }

    /**
     * Muestra la pantalla de llaves del torneo.
     */
    public void mostrarTorneo() {
        configuracionTorneo = false;
        cardLayout.show(mainPanel, CARTA_TORNEO);
        torneoView.repaint();
    }

    /**
     * Indica si hay un torneo pendiente de iniciar.
     *
     * @return true si hay un torneo pendiente
     */
    public boolean isTorneoPendiente() {
        return torneoPendiente;
    }

    /**
     * Define si hay un torneo pendiente de iniciar.
     *
     * @param torneoPendiente true si hay un torneo pendiente
     */
    public void setTorneoPendiente(boolean torneoPendiente) {
        this.torneoPendiente = torneoPendiente;
    }

    /**
     * Define la modalidad de juego seleccionada.
     *
     * @param modalidad modalidad elegida
     */
    public void setModalidadSeleccionada(Modalidad modalidad) {
        if (modalidad == null) {
            this.modalidadSeleccionada = Modalidad.PvsP;
        } else {
            this.modalidadSeleccionada = modalidad;
        }
    }

    /**
     * Inicia la selección del perfil de la máquina.
     */
    public void iniciarSeleccionPerfilIA() {
        seleccionPerfilIAView.reiniciar();
        cardLayout.show(mainPanel, CARTA_SELECCION_PERFIL_IA);
        seleccionPerfilIAView.repaint();
    }

    /**
     * Muestra la pantalla de selección del perfil de la máquina.
     */
    public void mostrarSeleccionPerfilIA() {
        cardLayout.show(mainPanel, CARTA_SELECCION_PERFIL_IA);
        seleccionPerfilIAView.repaint();
    }

    /**
     * Actualiza y muestra la pantalla de selección de perfil.
     */
    public void mostrarSeleccionPerfil() {
        seleccionPerfilController.refrescar();
        cardLayout.show(mainPanel, CARTA_SELECCION_PERFIL);
        seleccionPerfilView.repaint();
    }

    /**
     * Muestra la pantalla de gestión de progreso.
     */
    public void mostrarGestionProgreso() {
        gestionProgresoController.abrirPantalla();
        cardLayout.show(mainPanel, CARTA_GESTION_PROGRESO);
        gestionProgresoView.repaint();
    }

    /**
     * Muestra la pantalla de perfil del jugador.
     */
    public void mostrarPerfil() {
        mostrarPerfil(false);
    }

    /**
     * Muestra la pantalla de perfil y permite volver a la selección.
     */
    public void mostrarPerfilDesdeSeleccion() {
        mostrarPerfil(true);
    }

    /**
     * Muestra la pantalla de perfil indicando a dónde se debe volver.
     *
     * @param volverASeleccion true para volver a la selección de perfiles
     */
    private void mostrarPerfil(boolean volverASeleccion) {
        perfilVuelveASeleccion = volverASeleccion;
        perfilController.refrescar();
        cardLayout.show(mainPanel, CARTA_PERFIL);
        perfilView.repaint();
    }

    /**
     * Vuelve a la pantalla anterior desde el perfil.
     */
    public void volverDePerfil() {
        if (perfilVuelveASeleccion) {
            mostrarSeleccionPerfil();
        } else {
            mostrarMenu();
        }
    }

    /**
     * Muestra la pantalla de configuración de partido.
     */
    public void mostrarConfiguracionPartido() {
        configuracionTorneo = false;
        configuracionPartidoView.setTituloPantalla("CONFIGURACIÓN DE PARTIDO");
        configuracionPartidoView.setModalidad(modalidadSeleccionada);
        cardLayout.show(mainPanel, CARTA_CONFIGURACION);
    }

    /**
     * Inicia la selección de personaje para un partido nuevo.
     */
    public void iniciarSeleccionPersonaje() {
        cambiarPersonajeDesdeJuego = false;
        configuracionTorneo = false;
        seleccionPersonajeController.setSoloHumano(false);
        seleccionPersonajeController.reiniciar();
        cardLayout.show(mainPanel, CARTA_SELECCION_PERSONAJE);
        seleccionPersonajeView.repaint();
    }

    /**
     * Muestra la pantalla de selección de personaje.
     */
    public void mostrarSeleccionPersonaje() {
        cambiarPersonajeDesdeJuego = false;
        seleccionPersonajeController.setSoloHumano(false);
        seleccionPersonajeController.refrescar();
        cardLayout.show(mainPanel, CARTA_SELECCION_PERSONAJE);
        seleccionPersonajeView.repaint();
    }

    /**
     * Abre la selección de personaje desde una partida en curso.
     */
    public void cambiarPersonajeDesdeJuego() {
        cambiarPersonajeDesdeJuego = true;
        seleccionPersonajeController.setSoloHumano(false);
        seleccionPersonajeController.reiniciar();
        cardLayout.show(mainPanel, CARTA_SELECCION_PERSONAJE);
        seleccionPersonajeView.repaint();
    }

    /**
     * Indica si se está cambiando el personaje desde una partida.
     *
     * @return true si el cambio viene desde el juego
     */
    public boolean isCambiarPersonajeDesdeJuego() {
        return cambiarPersonajeDesdeJuego;
    }

    /**
     * Marca como terminado el cambio de personaje desde el juego.
     */
    public void terminarCambioPersonaje() {
        cambiarPersonajeDesdeJuego = false;
    }

    /**
     * Muestra la pantalla de configuración visual del partido.
     */
    public void mostrarConfiguracionVisual() {
        configuracionVisualView.setBotonReadySolo(esPvsM());
        configuracionVisualView.setPerfilesActivos(
                perfilPartido1, perfilPartido2);
        configuracionVisualView.actualizarDesbloqueos();
        cardLayout.show(mainPanel, CARTA_CONFIGURACION_VISUAL);
        configuracionVisualView.repaint();
    }

    /**
     * Muestra la pantalla de configuración general del juego.
     */
    public void mostrarConfiguracion() {
        configuracionVuelveAPausa = false;
        configuracionController.abrir();
        cardLayout.show(mainPanel, CARTA_CONFIGURACION_JUEGO);
        configuracionView.repaint();
    }

    /**
     * Muestra la pantalla de mejoras.
     */
    public void mostrarMejoras() {
        mejorasController.abrir();
        cardLayout.show(mainPanel, CARTA_MEJORAS);
        mejorasView.repaint();
    }

    /**
     * Muestra la configuración del juego desde la pausa.
     */
    public void mostrarConfiguracionDesdePausa() {
        configuracionVuelveAPausa = true;
        configuracionController.abrir();
        cardLayout.show(mainPanel, CARTA_CONFIGURACION_JUEGO);
        configuracionView.repaint();
    }

    /**
     * Vuelve a la pausa o al menú desde la configuración.
     */
    public void volverDeConfiguracion() {
        if (configuracionVuelveAPausa && juegoView != null) {
            cardLayout.show(mainPanel, CARTA_JUEGO);
            juegoView.setPausaVisible(true);
            juegoView.requestFocusInWindow();
            juegoView.repaint();
        } else {
            mostrarMenu();
        }
    }

    /**
     * Devuelve el gestor de desbloqueos.
     *
     * @return el gestor de desbloqueos
     */
    public Tienda getDesbloqueos() {
        return desbloqueos;
    }

    /**
     * Devuelve la configuración de controles.
     *
     * @return la configuración de controles
     */
    public ConfiguracionControles getConfiguracionControles() {
        return configuracionControles;
    }

    /**
     * Define la configuración de controles.
     *
     * @param configuracionControles configuración de controles
     */
    public void setConfiguracionControles(ConfiguracionControles configuracionControles) {
        this.configuracionControles = new ConfiguracionControles(configuracionControles);
    }

    /**
     * Devuelve el volumen actual del sonido.
     *
     * @return el volumen actual
     */
    public int getVolumen() {
        return gestorSonido.getVolumen();
    }

    /**
     * Define el volumen del sonido.
     *
     * @param volumen volumen deseado
     */
    public void setVolumen(int volumen) {
        gestorSonido.setVolumen(volumen);
    }

    /**
     * Define los personajes que juegan el partido.
     *
     * @param personajeJugador1 personaje del jugador 1
     * @param personajeJugador2 personaje del jugador 2
     */
    public void setPersonajes(Personaje personajeJugador1, Personaje personajeJugador2) {
        this.personajeJugador1 = personajeJugador1;
        this.personajeJugador2 = personajeJugador2;
    }

    /**
     * Devuelve el personaje del jugador 1.
     *
     * @return el personaje del jugador 1
     */
    public Personaje getPersonajeJugador1() {
        return personajeJugador1;
    }

    /**
     * Define el balón y el escenario seleccionados.
     *
     * @param rutaBalon ruta de la imagen del balón
     * @param rutaEscenario ruta de la imagen del escenario
     */
    public void setConfiguracionVisual(String rutaBalon, String rutaEscenario) {
        this.rutaBalonSeleccionado = rutaBalon;
        this.rutaEscenarioSeleccionado = rutaEscenario;
    }

    /**
     * Define los perfiles que juegan el partido.
     *
     * @param perfil1 perfil del jugador 1
     * @param perfil2 perfil del jugador 2
     */
    public void setPerfilesPartido(PerfilJugador perfil1, PerfilJugador perfil2) {
        this.perfilPartido1 = perfil1;
        this.perfilPartido2 = perfil2;
    }

    /**
     * Devuelve el perfil del jugador 1.
     *
     * @return el perfil del jugador 1
     */
    public PerfilJugador getPerfilPartido1() {
        return perfilPartido1;
    }

    /**
     * Devuelve el perfil del jugador 2.
     *
     * @return el perfil del jugador 2
     */
    public PerfilJugador getPerfilPartido2() {
        return perfilPartido2;
    }

    /**
     * Devuelve la ruta del balón seleccionado.
     *
     * @return la ruta del balón seleccionado
     */
    public String getRutaBalonSeleccionado() {
        return rutaBalonSeleccionado;
    }

    /**
     * Devuelve la ruta del escenario seleccionado.
     *
     * @return la ruta del escenario seleccionado
     */
    public String getRutaEscenarioSeleccionado() {
        return rutaEscenarioSeleccionado;
    }

    /**
     * Abre una partida guardada en un archivo .dat. Si el perfil guardado no
     * existe se recrea; si existe, se conserva el actual y se unen sus
     * desbloqueos.
     */
    public void continuarPartida() {
        JFileChooser selector = new JFileChooser();
        selector.setDialogTitle("Continuar partido");
        selector.setFileFilter(new FileNameExtensionFilter(
                "Partida de DOPOSoccer (*.dat)", "dat"));
        if (selector.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File archivo = selector.getSelectedFile();
        if (archivo == null) {
            return;
        }

        try {
            PartidaGuardada guardada = PartidaGuardada.open(archivo);
            PerfilJugador vivo1 = resolverPerfil(guardada.getPerfil1());
            PerfilJugador vivo2 = resolverPerfil(guardada.getPerfil2());
            iniciarJuegoCargado(guardada, vivo1, vivo2);
        } catch (ExcepcionDOPOSoccer e) {
            RegistroErrores.registrar("Error al abrir la partida", e);
            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage() + "\n\nDetalle técnico en "
                            + RegistroErrores.getRutaArchivo(),
                    "Continuar partido",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Busca el perfil guardado entre los perfiles en memoria. Si existe,
     * conserva el perfil actual y le une los desbloqueos guardados. Si no
     * existe, lo agrega a la lista.
     *
     * @param guardado perfil leído del archivo, o null
     * @return perfil vivo que se usará en la partida, o null
     */
    private PerfilJugador resolverPerfil(PerfilJugador guardado) {
        if (guardado == null) {
            return null;
        }
        for (PerfilJugador perfil : perfiles) {
            if (perfil.getId().equals(guardado.getId())) {
                unirDesbloqueos(perfil, guardado);
                return perfil;
            }
        }
        perfiles.add(guardado);
        return guardado;
    }

    /**
     * Agrega al perfil destino los desbloqueos del perfil guardado. Nunca
     * quita desbloqueos.
     *
     * @param destino perfil actual
     * @param guardado perfil leído del archivo
     */
    private void unirDesbloqueos(PerfilJugador destino, PerfilJugador guardado) {
        for (String nombre : guardado.getPersonajesDesbloqueados()) {
            destino.desbloquearPersonaje(nombre);
        }
        for (String nombre : guardado.getBalonesDesbloqueados()) {
            destino.desbloquearBalon(nombre);
        }
        for (String nombre : guardado.getEscenariosDesbloqueados()) {
            destino.desbloquearEscenario(nombre);
        }
    }

    /**
     * Inicia un partido normal con la configuración actual.
     */
    public void iniciarJuego() {
        iniciarJuego(true, null);
    }

    /**
     * Inicia un partido de torneo. No reparte puntos por partido y al
     * terminar avisa al torneo para volver a la llave.
     *
     * @param alVolverTorneo acción a ejecutar al salir del resultado
     */
    public void iniciarJuegoTorneo(Consumer<Partido> alVolverTorneo) {
        iniciarJuego(false, alVolverTorneo);
    }

    /**
     * Inicia un partido con las opciones indicadas.
     *
     * @param otorgarPuntos true si se otorgan puntos al ganar
     * @param alVolverTorneo acción a ejecutar al terminar, o null
     */
    private void iniciarJuego(boolean otorgarPuntos,
                              Consumer<Partido> alVolverTorneo) {
        liberarJuegoAnterior();
        prepararJuegoView();

        boolean iniciado = juegoController.iniciar(
                configuracionPartido,
                personajeJugador1,
                personajeJugador2,
                perfilPartido1,
                perfilPartido2,
                configuracionIA,
                rutaEscenarioSeleccionado,
                rutaBalonSeleccionado,
                otorgarPuntos,
                alVolverTorneo);

        if (!iniciado) {
            liberarJuegoAnterior();
            return;
        }

        cardLayout.show(mainPanel, CARTA_JUEGO);
        gestorSonido.cambiarMusicaFondo(AMBIENTE_PARTIDO);
        juegoView.requestFocusInWindow();
    }

    /**
     * Crea la vista y el controlador del partido y los conecta al sonido.
     */
    private void prepararJuegoView() {
        juegoView = new JuegoView();
        juegoController = new JuegoController(this, juegoView);
        mainPanel.add(juegoView, CARTA_JUEGO);
        oyenteSonidoJuego = juegoView.getBotonSonido()::setSilenciado;
        gestorSonido.agregarOyente(oyenteSonidoJuego);
    }

    /**
     * Inicia un partido cargado desde un archivo.
     *
     * @param guardada partida guardada
     * @param vivo1 perfil actual del jugador 1
     * @param vivo2 perfil actual del jugador 2, o null en PvsM
     */
    private void iniciarJuegoCargado(PartidaGuardada guardada,
                                     PerfilJugador vivo1,
                                     PerfilJugador vivo2) {
        liberarJuegoAnterior();
        prepararJuegoView();

        boolean iniciado = juegoController.iniciarDesdePartida(
                guardada, vivo1, vivo2);
        if (!iniciado) {
            liberarJuegoAnterior();
            return;
        }

        cardLayout.show(mainPanel, CARTA_JUEGO);
        gestorSonido.cambiarMusicaFondo(MUSICA_MENU);
        juegoView.requestFocusInWindow();
    }

    /**
     * Detiene el partido actual y lo saca de la ventana, junto con su oyente
     * de sonido, para no acumular vistas viejas.
     */
    private void liberarJuegoAnterior() {
        if (oyenteSonidoJuego != null) {
            gestorSonido.quitarOyente(oyenteSonidoJuego);
            oyenteSonidoJuego = null;
        }
        if (juegoController != null) {
            juegoController.detener();
            juegoController = null;
        }
        if (juegoView != null) {
            mainPanel.remove(juegoView);
            juegoView = null;
        }
    }

    /**
     * Vuelve al menú principal desde el juego.
     */
    public void volverAlMenuDesdeJuego() {
        mostrarMenu();
    }

    /**
     * Activa o desactiva el sonido del juego.
     */
    public void alternarSonido() {
        gestorSonido.alternarSilencio();
    }

    /**
     * Cambia la música de fondo a la del menú.
     */
    public void restaurarMusicaMenu() {
        gestorSonido.cambiarMusicaFondo(MUSICA_MENU);
    }

    /**
     * Cambia la música de fondo a la del partido.
     */
    public void restaurarMusicaPartido() {
        gestorSonido.cambiarMusicaFondo(AMBIENTE_PARTIDO);
    }

    /**
     * Reproduce un efecto de sonido.
     *
     * @param ruta ruta del archivo de sonido
     */
    public void reproducirEfecto(String ruta) {
        gestorSonido.reproducirEfecto(ruta);
    }

    /**
     * Reproduce un sonido corto.
     *
     * @param ruta ruta del archivo de sonido
     */
    public void reproducirSonidoCorto(String ruta) {
        gestorSonido.reproducirSonidoCorto(ruta);
    }

    /**
     * Detiene los efectos de sonido activos.
     */
    public void detenerSonido() {
        gestorSonido.detenerEfectos();
    }

    /**
     * Define la configuración del partido.
     *
     * @param configuracionPartido configuración del partido
     */
    public void setConfiguracionPartido(ConfiguracionPartido configuracionPartido) {
        this.configuracionPartido = configuracionPartido;
    }

    /**
     * Define la configuración de la máquina.
     *
     * @param configuracionIA configuración de la máquina
     */
    public void setConfiguracionIA(ConfiguracionIA configuracionIA) {
        this.configuracionIA = configuracionIA;
    }

    /**
     * Devuelve la configuración de la máquina.
     *
     * @return la configuración de la máquina
     */
    public ConfiguracionIA getConfiguracionIA() {
        return configuracionIA;
    }

    /**
     * Devuelve la modalidad de juego seleccionada.
     *
     * @return la modalidad seleccionada
     */
    public Modalidad getModalidadSeleccionada() {
        return modalidadSeleccionada;
    }

    /**
     * Indica si la modalidad seleccionada es jugador contra máquina.
     *
     * @return true si se juega contra la máquina
     */
    public boolean esPvsM() {
        return modalidadSeleccionada == Modalidad.PvsM;
    }
}
