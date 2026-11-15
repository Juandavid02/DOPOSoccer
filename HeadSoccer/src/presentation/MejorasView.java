package presentation;

import domain.Estadisticas;
import domain.ParametrosJuego;
import domain.PerfilJugador;
import domain.Poder;
import domain.TipoEstadistica;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Pantalla para gastar los puntos de un perfil en mejoras de estadísticas.
 */
public class MejorasView extends VistaBase {

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

    private DefaultListModel<PerfilJugador> modeloPerfiles;
    private JList<PerfilJugador> listaPerfiles;
    private JLabel nombrePerfil;
    private JLabel puntosPerfil;
    private JLabel costoMejora;

    private JPanel panelContenido;
    private CardLayout layoutContenido;
    private JPanel barraSecciones;
    private final Map<String, BotonSeccion> botonesSeccion = new java.util.LinkedHashMap<>();
    private JPanel panelPersonajes;
    private JPanel panelBalones;
    private JPanel panelEscenarios;
    private String seccionActual = "MEJORAS";
    private Desbloqueos desbloqueos;
    private PerfilJugador perfilActual;
    private final List<TarjetaDesbloqueo> tarjetasTienda = new ArrayList<>();
    private Consumer<TarjetaDesbloqueo> accionCompra = tarjeta -> { };

    /**
     * Crea la vista de la tienda y arma sus secciones.
     */
    public MejorasView() {
        super("resources/images/Ventanas/FondoPerfil.png");
        setTitulo("TIENDA");
        prepareViewCentro();
    }

    /**
     * Asigna el servicio de desbloqueos y refresca la tienda.
     *
     * @param desbloqueos servicio de compras de la tienda
     */
    public void setDesbloqueos(Desbloqueos desbloqueos) {
        this.desbloqueos = desbloqueos;
        actualizarTienda();
    }

    /**
     * Define la acción que se ejecuta al comprar una tarjeta.
     *
     * @param accionCompra acción de compra o null para no hacer nada
     */
    public void setAccionCompra(Consumer<TarjetaDesbloqueo> accionCompra) {
        this.accionCompra = accionCompra == null ? tarjeta -> { } : accionCompra;
    }

    /**
     * Construye la tarjeta central con secciones, listas y contenido.
     */
    private void prepareViewCentro() {
        TarjetaPanel tarjeta = new TarjetaPanel();
        tarjeta.setLayout(new BorderLayout(24, 0));
        tarjeta.setBorder(new EmptyBorder(18, 26, 16, 26));
        tarjeta.setPreferredSize(new Dimension(1140, 620));
        tarjeta.setMinimumSize(new Dimension(980, 560));

        barraSecciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        barraSecciones.setOpaque(false);
        agregarBotonSeccion("MEJORAS");
        agregarBotonSeccion("PERSONAJES");
        agregarBotonSeccion("BALONES");
        agregarBotonSeccion("ESCENARIOS");

        layoutContenido = new CardLayout();
        panelContenido = new JPanel(layoutContenido);
        panelContenido.setOpaque(false);
        panelContenido.add(prepararDetalle(), "MEJORAS");
        panelPersonajes = prepararTiendaPersonajes();
        panelBalones = prepararTiendaBalones();
        panelEscenarios = prepararTiendaEscenarios();
        panelContenido.add(panelPersonajes, "PERSONAJES");
        panelContenido.add(panelBalones, "BALONES");
        panelContenido.add(panelEscenarios, "ESCENARIOS");

        JPanel derecha = new JPanel(new BorderLayout(0, 8));
        derecha.setOpaque(false);
        derecha.add(barraSecciones, BorderLayout.NORTH);
        derecha.add(panelContenido, BorderLayout.CENTER);

        tarjeta.add(prepararListaPerfiles(), BorderLayout.WEST);
        tarjeta.add(derecha, BorderLayout.CENTER);
        seleccionarSeccion("MEJORAS");

        JPanel contenedor = new JPanel(new GridBagLayout());
        contenedor.setOpaque(false);
        contenedor.setBorder(new EmptyBorder(8, 0, 20, 0));
        contenedor.add(tarjeta);

        setCentro(contenedor, 0, 0);
    }

