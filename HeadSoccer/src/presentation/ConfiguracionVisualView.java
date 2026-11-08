package presentation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class ConfiguracionVisualView extends JPanel {

    private static final String RUTA_BOTONES = "resources/images/Botones.java/";
    private static final Color COLOR_TEXTO = new Color(22, 48, 68);

    private BufferedImage fondo;
    private JButton botonAtras;
    private BotonSonido botonSonido;
    private JButton botonReady;

    private final List<TarjetaVisual> tarjetasBalon = new ArrayList<>();
    private final List<TarjetaVisual> tarjetasEscenario = new ArrayList<>();

    private TarjetaVisual balonSeleccionado;
    private TarjetaVisual escenarioSeleccionado;

    public ConfiguracionVisualView() {
        prepareView();
    }

    private void prepareView() {
        setLayout(new BorderLayout());
        setOpaque(true);
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(1920, 1080));

        fondo = RecursosImagen.cargar("resources/images/Ventanas/FondoSelecionPersonaje.jpg");

        prepareViewSuperior();
        prepareViewCentro();
    }

    private void prepareViewSuperior() {
        botonAtras = crearBotonImagen(RUTA_BOTONES + "Retroceder.png", 90);
        botonSonido = new BotonSonido(64);

        JPanel panelTitulo = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panelTitulo.setOpaque(false);
        panelTitulo.add(new TituloVisual("BALÓN Y ESCENARIO"));

        JPanel barraSuperior = new JPanel(new BorderLayout());
        barraSuperior.setOpaque(false);
        barraSuperior.setBorder(new EmptyBorder(18, 20, 0, 20));
        barraSuperior.add(botonAtras, BorderLayout.WEST);
        barraSuperior.add(panelTitulo, BorderLayout.CENTER);
        barraSuperior.add(botonSonido, BorderLayout.EAST);

        add(barraSuperior, BorderLayout.NORTH);
    }

    private void prepareViewCentro() {
        PanelTarjeta tarjeta = new PanelTarjeta();
        tarjeta.setLayout(new BorderLayout(0, 12));
        tarjeta.setBorder(new EmptyBorder(20, 26, 16, 26));
        tarjeta.setPreferredSize(new Dimension(1120, 560));

        tarjeta.add(prepararSecciones(), BorderLayout.CENTER);
        tarjeta.add(prepararReady(), BorderLayout.SOUTH);

        JPanel contenedor = new JPanel(new GridBagLayout());
        contenedor.setOpaque(false);
        contenedor.setBorder(new EmptyBorder(10, 0, 26, 0));
        contenedor.add(tarjeta);

        add(contenedor, BorderLayout.CENTER);
    }

    private JPanel prepararSecciones() {
        JPanel secciones = new JPanel(new GridBagLayout());
        secciones.setOpaque(false);

        GridBagConstraints c = new GridBagConstraints();
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 1;

        c.gridx = 0;
        c.weightx = 0.30;
        c.insets = new Insets(0, 0, 0, 24);
        secciones.add(prepararBalones(), c);

        c.gridx = 1;
        c.weightx = 0.70;
        c.insets = new Insets(0, 0, 0, 0);
        secciones.add(prepararEscenarios(), c);

        return secciones;
    }

    private JPanel prepararBalones() {
        JLabel titulo = crearEtiqueta("BALÓN");

        JPanel cuadricula = new JPanel(new GridLayout(3, 1, 8, 8));
        cuadricula.setOpaque(false);

        for (CatalogoVisual.Opcion opcion : CatalogoVisual.getBalones()) {
            TarjetaVisual tarjeta = new TarjetaVisual(opcion, true);
            tarjetasBalon.add(tarjeta);
            cuadricula.add(tarjeta);
        }

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titulo);
        panel.add(Box.createVerticalStrut(10));
        panel.add(cuadricula);

        return panel;
    }

    private JPanel prepararEscenarios() {
        JLabel titulo = crearEtiqueta("ESCENARIO");

        JPanel cuadricula = new JPanel(new GridLayout(2, 2, 10, 10));
        cuadricula.setOpaque(false);

        for (CatalogoVisual.Opcion opcion : CatalogoVisual.getEscenarios()) {
            TarjetaVisual tarjeta = new TarjetaVisual(opcion, false);
            tarjetasEscenario.add(tarjeta);
            cuadricula.add(tarjeta);
        }

        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setOpaque(false);
        panel.add(titulo, BorderLayout.NORTH);
        panel.add(cuadricula, BorderLayout.CENTER);

        return panel;
    }

    private JPanel prepararReady() {
        botonReady = crearBotonImagen(RUTA_BOTONES + "ReadyPerfil.png", 280);

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panel.setOpaque(false);
        panel.add(botonReady);
        return panel;
    }

    private static JLabel crearEtiqueta(String texto) {
        JLabel etiqueta = new JLabel(texto, SwingConstants.CENTER);
        etiqueta.setFont(new Font("SansSerif", Font.BOLD, 20));
        etiqueta.setForeground(COLOR_TEXTO);
        return etiqueta;
    }

    public void seleccionarBalon(TarjetaVisual tarjeta) {
        for (TarjetaVisual actual : tarjetasBalon) {
            actual.setSeleccionada(actual == tarjeta);
        }
        balonSeleccionado = tarjeta;
        repaint();
    }

    public void seleccionarEscenario(TarjetaVisual tarjeta) {
        for (TarjetaVisual actual : tarjetasEscenario) {
            actual.setSeleccionada(actual == tarjeta);
        }
        escenarioSeleccionado = tarjeta;
        repaint();
    }

    public void seleccionarIniciales() {
        if (!tarjetasBalon.isEmpty() && balonSeleccionado == null) {
            seleccionarBalon(tarjetasBalon.get(0));
        }
        if (!tarjetasEscenario.isEmpty() && escenarioSeleccionado == null) {
            seleccionarEscenario(tarjetasEscenario.get(0));
        }
    }

    public List<TarjetaVisual> getTarjetasBalon() {
        return tarjetasBalon;
    }

    public List<TarjetaVisual> getTarjetasEscenario() {
        return tarjetasEscenario;
    }

    public CatalogoVisual.Opcion getBalonSeleccionado() {
        if (balonSeleccionado == null) {
            return null;
        }
        return balonSeleccionado.getOpcion();
    }

    public CatalogoVisual.Opcion getEscenarioSeleccionado() {
        if (escenarioSeleccionado == null) {
            return null;
        }
        return escenarioSeleccionado.getOpcion();
    }

    public JButton getBotonReady() {
        return botonReady;
    }

    public JButton getBotonAtras() {
        return botonAtras;
    }

    public BotonSonido getBotonSonido() {
        return botonSonido;
    }

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

        g2.drawImage(fondo, (getWidth() - ancho) / 2, (getHeight() - alto) / 2,
                ancho, alto, null);
        g2.dispose();
    }

    private JButton crearBotonImagen(String ruta, int ancho) {
        BufferedImage imagen = RecursosImagen.cargar(ruta);

        ImageIcon normal = crearIcono(imagen, ancho);
        ImageIcon encima = crearIcono(imagen, ancho + Math.max(4, ancho * 7 / 100));
        ImageIcon presionado = crearIcono(imagen, ancho + Math.max(2, ancho * 3 / 100));

        JButton boton = new JButton(normal);
        boton.setRolloverIcon(encima);
        boton.setPressedIcon(presionado);
        boton.setRolloverEnabled(true);
        boton.setPreferredSize(new Dimension(encima.getIconWidth(), encima.getIconHeight()));
        boton.setContentAreaFilled(false);
        boton.setBorderPainted(false);
        boton.setFocusPainted(false);
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return boton;
    }

    private ImageIcon crearIcono(BufferedImage imagen, int ancho) {
        int alto = ancho * imagen.getHeight() / imagen.getWidth();
        return new ImageIcon(imagen.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH));
    }

    // ------------------------------------------------------------------
    // Tarjeta de balón o escenario.
    // ------------------------------------------------------------------
    public static final class TarjetaVisual extends JButton {

        private final CatalogoVisual.Opcion opcion;
        private final boolean esBalon;
        private final BufferedImage imagen;
        private boolean seleccionada;

        private TarjetaVisual(CatalogoVisual.Opcion opcion, boolean esBalon) {
            this.opcion = opcion;
            this.esBalon = esBalon;

            BufferedImage original = RecursosImagen.cargar(opcion.getRuta());
            if (esBalon) {
                imagen = recortar(original);
            } else {
                imagen = original;
            }

            setOpaque(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setRolloverEnabled(true);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        public CatalogoVisual.Opcion getOpcion() {
            return opcion;
        }

        private void setSeleccionada(boolean seleccionada) {
            this.seleccionada = seleccionada;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            int ancho = getWidth();
            int alto = getHeight();
            int arco = 18;
            RoundRectangle2D forma = new RoundRectangle2D.Float(
                    0, 0, ancho - 1, alto - 1, arco, arco);

            g2.setClip(forma);

            if (esBalon) {
                dibujarBalon(g2, ancho, alto);
            } else {
                dibujarEscenario(g2, ancho, alto);
            }

            if (getModel().isRollover()) {
                g2.setColor(new Color(255, 255, 255, 45));
                g2.fill(forma);
            }

            g2.setClip(null);

            if (seleccionada) {
                g2.setStroke(new BasicStroke(4f));
                g2.setColor(new Color(255, 210, 70));
            } else {
                g2.setStroke(new BasicStroke(2f));
                g2.setColor(new Color(30, 70, 90));
            }
            g2.draw(forma);

            g2.dispose();
        }

        private void dibujarBalon(Graphics2D g2, int ancho, int alto) {
            g2.setColor(new Color(246, 250, 252));
            g2.fillRect(0, 0, ancho, alto);

            int altoTexto = 24;
            int areaAlto = alto - altoTexto;
            double escala = Math.min(
                    (double) (ancho - 20) / imagen.getWidth(),
                    (double) (areaAlto - 10) / imagen.getHeight());
            int anchoImagen = (int) (imagen.getWidth() * escala);
            int altoImagen = (int) (imagen.getHeight() * escala);
            int x = (ancho - anchoImagen) / 2;
            int y = (areaAlto - altoImagen) / 2;

            g2.drawImage(imagen, x, y, anchoImagen, altoImagen, null);

            g2.setFont(new Font("SansSerif", Font.BOLD, 15));
            g2.setColor(COLOR_TEXTO);
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(opcion.getNombre(),
                    (ancho - fm.stringWidth(opcion.getNombre())) / 2,
                    alto - 7);
        }

        private void dibujarEscenario(Graphics2D g2, int ancho, int alto) {
            double escala = Math.max(
                    (double) ancho / imagen.getWidth(),
                    (double) alto / imagen.getHeight());
            int anchoImagen = (int) Math.ceil(imagen.getWidth() * escala);
            int altoImagen = (int) Math.ceil(imagen.getHeight() * escala);

            g2.drawImage(imagen, (ancho - anchoImagen) / 2, (alto - altoImagen) / 2,
                    anchoImagen, altoImagen, null);

            g2.setColor(new Color(0, 0, 0, 150));
            g2.fillRect(0, alto - 30, ancho, 30);

            g2.setFont(new Font("SansSerif", Font.BOLD, 15));
            g2.setColor(Color.WHITE);
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(opcion.getNombre(),
                    (ancho - fm.stringWidth(opcion.getNombre())) / 2,
                    alto - 9);
        }

        private static BufferedImage recortar(BufferedImage imagen) {
            int minX = imagen.getWidth();
            int minY = imagen.getHeight();
            int maxX = -1;
            int maxY = -1;

            for (int y = 0; y < imagen.getHeight(); y++) {
                for (int x = 0; x < imagen.getWidth(); x++) {
                    int alpha = (imagen.getRGB(x, y) >>> 24) & 0xff;
                    if (alpha > 10) {
                        if (x < minX) {
                            minX = x;
                        }
                        if (x > maxX) {
                            maxX = x;
                        }
                        if (y < minY) {
                            minY = y;
                        }
                        if (y > maxY) {
                            maxY = y;
                        }
                    }
                }
            }

            if (maxX < minX || maxY < minY) {
                return imagen;
            }

            return imagen.getSubimage(minX, minY, maxX - minX + 1, maxY - minY + 1);
        }
    }

    // ------------------------------------------------------------------
    // Componentes auxiliares de dibujo.
    // ------------------------------------------------------------------
    private static final class PanelTarjeta extends JPanel {

        private PanelTarjeta() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            int ancho = getWidth();
            int alto = getHeight();
            int arco = 38;

            g2.setPaint(new GradientPaint(
                    0, 0, new Color(236, 246, 252, 130),
                    0, alto, new Color(204, 228, 242, 100)));
            g2.fillRoundRect(0, 0, ancho - 1, alto - 1, arco, arco);

            g2.setStroke(new BasicStroke(2f));
            g2.setColor(new Color(255, 255, 255, 120));
            g2.drawRoundRect(1, 1, ancho - 3, alto - 3, arco, arco);

            g2.dispose();
        }
    }

    private static final class TituloVisual extends JComponent {

        private final String texto;
        private final Font fuente = new Font("SansSerif", Font.BOLD, 28);

        private TituloVisual(String texto) {
            this.texto = texto;
        }

        @Override
        public Dimension getPreferredSize() {
            FontMetrics fm = getFontMetrics(fuente);
            return new Dimension(fm.stringWidth(texto) + 80, 58);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            int ancho = getWidth();
            int alto = getHeight();
            int arco = alto;

            g2.setColor(new Color(0, 0, 0, 90));
            g2.fillRoundRect(4, 6, ancho - 8, alto - 10, arco, arco);

            g2.setPaint(new GradientPaint(
                    0, 0, new Color(78, 160, 198, 235),
                    0, alto, new Color(26, 82, 122, 235)));
            g2.fillRoundRect(2, 2, ancho - 4, alto - 8, arco, arco);

            g2.setStroke(new BasicStroke(2f));
            g2.setColor(new Color(255, 255, 255, 170));
            g2.drawRoundRect(4, 4, ancho - 9, alto - 13, arco, arco);

            g2.setFont(fuente);
            FontMetrics fm = g2.getFontMetrics();
            int x = (ancho - fm.stringWidth(texto)) / 2;
            int y = (alto - 6 - fm.getHeight()) / 2 + fm.getAscent();

            g2.setColor(new Color(16, 32, 46));
            for (int dx = -2; dx <= 2; dx++) {
                for (int dy = -2; dy <= 2; dy++) {
                    if (dx != 0 || dy != 0) {
                        g2.drawString(texto, x + dx, y + dy);
                    }
                }
            }
            g2.setColor(Color.WHITE);
            g2.drawString(texto, x, y);

            g2.dispose();
        }
    }
}
