package presentation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

        botonVolumenMenos = new BotonPaso("-", BotonPaso.Estilo.DORADO, 42);
        botonVolumenMas = new BotonPaso("+", BotonPaso.Estilo.DORADO, 42);

        valorVolumen = new JLabel(volumen + " %", SwingConstants.CENTER);
        valorVolumen.setFont(new Font("SansSerif", Font.BOLD, 19));
        valorVolumen.setForeground(DORADO_CLARO);
        valorVolumen.setPreferredSize(new Dimension(80, 28));

        JPanel control = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 0));
        control.setOpaque(false);
        control.add(botonVolumenMenos);
        control.add(valorVolumen);
        control.add(botonVolumenMas);

        botonVolumenMenos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cambiarVolumen(-5);
            }
        });
        botonVolumenMas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cambiarVolumen(5);
            }
        });
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

        JLabel cabecera = new JLabel(nombre, SwingConstants.CENTER);
        cabecera.setOpaque(true);
        cabecera.setBackground(color);
        cabecera.setForeground(Color.WHITE);
        cabecera.setFont(new Font("SansSerif", Font.BOLD, 19));
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
            setOpaque(true);
            setBackground(new Color(10, 30, 50));
            setForeground(Color.WHITE);
            setBorder(BorderFactory.createLineBorder(new Color(180, 150, 75)));
            setFocusPainted(false);
            setFocusable(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    iniciarCaptura();
                }
            });
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
            setBackground(new Color(40, 70, 96));
            setForeground(DORADO_CLARO);
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
            setBackground(new Color(10, 30, 50));
            setForeground(Color.WHITE);
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
    }
}
