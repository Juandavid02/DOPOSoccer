package presentation;

import domain.ParticipanteTorneo;
import domain.PartidoTorneo;
import domain.ParametrosJuego;
import domain.PerfilJugador;
import domain.Personaje;
import domain.Torneo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * Pantalla del torneo. Primero muestra la inscripción y después el cuadro
 * de semifinales, final y campeón con componentes estándar.
 */
public class TorneoView extends VistaBase {

    private static final String RUTA_FONDO =
            "resources/images/Ventanas/FondoPerfil.png";
    private static final String RUTA_BOTONES =
            "resources/images/Botones/";

    private static final Color COLOR_TEXTO = new Color(22, 48, 68);
    private static final Color COLOR_SUAVE = new Color(70, 96, 116);
    private static final Color COLOR_ORO = new Color(190, 138, 20);
    private static final Color COLOR_ROJO = new Color(170, 52, 58);
    private static final Color COLOR_VERDE = new Color(36, 128, 74);
    private static final Color COLOR_JUGADOR1 = new Color(48, 132, 220);

    private static final Dimension TAMANO_INSCRIPCION =
            new Dimension(900, 560);
    private static final Dimension TAMANO_LLAVE =
            new Dimension(1280, 840);

    private final TarjetaPanel tarjeta = new TarjetaPanel();
    private final MarcoTorneo marco = new MarcoTorneo();
    private final JPanel contenido = new JPanel(new BorderLayout());
    private final JPanel pieInferior = new JPanel();
    private final JPanel pastillaMensaje = new PastillaMensaje();
    private final JLabel mensaje = new JLabel("", SwingConstants.CENTER);
    private final JButton botonInscribirse = new BotonTexto("INSCRIBIRSE");
    private final JButton botonJugar = BotonImagen.crear(
            RUTA_BOTONES + "Play.png", 280);
    private final Map<String, BufferedImage> banderas = new HashMap<>();

    /**
     * Crea la vista del torneo con la tarjeta de inscripción y la llave.
     */
    public TorneoView() {
        super(RUTA_FONDO);
        setTitulo("TORNEO");

        tarjeta.setLayout(new BorderLayout(0, 8));
        tarjeta.setBorder(new EmptyBorder(16, 24, 14, 24));
        tarjeta.setPreferredSize(TAMANO_INSCRIPCION);

        contenido.setOpaque(false);
        tarjeta.add(contenido, BorderLayout.CENTER);

        mensaje.setFont(new Font("SansSerif", Font.BOLD, 17));
        mensaje.setForeground(COLOR_SUAVE);
        mensaje.setAlignmentX(Component.CENTER_ALIGNMENT);

        pastillaMensaje.setAlignmentX(Component.CENTER_ALIGNMENT);
        pastillaMensaje.add(mensaje, BorderLayout.CENTER);

        botonJugar.setAlignmentX(Component.CENTER_ALIGNMENT);
        botonJugar.setVisible(false);

        pieInferior.setOpaque(false);
        pieInferior.setLayout(new BoxLayout(pieInferior, BoxLayout.Y_AXIS));
        pieInferior.add(pastillaMensaje);
        pieInferior.add(Box.createVerticalStrut(8));
        pieInferior.add(botonJugar);

        tarjeta.add(pieInferior, BorderLayout.SOUTH);

        marco.add(tarjeta);
        setCentroDirecto(marco);
    }

    /**
     * Muestra la pantalla de inscripción con el perfil del jugador.
     *
     * @param perfil perfil del jugador humano
     */
    public void mostrarInscripcion(PerfilJugador perfil) {
        tarjeta.setPreferredSize(TAMANO_INSCRIPCION);
        botonJugar.setVisible(false);

        contenido.removeAll();
        contenido.add(crearInscripcion(perfil), BorderLayout.CENTER);
        revalidar();
    }