    /**
     * Crea el panel lateral con la lista de perfiles.
     *
     * @return panel de la lista de perfiles
     */
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

    /**
     * Crea el panel de detalle con las estadísticas del perfil.
     *
     * @return panel de detalle de mejoras
     */
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

    /**
     * Agrega una fila con barra, valor y botón para una estadística.
     *
     * @param panel panel donde se agrega la fila
     * @param tipo estadística de la fila
     * @param fila número de fila en la cuadrícula
     */
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
     * Crea y agrega un botón para cambiar de sección.
     *
     * @param nombre nombre de la sección
     */
    private void agregarBotonSeccion(String nombre) {
        BotonSeccion boton = new BotonSeccion(nombre);
        boton.addActionListener(e -> seleccionarSeccion(nombre));
        botonesSeccion.put(nombre, boton);
        barraSecciones.add(boton);
    }

    /**
     * Muestra una sección y marca su botón como seleccionado.
     *
     * @param nombre nombre de la sección a mostrar
     */
    private void seleccionarSeccion(String nombre) {
        seccionActual = nombre;
        layoutContenido.show(panelContenido, nombre);
        for (Map.Entry<String, BotonSeccion> entrada : botonesSeccion.entrySet()) {
            entrada.getValue().setSeleccionado(
                    entrada.getKey().equals(nombre));
        }
    }

    /**
     * Crea la sección de compra de personajes.
     *
     * @return panel con la tienda de personajes
     */
    private JPanel prepararTiendaPersonajes() {
        PanelPrevisualizacion preview = new PanelPrevisualizacion();
        JPanel cuadricula = new JPanel(new GridLayout(3, 3, 10, 10));
        cuadricula.setOpaque(false);
        cuadricula.setBorder(new EmptyBorder(6, 6, 6, 6));

        TarjetaDesbloqueo primera = null;
        for (CatalogoSeleccion.Opcion opcion : CatalogoSeleccion.getOpciones()) {
            String nombre = opcion.getPersonaje().getNombre();
            TarjetaDesbloqueo tarjeta = crearTarjeta(TipoTienda.PERSONAJE,
                    nombre, opcion.getCaja(),
                    RecursosImagen.cargar(opcion.getDerecha()),
                    opcion.getPersonaje().getPoder(),
                    Desbloqueos.PRECIO_PERSONAJE, preview);
            if (primera == null) {
                primera = tarjeta;
            }
            cuadricula.add(tarjeta);
        }
        cuadricula.add(new TarjetaVaciaTienda());
        cuadricula.add(new TarjetaVaciaTienda());
        cuadricula.add(new TarjetaVaciaTienda());
        return crearPanelTienda(cuadricula, preview, primera);
    }

    /**
     * Crea la sección de compra de balones.
     *
     * @return panel con la tienda de balones
     */
    private JPanel prepararTiendaBalones() {
        JPanel cuadricula = new JPanel(new GridLayout(2, 2, 10, 10));
        cuadricula.setOpaque(false);
        cuadricula.setBorder(new EmptyBorder(6, 6, 6, 6));

        for (CatalogoVisual.Opcion opcion : CatalogoVisual.getBalones()) {
            cuadricula.add(crearTarjeta(TipoTienda.BALON,
                    opcion.getNombre(), opcion.getRuta(), null, null,
                    Desbloqueos.PRECIO_BALON, null));
        }
        cuadricula.add(new TarjetaVaciaTienda());
        return cuadricula;
    }

    /**
     * Crea la sección de compra de escenarios.
     *
     * @return panel con la tienda de escenarios
     */
    private JPanel prepararTiendaEscenarios() {
        JPanel cuadricula = new JPanel(new GridLayout(2, 3, 10, 10));
        cuadricula.setOpaque(false);
        cuadricula.setBorder(new EmptyBorder(6, 6, 6, 6));

        for (CatalogoVisual.Opcion opcion : CatalogoVisual.getEscenarios()) {
            cuadricula.add(crearTarjeta(TipoTienda.ESCENARIO,
                    opcion.getNombre(), opcion.getRuta(), null, null,
                    Desbloqueos.PRECIO_ESCENARIO, null));
        }
        cuadricula.add(new TarjetaVaciaTienda());
        cuadricula.add(new TarjetaVaciaTienda());
        return cuadricula;
    }

