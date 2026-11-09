package presentation;

import domain.Estadisticas;
import domain.ParametrosJuego;
import domain.PerfilJugador;
import domain.TipoEstadistica;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Pantalla para gastar los puntos de un perfil en mejoras de estadísticas.
 */
public class MejorasView extends JPanel {

    private static final String RUTA_BOTONES = "resources/images/Botones.java/";

    private static final Color TEXTO_CLARO = new Color(22, 48, 68);
    private static final Color TEXTO_SUAVE = new Color(30, 62, 86);
    private static final Color AZUL = new Color(38, 108, 148);
    private static final Color AZUL_CLARO = new Color(70, 150, 190);
    private static final Color NARANJA_PUNTOS = new Color(186, 116, 8);

    private final Map<TipoEstadistica, BarraEstadistica> barras =
            new EnumMap<>(TipoEstadistica.class);
    private final Map<TipoEstadistica, JLabel> valores =
            new EnumMap<>(TipoEstadistica.class);
    private final Map<TipoEstadistica, BotonMejora> botones =
            new EnumMap<>(TipoEstadistica.class);

    private BufferedImage fondo;
    private JButton botonAtras;
    private BotonSonido botonSonido;
    private DefaultListModel<PerfilJugador> modeloPerfiles;
    private JList<PerfilJugador> listaPerfiles;
    private JLabel nombrePerfil;
    private JLabel puntosPerfil;
    private JLabel costoMejora;

    public MejorasView() {
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
    }

    private void prepareViewSuperior() {
        botonAtras = crearBotonImagen(RUTA_BOTONES + "Retroceder.png", 90);
        botonSonido = new BotonSonido(64);

        JPanel panelTitulo = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panelTitulo.setOpaque(false);
        panelTitulo.add(new TituloMejoras("MEJORAS"));

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
        tarjeta.setLayout(new BorderLayout(24, 0));
        tarjeta.setBorder(new EmptyBorder(18, 26, 16, 26));
        tarjeta.setPreferredSize(new Dimension(980, 540));
        tarjeta.setMinimumSize(new Dimension(880, 500));

        tarjeta.add(prepararListaPerfiles(), BorderLayout.WEST);
        tarjeta.add(prepararDetalle(), BorderLayout.CENTER);

        JPanel contenedor = new JPanel(new GridBagLayout());
        contenedor.setOpaque(false);
        contenedor.setBorder(new EmptyBorder(8, 0, 20, 0));
        contenedor.add(tarjeta);

        add(contenedor, BorderLayout.CENTER);
    }

