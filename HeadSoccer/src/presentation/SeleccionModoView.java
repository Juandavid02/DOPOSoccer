package presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Pantalla del modo Arcade. Usa el mismo fondo del menú y el panel
 * "Seleccionar modo" con los botones de PvP y PvM dentro.
 */
public class SeleccionModoView extends VistaBase {

    private static final String RUTA_BOTONES = "resources/images/Botones/";
    private static final String RUTA_PANEL_MODO =
            "resources/images/Ventanas/SeleccionarModoDejuego.png";
    private static final String RUTA_FUENTE_DESCRIPCION =
            "resources/fuentes/fredoka-one/FredokaOne-Regular.ttf";

    private static final Font FUENTE_DESCRIPCION =
            cargarFuenteDescripcion();

    private final BufferedImage imagenModo;
    private final BufferedImage imagenPvsP;
    private final BufferedImage imagenPvsM;

    private final JButton botonPvsP;
    private final JButton botonPvsM;

    /**
     * Crea la vista del modo Arcade con sus botones de selección.
     */
    public SeleccionModoView() {
        super("resources/images/Ventanas/FondoMenu.jpg");

        imagenModo = RecursosImagen.cargar(RUTA_PANEL_MODO);
        imagenPvsP = RecursosImagen.cargar(RUTA_BOTONES + "PvsP.png");
        imagenPvsM = RecursosImagen.cargar(RUTA_BOTONES + "PvsM.png");

        botonPvsP = BotonImagen.crear(imagenPvsP, 420);
        botonPvsM = BotonImagen.crear(imagenPvsM, 420);

        setCentroDirecto(new PanelImagenModo(imagenModo, imagenPvsP, imagenPvsM,
                botonPvsP, botonPvsM));
    }

    /**
     * Carga la fuente usada en las descripciones o usa una por defecto.
     *
     * @return fuente cargada para las descripciones
     */
    private static Font cargarFuenteDescripcion() {
        try {
            return Font.createFont(Font.TRUETYPE_FONT,
                    new File(RUTA_FUENTE_DESCRIPCION));
        } catch (FontFormatException | IOException e) {
            System.err.println("No se pudo cargar la fuente: "
                    + RUTA_FUENTE_DESCRIPCION);
            e.printStackTrace();
            return new Font("SansSerif", Font.PLAIN, 16);
        }
    }

    /**
     * Devuelve el botón del modo jugador contra jugador.
     *
     * @return botón de PvsP
     */
    public JButton getBotonPvsP() {
        return botonPvsP;
    }

    /**
     * Devuelve el botón del modo jugador contra máquina.
     *
     * @return botón de PvsM
     */
    public JButton getBotonPvsM() {
        return botonPvsM;
    }

    /**
     * Texto descriptivo debajo de cada botón. Se ajusta automáticamente en
     * varias líneas para que la letra pueda ser grande.
     */
    private static final class EtiquetaModo extends JComponent {

        private final String texto;

        /**
         * Crea la etiqueta con el texto descriptivo del modo.
         *
         * @param texto texto que se va a dibujar
         */
        private EtiquetaModo(String texto) {
            this.texto = texto;
            setOpaque(false);
        }

        /**
         * Dibuja el texto descriptivo ajustado en varias líneas.
         *
         * @param g contexto gráfico del componente
         */
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setFont(getFont());
            FontMetrics fm = g2.getFontMetrics();
            List<String> lineas = envolver(texto, fm, getWidth() - 8);
            int altoLinea = fm.getHeight();
            // Ambas descripciones empiezan a la misma altura, justo debajo
            // de su botón.
            int y = fm.getAscent() + 2;

            for (String linea : lineas) {
                int x = (getWidth() - fm.stringWidth(linea)) / 2;

                // Sombra de 2 px detrás de cada línea.
                g2.setColor(new Color(0, 0, 0, 185));
                g2.drawString(linea, x + 2, y + 2);
                g2.setColor(new Color(0, 0, 0, 110));
                g2.drawString(linea, x + 1, y + 1);
                g2.setColor(Color.WHITE);
                g2.drawString(linea, x, y);
                y += altoLinea;
            }

            g2.dispose();
        }

