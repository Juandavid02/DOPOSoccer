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
 * Pantalla del torneo. Primero muestra la inscripción y después la llave
 * sobre la imagen de llaves, con las banderas de los participantes dentro de
 * los recuadros blancos.
 */
public class TorneoView extends VistaBase {

    private static final String RUTA_FONDO =
            "resources/images/Ventanas/FondoPerfil.png";
    private static final String RUTA_LLAVES =
            "resources/images/Objetos/Llaves.png";
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
    private final BufferedImage llaves = RecursosImagen.cargar(RUTA_LLAVES);
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
            setOpaque(false);
            setLayout(new BorderLayout());
            setBorder(new EmptyBorder(5, 18, 5, 18));
        }

        /**
         * Dibuja la pastilla redondeada detrás del mensaje.
         *
         * @param g contexto gráfico del componente
         */
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(255, 255, 255, 210));
            int alto = getHeight();
            g2.fill(new RoundRectangle2D.Float(
                    0, 0, getWidth(), alto, alto, alto));
            g2.dispose();
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

        int puntos = perfil == null ? 0 : perfil.getPuntos().getTotal();
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
        JPanel raiz = new JPanel(new BorderLayout(0, 4));
        raiz.setOpaque(false);
        raiz.add(crearCabecera(torneo), BorderLayout.NORTH);
        raiz.add(new PanelLlave(torneo), BorderLayout.CENTER);
        return raiz;
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

    // ------------------------------------------------------------------
    // Llave dibujada sobre la imagen Llaves.png.
    // ------------------------------------------------------------------
    private final class PanelLlave extends JComponent {

        private static final int IMAGEN_ANCHO = 1024;
        private static final int IMAGEN_ALTO = 572;

        // Coordenadas de los recuadros blancos dentro de la imagen.
        private static final int[][] CAJAS_IZQUIERDA = {
                {143, 38, 148, 84},   // humano
                {143, 162, 148, 84},  // rival semifinal 1
                {143, 322, 148, 84},  // semifinal 2, participante 1
                {143, 447, 148, 84}   // semifinal 2, participante 2
        };
        private static final int[] CAJA_SEMIFINAL_1 = {350, 94, 146, 85};
        private static final int[] CAJA_SEMIFINAL_2 = {350, 389, 146, 85};
        private static final int[] CAJA_CAMPEON = {578, 218, 185, 102};

        /** Todas las banderas usan la misma proporción y el mismo tamaño. */
        private static final double ASPECTO_BANDERA = 1.5;
        private static final int ALTO_BANDERA_REFERENCIA = 50;

        private final Torneo torneo;

        /**
         * Crea el panel que dibuja la llave sobre la imagen.
         *
         * @param torneo torneo que se va a dibujar
         */
        private PanelLlave(Torneo torneo) {
            this.torneo = torneo;
            setOpaque(false);
            setPreferredSize(new Dimension(1120, 626));
        }

        /**
         * Dibuja la imagen de la llave y sus participantes.
         *
         * @param g contexto gráfico del componente
         */
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            double escala = Math.min(
                    (double) getWidth() / IMAGEN_ANCHO,
                    (double) getHeight() / IMAGEN_ALTO);
            int ancho = (int) Math.floor(IMAGEN_ANCHO * escala);
            int alto = (int) Math.floor(IMAGEN_ALTO * escala);
            Rectangle area = new Rectangle(
                    (getWidth() - ancho) / 2,
                    (getHeight() - alto) / 2,
                    ancho, alto);

            g2.drawImage(llaves, area.x, area.y, area.width, area.height, null);

            PartidoTorneo semifinal1 = torneo.getSemifinal1();
            PartidoTorneo semifinal2 = torneo.getSemifinal2();

            dibujarParticipante(g2, area, CAJAS_IZQUIERDA[0],
                    semifinal1.getPrimero(), false);
            dibujarEtiquetaJ1(g2, area, CAJAS_IZQUIERDA[0]);
            dibujarParticipante(g2, area, CAJAS_IZQUIERDA[1],
                    semifinal1.getSegundo(), false);
            dibujarParticipante(g2, area, CAJAS_IZQUIERDA[2],
                    semifinal2.getPrimero(), false);
            dibujarParticipante(g2, area, CAJAS_IZQUIERDA[3],
                    semifinal2.getSegundo(), false);

            dibujarGanador(g2, area, CAJA_SEMIFINAL_1, semifinal1);
            dibujarGanador(g2, area, CAJA_SEMIFINAL_2, semifinal2);

            dibujarParticipante(g2, area, CAJA_CAMPEON,
                    torneo.getCampeon(), true);

            g2.dispose();
        }

        /**
         * Dibuja al ganador del partido en su recuadro.
         *
         * @param g2 contexto gráfico
         * @param area área de la imagen en pantalla
         * @param caja coordenadas del recuadro
         * @param partido partido del que se toma el ganador
         */
        private void dibujarGanador(Graphics2D g2, Rectangle area, int[] caja,
                                    PartidoTorneo partido) {
            if (!partido.isJugado()) {
                return;
            }
            dibujarParticipante(g2, area, caja, partido.getGanador(), false);
        }

        /**
         * Dibuja la bandera del participante dentro del recuadro de la imagen.
         * El jugador humano usa fondo azul y su etiqueta "J1" va fuera del
         * recuadro. Las banderas de todos los participantes tienen el mismo
         * tamaño y la misma proporción.
         *
         * @param g2 contexto gráfico
         * @param area área de dibujo de la llave
         * @param caja coordenadas del recuadro
         * @param participante participante que se dibuja
         * @param campeon indica si el participante es el campeón
         */
        private void dibujarParticipante(Graphics2D g2, Rectangle area,
                                         int[] caja,
                                         ParticipanteTorneo participante,
                                         boolean campeon) {
            if (participante == null) {
                return;
            }

            Rectangle cajaPantalla = cajaEnPantalla(area, caja);
            if (participante.isHumano()) {
                pintarFondoHumano(g2, cajaPantalla);
            }

            BufferedImage imagen = bandera(participante.getPersonaje());
            String etiqueta = campeon ? "CAMPEÓN"
                    : participante.isHumano() ? null
                    : participante.getPerfilIA().name();

            int pad = Math.max(6, cajaPantalla.height / 10);
            int anchoInterior = Math.max(1, cajaPantalla.width - pad * 2);
            int separacion = Math.max(6, cajaPantalla.width / 25);

            int altoBandera = Math.max(1, (int) Math.round(
                    ALTO_BANDERA_REFERENCIA
                            * (double) area.height / IMAGEN_ALTO));
            int anchoBandera = Math.max(1,
                    (int) Math.round(altoBandera * ASPECTO_BANDERA));

            if (anchoBandera > anchoInterior) {
                anchoBandera = anchoInterior;
                altoBandera = Math.max(1,
                        (int) Math.round(anchoBandera / ASPECTO_BANDERA));
            }

            Font fuente = new Font("SansSerif", Font.BOLD,
                    Math.max(10, cajaPantalla.height / 8));
            FontMetrics fm = g2.getFontMetrics(fuente);
            int anchoTexto = etiqueta == null ? 0 : fm.stringWidth(etiqueta);

            boolean conTexto = etiqueta != null
                    && anchoBandera + separacion + anchoTexto <= anchoInterior;
            int anchoTotal = conTexto
                    ? anchoBandera + separacion + anchoTexto
                    : anchoBandera;
            int x = cajaPantalla.x + (cajaPantalla.width - anchoTotal) / 2;
            int yBandera = cajaPantalla.y
                    + (cajaPantalla.height - altoBandera) / 2;

            if (imagen != null) {
                dibujarBandera(g2, imagen, x, yBandera,
                        anchoBandera, altoBandera);
            }

            if (conTexto) {
                g2.setFont(fuente);
                g2.setColor(colorEtiqueta(campeon, participante));
                int yTexto = cajaPantalla.y
                        + (cajaPantalla.height + fm.getAscent()
                        - fm.getDescent()) / 2;
                g2.drawString(etiqueta,
                        x + anchoBandera + separacion, yTexto);
            } else if (imagen == null && etiqueta != null) {
                g2.setFont(fuente);
                g2.setColor(colorEtiqueta(campeon, participante));
                int yTexto = cajaPantalla.y
                        + (cajaPantalla.height + fm.getAscent()
                        - fm.getDescent()) / 2;
                g2.drawString(etiqueta,
                        cajaPantalla.x
                                + (cajaPantalla.width - anchoTexto) / 2,
                        yTexto);
            }
        }

        /**
         * Devuelve el color de la etiqueta del participante.
         *
         * @param campeon indica si el participante es el campeón
         * @param participante participante dibujado
         * @return color de la etiqueta
         */
        private Color colorEtiqueta(boolean campeon,
                                    ParticipanteTorneo participante) {
            if (!campeon) {
                return COLOR_TEXTO;
            }
            return participante.isHumano()
                    ? new Color(255, 238, 180) : COLOR_ORO;
        }

        /**
         * Pinta de azul el recuadro del jugador humano, como el marco de J1
         * en la selección de personajes.
         *
         * @param g2 contexto gráfico
         * @param caja recuadro que se pinta
         */
        private void pintarFondoHumano(Graphics2D g2, Rectangle caja) {
            int inset = Math.max(3, caja.height / 18);
            int radio = Math.max(6, caja.height / 6);
            RoundRectangle2D fondo = new RoundRectangle2D.Float(
                    caja.x + inset,
                    caja.y + inset,
                    Math.max(1, caja.width - inset * 2),
                    Math.max(1, caja.height - inset * 2),
                    radio, radio);

            g2.setPaint(new GradientPaint(
                    0, caja.y, COLOR_JUGADOR1.brighter(),
                    0, caja.y + caja.height, COLOR_JUGADOR1.darker()));
            g2.fill(fondo);

            g2.setStroke(new BasicStroke(Math.max(1f, caja.height / 40f)));
            g2.setColor(new Color(255, 255, 255, 165));
            g2.draw(fondo);
        }

        /**
         * Etiqueta "J1" fuera del recuadro del jugador, con el mismo estilo
         * azul que usa la selección de personajes.
         *
         * @param g2 contexto gráfico
         * @param area área de dibujo de la llave
         * @param caja coordenadas del recuadro
         */
        private void dibujarEtiquetaJ1(Graphics2D g2, Rectangle area, int[] caja) {
            Rectangle cajaPantalla = cajaEnPantalla(area, caja);
            int tamano = Math.max(30, Math.min(48,
                    (int) Math.round(cajaPantalla.height * 0.55)));
            int separacion = Math.max(6, cajaPantalla.height / 10);
            int x = cajaPantalla.x - tamano - separacion;
            int y = cajaPantalla.y + (cajaPantalla.height - tamano) / 2;
            int arco = Math.max(8, tamano / 4);

            g2.setColor(new Color(0, 0, 0, 80));
            g2.fillRoundRect(x + 2, y + 3, tamano, tamano, arco, arco);

            g2.setPaint(new GradientPaint(
                    0, y, COLOR_JUGADOR1.brighter(),
                    0, y + tamano, COLOR_JUGADOR1.darker()));
            g2.fillRoundRect(x, y, tamano, tamano, arco, arco);

            g2.setStroke(new BasicStroke(2f));
            g2.setColor(new Color(255, 255, 255, 210));
            g2.drawRoundRect(x, y, tamano, tamano, arco, arco);

            String texto = "J1";
            g2.setFont(new Font("SansSerif", Font.BOLD,
                    Math.max(13, tamano / 3)));
            FontMetrics fm = g2.getFontMetrics();
            int xTexto = x + (tamano - fm.stringWidth(texto)) / 2;
            int yTexto = y + (tamano - fm.getHeight()) / 2 + fm.getAscent();

            g2.setColor(new Color(0, 0, 0, 160));
            g2.drawString(texto, xTexto + 1, yTexto + 1);
            g2.setColor(Color.WHITE);
            g2.drawString(texto, xTexto, yTexto);
        }

        /**
         * Dibuja la bandera con esquinas redondeadas y un borde suave para
         * que no se confunda con el fondo blanco del recuadro.
         *
         * @param g2 contexto gráfico
         * @param imagen bandera que se dibuja
         * @param x posición horizontal
         * @param y posición vertical
         * @param ancho ancho de la bandera
         * @param alto alto de la bandera
         */
        private void dibujarBandera(Graphics2D g2, BufferedImage imagen,
                                    int x, int y, int ancho, int alto) {
            if (imagen == null || ancho <= 0 || alto <= 0) {
                return;
            }

            int radio = Math.max(4, Math.min(ancho, alto) / 8);
            RoundRectangle2D forma = new RoundRectangle2D.Float(
                    x, y, ancho, alto, radio, radio);

            int anchoOrigen = imagen.getWidth();
            int altoOrigen = imagen.getHeight();
            double aspectoOrigen = (double) anchoOrigen / altoOrigen;
            int sx = 0;
            int sy = 0;
            int sw = anchoOrigen;
            int sh = altoOrigen;

            if (aspectoOrigen > ASPECTO_BANDERA) {
                sw = Math.max(1,
                        (int) Math.round(altoOrigen * ASPECTO_BANDERA));
                sx = (anchoOrigen - sw) / 2;
            } else {
                sh = Math.max(1,
                        (int) Math.round(anchoOrigen / ASPECTO_BANDERA));
                sy = (altoOrigen - sh) / 2;
            }

            Shape clipAnterior = g2.getClip();
            g2.clip(forma);
            g2.drawImage(imagen, x, y, x + ancho, y + alto,
                    sx, sy, sx + sw, sy + sh, null);
            g2.setClip(clipAnterior);

            g2.setColor(new Color(35, 55, 75, 130));
            g2.setStroke(new BasicStroke(Math.max(1f, alto / 40f)));
            g2.draw(forma);
        }

        /**
         * Convierte una caja de la imagen a coordenadas en pantalla.
         *
         * @param area área de la imagen en pantalla
         * @param caja coordenadas de la caja en la imagen
         * @return caja escalada a la pantalla
         */
        private Rectangle cajaEnPantalla(Rectangle area, int[] caja) {
            double escalaX = (double) area.width / IMAGEN_ANCHO;
            double escalaY = (double) area.height / IMAGEN_ALTO;
            return new Rectangle(
                    area.x + (int) Math.round(caja[0] * escalaX),
                    area.y + (int) Math.round(caja[1] * escalaY),
                    Math.max(1, (int) Math.round(caja[2] * escalaX)),
                    Math.max(1, (int) Math.round(caja[3] * escalaY)));
        }
    }
}