    /**
     * Une la cuadrícula de tarjetas con el panel de previsualización.
     *
     * @param cuadricula tarjetas de la sección
     * @param preview panel de previsualización
     * @param primera primera tarjeta que se selecciona
     * @return panel completo de la sección
     */
    private JPanel crearPanelTienda(JPanel cuadricula,
                                    PanelPrevisualizacion preview,
                                    TarjetaDesbloqueo primera) {
        preview.setAccionCompra(tarjeta -> accionCompra.accept(tarjeta));

        JPanel panel = new JPanel(new BorderLayout(14, 0));
        panel.setOpaque(false);
        panel.add(cuadricula, BorderLayout.CENTER);
        panel.add(preview, BorderLayout.EAST);

        if (primera != null) {
            seleccionarTarjeta(primera, preview);
        }
        return panel;
    }

    /**
     * Crea una tarjeta de la tienda según el tipo de objeto.
     *
     * @param tipo tipo de objeto de la tienda
     * @param nombre nombre del objeto
     * @param ruta ruta de la imagen del objeto
     * @param imagenPrevia imagen alternativa para la previsualización
     * @param poder poder del personaje o null
     * @param precio precio en puntos
     * @param preview panel de previsualización o null
     * @return tarjeta creada
     */
    private TarjetaDesbloqueo crearTarjeta(TipoTienda tipo, String nombre,
                                           String ruta,
                                           BufferedImage imagenPrevia,
                                           Poder poder, int precio,
                                           PanelPrevisualizacion preview) {
        boolean desbloqueado = estaDesbloqueado(tipo, nombre);
        BufferedImage imagen = RecursosImagen.cargar(ruta);

        TarjetaDesbloqueo tarjeta = new TarjetaDesbloqueo(tipo, nombre,
                imagen, imagenPrevia, poder, precio, desbloqueado);

        if (preview != null) {
            tarjeta.addMouseListener(new java.awt.event.MouseAdapter() {
                /**
                 * Selecciona la tarjeta al hacer clic sobre ella.
                 *
                 * @param e evento del clic del ratón
                 */
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    seleccionarTarjeta(tarjeta, preview);
                }
            });
        }

        if (tarjeta.getBotonComprar() != null) {
            tarjeta.getBotonComprar().addActionListener(
                    e -> accionCompra.accept(tarjeta));
        }

