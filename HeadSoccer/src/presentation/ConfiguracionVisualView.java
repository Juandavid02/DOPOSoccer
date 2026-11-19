package presentation;

import domain.PerfilJugador;
import domain.Tienda;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;


public class ConfiguracionVisualView extends VistaBase {

    private static final String RUTA_BOTONES = "resources/images/Botones/";
    private static final Color COLOR_TEXTO = new Color(22, 48, 68);
    private static final int ANCHO_READY_PVP = 280;
    private static final int ANCHO_READY_SOLO = 250;

    private final BufferedImage imagenReadyPvp =
            RecursosImagen.cargar(RUTA_BOTONES + "ReadyPerfil.png");
    private final BufferedImage imagenReadySolo =
            RecursosImagen.cargar(RUTA_BOTONES + "ReadySolo.png");

    private JButton botonReady;

    private final List<TarjetaVisual> tarjetasBalon = new ArrayList<>();
    private final List<TarjetaVisual> tarjetasEscenario = new ArrayList<>();

    private TarjetaVisual balonSeleccionado;
    private TarjetaVisual escenarioSeleccionado;
    private Tienda desbloqueos;
    private PerfilJugador perfil1;
    private PerfilJugador perfil2;

    /**
     * Crea la pantalla de selección de balón y escenario.
     */
    public ConfiguracionVisualView() {
        super("resources/images/Ventanas/FondoSelecionPersonaje.jpg");
        setTitulo("BALÓN Y ESCENARIO");
        prepareViewCentro();
    }

    /**
     * Guarda el estado de desbloqueos y actualiza las tarjetas.
     *
     * @param desbloqueos desbloqueos de los perfiles
     */
    public void setDesbloqueos(Tienda desbloqueos) {
        this.desbloqueos = desbloqueos;
        actualizarDesbloqueos();
    }

    /**
     * Indica qué perfiles juegan el partido. Balones y escenarios quedan
     * disponibles si alguno de los dos perfiles los desbloqueó.
     *
     * @param perfil1 perfil del jugador 1
     * @param perfil2 perfil del jugador 2, o null si juega la máquina
     */
    public void setPerfilesActivos(PerfilJugador perfil1, PerfilJugador perfil2) {
        this.perfil1 = perfil1;
        this.perfil2 = perfil2;
    }

    /**
     * Actualiza las cadenas de los balones y escenarios bloqueados.
     */
    public void actualizarDesbloqueos() {
        if (desbloqueos == null) {
            return;
        }

        for (TarjetaVisual tarjeta : tarjetasBalon) {
            tarjeta.setBloqueada(!estaDesbloqueadoBalon(
                    tarjeta.getOpcion().getNombre()));
        }
        for (TarjetaVisual tarjeta : tarjetasEscenario) {
            tarjeta.setBloqueada(!estaDesbloqueadoEscenario(
                    tarjeta.getOpcion().getNombre()));
        }

        if (balonSeleccionado != null && balonSeleccionado.isBloqueada()) {
            balonSeleccionado = null;
        }
        if (escenarioSeleccionado != null && escenarioSeleccionado.isBloqueada()) {
            escenarioSeleccionado = null;
        }
        seleccionarIniciales();
        repaint();
    }

    /**
     * Indica si un balón está desbloqueado para los perfiles activos.
     *
     * @param nombre nombre del balón
     * @return true si está desbloqueado
     */
    private boolean estaDesbloqueadoBalon(String nombre) {
        if (perfil1 == null && perfil2 == null) {
            return true;
        }
        return desbloqueos.estaDesbloqueadoBalon(nombre, perfil1)
                || desbloqueos.estaDesbloqueadoBalon(nombre, perfil2);
    }

    /**
     * Indica si un escenario está desbloqueado para los perfiles activos.
     *
     * @param nombre nombre del escenario
     * @return true si está desbloqueado
     */
    private boolean estaDesbloqueadoEscenario(String nombre) {
        if (perfil1 == null && perfil2 == null) {
            return true;
        }
        return desbloqueos.estaDesbloqueadoEscenario(nombre, perfil1)
                || desbloqueos.estaDesbloqueadoEscenario(nombre, perfil2);
    }

