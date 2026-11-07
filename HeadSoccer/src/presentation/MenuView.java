package presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class MenuView extends JPanel {

    private static final String RUTA_BOTONES = "resources/images/Botones.java/";

    private BufferedImage fondo; //IA generativa para poder cargar la imagen de fondo y luego poder escalarla al tamaño del panel
    private JButton botonContinuar;
    private JButton botonPvsP;
    private JButton botonPractica;
    private JButton botonConfiguracion;
    private JButton botonMejoras;
    private JButton botonPerfil;
    private JButton botonSalir;

    private JButton botonSonido;
    private JButton botonAtras;

    private ImageIcon[] iconosVolumenActivo;
    private ImageIcon[] iconosVolumenPausado;

    private boolean silenciado;

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
        botonContinuar = crearBoton(RUTA_BOTONES + "ContinuarPartida.png", 380);
        botonPvsP = crearBoton(RUTA_BOTONES + "PvsP.png", 380);
        botonPractica = crearBoton(RUTA_BOTONES + "PvsM.png", 380);
        botonConfiguracion = crearBoton(RUTA_BOTONES + "Configuracion.png", 380);
        botonMejoras = crearBoton(RUTA_BOTONES + "Mejoras.png", 380);
        botonPerfil = crearBoton(RUTA_BOTONES + "Perfil.png", 380);
        botonSalir = crearBoton(RUTA_BOTONES + "SalirMenu.png", 380);

        JPanel panelBotones = new JPanel(new GridLayout(3, 3, 24, 16));
        panelBotones.setOpaque(false);
        panelBotones.add(botonContinuar);
        panelBotones.add(botonPvsP);
        panelBotones.add(botonPractica);
        panelBotones.add(botonConfiguracion);
        panelBotones.add(botonMejoras);
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
        botonAtras = crearBoton(RUTA_BOTONES + "Retroceder.png", 110);

        iconosVolumenActivo = crearIconos(RUTA_BOTONES + "VolActivo.png", 72);
        iconosVolumenPausado = crearIconos(RUTA_BOTONES + "VolPausado.png", 72);
        botonSonido = crearBotonConIconos(iconosVolumenActivo);

        JPanel panelIconos = new JPanel(new BorderLayout());
        panelIconos.setOpaque(false);
        panelIconos.setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 20));
        panelIconos.add(botonAtras, BorderLayout.WEST);
        panelIconos.add(botonSonido, BorderLayout.EAST);

        add(panelIconos, BorderLayout.NORTH);
    }

    /**
     * Crea un botón con imagen. La imagen se agranda un poco cuando el
     * mouse está encima y se encoge un poco cuando se presiona.
     *
     * @param ruta la ruta de la imagen del botón
     * @param ancho el ancho normal de la imagen
     * @return el botón con la imagen
     */
    private JButton crearBoton(String ruta, int ancho) {
        return crearBotonConIconos(crearIconos(ruta, ancho));
    }

    /**
     * Crea los tres íconos de un botón: normal, con el mouse encima y
     * presionado.
     *
     * @param ruta la ruta de la imagen del botón
     * @param ancho el ancho normal de la imagen
     * @return los íconos normal, encima y presionado
     */
    private ImageIcon[] crearIconos(String ruta, int ancho) {
        BufferedImage imagen = RecursosImagen.cargar(ruta);

        int anchoEncima = ancho + Math.max(4, ancho * 7 / 100);
        int anchoPresionado = ancho + Math.max(2, ancho * 3 / 100);

        return new ImageIcon[]{crearIcono(imagen, ancho), crearIcono(imagen, anchoEncima), crearIcono(imagen, anchoPresionado)
        };
    }

    /**
     * Crea un botón con los íconos ya preparados.
     *
     * @param iconos los íconos normal, encima y presionado
     * @return el botón con los íconos
     */
    private JButton crearBotonConIconos(ImageIcon[] iconos) {
        JButton boton = new JButton(iconos[0]);
        boton.setRolloverIcon(iconos[1]);
        boton.setPressedIcon(iconos[2]);
        boton.setRolloverEnabled(true);
        boton.setPreferredSize(new Dimension(iconos[1].getIconWidth(), iconos[1].getIconHeight()));

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

    public JButton getBotonContinuar() {
        return botonContinuar;
    }

    public JButton getBotonPvsP() {
        return botonPvsP;
    }

    public JButton getBotonPractica() {
        return botonPractica;
    }

    public JButton getBotonConfiguracion() {
        return botonConfiguracion;
    }

    public JButton getBotonMejoras() {
        return botonMejoras;
    }

    public JButton getBotonPerfil() {
        return botonPerfil;
    }

    public JButton getBotonSalir() {
        return botonSalir;
    }

    public JButton getBotonSonido() {
        return botonSonido;
    }

    public JButton getBotonAtras() {
        return botonAtras;
    }

    public JButton getBotonPrincipal() {
        return botonPvsP;
    }

    public boolean isSilenciado() {
        return silenciado;
    }

    /**
     * Alterna el estado del sonido y actualiza el ícono del botón.
    */
    
    public void alternarSonido() {
        if (isSilenciado()) {
            silenciado = false;
            aplicarIconos(botonSonido, iconosVolumenActivo);
        } else {
            silenciado = true;
            aplicarIconos(botonSonido, iconosVolumenPausado);
        }
    }   
    /**
     * Pone en un botón los íconos normal, encima y presionado.
     *
     * @param boton el botón que se va a actualizar
     * @param iconos los íconos normal, encima y presionado
     */
    private void aplicarIconos(JButton boton, ImageIcon[] iconos) {
        boton.setIcon(iconos[0]);
        boton.setRolloverIcon(iconos[1]);
        boton.setPressedIcon(iconos[2]);
        boton.setPreferredSize(new Dimension(iconos[1].getIconWidth(), iconos[1].getIconHeight()));
        boton.revalidate();
        boton.repaint();
    }
}
