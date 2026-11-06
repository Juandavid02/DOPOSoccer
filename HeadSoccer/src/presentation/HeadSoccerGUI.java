package presentation;

import javax.swing.*;
import java.awt.*;

public class HeadSoccerGUI extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;

    public HeadSoccerGUI() {
        super("HeadSoccer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setSize(screenSize.width, screenSize.height);  
        prepareView();
        prepareController();
    }

    public static void main(String[] args) {
        HeadSoccerGUI gui = new HeadSoccerGUI();
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