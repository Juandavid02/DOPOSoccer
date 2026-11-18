package presentation;

import domain.Modalidad;
import domain.Resolucion;
import domain.TipoPartido;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ConfiguracionPartidoView extends VistaBase {

    private static final String RUTA_BOTONES = "resources/images/Botones/";
    private static final Color COLOR_TEXTO = new Color(22, 48, 68);
    private static final Color COLOR_ETIQUETA = new Color(30, 62, 86);

    private static final int TIEMPO_INICIAL = 3;
    private static final int GOLES_INICIALES = 5;
    private static final int MINIMO = 1;
    private static final int MAXIMO = 10;
    private static final int ANCHO_READY_PVP = 280;
    private static final int ANCHO_READY_SOLO = 250;

    private final BufferedImage imagenReadyPvp =
            RecursosImagen.cargar(RUTA_BOTONES + "ReadyPerfil.png");
    private final BufferedImage imagenReadySolo =
            RecursosImagen.cargar(RUTA_BOTONES + "ReadySolo.png");

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
    private JLabel etiquetaModalidad;

    private int tiempo = TIEMPO_INICIAL;
    private int goles = GOLES_INICIALES;

    /**
     * Crea la pantalla con el título por defecto.
     */
    public ConfiguracionPartidoView() {
        this("CONFIGURACIÓN DE PARTIDO");
    }

    /**
     * Crea la pantalla con un título propio.
     *
     * @param titulo título de la barra superior
     */
    public ConfiguracionPartidoView(String titulo) {
        super("resources/images/Ventanas/FondoPerfil.png");
        setTitulo(titulo);
        prepareViewCentro();
        actualizarControles();
    }

    /**
     * Construye el contenido central de la pantalla.
     */
    private void prepareViewCentro() {
        TarjetaPanel tarjeta = new TarjetaPanel();
        tarjeta.setLayout(new BorderLayout(0, 18));
        tarjeta.setBorder(new EmptyBorder(28, 40, 24, 40));
        tarjeta.setPreferredSize(new Dimension(860, 500));

        tarjeta.add(prepararFormulario(), BorderLayout.CENTER);
        tarjeta.add(prepararBotonReady(), BorderLayout.SOUTH);

        setCentro(tarjeta, 10, 30);
    }

    /**
     * Prepara el formulario con las opciones del partido.
     *
     * @return panel con el formulario
     */
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
        etiquetaModalidad = modalidad;
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

        botonTiempoMenos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cambiarTiempo(-1);
            }
        });
        botonTiempoMas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cambiarTiempo(1);
            }
        });
        botonGolesMenos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cambiarGoles(-1);
            }
        });
        botonGolesMas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cambiarGoles(1);
            }
        });
        botonTipoTiempo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarControles();
            }
        });
        botonTipoGoles.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarControles();
            }
        });

        return panel;
    }

    /**
     * Crea una copia de las restricciones con la columna y el ancho dados.
     *
     * @param base restricciones de partida
     * @param x columna de la cuadrícula
     * @param gridwidth número de columnas que ocupa
     * @return copia ajustada de las restricciones
     */
    private GridBagConstraints constraint(GridBagConstraints base, int x, int gridwidth) {
        GridBagConstraints c = (GridBagConstraints) base.clone();
        c.gridx = x;
        c.gridwidth = gridwidth;
        c.fill = GridBagConstraints.NONE;
        return c;
    }

    /**
     * Agrupa los botones de menos y más con el valor en el centro.
     *
     * @param menos botón para disminuir
     * @param valor etiqueta con el valor actual
     * @param mas botón para aumentar
     * @return panel con el control completo
     */
    private JPanel prepararControl(JButton menos, JLabel valor, JButton mas) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        panel.setOpaque(false);
        panel.add(menos);
        panel.add(valor);
        panel.add(mas);
        return panel;
    }

    /**
     * Prepara el botón READY en la parte inferior.
     *
     * @return panel con el botón READY
     */
    private JPanel prepararBotonReady() {
        botonReady = BotonImagen.crear(imagenReadyPvp, ANCHO_READY_PVP);

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panel.setOpaque(false);
        panel.add(botonReady);
        return panel;
    }

    /**
     * Crea una etiqueta con el estilo del formulario.
     *
     * @param texto texto de la etiqueta
     * @return etiqueta creada
     */
    private static JLabel crearEtiqueta(String texto) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(new Font("SansSerif", Font.BOLD, 17));
        etiqueta.setForeground(COLOR_ETIQUETA);
        return etiqueta;
    }

    /**
     * Crea una etiqueta vacía para mostrar un valor numérico.
     *
     * @return etiqueta de valor creada
     */
    private static JLabel crearValor() {
        JLabel valor = new JLabel("", SwingConstants.CENTER);
        valor.setFont(new Font("SansSerif", Font.BOLD, 19));
        valor.setForeground(COLOR_TEXTO);
        valor.setPreferredSize(new Dimension(110, 30));
        return valor;
    }

    /**
     * Ajusta el tiempo del partido dentro de los límites.
     *
     * @param delta cantidad de minutos que se suma
     */
    private void cambiarTiempo(int delta) {
        tiempo = Math.max(MINIMO, Math.min(MAXIMO, tiempo + delta));
        actualizarControles();
    }

    /**
     * Ajusta los goles del partido dentro de los límites.
     *
     * @param delta cantidad de goles que se suma
     */
    private void cambiarGoles(int delta) {
        goles = Math.max(MINIMO, Math.min(MAXIMO, goles + delta));
        actualizarControles();
    }

    /**
     * Actualiza los textos y el estado de los controles según el tipo elegido.
     */
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

    /**
     * Devuelve el tipo de partido elegido.
     *
     * @return tipo de partido seleccionado
     */
    public TipoPartido getTipoPartido() {
        if (botonTipoGoles.isSelected()) {
            return TipoPartido.GOLES;
        }
        return TipoPartido.TIEMPO;
    }

    /**
     * Devuelve el tiempo configurado en minutos.
     *
     * @return minutos del partido
     */
    public int getTiempo() {
        return tiempo;
    }

    /**
     * Devuelve los goles configurados.
     *
     * @return goles del partido
     */
    public int getGoles() {
        return goles;
    }

    /**
     * Devuelve la resolución elegida.
     *
     * @return resolución seleccionada
     */
    public Resolucion getResolucion() {
        return Resolucion.values()[comboResolucion.getSelectedIndex()];
    }

    /**
     * Devuelve el botón READY.
     *
     * @return botón READY
     */
    public JButton getBotonReady() {
        return botonReady;
    }

    /**
     * Indica qué modalidad se está configurando. La pantalla es la misma
     * para PvsP y PvsM; solo cambia esta etiqueta.
     *
     * @param modalidad modalidad del partido
     */
    public void setModalidad(Modalidad modalidad) {
        if (modalidad != null) {
            etiquetaModalidad.setText(modalidad.name());
        }
        setBotonReadySolo(modalidad == Modalidad.PvsM);
    }

    /**
     * Indica si el botón READY debe mostrar la versión de un solo jugador.
     *
     * @param solo true para PvsM o torneo; false para PvsP
     */
    public void setBotonReadySolo(boolean solo) {
        BufferedImage imagen = imagenReadyPvp;
        int ancho = ANCHO_READY_PVP;
        if (solo) {
            imagen = imagenReadySolo;
            ancho = ANCHO_READY_SOLO;
        }
        BotonImagen.cambiarImagen(botonReady, imagen, ancho);
    }

    /**
     * Permite mostrar un texto propio en la fila de modalidad, por ejemplo
     * "TORNEO" cuando la pantalla se usa para configurar el torneo.
     *
     * @param texto texto a mostrar
     */
    public void setModalidadTexto(String texto) {
        if (texto != null) {
            etiquetaModalidad.setText(texto);
        }
    }

    /**
     * Cambia el título de la barra superior para reutilizar esta pantalla en
     * el torneo.
     *
     * @param texto nuevo título
     */
    public void setTituloPantalla(String texto) {
        setTitulo(texto);
    }

    // ------------------------------------------------------------------
    // Botón de opción tipo TIEMPO / GOLES.
    // ------------------------------------------------------------------
    private static final class BotonOpcion extends JToggleButton {

        /**
         * Crea un botón de opción con el texto indicado.
         *
         * @param texto texto que muestra el botón
         */
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

        /**
         * Dibuja el botón de opción según su estado.
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
}
