package presentation;

import domain.Estadisticas;
import domain.ParametrosJuego;
import domain.PerfilIA;
import domain.TipoEstadistica;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.EnumMap;
import java.util.Map;

/**
 * Pantalla para elegir el perfil de la máquina y ajustar sus estadísticas.
 *
 * El perfil decide cómo juega la IA; las estadísticas deciden su
 * velocidad, salto, patada y vida. El jugador humano puede subirlas o
 * bajarlas entre 1 y 10 antes de empezar.
 */
public class SeleccionPerfilIAView extends VistaBase {

    private static final String RUTA_FONDO =
            "resources/images/Ventanas/FondoPerfil.png";
    private static final String RUTA_READY =
            "resources/images/Botones/ReadySolo.png";
    private static final String RUTA_IMAGENES_IA =
            "resources/images/TiposIA/";
    private static final Color COLOR_TEXTO = new Color(22, 48, 68);
    private static final Color COLOR_ETIQUETA = new Color(30, 62, 86);

    private final PanelEstadisticasIA panelEstadisticas =
            new PanelEstadisticasIA();
    private final ButtonGroup grupo = new ButtonGroup();
    private final JButton botonReady;

    private PerfilIA perfilSeleccionado;

    /**
     * Crea la vista de selección del perfil de la IA.
     */
    public SeleccionPerfilIAView() {
        super(RUTA_FONDO);
        setTitulo("PERFIL DE LA IA");
        botonReady = BotonImagen.crear(RUTA_READY, 250);

        TarjetaPanel tarjeta = new TarjetaPanel();
        tarjeta.setLayout(new BorderLayout(0, 14));
        tarjeta.setBorder(new EmptyBorder(24, 34, 18, 34));
        tarjeta.setPreferredSize(new Dimension(1080, 600));
        tarjeta.add(prepararCentro(), BorderLayout.CENTER);
        tarjeta.add(prepararBotonReady(), BorderLayout.SOUTH);

        setCentro(tarjeta, 8, 24);
    }

    /**
     * Crea el panel central con las tarjetas y las estadísticas.
     *
     * @return panel central de la vista
     */
    private JPanel prepararCentro() {
        JPanel centro = new JPanel(new BorderLayout(26, 0));
        centro.setOpaque(false);
        centro.add(prepararTarjetas(), BorderLayout.WEST);
        centro.add(panelEstadisticas, BorderLayout.CENTER);
        return centro;
    }

