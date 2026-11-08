package presentation;

import domain.Resolucion;
import domain.TipoPartido;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ConfiguracionPartidoView extends JPanel {

    private static final String RUTA_BOTONES = "resources/images/Botones.java/";
    private static final Color COLOR_TEXTO = new Color(22, 48, 68);
    private static final Color COLOR_ETIQUETA = new Color(30, 62, 86);

    private static final int TIEMPO_INICIAL = 3;
    private static final int GOLES_INICIALES = 5;
    private static final int MINIMO = 1;
    private static final int MAXIMO = 10;

    private BufferedImage fondo;

    private JButton botonAtras;
    private BotonSonido botonSonido;
    private JButton botonReady;

    private BotonOpcion botonTipoTiempo;
    private BotonOpcion botonTipoGoles;

    private JLabel valorTiempo;
    private JLabel valorGoles;
    private BotonPaso botonTiempoMenos;
    private BotonPaso botonTiempoMas;
    private BotonPaso botonGolesMenos;
    private BotonPaso botonGolesMas;

    private JComboBox<String> comboResolucion;

    private int tiempo = TIEMPO_INICIAL;
    private int goles = GOLES_INICIALES;

    public ConfiguracionPartidoView() {
        prepareView();
    }

    private void prepareView() {
        setLayout(new BorderLayout());
        setOpaque(true);
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(1920, 1080));

        fondo = RecursosImagen.cargar("resources/images/Ventanas/FondoPerfil.png");

        prepareViewSuperior();
        prepareViewCentro();
        actualizarControles();
    }

    private void prepareViewSuperior() {
        botonAtras = crearBotonImagen(RUTA_BOTONES + "Retroceder.png", 90);
        botonSonido = new BotonSonido(64);

        JPanel panelTitulo = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panelTitulo.setOpaque(false);
        panelTitulo.add(new TituloConfiguracion("CONFIGURACIÓN DE PARTIDO"));

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
        tarjeta.setLayout(new BorderLayout(0, 18));
        tarjeta.setBorder(new EmptyBorder(28, 40, 24, 40));
        tarjeta.setPreferredSize(new Dimension(860, 500));

        tarjeta.add(prepararFormulario(), BorderLayout.CENTER);
        tarjeta.add(prepararBotonReady(), BorderLayout.SOUTH);

        JPanel contenedor = new JPanel(new GridBagLayout());
        contenedor.setOpaque(false);
        contenedor.setBorder(new EmptyBorder(10, 0, 30, 0));
        contenedor.add(tarjeta);

        add(contenedor, BorderLayout.CENTER);
    }

    private JPanel prepararFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);

        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(8, 0, 8, 20);
        c.gridx = 0;

        int fila = 0;

        c.gridy = fila;
        panel.add(crearEtiqueta("MODALIDAD"), c);

        JLabel modalidad = new JLabel("PvsP");
        modalidad.setFont(new Font("SansSerif", Font.BOLD, 20));
        modalidad.setForeground(COLOR_TEXTO);
        c.gridx = 1;
        c.gridwidth = 3;
        panel.add(modalidad, c);

        fila++;
        c.gridx = 0;
        c.gridwidth = 1;
        c.gridy = fila;
        panel.add(crearEtiqueta("TIPO DE PARTIDO"), c);

        botonTipoTiempo = new BotonOpcion("TIEMPO");
        botonTipoGoles = new BotonOpcion("GOLES");
        botonTipoTiempo.setSelected(true);

        ButtonGroup grupo = new ButtonGroup();
        grupo.add(botonTipoTiempo);
        grupo.add(botonTipoGoles);

        JPanel panelTipo = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelTipo.setOpaque(false);
        panelTipo.add(botonTipoTiempo);
        panelTipo.add(botonTipoGoles);

        c.gridx = 1;
        c.gridwidth = 3;
        panel.add(panelTipo, c);

        fila++;
        c.gridx = 0;
        c.gridwidth = 1;
        c.gridy = fila;
        panel.add(crearEtiqueta("TIEMPO"), c);

        botonTiempoMenos = new BotonPaso("-");
        botonTiempoMas = new BotonPaso("+");
        valorTiempo = crearValor();
        panel.add(prepararControl(botonTiempoMenos, valorTiempo, botonTiempoMas), constraint(c, 1, 3));

        fila++;
        c.gridx = 0;
        c.gridwidth = 1;
        c.gridy = fila;
        panel.add(crearEtiqueta("GOLES"), c);

        botonGolesMenos = new BotonPaso("-");
        botonGolesMas = new BotonPaso("+");
        valorGoles = crearValor();
        panel.add(prepararControl(botonGolesMenos, valorGoles, botonGolesMas), constraint(c, 1, 3));

        fila++;
        c.gridx = 0;
        c.gridwidth = 1;
        c.gridy = fila;
        panel.add(crearEtiqueta("RESOLUCIÓN"), c);

        comboResolucion = new JComboBox<>(new String[]{
                "1280 x 720", "1600 x 900", "1920 x 1080"
        });
        comboResolucion.setFont(new Font("SansSerif", Font.BOLD, 17));
        comboResolucion.setBackground(Color.WHITE);
        comboResolucion.setForeground(COLOR_TEXTO);
        c.gridx = 1;
        c.gridwidth = 3;
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(comboResolucion, c);

        fila++;
        c.gridy = fila;
        c.gridx = 0;
        c.gridwidth = 4;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        JPanel relleno = new JPanel();
        relleno.setOpaque(false);
        panel.add(relleno, c);

        botonTiempoMenos.addActionListener(e -> cambiarTiempo(-1));
        botonTiempoMas.addActionListener(e -> cambiarTiempo(1));
        botonGolesMenos.addActionListener(e -> cambiarGoles(-1));
        botonGolesMas.addActionListener(e -> cambiarGoles(1));
        botonTipoTiempo.addActionListener(e -> actualizarControles());
        botonTipoGoles.addActionListener(e -> actualizarControles());

        return panel;
    }

    private GridBagConstraints constraint(GridBagConstraints base, int x, int gridwidth) {
        GridBagConstraints c = (GridBagConstraints) base.clone();
        c.gridx = x;
        c.gridwidth = gridwidth;
        c.fill = GridBagConstraints.NONE;
        return c;
    }

    private JPanel prepararControl(JButton menos, JLabel valor, JButton mas) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        panel.setOpaque(false);
        panel.add(menos);
        panel.add(valor);
        panel.add(mas);
        return panel;
    }

    private JPanel prepararBotonReady() {
        botonReady = crearBotonImagen(RUTA_BOTONES + "ReadyPerfil.png", 280);

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panel.setOpaque(false);
        panel.add(botonReady);
        return panel;
    }

    private static JLabel crearEtiqueta(String texto) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(new Font("SansSerif", Font.BOLD, 17));
        etiqueta.setForeground(COLOR_ETIQUETA);
        return etiqueta;
    }

    private static JLabel crearValor() {
        JLabel valor = new JLabel("", SwingConstants.CENTER);
        valor.setFont(new Font("SansSerif", Font.BOLD, 19));
        valor.setForeground(COLOR_TEXTO);
        valor.setPreferredSize(new Dimension(110, 30));
        return valor;
    }

    private void cambiarTiempo(int delta) {
        tiempo = Math.max(MINIMO, Math.min(MAXIMO, tiempo + delta));
        actualizarControles();
    }

    private void cambiarGoles(int delta) {
        goles = Math.max(MINIMO, Math.min(MAXIMO, goles + delta));
        actualizarControles();
    }

    private void actualizarControles() {
        boolean esTiempo = botonTipoTiempo.isSelected();

        String textoTiempo;
        if (tiempo == 1) {
            textoTiempo = tiempo + " minuto";
        } else {
            textoTiempo = tiempo + " minutos";
        }

        String textoGoles;
        if (goles == 1) {
            textoGoles = goles + " gol";
        } else {
            textoGoles = goles + " goles";
        }

        valorTiempo.setText(textoTiempo);
        valorGoles.setText(textoGoles);

        botonTiempoMenos.setEnabled(esTiempo && tiempo > MINIMO);
        botonTiempoMas.setEnabled(esTiempo && tiempo < MAXIMO);
        botonGolesMenos.setEnabled(!esTiempo && goles > MINIMO);
        botonGolesMas.setEnabled(!esTiempo && goles < MAXIMO);

        valorTiempo.setEnabled(esTiempo);
        valorGoles.setEnabled(!esTiempo);
        repaint();
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

    public TipoPartido getTipoPartido() {
        if (botonTipoGoles.isSelected()) {
            return TipoPartido.GOLES;
        }
        return TipoPartido.TIEMPO;
    }

    public int getTiempo() {
        return tiempo;
    }

    public int getGoles() {
        return goles;
    }

    public Resolucion getResolucion() {
        return Resolucion.values()[comboResolucion.getSelectedIndex()];
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

    private JButton crearBotonImagen(String ruta, int ancho) {
        BufferedImage imagen = RecursosImagen.cargar(ruta);

        ImageIcon iconoNormal = crearIcono(imagen, ancho);
        ImageIcon iconoEncima = crearIcono(imagen, ancho + Math.max(4, ancho * 7 / 100));
        ImageIcon iconoPresionado = crearIcono(imagen, ancho + Math.max(2, ancho * 3 / 100));

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

    private ImageIcon crearIcono(BufferedImage imagen, int ancho) {
        int alto = ancho * imagen.getHeight() / imagen.getWidth();
        return new ImageIcon(imagen.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH));
    }

    // ------------------------------------------------------------------
    // Botón de opción tipo TIEMPO / GOLES.
    // ------------------------------------------------------------------
    private static final class BotonOpcion extends JToggleButton {

        private BotonOpcion(String texto) {
            super(texto);
            setFont(new Font("SansSerif", Font.BOLD, 16));
            setPreferredSize(new Dimension(140, 42));
            setOpaque(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            int ancho = getWidth();
            int alto = getHeight();
            boolean seleccionado = isSelected() || getModel().isRollover();

            Color colorFondo;
            Color colorBorde;
            Color colorTexto;

            if (seleccionado) {
                colorFondo = new Color(46, 168, 74);
                colorBorde = new Color(20, 90, 45);
                colorTexto = Color.WHITE;
            } else {
                colorFondo = new Color(214, 232, 242);
                colorBorde = new Color(120, 160, 185);
                colorTexto = COLOR_TEXTO;
            }

            g2.setColor(colorFondo);
            g2.fillRoundRect(0, 0, ancho, alto, alto, alto);

            g2.setColor(colorBorde);
            g2.setStroke(new BasicStroke(2f));
            g2.drawRoundRect(1, 1, ancho - 2, alto - 2, alto, alto);

            g2.setFont(getFont());
            FontMetrics fm = g2.getFontMetrics();
            int x = (ancho - fm.stringWidth(getText())) / 2;
            int y = (alto + fm.getAscent() - fm.getDescent()) / 2;
            g2.setColor(colorTexto);
            g2.drawString(getText(), x, y);

            g2.dispose();
        }
    }

    // ------------------------------------------------------------------
    // Botón pequeño de + / -.
    // ------------------------------------------------------------------
    private static final class BotonPaso extends JButton {

        private BotonPaso(String texto) {
            super(texto);
            setFont(new Font("SansSerif", Font.BOLD, 22));
            setPreferredSize(new Dimension(40, 40));
            setOpaque(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            Color base;
            if (isEnabled()) {
                base = new Color(48, 118, 196);
            } else {
                base = new Color(150, 170, 185);
            }

            g2.setColor(base);
            g2.fillOval(0, 0, getWidth(), getHeight());

            g2.setColor(base.darker());
            g2.setStroke(new BasicStroke(2f));
            g2.drawOval(1, 1, getWidth() - 2, getHeight() - 2);

            g2.setFont(getFont());
            FontMetrics fm = g2.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(getText())) / 2;
            int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
            g2.setColor(Color.WHITE);
            g2.drawString(getText(), x, y);

            g2.dispose();
        }
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

    private static final class TituloConfiguracion extends JComponent {

        private final String texto;
        private final Font fuente = new Font("SansSerif", Font.BOLD, 28);

        private TituloConfiguracion(String texto) {
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