    /**
     * Construye el contenido central de la pantalla.
     */
    private void prepareViewCentro() {
        TarjetaPanel tarjeta = new TarjetaPanel();
        tarjeta.setLayout(new BorderLayout(0, 12));
        tarjeta.setBorder(new EmptyBorder(20, 26, 16, 26));
        tarjeta.setPreferredSize(new Dimension(1120, 560));

        tarjeta.add(prepararSecciones(), BorderLayout.CENTER);
        tarjeta.add(prepararReady(), BorderLayout.SOUTH);

        setCentro(tarjeta, 10, 26);
    }

    /**
     * Prepara las secciones de balones y escenarios.
     *
     * @return panel con ambas secciones
     */
    private JPanel prepararSecciones() {
        JPanel secciones = new JPanel(new GridBagLayout());
        secciones.setOpaque(false);

        GridBagConstraints c = new GridBagConstraints();
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 1;

        c.gridx = 0;
        c.weightx = 0.42;
        c.insets = new Insets(0, 0, 0, 24);
        secciones.add(prepararBalones(), c);

        c.gridx = 1;
        c.weightx = 0.58;
        c.insets = new Insets(0, 0, 0, 0);
        secciones.add(prepararEscenarios(), c);

        return secciones;
    }

    /**
     * Prepara la cuadrícula de balones disponibles.
     *
     * @return panel con las tarjetas de balones
     */
    private JPanel prepararBalones() {
        JLabel titulo = crearEtiqueta("BALÓN");

        JPanel cuadricula = new JPanel(new GridLayout(3, 2, 8, 8));
        cuadricula.setOpaque(false);

        for (CatalogoVisual.Opcion opcion : CatalogoVisual.getBalones()) {
            TarjetaVisual tarjeta = new TarjetaVisual(opcion, true);
            tarjetasBalon.add(tarjeta);
            cuadricula.add(tarjeta);
        }
        // Tres espacios reservados para futuros balones.
        cuadricula.add(TarjetaPanel.vacia(40));
        cuadricula.add(TarjetaPanel.vacia(40));
        cuadricula.add(TarjetaPanel.vacia(40));

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titulo);
        panel.add(Box.createVerticalStrut(10));
        panel.add(cuadricula);

