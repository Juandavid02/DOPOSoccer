package presentation;

import domain.ConfiguracionIA;
import domain.Estadisticas;
import domain.ParametrosJuego;
import domain.PerfilJugador;
import domain.Personaje;
import domain.Poder;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class SeleccionPersonajeView extends JPanel {

    private static final String RUTA_BOTONES = "resources/images/Botones/";
    private static final String RUTA_PERSONAJES = "resources/images/personajes/";
    private static final Color COLOR_JUGADOR1 = new Color(48, 132, 220);
    private static final Color COLOR_JUGADOR2 = new Color(214, 62, 72);
    private static final Color COLOR_IA = new Color(112, 82, 190);

    private final List<BotonCaja> botonesCaja = new ArrayList<>();
    private Desbloqueos desbloqueos;
    private BufferedImage fondo;
    private PanelLateral panelJugador1;
    private PanelLateral panelJugador2;
    private PanelCentro panelCentro;
    private JButton botonAtras;
    private BotonSonido botonSonido;
    private boolean modoIA;
    private boolean soloHumano;
    private PerfilJugador perfil1;
    private PerfilJugador perfil2;

    /**
     * Crea la vista de selección de personaje y arma sus componentes.
     */
    public SeleccionPersonajeView() {
        prepareView();
    }

    /**
     * Inicializa los paneles, las imágenes y los botones de la vista.
     */
    private void prepareView() {
        setLayout(new GridBagLayout());
        setOpaque(true);
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(1920, 1080));

        fondo = RecursosImagen.cargar("resources/images/Ventanas/FondoSelecionPersonaje.jpg");

        botonAtras = BotonImagen.crear(RUTA_BOTONES + "Retroceder.png",
                VistaBase.ANCHO_BOTON_ATRAS);
        botonSonido = new BotonSonido(VistaBase.ANCHO_BOTON_SONIDO);

        panelJugador1 = new PanelLateral(true, RUTA_PERSONAJES + "SeleccionPersonaje1.png");
        panelJugador2 = new PanelLateral(false, RUTA_PERSONAJES + "SeleccionPersonaje2.png");
        panelCentro = new PanelCentro();
        botonesCaja.addAll(panelCentro.getBotones());

        // Las columnas mantienen siempre la misma proporción sin importar
        // qué tan grandes sean los elementos que aparecen dentro.
        panelJugador1.setPreferredSize(new Dimension(0, 0));
        panelJugador1.setMinimumSize(new Dimension(0, 0));
        panelCentro.setPreferredSize(new Dimension(0, 0));
        panelCentro.setMinimumSize(new Dimension(0, 0));
        panelJugador2.setPreferredSize(new Dimension(0, 0));
        panelJugador2.setMinimumSize(new Dimension(0, 0));

        panelJugador1.getBarraSuperior().add(botonAtras, BorderLayout.WEST);
        panelJugador2.getBarraSuperior().add(botonSonido, BorderLayout.EAST);

        aplicarLayout();
    }

    /**
     * Organiza los paneles laterales y el central según el modo de juego.
     */
    private void aplicarLayout() {
        removeAll();

        GridBagConstraints c = new GridBagConstraints();
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 1;

        c.gridx = 0;
        c.weightx = soloHumano ? 0.42 : 0.26;
        add(panelJugador1, c);

        c.gridx = 1;
        c.weightx = soloHumano ? 0.58 : 0.48;
        add(panelCentro, c);

        if (!soloHumano) {
            c.gridx = 2;
            c.weightx = 0.26;
            add(panelJugador2, c);
        }

        revalidate();
        repaint();
    }

    /**
     * Dibuja el fondo de la pantalla de selección.
     *
     * @param g contexto gráfico usado por Swing
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

        g2.drawImage(fondo, (getWidth() - ancho) / 2, (getHeight() - alto) / 2,
                ancho, alto, null);

        // Capa azul marino semitransparente para dar contraste a los paneles.
        g2.setColor(new Color(8, 24, 46, 135));
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.dispose();
    }

    /**
     * Refresca los paneles y las celdas con los personajes elegidos.
     *
     * @param personaje1 personaje elegido por el jugador 1
     * @param personaje2 personaje elegido por el jugador 2
     * @param turno jugador al que le toca elegir
     * @param perfilActivo perfil que está eligiendo, o null
     */
    public void actualizar(Personaje personaje1, Personaje personaje2, int turno,
                           PerfilJugador perfilActivo) {
        Personaje segundoVisible = soloHumano ? null : personaje2;

        panelJugador1.mostrar(CatalogoSeleccion.buscar(personaje1));
        panelJugador2.mostrar(CatalogoSeleccion.buscar(segundoVisible));

        PerfilJugador perfilQueElige = perfilActivo;
        if (perfilQueElige == null && turno == 1) {
            perfilQueElige = this.perfil1;
        }

        for (BotonCaja boton : botonesCaja) {
            actualizarBoton(boton, personaje1, segundoVisible,
                    turno, perfilQueElige);
        }

        boolean ambosListos = personaje1 != null
                && (soloHumano || personaje2 != null);
        panelCentro.actualizarTurno(turno, ambosListos, modoIA && !soloHumano);
    }

    /**
     * Vuelve la selección al estado inicial, sin personajes elegidos.
     */
    public void reiniciar() {
        actualizar(null, null, 1, perfil1);
    }

    /**
     * Muestra las estadísticas de los perfiles que jugarán el partido.
     *
     * @param perfil1 perfil del jugador 1
     * @param perfil2 perfil del jugador 2
     */
    public void setPerfiles(PerfilJugador perfil1, PerfilJugador perfil2) {
        this.perfil1 = perfil1;
        this.perfil2 = perfil2;
        modoIA = false;
        panelCentro.setBotonReadySolo(false);
        panelJugador1.setPerfil(perfil1);
        panelJugador2.setPerfil(perfil2);
        panelJugador2.setEtiqueta("J2");
        panelJugador2.setColor(COLOR_JUGADOR2);
        panelJugador2.setPermiteCambiar(true);
        panelJugador2.setAleatorioVisible(false);
    }

    /**
     * Muestra las estadísticas del perfil humano de J1. En PvsM se usa para
     * que J1 conserve el perfil elegido mientras el lado derecho es la IA.
     *
     * @param perfil perfil del jugador humano
     */
    public void setPerfilHumano(PerfilJugador perfil) {
        this.perfil1 = perfil;
        this.perfil2 = null;
        panelJugador1.setPerfil(perfil);
    }

    /**
     * Convierte el lado derecho en la máquina: etiqueta "IA", estadísticas
     * configuradas y botones para cambiar su personaje o elegirlo al azar.
     *
     * @param configuracionIA configuración de la máquina
     */
    public void setLadoIA(ConfiguracionIA configuracionIA) {
        modoIA = true;
        panelCentro.setBotonReadySolo(true);
        panelJugador2.setEtiqueta("IA");
        panelJugador2.setColor(COLOR_IA);
        panelJugador2.setPermiteCambiar(true);
        panelJugador2.setAleatorioVisible(true);
        if (configuracionIA != null) {
            panelJugador2.setEstadisticas(
                    "IA · " + configuracionIA.getPerfil().name(),
                    configuracionIA.getEstadisticas());
        }
    }

    /**
     * Modo torneo: el humano solo elige su personaje y el panel de la
     * máquina se oculta.
     *
     * @param soloHumano true para ocultar el lado derecho
     */
    public void setSoloHumano(boolean soloHumano) {
        this.soloHumano = soloHumano;
        panelCentro.setBotonReadySolo(soloHumano);
        panelJugador1.setFactorPersonaje(soloHumano ? 1.32 : 1.0);
        aplicarLayout();
    }

    /**
     * Indica si un personaje está desbloqueado para un perfil. Si el perfil
     * es null (por ejemplo, la máquina) se considera disponible.
     *
     * @param personaje personaje a consultar
     * @param perfil perfil que quiere usar el personaje, o null
     * @return true si se puede usar
     */
    public boolean estaDesbloqueado(Personaje personaje, PerfilJugador perfil) {
        return personaje != null && (perfil == null || desbloqueos == null
                || desbloqueos.estaDesbloqueadoPersonaje(
                        personaje.getNombre(), perfil));
    }

    /**
     * Define el control de desbloqueos usado por la vista.
     *
     * @param desbloqueos control de personajes desbloqueados
     */
    public void setDesbloqueos(Desbloqueos desbloqueos) {
        this.desbloqueos = desbloqueos;
        repaint();
    }

    /**
     * Actualiza el estado visual de una celda de la grilla.
     *
     * @param boton celda que se va a actualizar
     * @param personaje1 personaje elegido por el jugador 1
     * @param personaje2 personaje elegido por el jugador 2
     * @param turno jugador al que le toca elegir
     * @param perfilQueElige perfil que está eligiendo
     */
    private void actualizarBoton(BotonCaja boton, Personaje personaje1,
                                 Personaje personaje2, int turno,
                                 PerfilJugador perfilQueElige) {
        CatalogoSeleccion.Opcion opcion = boton.getOpcion();
        if (opcion == null) {
            boton.setJugadorSeleccionado(0);
            boton.setEnabled(false);
            return;
        }

        boolean esJugador1 = personaje1 != null && opcion.es(personaje1);
        boolean esJugador2 = personaje2 != null && opcion.es(personaje2);
        boolean elegido = esJugador1 || esJugador2;

        // Con ambos personajes elegidos ya no se puede cambiar desde la
        // grilla, así que no se muestran cadenas que confundan.
        boolean bloqueado = turno != 0 && !elegido
                && !estaDesbloqueado(opcion.getPersonaje(), perfilQueElige);

        boton.setBloqueado(bloqueado);
        boton.setJugadorSeleccionado(esJugador1 ? 1 : esJugador2 ? 2 : 0);
        boton.setEnabled(!elegido && !bloqueado);
        boton.repaint();
    }

    /**
     * Devuelve las celdas de la grilla de personajes.
     *
     * @return lista de celdas de la grilla
     */
    public List<BotonCaja> getBotonesCaja() {
        return botonesCaja;
    }

    /**
     * Devuelve el botón para cambiar el personaje del jugador 1.
     *
     * @return botón de cambio del panel izquierdo
     */
    public JButton getBotonCambiar1() {
        return panelJugador1.getBotonCambiar();
    }

    /**
     * Devuelve el botón para cambiar el personaje del jugador 2.
     *
     * @return botón de cambio del panel derecho
     */
    public JButton getBotonCambiar2() {
        return panelJugador2.getBotonCambiar();
    }

    /**
     * Devuelve el botón que confirma la selección.
     *
     * @return botón READY del panel central
     */
    public JButton getBotonReady() {
        return panelCentro.getBotonReady();
    }

    /**
     * Botón que elige al azar el personaje de la máquina.
     *
     * @return botón de aleatorio del panel derecho
     */
    public JButton getBotonAleatorio() {
        return panelJugador2.getBotonAleatorio();
    }

    /**
     * Devuelve el botón para volver a la pantalla anterior.
     *
     * @return botón de retroceso
     */
    public JButton getBotonAtras() {
        return botonAtras;
    }

    /**
     * Devuelve el botón que activa o silencia el sonido.
     *
     * @return botón de sonido
     */
    public BotonSonido getBotonSonido() {
        return botonSonido;
    }

    /**
     * Escala una imagen respetando su proporción para que quepa dentro de
     * un ancho y un alto máximos (los títulos de los personajes tienen
     * proporciones muy distintas entre sí).
     *
     * @param imagen imagen original
     * @param maxAncho ancho máximo disponible
     * @param maxAlto alto máximo disponible
     * @return el icono escalado
     */
    private static ImageIcon crearIconoAjustado(BufferedImage imagen, int maxAncho, int maxAlto) {
        double escala = Math.min(
                (double) maxAncho / imagen.getWidth(),
                (double) maxAlto / imagen.getHeight());
        int ancho = Math.max(1, (int) Math.round(imagen.getWidth() * escala));
        int alto = Math.max(1, (int) Math.round(imagen.getHeight() * escala));
        return new ImageIcon(imagen.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH));
    }

    // ------------------------------------------------------------------
    // Panel lateral con la bandera y el personaje elegido.
    // ------------------------------------------------------------------
    private static final class PanelLateral extends JPanel {

        private final boolean jugador1;
        private final BufferedImage base;
        private final PanelVistaPersonaje vista;
        private final JLabel titulo;
        private final JButton botonCambiar;
        private final JButton botonAleatorio;
        private final JPanel barraSuperior;
        private final BufferedImage imagenCambiar =
                RecursosImagen.cargar(RUTA_BOTONES + "CambiarPerfil.png");
        private final BufferedImage imagenAleatorio =
                RecursosImagen.cargar(RUTA_BOTONES + "Aleatorio.png");
        private int anchoBotones = -1;
        private Color colorJugador;
        private String etiquetaJugador;
        private boolean permiteCambiar = true;
        private final PanelPoder panelPoder;
        private final PanelEstadisticas estadisticas;

        private BufferedImage bandera;

        /**
         * Crea el panel lateral de uno de los jugadores.
         *
         * @param jugador1 true si el panel corresponde al jugador 1
         * @param rutaBase ruta de la imagen base del panel
         */
        private PanelLateral(boolean jugador1, String rutaBase) {
            this.jugador1 = jugador1;
            this.base = RecursosImagen.cargar(rutaBase);
            this.colorJugador = jugador1 ? COLOR_JUGADOR1 : COLOR_JUGADOR2;
            this.etiquetaJugador = jugador1 ? "J1" : "J2";

            setOpaque(false);
            setBackground(new Color(18, 30, 40));
            setLayout(new BorderLayout());

            barraSuperior = new JPanel(new BorderLayout());
            barraSuperior.setOpaque(false);
            barraSuperior.setBorder(new EmptyBorder(18, 18, 0, 18));
            barraSuperior.setPreferredSize(new Dimension(0, 84));

            vista = new PanelVistaPersonaje(base);

            titulo = new JLabel("", SwingConstants.CENTER);
            titulo.setOpaque(false);
            titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
            titulo.setVisible(false);

            JPanel panelTitulo = new JPanel(new GridBagLayout());
            panelTitulo.setOpaque(false);
            panelTitulo.setPreferredSize(new Dimension(0, 140));
            panelTitulo.add(titulo);

            JPanel contenido = new JPanel(new BorderLayout());
            contenido.setOpaque(false);
            contenido.add(panelTitulo, BorderLayout.NORTH);
            contenido.add(vista, BorderLayout.CENTER);

            botonCambiar = BotonImagen.crear(imagenCambiar, 145);
            botonCambiar.setVisible(false);

            botonAleatorio = BotonImagen.crear(imagenAleatorio, 145);
            botonAleatorio.setToolTipText(
                    "Elige un personaje al azar para la IA");
            botonAleatorio.setVisible(false);

            panelPoder = new PanelPoder();
            panelPoder.setAlignmentX(Component.CENTER_ALIGNMENT);
            estadisticas = new PanelEstadisticas(colorJugador);
            estadisticas.setAlignmentX(Component.CENTER_ALIGNMENT);
            botonCambiar.setAlignmentX(Component.CENTER_ALIGNMENT);
            botonAleatorio.setAlignmentX(Component.CENTER_ALIGNMENT);

            JPanel info = new JPanel();
            info.setOpaque(false);
            info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
            info.setBorder(new EmptyBorder(0, 14, 14, 14));
            info.add(panelPoder);
            info.add(Box.createVerticalStrut(8));
            info.add(estadisticas);
            info.add(Box.createVerticalStrut(8));

            JPanel acciones = new JPanel(new FlowLayout(
                    FlowLayout.CENTER, 4, 0));
            acciones.setOpaque(false);
            acciones.add(botonAleatorio);
            acciones.add(botonCambiar);
            info.add(acciones);

            add(barraSuperior, BorderLayout.NORTH);
            add(contenido, BorderLayout.CENTER);
            add(info, BorderLayout.SOUTH);
        }

        /**
         * Muestra las estadísticas del perfil indicado.
         *
         * @param perfil perfil del jugador
         */
        private void setPerfil(PerfilJugador perfil) {
            estadisticas.setPerfil(perfil);
        }

        /**
         * Muestra un nombre y unas estadísticas concretas en el panel.
         *
         * @param nombre nombre que se mostrará
         * @param estadisticasJugador estadísticas que se mostrarán
         */
        private void setEstadisticas(String nombre, Estadisticas estadisticasJugador) {
            estadisticas.setEstadisticas(nombre, estadisticasJugador);
        }

        /**
         * Ajusta el tamaño con el que se dibuja el personaje.
         *
         * @param factor factor de escala del personaje
         */
        private void setFactorPersonaje(double factor) {
            vista.setFactorPersonaje(factor);
        }

        /**
         * Cambia la etiqueta del jugador.
         *
         * @param etiqueta texto de la etiqueta
         */
        private void setEtiqueta(String etiqueta) {
            etiquetaJugador = etiqueta;
            repaint();
        }

        /**
         * Cambia el color asociado al jugador.
         *
         * @param color nuevo color del jugador
         */
        private void setColor(Color color) {
            if (color == null) {
                return;
            }
            colorJugador = color;
            estadisticas.setColorBarra(color);
            repaint();
        }

        /**
         * Indica si el botón para cambiar de personaje está disponible.
         *
         * @param permiteCambiar true para mostrar el botón de cambio
         */
        private void setPermiteCambiar(boolean permiteCambiar) {
            this.permiteCambiar = permiteCambiar;
            botonCambiar.setVisible(permiteCambiar);
        }

        /**
         * Muestra u oculta el botón de personaje aleatorio.
         *
         * @param visible true para mostrar el botón
         */
        private void setAleatorioVisible(boolean visible) {
            botonAleatorio.setVisible(visible);
            revalidate();
            repaint();
        }

        /**
         * Ajusta el tamaño de los botones y reacomoda los componentes.
         */
        @Override
        public void doLayout() {
            int anchoFila = Math.max(240, getWidth() - 28);
            int ancho = Math.max(120, Math.min(210, (anchoFila - 12) / 2));
            if (ancho != anchoBotones) {
                anchoBotones = ancho;
                escalar(botonCambiar, imagenCambiar, ancho);
                escalar(botonAleatorio, imagenAleatorio, ancho);
            }
            super.doLayout();
        }

        /**
         * Cambia los iconos de un botón para ajustarlos a un ancho dado.
         *
         * @param boton botón que se va a escalar
         * @param imagen imagen base del botón
         * @param ancho ancho deseado para el icono
         */
        private static void escalar(JButton boton, BufferedImage imagen,
                                    int ancho) {
            ImageIcon normal = BotonImagen.icono(imagen, ancho);
            ImageIcon encima = BotonImagen.icono(imagen,
                    ancho + Math.max(3, ancho * 5 / 100));
            ImageIcon presionado = BotonImagen.icono(imagen,
                    ancho + Math.max(2, ancho * 2 / 100));
            boton.setIcon(normal);
            boton.setRolloverIcon(encima);
            boton.setPressedIcon(presionado);
            boton.setPreferredSize(new Dimension(
                    normal.getIconWidth(), normal.getIconHeight()));
        }

        /**
         * Muestra la opción indicada o limpia el panel si es null.
         *
         * @param opcion opción del catálogo que se va a mostrar
         */
        private void mostrar(CatalogoSeleccion.Opcion opcion) {
            if (opcion == null) {
                bandera = null;
                vista.setSprite(null);
                titulo.setIcon(null);
                titulo.setVisible(false);
                panelPoder.setPoder(null);
                botonCambiar.setVisible(false);
                repaint();
                return;
            }

            panelPoder.setPoder(opcion.getPersonaje().getPoder());
            bandera = RecursosImagen.cargar(opcion.getBandera());
            String rutaSprite;
            if (jugador1) {
                rutaSprite = opcion.getIzquierda();
            } else {
                rutaSprite = opcion.getDerecha();
            }

            vista.setSprite(RecursosImagen.cargar(rutaSprite));
            ImageIcon iconoTitulo = crearIconoAjustado(
                    RecursosImagen.cargar(opcion.getTitulo()), 210, 118);
            titulo.setIcon(iconoTitulo);
            titulo.setVisible(true);
            botonCambiar.setVisible(permiteCambiar);
            revalidate();
            repaint();
        }

        /**
         * Devuelve la barra superior del panel.
         *
         * @return barra superior
         */
        private JPanel getBarraSuperior() {
            return barraSuperior;
        }

        /**
         * Devuelve el botón para cambiar de personaje.
         *
         * @return botón de cambio
         */
        private JButton getBotonCambiar() {
            return botonCambiar;
        }

        /**
         * Devuelve el botón de personaje aleatorio.
         *
         * @return botón de aleatorio
         */
        private JButton getBotonAleatorio() {
            return botonAleatorio;
        }

        /**
         * Dibuja el fondo y la bandera del jugador.
         *
         * @param g contexto gráfico usado por Swing
         */
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.setComposite(AlphaComposite.SrcOver);

            // La bandera y el fondo se recortan con la forma redondeada
            // del marco para que no sobresalgan por las esquinas.
            int margen = 7;
            RoundRectangle2D recorte = new RoundRectangle2D.Float(
                    margen, margen, getWidth() - 1 - margen * 2,
                    getHeight() - 1 - margen * 2, 26, 26);
            g2.setColor(new Color(18, 30, 40));
            g2.fill(recorte);

            if (bandera != null) {
                double escala = Math.max(
                        (double) getWidth() / bandera.getWidth(),
                        (double) getHeight() / bandera.getHeight());
                int ancho = (int) Math.ceil(bandera.getWidth() * escala);
                int alto = (int) Math.ceil(bandera.getHeight() * escala);

                g2.clip(recorte);
                g2.drawImage(bandera, (getWidth() - ancho) / 2,
                        (getHeight() - alto) / 2, ancho, alto, null);
                g2.setClip(null);
            }

            dibujarMarco(g2);
            g2.dispose();
        }

        /**
         * Marco del color del jugador (azul para J1, rojo para J2).
         *
         * @param g2 contexto gráfico
         */
        private void dibujarMarco(Graphics2D g2) {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            int ancho = getWidth();
            int alto = getHeight();
            int margen = 4;
            int arco = 30;

            g2.setStroke(new BasicStroke(5f));
            g2.setColor(colorJugador);
            g2.drawRoundRect(margen, margen, ancho - 1 - margen * 2,
                    alto - 1 - margen * 2, arco, arco);

            g2.setStroke(new BasicStroke(1.5f));
            g2.setColor(new Color(255, 255, 255, 170));
            g2.drawRoundRect(margen + 5, margen + 5,
                    ancho - 11 - margen * 2, alto - 11 - margen * 2,
                    arco, arco);
        }

        /**
         * Dibuja la etiqueta J1 o J2 sobre el panel.
         *
         * @param g contexto gráfico usado para dibujar
         */
        @Override
        protected void paintChildren(Graphics g) {
            super.paintChildren(g);

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            int anchoEtiqueta = 82;
            int altoEtiqueta = 46;
            int x = jugador1 ? getWidth() - anchoEtiqueta - 20 : 20;
            int y = 18;

            g2.setColor(new Color(0, 0, 0, 120));
            g2.fillRoundRect(x + 3, y + 4, anchoEtiqueta, altoEtiqueta, 18, 18);

            g2.setPaint(new GradientPaint(0, y, colorJugador.brighter(),
                    0, y + altoEtiqueta, colorJugador.darker()));
            g2.fillRoundRect(x, y, anchoEtiqueta, altoEtiqueta, 18, 18);

            g2.setStroke(new BasicStroke(2.5f));
            g2.setColor(new Color(255, 255, 255, 220));
            g2.drawRoundRect(x, y, anchoEtiqueta, altoEtiqueta, 18, 18);

            g2.setFont(new Font("SansSerif", Font.BOLD, 26));
            FontMetrics fm = g2.getFontMetrics();
            int xTexto = x + (anchoEtiqueta - fm.stringWidth(etiquetaJugador)) / 2;
            int yTexto = y + (altoEtiqueta - fm.getHeight()) / 2 + fm.getAscent();

            g2.setColor(new Color(0, 0, 0, 150));
            g2.drawString(etiquetaJugador, xTexto + 1, yTexto + 1);
            g2.setColor(Color.WHITE);
            g2.drawString(etiquetaJugador, xTexto, yTexto);

            g2.dispose();
        }
    }

    // ------------------------------------------------------------------
    // Poder del personaje seleccionado.
    // ------------------------------------------------------------------
    private static final class PanelPoder extends JComponent {

        private static final Font FUENTE_TITULO =
                new Font("SansSerif", Font.BOLD, 11);
        private static final Font FUENTE_NOMBRE =
                new Font("SansSerif", Font.BOLD, 15);

        private Poder poder;
        private BufferedImage emoji;

        /**
         * Crea el panel que muestra el poder del personaje.
         */
        private PanelPoder() {
            setPreferredSize(new Dimension(280, 50));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        }

        /**
         * Asigna el poder que se mostrará en el panel.
         *
         * @param poder poder del personaje
         */
        private void setPoder(Poder poder) {
            this.poder = poder;
            emoji = null;

            if (poder != null) {
                String ruta = CatalogoPoderes.rutaEmoji(poder);
                if (ruta != null) {
                    try {
                        emoji = RecursosImagen.cargar(ruta);
                    } catch (RuntimeException e) {
                        emoji = null;
                    }
                }
            }
            repaint();
        }

        /**
         * Dibuja el nombre y el emoji del poder.
         *
         * @param g contexto gráfico usado por Swing
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
            RoundRectangle2D fondo = new RoundRectangle2D.Float(
                    1, 1, ancho - 3, alto - 3, 18, 18);

            g2.setColor(new Color(6, 20, 34, 195));
            g2.fill(fondo);
            g2.setStroke(new BasicStroke(1.5f));
            g2.setColor(new Color(255, 255, 255, 90));
            g2.draw(fondo);

            Color colorPoder = CatalogoPoderes.colorPoder(poder);
            g2.setColor(colorPoder);
            g2.fillRoundRect(1, 1, 5, alto - 3, 5, 5);

            int tamanoEmoji = alto - 16;
            int xEmoji = 14;
            int yEmoji = 8;

            if (emoji != null) {
                double escala = Math.min(
                        (double) tamanoEmoji / emoji.getWidth(),
                        (double) tamanoEmoji / emoji.getHeight());
                int anchoEmoji = (int) Math.round(emoji.getWidth() * escala);
                int altoEmoji = (int) Math.round(emoji.getHeight() * escala);
                g2.drawImage(emoji,
                        xEmoji + (tamanoEmoji - anchoEmoji) / 2,
                        yEmoji + (tamanoEmoji - altoEmoji) / 2,
                        anchoEmoji, altoEmoji, null);
            }

            int xTexto = xEmoji + tamanoEmoji + 12;
            g2.setFont(FUENTE_TITULO);
            g2.setColor(new Color(190, 215, 235));
            g2.drawString("PODER", xTexto, 19);

            String nombre = poder == null ? "Sin poder" : poder.getNombre();
            g2.setFont(FUENTE_NOMBRE);
            FontMetrics fm = g2.getFontMetrics();
            int anchoMaximo = ancho - xTexto - 16;
            nombre = recortarTexto(nombre, fm, anchoMaximo);

            g2.setColor(new Color(0, 0, 0, 160));
            g2.drawString(nombre, xTexto + 1, 40);
            g2.setColor(poder == null ? Color.WHITE : colorPoder);
            g2.drawString(nombre, xTexto, 39);

            g2.dispose();
        }

        /**
         * Recorta un texto con puntos suspensivos si supera el ancho máximo.
         *
         * @param texto texto original
         * @param fm métricas de la fuente usada
         * @param anchoMaximo ancho disponible en píxeles
         * @return texto recortado
         */
        private static String recortarTexto(String texto, FontMetrics fm,
                                            int anchoMaximo) {
            if (texto == null) {
                return "";
            }
            if (fm.stringWidth(texto) <= anchoMaximo) {
                return texto;
            }

            String puntos = "...";
            String recortado = texto;
            while (recortado.length() > 1
                    && fm.stringWidth(recortado + puntos) > anchoMaximo) {
                recortado = recortado.substring(0, recortado.length() - 1);
            }
            return recortado + puntos;
        }
    }

    // ------------------------------------------------------------------
    // Estadísticas del perfil del jugador.
    // ------------------------------------------------------------------
    private static final class PanelEstadisticas extends JComponent {

        private static final Font FUENTE_TITULO =
                new Font("SansSerif", Font.BOLD, 15);
        private static final Font FUENTE_TEXTO =
                new Font("SansSerif", Font.BOLD, 13);
        private static final Font FUENTE_VALOR =
                new Font("SansSerif", Font.BOLD, 13);

        private Color colorBarra;
        private String nombre;
        private Estadisticas estadisticas;
        private int puntos = -1;

        /**
         * Crea el panel de estadísticas con el color de barra indicado.
         *
         * @param colorBarra color de las barras de estadísticas
         */
        private PanelEstadisticas(Color colorBarra) {
            this.colorBarra = colorBarra;
            setPreferredSize(new Dimension(280, 162));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 162));
        }

        /**
         * Carga el nombre, las estadísticas y los puntos de un perfil.
         *
         * @param perfil perfil que se mostrará
         */
        private void setPerfil(PerfilJugador perfil) {
            if (perfil == null) {
                this.nombre = null;
                this.estadisticas = null;
                this.puntos = -1;
            } else {
                this.nombre = perfil.getNombre();
                this.estadisticas = perfil.getEstadisticas();
                this.puntos = perfil.getPuntos().getTotal();
            }
            repaint();
        }

        /**
         * Muestra un nombre y unas estadísticas concretas.
         *
         * @param nombre nombre que se mostrará
         * @param estadisticas estadísticas que se mostrarán
         */
        private void setEstadisticas(String nombre, Estadisticas estadisticas) {
            this.nombre = nombre;
            this.estadisticas = estadisticas;
            this.puntos = -1;
            repaint();
        }

        /**
         * Cambia el color de las barras de estadísticas.
         *
         * @param color nuevo color de las barras
         */
        private void setColorBarra(Color color) {
            if (color != null) {
                this.colorBarra = color;
                repaint();
            }
        }

        /**
         * Dibuja el nombre del perfil y sus barras de estadísticas.
         *
         * @param g contexto gráfico usado por Swing
         */
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            int ancho = getWidth();
            int alto = getHeight();
            RoundRectangle2D fondo = new RoundRectangle2D.Float(
                    1, 1, ancho - 3, alto - 3, 22, 22);

            g2.setColor(new Color(6, 20, 34, 195));
            g2.fill(fondo);
            g2.setStroke(new BasicStroke(1.5f));
            g2.setColor(new Color(255, 255, 255, 90));
            g2.draw(fondo);

            g2.setFont(FUENTE_TITULO);
            FontMetrics fm = g2.getFontMetrics();
            String nombreMostrado = nombre == null ? "Sin perfil" : nombre;
            String textoPuntos = puntos < 0 ? "" : "Puntos: " + puntos;
            int anchoPuntos = textoPuntos.isEmpty()
                    ? 0 : fm.stringWidth(textoPuntos);
            nombreMostrado = recortarTexto(nombreMostrado, fm,
                    ancho - 30 - anchoPuntos - 12);

            g2.setColor(new Color(205, 228, 246));
            g2.drawString(nombreMostrado, 14, 25);

            if (!textoPuntos.isEmpty()) {
                g2.setColor(new Color(255, 217, 110));
                g2.drawString(textoPuntos, ancho - 14 - anchoPuntos, 25);
            }

            if (estadisticas == null) {
                g2.dispose();
                return;
            }

            dibujarFila(g2, "Velocidad", estadisticas.getSpeed(), 46, ancho);
            dibujarFila(g2, "Salto", estadisticas.getJump(), 76, ancho);
            dibujarFila(g2, "Patada", estadisticas.getKick(), 106, ancho);
            dibujarFila(g2, "Supervivencia",
                    estadisticas.getSurvival(), 136, ancho);

            g2.dispose();
        }

        /**
         * Dibuja una fila con la etiqueta, la barra y el valor de una estadística.
         *
         * @param g2 contexto gráfico usado para dibujar
         * @param etiqueta nombre de la estadística
         * @param valor valor de la estadística
         * @param y posición vertical de la fila
         * @param ancho ancho disponible del panel
         */
        private void dibujarFila(Graphics2D g2, String etiqueta, int valor,
                                 int y, int ancho) {
            g2.setFont(FUENTE_TEXTO);
            g2.setColor(new Color(205, 228, 246));
            g2.drawString(etiqueta, 14, y + 12);

            int xBarra = 112;
            int anchoValor = 26;
            int anchoBarra = ancho - xBarra - anchoValor - 16;
            int segmentos = ParametrosJuego.MAX_ESTADISTICA;
            int separacion = 3;
            int anchoSegmento = Math.max(3,
                    (anchoBarra - separacion * (segmentos - 1)) / segmentos);
            int altoSegmento = 12;

            for (int i = 0; i < segmentos; i++) {
                int x = xBarra + i * (anchoSegmento + separacion);
                if (i < valor) {
                    g2.setColor(colorBarra);
                } else {
                    g2.setColor(new Color(255, 255, 255, 35));
                }
                g2.fillRoundRect(x, y, anchoSegmento, altoSegmento, 4, 4);
            }

            g2.setFont(FUENTE_VALOR);
            String textoValor = String.valueOf(valor);
            g2.setColor(new Color(255, 217, 110));
            g2.drawString(textoValor,
                    ancho - 14 - g2.getFontMetrics().stringWidth(textoValor),
                    y + 12);
        }

        /**
         * Recorta un texto con puntos suspensivos si supera el ancho máximo.
         *
         * @param texto texto original
         * @param fm métricas de la fuente usada
         * @param anchoMaximo ancho disponible en píxeles
         * @return texto recortado
         */
        private static String recortarTexto(String texto, FontMetrics fm,
                                            int anchoMaximo) {
            if (texto == null) {
                return "";
            }
            if (fm.stringWidth(texto) <= anchoMaximo) {
                return texto;
            }

            String puntos = "...";
            String recortado = texto;
            while (recortado.length() > 1
                    && fm.stringWidth(recortado + puntos) > anchoMaximo) {
                recortado = recortado.substring(0, recortado.length() - 1);
            }
            return recortado + puntos;
        }
    }

    // ------------------------------------------------------------------
    // Dibuja el personaje sobre su base.
    // ------------------------------------------------------------------
    private static final class PanelVistaPersonaje extends JPanel {

        private final BufferedImage base;
        private BufferedImage sprite;
        private BufferedImage lienzo;
        private double factor = 1.0;

        /**
         * Crea el panel que dibuja al personaje sobre su base.
         *
         * @param base imagen base del personaje
         */
        private PanelVistaPersonaje(BufferedImage base) {
            this.base = RecursosImagen.recortarTransparente(base);
            setOpaque(false);
        }

        /**
         * Ajusta el tamaño con el que se dibuja el personaje.
         *
         * @param factor factor de escala del personaje
         */
        private void setFactorPersonaje(double factor) {
            this.factor = Math.max(1.0, factor);
            lienzo = null;
            repaint();
        }

        /**
         * Muestra u oculta el panel y limpia el lienzo al mostrarlo.
         *
         * @param visible true para mostrar el panel
         */
        @Override
        public void setVisible(boolean visible) {
            super.setVisible(visible);
            if (visible) {
                lienzo = null;
                repaint();
            }
        }

        /**
         * Asigna la imagen del personaje que se dibujará.
         *
         * @param sprite imagen del personaje
         */
        private void setSprite(BufferedImage sprite) {
            if (sprite == null) {
                this.sprite = null;
            } else {
                this.sprite = RecursosImagen.recortarTransparente(sprite);
            }
            lienzo = null;
            repaint();
        }

        /**
         * Dibuja el lienzo con la base y el personaje.
         *
         * @param g contexto gráfico usado por Swing
         */
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            int ancho = getWidth();
            int alto = getHeight();
            if (ancho <= 0 || alto <= 0) {
                return;
            }

            if (lienzo == null || lienzo.getWidth() != ancho
                    || lienzo.getHeight() != alto) {
                lienzo = crearLienzo(ancho, alto);
            }

            g.drawImage(lienzo, 0, 0, null);
        }

        /**
         * Compone la base y el personaje en una imagen propia. Así el
         * personaje no hereda colores, transparencias ni recortes del
         * contenedor y siempre se ve con sus colores originales.
         *
         * @param ancho ancho del lienzo
         * @param alto alto del lienzo
         * @return el lienzo creado
         */
        private BufferedImage crearLienzo(int ancho, int alto) {
            BufferedImage imagen = new BufferedImage(
                    ancho, alto, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = imagen.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.setComposite(AlphaComposite.Src);
            g2.setColor(new Color(0, 0, 0, 0));
            g2.fillRect(0, 0, ancho, alto);
            g2.setComposite(AlphaComposite.SrcOver);

            int anchoBase = (int) (ancho * 0.80 / Math.sqrt(factor));
            int altoBase = anchoBase * base.getHeight() / base.getWidth();
            int xBase = (ancho - anchoBase) / 2;
            int yBase = alto - altoBase - 2;

            // Primero la base y después el personaje, para que el personaje
            // quede delante del pedestal y no oculto por él.
            g2.drawImage(base, xBase, yBase, anchoBase, altoBase, null);

            if (sprite == null) {
                g2.setFont(new Font("SansSerif", Font.BOLD,
                        Math.max(40, alto / 3)));
                FontMetrics fm = g2.getFontMetrics();
                String signo = "?";
                int x = (ancho - fm.stringWidth(signo)) / 2;
                int y = yBase - fm.getDescent() - 20;
                g2.setColor(new Color(0, 0, 0, 120));
                g2.drawString(signo, x + 2, y + 2);
                g2.setColor(new Color(255, 255, 255, 200));
                g2.drawString(signo, x, y);
            } else {
                int maxAncho = (int) Math.min(ancho * 0.98,
                        ancho * 0.88 * factor);
                // Se deja una separación con el título para que no tape ni
                // recorte la cabeza del personaje.
                int separacionTitulo = Math.max(10,
                        (int) (altoBase * 0.06));
                int bordeSuperiorSprite = yBase + (int) (altoBase
                        * (0.10 + (factor - 1.0) * 0.25));
                int maxAlto = Math.max(10,
                        bordeSuperiorSprite - separacionTitulo);

                double escala = Math.min(
                        (double) maxAncho / sprite.getWidth(),
                        (double) maxAlto / sprite.getHeight());
                int anchoSprite = (int) (sprite.getWidth() * escala);
                int altoSprite = (int) (sprite.getHeight() * escala);
                int xSprite = (ancho - anchoSprite) / 2;
                int ySprite = Math.max(2, bordeSuperiorSprite - altoSprite);

                g2.drawImage(sprite, xSprite, ySprite,
                        anchoSprite, altoSprite, null);
            }

            g2.dispose();
            return imagen;
        }
    }

    // ------------------------------------------------------------------
    // Panel central verde con el título, la grilla y READY.
    // ------------------------------------------------------------------
    private static final class PanelCentro extends JPanel {

        private static final int ANCHO_READY_PVP = 300;
        private static final int ANCHO_READY_SOLO = 265;

        private final BufferedImage readyPvp =
                RecursosImagen.cargar(RUTA_BOTONES + "ReadyPerfil.png");
        private final BufferedImage readySolo =
                RecursosImagen.cargar(RUTA_BOTONES + "ReadySolo.png");
        private final PanelTurno turno;
        private final JButton botonReady;
        private final PanelTitulo titulo;
        private final PanelCuadricula contenedorCuadricula;
        private final List<BotonCaja> botones = new ArrayList<>();

        /**
         * Crea el panel central con el título, la grilla y el botón READY.
         */
        private PanelCentro() {
            setOpaque(false);
            setLayout(new GridBagLayout());

            titulo = new PanelTitulo("SELECCIONA TU PERSONAJE");

            turno = new PanelTurno("ELIGE JUGADOR 1");

            JPanel cuadricula = new JPanel(new GridLayout(3, 3, 8, 8));
            cuadricula.setOpaque(false);
            cuadricula.setBorder(new EmptyBorder(10, 10, 10, 10));

            List<CatalogoSeleccion.Opcion> opciones = CatalogoSeleccion.getOpciones();
            for (CatalogoSeleccion.Opcion opcion : opciones) {
                BotonCaja boton = new BotonCaja(opcion);
                botones.add(boton);
                cuadricula.add(boton);
            }
            // La última fila conserva las celdas bloqueadas con "?".
            for (int i = opciones.size(); i < 9; i++) {
                BotonCaja boton = new BotonCaja(null);
                botones.add(boton);
                cuadricula.add(boton);
            }

            contenedorCuadricula = new PanelCuadricula();
            contenedorCuadricula.setLayout(new BorderLayout());
            contenedorCuadricula.setPreferredSize(new Dimension(620, 620));
            contenedorCuadricula.setBorder(new EmptyBorder(10, 10, 10, 10));
            contenedorCuadricula.add(cuadricula, BorderLayout.CENTER);

            botonReady = crearBotonReady();
            botonReady.setVisible(false);

            GridBagConstraints c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 0;
            c.insets = new Insets(16, 18, 2, 18);
            add(titulo, c);

            c.gridy = 1;
            c.insets = new Insets(2, 18, 6, 18);
            add(turno, c);

            c.gridy = 2;
            c.insets = new Insets(0, 18, 8, 18);
            add(contenedorCuadricula, c);

            c.gridy = 3;
            c.insets = new Insets(0, 18, 8, 18);
            add(botonReady, c);
        }

        /**
         * Crea el botón READY con la imagen del modo dos jugadores.
         *
         * @return botón READY
         */
        private JButton crearBotonReady() {
            return BotonImagen.crear(readyPvp, ANCHO_READY_PVP, 5, 2);
        }

        /**
         * Cambia la imagen del botón READY según el modo de juego.
         *
         * @param solo true para usar la imagen del modo de un solo jugador
         */
        private void setBotonReadySolo(boolean solo) {
            BotonImagen.cambiarImagen(botonReady,
                    solo ? readySolo : readyPvp,
                    solo ? ANCHO_READY_SOLO : ANCHO_READY_PVP, 5, 2);
            revalidate();
            repaint();
        }

        /**
         * Actualiza el mensaje de turno y muestra u oculta el botón READY.
         *
         * @param turnoActual jugador al que le toca elegir
         * @param ambosListos true si los dos personajes ya están elegidos
         * @param modoIA true si el jugador 2 es la máquina
         */
        private void actualizarTurno(int turnoActual, boolean ambosListos,
                                     boolean modoIA) {
            turno.actualizar(turnoActual, ambosListos, modoIA);
            botonReady.setVisible(ambosListos);
            revalidate();
            repaint();
        }

        /**
         * Ajusta el tamaño de la cuadrícula al espacio disponible.
         */
        @Override
        public void doLayout() {
            int extra = titulo.getPreferredSize().height
                    + turno.getPreferredSize().height
                    + botonReady.getPreferredSize().height + 50;
            int disponibleAlto = Math.max(240, getHeight() - extra);
            int disponibleAncho = Math.max(240, getWidth() - 60);
            int lado = Math.max(280, Math.min(620,
                    Math.min(disponibleAlto, disponibleAncho)));
            contenedorCuadricula.setPreferredSize(new Dimension(lado, lado));
            super.doLayout();
        }

        /**
         * Devuelve el botón READY del panel.
         *
         * @return botón READY
         */
        private JButton getBotonReady() {
            return botonReady;
        }

        /**
         * Devuelve las celdas de la grilla del panel.
         *
         * @return lista de celdas
         */
        private List<BotonCaja> getBotones() {
            return botones;
        }
    }

    // ------------------------------------------------------------------
    // Celda de la grilla 3x3.
    // ------------------------------------------------------------------
    public static final class BotonCaja extends JButton {

        private static final Color COLOR_J1 = new Color(48, 132, 220);
        private static final Color COLOR_J2 = new Color(214, 62, 72);
        private static final BufferedImage CADENA = cargarCadena();

        private final CatalogoSeleccion.Opcion opcion;
        private BufferedImage imagen;
        private int jugadorSeleccionado;
        private boolean bloqueado;

        /**
         * Crea una celda de la grilla para la opción indicada.
         *
         * @param opcion opción del catálogo, o null para una celda bloqueada
         */
        private BotonCaja(CatalogoSeleccion.Opcion opcion) {
            this.opcion = opcion;
            setOpaque(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setRolloverEnabled(opcion != null);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            if (opcion != null) {
                imagen = RecursosImagen.cargar(opcion.getCaja());
            } else {
                setEnabled(false);
                setCursor(Cursor.getDefaultCursor());
            }
        }

        /**
         * Devuelve la opción asociada a la celda.
         *
         * @return opción de la celda, o null si está bloqueada
         */
        public CatalogoSeleccion.Opcion getOpcion() {
            return opcion;
        }

        /**
         * Marca cuál jugador tiene elegido este personaje.
         *
         * @param jugador 1, 2 o 0 si nadie lo eligió
         */
        private void setJugadorSeleccionado(int jugador) {
            this.jugadorSeleccionado = jugador == 1 || jugador == 2 ? jugador : 0;
            repaint();
        }

        /**
         * Indica si la celda se dibuja con la cadena de bloqueo.
         *
         * @param bloqueado true para mostrar la cadena
         */
        private void setBloqueado(boolean bloqueado) {
            this.bloqueado = bloqueado;
            repaint();
        }

        /**
         * Carga la imagen de la cadena de bloqueo.
         *
         * @return imagen de la cadena, o null si no se pudo cargar
         */
        private static BufferedImage cargarCadena() {
            try {
                return RecursosImagen.cargar(
                        "resources/images/Objetos/CadenaDeBloqueo.png");
            } catch (RuntimeException e) {
                return null;
            }
        }

        /**
         * Dibuja la imagen del personaje y las marcas de selección.
         *
         * @param g contexto gráfico usado por Swing
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
            boolean encima = getModel().isRollover() && isEnabled();
            int margen = encima ? 1 : 4;
            int arco = 18;
            RoundRectangle2D forma = new RoundRectangle2D.Float(
                    margen, margen, ancho - 1 - margen * 2,
                    alto - 1 - margen * 2, arco, arco);

            if (opcion == null) {
                g2.setColor(new Color(8, 26, 48, 235));
                g2.fill(forma);

                g2.setStroke(new BasicStroke(2.5f));
                g2.setColor(new Color(202, 170, 92));
                g2.draw(forma);

                g2.setStroke(new BasicStroke(1f));
                g2.setColor(new Color(255, 225, 145, 80));
                g2.draw(new RoundRectangle2D.Float(
                        margen + 3, margen + 3, ancho - 7 - margen * 2,
                        alto - 7 - margen * 2, arco, arco));

                g2.setFont(new Font("SansSerif", Font.BOLD, Math.max(28, alto / 3)));
                FontMetrics fm = g2.getFontMetrics();
                String signo = "?";
                int x = (ancho - fm.stringWidth(signo)) / 2;
                int y = (alto + fm.getAscent() - fm.getDescent()) / 2;

                g2.setColor(new Color(0, 0, 0, 130));
                g2.drawString(signo, x + 2, y + 2);
                g2.setColor(new Color(242, 205, 98));
                g2.drawString(signo, x, y);
                g2.dispose();
                return;
            }

            double escala = Math.max(
                    (double) ancho / imagen.getWidth(),
                    (double) alto / imagen.getHeight());
            int anchoImagen = (int) Math.ceil(imagen.getWidth() * escala);
            int altoImagen = (int) Math.ceil(imagen.getHeight() * escala);
            int xImagen = (ancho - anchoImagen) / 2;
            int yImagen = (alto - altoImagen) / 2;

            g2.setClip(forma);
            g2.drawImage(imagen, xImagen, yImagen, anchoImagen, altoImagen, null);

            if (bloqueado && CADENA != null) {
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

            if (encima && !bloqueado) {
                g2.setColor(new Color(255, 255, 255, 42));
                g2.fill(forma);
            }
            g2.setClip(null);

            if (!isEnabled() && jugadorSeleccionado == 0) {
                g2.setColor(new Color(20, 35, 45, 130));
                g2.fill(forma);
            }

            if (jugadorSeleccionado != 0) {
                Color color = jugadorSeleccionado == 1 ? COLOR_J1 : COLOR_J2;

                // Solo se marca el borde; no se tiñe la imagen del personaje.
                g2.setStroke(new BasicStroke(4.5f));
                g2.setColor(color);
                g2.draw(forma);

                dibujarEtiquetaJugador(g2, color, ancho);
            } else {
                g2.setStroke(new BasicStroke(encima ? 3f : 2.2f));
                g2.setColor(encima
                        ? new Color(255, 222, 110)
                        : new Color(210, 176, 78));
                g2.draw(forma);
            }

            g2.dispose();
        }

        /**
         * Cuadrado con J1 (azul) o J2 (rojo) sobre el personaje elegido.
         *
         * @param g2 contexto gráfico
         * @param color color de la etiqueta
         * @param ancho ancho disponible para dibujarla
         */
        private void dibujarEtiquetaJugador(Graphics2D g2, Color color, int ancho) {
            int tamano = Math.max(30, Math.min(52, ancho / 5));
            int x = ancho - tamano - 10;
            int y = 10;
            int arco = Math.max(8, tamano / 4);

            g2.setColor(new Color(0, 0, 0, 130));
            g2.fillRoundRect(x + 2, y + 3, tamano, tamano, arco, arco);

            g2.setPaint(new GradientPaint(0, y, color.brighter(),
                    0, y + tamano, color.darker()));
            g2.fillRoundRect(x, y, tamano, tamano, arco, arco);

            g2.setStroke(new BasicStroke(2.5f));
            g2.setColor(new Color(255, 255, 255, 220));
            g2.drawRoundRect(x, y, tamano, tamano, arco, arco);

            String texto = jugadorSeleccionado == 1 ? "J1" : "J2";
            g2.setFont(new Font("SansSerif", Font.BOLD,
                    Math.max(14, tamano / 3)));
            FontMetrics fm = g2.getFontMetrics();
            int xTexto = x + (tamano - fm.stringWidth(texto)) / 2;
            int yTexto = y + (tamano - fm.getHeight()) / 2 + fm.getAscent();

            g2.setColor(new Color(0, 0, 0, 160));
            g2.drawString(texto, xTexto + 1, yTexto + 1);
            g2.setColor(Color.WHITE);
            g2.drawString(texto, xTexto, yTexto);
        }
    }

    // ------------------------------------------------------------------
    // Componentes auxiliares de dibujo.
    // ------------------------------------------------------------------
    private static final class PanelTitulo extends JComponent {

        private final String texto;
        private final Font fuente = new Font("SansSerif", Font.BOLD, 24);

        /**
         * Crea el título del panel central.
         *
         * @param texto texto que se mostrará
         */
        private PanelTitulo(String texto) {
            this.texto = texto;
        }

        /**
         * Calcula el tamaño preferido según el texto del título.
         *
         * @return tamaño preferido del componente
         */
        @Override
        public Dimension getPreferredSize() {
            FontMetrics fm = getFontMetrics(fuente);
            return new Dimension(fm.stringWidth(texto) + 70, 58);
        }

        /**
         * Dibuja el fondo redondeado y el texto del título.
         *
         * @param g contexto gráfico usado por Swing
         */
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

    /**
     * Píldora que indica de quién es el turno. Usa el mismo estilo del
     * título, pero cambia a azul para el jugador 1 y rojo para el jugador 2.
     */
    private static final class PanelTurno extends JComponent {

        private static final Font FUENTE = new Font("SansSerif", Font.BOLD, 19);

        private String texto;
        private Color colorSuperior = new Color(84, 176, 104, 240);
        private Color colorInferior = new Color(28, 100, 54, 240);

        /**
         * Crea la píldora del turno.
         *
         * @param texto texto inicial del turno
         */
        private PanelTurno(String texto) {
            this.texto = texto;
        }

        /**
         * Cambia el texto y los colores según el turno actual.
         *
         * @param turnoActual jugador al que le toca elegir
         * @param ambosListos true si los dos personajes ya están elegidos
         * @param modoIA true si el jugador 2 es la máquina
         */
        private void actualizar(int turnoActual, boolean ambosListos, boolean modoIA) {
            if (ambosListos) {
                texto = "¡LISTOS!";
                colorSuperior = new Color(232, 196, 92, 245);
                colorInferior = new Color(156, 112, 20, 245);
            } else if (turnoActual == 2) {
                texto = modoIA ? "ELIGE EL PERSONAJE DE LA IA" : "ELIGE JUGADOR 2";
                colorSuperior = new Color(210, 92, 92, 240);
                colorInferior = new Color(128, 28, 38, 240);
            } else {
                texto = "ELIGE JUGADOR 1";
                colorSuperior = new Color(84, 176, 104, 240);
                colorInferior = new Color(28, 100, 54, 240);
            }

            revalidate();
            repaint();
        }

        /**
         * Calcula el tamaño preferido según el texto del turno.
         *
         * @return tamaño preferido del componente
         */
        @Override
        public Dimension getPreferredSize() {
            FontMetrics fm = getFontMetrics(FUENTE);
            return new Dimension(fm.stringWidth(texto) + 64, 46);
        }

        /**
         * Dibuja la píldora del turno con su texto.
         *
         * @param g contexto gráfico usado por Swing
         */
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            int ancho = getWidth();
            int alto = getHeight();
            int arco = alto - 8;

            g2.setColor(new Color(0, 0, 0, 100));
            g2.fillRoundRect(4, 6, ancho - 8, alto - 10, arco, arco);

            g2.setPaint(new GradientPaint(0, 0, colorSuperior, 0, alto, colorInferior));
            g2.fillRoundRect(2, 2, ancho - 4, alto - 8, arco, arco);

            g2.setStroke(new BasicStroke(2f));
            g2.setColor(new Color(255, 255, 255, 180));
            g2.drawRoundRect(4, 4, ancho - 9, alto - 13, arco, arco);

            g2.setFont(FUENTE);
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

    private static final class PanelCuadricula extends JPanel {

        /**
         * Crea el contenedor de la cuadrícula de personajes.
         */
        private PanelCuadricula() {
            setOpaque(false);
        }

        /**
         * Dibuja el marco redondeado de la cuadrícula.
         *
         * @param g contexto gráfico usado por Swing
         */
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            int ancho = getWidth();
            int alto = getHeight();
            RoundRectangle2D forma = new RoundRectangle2D.Float(
                    1, 1, ancho - 3, alto - 3, 30, 30);

            g2.setColor(new Color(8, 28, 50, 205));
            g2.fill(forma);

            g2.setStroke(new BasicStroke(3f));
            g2.setColor(new Color(212, 175, 55));
            g2.draw(forma);

            g2.setStroke(new BasicStroke(1f));
            g2.setColor(new Color(255, 225, 140, 95));
            g2.draw(new RoundRectangle2D.Float(
                    5, 5, ancho - 11, alto - 11, 24, 24));

            g2.dispose();
        }
    }
}
