package presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class MenuView extends JPanel {

    private static final String RUTA_BOTONES = "resources/images/Botones/";

    /** Imagen de fondo escalada al tamaño del panel. */
    private BufferedImage fondo;
    private JButton botonGestionarProgreso;
    private JButton botonArcade;
    private JButton botonTorneo;
    private JButton botonConfiguracion;
    private JButton botonTienda;
    private JButton botonPerfil;
    private JButton botonSalir;

    private BotonSonido botonSonido;
    private JButton botonAtras;

    /**
     * Crea la pantalla del menú con el fondo y los botones principales.
     */
    public MenuView() {
        prepareView();
    }

    /**
     * Prepara la vista: el tamaño, el fondo, los botones y los íconos.
     */
    private void prepareView() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(1920, 1080));
        prepareViewFondo();
        prepareViewBotones();
        prepareViewIconos();
    }

    /**
     * Carga la imagen de fondo de la pantalla.
     */
    private void prepareViewFondo() {
        fondo = RecursosImagen.cargar("resources/images/Ventanas/FondoMenu.jpg");
    }

    /**
     * Prepara los botones principales en una grilla de dos columnas.
     */
    private void prepareViewBotones() {
        botonGestionarProgreso = crearBoton(
                RUTA_BOTONES + "GestionarProgreso.png", 380);
        botonArcade = crearBoton(RUTA_BOTONES + "Arcade.png", 380);
        botonTorneo = crearBoton(RUTA_BOTONES + "Torne.png", 380);
        botonConfiguracion = crearBoton(RUTA_BOTONES + "Configuracion.png", 380);
        botonTienda = crearBoton(RUTA_BOTONES + "Tienda.png", 380);
        botonPerfil = crearBoton(RUTA_BOTONES + "Perfil.png", 380);
        botonSalir = crearBoton(RUTA_BOTONES + "SalirMenu.png", 380);

        JPanel panelBotones = new JPanel(new GridLayout(3, 3, 24, 16));
        panelBotones.setOpaque(false);
        panelBotones.add(botonGestionarProgreso);
        panelBotones.add(botonArcade);
        panelBotones.add(botonTorneo);
        panelBotones.add(botonConfiguracion);
        panelBotones.add(botonTienda);
        panelBotones.add(botonPerfil);
        panelBotones.add(crearEspacioVacio());
        panelBotones.add(botonSalir);
        panelBotones.add(crearEspacioVacio());

        JPanel contenedorBotones = new JPanel(new GridBagLayout());
        contenedorBotones.setOpaque(false);
        contenedorBotones.setBorder(BorderFactory.createEmptyBorder(0, 0, 40, 0));
        contenedorBotones.add(panelBotones);

        add(contenedorBotones, BorderLayout.SOUTH);
    }

    /**
     * Crea un panel transparente para dejar un espacio vacío en la grilla.
     *
     * @return el panel vacío
     */
    private JPanel crearEspacioVacio() {
        JPanel espacio = new JPanel();
        espacio.setOpaque(false);
        return espacio;
    }

    /**
     * Prepara los botones de atrás (arriba a la izquierda) y de sonido
     * (arriba a la derecha).
     */
    private void prepareViewIconos() {
        botonAtras = crearBoton(RUTA_BOTONES + "Retroceder.png",
                VistaBase.ANCHO_BOTON_ATRAS);

        botonSonido = new BotonSonido(VistaBase.ANCHO_BOTON_SONIDO);

        JPanel panelIconos = new JPanel(new BorderLayout());
        panelIconos.setOpaque(false);
        panelIconos.setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 20));
        panelIconos.add(botonAtras, BorderLayout.WEST);
        panelIconos.add(botonSonido, BorderLayout.EAST);

        add(panelIconos, BorderLayout.NORTH);
    }

    /**
     * Crea un botón con imagen.
     *
     * @param ruta la ruta de la imagen del botón
     * @param ancho el ancho normal de la imagen
     * @return el botón con la imagen
     */
    private JButton crearBoton(String ruta, int ancho) {
        return BotonImagen.crear(ruta, ancho);
    }

    /**
     * Dibuja el fondo ocupando todo el panel.
     *
     * @param g contexto gráfico del componente
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);

        double escala = Math.max(
                (double) getWidth() / fondo.getWidth(),
                (double) getHeight() / fondo.getHeight());
        int ancho = (int) Math.ceil(fondo.getWidth() * escala);
        int alto = (int) Math.ceil(fondo.getHeight() * escala);

        g2.drawImage(fondo, (getWidth() - ancho) / 2,
                (getHeight() - alto) / 2, ancho, alto, null);
        g2.dispose();
    }

    /**
     * Devuelve el botón de gestionar progreso.
     *
     * @return el botón de gestionar progreso
     */
    public JButton getBotonGestionarProgreso() {
        return botonGestionarProgreso;
    }

    /**
     * Devuelve el botón de arcade.
     *
     * @return el botón de arcade
     */
    public JButton getBotonArcade() {
        return botonArcade;
    }

    /**
     * Devuelve el botón de torneo.
     *
     * @return el botón de torneo
     */
    public JButton getBotonTorneo() {
        return botonTorneo;
    }

    /**
     * Devuelve el botón de configuración.
     *
     * @return el botón de configuración
     */
    public JButton getBotonConfiguracion() {
        return botonConfiguracion;
    }

    /**
     * Devuelve el botón de tienda.
     *
     * @return el botón de tienda
     */
    public JButton getBotonTienda() {
        return botonTienda;
    }

    /**
     * Devuelve el botón de perfil.
     *
     * @return el botón de perfil
     */
    public JButton getBotonPerfil() {
        return botonPerfil;
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

    /**
     * Devuelve el botón de atrás.
     *
     * @return el botón de atrás
     */
    public JButton getBotonAtras() {
        return botonAtras;
    }

    /**
     * Devuelve el botón principal del menú.
     *
     * @return el botón principal del menú
     */
    public JButton getBotonPrincipal() {
        return botonArcade;
    }

}
