package presentation;

import domain.PerfilJugador;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class SeleccionPerfilView extends JPanel {

    private static final String RUTA_BOTONES = "resources/images/Botones.java/";
    private static final Color COLOR_TEXTO = new Color(22, 48, 68);
    private static final Color COLOR_ETIQUETA = new Color(30, 62, 86);

    private BufferedImage fondo;
    private JButton botonAtras;
    private BotonSonido botonSonido;
    private PanelJugador panelJugador1;
    private PanelJugador panelJugador2;
    private JButton botonContinuarCentral;

    /**
     * Crea la pantalla para que los dos jugadores elijan su perfil.
     */
    public SeleccionPerfilView() {
        prepareView();
    }

    private void prepareView() {
        setLayout(new BorderLayout());
        setOpaque(true);
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(1920, 1080));
        prepareViewFondo();
        prepareViewSuperior();
        prepareViewCentro();
    }

    private void prepareViewFondo() {
        fondo = RecursosImagen.cargar("resources/images/Ventanas/FondoPerfil.png");
    }

    private void prepareViewSuperior() {
        botonAtras = crearBotonImagen(RUTA_BOTONES + "Retroceder.png", 90);
        botonSonido = new BotonSonido(64);

        JPanel panelTitulo = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panelTitulo.setOpaque(false);
        panelTitulo.add(new TituloPerfil("SELECCIONAR PERFIL"));

        JPanel barraSuperior = new JPanel(new BorderLayout());
        barraSuperior.setOpaque(false);
        barraSuperior.setBorder(new EmptyBorder(18, 20, 0, 20));
        barraSuperior.add(botonAtras, BorderLayout.WEST);
        barraSuperior.add(panelTitulo, BorderLayout.CENTER);
        barraSuperior.add(botonSonido, BorderLayout.EAST);

        add(barraSuperior, BorderLayout.NORTH);
    }

    private void prepareViewCentro() {
        panelJugador1 = new PanelJugador("JUGADOR 1");
        panelJugador2 = new PanelJugador("JUGADOR 2");
        panelJugador1.setPreferredSize(new Dimension(500, 430));
        panelJugador2.setPreferredSize(new Dimension(500, 430));

        botonContinuarCentral = crearBotonImagen(RUTA_BOTONES + "ReadyPerfil.png", 300);
        botonContinuarCentral.setVisible(false);

        JPanel contenedor = new JPanel(new GridBagLayout());
        contenedor.setOpaque(false);
        contenedor.setBorder(new EmptyBorder(6, 30, 26, 30));

        GridBagConstraints c = new GridBagConstraints();
        c.gridy = 0;
        c.insets = new Insets(0, 18, 0, 18);
        c.gridx = 0;
        contenedor.add(panelJugador1, c);
        c.gridx = 1;
        contenedor.add(panelJugador2, c);

        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        c.insets = new Insets(22, 0, 0, 0);
        JPanel panelContinuar = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panelContinuar.setOpaque(false);
        panelContinuar.add(botonContinuarCentral);
        contenedor.add(panelContinuar, c);

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

    public void setContinuarCentralVisible(boolean visible) {
        botonContinuarCentral.setVisible(visible);
        revalidate();
        repaint();
    }

    public void reiniciar() {
        panelJugador1.reiniciar();
        panelJugador2.reiniciar();
        setContinuarCentralVisible(false);
    }

    public PanelJugador getPanelJugador1() {
        return panelJugador1;
    }

    public PanelJugador getPanelJugador2() {
        return panelJugador2;
    }

    public JButton getBotonContinuarCentral() {
        return botonContinuarCentral;
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
    // Panel de un jugador: lista de perfiles y botones.
    // ------------------------------------------------------------------
    public static final class PanelJugador extends JPanel {

        private final CardLayout cardLayout = new CardLayout();
        private final JPanel cuerpo = new JPanel(cardLayout);

        private final DefaultListModel<String> modelo = new DefaultListModel<>();
        private final JList<String> lista = new JList<>(modelo);
        private final JLabel nombreElegido = new JLabel("", SwingConstants.CENTER);

        private final JButton botonContinuar;
        private final JButton botonCambiar;
        private final JButton botonCrear;
        private final JButton botonCrearListo;

        private List<PerfilJugador> perfilesMostrados = new ArrayList<>();

        private PanelJugador(String titulo) {
            setOpaque(false);
            setLayout(new BorderLayout(0, 14));
            setBorder(new EmptyBorder(22, 24, 22, 24));

            botonContinuar = crearBotonImagen(RUTA_BOTONES + "ContinuarSeleccionPerfil.png", 180);
            botonCambiar = crearBotonImagen(RUTA_BOTONES + "CambiarPerfil.png", 200);
            botonCrear = crearBotonCrear();
            botonCrearListo = crearBotonCrear();

            add(prepararTitulo(titulo), BorderLayout.NORTH);

            cuerpo.setOpaque(false);
            cuerpo.add(prepararSeleccion(), "seleccion");
            cuerpo.add(prepararListo(), "listo");
            add(cuerpo, BorderLayout.CENTER);
        }

        private JLabel prepararTitulo(String titulo) {
            JLabel etiqueta = new JLabel(titulo, SwingConstants.CENTER);
            etiqueta.setFont(new Font("SansSerif", Font.BOLD, 24));
            etiqueta.setForeground(COLOR_TEXTO);
            return etiqueta;
        }

        private JPanel prepararSeleccion() {
            lista.setOpaque(false);
            lista.setForeground(COLOR_TEXTO);
            lista.setFont(new Font("SansSerif", Font.BOLD, 17));
            lista.setSelectionForeground(Color.WHITE);
            lista.setFixedCellHeight(38);
            lista.setCellRenderer(new PerfilRenderer());
            lista.setBorder(new EmptyBorder(0, 0, 0, 8));

            JScrollPane scroll = new JScrollPane(lista);
            scroll.setOpaque(false);
            scroll.getViewport().setOpaque(false);
            scroll.setBorder(BorderFactory.createEmptyBorder());

            JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
            panelBotones.setOpaque(false);
            panelBotones.add(botonContinuar);
            panelBotones.add(botonCrear);

            JPanel panel = new JPanel(new BorderLayout(0, 12));
            panel.setOpaque(false);
            panel.add(scroll, BorderLayout.CENTER);
            panel.add(panelBotones, BorderLayout.SOUTH);
            return panel;
        }

        private JPanel prepararListo() {
            nombreElegido.setFont(new Font("SansSerif", Font.BOLD, 26));
            nombreElegido.setForeground(COLOR_TEXTO);

            JLabel etiqueta = new JLabel("PERFIL ELEGIDO", SwingConstants.CENTER);
            etiqueta.setFont(new Font("SansSerif", Font.BOLD, 15));
            etiqueta.setForeground(COLOR_ETIQUETA);

            JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
            panelBotones.setOpaque(false);
            panelBotones.add(botonCambiar);
            panelBotones.add(botonCrearListo);

            JPanel panel = new JPanel(new GridBagLayout());
            panel.setOpaque(false);

            GridBagConstraints c = new GridBagConstraints();
            c.gridx = 0;

            c.gridy = 0;
            c.weighty = 1;
            JPanel rellenoArriba = new JPanel();
            rellenoArriba.setOpaque(false);
            panel.add(rellenoArriba, c);

            c.gridy = 1;
            c.weighty = 0;
            c.insets = new Insets(0, 0, 8, 0);
            panel.add(etiqueta, c);

            c.gridy = 2;
            c.insets = new Insets(0, 0, 18, 0);
            panel.add(nombreElegido, c);

            c.gridy = 3;
            c.insets = new Insets(0, 0, 0, 0);
            panel.add(panelBotones, c);

            c.gridy = 4;
            c.weighty = 1;
            JPanel rellenoAbajo = new JPanel();
            rellenoAbajo.setOpaque(false);
            panel.add(rellenoAbajo, c);

            return panel;
        }

        private JButton crearBotonCrear() {
            return crearBotonImagen(RUTA_BOTONES + "CrearPerfil.png", 170);
        }

        private static JButton crearBotonImagen(String ruta, int ancho) {
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

        private static ImageIcon crearIcono(BufferedImage imagen, int ancho) {
            int alto = ancho * imagen.getHeight() / imagen.getWidth();
            return new ImageIcon(imagen.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH));
        }

        public void mostrarPerfiles(List<PerfilJugador> perfiles) {
            PerfilJugador anterior = getSeleccionado();

            modelo.clear();
            perfilesMostrados = new ArrayList<>(perfiles);
            for (PerfilJugador perfil : perfiles) {
                modelo.addElement(perfil.getNombre());
            }

            if (anterior != null) {
                for (int i = 0; i < perfilesMostrados.size(); i++) {
                    if (perfilesMostrados.get(i).getId().equals(anterior.getId())) {
                        lista.setSelectedIndex(i);
                        break;
                    }
                }
            } else {
                lista.clearSelection();
            }
        }

        public PerfilJugador getSeleccionado() {
            int indice = lista.getSelectedIndex();
            if (indice >= 0 && indice < perfilesMostrados.size()) {
                return perfilesMostrados.get(indice);
            }
            return null;
        }

        public void marcarListo(String nombre) {
            nombreElegido.setText(nombre);
            cardLayout.show(cuerpo, "listo");
            revalidate();
            repaint();
        }

        public void volverASeleccion() {
            cardLayout.show(cuerpo, "seleccion");
            lista.clearSelection();
            revalidate();
            repaint();
        }

        public void reiniciar() {
            volverASeleccion();
            modelo.clear();
            perfilesMostrados.clear();
        }

        public JList<String> getLista() {
            return lista;
        }

        public JButton getBotonContinuar() {
            return botonContinuar;
        }

        public JButton getBotonCambiar() {
            return botonCambiar;
        }

        public JButton getBotonCrear() {
            return botonCrear;
        }

        public JButton getBotonCrearListo() {
            return botonCrearListo;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            int ancho = getWidth();
            int alto = getHeight();
            int arco = 34;

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

    // ------------------------------------------------------------------
    // Título superior.
    // ------------------------------------------------------------------
    private static final class TituloPerfil extends JComponent {

        private final String texto;
        private final Font fuente = new Font("SansSerif", Font.BOLD, 30);

        private TituloPerfil(String texto) {
            this.texto = texto;
        }

        @Override
        public Dimension getPreferredSize() {
            FontMetrics fm = getFontMetrics(fuente);
            return new Dimension(fm.stringWidth(texto) + 90, 58);
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

    // ------------------------------------------------------------------
    // Renderer de la lista de perfiles.
    // ------------------------------------------------------------------
    private static final class PerfilRenderer extends DefaultListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList<?> lista, Object valor,
                                                      int indice, boolean seleccionado,
                                                      boolean tieneFoco) {
            JLabel etiqueta = (JLabel) super.getListCellRendererComponent(
                    lista, valor, indice, seleccionado, tieneFoco);

            Color colorTexto;
            Color colorFondo;

            if (seleccionado) {
                colorTexto = Color.WHITE;
                colorFondo = new Color(38, 108, 148);
            } else {
                colorTexto = COLOR_TEXTO;
                colorFondo = new Color(214, 232, 242);
            }

            etiqueta.setOpaque(true);
            etiqueta.setFont(new Font("SansSerif", Font.BOLD, 17));
            etiqueta.setForeground(colorTexto);
            etiqueta.setBorder(new EmptyBorder(6, 14, 6, 14));
            etiqueta.setBackground(colorFondo);
            return etiqueta;
        }
    }
}
