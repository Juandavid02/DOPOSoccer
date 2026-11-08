package presentation;

import domain.ConfiguracionPartido;
import domain.PerfilJugador;
import domain.Personaje;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DOPOSoccerGUI extends JFrame {

    private static final String CARTA_INICIO = "inicio";
    private static final String CARTA_MENU = "menu";
    private static final String CARTA_PERFIL = "perfil";
    private static final String CARTA_SELECCION_PERFIL = "seleccionPerfil";
    private static final String CARTA_CONFIGURACION = "configuracionPartido";
    private static final String CARTA_SELECCION_PERSONAJE = "seleccionPersonaje";
    private static final String CARTA_CONFIGURACION_VISUAL = "configuracionVisual";

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private InicioView inicioView;
    private MenuView menuView;
    private PerfilView perfilView;
    private PerfilController perfilController;
    private SeleccionPerfilView seleccionPerfilView;
    private SeleccionPerfilController seleccionPerfilController;
    private ConfiguracionPartidoView configuracionPartidoView;
    private SeleccionPersonajeView seleccionPersonajeView;
    private SeleccionPersonajeController seleccionPersonajeController;
    private ConfiguracionVisualView configuracionVisualView;
    private final List<PerfilJugador> perfiles = new ArrayList<>();
    private boolean perfilVuelveASeleccion;
    private final GestorSonido gestorSonido = new GestorSonido();
    private ConfiguracionPartido configuracionPartido;
    private Personaje personajeJugador1;
    private Personaje personajeJugador2;
    private String rutaBalonSeleccionado;
    private String rutaEscenarioSeleccionado;

    public DOPOSoccerGUI() {
        super("DOPOSoccer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setSize(screenSize.width, screenSize.height);
        prepareView();
        prepareController();
        gestorSonido.iniciarMusicaFondo("resources/audio/CancionDeFondo.wav");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DOPOSoccerGUI gui = new DOPOSoccerGUI();
            gui.setVisible(true);
        });
    }

    private void prepareView() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        add(mainPanel);

        prepareViewInicio();
        prepareViewMenu();
        prepareViewPerfil();
        prepareViewSeleccionPerfil();
        prepareViewConfiguracionPartido();
        prepareViewSeleccionPersonaje();
        prepareViewConfiguracionVisual();
        mostrarInicio();
    }

    private void prepareViewInicio() {
        inicioView = new InicioView();
        mainPanel.add(inicioView, CARTA_INICIO);
    }

    private void prepareViewMenu() {
        menuView = new MenuView();
        mainPanel.add(menuView, CARTA_MENU);
    }

    private void prepareViewPerfil() {
        perfilView = new PerfilView();
        mainPanel.add(perfilView, CARTA_PERFIL);
    }

    private void prepareViewSeleccionPerfil() {
        seleccionPerfilView = new SeleccionPerfilView();
        mainPanel.add(seleccionPerfilView, CARTA_SELECCION_PERFIL);
    }

    private void prepareViewConfiguracionPartido() {
        configuracionPartidoView = new ConfiguracionPartidoView();
        mainPanel.add(configuracionPartidoView, CARTA_CONFIGURACION);
    }

    private void prepareViewSeleccionPersonaje() {
        seleccionPersonajeView = new SeleccionPersonajeView();
        mainPanel.add(seleccionPersonajeView, CARTA_SELECCION_PERSONAJE);
    }

    private void prepareViewConfiguracionVisual() {
        configuracionVisualView = new ConfiguracionVisualView();
        mainPanel.add(configuracionVisualView, CARTA_CONFIGURACION_VISUAL);
    }

    private void prepareController() {
        new InicioController(this, inicioView);
        new MenuController(this, menuView);
        seleccionPerfilController = new SeleccionPerfilController(this, seleccionPerfilView, perfiles);
        perfilController = new PerfilController(this, perfilView, perfiles);
        new ConfiguracionPartidoController(this, configuracionPartidoView);
        seleccionPersonajeController = new SeleccionPersonajeController(this, seleccionPersonajeView);
        new ConfiguracionVisualController(this, configuracionVisualView);

        gestorSonido.agregarOyente(inicioView.getBotonSonido()::setSilenciado);
        gestorSonido.agregarOyente(menuView.getBotonSonido()::setSilenciado);
        gestorSonido.agregarOyente(perfilView.getBotonSonido()::setSilenciado);
        gestorSonido.agregarOyente(seleccionPerfilView.getBotonSonido()::setSilenciado);
        gestorSonido.agregarOyente(configuracionPartidoView.getBotonSonido()::setSilenciado);
        gestorSonido.agregarOyente(seleccionPersonajeView.getBotonSonido()::setSilenciado);
        gestorSonido.agregarOyente(configuracionVisualView.getBotonSonido()::setSilenciado);
    }

    public void mostrarInicio() {
        cardLayout.show(mainPanel, CARTA_INICIO);
        inicioView.getBotonPlay().requestFocusInWindow();
    }

    public void mostrarMenu() {
        cardLayout.show(mainPanel, CARTA_MENU);
        menuView.getBotonPrincipal().requestFocusInWindow();
    }

    public void iniciarSeleccionPerfil() {
        seleccionPerfilController.reiniciar();
        cardLayout.show(mainPanel, CARTA_SELECCION_PERFIL);
        seleccionPerfilView.repaint();
    }

    public void mostrarSeleccionPerfil() {
        seleccionPerfilController.refrescar();
        cardLayout.show(mainPanel, CARTA_SELECCION_PERFIL);
        seleccionPerfilView.repaint();
    }

    public void mostrarPerfil() {
        mostrarPerfil(false);
    }

    public void mostrarPerfilDesdeSeleccion() {
        mostrarPerfil(true);
    }

    private void mostrarPerfil(boolean volverASeleccion) {
        perfilVuelveASeleccion = volverASeleccion;
        perfilController.refrescar();
        cardLayout.show(mainPanel, CARTA_PERFIL);
        perfilView.repaint();
    }

    public void volverDePerfil() {
        if (perfilVuelveASeleccion) {
            mostrarSeleccionPerfil();
        } else {
            mostrarMenu();
        }
    }

    public void mostrarConfiguracionPartido() {
        cardLayout.show(mainPanel, CARTA_CONFIGURACION);
    }

    public void iniciarSeleccionPersonaje() {
        seleccionPersonajeController.reiniciar();
        cardLayout.show(mainPanel, CARTA_SELECCION_PERSONAJE);
        seleccionPersonajeView.repaint();
    }

    public void mostrarSeleccionPersonaje() {
        seleccionPersonajeController.refrescar();
        cardLayout.show(mainPanel, CARTA_SELECCION_PERSONAJE);
        seleccionPersonajeView.repaint();
    }

    public void mostrarConfiguracionVisual() {
        cardLayout.show(mainPanel, CARTA_CONFIGURACION_VISUAL);
        configuracionVisualView.repaint();
    }

    public void setPersonajes(Personaje personajeJugador1, Personaje personajeJugador2) {
        this.personajeJugador1 = personajeJugador1;
        this.personajeJugador2 = personajeJugador2;
    }

    public Personaje getPersonajeJugador1() {
        return personajeJugador1;
    }

    public Personaje getPersonajeJugador2() {
        return personajeJugador2;
    }

    public void setConfiguracionVisual(String rutaBalon, String rutaEscenario) {
        this.rutaBalonSeleccionado = rutaBalon;
        this.rutaEscenarioSeleccionado = rutaEscenario;
    }

    public String getRutaBalonSeleccionado() {
        return rutaBalonSeleccionado;
    }

    public String getRutaEscenarioSeleccionado() {
        return rutaEscenarioSeleccionado;
    }

    public void alternarSonido() {
        gestorSonido.alternarSilencio();
    }

    public void reproducirEfecto(String ruta) {
        gestorSonido.reproducirEfecto(ruta);
    }

    public void detenerSonido() {
        gestorSonido.detenerEfectos();
    }

    public void setConfiguracionPartido(ConfiguracionPartido configuracionPartido) {
        this.configuracionPartido = configuracionPartido;
    }

    public ConfiguracionPartido getConfiguracionPartido() {
        return configuracionPartido;
    }

    public void mostrarProximamente(String pantalla) {
        JOptionPane.showMessageDialog(
                this,
                "La pantalla \"" + pantalla + "\" estará disponible próximamente.",
                "DOPOSoccer",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