        /**
         * Divide el texto en líneas que caben en el ancho indicado.
         *
         * @param texto texto que se va a dividir
         * @param fm métricas de la fuente usada
         * @param anchoMaximo ancho máximo disponible
         * @return lista con las líneas del texto
         */
        private static List<String> envolver(String texto, FontMetrics fm,
                                             int anchoMaximo) {
            List<String> lineas = new ArrayList<>();
            String[] palabras = texto.split(" ");
            StringBuilder linea = new StringBuilder();

            for (String palabra : palabras) {
                String prueba = linea.length() == 0
                        ? palabra : linea + " " + palabra;
                if (fm.stringWidth(prueba) <= anchoMaximo
                        || linea.length() == 0) {
                    linea.setLength(0);
                    linea.append(prueba);
                } else {
                    lineas.add(linea.toString());
                    linea.setLength(0);
                    linea.append(palabra);
                }
            }

            if (linea.length() > 0) {
                lineas.add(linea.toString());
            }
            return lineas;
        }
    }

    /**
     * Panel con la imagen de "Seleccionar modo". La imagen se escala al
     * mayor tamaño posible y los botones se colocan dentro de su área verde,
     * escalándose también con el panel.
     */
    private static final class PanelImagenModo extends JPanel {

        private final BufferedImage imagen;
        private final BufferedImage imagenPvsP;
        private final BufferedImage imagenPvsM;
        private final JButton botonPvsP;
        private final JButton botonPvsM;
        private final EtiquetaModo descripcionPvsP;
        private final EtiquetaModo descripcionPvsM;
        private Rectangle areaImagen;

        /**
         * Crea el panel con la imagen del modo y sus botones.
         *
         * @param imagen imagen del panel de selección
         * @param imagenPvsP imagen del botón PvsP
         * @param imagenPvsM imagen del botón PvsM
         * @param botonPvsP botón del modo PvsP
         * @param botonPvsM botón del modo PvsM
         */
        private PanelImagenModo(BufferedImage imagen, BufferedImage imagenPvsP,
                                BufferedImage imagenPvsM, JButton botonPvsP,
                                JButton botonPvsM) {
            this.imagen = imagen;
            this.imagenPvsP = imagenPvsP;
            this.imagenPvsM = imagenPvsM;
            this.botonPvsP = botonPvsP;
            this.botonPvsM = botonPvsM;

            descripcionPvsP = new EtiquetaModo(
                    "Reta a un amigo en el mismo teclado");
            descripcionPvsM = new EtiquetaModo(
                    "Enfréntate a la máquina y demuestra quién manda");

            setOpaque(false);
            setLayout(null);
            add(botonPvsP);
            add(botonPvsM);
            add(descripcionPvsP);
            add(descripcionPvsM);
        }

        /**
         * Coloca los botones y las descripciones dentro de la imagen.
         */
        @Override
        public void doLayout() {
            areaImagen = calcularAreaImagen();
            if (areaImagen == null) {
                return;
            }

            int anchoBoton = (int) (areaImagen.width * 0.38);
            int altoBoton = anchoBoton * imagenPvsP.getHeight()
                    / imagenPvsP.getWidth();
            int separacion = (int) (areaImagen.width * 0.035);
            int total = anchoBoton * 2 + separacion;
            int x = areaImagen.x + (areaImagen.width - total) / 2;
            int y = areaImagen.y + (int) (areaImagen.height * 0.52)
                    - altoBoton / 2;

            escalarBoton(botonPvsP, imagenPvsP, anchoBoton);
            escalarBoton(botonPvsM, imagenPvsM, anchoBoton);
            botonPvsP.setBounds(x, y, anchoBoton, altoBoton);
            botonPvsM.setBounds(x + anchoBoton + separacion, y,
                    anchoBoton, altoBoton);

            int tamanoTexto = Math.max(18, anchoBoton / 11);
            int altoTexto = Math.max(120, (int) (areaImagen.height * 0.22));
            int yTexto = y + altoBoton + 5;
            int anchoTexto = anchoBoton;
            int xTextoPvsP = x;
            int xTextoPvsM = x + anchoBoton + separacion;

            Font fuenteTexto = FUENTE_DESCRIPCION.deriveFont(
                    Font.PLAIN, (float) tamanoTexto);
            descripcionPvsP.setFont(fuenteTexto);
            descripcionPvsM.setFont(fuenteTexto);
            descripcionPvsP.setBounds(xTextoPvsP, yTexto,
                    anchoTexto, altoTexto);
            descripcionPvsM.setBounds(xTextoPvsM, yTexto,
                    anchoTexto, altoTexto);
        }

        /**
         * Ajusta el tamaño de los iconos del botón al ancho indicado.
         *
         * @param boton botón que se va a escalar
         * @param imagen imagen del botón
         * @param ancho ancho deseado para el botón
         */
        private static void escalarBoton(JButton boton, BufferedImage imagen,
                                         int ancho) {
            boton.setIcon(BotonImagen.icono(imagen, ancho));
            boton.setRolloverIcon(BotonImagen.icono(imagen,
                    ancho + Math.max(3, ancho * 5 / 100)));
            boton.setPressedIcon(BotonImagen.icono(imagen,
                    ancho + Math.max(2, ancho * 2 / 100)));
        }

        /**
         * Calcula el área donde se dibuja la imagen del panel.
         *
         * @return área de la imagen en pantalla, o null si no hay espacio
         */
        private Rectangle calcularAreaImagen() {
            if (getWidth() <= 0 || getHeight() <= 0) {
                return null;
            }

            double escala = Math.min(
                    (double) getWidth() / imagen.getWidth(),
                    (double) getHeight() / imagen.getHeight());
            int ancho = (int) Math.ceil(imagen.getWidth() * escala);
            int alto = (int) Math.ceil(imagen.getHeight() * escala);
            return new Rectangle((getWidth() - ancho) / 2,
                    (getHeight() - alto) / 2, ancho, alto);
        }

        /**
         * Dibuja la imagen del panel de selección de modo.
         *
         * @param g contexto gráfico del componente
         */
        @Override
        protected void paintComponent(Graphics g) {
            Rectangle area = calcularAreaImagen();
            if (area == null) {
                return;
            }

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                    RenderingHints.VALUE_RENDER_QUALITY);
            g2.drawImage(imagen, area.x, area.y, area.width, area.height, null);
            g2.dispose();
        }
    }
}
