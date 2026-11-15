package presentation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.IntConsumer;
import java.util.function.IntPredicate;

/**
 * Pantalla de configuración general: volumen del juego y teclas de ambos
 * jugadores.
 */
public class ConfiguracionView extends VistaBase {

    private static final String RUTA_BOTONES = "resources/images/Botones/";

    private static final Color TEXTO_CLARO = new Color(232, 242, 250);
    private static final Color TEXTO_SUAVE = new Color(190, 210, 228);
    private static final Color DORADO = new Color(212, 175, 55);
    private static final Color DORADO_CLARO = new Color(255, 226, 130);
    private static final Color AZUL_JUGADOR1 = new Color(61, 135, 186);
    private static final Color ROJO_JUGADOR2 = new Color(174, 62, 72);

    private final Map<ConfiguracionControles.Accion, BotonTecla> botonesJugador1 =
            new EnumMap<>(ConfiguracionControles.Accion.class);
    private final Map<ConfiguracionControles.Accion, BotonTecla> botonesJugador2 =
            new EnumMap<>(ConfiguracionControles.Accion.class);

    private JButton botonGuardar;
    private JButton botonReiniciar;
    private JLabel valorVolumen;
    private JButton botonVolumenMenos;
    private JButton botonVolumenMas;
    private int volumen = 100;
    private IntConsumer alCambiarVolumen;

    /**
     * Crea la pantalla de configuración general.
     */
    public ConfiguracionView() {
        super("resources/images/Ventanas/FondoConfiguracion.png");
        setTitulo("CONFIGURACIÓN");
        prepareViewCentro();
    }

    /**
     * Construye el contenido central de la pantalla.
     */
    private void prepareViewCentro() {
        TarjetaPanel tarjeta = new TarjetaPanel(TarjetaPanel.Estilo.OSCURA);
        tarjeta.setLayout(new GridBagLayout());
        tarjeta.setBorder(new EmptyBorder(18, 32, 16, 32));
        tarjeta.setPreferredSize(new Dimension(980, 540));
        tarjeta.setMinimumSize(new Dimension(880, 500));

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0, 0, 8, 0);

        c.gridy = 0;
        tarjeta.add(prepararAudio(), c);

        c.gridy = 1;
        c.insets = new Insets(2, 0, 8, 0);
        tarjeta.add(prepararControles(), c);

        c.gridy = 2;
        c.weighty = 1;
        c.insets = new Insets(4, 0, 0, 0);
        tarjeta.add(prepararAcciones(), c);

        JPanel contenedor = new JPanel(new GridBagLayout());
        contenedor.setOpaque(false);
        contenedor.setBorder(new EmptyBorder(8, 0, 20, 0));
        contenedor.add(tarjeta);

