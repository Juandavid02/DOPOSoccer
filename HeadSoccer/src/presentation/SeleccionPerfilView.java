package presentation;

import domain.Modalidad;
import domain.PerfilJugador;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class SeleccionPerfilView extends VistaBase {

    private static final String RUTA_BOTONES = "resources/images/Botones/";
    private static final Color COLOR_TEXTO = new Color(22, 48, 68);
    private static final Color COLOR_ETIQUETA = new Color(30, 62, 86);
    private static final int ANCHO_READY_PVP = 300;
    private static final int ANCHO_READY_SOLO = 265;

    private final BufferedImage imagenReadyPvp =
            RecursosImagen.cargar(RUTA_BOTONES + "ReadyPerfil.png");
    private final BufferedImage imagenReadySolo =
            RecursosImagen.cargar(RUTA_BOTONES + "ReadySolo.png");

    private PanelJugador panelJugador1;
    private PanelJugador panelJugador2;
    private JButton botonContinuarCentral;

    /**
     * Crea la pantalla para que los dos jugadores elijan su perfil.
     */
    public SeleccionPerfilView() {
        super("resources/images/Ventanas/FondoPerfil.png");
        setTitulo("SELECCIONAR PERFIL");
        prepareViewCentro();
    }

    /**
     * Prepara la tarjeta central con los paneles de ambos jugadores y el
     * botón de continuar.
     */
    private void prepareViewCentro() {
        panelJugador1 = new PanelJugador("JUGADOR 1");
        panelJugador2 = new PanelJugador("JUGADOR 2");
        panelJugador1.setPreferredSize(new Dimension(500, 430));
        panelJugador2.setPreferredSize(new Dimension(500, 430));

        botonContinuarCentral = BotonImagen.crear(
                imagenReadyPvp, ANCHO_READY_PVP);
        botonContinuarCentral.setVisible(false);

        JPanel contenedor = new JPanel(new GridBagLayout());
        contenedor.setOpaque(false);
        contenedor.setBorder(new EmptyBorder(0, 30, 0, 30));

        GridBagConstraints c = new GridBagConstraints();
        c.gridy = 0;
        c.insets = new Insets(0, 18, 0, 18);
        c.gridx = 0;
        contenedor.add(panelJugador1, c);
        c.gridx = 1;
        contenedor.add(panelJugador2, c);

        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        c.insets = new Insets(22, 0, 0, 0);
        JPanel panelContinuar = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panelContinuar.setOpaque(false);
        panelContinuar.add(botonContinuarCentral);
        contenedor.add(panelContinuar, c);

        setCentro(contenedor, 6, 26);
    }

    /**
     * Muestra u oculta el botón central de continuar.
     *
     * @param visible indica si el botón se muestra
     */
    public void setContinuarCentralVisible(boolean visible) {
        botonContinuarCentral.setVisible(visible);
        revalidate();
        repaint();
    }

    /**
     * En PvsP se piden dos perfiles humanos; en PvsM solo el del humano y
     * se oculta el panel de la máquina.
     *
     * @param modalidad modalidad seleccionada
     */
    public void setModo(Modalidad modalidad) {
        panelJugador2.setVisible(modalidad != Modalidad.PvsM);
        boolean solo = modalidad == Modalidad.PvsM;
        BufferedImage imagen = imagenReadyPvp;
        int ancho = ANCHO_READY_PVP;
        if (solo) {
            imagen = imagenReadySolo;
            ancho = ANCHO_READY_SOLO;
        }
        BotonImagen.cambiarImagen(botonContinuarCentral, imagen, ancho);
        revalidate();
        repaint();
    }

    /**
     * Deja la pantalla lista para una nueva selección de perfiles.
     */
    public void reiniciar() {
        panelJugador1.reiniciar();
        panelJugador2.reiniciar();
        setContinuarCentralVisible(false);
    }

    /**
     * Devuelve el panel del jugador 1.
     *
     * @return el panel del jugador 1
     */
    public PanelJugador getPanelJugador1() {
        return panelJugador1;
    }

    /**
     * Devuelve el panel del jugador 2.
     *
     * @return el panel del jugador 2
     */
    public PanelJugador getPanelJugador2() {
        return panelJugador2;
    }

    /**
     * Devuelve el botón central para continuar.
     *
     * @return el botón de continuar
     */
    public JButton getBotonContinuarCentral() {
        return botonContinuarCentral;
    }

    // ------------------------------------------------------------------
    // Panel de un jugador: lista de perfiles y botones.
    // ------------------------------------------------------------------
    public static final class PanelJugador extends TarjetaPanel {

        private final CardLayout cardLayout = new CardLayout();
        private final JPanel cuerpo = new JPanel(cardLayout);

        private final DefaultListModel<String> modelo = new DefaultListModel<>();
        private final JList<String> lista = new JList<>(modelo);
        private final JLabel nombreElegido = new JLabel("", SwingConstants.CENTER);

        private final JButton botonContinuar;
        private final JButton botonCambiar;
        private final JButton botonCrear;
        private final JButton botonCrearListo;

        private List<PerfilJugador> perfilesMostrados = new ArrayList<>();

        /**
         * Crea el panel de un jugador con su título y sus botones.
         *
         * @param titulo título del panel
         */
        private PanelJugador(String titulo) {
            setLayout(new BorderLayout(0, 14));
            setBorder(new EmptyBorder(22, 24, 22, 24));

            botonContinuar = BotonImagen.crear(
                    RUTA_BOTONES + "ContinuarSeleccionPerfil.png", 180);
            botonCambiar = BotonImagen.crear(
                    RUTA_BOTONES + "CambiarPerfil.png", 200);
            botonCrear = crearBotonCrear();
            botonCrearListo = crearBotonCrear();

            add(prepararTitulo(titulo), BorderLayout.NORTH);

            cuerpo.setOpaque(false);
            cuerpo.add(prepararSeleccion(), "seleccion");
            cuerpo.add(prepararListo(), "listo");
            add(cuerpo, BorderLayout.CENTER);
        }

        /**
         * Crea la etiqueta del título del panel.
         *
         * @param titulo texto del título
         * @return la etiqueta del título
         */
        private JLabel prepararTitulo(String titulo) {
            JLabel etiqueta = new JLabel(titulo, SwingConstants.CENTER);
            etiqueta.setFont(new Font("SansSerif", Font.BOLD, 24));
            etiqueta.setForeground(COLOR_TEXTO);
            return etiqueta;
        }

        /**
         * Prepara el panel donde se elige el perfil.
         *
         * @return el panel de selección
         */
        private JPanel prepararSeleccion() {
            lista.setOpaque(false);
            lista.setForeground(COLOR_TEXTO);
            lista.setFont(new Font("SansSerif", Font.BOLD, 17));
            lista.setSelectionForeground(Color.WHITE);
            lista.setFixedCellHeight(38);
            lista.setCellRenderer(new PerfilRenderer(17, 6, false));
            lista.setBorder(new EmptyBorder(0, 0, 0, 8));

            JScrollPane scroll = new JScrollPane(lista);
            scroll.setOpaque(false);
            scroll.getViewport().setOpaque(false);
            scroll.setBorder(BorderFactory.createEmptyBorder());

            JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
            panelBotones.setOpaque(false);
            panelBotones.add(botonContinuar);
            panelBotones.add(botonCrear);

            JPanel panel = new JPanel(new BorderLayout(0, 12));
            panel.setOpaque(false);
            panel.add(scroll, BorderLayout.CENTER);
            panel.add(panelBotones, BorderLayout.SOUTH);
            return panel;
        }

        /**
         * Prepara el panel que se muestra cuando el perfil ya está elegido.
         *
         * @return el panel de perfil listo
         */
        private JPanel prepararListo() {
            nombreElegido.setFont(new Font("SansSerif", Font.BOLD, 26));
            nombreElegido.setForeground(COLOR_TEXTO);

            JLabel etiqueta = new JLabel("PERFIL ELEGIDO", SwingConstants.CENTER);
            etiqueta.setFont(new Font("SansSerif", Font.BOLD, 15));
            etiqueta.setForeground(COLOR_ETIQUETA);

            JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
            panelBotones.setOpaque(false);
            panelBotones.add(botonCambiar);
            panelBotones.add(botonCrearListo);

            JPanel panel = new JPanel(new GridBagLayout());
            panel.setOpaque(false);

            GridBagConstraints c = new GridBagConstraints();
            c.gridx = 0;

            c.gridy = 0;
            c.weighty = 1;
            JPanel rellenoArriba = new JPanel();
            rellenoArriba.setOpaque(false);
            panel.add(rellenoArriba, c);

            c.gridy = 1;
            c.weighty = 0;
            c.insets = new Insets(0, 0, 8, 0);
            panel.add(etiqueta, c);

            c.gridy = 2;
            c.insets = new Insets(0, 0, 18, 0);
            panel.add(nombreElegido, c);

            c.gridy = 3;
            c.insets = new Insets(0, 0, 0, 0);
            panel.add(panelBotones, c);

            c.gridy = 4;
            c.weighty = 1;
            JPanel rellenoAbajo = new JPanel();
            rellenoAbajo.setOpaque(false);
            panel.add(rellenoAbajo, c);

            return panel;
        }

        /**
         * Crea el botón para crear un perfil.
         *
         * @return el botón de crear perfil
         */
        private JButton crearBotonCrear() {
            return BotonImagen.crear(RUTA_BOTONES + "CrearPerfil.png", 170);
        }

        /**
         * Muestra en la lista los perfiles disponibles.
         *
         * @param perfiles perfiles que se pueden elegir
         */
        public void mostrarPerfiles(List<PerfilJugador> perfiles) {
            PerfilJugador anterior = getSeleccionado();

            modelo.clear();
            perfilesMostrados = new ArrayList<>(perfiles);
            for (PerfilJugador perfil : perfiles) {
                modelo.addElement(perfil.getNombre());
            }

            if (anterior != null) {
                for (int i = 0; i < perfilesMostrados.size(); i++) {
                    if (perfilesMostrados.get(i).getId().equals(anterior.getId())) {
                        lista.setSelectedIndex(i);
                        break;
                    }
                }
            } else {
                lista.clearSelection();
            }
        }

        /**
         * Devuelve el perfil seleccionado en la lista.
         *
         * @return el perfil seleccionado o null si no hay ninguno
         */
        public PerfilJugador getSeleccionado() {
            int indice = lista.getSelectedIndex();
            if (indice >= 0 && indice < perfilesMostrados.size()) {
                return perfilesMostrados.get(indice);
            }
            return null;
        }

        /**
         * Muestra el nombre del perfil elegido y cambia al panel de listo.
         *
         * @param nombre nombre del perfil elegido
         */
        public void marcarListo(String nombre) {
            nombreElegido.setText(nombre);
            cardLayout.show(cuerpo, "listo");
            revalidate();
            repaint();
        }

        /**
         * Vuelve al panel de selección de perfil.
         */
        public void volverASeleccion() {
            cardLayout.show(cuerpo, "seleccion");
            lista.clearSelection();
            revalidate();
            repaint();
        }

        /**
         * Deja el panel sin perfiles para una nueva selección.
         */
        public void reiniciar() {
            volverASeleccion();
            modelo.clear();
            perfilesMostrados.clear();
        }

        /**
         * Devuelve la lista de perfiles.
         *
         * @return la lista de perfiles
         */
        public JList<String> getLista() {
            return lista;
        }

        /**
         * Devuelve el botón de continuar.
         *
         * @return el botón de continuar
         */
        public JButton getBotonContinuar() {
            return botonContinuar;
        }

        /**
         * Devuelve el botón de cambiar perfil.
         *
         * @return el botón de cambiar perfil
         */
        public JButton getBotonCambiar() {
            return botonCambiar;
        }

        /**
         * Devuelve el botón de crear perfil.
         *
         * @return el botón de crear perfil
         */
        public JButton getBotonCrear() {
            return botonCrear;
        }

        /**
         * Devuelve el botón de crear perfil del panel de listo.
         *
         * @return el botón de crear perfil
         */
        public JButton getBotonCrearListo() {
            return botonCrearListo;
        }
    }

    // ------------------------------------------------------------------
    // Renderer de la lista de perfiles.
    // ------------------------------------------------------------------
}