        tarjetasTienda.add(tarjeta);
        return tarjeta;
    }

    /**
     * Marca una tarjeta como seleccionada y actualiza la previsualización.
     *
     * @param tarjeta tarjeta seleccionada
     * @param preview panel de previsualización
     */
    private void seleccionarTarjeta(TarjetaDesbloqueo tarjeta,
                                    PanelPrevisualizacion preview) {
        for (TarjetaDesbloqueo actual : tarjetasTienda) {
            if (actual.getTipo() == tarjeta.getTipo()) {
                actual.setSeleccionada(actual == tarjeta);
            }
        }
        preview.setTarjeta(tarjeta);
    }

    /**
     * Indica si un objeto de la tienda ya está desbloqueado para el perfil.
     *
     * @param tipo tipo de objeto de la tienda
     * @param nombre nombre del objeto
     * @return true si el objeto está desbloqueado
     */
    private boolean estaDesbloqueado(TipoTienda tipo, String nombre) {
        if (desbloqueos == null || perfilActual == null) {
            return true;
        }
        switch (tipo) {
            case PERSONAJE:
                return desbloqueos.estaDesbloqueadoPersonaje(
                        nombre, perfilActual);
            case BALON:
                return desbloqueos.estaDesbloqueadoBalon(nombre, perfilActual);
            case ESCENARIO:
                return desbloqueos.estaDesbloqueadoEscenario(
                        nombre, perfilActual);
            default:
                return true;
        }
    }

    /**
     * Reconstruye las secciones de la tienda después de una compra.
     */
    public void actualizarTienda() {
        if (panelContenido == null) {
            return;
        }
        tarjetasTienda.clear();

        panelContenido.remove(panelPersonajes);
        panelContenido.remove(panelBalones);
        panelContenido.remove(panelEscenarios);

        panelPersonajes = prepararTiendaPersonajes();
        panelBalones = prepararTiendaBalones();
        panelEscenarios = prepararTiendaEscenarios();
        panelContenido.add(panelPersonajes, "PERSONAJES");
        panelContenido.add(panelBalones, "BALONES");
        panelContenido.add(panelEscenarios, "ESCENARIOS");

        layoutContenido.show(panelContenido, seccionActual);
        revalidate();
        repaint();
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
        perfilActual = perfil;
        if (perfil == null) {
            nombrePerfil.setText("Selecciona un perfil");
            puntosPerfil.setText("Puntos: 0");
            costoMejora.setText("Crea o selecciona un perfil para empezar.");
            for (TipoEstadistica tipo : TipoEstadistica.values()) {
                barras.get(tipo).setValor(0);
                valores.get(tipo).setText("0/" + ParametrosJuego.MAX_ESTADISTICA);
                botones.get(tipo).setEnabled(false);
            }
            actualizarTienda();
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

        actualizarTienda();
        revalidate();
        repaint();
    }

    /**
     * Devuelve la lista de perfiles de la vista.
     *
     * @return lista de perfiles
     */
    public JList<PerfilJugador> getListaPerfiles() {
        return listaPerfiles;
    }

    /**
     * Devuelve el botón de mejora de una estadística.
     *
     * @param tipo estadística del botón
     * @return botón de mejora de la estadística
     */
    public JButton getBotonMejorar(TipoEstadistica tipo) {
        return botones.get(tipo);
    }

    private static final class PerfilRenderer extends DefaultListCellRenderer {

        /**
         * Configura la etiqueta de una celda de la lista de perfiles.
         *
         * @param lista lista que dibuja la celda
         * @param valor perfil de la celda
         * @param indice índice de la celda
         * @param seleccionado si la celda está seleccionada
         * @param tieneFoco si la celda tiene el foco
         * @return componente de la celda
         */
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

    public enum TipoTienda {
        PERSONAJE,
        BALON,
        ESCENARIO
    }

    private static final class PanelPrevisualizacion extends JPanel {

        private final PanelImagenTienda imagen;
        private final JLabel nombre;
        private final JLabel poder;
        private final JLabel estado;
        private final JLabel precio;
        private final JButton boton;
        private TarjetaDesbloqueo tarjeta;
        private Consumer<TarjetaDesbloqueo> accionCompra = t -> { };

        /**
         * Crea el panel de previsualización de la tienda.
         */
        private PanelPrevisualizacion() {
            setOpaque(false);
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBorder(new EmptyBorder(10, 10, 10, 10));
            setPreferredSize(new Dimension(240, 0));

            imagen = new PanelImagenTienda(null, true);
            imagen.setPreferredSize(new Dimension(210, 240));
            imagen.setMaximumSize(new Dimension(Integer.MAX_VALUE, 240));
            imagen.setAlignmentX(Component.CENTER_ALIGNMENT);
            add(imagen);
            add(Box.createVerticalStrut(8));

            nombre = new JLabel("Selecciona un objeto", SwingConstants.CENTER);
            nombre.setFont(new Font("SansSerif", Font.BOLD, 17));
            nombre.setForeground(TEXTO_CLARO);
            nombre.setAlignmentX(Component.CENTER_ALIGNMENT);
            add(nombre);

            poder = new JLabel(" ", SwingConstants.CENTER);
            poder.setFont(new Font("SansSerif", Font.BOLD, 13));
            poder.setForeground(AZUL);
            poder.setAlignmentX(Component.CENTER_ALIGNMENT);
            add(poder);

            estado = new JLabel(" ", SwingConstants.CENTER);
            estado.setFont(new Font("SansSerif", Font.BOLD, 14));
            estado.setAlignmentX(Component.CENTER_ALIGNMENT);
            add(estado);

            precio = new JLabel(" ", SwingConstants.CENTER);
            precio.setFont(new Font("SansSerif", Font.BOLD, 14));
            precio.setForeground(NARANJA_PUNTOS);
            precio.setAlignmentX(Component.CENTER_ALIGNMENT);
            add(precio);

            add(Box.createVerticalGlue());

            boton = new BotonMejora("COMPRAR");
            boton.setAlignmentX(Component.CENTER_ALIGNMENT);
            boton.addActionListener(e -> {
                if (tarjeta != null) {
                    accionCompra.accept(tarjeta);
                }
            });
            add(boton);

            setTarjeta(null);
        }

        /**
         * Define la acción de compra del panel de previsualización.
         *
         * @param accionCompra acción de compra o null para no hacer nada
         */
        private void setAccionCompra(Consumer<TarjetaDesbloqueo> accionCompra) {
            this.accionCompra = accionCompra == null ? t -> { } : accionCompra;
        }

        /**
         * Muestra en el panel los datos de una tarjeta.
         *
         * @param tarjeta tarjeta a mostrar o null para limpiar el panel
         */
        private void setTarjeta(TarjetaDesbloqueo tarjeta) {
            this.tarjeta = tarjeta;

            if (tarjeta == null) {
                imagen.setContenido(null, true);
                nombre.setText("Selecciona un objeto");
                poder.setText(" ");
                poder.setIcon(null);
                estado.setText(" ");
                precio.setText(" ");
                boton.setVisible(false);
                revalidate();
                repaint();
                return;
            }

            BufferedImage previa = tarjeta.getImagenPrevia() != null
                    ? tarjeta.getImagenPrevia() : tarjeta.getImagen();
            imagen.setContenido(previa, tarjeta.isDesbloqueado());
            nombre.setText(tarjeta.getNombre());

            Poder poderPersonaje = tarjeta.getPoder();
            if (poderPersonaje == null) {
                poder.setText(" ");
                poder.setIcon(null);
            } else {
                poder.setText("Poder: " + poderPersonaje.getNombre());
                String rutaEmoji = CatalogoPoderes.rutaEmoji(poderPersonaje);
                if (rutaEmoji != null) {
                    try {
                        poder.setIcon(BotonImagen.icono(
                                RecursosImagen.cargar(rutaEmoji), 24));
                    } catch (RuntimeException e) {
                        poder.setIcon(null);
                    }
                }
            }

            estado.setText(tarjeta.isDesbloqueado()
                    ? "Desbloqueado" : "Bloqueado");
            estado.setForeground(tarjeta.isDesbloqueado()
                    ? new Color(42, 130, 70)
                    : new Color(190, 60, 60));
            precio.setText(tarjeta.isDesbloqueado()
                    ? " " : tarjeta.getPrecio() + " puntos");
            boton.setVisible(!tarjeta.isDesbloqueado());
            boton.setEnabled(!tarjeta.isDesbloqueado());
            revalidate();
            repaint();
        }

        /**
         * Dibuja el fondo redondeado del panel de previsualización.
         *
         * @param g gráficos del componente
         */
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            RoundRectangle2D forma = new RoundRectangle2D.Float(
                    1, 1, getWidth() - 3, getHeight() - 3, 24, 24);
            g2.setColor(new Color(255, 255, 255, 145));
            g2.fill(forma);
            g2.setStroke(new BasicStroke(1.5f));
            g2.setColor(new Color(255, 255, 255, 190));
            g2.draw(forma);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private static final class BotonSeccion extends JButton {

        private boolean seleccionado;

        /**
         * Crea un botón de sección con el texto indicado.
         *
         * @param texto texto del botón
         */
        private BotonSeccion(String texto) {
            super(texto);
            setFont(new Font("SansSerif", Font.BOLD, 14));
            setPreferredSize(new Dimension(140, 38));
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        /**
         * Cambia el estado de selección del botón.
         *
         * @param seleccionado true si el botón queda seleccionado
         */
        private void setSeleccionado(boolean seleccionado) {
            this.seleccionado = seleccionado;
            repaint();
        }

        /**
         * Dibuja el botón de sección según su estado.
         *
         * @param g gráficos del componente
         */
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            int ancho = getWidth();
            int alto = getHeight();
            RoundRectangle2D forma = new RoundRectangle2D.Float(
                    1, 1, ancho - 3, alto - 3, 16, 16);

            if (seleccionado) {
                g2.setPaint(new GradientPaint(
                        0, 0, AZUL_CLARO, 0, alto, AZUL));
                g2.fill(forma);
                g2.setColor(new Color(255, 255, 255, 200));
            } else {
                g2.setColor(new Color(236, 246, 252, 210));
                g2.fill(forma);
                g2.setColor(TEXTO_SUAVE);
            }

            g2.setStroke(new BasicStroke(1.5f));
            g2.setColor(new Color(120, 160, 185));
            g2.draw(forma);

            g2.setFont(getFont());
            FontMetrics fm = g2.getFontMetrics();
            int x = (ancho - fm.stringWidth(getText())) / 2;
            int y = (alto + fm.getAscent() - fm.getDescent()) / 2;
            g2.setColor(seleccionado ? Color.WHITE : TEXTO_SUAVE);
            g2.drawString(getText(), x, y);
            g2.dispose();
        }
    }

    public static final class TarjetaDesbloqueo extends JPanel {

        private final TipoTienda tipo;
        private final String nombre;
        private final int precio;
        private final boolean desbloqueado;
        private final BufferedImage imagen;
        private final BufferedImage imagenPrevia;
        private final Poder poder;
        private final JButton botonComprar;
        private boolean seleccionada;

        /**
         * Crea una tarjeta de la tienda con sus datos y su botón de compra.
         *
         * @param tipo tipo de objeto de la tienda
         * @param nombre nombre del objeto
         * @param imagen imagen del objeto
         * @param imagenPrevia imagen alternativa para la previsualización
         * @param poder poder del personaje o null
         * @param precio precio en puntos
         * @param desbloqueado true si el objeto ya está desbloqueado
         */
        private TarjetaDesbloqueo(TipoTienda tipo, String nombre,
                                  BufferedImage imagen,
                                  BufferedImage imagenPrevia, Poder poder,
                                  int precio, boolean desbloqueado) {
            this.tipo = tipo;
            this.nombre = nombre;
            this.precio = precio;
            this.desbloqueado = desbloqueado;
            this.imagen = imagen;
            this.imagenPrevia = imagenPrevia;
            this.poder = poder;

            setOpaque(false);
            setLayout(new BorderLayout(0, 4));
            setBorder(new EmptyBorder(6, 6, 6, 6));
            // Solo los personajes se seleccionan con clic; los balones y
            // escenarios solo muestran la mano sobre su botón de compra.
            setCursor(Cursor.getPredefinedCursor(
                    tipo == TipoTienda.PERSONAJE
                            ? Cursor.HAND_CURSOR : Cursor.DEFAULT_CURSOR));

            add(new PanelImagenTienda(imagen, desbloqueado,
                    tipo == TipoTienda.PERSONAJE), BorderLayout.CENTER);

            JPanel pie = new JPanel(new BorderLayout(0, 2));
            pie.setOpaque(false);

            JLabel etiquetaNombre = new JLabel(nombre, SwingConstants.CENTER);
            etiquetaNombre.setFont(new Font("SansSerif", Font.BOLD, 14));
            etiquetaNombre.setForeground(TEXTO_CLARO);
            pie.add(etiquetaNombre, BorderLayout.NORTH);

            if (tipo == TipoTienda.PERSONAJE) {
                botonComprar = null;
            } else {
                JLabel etiquetaPrecio = new JLabel(
                        desbloqueado ? "Desbloqueado" : precio + " puntos",
                        SwingConstants.CENTER);
                etiquetaPrecio.setFont(new Font("SansSerif", Font.BOLD, 12));
                etiquetaPrecio.setForeground(desbloqueado
                        ? new Color(42, 130, 70)
                        : NARANJA_PUNTOS);
                pie.add(etiquetaPrecio, BorderLayout.CENTER);

                botonComprar = new BotonMejora(
                        desbloqueado ? "DESBLOQUEADO" : "COMPRAR");
                botonComprar.setEnabled(!desbloqueado);
                pie.add(botonComprar, BorderLayout.SOUTH);
            }

            add(pie, BorderLayout.SOUTH);
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
         * Devuelve la imagen del objeto.
         *
         * @return imagen del objeto
         */
        public BufferedImage getImagen() {
            return imagen;
        }

        /**
         * Devuelve la imagen alternativa de previsualización.
         *
         * @return imagen de previsualización o null
         */
        public BufferedImage getImagenPrevia() {
            return imagenPrevia;
        }

        /**
         * Devuelve el poder del personaje.
         *
         * @return poder del personaje o null
         */
        public Poder getPoder() {
            return poder;
        }

        /**
         * Devuelve el botón de compra de la tarjeta.
         *
         * @return botón de compra o null para los personajes
         */
        public JButton getBotonComprar() {
            return botonComprar;
        }

        /**
         * Devuelve el tipo de objeto de la tarjeta.
         *
         * @return tipo de objeto de la tienda
         */
        public TipoTienda getTipo() {
            return tipo;
        }

        /**
         * Devuelve el nombre del objeto.
         *
         * @return nombre del objeto
         */
        public String getNombre() {
            return nombre;
        }

        /**
         * Devuelve el precio del objeto en puntos.
         *
         * @return precio en puntos
         */
        public int getPrecio() {
            return precio;
        }

        /**
         * Indica si el objeto ya está desbloqueado.
         *
         * @return true si el objeto está desbloqueado
         */
        public boolean isDesbloqueado() {
            return desbloqueado;
        }

        /**
         * Dibuja el fondo y el borde de la tarjeta.
         *
         * @param g gráficos del componente
         */
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            RoundRectangle2D forma = new RoundRectangle2D.Float(
                    1, 1, getWidth() - 3, getHeight() - 3, 22, 22);
            g2.setColor(new Color(236, 246, 252, 230));
            g2.fill(forma);

            if (seleccionada) {
                g2.setStroke(new BasicStroke(4f));
                g2.setColor(new Color(255, 205, 70));
            } else {
                g2.setStroke(new BasicStroke(1.5f));
                g2.setColor(desbloqueado
                        ? new Color(120, 190, 145)
                        : new Color(120, 160, 185));
            }
            g2.draw(forma);
            g2.dispose();

            super.paintComponent(g);
        }
    }

    private static final class PanelImagenTienda extends JComponent {

        private static final BufferedImage CADENA = cargarCadena();

        private BufferedImage imagen;
        private boolean desbloqueado;
        private boolean cubrir;

        /**
         * Crea el panel de imagen sin recorte de relleno.
         *
         * @param imagen imagen a mostrar
         * @param desbloqueado true si el objeto está desbloqueado
         */
        private PanelImagenTienda(BufferedImage imagen, boolean desbloqueado) {
            this(imagen, desbloqueado, false);
        }

        /**
         * Crea el panel de imagen indicando si debe cubrir el espacio.
         *
         * @param imagen imagen a mostrar
         * @param desbloqueado true si el objeto está desbloqueado
         * @param cubrir true si la imagen debe cubrir el panel
         */
        private PanelImagenTienda(BufferedImage imagen, boolean desbloqueado,
                                  boolean cubrir) {
            this.cubrir = cubrir;
            setContenido(imagen, desbloqueado);
        }

        /**
         * Cambia la imagen y el estado de bloqueo del panel.
         *
         * @param imagen nueva imagen o null
         * @param desbloqueado true si el objeto está desbloqueado
         */
        private void setContenido(BufferedImage imagen, boolean desbloqueado) {
            this.imagen = imagen == null ? null : RecursosImagen.recortarTransparente(imagen);
            this.desbloqueado = desbloqueado;
            repaint();
        }

        /**
         * Dibuja la imagen y la cadena de bloqueo si corresponde.
         *
         * @param g gráficos del componente
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
            RoundRectangle2D forma = new RoundRectangle2D.Float(
                    1, 1, ancho - 3, alto - 3, 16, 16);
            g2.setColor(new Color(248, 252, 255));
            g2.fill(forma);
            g2.clip(forma);

            if (imagen != null) {
                double escala = cubrir
                        ? Math.max(
                                (double) (ancho - 14) / imagen.getWidth(),
                                (double) (alto - 14) / imagen.getHeight())
                        : Math.min(
                                (double) (ancho - 14) / imagen.getWidth(),
                                (double) (alto - 14) / imagen.getHeight());
                int anchoImagen = (int) (imagen.getWidth() * escala);
                int altoImagen = (int) (imagen.getHeight() * escala);
                g2.drawImage(imagen,
                        (ancho - anchoImagen) / 2,
                        (alto - altoImagen) / 2,
                        anchoImagen, altoImagen, null);
            }

            if (!desbloqueado && CADENA != null) {
                double escalaCadena = Math.max(
                        (double) ancho / CADENA.getWidth(),
                        (double) alto / CADENA.getHeight());
                int anchoCadena = (int) Math.ceil(CADENA.getWidth() * escalaCadena);
                int altoCadena = (int) Math.ceil(CADENA.getHeight() * escalaCadena);
                g2.drawImage(CADENA,
                        (ancho - anchoCadena) / 2,
                        (alto - altoCadena) / 2,
                        anchoCadena, altoCadena, null);
            }

            g2.dispose();
        }

        /**
         * Carga la imagen de la cadena de bloqueo.
         *
         * @return imagen de la cadena o null si no se puede cargar
         */
        private static BufferedImage cargarCadena() {
            try {
                return RecursosImagen.cargar(
                        "resources/images/Objetos/CadenaDeBloqueo.png");
            } catch (RuntimeException e) {
                return null;
            }
        }
    }

    private static final class TarjetaVaciaTienda extends JComponent {

        /**
         * Dibuja la tarjeta vacía con el signo de interrogación.
         *
         * @param g gráficos del componente
         */
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            RoundRectangle2D forma = new RoundRectangle2D.Float(
                    1, 1, getWidth() - 3, getHeight() - 3, 22, 22);
            g2.setColor(new Color(8, 26, 48, 235));
            g2.fill(forma);
            g2.setStroke(new BasicStroke(2.5f));
            g2.setColor(new Color(202, 170, 92));
            g2.draw(forma);

            g2.setFont(new Font("SansSerif", Font.BOLD,
                    Math.max(30, getHeight() / 3)));
            FontMetrics fm = g2.getFontMetrics();
            String signo = "?";
            int x = (getWidth() - fm.stringWidth(signo)) / 2;
            int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
            g2.setColor(new Color(242, 205, 98));
            g2.drawString(signo, x, y);
            g2.dispose();
        }
    }

    private static final class BotonMejora extends JButton {

        /**
         * Crea el botón de mejora con el texto por defecto.
         */
        private BotonMejora() {
            this("MEJORAR");
        }

        /**
         * Crea el botón de mejora con el texto indicado.
         *
         * @param texto texto del botón
         */
        private BotonMejora(String texto) {
            super(texto);
            setFont(new Font("SansSerif", Font.BOLD, 14));
            setPreferredSize(new Dimension(150, 34));
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        /**
         * Dibuja el botón según su estado y el cursor encima.
         *
         * @param g gráficos del componente
         */
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

        /**
         * Crea la barra de estadística con su tamaño preferido.
         */
        private BarraEstadistica() {
            setPreferredSize(new Dimension(240, 20));
        }

        /**
         * Actualiza el valor de la barra y la redibuja.
         *
         * @param valor nuevo valor de la estadística
         */
        private void setValor(int valor) {
            this.valor = Math.max(0,
                    Math.min(ParametrosJuego.MAX_ESTADISTICA, valor));
            repaint();
        }

        /**
         * Dibuja los segmentos llenos y vacíos de la barra.
         *
         * @param g gráficos del componente
         */
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

}
