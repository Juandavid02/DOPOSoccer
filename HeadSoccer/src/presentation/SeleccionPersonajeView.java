package presentation;

import domain.Personaje;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class SeleccionPersonajeView extends JPanel {

    private static final String RUTA_BOTONES = "resources/images/Botones.java/";
    private static final String RUTA_PERSONAJES = "resources/images/personajes/";

    private final List<BotonCaja> botonesCaja = new ArrayList<>();
    private BufferedImage fondo;
    private PanelLateral panelJugador1;
    private PanelLateral panelJugador2;
    private PanelCentro panelCentro;
    private JButton botonAtras;
    private BotonSonido botonSonido;

    public SeleccionPersonajeView() {
        prepareView();
    }

    private void prepareView() {
        setLayout(new GridBagLayout());
        setOpaque(true);
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(1920, 1080));

        fondo = RecursosImagen.cargar("resources/images/Ventanas/FondoSelecionPersonaje.png");

        botonAtras = crearBotonImagen(RUTA_BOTONES + "Retroceder.png", 90);
        botonSonido = new BotonSonido(64);

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

        GridBagConstraints c = new GridBagConstraints();
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 1;

        c.gridx = 0;
        c.weightx = 0.26;
        add(panelJugador1, c);

        c.gridx = 1;
        c.weightx = 0.48;
        add(panelCentro, c);

        c.gridx = 2;
        c.weightx = 0.26;
        add(panelJugador2, c);
    }

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
        g2.dispose();
    }

    public void actualizar(Personaje personaje1, Personaje personaje2, int turno) {
        panelJugador1.mostrar(CatalogoSeleccion.buscar(personaje1));
        panelJugador2.mostrar(CatalogoSeleccion.buscar(personaje2));

        for (BotonCaja boton : botonesCaja) {
            actualizarBoton(boton, personaje1, personaje2);
        }

        boolean ambosListos = personaje1 != null && personaje2 != null;
        panelCentro.actualizarTurno(turno, ambosListos);
    }

    public void reiniciar() {
        actualizar(null, null, 1);
    }

    private void actualizarBoton(BotonCaja boton, Personaje personaje1, Personaje personaje2) {
        CatalogoSeleccion.Opcion opcion = boton.getOpcion();
        if (opcion == null) {
            boton.setSeleccionado(false);
            boton.setEnabled(false);
            return;
        }

        boolean esJugador1 = personaje1 != null && opcion.es(personaje1);
        boolean esJugador2 = personaje2 != null && opcion.es(personaje2);
        boolean elegido = esJugador1 || esJugador2;

        boton.setSeleccionado(elegido);
        boton.setEnabled(!elegido);
        boton.repaint();
    }

    public List<BotonCaja> getBotonesCaja() {
        return botonesCaja;
    }

    public JButton getBotonCambiar1() {
        return panelJugador1.getBotonCambiar();
    }

    public JButton getBotonCambiar2() {
        return panelJugador2.getBotonCambiar();
    }

    public JButton getBotonReady() {
        return panelCentro.getBotonReady();
    }

    public JButton getBotonAtras() {
        return botonAtras;
    }

    public BotonSonido getBotonSonido() {
        return botonSonido;
    }

    private JButton crearBotonImagen(String ruta, int ancho) {
        BufferedImage imagen = RecursosImagen.cargar(ruta);

        ImageIcon normal = crearIcono(imagen, ancho);
        ImageIcon encima = crearIcono(imagen, ancho + Math.max(4, ancho * 7 / 100));
        ImageIcon presionado = crearIcono(imagen, ancho + Math.max(2, ancho * 3 / 100));

        JButton boton = new JButton(normal);
        boton.setRolloverIcon(encima);
        boton.setPressedIcon(presionado);
        boton.setRolloverEnabled(true);
        boton.setPreferredSize(new Dimension(encima.getIconWidth(), encima.getIconHeight()));
        boton.setContentAreaFilled(false);
        boton.setBorderPainted(false);
        boton.setFocusPainted(false);
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return boton;
    }

    private static ImageIcon crearIcono(BufferedImage imagen, int ancho) {
        int alto = ancho * imagen.getHeight() / imagen.getWidth();
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
        private final JPanel barraSuperior;

        private BufferedImage bandera;

        private PanelLateral(boolean jugador1, String rutaBase) {
            this.jugador1 = jugador1;
            this.base = RecursosImagen.cargar(rutaBase);

            setOpaque(true);
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
            panelTitulo.setPreferredSize(new Dimension(0, 160));
            panelTitulo.add(titulo);

            JPanel contenido = new JPanel(new BorderLayout());
            contenido.setOpaque(false);
            contenido.add(panelTitulo, BorderLayout.NORTH);
            contenido.add(vista, BorderLayout.CENTER);

            botonCambiar = crearBotonCambiar();
            botonCambiar.setVisible(false);

            JPanel info = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
            info.setOpaque(false);
            info.setPreferredSize(new Dimension(0, 68));
            info.add(botonCambiar);

            add(barraSuperior, BorderLayout.NORTH);
            add(contenido, BorderLayout.CENTER);
            add(info, BorderLayout.SOUTH);
        }

        private static JButton crearBotonCambiar() {
            BufferedImage imagen = RecursosImagen.cargar(RUTA_BOTONES + "CambiarPerfil.png");
            int ancho = 200;
            ImageIcon normal = crearIcono(imagen, ancho);
            ImageIcon encima = crearIcono(imagen, ancho + 10);
            ImageIcon presionado = crearIcono(imagen, ancho + 5);

            JButton boton = new JButton(normal);
            boton.setRolloverIcon(encima);
            boton.setPressedIcon(presionado);
            boton.setRolloverEnabled(true);
            boton.setPreferredSize(new Dimension(encima.getIconWidth(), encima.getIconHeight()));
            boton.setContentAreaFilled(false);
            boton.setBorderPainted(false);
            boton.setFocusPainted(false);
            boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            return boton;
        }

        private void mostrar(CatalogoSeleccion.Opcion opcion) {
            if (opcion == null) {
                bandera = null;
                vista.setSprite(null);
                titulo.setIcon(null);
                titulo.setVisible(false);
                botonCambiar.setVisible(false);
                repaint();
                return;
            }

            bandera = RecursosImagen.cargar(opcion.getBandera());
            String rutaSprite;
            if (jugador1) {
                rutaSprite = opcion.getIzquierda();
            } else {
                rutaSprite = opcion.getDerecha();
            }

            vista.setSprite(RecursosImagen.cargar(rutaSprite));
            ImageIcon iconoTitulo = crearIcono(RecursosImagen.cargar(opcion.getTitulo()), 210);
            titulo.setIcon(iconoTitulo);
            titulo.setVisible(true);
            botonCambiar.setVisible(true);
            revalidate();
            repaint();
        }

        private JPanel getBarraSuperior() {
            return barraSuperior;
        }

        private JButton getBotonCambiar() {
            return botonCambiar;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (bandera == null) {
                return;
            }

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            double escala = Math.max(
                    (double) getWidth() / bandera.getWidth(),
                    (double) getHeight() / bandera.getHeight());
            int ancho = (int) Math.ceil(bandera.getWidth() * escala);
            int alto = (int) Math.ceil(bandera.getHeight() * escala);

            g2.drawImage(bandera, (getWidth() - ancho) / 2, (getHeight() - alto) / 2,
                    ancho, alto, null);
            g2.dispose();
        }
    }

    // ------------------------------------------------------------------
    // Dibuja el personaje sobre su base.
    // ------------------------------------------------------------------
    private static final class PanelVistaPersonaje extends JPanel {

        private final BufferedImage base;
        private BufferedImage sprite;

        private PanelVistaPersonaje(BufferedImage base) {
            this.base = recortar(base);
            setOpaque(false);
        }

        private void setSprite(BufferedImage sprite) {
            if (sprite == null) {
                this.sprite = null;
            } else {
                this.sprite = recortar(sprite);
            }
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            int ancho = getWidth();
            int alto = getHeight();

            int anchoBase = (int) (ancho * 0.86);
            int altoBase = anchoBase * base.getHeight() / base.getWidth();
            int xBase = (ancho - anchoBase) / 2;
            int yBase = alto - altoBase - 2;

            if (sprite == null) {
                g2.setFont(new Font("SansSerif", Font.BOLD, Math.max(40, alto / 3)));
                FontMetrics fm = g2.getFontMetrics();
                String signo = "?";
                int x = (ancho - fm.stringWidth(signo)) / 2;
                int y = yBase - fm.getDescent() - 20;
                g2.setColor(new Color(0, 0, 0, 120));
                g2.drawString(signo, x + 2, y + 2);
                g2.setColor(new Color(255, 255, 255, 200));
                g2.drawString(signo, x, y);
            } else {
                int maxAncho = (int) (ancho * 0.58);
                int maxAlto = (int) (yBase + altoBase * 0.30);

                double escala = Math.min(
                        (double) maxAncho / sprite.getWidth(),
                        (double) maxAlto / sprite.getHeight());
                int anchoSprite = (int) (sprite.getWidth() * escala);
                int altoSprite = (int) (sprite.getHeight() * escala);
                int xSprite = (ancho - anchoSprite) / 2;
                int ySprite = yBase + (int) (altoBase * 0.14) - altoSprite;

                g2.drawImage(sprite, xSprite, ySprite, anchoSprite, altoSprite, null);
            }

            g2.drawImage(base, xBase, yBase, anchoBase, altoBase, null);
            g2.dispose();
        }

        private static BufferedImage recortar(BufferedImage imagen) {
            int minX = imagen.getWidth();
            int minY = imagen.getHeight();
            int maxX = -1;
            int maxY = -1;

            for (int y = 0; y < imagen.getHeight(); y++) {
                for (int x = 0; x < imagen.getWidth(); x++) {
                    int alpha = (imagen.getRGB(x, y) >>> 24) & 0xff;
                    if (alpha > 10) {
                        if (x < minX) {
                            minX = x;
                        }
                        if (x > maxX) {
                            maxX = x;
                        }
                        if (y < minY) {
                            minY = y;
                        }
                        if (y > maxY) {
                            maxY = y;
                        }
                    }
                }
            }

            if (maxX < minX || maxY < minY) {
                return imagen;
            }

            return imagen.getSubimage(minX, minY, maxX - minX + 1, maxY - minY + 1);
        }
    }

    // ------------------------------------------------------------------
    // Panel central verde con el título, la grilla y READY.
    // ------------------------------------------------------------------
    private static final class PanelCentro extends JPanel {

        private final JLabel turno;
        private final JButton botonReady;
        private final List<BotonCaja> botones = new ArrayList<>();

        private PanelCentro() {
            setOpaque(false);
            setLayout(new GridBagLayout());

            PanelTitulo titulo = new PanelTitulo("SELECCIONA TU PERSONAJE");

            turno = new JLabel("ELIGE JUGADOR 1", SwingConstants.CENTER);
            turno.setFont(new Font("SansSerif", Font.BOLD, 19));
            turno.setForeground(Color.WHITE);
            turno.setOpaque(true);
            turno.setBackground(new Color(0, 0, 0, 140));
            turno.setBorder(new EmptyBorder(4, 18, 4, 18));

            JPanel cuadricula = new JPanel(new GridLayout(3, 3, 8, 8));
            cuadricula.setOpaque(false);
            cuadricula.setBorder(new EmptyBorder(10, 10, 10, 10));

            List<CatalogoSeleccion.Opcion> opciones = CatalogoSeleccion.getOpciones();
            for (CatalogoSeleccion.Opcion opcion : opciones) {
                BotonCaja boton = new BotonCaja(opcion);
                botones.add(boton);
                cuadricula.add(boton);
            }
            for (int i = opciones.size(); i < 9; i++) {
                BotonCaja boton = new BotonCaja(null);
                botones.add(boton);
                cuadricula.add(boton);
            }

            PanelCuadricula contenedorCuadricula = new PanelCuadricula();
            contenedorCuadricula.setLayout(new BorderLayout());
            contenedorCuadricula.setPreferredSize(new Dimension(500, 500));
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

        private static JButton crearBotonReady() {
            BufferedImage imagen = RecursosImagen.cargar(RUTA_BOTONES + "ReadyPerfil.png");
            int ancho = 300;
            ImageIcon normal = crearIcono(imagen, ancho);
            ImageIcon encima = crearIcono(imagen, ancho + 14);
            ImageIcon presionado = crearIcono(imagen, ancho + 7);

            JButton boton = new JButton(normal);
            boton.setRolloverIcon(encima);
            boton.setPressedIcon(presionado);
            boton.setRolloverEnabled(true);
            boton.setPreferredSize(new Dimension(encima.getIconWidth(), encima.getIconHeight()));
            boton.setContentAreaFilled(false);
            boton.setBorderPainted(false);
            boton.setFocusPainted(false);
            boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            return boton;
        }

        private void actualizarTurno(int turnoActual, boolean ambosListos) {
            if (ambosListos) {
                turno.setText("¡LISTOS!");
            } else if (turnoActual == 2) {
                turno.setText("ELIGE JUGADOR 2");
            } else {
                turno.setText("ELIGE JUGADOR 1");
            }

            botonReady.setVisible(ambosListos);
            revalidate();
            repaint();
        }

        private JButton getBotonReady() {
            return botonReady;
        }

        private List<BotonCaja> getBotones() {
            return botones;
        }
    }

    // ------------------------------------------------------------------
    // Celda de la grilla 3x3.
    // ------------------------------------------------------------------
    public static final class BotonCaja extends JButton {

        private final CatalogoSeleccion.Opcion opcion;
        private BufferedImage imagen;
        private boolean seleccionado;

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

        public CatalogoSeleccion.Opcion getOpcion() {
            return opcion;
        }

        private void setSeleccionado(boolean seleccionado) {
            this.seleccionado = seleccionado;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            int ancho = getWidth();
            int alto = getHeight();
            int arco = 16;
            RoundRectangle2D forma = new RoundRectangle2D.Float(
                    0, 0, ancho - 1, alto - 1, arco, arco);

            if (opcion == null) {
                g2.setColor(new Color(240, 246, 240));
                g2.fill(forma);

                g2.setColor(new Color(150, 170, 155));
                g2.setStroke(new BasicStroke(2f));
                g2.draw(forma);

                g2.setFont(new Font("SansSerif", Font.BOLD, Math.max(28, alto / 3)));
                g2.setColor(new Color(120, 140, 125));
                FontMetrics fm = g2.getFontMetrics();
                String signo = "?";
                int x = (ancho - fm.stringWidth(signo)) / 2;
                int y = (alto + fm.getAscent() - fm.getDescent()) / 2;
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

            if (getModel().isRollover() && isEnabled()) {
                g2.setColor(new Color(255, 255, 255, 40));
                g2.fill(forma);
            }

            g2.setClip(null);

            if (!isEnabled()) {
                g2.setColor(new Color(20, 35, 45, 130));
                g2.fill(forma);
            }

            if (seleccionado) {
                g2.setStroke(new BasicStroke(5f));
                g2.setColor(new Color(255, 210, 70));
            } else {
                g2.setStroke(new BasicStroke(2f));
                g2.setColor(new Color(30, 70, 90));
            }
            g2.draw(forma);

            g2.dispose();
        }
    }

    // ------------------------------------------------------------------
    // Componentes auxiliares de dibujo.
    // ------------------------------------------------------------------
    private static final class PanelTitulo extends JComponent {

        private final String texto;
        private final Font fuente = new Font("SansSerif", Font.BOLD, 24);

        private PanelTitulo(String texto) {
            this.texto = texto;
        }

        @Override
        public Dimension getPreferredSize() {
            FontMetrics fm = getFontMetrics(fuente);
            return new Dimension(fm.stringWidth(texto) + 70, 58);
        }

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

    private static final class PanelCuadricula extends JPanel {

        private PanelCuadricula() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(new Color(230, 242, 233));
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 24, 24);

            g2.setStroke(new BasicStroke(2f));
            g2.setColor(new Color(90, 150, 110));
            g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 24, 24);

            g2.dispose();
        }
    }
}
