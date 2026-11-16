package presentation;

import domain.Estadisticas;
import domain.ParametrosJuego;
import domain.PerfilJugador;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class PerfilView extends VistaBase {

    private static final String RUTA_BOTONES = "resources/images/Botones/";
    private static final Color COLOR_TEXTO = new Color(22, 48, 68);
    private static final Color COLOR_ETIQUETA = new Color(30, 62, 86);

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

    /**
     * Crea la pantalla de perfiles con el fondo, el título y la tarjeta
     * central donde se administran los perfiles.
     */
    public PerfilView() {
        super("resources/images/Ventanas/FondoPerfil.png");
        setTitulo("TU PERFIL");
        prepareViewCentro();
    }

    /**
     * Prepara la tarjeta central con la lista de perfiles, los datos del
     * perfil seleccionado y los botones de acción.
     */
    private void prepareViewCentro() {
        TarjetaPanel tarjeta = new TarjetaPanel();
        tarjeta.setLayout(new BorderLayout(26, 18));
        tarjeta.setBorder(new EmptyBorder(28, 30, 22, 30));
        tarjeta.setPreferredSize(new Dimension(900, 500));

        tarjeta.add(preparePanelLista(), BorderLayout.WEST);
        tarjeta.add(preparePanelDatos(), BorderLayout.CENTER);
        tarjeta.add(preparePanelBotones(), BorderLayout.SOUTH);

        setCentro(tarjeta, 10, 30);
    }

    /**
     * Prepara la lista de perfiles creados.
     *
     * @return el panel con la lista de perfiles
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
     *
     * @return el panel con los datos del perfil
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
        // Fija el ancho preferido para que un nombre largo no ensanche el
        // panel ni empuje las estadísticas fuera de la tarjeta.
        campoNombre.setColumns(14);
        campoNombre.setMinimumSize(new Dimension(120, 34));
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
     *
     * @param panel panel de datos
     * @param nombre nombre de la estadística
     * @param fila fila del grid donde se agrega
     * @param barra barra que representa la estadística
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
     *
     * @return el panel con los botones
     */
    private JPanel preparePanelBotones() {
        botonCrear = BotonImagen.crear(RUTA_BOTONES + "CrearPerfil.png", 215);
        botonGuardar = BotonImagen.crear(RUTA_BOTONES + "GuardarPerfil.png", 215);
        botonEliminar = BotonImagen.crear(RUTA_BOTONES + "EliminarPerfil.png", 215);

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 0));
        panel.setOpaque(false);
        panel.add(botonCrear);
        panel.add(botonGuardar);
        panel.add(botonEliminar);
        return panel;
    }

    /**
     * Crea una etiqueta con el estilo de la pantalla.
     *
     * @param texto texto de la etiqueta
     * @return la etiqueta creada
     */
    private static JLabel crearEtiqueta(String texto) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(new Font("SansSerif", Font.BOLD, 16));
        etiqueta.setForeground(COLOR_ETIQUETA);
        return etiqueta;
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

    /**
     * Devuelve el nombre escrito en el campo de texto.
     *
     * @return el nombre sin espacios sobrantes
     */
    public String getNombre() {
        return campoNombre.getText().trim();
    }

    /**
     * Devuelve la lista de perfiles.
     *
     * @return la lista de perfiles
     */
    public JList<String> getListaPerfiles() {
        return listaPerfiles;
    }

    /**
     * Devuelve el botón de crear perfil.
     *
     * @return el botón de crear
     */
    public JButton getBotonCrear() {
        return botonCrear;
    }

    /**
     * Devuelve el botón de guardar perfil.
     *
     * @return el botón de guardar
     */
    public JButton getBotonGuardar() {
        return botonGuardar;
    }

    /**
     * Devuelve el botón de eliminar perfil.
     *
     * @return el botón de eliminar
     */
    public JButton getBotonEliminar() {
        return botonEliminar;
    }

    // ------------------------------------------------------------------
    // Renderer de la lista de perfiles.
    // ------------------------------------------------------------------
    private static final class PerfilRenderer extends DefaultListCellRenderer {

        /**
         * Prepara la etiqueta que representa un perfil en la lista.
         *
         * @param lista lista que dibuja la celda
         * @param valor valor de la celda
         * @param indice índice de la celda
         * @param seleccionado indica si la celda está seleccionada
         * @param tieneFoco indica si la lista tiene el foco
         * @return el componente de la celda
         */
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
            etiqueta.setFont(new Font("SansSerif", Font.BOLD, 18));
            etiqueta.setForeground(colorTexto);
            etiqueta.setBorder(new EmptyBorder(7, 14, 7, 14));
            etiqueta.setBackground(colorFondo);
            return etiqueta;
        }
    }

    // ------------------------------------------------------------------
    // Barra de estadística de 1 a 10.
    // ------------------------------------------------------------------
    private static final class BarraEstadistica extends JComponent {

        private int valor;

        /**
         * Crea la barra de estadística.
         */
        private BarraEstadistica() {
            setPreferredSize(new Dimension(240, 18));
        }

        /**
         * Asigna el valor que muestra la barra.
         *
         * @param valor valor entre 0 y el máximo permitido
         */
        private void setValor(int valor) {
            this.valor = Math.max(0, Math.min(ParametrosJuego.MAX_ESTADISTICA, valor));
            repaint();
        }

        /**
         * Dibuja los segmentos y el valor de la estadística.
         *
         * @param g contexto gráfico
         */
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
