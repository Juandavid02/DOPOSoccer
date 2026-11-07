package presentation;

import javax.swing.*;
import java.awt.*;

public class DOPOSoccerGUI extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;

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
        DOPOSoccerGUI gui = new DOPOSoccerGUI();
        gui.setVisible(true);
    }

    private void prepareView() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        add(mainPanel);
    }

    private void prepareController() {
    
    }
}