    /**
     * Muestra la llave del torneo con los participantes.
     *
     * @param torneo torneo que se va a dibujar
     */
    public void mostrarTorneo(Torneo torneo) {
        tarjeta.setPreferredSize(TAMANO_LLAVE);
        botonJugar.setVisible(true);

        contenido.removeAll();
        contenido.add(crearLlave(torneo), BorderLayout.CENTER);

        PartidoTorneo siguiente = torneo.getSiguientePartidoHumano();
        botonJugar.setEnabled(siguiente != null);
        actualizarMensaje(torneo, siguiente);
        revalidar();
    }

    /**
     * Actualiza y repinta los paneles de la vista.
     */
    private void revalidar() {
        tarjeta.revalidate();
        contenido.revalidate();
        pieInferior.revalidate();
        pastillaMensaje.revalidate();
        marco.revalidate();
        revalidate();
        contenido.repaint();
        marco.repaint();
        repaint();
    }

    /**
     * Centra la tarjeta y la encoge si la ventana no alcanza para su tamaño
     * preferido. Así la llave se sigue viendo en pantallas pequeñas.
     */
    private final class MarcoTorneo extends JPanel {

        private static final int MARGEN = 20;

        /**
         * Crea el panel que centra la tarjeta dentro de la ventana.
         */
        private MarcoTorneo() {
            super(null);
            setOpaque(false);
        }

        /**
         * Distribuye la tarjeta centrada y ajustada al tamaño disponible.
         */
        @Override
        public void doLayout() {
            Dimension preferido = tarjeta.getPreferredSize();
            int ancho = Math.min(preferido.width,
                    Math.max(1, getWidth() - MARGEN * 2));
            int alto = Math.min(preferido.height,
                    Math.max(1, getHeight() - MARGEN * 2));
            tarjeta.setBounds(
                    (getWidth() - ancho) / 2,
                    (getHeight() - alto) / 2,
                    ancho,
                    alto);
        }
    }

    /**
     * Pastilla clara detrás del mensaje para que se lea sobre el fondo del
     * estadio.
     */
    private static final class PastillaMensaje extends JPanel {

        /**
         * Crea la pastilla de fondo del mensaje.
         */
        private PastillaMensaje() {
            setOpaque(true);
            setBackground(Color.WHITE);
            setLayout(new BorderLayout());
            setBorder(new EmptyBorder(5, 18, 5, 18));
        }

        /**
         * Devuelve el tamaño máximo de la pastilla.
         *
         * @return tamaño máximo de la pastilla
         */
        @Override
        public Dimension getMaximumSize() {
            return getPreferredSize();
        }
    }