    /**
     * Crea las tarjetas con los tipos de IA disponibles.
     *
     * @return panel con las tarjetas de los perfiles
     */
    private JPanel prepararTarjetas() {
        JLabel titulo = new JLabel("TIPO DE IA");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        titulo.setForeground(COLOR_ETIQUETA);

        JPanel cuadricula = new JPanel(new GridLayout(2, 2, 14, 14));
        cuadricula.setOpaque(false);

        for (PerfilIA perfil : PerfilIA.values()) {
            TarjetaOpcion<PerfilIA> tarjeta = new TarjetaOpcion<>(
                    perfil, perfil.name(), descripcion(perfil),
                    RecursosImagen.recortarContenido(
                            RecursosImagen.cargar(imagen(perfil))));
            tarjeta.setPreferredSize(new Dimension(230, 195));
            grupo.add(tarjeta);
            tarjeta.addActionListener(e -> perfilSeleccionado = perfil);
            cuadricula.add(tarjeta);
        }

        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(486, 0));
        panel.add(titulo, BorderLayout.NORTH);
        panel.add(cuadricula, BorderLayout.CENTER);
        return panel;
    }

    /**
     * Devuelve la descripción del perfil de IA indicado.
     *
     * @param perfil perfil de IA consultado
     * @return descripción del perfil
     */
    private static String descripcion(PerfilIA perfil) {
        switch (perfil) {
            case TORPE:
                return "Se mueve y decide al azar.";
            case OFENSIVA:
                return "Su prioridad es anotar en el arco rival.";
            case DEFENSIVA:
                return "Su prioridad es cuidar su propio arco.";
            case EXPERTA:
                return "Decide según el estado del partido.";
            default:
                return "";
        }
    }

    /**
     * Devuelve la ruta de la imagen del perfil de IA indicado.
     *
     * @param perfil perfil de IA consultado
     * @return ruta de la imagen del perfil
     */
    private static String imagen(PerfilIA perfil) {
        switch (perfil) {
            case TORPE:
                return RUTA_IMAGENES_IA + "Torpe (1).png";
            case OFENSIVA:
                return RUTA_IMAGENES_IA + "Ofensiva.png";
            case DEFENSIVA:
                return RUTA_IMAGENES_IA + "Defensiva.png";
            case EXPERTA:
                return RUTA_IMAGENES_IA + "Experta.png";
            default:
                return null;
        }
    }

    /**
     * Crea el panel que contiene el botón Ready.
     *
     * @return panel con el botón Ready
     */
    private JPanel prepararBotonReady() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panel.setOpaque(false);
        panel.add(botonReady);
        return panel;
    }

    /**
     * Reinicia la pantalla sin ningún perfil elegido y con las estadísticas
     * en su valor inicial.
     */
    public void reiniciar() {
        perfilSeleccionado = null;
        grupo.clearSelection();
        panelEstadisticas.reiniciar();
        repaint();
    }

    /**
     * Devuelve el perfil de IA seleccionado.
     *
     * @return perfil seleccionado, o null si no hay ninguno
     */
    public PerfilIA getPerfilSeleccionado() {
        return perfilSeleccionado;
    }

    /**
     * Devuelve las estadísticas configuradas para la IA.
     *
     * @return estadísticas actuales de la IA
     */
    public Estadisticas getEstadisticasIA() {
        return panelEstadisticas.getEstadisticas();
    }

    /**
     * Devuelve el botón Ready de la vista.
     *
     * @return botón Ready
     */
    public JButton getBotonReady() {
        return botonReady;
    }

    // ------------------------------------------------------------------
    // Panel de estadísticas ajustables.
    // ------------------------------------------------------------------
    private static final class PanelEstadisticasIA extends JPanel {

        private final Map<TipoEstadistica, Integer> valores =
                new EnumMap<>(TipoEstadistica.class);
        private final Map<TipoEstadistica, JLabel> etiquetasValor =
                new EnumMap<>(TipoEstadistica.class);
        private final Map<TipoEstadistica, BarraStat> barras =
                new EnumMap<>(TipoEstadistica.class);
        private final Map<TipoEstadistica, BotonPaso> botonesMenos =
                new EnumMap<>(TipoEstadistica.class);
        private final Map<TipoEstadistica, BotonPaso> botonesMas =
                new EnumMap<>(TipoEstadistica.class);

        /**
         * Crea el panel de estadísticas de la IA.
         */
        private PanelEstadisticasIA() {
            setOpaque(false);
            setLayout(new BorderLayout(0, 12));

            JLabel titulo = new JLabel("ESTADÍSTICAS DE LA IA");
            titulo.setFont(new Font("SansSerif", Font.BOLD, 18));
            titulo.setForeground(COLOR_ETIQUETA);

            JLabel ayuda = new JLabel(
                    "Ajusta la velocidad, el salto, la patada y la vida de la máquina.");
            ayuda.setFont(new Font("SansSerif", Font.PLAIN, 14));
            ayuda.setForeground(new Color(70, 96, 116));

            JPanel cabecera = new JPanel();
            cabecera.setOpaque(false);
            cabecera.setLayout(new BoxLayout(cabecera, BoxLayout.Y_AXIS));
            cabecera.add(titulo);
            cabecera.add(Box.createVerticalStrut(2));
            cabecera.add(ayuda);

            add(cabecera, BorderLayout.NORTH);
            add(prepararFilas(), BorderLayout.CENTER);

            reiniciar();
        }

        /**
         * Crea las filas con las estadísticas ajustables.
         *
         * @return panel con las filas de estadísticas
         */
        private JPanel prepararFilas() {
            JPanel filas = new JPanel(new GridBagLayout());
            filas.setOpaque(false);

            GridBagConstraints c = new GridBagConstraints();
            c.gridy = 0;
            c.insets = new Insets(4, 0, 4, 8);
            c.anchor = GridBagConstraints.WEST;

            for (TipoEstadistica tipo : TipoEstadistica.values()) {
                int fila = c.gridy;

                JLabel nombre = new JLabel(tipo.getEtiqueta());
                nombre.setFont(new Font("SansSerif", Font.BOLD, 15));
                nombre.setForeground(COLOR_TEXTO);
                c.gridx = 0;
                c.gridy = fila;
                filas.add(nombre, c);

                BarraStat barra = new BarraStat();
                barras.put(tipo, barra);
                c.gridx = 1;
                filas.add(barra, c);

                JLabel valor = new JLabel("", SwingConstants.CENTER);
                valor.setFont(new Font("SansSerif", Font.BOLD, 16));
                valor.setForeground(COLOR_TEXTO);
                valor.setPreferredSize(new Dimension(28, 24));
                etiquetasValor.put(tipo, valor);
                c.gridx = 2;
                filas.add(valor, c);

                BotonPaso menos = new BotonPaso("-");
                BotonPaso mas = new BotonPaso("+");
                botonesMenos.put(tipo, menos);
                botonesMas.put(tipo, mas);
                menos.addActionListener(e -> cambiar(tipo, -1));
                mas.addActionListener(e -> cambiar(tipo, 1));

                c.gridx = 3;
                c.insets = new Insets(4, 0, 4, 4);
                filas.add(menos, c);
                c.gridx = 4;
                c.insets = new Insets(4, 0, 4, 8);
                filas.add(mas, c);

                c.gridy = fila + 1;
            }

            return filas;
        }

        /**
         * Reinicia todas las estadísticas a su valor inicial.
         */
        private void reiniciar() {
            for (TipoEstadistica tipo : TipoEstadistica.values()) {
                valores.put(tipo, ParametrosJuego.ESTADISTICA_INICIAL);
            }
            actualizar();
        }

        /**
         * Cambia el valor de una estadística dentro de sus límites.
         *
         * @param tipo estadística que se va a modificar
         * @param delta cantidad que se suma al valor actual
         */
        private void cambiar(TipoEstadistica tipo, int delta) {
            int actual = valores.get(tipo);
            int nuevo = Math.max(ParametrosJuego.MIN_ESTADISTICA,
                    Math.min(ParametrosJuego.MAX_ESTADISTICA, actual + delta));
            valores.put(tipo, nuevo);
            actualizar();
        }

        /**
         * Actualiza las barras, los valores y los botones del panel.
         */
        private void actualizar() {
            for (TipoEstadistica tipo : TipoEstadistica.values()) {
                int valor = valores.get(tipo);
                etiquetasValor.get(tipo).setText(String.valueOf(valor));
                barras.get(tipo).setValor(valor);
                botonesMenos.get(tipo).setEnabled(
                        valor > ParametrosJuego.MIN_ESTADISTICA);
                botonesMas.get(tipo).setEnabled(
                        valor < ParametrosJuego.MAX_ESTADISTICA);
            }
            repaint();
        }

        /**
         * Devuelve las estadísticas con los valores elegidos.
         *
         * @return estadísticas configuradas
         */
        private Estadisticas getEstadisticas() {
            return new Estadisticas(
                    valores.get(TipoEstadistica.VELOCIDAD),
                    valores.get(TipoEstadistica.SALTO),
                    valores.get(TipoEstadistica.PATADA),
                    valores.get(TipoEstadistica.SUPERVIVENCIA));
        }
    }

    // ------------------------------------------------------------------
    // Barra de 10 segmentos.
    // ------------------------------------------------------------------
    private static final class BarraStat extends JComponent {

        private int valor;

        /**
         * Crea la barra de segmentos de la estadística.
         */
        private BarraStat() {
            setPreferredSize(new Dimension(150, 16));
        }

        /**
         * Cambia el valor que muestra la barra.
         *
         * @param valor nuevo valor de la barra
         */
        private void setValor(int valor) {
            this.valor = valor;
            repaint();
        }

        /**
         * Dibuja los segmentos de la barra según el valor actual.
         *
         * @param g contexto gráfico del componente
         */
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            int segmentos = ParametrosJuego.MAX_ESTADISTICA;
            int separacion = 3;
            int anchoSegmento = Math.max(4,
                    (getWidth() - separacion * (segmentos - 1)) / segmentos);
            int alto = Math.max(8, getHeight() - 2);

            for (int i = 0; i < segmentos; i++) {
                int x = i * (anchoSegmento + separacion);
                if (i < valor) {
                    g2.setColor(new Color(52, 170, 110));
                } else {
                    g2.setColor(new Color(30, 70, 95, 45));
                }
                g2.fillRoundRect(x, 1, anchoSegmento, alto, 5, 5);
            }

            g2.dispose();
        }
    }
}
