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

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private InicioView inicioView;
    private MenuView menuView;
    private PerfilView perfilView;
    private PerfilController perfilController;
    private final List<PerfilJugador> perfiles = new ArrayList<>();

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

    private void prepareController() {
        new InicioController(this, inicioView);
        new MenuController(this, menuView);
        perfilController = new PerfilController(this, perfilView, perfiles);
    }

    public void mostrarInicio() {
        cardLayout.show(mainPanel, CARTA_INICIO);
        inicioView.getBotonPlay().requestFocusInWindow();
    }

    public void mostrarMenu() {
        cardLayout.show(mainPanel, CARTA_MENU);
        menuView.getBotonPrincipal().requestFocusInWindow();
    }

    public void mostrarPerfil() {
        perfilController.refrescar();
        cardLayout.show(mainPanel, CARTA_PERFIL);
        perfilView.repaint();
    }

    public void mostrarProximamente(String pantalla) {
        JOptionPane.showMessageDialog(
                this,
                "La pantalla \"" + pantalla + "\" estará disponible próximamente.",
                "DOPOSoccer",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
