package presentation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Base para las pantallas tipo menú o formulario. Resuelve el fondo escalado,
 * la barra superior con el botón atrás, el título y el botón de sonido.
 *
 * Las subclases solo construyen su contenido y llaman a setTitulo y
 * setCentro.
 */
public abstract class VistaBase extends JPanel {

    private static final String RUTA_BOTONES = "resources/images/Botones/";
    /** Tamaño uniforme del botón de volver en todas las pantallas. */
    public static final int ANCHO_BOTON_ATRAS = 90;
    /** Tamaño uniforme del botón de sonido en todas las pantallas. */
    public static final int ANCHO_BOTON_SONIDO = 64;

    private final BufferedImage fondo;
    private final JButton botonAtras;
    private final BotonSonido botonSonido;
    private final JPanel barraSuperior;
    private final JPanel contenedorCentro;
    private JPanel panelTitulo;

    /**
     * Crea la vista con el fondo indicado y la barra superior.
     *
     * @param rutaFondo ruta de la imagen de fondo
     */
    protected VistaBase(String rutaFondo) {
        this.fondo = RecursosImagen.cargar(rutaFondo);

        setLayout(new BorderLayout());
        setOpaque(true);
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(1920, 1080));

        botonAtras = BotonImagen.crear(RUTA_BOTONES + "Retroceder.png",
                ANCHO_BOTON_ATRAS);
        botonSonido = new BotonSonido(ANCHO_BOTON_SONIDO);

        barraSuperior = new JPanel(new BorderLayout());
        barraSuperior.setOpaque(false);
        barraSuperior.setBorder(new EmptyBorder(18, 20, 0, 20));
        barraSuperior.add(botonAtras, BorderLayout.WEST);
        barraSuperior.add(botonSonido, BorderLayout.EAST);

        contenedorCentro = new JPanel(new GridBagLayout());
        contenedorCentro.setOpaque(false);

        add(barraSuperior, BorderLayout.NORTH);
        add(contenedorCentro, BorderLayout.CENTER);
    }

    /**
     * Coloca el título en el centro de la barra superior.
     *
     * @param texto texto del título
     */
    protected final void setTitulo(String texto) {
        if (panelTitulo != null) {
            barraSuperior.remove(panelTitulo);
        }
        panelTitulo = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panelTitulo.setOpaque(false);
        panelTitulo.add(new TituloPantalla(texto));
        barraSuperior.add(panelTitulo, BorderLayout.CENTER);
        barraSuperior.revalidate();
        barraSuperior.repaint();
    }

    /**
     * Coloca un contenido centrado con márgenes superior e inferior.
     * Es lo que usan las pantallas con una tarjeta en el medio.
     *
     * @param contenido componente central
     * @param arriba margen superior en píxeles
     * @param abajo margen inferior en píxeles
     */
    protected final void setCentro(JComponent contenido, int arriba, int abajo) {
        contenedorCentro.removeAll();
        contenedorCentro.setBorder(new EmptyBorder(arriba, 0, abajo, 0));
        contenedorCentro.add(contenido);
        revalidate();
        repaint();
    }

    /**
     * Coloca un contenido que debe ocupar todo el centro, sin envolverlo en
     * un contenedor con GridBagLayout.
     *
     * @param contenido componente que rellena el centro
     */
    protected final void setCentroDirecto(JComponent contenido) {
        remove(contenedorCentro);
        add(contenido, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    /**
     * Punto de extensión para dibujar algo encima del fondo. Por defecto no
     * hace nada.
     *
     * @param g2 gráficos del panel
     */
    protected void pintarEncimaDeFondo(Graphics2D g2) {
    }

    /**
     * Dibuja el fondo escalado y el contenido extra de la subclase.
     *
     * @param g gráficos del componente
     */
    @Override
    protected final void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (fondo == null) {
            return;
        }

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
        pintarEncimaDeFondo(g2);
        g2.dispose();
    }

    /**
     * Devuelve el botón para volver al menú anterior.
     *
     * @return botón de atrás
     */
    public JButton getBotonAtras() {
        return botonAtras;
    }

    /**
     * Devuelve el botón para activar o desactivar el sonido.
     *
     * @return botón de sonido
     */
    public BotonSonido getBotonSonido() {
        return botonSonido;
    }
}