    /**
     * Crea el panel de inscripción con el saldo del perfil.
     *
     * @param perfil perfil del jugador humano
     * @return panel con los datos de la inscripción
     */
    private JPanel crearInscripcion(PerfilJugador perfil) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);

        JPanel columna = new JPanel();
        columna.setOpaque(false);
        columna.setLayout(new BoxLayout(columna, BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel("INSCRIPCIÓN AL TORNEO");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 30));
        titulo.setForeground(COLOR_TEXTO);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        columna.add(titulo);
        columna.add(Box.createVerticalStrut(18));

        columna.add(crearFila("INSCRIPCIÓN", "- " + ParametrosJuego.COSTO_TORNEO
                + " pts", COLOR_ROJO));
        columna.add(Box.createVerticalStrut(8));
        columna.add(crearFila("CAMPEÓN", "+ "
                + ParametrosJuego.PREMIO_CAMPEON_TORNEO + " pts", COLOR_ORO));
        columna.add(Box.createVerticalStrut(8));
        columna.add(crearFila("SUBCAMPEÓN", "+ "
                + ParametrosJuego.PREMIO_SUBCAMPEON_TORNEO + " pts", COLOR_ORO));
        columna.add(Box.createVerticalStrut(18));

        int puntos = 0;
        if (perfil != null) {
            puntos = perfil.getPuntos().getTotal();
        }
        JLabel saldo = new JLabel("TUS PUNTOS: " + puntos);
        saldo.setFont(new Font("SansSerif", Font.BOLD, 22));
        saldo.setForeground(COLOR_TEXTO);
        saldo.setAlignmentX(Component.CENTER_ALIGNMENT);
        columna.add(saldo);
        columna.add(Box.createVerticalStrut(12));

        JLabel aviso = new JLabel("La inscripción no es reembolsable.");
        aviso.setFont(new Font("SansSerif", Font.PLAIN, 15));
        aviso.setForeground(COLOR_SUAVE);
        aviso.setAlignmentX(Component.CENTER_ALIGNMENT);
        columna.add(aviso);
        columna.add(Box.createVerticalStrut(18));

        botonInscribirse.setAlignmentX(Component.CENTER_ALIGNMENT);
        botonInscribirse.setEnabled(puntos >= ParametrosJuego.COSTO_TORNEO);
        columna.add(botonInscribirse);

        if (puntos < ParametrosJuego.COSTO_TORNEO) {
            int faltan = ParametrosJuego.COSTO_TORNEO - puntos;
            actualizarMensaje("Te faltan " + faltan
                    + " puntos para inscribirte.", COLOR_ROJO);
        } else {
            actualizarMensaje("Al inscribirte se descuentan "
                    + ParametrosJuego.COSTO_TORNEO + " puntos.", COLOR_SUAVE);
        }

        panel.add(columna);
        return panel;
    }

    /**
     * Crea una fila con la etiqueta y el valor del premio.
     *
     * @param etiqueta texto de la etiqueta
     * @param valor texto del valor
     * @param color color del valor
     * @return panel con la fila creada
     */
    private JPanel crearFila(String etiqueta, String valor, Color color) {
        JPanel fila = new JPanel(new BorderLayout());
        fila.setOpaque(false);
        fila.setMaximumSize(new Dimension(520, 40));

        JLabel nombre = new JLabel(etiqueta);
        nombre.setFont(new Font("SansSerif", Font.BOLD, 18));
        nombre.setForeground(COLOR_TEXTO);

        JLabel monto = new JLabel(valor);
        monto.setFont(new Font("SansSerif", Font.BOLD, 20));
        monto.setForeground(color);

        fila.add(nombre, BorderLayout.WEST);
        fila.add(monto, BorderLayout.EAST);
        return fila;
    }

    /**
     * Crea la llave del torneo con su cabecera.
     *
     * @param torneo torneo que se va a dibujar
     * @return componente con la llave del torneo
     */
    private JComponent crearLlave(Torneo torneo) {
        JPanel raiz = new JPanel(new BorderLayout(0, 10));
        raiz.setOpaque(false);
        raiz.add(crearCabecera(torneo), BorderLayout.NORTH);
        raiz.add(crearCuadro(torneo), BorderLayout.CENTER);
        return raiz;
    }

    /**
     * Crea el cuadro con las semifinales, la final y el campeón usando
     * paneles y etiquetas estándar.
     *
     * @param torneo torneo mostrado
     * @return panel con el cuadro del torneo
     */
    private JComponent crearCuadro(Torneo torneo) {
        JPanel columnas = new JPanel(new GridLayout(1, 3, 18, 0));
        columnas.setOpaque(false);

        JPanel semifinales = new JPanel(new GridLayout(2, 1, 0, 14));
        semifinales.setOpaque(false);
        semifinales.add(crearPartido("SEMIFINAL 1", torneo.getSemifinal1()));
        semifinales.add(crearPartido("SEMIFINAL 2", torneo.getSemifinal2()));
        columnas.add(semifinales);

        JPanel centro = new JPanel(new GridBagLayout());
        centro.setOpaque(false);
        centro.add(crearPartido("FINAL", torneo.getFinal()));
        columnas.add(centro);

        JPanel derecha = new JPanel(new GridBagLayout());
        derecha.setOpaque(false);
        derecha.add(crearCampeon(torneo));
        columnas.add(derecha);
        return columnas;
    }

    /**
     * Crea la tarjeta de un partido con sus dos participantes.
     *
     * @param titulo título del partido
     * @param partido partido que se muestra
     * @return tarjeta del partido
     */
    private JComponent crearPartido(String titulo, PartidoTorneo partido) {
        TarjetaPanel tarjeta = new TarjetaPanel();
        tarjeta.setLayout(new BorderLayout(0, 8));
        tarjeta.setBorder(new EmptyBorder(12, 14, 12, 14));
        tarjeta.setPreferredSize(new Dimension(300, 150));

        JLabel etiqueta = new JLabel(titulo, SwingConstants.CENTER);
        etiqueta.setFont(new Font("SansSerif", Font.BOLD, 16));
        etiqueta.setForeground(COLOR_TEXTO);
        tarjeta.add(etiqueta, BorderLayout.NORTH);

        boolean jugado = partido != null && partido.isJugado();
        ParticipanteTorneo ganador = jugado ? partido.getGanador() : null;
        ParticipanteTorneo primero = partido == null ? null : partido.getPrimero();
        ParticipanteTorneo segundo = partido == null ? null : partido.getSegundo();

        JPanel filas = new JPanel(new GridLayout(2, 1, 0, 4));
        filas.setOpaque(false);
        filas.add(crearParticipante(primero, ganador, jugado));
        filas.add(crearParticipante(segundo, ganador, jugado));
        tarjeta.add(filas, BorderLayout.CENTER);
        return tarjeta;
    }

    /**
     * Crea la fila de un participante con su bandera y su nombre.
     *
     * @param participante participante que se muestra
     * @param ganador ganador del partido, o null si aún no se jugó
     * @param jugado true si el partido ya se jugó
     * @return fila del participante
     */
    private JComponent crearParticipante(ParticipanteTorneo participante,
                                         ParticipanteTorneo ganador,
                                         boolean jugado) {
        JPanel fila = new JPanel(new BorderLayout(8, 0));
        fila.setOpaque(false);

        JLabel bandera = new JLabel();
        if (participante != null) {
            ImageIcon icono = iconoBandera(participante.getPersonaje());
            if (icono != null) {
                bandera.setIcon(icono);
            }
        }

        JLabel nombre = new JLabel(participante == null
                ? "Por definir" : participante.getNombre());
        nombre.setFont(new Font("SansSerif", Font.BOLD, 15));
        if (participante == null) {
            nombre.setForeground(COLOR_SUAVE);
        } else if (jugado && ganador != null && participante != ganador) {
            nombre.setForeground(COLOR_SUAVE);
        } else if (participante == ganador) {
            nombre.setForeground(COLOR_VERDE);
        } else {
            nombre.setForeground(COLOR_TEXTO);
        }

        fila.add(bandera, BorderLayout.WEST);
        fila.add(nombre, BorderLayout.CENTER);
        return fila;
    }

    /**
     * Crea la tarjeta del campeón del torneo.
     *
     * @param torneo torneo mostrado
     * @return tarjeta del campeón
     */
    private JComponent crearCampeon(Torneo torneo) {
        TarjetaPanel tarjeta = new TarjetaPanel(TarjetaPanel.Estilo.OSCURA);
        tarjeta.setLayout(new BorderLayout(0, 10));
        tarjeta.setBorder(new EmptyBorder(16, 18, 16, 18));
        tarjeta.setPreferredSize(new Dimension(300, 200));

        JLabel titulo = new JLabel("CAMPEÓN", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        titulo.setForeground(new Color(242, 205, 98));
        tarjeta.add(titulo, BorderLayout.NORTH);

        ParticipanteTorneo campeon = torneo.getCampeon();
        JPanel centro = new JPanel();
        centro.setOpaque(false);
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));

        JLabel bandera = new JLabel();
        bandera.setAlignmentX(Component.CENTER_ALIGNMENT);
        if (campeon != null) {
            ImageIcon icono = iconoBandera(campeon.getPersonaje());
            if (icono != null) {
                bandera.setIcon(icono);
            }
        }

        JLabel nombre = new JLabel(campeon == null
                ? "Por definir" : campeon.getNombre());
        nombre.setFont(new Font("SansSerif", Font.BOLD, 18));
        nombre.setForeground(campeon == null
                ? COLOR_SUAVE : Color.WHITE);
        nombre.setAlignmentX(Component.CENTER_ALIGNMENT);

        centro.add(Box.createVerticalGlue());
        centro.add(bandera);
        centro.add(Box.createVerticalStrut(8));
        centro.add(nombre);
        centro.add(Box.createVerticalGlue());
        tarjeta.add(centro, BorderLayout.CENTER);
        return tarjeta;
    }

    /**
     * Crea el icono escalado de la bandera de un personaje.
     *
     * @param personaje personaje del que se toma la bandera
     * @return icono de la bandera, o null si no existe
     */
    private ImageIcon iconoBandera(Personaje personaje) {
        BufferedImage imagen = bandera(personaje);
        if (imagen == null) {
            return null;
        }
        Image escalada = imagen.getScaledInstance(48, 32, Image.SCALE_SMOOTH);
        return new ImageIcon(escalada);
    }

    /**
     * Cabecera de la llave. Se dibuja fuera de la imagen para que el título
     * no quede encima de los recuadros de los participantes.
     *
     * @param torneo torneo mostrado
     * @return el panel de la cabecera
     */
    private JPanel crearCabecera(Torneo torneo) {
        JPanel cabecera = new JPanel(new BorderLayout());
        cabecera.setOpaque(false);
        cabecera.setBorder(new EmptyBorder(0, 18, 0, 18));

        JLabel titulo = new JLabel("LLAVE DEL TORNEO");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        titulo.setForeground(COLOR_TEXTO);

        JLabel puntos = new JLabel("PUNTOS: "
                + torneo.getPerfilHumano().getPuntos().getTotal());
        puntos.setFont(new Font("SansSerif", Font.BOLD, 19));
        puntos.setForeground(COLOR_TEXTO);

        cabecera.add(titulo, BorderLayout.WEST);
        cabecera.add(puntos, BorderLayout.EAST);
        return cabecera;
    }

    /**
     * Carga y devuelve la bandera del personaje.
     *
     * @param personaje personaje del que se quiere la bandera
     * @return bandera del personaje, o null si no existe
     */
    private BufferedImage bandera(Personaje personaje) {
        if (personaje == null) {
            return null;
        }
        CatalogoSeleccion.Opcion opcion = CatalogoSeleccion.buscar(personaje);
        if (opcion == null) {
            return null;
        }
        String ruta = opcion.getBandera();
        if (!banderas.containsKey(ruta)) {
            banderas.put(ruta, RecursosImagen.cargar(ruta));
        }
        return banderas.get(ruta);
    }

    /**
     * Actualiza el mensaje según el estado del torneo.
     *
     * @param torneo torneo consultado
     * @param siguiente siguiente partido del jugador humano
     */
    private void actualizarMensaje(Torneo torneo, PartidoTorneo siguiente) {
        if (torneo.isHumanoCampeon()) {
            actualizarMensaje("¡CAMPEÓN! Recibiste "
                    + ParametrosJuego.PREMIO_CAMPEON_TORNEO + " puntos.",
                    COLOR_ORO);
        } else if (torneo.isHumanoSubcampeon()) {
            actualizarMensaje("Subcampeón. Recibiste "
                    + ParametrosJuego.PREMIO_SUBCAMPEON_TORNEO + " puntos.",
                    COLOR_ORO);
        } else if (torneo.isHumanoEliminado()) {
            actualizarMensaje("Eliminado en semifinal. Puedes intentarlo de nuevo.",
                    COLOR_ROJO);
        } else if (siguiente != null && siguiente == torneo.getFinal()) {
            actualizarMensaje("¡Estás en la final! Gana para llevarte la copa.",
                    COLOR_VERDE);
        } else {
            actualizarMensaje("Gana tu semifinal para llegar a la final.",
                    COLOR_SUAVE);
        }
    }

    /**
     * Actualiza el texto y el color del mensaje.
     *
     * @param texto mensaje que se va a mostrar
     * @param color color del texto
     */
    private void actualizarMensaje(String texto, Color color) {
        mensaje.setText(texto);
        mensaje.setForeground(color);
    }

    /**
     * Devuelve el botón para inscribirse en el torneo.
     *
     * @return botón de inscripción
     */
    public JButton getBotonInscribirse() {
        return botonInscribirse;
    }

    /**
     * Devuelve el botón para jugar el siguiente partido.
     *
     * @return botón de juego
     */
    public JButton getBotonJugar() {
        return botonJugar;
    }

}