    private JPanel prepararListaPerfiles() {
        JLabel titulo = new JLabel("PERFILES");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 19));
        titulo.setForeground(TEXTO_CLARO);

        modeloPerfiles = new DefaultListModel<>();
        listaPerfiles = new JList<>(modeloPerfiles);
        listaPerfiles.setOpaque(false);
        listaPerfiles.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaPerfiles.setFixedCellHeight(46);
        listaPerfiles.setCellRenderer(new PerfilRenderer());
        listaPerfiles.setBorder(new EmptyBorder(0, 0, 0, 8));
        listaPerfiles.setFont(new Font("SansSerif", Font.BOLD, 17));

        JScrollPane scroll = new JScrollPane(listaPerfiles);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(BorderFactory.createEmptyBorder());

        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(280, 0));
        panel.add(titulo, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private JPanel prepararDetalle() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridwidth = 4;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(0, 0, 2, 0);

        nombrePerfil = new JLabel("Selecciona un perfil");
        nombrePerfil.setFont(new Font("SansSerif", Font.BOLD, 25));
        nombrePerfil.setForeground(TEXTO_CLARO);
        panel.add(nombrePerfil, c);

        c.gridy = 1;
        c.insets = new Insets(0, 0, 2, 0);
        puntosPerfil = new JLabel("Puntos: 0");
        puntosPerfil.setFont(new Font("SansSerif", Font.BOLD, 19));
        puntosPerfil.setForeground(NARANJA_PUNTOS);
        panel.add(puntosPerfil, c);

        c.gridy = 2;
        c.insets = new Insets(0, 0, 16, 0);
        costoMejora = new JLabel("Siguiente mejora: —");
        costoMejora.setFont(new Font("SansSerif", Font.PLAIN, 17));
        costoMejora.setForeground(TEXTO_SUAVE);
        panel.add(costoMejora, c);

        int fila = 3;
        for (TipoEstadistica tipo : TipoEstadistica.values()) {
            agregarFila(panel, tipo, fila);
            fila++;
        }

        c.gridx = 0;
        c.gridy = fila;
        c.gridwidth = 4;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        JPanel relleno = new JPanel();
        relleno.setOpaque(false);
        panel.add(relleno, c);

        return panel;
    }

    private void agregarFila(JPanel panel, TipoEstadistica tipo, int fila) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = fila;
        c.insets = new Insets(5, 0, 5, 10);
        c.anchor = GridBagConstraints.WEST;

        JLabel etiqueta = new JLabel(tipo.getEtiqueta());
        etiqueta.setFont(new Font("SansSerif", Font.BOLD, 17));
        etiqueta.setForeground(TEXTO_CLARO);
        etiqueta.setPreferredSize(new Dimension(132, 26));
        c.gridx = 0;
        panel.add(etiqueta, c);

        BarraEstadistica barra = new BarraEstadistica();
        barras.put(tipo, barra);
        c.gridx = 1;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(barra, c);

        JLabel valor = new JLabel("0/10", SwingConstants.CENTER);
        valor.setFont(new Font("SansSerif", Font.BOLD, 16));
        valor.setForeground(TEXTO_CLARO);
        valor.setPreferredSize(new Dimension(54, 26));
        valores.put(tipo, valor);
        c.gridx = 2;
        c.weightx = 0;
        c.fill = GridBagConstraints.NONE;
        c.insets = new Insets(5, 8, 5, 12);
        panel.add(valor, c);

        BotonMejora boton = new BotonMejora();
        botones.put(tipo, boton);
        c.gridx = 3;
        c.insets = new Insets(5, 0, 5, 0);
        panel.add(boton, c);
    }

    /**
     * Carga la lista de perfiles disponibles.
     *
     * @param perfiles perfiles del jugador
     */
    public void setPerfiles(List<PerfilJugador> perfiles) {
        int seleccion = listaPerfiles.getSelectedIndex();

        modeloPerfiles.clear();
        for (PerfilJugador perfil : perfiles) {
            modeloPerfiles.addElement(perfil);
        }

        if (modeloPerfiles.isEmpty()) {
            listaPerfiles.clearSelection();
            mostrarPerfil(null);
            return;
        }

        int nuevoIndice = seleccion >= 0
                ? Math.max(0, Math.min(seleccion, modeloPerfiles.size() - 1))
                : 0;
        listaPerfiles.setSelectedIndex(nuevoIndice);
        mostrarPerfil(modeloPerfiles.get(nuevoIndice));
    }

    /**
     * Muestra las estadísticas y el costo de mejora de un perfil.
     *
     * @param perfil perfil seleccionado o null
     */
    public void mostrarPerfil(PerfilJugador perfil) {
        if (perfil == null) {
            nombrePerfil.setText("Selecciona un perfil");
            puntosPerfil.setText("Puntos: 0");
            costoMejora.setText("Crea o selecciona un perfil para empezar.");
            for (TipoEstadistica tipo : TipoEstadistica.values()) {
                barras.get(tipo).setValor(0);
                valores.get(tipo).setText("0/" + ParametrosJuego.MAX_ESTADISTICA);
                botones.get(tipo).setEnabled(false);
            }
            return;
        }

        Estadisticas estadisticas = perfil.getEstadisticas();
        int costo = perfil.getCostoSiguienteMejora();
        boolean todoAlMaximo = true;

        nombrePerfil.setText(perfil.getNombre());
        puntosPerfil.setText("Puntos: " + perfil.getPuntos().getTotal());

        for (TipoEstadistica tipo : TipoEstadistica.values()) {
            int valor = tipo.getValor(estadisticas);
            barras.get(tipo).setValor(valor);
            valores.get(tipo).setText(valor + "/" + ParametrosJuego.MAX_ESTADISTICA);

            boolean puede = perfil.puedeMejorar(tipo);
            if (puede) {
                todoAlMaximo = false;
            }
            botones.get(tipo).setEnabled(puede
                    && perfil.getPuntos().puedePagar(costo));
        }

        if (todoAlMaximo) {
            costoMejora.setText("Todas las estadísticas están al máximo.");
        } else {
            costoMejora.setText("Costo de la siguiente mejora: "
                    + costo + " puntos");
        }

        revalidate();
        repaint();
    }

    public JList<PerfilJugador> getListaPerfiles() {
        return listaPerfiles;
    }

    public JButton getBotonMejorar(TipoEstadistica tipo) {
        return botones.get(tipo);
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
        boton.setPreferredSize(new Dimension(
                encima.getIconWidth(), encima.getIconHeight()));
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

    @Override
    protected void paintComponent(Graphics g) {
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

        g2.drawImage(fondo, (getWidth() - ancho) / 2, (getHeight() - alto) / 2,
                ancho, alto, null);
        g2.dispose();
    }

    private static final class PerfilRenderer extends DefaultListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList<?> lista, Object valor,
                                                      int indice, boolean seleccionado,
                                                      boolean tieneFoco) {
            JLabel etiqueta = (JLabel) super.getListCellRendererComponent(
                    lista, valor, indice, seleccionado, tieneFoco);

            PerfilJugador perfil = (PerfilJugador) valor;
            etiqueta.setText(perfil.getNombre()
                    + "  ·  " + perfil.getPuntos().getTotal() + " pts");
            etiqueta.setOpaque(true);
            etiqueta.setFont(new Font("SansSerif", Font.BOLD, 16));
            etiqueta.setBorder(new EmptyBorder(7, 12, 7, 12));

            if (seleccionado) {
                etiqueta.setForeground(Color.WHITE);
                etiqueta.setBackground(AZUL);
            } else {
                etiqueta.setForeground(TEXTO_SUAVE);
                etiqueta.setBackground(new Color(214, 232, 242));
            }
            return etiqueta;
        }
    }

    private static final class BotonMejora extends JButton {

        private BotonMejora() {
            super("MEJORAR");
            setFont(new Font("SansSerif", Font.BOLD, 14));
            setPreferredSize(new Dimension(122, 34));
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
            boolean activo = isEnabled();
            boolean encima = getModel().isRollover() && activo;
            RoundRectangle2D forma = new RoundRectangle2D.Float(
                    1, 1, ancho - 3, alto - 3, 16, 16);

            g2.setColor(activo
                    ? AZUL
                    : new Color(205, 216, 222));
            g2.fill(forma);

            g2.setStroke(new BasicStroke(encima ? 2.5f : 1.8f));
            g2.setColor(activo
                    ? (encima ? AZUL_CLARO : new Color(24, 78, 110))
                    : new Color(150, 168, 180));
            g2.draw(forma);

            g2.setFont(getFont());
            FontMetrics fm = g2.getFontMetrics();
            String texto = getText();
            int x = (ancho - fm.stringWidth(texto)) / 2;
            int y = (alto + fm.getAscent() - fm.getDescent()) / 2;
            g2.setColor(activo ? Color.WHITE : new Color(110, 125, 135));
            g2.drawString(texto, x, y);

            g2.dispose();
        }
    }

    private static final class BarraEstadistica extends JComponent {

        private int valor;

        private BarraEstadistica() {
            setPreferredSize(new Dimension(240, 20));
        }

        private void setValor(int valor) {
            this.valor = Math.max(0,
                    Math.min(ParametrosJuego.MAX_ESTADISTICA, valor));
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            int segmentos = ParametrosJuego.MAX_ESTADISTICA;
            int separacion = 4;
            int anchoSegmento = Math.max(4,
                    (getWidth() - separacion * (segmentos - 1)) / segmentos);
            int altoSegmento = Math.max(8, getHeight() - 4);

            for (int i = 0; i < segmentos; i++) {
                int x = i * (anchoSegmento + separacion);
                if (i < valor) {
                    g2.setPaint(new GradientPaint(
                            x, 0, new Color(120, 235, 150),
                            x, altoSegmento, new Color(52, 170, 110)));
                } else {
                    g2.setColor(new Color(30, 70, 95, 55));
                }
                g2.fillRoundRect(x, 2, anchoSegmento, altoSegmento, 6, 6);
            }

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
            RoundRectangle2D forma = new RoundRectangle2D.Float(
                    1, 1, ancho - 3, alto - 3, 38, 38);

            g2.setPaint(new GradientPaint(
                    0, 0, new Color(236, 246, 252, 130),
                    0, alto, new Color(204, 228, 242, 100)));
            g2.fill(forma);

            g2.setStroke(new BasicStroke(2f));
            g2.setColor(new Color(255, 255, 255, 120));
            g2.draw(forma);

            g2.dispose();
        }
    }

    private static final class TituloMejoras extends JComponent {

        private final String texto;
        private final Font fuente = new Font("SansSerif", Font.BOLD, 28);

        private TituloMejoras(String texto) {
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
