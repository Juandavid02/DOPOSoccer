package presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class InicioView extends JPanel {

    /** Imagen de fondo escalada al tamaño del panel. */
    private BufferedImage fondo;
    private JButton botonPlay;
    private JButton botonSalir;
    private BotonSonido botonSonido;

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
        prepareViewSonido();
    }

    /**
     * Prepara el botón de sonido arriba a la derecha.
     */
    private void prepareViewSonido() {
        botonSonido = new BotonSonido(VistaBase.ANCHO_BOTON_SONIDO);

        JPanel panelSonido = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 16));
        panelSonido.setOpaque(false);
        panelSonido.add(botonSonido);

        add(panelSonido, BorderLayout.NORTH);
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
        botonPlay = crearBoton("resources/images/Botones/Play.png");
        botonSalir = crearBoton("resources/images/Botones/Salir.png");

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
        return BotonImagen.crear(ruta, 280);
    }

    /**
     * Dibuja el fondo ocupando todo el panel.
     *
     * @param g contexto gráfico del componente
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
    }

    /**
     * Devuelve el botón de jugar.
     *
     * @return el botón de jugar
     */
    public JButton getBotonPlay() {
        return botonPlay;
    }

    /**
     * Devuelve el botón de salir.
     *
     * @return el botón de salir
     */
    public JButton getBotonSalir() {
        return botonSalir;
    }

    /**
     * Devuelve el botón de sonido.
     *
     * @return el botón de sonido
     */
    public BotonSonido getBotonSonido() {
        return botonSonido;
    }
}