        return panel;
    }

    /**
     * Prepara la cuadrícula de escenarios disponibles.
     *
     * @return panel con las tarjetas de escenarios
     */
    private JPanel prepararEscenarios() {
        JLabel titulo = crearEtiqueta("ESCENARIO");

        JPanel cuadricula = new JPanel(new GridLayout(2, 3, 10, 8));
        cuadricula.setOpaque(false);

        for (CatalogoVisual.Opcion opcion : CatalogoVisual.getEscenarios()) {
            TarjetaVisual tarjeta = new TarjetaVisual(opcion, false);
            tarjetasEscenario.add(tarjeta);
            cuadricula.add(tarjeta);
        }
        // Dos espacios reservados para futuros escenarios.
        cuadricula.add(TarjetaPanel.vacia(40));
        cuadricula.add(TarjetaPanel.vacia(40));

        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setOpaque(false);
        panel.add(titulo, BorderLayout.NORTH);
        panel.add(cuadricula, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Prepara el botón READY en la parte inferior.
     *
     * @return panel con el botón READY
     */
    private JPanel prepararReady() {
        botonReady = BotonImagen.crear(imagenReadyPvp, ANCHO_READY_PVP);

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panel.setOpaque(false);
        panel.add(botonReady);
        return panel;
    }

    /**
     * Cambia el botón READY entre la versión de dos jugadores y la de uno
     * solo, según la modalidad.
     *
     * @param solo true para PvsM
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
     * Crea una etiqueta de título con el estilo de la pantalla.
     *
     * @param texto texto de la etiqueta
     * @return etiqueta creada
     */
    private static JLabel crearEtiqueta(String texto) {
        JLabel etiqueta = new JLabel(texto, SwingConstants.CENTER);
        etiqueta.setFont(new Font("SansSerif", Font.BOLD, 20));
        etiqueta.setForeground(COLOR_TEXTO);
        return etiqueta;
    }

    /**
     * Marca una tarjeta como balón seleccionado.
     *
     * @param tarjeta tarjeta de balón elegida
     */
    public void seleccionarBalon(TarjetaVisual tarjeta) {
        if (tarjeta == null || tarjeta.isBloqueada()) {
            return;
        }
        for (TarjetaVisual actual : tarjetasBalon) {
            actual.setSeleccionada(actual == tarjeta);
        }
        balonSeleccionado = tarjeta;
        repaint();
    }

    /**
     * Marca una tarjeta como escenario seleccionado.
     *
     * @param tarjeta tarjeta de escenario elegida
     */
    public void seleccionarEscenario(TarjetaVisual tarjeta) {
        if (tarjeta == null || tarjeta.isBloqueada()) {
            return;
        }
        for (TarjetaVisual actual : tarjetasEscenario) {
            actual.setSeleccionada(actual == tarjeta);
        }
        escenarioSeleccionado = tarjeta;
        repaint();
    }

    /**
     * Selecciona el primer balón y escenario desbloqueados si no hay ninguno.
     */
    public void seleccionarIniciales() {
        if (balonSeleccionado == null) {
            for (TarjetaVisual tarjeta : tarjetasBalon) {
                if (!tarjeta.isBloqueada()) {
                    seleccionarBalon(tarjeta);
                    break;
                }
            }
        }
        if (escenarioSeleccionado == null) {
            for (TarjetaVisual tarjeta : tarjetasEscenario) {
                if (!tarjeta.isBloqueada()) {
                    seleccionarEscenario(tarjeta);
                    break;
                }
            }
        }
    }

    /**
     * Devuelve las tarjetas de balones.
     *
     * @return lista de tarjetas de balones
     */
    public List<TarjetaVisual> getTarjetasBalon() {
        return tarjetasBalon;
    }

    /**
     * Devuelve las tarjetas de escenarios.
     *
     * @return lista de tarjetas de escenarios
     */
    public List<TarjetaVisual> getTarjetasEscenario() {
        return tarjetasEscenario;
    }

    /**
     * Devuelve la opción de balón seleccionada.
     *
     * @return opción seleccionada o null si no hay
     */
    public CatalogoVisual.Opcion getBalonSeleccionado() {
        if (balonSeleccionado == null || balonSeleccionado.isBloqueada()) {
            return null;
        }
        return balonSeleccionado.getOpcion();
    }

    /**
     * Devuelve la opción de escenario seleccionada.
     *
     * @return opción seleccionada o null si no hay
     */
    public CatalogoVisual.Opcion getEscenarioSeleccionado() {
        if (escenarioSeleccionado == null || escenarioSeleccionado.isBloqueada()) {
            return null;
        }
        return escenarioSeleccionado.getOpcion();
    }

    /**
     * Devuelve el botón READY.
     *
     * @return botón READY
     */
    public JButton getBotonReady() {
        return botonReady;
    }

    // ------------------------------------------------------------------
    // Tarjeta de balón o escenario.
    // ------------------------------------------------------------------
    public static final class TarjetaVisual extends JButton {

        private static final BufferedImage CADENA = RecursosImagen.cargarCadena();

        private final CatalogoVisual.Opcion opcion;
        private final BufferedImage imagen;
        private final boolean esBalon;
        private boolean seleccionada;
        private boolean bloqueada;

        /**
         * Crea la tarjeta de un balón o escenario del catálogo.
         *
         * @param opcion opción del catálogo que representa
         * @param esBalon true si la tarjeta es de balón
         */
        private TarjetaVisual(CatalogoVisual.Opcion opcion, boolean esBalon) {
            this.opcion = opcion;
            this.esBalon = esBalon;
            BufferedImage original = RecursosImagen.cargar(opcion.getRuta());
            this.imagen = esBalon
                    ? RecursosImagen.recortarTransparente(original)
                    : original;

            setOpaque(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setRolloverEnabled(true);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        /**
         * Devuelve la opción del catálogo de la tarjeta.
         *
         * @return opción representada
         */
        public CatalogoVisual.Opcion getOpcion() {
            return opcion;
        }

        /**
         * Cambia el estado de selección de la tarjeta.
         *
         * @param seleccionada true si la tarjeta queda seleccionada
         */
        private void setSeleccionada(boolean seleccionada) {
            this.seleccionada = seleccionada;
            repaint();
        }

        /**
         * Cambia el estado de bloqueo de la tarjeta.
         *
         * @param bloqueada true si la tarjeta queda bloqueada
         */
        private void setBloqueada(boolean bloqueada) {
            this.bloqueada = bloqueada;
            repaint();
        }

        /**
         * Indica si la tarjeta está bloqueada.
         *
         * @return true si está bloqueada
         */
        private boolean isBloqueada() {
            return bloqueada;
        }

        /**
         * Dibuja la imagen ajustada, el nombre y la cadena si está bloqueada.
         *
         * @param g contexto gráfico donde se dibuja
         */
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            int ancho = getWidth();
            int alto = getHeight();
            int margen = 6;
            int altoTexto = 24;

            g2.setColor(bloqueada
                    ? new Color(228, 232, 236)
                    : new Color(246, 250, 252));
            g2.fillRoundRect(0, 0, ancho - 1, alto - 1, 16, 16);

            int areaAlto = Math.max(1, alto - altoTexto - margen * 2);
            double escala = Math.min(
                    (double) (ancho - margen * 2) / imagen.getWidth(),
                    (double) areaAlto / imagen.getHeight());
            int anchoImagen = Math.max(1,
                    (int) Math.round(imagen.getWidth() * escala));
            int altoImagen = Math.max(1,
                    (int) Math.round(imagen.getHeight() * escala));
            int x = (ancho - anchoImagen) / 2;
            int y = margen + (areaAlto - altoImagen) / 2;

            if (bloqueada) {
                g2.setComposite(AlphaComposite.getInstance(
                        AlphaComposite.SRC_OVER, 0.45f));
            }
            g2.drawImage(imagen, x, y, anchoImagen, altoImagen, null);
            g2.setComposite(AlphaComposite.SrcOver);

            g2.setFont(new Font("SansSerif", Font.BOLD, 15));
            FontMetrics fm = g2.getFontMetrics();
            g2.setColor(bloqueada
                    ? new Color(110, 120, 130)
                    : COLOR_TEXTO);
            g2.drawString(opcion.getNombre(),
                    (ancho - fm.stringWidth(opcion.getNombre())) / 2,
                    alto - 6);

            if (bloqueada && CADENA != null) {
                double escalaCadena = Math.max(
                        (double) ancho / CADENA.getWidth(),
                        (double) alto / CADENA.getHeight());
                int anchoCadena = (int) Math.ceil(
                        CADENA.getWidth() * escalaCadena);
                int altoCadena = (int) Math.ceil(
                        CADENA.getHeight() * escalaCadena);
                g2.drawImage(CADENA,
                        (ancho - anchoCadena) / 2,
                        (alto - altoCadena) / 2,
                        anchoCadena, altoCadena, null);
            }

            if (seleccionada) {
                g2.setStroke(new BasicStroke(4f));
                g2.setColor(new Color(255, 210, 70));
            } else {
                g2.setStroke(new BasicStroke(2f));
                g2.setColor(new Color(30, 70, 90));
            }
            g2.drawRoundRect(1, 1, ancho - 3, alto - 3, 16, 16);

            g2.dispose();
        }
    }
}