        setCentro(contenedor, 0, 0);
    }

    /**
     * Prepara la sección de audio con el control de volumen.
     *
     * @return panel con el control de volumen
     */
    private JPanel prepararAudio() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(0, 8, 4, 8));

        JLabel titulo = crearTituloSeccion("AUDIO");

        JLabel etiqueta = new JLabel("Volumen");
        etiqueta.setFont(new Font("SansSerif", Font.BOLD, 19));
        etiqueta.setForeground(TEXTO_CLARO);

        botonVolumenMenos = new BotonPaso("-");
        botonVolumenMas = new BotonPaso("+");

        valorVolumen = new JLabel(volumen + " %", SwingConstants.CENTER);
        valorVolumen.setFont(new Font("SansSerif", Font.BOLD, 19));
        valorVolumen.setForeground(DORADO_CLARO);
        valorVolumen.setPreferredSize(new Dimension(80, 28));

        JPanel control = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 0));
        control.setOpaque(false);
        control.add(botonVolumenMenos);
        control.add(valorVolumen);
        control.add(botonVolumenMas);

        botonVolumenMenos.addActionListener(e -> cambiarVolumen(-5));
        botonVolumenMas.addActionListener(e -> cambiarVolumen(5));
        actualizarBotonesVolumen();

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 3;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(0, 0, 6, 0);
        panel.add(titulo, c);

        c.gridy = 1;
        c.gridwidth = 1;
        c.insets = new Insets(0, 0, 0, 20);
        panel.add(etiqueta, c);

        c.gridx = 1;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(0, 0, 0, 0);
        panel.add(control, c);

        return panel;
    }

    /**
     * Prepara la sección con los controles de ambos jugadores.
     *
     * @return panel con las columnas de controles
     */
    private JPanel prepararControles() {
        JPanel panel = new JPanel(new BorderLayout(0, 6));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(0, 8, 0, 8));

        panel.add(crearTituloSeccion("CONTROLES"), BorderLayout.NORTH);

        JPanel columnas = new JPanel(new GridLayout(1, 2, 28, 0));
        columnas.setOpaque(false);
        columnas.add(crearPanelJugador(1, "JUGADOR 1", AZUL_JUGADOR1,
                botonesJugador1));
        columnas.add(crearPanelJugador(2, "JUGADOR 2", ROJO_JUGADOR2,
                botonesJugador2));

        panel.add(columnas, BorderLayout.CENTER);
        return panel;
    }

    /**
     * Crea el panel de controles de un jugador.
     *
     * @param jugador número del jugador
     * @param nombre nombre mostrado en la cabecera
     * @param color color de la cabecera
     * @param botones mapa donde se guardan los botones de tecla
     * @return panel con los controles del jugador
     */
    private JPanel crearPanelJugador(
            int jugador, String nombre, Color color,
            Map<ConfiguracionControles.Accion, BotonTecla> botones) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0, 0, 8, 0);

        PanelCabecera cabecera = new PanelCabecera(nombre, color);
        cabecera.setPreferredSize(new Dimension(0, 38));
        cabecera.setMinimumSize(new Dimension(0, 38));
        panel.add(cabecera, c);

        c.insets = new Insets(2, 4, 2, 4);
        for (ConfiguracionControles.Accion accion : ConfiguracionControles.Accion.values()) {
            BotonTecla boton = new BotonTecla();
            botones.put(accion, boton);

            JPanel fila = new JPanel(new BorderLayout(12, 0));
            fila.setOpaque(false);

            JLabel etiqueta = new JLabel(accion.getEtiqueta());
            etiqueta.setFont(new Font("SansSerif", Font.PLAIN, 18));
            etiqueta.setForeground(TEXTO_SUAVE);

            fila.add(etiqueta, BorderLayout.WEST);
            fila.add(boton, BorderLayout.EAST);

            c.gridy++;
            panel.add(fila, c);
        }

        return panel;
    }

    /**
     * Prepara los botones de reiniciar y guardar.
     *
     * @return panel con los botones de acción
     */
    private JPanel prepararAcciones() {
        botonReiniciar = BotonImagen.crear(
                RUTA_BOTONES + "ReiniciarConfiguracion.png", 280);
        botonGuardar = BotonImagen.crear(
                RUTA_BOTONES + "GuardarConfiguracion.png", 280);

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 24, 0));
        panel.setOpaque(false);
        panel.add(botonReiniciar);
        panel.add(botonGuardar);
        return panel;
    }

    /**
     * Crea una etiqueta con el estilo de título de sección.
     *
     * @param texto texto de la etiqueta
     * @return etiqueta creada
     */
    private static JLabel crearTituloSeccion(String texto) {
        JLabel titulo = new JLabel(texto);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 19));
        titulo.setForeground(DORADO_CLARO);
        return titulo;
    }

    /**
     * Muestra las teclas actuales de ambos jugadores.
     *
     * @param controles configuración de controles con las teclas asignadas
     */
    public void setTeclas(ConfiguracionControles controles) {
        for (ConfiguracionControles.Accion accion : ConfiguracionControles.Accion.values()) {
            botonesJugador1.get(accion).setTecla(controles.getTecla(1, accion));
            botonesJugador2.get(accion).setTecla(controles.getTecla(2, accion));
        }
    }

    /**
     * Actualiza el volumen mostrado y notifica el cambio.
     *
     * @param volumen nuevo volumen entre 0 y 100
     */
    public void setVolumen(int volumen) {
        int nuevo = Math.max(0, Math.min(100, volumen));
        if (nuevo == this.volumen) {
            valorVolumen.setText(nuevo + " %");
            actualizarBotonesVolumen();
            return;
        }

        this.volumen = nuevo;
        valorVolumen.setText(nuevo + " %");
        actualizarBotonesVolumen();

        if (alCambiarVolumen != null) {
            alCambiarVolumen.accept(nuevo);
        }
    }

    /**
     * Devuelve el volumen actual.
     *
     * @return volumen entre 0 y 100
     */
    public int getVolumen() {
        return volumen;
    }

    /**
     * Ajusta el volumen sumando o restando una cantidad.
     *
     * @param delta cantidad que se suma al volumen
     */
    private void cambiarVolumen(int delta) {
        setVolumen(volumen + delta);
    }

    /**
     * Activa o desactiva los botones de volumen según el valor actual.
     */
    private void actualizarBotonesVolumen() {
        botonVolumenMenos.setEnabled(volumen > 0);
        botonVolumenMas.setEnabled(volumen < 100);
    }

    /**
     * Registra la acción que se ejecuta al cambiar el volumen.
     *
     * @param alCambiarVolumen acción que recibe el nuevo volumen
     */
    public void setAlCambiarVolumen(IntConsumer alCambiarVolumen) {
        this.alCambiarVolumen = alCambiarVolumen;
    }

    /**
     * Cancela la captura de tecla de todos los botones.
     */
    public void cancelarCaptura() {
        for (BotonTecla boton : botonesJugador1.values()) {
            boton.cancelarCaptura();
        }
        for (BotonTecla boton : botonesJugador2.values()) {
            boton.cancelarCaptura();
        }
    }

    /**
     * Devuelve los botones de tecla del jugador 1.
     *
     * @return mapa de acciones y botones del jugador 1
     */
    public Map<ConfiguracionControles.Accion, BotonTecla> getBotonesJugador1() {
        return botonesJugador1;
    }

    /**
     * Devuelve los botones de tecla del jugador 2.
     *
     * @return mapa de acciones y botones del jugador 2
     */
    public Map<ConfiguracionControles.Accion, BotonTecla> getBotonesJugador2() {
        return botonesJugador2;
    }

    /**
     * Devuelve el botón de guardar.
     *
     * @return botón de guardar
     */
    public JButton getBotonGuardar() {
        return botonGuardar;
    }

    /**
     * Devuelve el botón de reiniciar.
     *
     * @return botón de reiniciar
     */
    public JButton getBotonReiniciar() {
        return botonReiniciar;
    }

    /**
     * Oscurece el fondo de la pantalla.
     *
     * @param g2 contexto gráfico donde se dibuja
     */
    @Override
    protected void pintarEncimaDeFondo(Graphics2D g2) {
        g2.setColor(new Color(6, 20, 38, 75));
        g2.fillRect(0, 0, getWidth(), getHeight());
    }

    /**
     * Botón que muestra una tecla y espera la siguiente pulsación para
     * cambiarla.
     */
    public static final class BotonTecla extends JButton {

        private int tecla;
        private IntPredicate asignador;
        private KeyEventDispatcher capturador;
        private boolean capturando;

        /**
         * Crea el botón de tecla con su estilo inicial.
         */
        private BotonTecla() {
            setFont(new Font("SansSerif", Font.BOLD, 15));
            setPreferredSize(new Dimension(190, 32));
            setMinimumSize(new Dimension(150, 32));
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setFocusable(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addActionListener(e -> iniciarCaptura());
            setText("—");
        }

        /**
         * Devuelve el código de la tecla asignada.
         *
         * @return código de tecla
         */
        public int getTecla() {
            return tecla;
        }

        /**
         * Asigna una tecla al botón y actualiza su texto.
         *
         * @param tecla código de la nueva tecla
         */
        public void setTecla(int tecla) {
            this.tecla = tecla;
            actualizarTexto();
        }

        /**
         * Registra la función que valida y asigna la tecla capturada.
         *
         * @param asignador validador que decide si la tecla es válida
         */
        public void setAsignador(IntPredicate asignador) {
            this.asignador = asignador;
        }

        /**
         * Empieza a escuchar la siguiente pulsación de tecla.
         */
        private void iniciarCaptura() {
            if (capturando) {
                return;
            }

            capturando = true;
            setText("Presiona una tecla...");
            repaint();

            capturador = event -> {
                if (event.getID() != KeyEvent.KEY_PRESSED) {
                    return false;
                }

                int codigo = event.getKeyCode();
                if (codigo == KeyEvent.VK_ESCAPE) {
                    cancelarCaptura();
                    return true;
                }
                if (codigo == KeyEvent.VK_UNDEFINED) {
                    return true;
                }

                if (asignador != null && asignador.test(codigo)) {
                    tecla = codigo;
                }
                cancelarCaptura();
                return true;
            };

            KeyboardFocusManager.getCurrentKeyboardFocusManager()
                    .addKeyEventDispatcher(capturador);
        }

        /**
         * Deja de escuchar pulsaciones y restaura el texto del botón.
         */
        private void cancelarCaptura() {
            if (!capturando) {
                return;
            }

            capturando = false;
            if (capturador != null) {
                KeyboardFocusManager.getCurrentKeyboardFocusManager()
                        .removeKeyEventDispatcher(capturador);
                capturador = null;
            }
            actualizarTexto();
            repaint();
        }

        /**
         * Muestra el nombre de la tecla asignada en el botón.
         */
        private void actualizarTexto() {
            setText(ConfiguracionControles.nombreTecla(tecla));
        }

        /**
         * Cancela la captura cuando el botón sale de la pantalla.
         */
        @Override
        public void removeNotify() {
            cancelarCaptura();
            super.removeNotify();
        }

        /**
         * Dibuja el botón con el estilo de la pantalla.
         *
         * @param g contexto gráfico donde se dibuja
         */
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            int ancho = getWidth();
            int alto = getHeight();
            boolean encima = getModel().isRollover();

            RoundRectangle2D forma = new RoundRectangle2D.Float(
                    1, 1, ancho - 3, alto - 3, 16, 16);

            g2.setColor(capturando
                    ? new Color(40, 70, 96, 245)
                    : new Color(10, 30, 50, 230));
            g2.fill(forma);

            g2.setStroke(new BasicStroke(capturando || encima ? 2.5f : 1.8f));
            g2.setColor(capturando || encima ? DORADO_CLARO : new Color(180, 150, 75));
            g2.draw(forma);

            g2.setFont(getFont());
            FontMetrics fm = g2.getFontMetrics();
            String texto = getText();
            int x = (ancho - fm.stringWidth(texto)) / 2;
            int y = (alto + fm.getAscent() - fm.getDescent()) / 2;
            g2.setColor(capturando ? DORADO_CLARO : Color.WHITE);
            g2.drawString(texto, x, y);

            g2.dispose();
        }
    }

    /**
     * Deslizador de volumen con el estilo dorado de la pantalla.
     */
    private static final class BotonPaso extends JButton {

        /**
         * Crea un botón de paso con el texto indicado.
         *
         * @param texto texto que muestra el botón
         */
        private BotonPaso(String texto) {
            super(texto);
            setFont(new Font("SansSerif", Font.BOLD, 24));
            setPreferredSize(new Dimension(42, 42));
            setMinimumSize(new Dimension(42, 42));
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        /**
         * Dibuja el botón circular de paso.
         *
         * @param g contexto gráfico donde se dibuja
         */
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            boolean activo = isEnabled();
            boolean encima = getModel().isRollover() && activo;

            Color relleno = activo
                    ? new Color(10, 32, 54, 240)
                    : new Color(28, 42, 56, 200);
            Color borde = activo
                    ? (encima ? DORADO_CLARO : DORADO)
                    : new Color(100, 110, 118);

            int tamano = Math.min(getWidth(), getHeight());
            int x = (getWidth() - tamano) / 2;
            int y = (getHeight() - tamano) / 2;

            g2.setColor(relleno);
            g2.fillOval(x + 1, y + 1, tamano - 3, tamano - 3);

            g2.setStroke(new BasicStroke(encima ? 3f : 2.2f));
            g2.setColor(borde);
            g2.drawOval(x + 1, y + 1, tamano - 3, tamano - 3);

            g2.setFont(getFont());
            FontMetrics fm = g2.getFontMetrics();
            String texto = getText();
            int tx = (getWidth() - fm.stringWidth(texto)) / 2;
            int ty = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
            g2.setColor(activo ? Color.WHITE : new Color(160, 170, 178));
            g2.drawString(texto, tx, ty);

            g2.dispose();
        }
    }

    private static final class PanelCabecera extends JComponent {

        private final String texto;
        private final Color color;

        /**
         * Crea la cabecera de un jugador.
         *
         * @param texto nombre del jugador
         * @param color color de fondo de la cabecera
         */
        private PanelCabecera(String texto, Color color) {
            this.texto = texto;
            this.color = color;
            setFont(new Font("SansSerif", Font.BOLD, 19));
        }

        /**
         * Dibuja la cabecera con el nombre del jugador.
         *
         * @param g contexto gráfico donde se dibuja
         */
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            int ancho = getWidth();
            int alto = getHeight();
            RoundRectangle2D forma = new RoundRectangle2D.Float(
                    1, 1, ancho - 3, alto - 3, 20, 20);

            g2.setColor(color);
            g2.fill(forma);

            g2.setStroke(new BasicStroke(2f));
            g2.setColor(new Color(255, 255, 255, 170));
            g2.draw(forma);

            g2.setFont(getFont());
            FontMetrics fm = g2.getFontMetrics();
            int x = (ancho - fm.stringWidth(texto)) / 2;
            int y = (alto + fm.getAscent() - fm.getDescent()) / 2;
            g2.setColor(Color.WHITE);
            g2.drawString(texto, x, y);

            g2.dispose();
        }
    }
}
