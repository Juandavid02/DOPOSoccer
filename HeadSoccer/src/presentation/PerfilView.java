package presentation;

import domain.Estadisticas;
import domain.ParametrosJuego;
import domain.PerfilJugador;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class PerfilView extends JPanel {

    private static final String RUTA_BOTONES = "resources/images/Botones.java/";
    private static final Color COLOR_TEXTO = new Color(22, 48, 68);
    private static final Color COLOR_ETIQUETA = new Color(30, 62, 86);

    private BufferedImage fondo;

    private DefaultListModel<String> modeloPerfiles;
    private JList<String> listaPerfiles;
    private JTextField campoNombre;
    private JLabel etiquetaPuntos;
    private BarraEstadistica barraVelocidad;
    private BarraEstadistica barraSalto;
    private BarraEstadistica barraPatada;
    private BarraEstadistica barraSupervivencia;

    private JButton botonCrear;
    private JButton botonGuardar;
    private JButton botonEliminar;
    private JButton botonAtras;

    /**
     * Crea la pantalla de perfiles con el fondo, el título y la tarjeta
     * central donde se administran los perfiles.
     */
    public PerfilView() {
        prepareView();
    }

    /**
     * Prepara la vista: tamaño, fondo, barra superior y tarjeta central.
     */
    private void prepareView() {
        setLayout(new BorderLayout());
        setOpaque(true);
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(1920, 1080));
        prepareViewFondo();
        prepareViewSuperior();
        prepareViewCentro();
    }

    /**
     * Carga la imagen de fondo de la pantalla.
     */
    private void prepareViewFondo() {
        fondo = RecursosImagen.cargar("resources/images/Ventanas/FondoPerfil.png");
    }

    /**
     * Prepara el botón de atrás y el título de la pantalla.
     */
    private void prepareViewSuperior() {
        botonAtras = crearBoton(RUTA_BOTONES + "Retroceder.png", 90);

        JPanel panelTitulo = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panelTitulo.setOpaque(false);
        panelTitulo.add(new TituloPerfil("TU PERFIL"));

        JPanel espacioDerecha = new JPanel();
        espacioDerecha.setOpaque(false);
        espacioDerecha.setPreferredSize(botonAtras.getPreferredSize());

        JPanel barraSuperior = new JPanel(new BorderLayout());
        barraSuperior.setOpaque(false);
        barraSuperior.setBorder(new EmptyBorder(18, 20, 0, 20));
        barraSuperior.add(botonAtras, BorderLayout.WEST);
        barraSuperior.add(panelTitulo, BorderLayout.CENTER);
        barraSuperior.add(espacioDerecha, BorderLayout.EAST);

        add(barraSuperior, BorderLayout.NORTH);
    }

    /**
     * Prepara la tarjeta central con la lista de perfiles, los datos del
     * perfil seleccionado y los botones de acción.
     */
    private void prepareViewCentro() {
        PanelTarjeta tarjeta = new PanelTarjeta();
        tarjeta.setLayout(new BorderLayout(26, 18));
        tarjeta.setBorder(new EmptyBorder(28, 30, 22, 30));
        tarjeta.setPreferredSize(new Dimension(900, 500));

        tarjeta.add(preparePanelLista(), BorderLayout.WEST);
        tarjeta.add(preparePanelDatos(), BorderLayout.CENTER);
        tarjeta.add(preparePanelBotones(), BorderLayout.SOUTH);

        JPanel contenedor = new JPanel(new GridBagLayout());
        contenedor.setOpaque(false);
        contenedor.setBorder(new EmptyBorder(10, 0, 30, 0));
        contenedor.add(tarjeta);

        add(contenedor, BorderLayout.CENTER);
    }

    /**
     * Prepara la lista de perfiles creados.
     */
    private JPanel preparePanelLista() {
        JLabel titulo = crearEtiqueta("PERFILES");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 18));

        modeloPerfiles = new DefaultListModel<>();
        listaPerfiles = new JList<>(modeloPerfiles);
        listaPerfiles.setOpaque(false);
        listaPerfiles.setForeground(COLOR_TEXTO);
        listaPerfiles.setFont(new Font("SansSerif", Font.BOLD, 18));
        listaPerfiles.setSelectionForeground(COLOR_TEXTO);
        listaPerfiles.setFixedCellHeight(40);
        listaPerfiles.setCellRenderer(new PerfilRenderer());
        listaPerfiles.setBorder(new EmptyBorder(0, 0, 0, 10));

        JScrollPane scroll = new JScrollPane(listaPerfiles);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(BorderFactory.createEmptyBorder());

        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(250, 0));
        panel.add(titulo, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    /**
     * Prepara el panel con el nombre, los puntos y las estadísticas.
     */
    private JPanel preparePanelDatos() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 0, 5, 12);
        c.anchor = GridBagConstraints.WEST;

        c.gridx = 0;
        c.gridy = 0;
        panel.add(crearEtiqueta("NOMBRE"), c);

        campoNombre = new JTextField();
        campoNombre.setFont(new Font("SansSerif", Font.BOLD, 20));
        campoNombre.setForeground(new Color(18, 42, 60));
        campoNombre.setOpaque(true);
        campoNombre.setBackground(Color.WHITE);
        campoNombre.setBorder(new EmptyBorder(8, 12, 8, 12));
        c.gridx = 1;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(campoNombre, c);

        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0;
        c.fill = GridBagConstraints.NONE;
        panel.add(crearEtiqueta("PUNTOS"), c);

        etiquetaPuntos = new JLabel("0");
        etiquetaPuntos.setFont(new Font("SansSerif", Font.BOLD, 24));
        etiquetaPuntos.setForeground(new Color(186, 116, 8));
        c.gridx = 1;
        c.weightx = 1;
        panel.add(etiquetaPuntos, c);

        barraVelocidad = new BarraEstadistica();
        barraSalto = new BarraEstadistica();
        barraPatada = new BarraEstadistica();
        barraSupervivencia = new BarraEstadistica();

        agregarEstadistica(panel, "Velocidad", 2, barraVelocidad);
        agregarEstadistica(panel, "Salto", 3, barraSalto);
        agregarEstadistica(panel, "Patada", 4, barraPatada);
        agregarEstadistica(panel, "Supervivencia", 5, barraSupervivencia);

        c.gridx = 0;
        c.gridy = 6;
        c.gridwidth = 2;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        JPanel relleno = new JPanel();
        relleno.setOpaque(false);
        panel.add(relleno, c);

        return panel;
    }

    /**
     * Agrega una fila de estadística al panel de datos.
     */
    private void agregarEstadistica(JPanel panel, String nombre, int fila, BarraEstadistica barra) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = fila;
        c.insets = new Insets(5, 0, 5, 14);
        c.anchor = GridBagConstraints.WEST;
        panel.add(crearEtiqueta(nombre), c);

        c.gridx = 1;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(barra, c);
    }

    /**
     * Prepara los botones de crear, guardar y eliminar.
     */
    private JPanel preparePanelBotones() {
        botonCrear = crearBoton(RUTA_BOTONES + "CrearPerfil.png", 215);
        botonGuardar = crearBoton(RUTA_BOTONES + "GuardarPerfil.png", 215);
        botonEliminar = crearBoton(RUTA_BOTONES + "EliminarPerfil.png", 215);

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 0));
        panel.setOpaque(false);
        panel.add(botonCrear);
        panel.add(botonGuardar);
        panel.add(botonEliminar);
        return panel;
    }

    /**
     * Crea una etiqueta con el estilo de la pantalla.
     */
    private static JLabel crearEtiqueta(String texto) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(new Font("SansSerif", Font.BOLD, 16));
        etiqueta.setForeground(COLOR_ETIQUETA);
        return etiqueta;
    }

    /**
     * Crea un botón con imagen. La imagen se agranda un poco cuando el
     * mouse está encima y se encoge un poco cuando se presiona.
     */
    private JButton crearBoton(String ruta, int ancho) {
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

    /**
     * Crea un ícono con el ancho dado, manteniendo la proporción de la imagen.
     */
    private ImageIcon crearIcono(BufferedImage imagen, int ancho) {
        int alto = ancho * imagen.getHeight() / imagen.getWidth();
        return new ImageIcon(imagen.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH));
    }

    /**
     * Dibuja el fondo ocupando todo el panel, recortando lo necesario para
     * mantener la proporción de la imagen.
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

        g2.drawImage(fondo, (getWidth() - ancho) / 2, (getHeight() - alto) / 2, ancho, alto, null);
        g2.dispose();
    }

    /**
     * Muestra en la lista los nombres de los perfiles existentes.
     *
     * @param perfiles los perfiles creados
     */
    public void mostrarPerfiles(List<PerfilJugador> perfiles) {
        int seleccion = listaPerfiles.getSelectedIndex();

        modeloPerfiles.clear();
        for (PerfilJugador perfil : perfiles) {
            modeloPerfiles.addElement(perfil.getNombre());
        }

        if (modeloPerfiles.isEmpty()) {
            limpiarFormulario();
        } else {
            listaPerfiles.setSelectedIndex(Math.max(0, Math.min(seleccion, modeloPerfiles.size() - 1)));
        }

        revalidate();
        repaint();
    }

    /**
     * Muestra en el panel derecho los datos de un perfil.
     *
     * @param perfil el perfil seleccionado
     */
    public void mostrarPerfil(PerfilJugador perfil) {
        if (perfil == null) {
            limpiarFormulario();
            return;
        }

        campoNombre.setText(perfil.getNombre());
        etiquetaPuntos.setText(String.valueOf(perfil.getPuntos().getTotal()));

        Estadisticas estadisticas = perfil.getEstadisticas();
        barraVelocidad.setValor(estadisticas.getSpeed());
        barraSalto.setValor(estadisticas.getJump());
        barraPatada.setValor(estadisticas.getKick());
        barraSupervivencia.setValor(estadisticas.getSurvival());

        revalidate();
        repaint();
    }

    /**
     * Limpia el formulario cuando no hay ningún perfil seleccionado.
     */
    public void limpiarFormulario() {
        campoNombre.setText("");
        etiquetaPuntos.setText("0");
        barraVelocidad.setValor(0);
        barraSalto.setValor(0);
        barraPatada.setValor(0);
        barraSupervivencia.setValor(0);
    }

    public String getNombre() {
        return campoNombre.getText().trim();
    }

    public JList<String> getListaPerfiles() {
        return listaPerfiles;
    }

    public JButton getBotonCrear() {
        return botonCrear;
    }

    public JButton getBotonGuardar() {
        return botonGuardar;
    }

    public JButton getBotonEliminar() {
        return botonEliminar;
    }

    public JButton getBotonAtras() {
        return botonAtras;
    }

    // ------------------------------------------------------------------
    // Título con fondo redondeado y texto con contorno.
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
    // Tarjeta translúcida que contiene toda la información del perfil.
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

            etiqueta.setOpaque(true);
            etiqueta.setFont(new Font("SansSerif", Font.BOLD, 18));
            etiqueta.setForeground(seleccionado ? Color.WHITE : COLOR_TEXTO);
            etiqueta.setBorder(new EmptyBorder(7, 14, 7, 14));
            etiqueta.setBackground(seleccionado
                    ? new Color(38, 108, 148)
                    : new Color(214, 232, 242));
            return etiqueta;
        }
    }

    // ------------------------------------------------------------------
    // Barra de estadística de 1 a 10.
    // ------------------------------------------------------------------
    private static final class BarraEstadistica extends JComponent {

        private int valor;

        private BarraEstadistica() {
            setPreferredSize(new Dimension(240, 18));
        }

        private void setValor(int valor) {
            this.valor = Math.max(0, Math.min(ParametrosJuego.MAX_ESTADISTICA, valor));
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            int segmentos = ParametrosJuego.MAX_ESTADISTICA;
            int anchoTexto = 30;
            int separacion = 4;
            int anchoBarra = Math.max(40, getWidth() - anchoTexto);
            int anchoSegmento = Math.max(4, (anchoBarra - separacion * (segmentos - 1)) / segmentos);
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

            g2.setFont(new Font("SansSerif", Font.BOLD, 15));
            g2.setColor(COLOR_TEXTO);
            String texto = String.valueOf(valor);
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(texto,
                    getWidth() - fm.stringWidth(texto),
                    (getHeight() + fm.getAscent() - fm.getDescent()) / 2);

            g2.dispose();
        }
    }
}
