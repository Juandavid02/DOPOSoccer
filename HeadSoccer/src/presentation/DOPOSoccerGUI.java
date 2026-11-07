package presentation;

import domain.PerfilJugador;

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

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private InicioView inicioView;
    private MenuView menuView;
    private PerfilView perfilView;
    private PerfilController perfilController;
    private SeleccionPerfilView seleccionPerfilView;
    private SeleccionPerfilController seleccionPerfilController;
    private ConfiguracionPartidoView configuracionPartidoView;
    private final List<PerfilJugador> perfiles = new ArrayList<>();
    private boolean perfilVuelveASeleccion;

    public DOPOSoccerGUI() {
        super("DOPOSoccer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setSize(screenSize.width, screenSize.height);
        prepareView();
        prepareController();
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

    private void prepareController() {
        new InicioController(this, inicioView);
        new MenuController(this, menuView);
        seleccionPerfilController = new SeleccionPerfilController(this, seleccionPerfilView, perfiles);
        perfilController = new PerfilController(this, perfilView, perfiles);
        new ConfiguracionPartidoController(this, configuracionPartidoView);
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

    public void mostrarProximamente(String pantalla) {
        JOptionPane.showMessageDialog(
                this,
                "La pantalla \"" + pantalla + "\" estará disponible próximamente.",
                "DOPOSoccer",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
