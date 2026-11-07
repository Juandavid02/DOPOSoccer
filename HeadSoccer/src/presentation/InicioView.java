package presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class InicioView extends JPanel {

    private BufferedImage fondo; //IA generativa para poder cargar la imagen de fondo y luego poder escalarla al tamaño del panel
    private JButton botonPlay;
    private JButton botonSalir;

    /**
     * Crea la pantalla de inicio con el fondo y los botones Play y Salir.
     */
    public InicioView() {
        prepareView();
    }

    /**
     * Prepara la vista: el tamaño, el fondo y los botones.
     */
    private void prepareView() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(1920, 1080));
        prepareViewFondo();
        prepareViewBotones();
    }

    /**
     * Carga la imagen de fondo de la pantalla.
     */
    private void prepareViewFondo() {
        fondo = RecursosImagen.cargar("resources/images/Ventanas/FondoInicio.png");
    }

    /**
     * Prepara los botones Play y Salir en la parte de abajo de la pantalla.
     */
    private void prepareViewBotones() {
        botonPlay = crearBoton("resources/images/Botones.java/Play.png");
        botonSalir = crearBoton("resources/images/Botones.java/Salir.png");

        JPanel panelBotones = new JPanel();
        panelBotones.setOpaque(false);
        panelBotones.add(botonPlay);
        panelBotones.add(botonSalir);
        panelBotones.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        add(panelBotones, BorderLayout.SOUTH);
    }

    /**
     * Crea un botón con imagen. La imagen se agranda un poco cuando el
     * mouse está encima y se encoge un poco cuando se presiona.
     *
     * @param ruta la ruta de la imagen del botón
     * @return el botón con la imagen
     */
    private JButton crearBoton(String ruta) {
        BufferedImage imagen = RecursosImagen.cargar(ruta);

        ImageIcon iconoNormal = crearIcono(imagen, 280);
        ImageIcon iconoEncima = crearIcono(imagen, 300);
        ImageIcon iconoPresionado = crearIcono(imagen, 290);

        JButton boton = new JButton(iconoNormal);
        boton.setRolloverIcon(iconoEncima);
        boton.setPressedIcon(iconoPresionado);
        boton.setRolloverEnabled(true);
        boton.setPreferredSize(new Dimension(iconoEncima.getIconWidth(), iconoEncima.getIconHeight()));

        boton.setContentAreaFilled(false);
        boton.setBorderPainted(false);
        boton.setFocusPainted(false);
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return boton;
    }

    /**
     * Crea un ícono con el ancho dado, manteniendo la proporción de la imagen.
     *
     * @param imagen la imagen original
     * @param ancho el ancho que debe tener el ícono
     * @return el ícono ya cambiado de tamaño
     */
    private ImageIcon crearIcono(BufferedImage imagen, int ancho) {
        int alto = ancho * imagen.getHeight() / imagen.getWidth();
        return new ImageIcon(imagen.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH));
    }

    /**
     * Dibuja el fondo ocupando todo el panel.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
    }

    public JButton getBotonPlay() {
        return botonPlay;
    }

    public JButton getBotonSalir() {
        return botonSalir;
    }
}