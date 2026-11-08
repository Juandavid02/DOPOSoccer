package presentation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ConfiguracionVisualView extends JPanel {

    private static final String RUTA_BOTONES = "resources/images/Botones.java/";

    private BufferedImage fondo;
    private JButton botonAtras;
    private BotonSonido botonSonido;

    public ConfiguracionVisualView() {
        prepareView();
    }

    private void prepareView() {
        setLayout(new BorderLayout());
        setOpaque(true);
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(1920, 1080));

        fondo = RecursosImagen.cargar("resources/images/Ventanas/FondoSelecionPersonaje.png");

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
        JLabel mensaje = new JLabel("Próximamente: selección de balón y escenario.",
                SwingConstants.CENTER);
        mensaje.setFont(new Font("SansSerif", Font.BOLD, 24));
        mensaje.setForeground(new Color(22, 48, 68));

        PanelTarjeta tarjeta = new PanelTarjeta();
        tarjeta.setLayout(new GridBagLayout());
        tarjeta.setBorder(new EmptyBorder(30, 40, 30, 40));
        tarjeta.setPreferredSize(new Dimension(760, 260));
        tarjeta.add(mensaje);

        JPanel contenedor = new JPanel(new GridBagLayout());
        contenedor.setOpaque(false);
        contenedor.setBorder(new EmptyBorder(10, 0, 30, 0));
        contenedor.add(tarjeta);

        add(contenedor, BorderLayout.CENTER);
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

        g2.drawImage(fondo, (getWidth() - ancho) / 2, (getHeight() - alto) / 2, ancho, alto, null);
        g2.dispose();
    }

    public JButton getBotonAtras() {
        return botonAtras;
    }

    public BotonSonido getBotonSonido() {
        return botonSonido;
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
