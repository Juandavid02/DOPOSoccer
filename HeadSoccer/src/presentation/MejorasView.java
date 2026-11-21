package presentation;

import domain.Estadisticas;
import domain.ParametrosJuego;
import domain.PerfilJugador;
import domain.Poder;
import domain.TipoEstadistica;
import domain.Tienda;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private Tienda desbloqueos;
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
    public void setDesbloqueos(Tienda desbloqueos) {
        this.desbloqueos = desbloqueos;
        actualizarTienda();
    }

    /**
     * Define la acción que se ejecuta al comprar una tarjeta.
     *
     * @param accionCompra acción de compra o null para no hacer nada
     */
    public void setAccionCompra(Consumer<TarjetaDesbloqueo> accionCompra) {
        if (accionCompra == null) {
            this.accionCompra = tarjeta -> { };
        } else {
            this.accionCompra = accionCompra;
        }
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
        listaPerfiles.setCellRenderer(new PerfilRenderer(16, 7, true));
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

        BarraEstadistica barra = new BarraEstadistica(240, 20, 4, false);
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
        boton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                seleccionarSeccion(nombre);
            }
        });
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
                    Tienda.PRECIO_PERSONAJE, preview);
            if (primera == null) {
                primera = tarjeta;
            }
            cuadricula.add(tarjeta);
        }
        cuadricula.add(TarjetaPanel.vacia());
        cuadricula.add(TarjetaPanel.vacia());
        cuadricula.add(TarjetaPanel.vacia());
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
                    Tienda.PRECIO_BALON, null));
        }
        cuadricula.add(TarjetaPanel.vacia());
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
                    Tienda.PRECIO_ESCENARIO, null));
        }
        cuadricula.add(TarjetaPanel.vacia());
        cuadricula.add(TarjetaPanel.vacia());
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
            tarjeta.getBotonComprar().addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    accionCompra.accept(tarjeta);
                }
            });
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

        int nuevoIndice = 0;
        if (seleccion >= 0) {
            nuevoIndice = Math.max(0,
                    Math.min(seleccion, modeloPerfiles.size() - 1));
        }
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
            setOpaque(true);
            setBackground(new Color(245, 250, 253));
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(200, 220, 235)),
                    new EmptyBorder(10, 10, 10, 10)));
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
            boton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (tarjeta != null) {
                        accionCompra.accept(tarjeta);
                    }
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
            if (accionCompra == null) {
                this.accionCompra = t -> { };
            } else {
                this.accionCompra = accionCompra;
            }
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

            BufferedImage previa;
            if (tarjeta.getImagenPrevia() != null) {
                previa = tarjeta.getImagenPrevia();
            } else {
                previa = tarjeta.getImagen();
            }
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

            String textoEstado;
            if (tarjeta.isDesbloqueado()) {
                textoEstado = "Desbloqueado";
            } else {
                textoEstado = "Bloqueado";
            }
            estado.setText(textoEstado);

            Color colorEstado;
            if (tarjeta.isDesbloqueado()) {
                colorEstado = new Color(42, 130, 70);
            } else {
                colorEstado = new Color(190, 60, 60);
            }
            estado.setForeground(colorEstado);

            String textoPrecio;
            if (tarjeta.isDesbloqueado()) {
                textoPrecio = " ";
            } else {
                textoPrecio = tarjeta.getPrecio() + " puntos";
            }
            precio.setText(textoPrecio);
            boton.setVisible(!tarjeta.isDesbloqueado());
            boton.setEnabled(!tarjeta.isDesbloqueado());
            revalidate();
            repaint();
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
            setOpaque(true);
            setFocusPainted(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            actualizarEstilo();
        }

        /**
         * Cambia el estado de selección del botón.
         *
         * @param seleccionado true si el botón queda seleccionado
         */
        private void setSeleccionado(boolean seleccionado) {
            this.seleccionado = seleccionado;
            actualizarEstilo();
        }

        /**
         * Aplica el color y el borde según el estado de selección.
         */
        private void actualizarEstilo() {
            if (seleccionado) {
                setBackground(AZUL);
                setForeground(Color.WHITE);
                setBorder(BorderFactory.createLineBorder(AZUL_CLARO));
            } else {
                setBackground(new Color(236, 246, 252));
                setForeground(TEXTO_SUAVE);
                setBorder(BorderFactory.createLineBorder(
                        new Color(120, 160, 185)));
            }
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

            setOpaque(true);
            setBackground(new Color(236, 246, 252));
            setLayout(new BorderLayout(0, 4));
            setBorder(new EmptyBorder(6, 6, 6, 6));
            // Solo los personajes se seleccionan con clic; los balones y
            // escenarios solo muestran la mano sobre su botón de compra.
            int tipoCursor = Cursor.DEFAULT_CURSOR;
            if (tipo == TipoTienda.PERSONAJE) {
                tipoCursor = Cursor.HAND_CURSOR;
            }
            setCursor(Cursor.getPredefinedCursor(tipoCursor));

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
                String textoPrecio;
                if (desbloqueado) {
                    textoPrecio = "Desbloqueado";
                } else {
                    textoPrecio = precio + " puntos";
                }
                JLabel etiquetaPrecio = new JLabel(
                        textoPrecio, SwingConstants.CENTER);
                etiquetaPrecio.setFont(new Font("SansSerif", Font.BOLD, 12));
                Color colorPrecio;
                if (desbloqueado) {
                    colorPrecio = new Color(42, 130, 70);
                } else {
                    colorPrecio = NARANJA_PUNTOS;
                }
                etiquetaPrecio.setForeground(colorPrecio);
                pie.add(etiquetaPrecio, BorderLayout.CENTER);

                String textoBoton;
                if (desbloqueado) {
                    textoBoton = "DESBLOQUEADO";
                } else {
                    textoBoton = "COMPRAR";
                }
                botonComprar = new BotonMejora(textoBoton);
                botonComprar.setEnabled(!desbloqueado);
                pie.add(botonComprar, BorderLayout.SOUTH);
            }

            add(pie, BorderLayout.SOUTH);
            actualizarEstilo();
        }

        /**
         * Cambia el estado de selección de la tarjeta.
         *
         * @param seleccionada true si la tarjeta queda seleccionada
         */
        private void setSeleccionada(boolean seleccionada) {
            this.seleccionada = seleccionada;
            actualizarEstilo();
        }

        /**
         * Aplica el borde según el estado de selección y desbloqueo.
         */
        private void actualizarEstilo() {
            Color colorBorde;
            int grosor;
            if (seleccionada) {
                colorBorde = new Color(255, 205, 70);
                grosor = 4;
            } else {
                colorBorde = desbloqueado
                        ? new Color(120, 190, 145)
                        : new Color(120, 160, 185);
                grosor = 1;
            }
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(colorBorde, grosor),
                    new EmptyBorder(6, 6, 6, 6)));
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
    }

    private static final class PanelImagenTienda extends JComponent {

        private static final BufferedImage CADENA = RecursosImagen.cargarCadena();

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
            if (imagen == null) {
                this.imagen = null;
            } else {
                this.imagen = RecursosImagen.recortarTransparente(imagen);
            }
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
                double escala;
                if (cubrir) {
                    escala = Math.max(
                            (double) (ancho - 14) / imagen.getWidth(),
                            (double) (alto - 14) / imagen.getHeight());
                } else {
                    escala = Math.min(
                            (double) (ancho - 14) / imagen.getWidth(),
                            (double) (alto - 14) / imagen.getHeight());
                }
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
            setOpaque(true);
            setBackground(AZUL);
            setForeground(Color.WHITE);
            setBorder(BorderFactory.createLineBorder(new Color(24, 78, 110)));
            setFocusPainted(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        /**
         * Ajusta los colores cuando el botón se habilita o deshabilita.
         *
         * @param enabled true para habilitar el botón
         */
        @Override
        public void setEnabled(boolean enabled) {
            super.setEnabled(enabled);
            setBackground(enabled ? AZUL : new Color(205, 216, 222));
            setForeground(enabled ? Color.WHITE : new Color(110, 125, 135));
        }
    }

}
