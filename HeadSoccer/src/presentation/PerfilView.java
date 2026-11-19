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
        listaPerfiles.setCellRenderer(new PerfilRenderer(18, 7, false));
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

        barraVelocidad = new BarraEstadistica(240, 18, 4, true);
        barraSalto = new BarraEstadistica(240, 18, 4, true);
        barraPatada = new BarraEstadistica(240, 18, 4, true);
        barraSupervivencia = new BarraEstadistica(240, 18, 4, true);

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
}
