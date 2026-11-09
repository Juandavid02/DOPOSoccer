package presentation;

import domain.Arco;
import domain.Balon;
import domain.Cancha;
import domain.Efecto;
import domain.Jugador;
import domain.ParametrosJuego;
import domain.Partido;
import domain.Poder;
import domain.ResultadoPartido;
import domain.TipoEfecto;
import domain.TipoPartido;
import domain.TiroPoder;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.util.EnumMap;
import java.util.Map;

public class JuegoView extends JPanel {

    private static final String RUTA_BOTONES = "resources/images/Botones.java/";

    private Partido partido;
    private BufferedImage escenario;
    private BufferedImage balon;
    private BufferedImage arcoIzquierdo;
    private BufferedImage arcoDerecho;
    private BufferedImage spriteJugador1;
    private BufferedImage spriteJugador2;
    private BufferedImage spriteJugador1Patada;
    private BufferedImage spriteJugador2Patada;
    private BufferedImage efectoAturdimiento;
    private BufferedImage auraJugador1;
    private BufferedImage auraJugador2;
    private BufferedImage tiroJugador1;
    private BufferedImage tiroJugador2;
    private final Map<TipoEfecto, BufferedImage> efectosPoder =
            new EnumMap<>(TipoEfecto.class);
    private long finPatadaJugador1;
    private long finPatadaJugador2;

    private final JButton botonAtras;
    private final BotonSonido botonSonido;
    private final JPanel barraSuperior;
    private final PanelPausa panelPausa;
    private final PanelResultado panelResultado;

    public JuegoView() {
        setOpaque(true);
        setBackground(Color.BLACK);
        setFocusable(true);
        setLayout(null);

        botonAtras = crearBotonImagen(RUTA_BOTONES + "Retroceder.png", 90);
        botonSonido = new BotonSonido(64);

        barraSuperior = new JPanel(new BorderLayout());
        barraSuperior.setOpaque(false);
        barraSuperior.setBorder(new EmptyBorder(16, 16, 0, 16));
        barraSuperior.add(botonAtras, BorderLayout.WEST);
        barraSuperior.add(botonSonido, BorderLayout.EAST);

        panelPausa = new PanelPausa();
        panelPausa.setVisible(false);

        panelResultado = new PanelResultado();
        panelResultado.setVisible(false);

        add(panelPausa);
        add(barraSuperior);
        add(panelResultado);
        setComponentZOrder(barraSuperior, 0);
        setComponentZOrder(panelResultado, 0);
    }

    @Override
    public void doLayout() {
        barraSuperior.setBounds(0, 0, getWidth(), 84);
        panelPausa.setBounds(0, 0, getWidth(), getHeight());
        panelResultado.setBounds(0, 0, getWidth(), getHeight());
    }

    public void iniciar(Partido partido, String rutaEscenario, String rutaBalon) {
        this.partido = partido;
        escenario = RecursosImagen.cargar(rutaEscenario);
        balon = recortar(RecursosImagen.cargar(rutaBalon));
        arcoIzquierdo = recortar(RecursosImagen.cargar(
                "resources/images/RedDeLaCancha/RedDelaCanchaIzquierda.png"));
        arcoDerecho = recortar(RecursosImagen.cargar(
                "resources/images/RedDeLaCancha/RedDelaCanchaDerecha.png"));
        efectoAturdimiento = recortar(RecursosImagen.cargar(
                "resources/images/Efectos/EfectoAturdimiento.png"));

        spriteJugador1 = cargarSprite(partido.getJugadores().get(0), true, false);
        spriteJugador2 = cargarSprite(partido.getJugadores().get(1), false, false);
        spriteJugador1Patada = cargarSprite(partido.getJugadores().get(0), true, true);
        spriteJugador2Patada = cargarSprite(partido.getJugadores().get(1), false, true);

        Poder poder1 = partido.getJugadores().get(0).getPersonaje().getPoder();
        Poder poder2 = partido.getJugadores().get(1).getPersonaje().getPoder();
        auraJugador1 = cargarOpcional(CatalogoPoderes.rutaAura(poder1));
        auraJugador2 = cargarOpcional(CatalogoPoderes.rutaAura(poder2));
        tiroJugador1 = cargarOpcional(CatalogoPoderes.rutaTiro(poder1));
        tiroJugador2 = cargarOpcional(CatalogoPoderes.rutaTiro(poder2));

        efectosPoder.clear();
        for (TipoEfecto tipo : TipoEfecto.values()) {
            BufferedImage imagen = cargarOpcional(CatalogoPoderes.rutaEfecto(tipo));
            if (imagen != null) {
                efectosPoder.put(tipo, recortar(imagen));
            }
        }

        finPatadaJugador1 = 0;
        finPatadaJugador2 = 0;

        panelPausa.setVisible(false);
        panelResultado.setVisible(false);
        revalidate();
        repaint();
    }

    public void setPausaVisible(boolean visible) {
        panelPausa.setVisible(visible);
        revalidate();
        repaint();
    }

    public void mostrarResultado(Partido partido) {
        panelPausa.setVisible(false);
        panelResultado.mostrar(partido);
        panelResultado.setVisible(true);
        revalidate();
        repaint();
    }

    public JButton getBotonResultadoRevancha() {
        return panelResultado.getBotonRevancha();
    }

    public JButton getBotonResultadoCambiarPersonaje() {
        return panelResultado.getBotonCambiarPersonaje();
    }

    public JButton getBotonResultadoMenu() {
        return panelResultado.getBotonMenu();
    }

    public void mostrarPatada(int jugador) {
        long fin = System.currentTimeMillis() + 150;
        if (jugador == 1) {
            finPatadaJugador1 = fin;
        } else {
            finPatadaJugador2 = fin;
        }
        repaint();
    }

    public JButton getBotonAtras() {
        return botonAtras;
    }

    public BotonSonido getBotonSonido() {
        return botonSonido;
    }

    public JButton getBotonPausaContinuar() {
        return panelPausa.getBotonContinuar();
    }

    public JButton getBotonPausaReiniciar() {
        return panelPausa.getBotonReiniciar();
    }

    public JButton getBotonPausaCambiarPersonaje() {
        return panelPausa.getBotonCambiarPersonaje();
    }

    public JButton getBotonPausaConfiguracion() {
        return panelPausa.getBotonConfiguracion();
    }

    public JButton getBotonPausaSalir() {
        return panelPausa.getBotonSalir();
    }

    private static JButton crearBotonImagen(String ruta, int ancho) {
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

    private static BufferedImage cargarOpcional(String ruta) {
        if (ruta == null) {
            return null;
        }
        try {
            return RecursosImagen.cargar(ruta);
        } catch (RuntimeException e) {
            System.err.println("No se pudo cargar la imagen de poder: " + ruta);
            return null;
        }
    }

    private BufferedImage cargarSprite(Jugador jugador, boolean jugador1, boolean patada) {
        CatalogoSeleccion.Opcion opcion = CatalogoSeleccion.buscar(jugador.getPersonaje());
        if (opcion == null) {
            return null;
        }

        String ruta;
        if (jugador1) {
            if (patada) {
                ruta = opcion.getPatadaIzquierda();
            } else {
                ruta = opcion.getIzquierda();
            }
        } else {
            if (patada) {
                ruta = opcion.getPatadaDerecha();
            } else {
                ruta = opcion.getDerecha();
            }
        }

        return recortar(RecursosImagen.cargar(ruta));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (partido == null) {
            return;
        }

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);

        Cancha cancha = partido.getCancha();

        // La cancha ocupa todo el ancho de la ventana; los arcos quedan
        // pegados a los márgenes.
        double escala = (double) getWidth() / cancha.getAncho();
        double anchoCampo = getWidth();
        double altoCampo = cancha.getAlto() * escala;
        double campoX = 0;
        // Sube un poco la cancha para que los pies queden sobre la línea
        // blanca del escenario.
        double campoY = (getHeight() - altoCampo) / 2 - getHeight() * 0.19;

        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, getWidth(), getHeight());

        // El escenario cubre toda la ventana; la cancha queda centrada dentro
        // del escenario y los arcos ocupan los márgenes laterales.
        double escalaFondo = Math.max(
                (double) getWidth() / escenario.getWidth(),
                (double) getHeight() / escenario.getHeight());
        int anchoFondo = (int) Math.ceil(escenario.getWidth() * escalaFondo);
        int altoFondo = (int) Math.ceil(escenario.getHeight() * escalaFondo);
        int xFondo = (getWidth() - anchoFondo) / 2;
        int yFondo = (getHeight() - altoFondo) / 2;
        g2.drawImage(escenario, xFondo, yFondo, anchoFondo, altoFondo, null);

        BufferedImage sprite1 = spriteJugador1;
        if (System.currentTimeMillis() < finPatadaJugador1 && spriteJugador1Patada != null) {
            sprite1 = spriteJugador1Patada;
        }

        BufferedImage sprite2 = spriteJugador2;
        if (System.currentTimeMillis() < finPatadaJugador2 && spriteJugador2Patada != null) {
            sprite2 = spriteJugador2Patada;
        }

        dibujarAura(g2, partido.getJugadores().get(0), auraJugador1,
                escala, campoX, campoY);
        dibujarAura(g2, partido.getJugadores().get(1), auraJugador2,
                escala, campoX, campoY);

        dibujarJugador(g2, partido.getJugadores().get(0), sprite1, escala, campoX, campoY);
        dibujarJugador(g2, partido.getJugadores().get(1), sprite2, escala, campoX, campoY);

        dibujarBalon(g2, partido.getBalon(), cancha, escala, campoX, campoY);
        dibujarTiroPoder(g2, cancha, escala, campoX, campoY);

        // Las redes se dibujan delante para que el personaje y el balón
        // queden detrás de la malla.
        dibujarArco(g2, cancha.getArcoIzquierdo(), arcoIzquierdo, escala, campoX, campoY, true);
        dibujarArco(g2, cancha.getArcoDerecho(), arcoDerecho, escala, campoX, campoY, false);

        dibujarHud(g2);

        g2.dispose();
    }

    private void dibujarAura(Graphics2D g2, Jugador jugador, BufferedImage aura,
                             double escala, double campoX, double campoY) {
        if (aura == null || !jugador.isAuraActiva()) {
            return;
        }

        double centroX = campoX + jugador.getPosicionX() * escala;
        double suelo = campoY
                + (jugador.getPosicionY() + ParametrosJuego.RADIO_JUGADOR) * escala;
        double altoJugador = ParametrosJuego.RADIO_JUGADOR * 6.0 * escala;

        double pulso = 1.0 + 0.08 * Math.sin(System.currentTimeMillis() / 150.0);
        double altoAura = altoJugador * 1.35 * pulso;
        double anchoAura = altoAura * aura.getWidth() / aura.getHeight();

        int x = (int) (centroX - anchoAura / 2);
        int y = (int) (suelo - altoAura * 0.92);

        Graphics2D gAura = (Graphics2D) g2.create();
        gAura.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        gAura.drawImage(aura, x, y, (int) anchoAura, (int) altoAura, null);
        gAura.dispose();
    }

    private void dibujarTiroPoder(Graphics2D g2, Cancha cancha,
                                  double escala, double campoX, double campoY) {
        TiroPoder tiro = partido.getTiroPoder();
        if (tiro == null || !tiro.isActivo()) {
            return;
        }

        BufferedImage imagen = tiro.getDueno() == partido.getJugadores().get(0)
                ? tiroJugador1 : tiroJugador2;
        if (imagen == null) {
            return;
        }

        double radioVisual = ParametrosJuego.RADIO_BALON_COLISION;
        double centroX = campoX + partido.getBalon().getX() * escala;
        double centroY = campoY
                + (partido.getBalon().getY()
                - (radioVisual - partido.getBalon().getRadio())) * escala;

        double pulso = 1.0 + 0.12 * Math.sin(System.currentTimeMillis() / 90.0);
        double diametro = radioVisual * 2 * escala * 1.8 * pulso;

        Graphics2D gTiro = (Graphics2D) g2.create();
        gTiro.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        gTiro.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        gTiro.rotate(Math.toRadians(12 * Math.sin(System.currentTimeMillis() / 250.0)),
                centroX, centroY);
        gTiro.drawImage(imagen,
                (int) (centroX - diametro / 2),
                (int) (centroY - diametro / 2),
                (int) diametro, (int) diametro, null);
        gTiro.dispose();
    }

    private void dibujarArco(Graphics2D g2, Arco arco, BufferedImage imagen,
                             double escala, double campoX, double campoY,
                             boolean izquierdo) {
        if (imagen == null) {
            return;
        }

        int x = (int) (campoX + arco.getX() * escala);
        int y = (int) (campoY + arco.getY() * escala);
        int ancho = (int) (arco.getAncho() * escala);
        int alto = (int) (arco.getAlto() * escala);

        dibujarAjustado(g2, imagen, x, y, ancho, alto, izquierdo);
    }

    private void dibujarJugador(Graphics2D g2, Jugador jugador, BufferedImage sprite,
                                double escala, double campoX, double campoY) {
        double centroX = campoX + jugador.getPosicionX() * escala;
        double suelo = campoY + (jugador.getPosicionY() + ParametrosJuego.RADIO_JUGADOR) * escala;

        if (sprite == null) {
            int radio = (int) (ParametrosJuego.RADIO_JUGADOR * escala);
            g2.setColor(Color.WHITE);
            g2.fillOval((int) centroX - radio, (int) suelo - 2 * radio,
                    radio * 2, radio * 2);
            return;
        }

        double alto = ParametrosJuego.RADIO_JUGADOR * 6.0 * escala;
        double ancho = alto * sprite.getWidth() / sprite.getHeight();
        int x = (int) (centroX - ancho / 2);
        int y = (int) (suelo - alto);

        g2.drawImage(sprite, x, y, (int) ancho, (int) alto, null);

        dibujarEfectoPoder(g2, jugador, centroX, y, ancho, alto);

        if (jugador.isAturdido() && efectoAturdimiento != null) {
            double anchoEfecto = ancho * 2.0;
            double altoEfecto = anchoEfecto * efectoAturdimiento.getHeight()
                    / efectoAturdimiento.getWidth();

            long tiempo = System.currentTimeMillis();
            int xEfecto = (int) (centroX - anchoEfecto / 2);
            int yEfecto = (int) (y - altoEfecto * 0.55
                    + Math.sin(tiempo / 180.0) * 3 * escala);

            Graphics2D gEfecto = (Graphics2D) g2.create();
            gEfecto.rotate(Math.toRadians(8 * Math.sin(tiempo / 220.0)),
                    centroX, yEfecto + altoEfecto / 2);
            gEfecto.drawImage(efectoAturdimiento, xEfecto, yEfecto,
                    (int) anchoEfecto, (int) altoEfecto, null);
            gEfecto.dispose();
        }
    }

    private void dibujarEfectoPoder(Graphics2D g2, Jugador jugador,
                                    double centroX, double y,
                                    double anchoJugador, double altoJugador) {
        Efecto efecto = jugador.getEfectoActivo();
        if (efecto == null) {
            return;
        }

        BufferedImage imagen = efectosPoder.get(efecto.getTipo());
        if (imagen == null) {
            return;
        }

        long tiempo = System.currentTimeMillis();
        double pulso = 1.0 + 0.07 * Math.sin(tiempo / 140.0);
        boolean barril = efecto.getTipo() == TipoEfecto.BARRIL;

        double anchoEfecto = anchoJugador * (barril ? 1.45 : 1.05) * pulso;
        double altoEfecto = anchoEfecto * imagen.getHeight() / imagen.getWidth();

        int x = (int) (centroX - anchoEfecto / 2);
        int yEfecto;
        if (barril) {
            yEfecto = (int) (y + altoJugador * 0.20
                    - altoEfecto * 0.50);
        } else {
            yEfecto = (int) (y - altoEfecto * 0.75
                    + Math.sin(tiempo / 180.0) * 4);
        }

        Graphics2D gEfecto = (Graphics2D) g2.create();
        gEfecto.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        gEfecto.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        if (barril) {
            gEfecto.rotate(Math.toRadians(5 * Math.sin(tiempo / 190.0)),
                    centroX, yEfecto + altoEfecto / 2);
        }
        gEfecto.drawImage(imagen, x, yEfecto,
                (int) anchoEfecto, (int) altoEfecto, null);
        gEfecto.dispose();
    }

    private void dibujarBalon(Graphics2D g2, Balon balon, Cancha cancha,
                              double escala, double campoX, double campoY) {
        if (this.balon == null) {
            return;
        }

        double radioVisual = ParametrosJuego.RADIO_BALON_COLISION;
        dibujarBalonesFalsos(g2, balon, cancha, escala, campoX, campoY);

        double centroX = campoX + balon.getX() * escala;
        double centroY = campoY
                + (balon.getY() - (radioVisual - balon.getRadio())) * escala;

        double minX = campoX + radioVisual * escala;
        double maxX = campoX + (cancha.getAncho() - radioVisual) * escala;
        double minY = campoY + radioVisual * escala;
        double maxY = campoY + (cancha.getSuelo() - radioVisual) * escala;

        centroX = Math.max(minX, Math.min(maxX, centroX));
        centroY = Math.max(minY, Math.min(maxY, centroY));

        int diametro = (int) (radioVisual * 2 * escala);

        g2.setColor(new Color(0, 0, 0, 70));
        g2.fillOval((int) centroX - diametro / 2,
                (int) centroY - diametro / 4,
                diametro, Math.max(4, diametro / 2));

        g2.drawImage(this.balon,
                (int) centroX - diametro / 2,
                (int) centroY - diametro / 2,
                diametro, diametro, null);
    }

    /**
     * Mientras algún jugador está confundido, dibuja siete balones falsos
     * alrededor del balón real.
     */
    private void dibujarBalonesFalsos(Graphics2D g2, Balon balon, Cancha cancha,
                                      double escala, double campoX, double campoY) {
        boolean confundido = false;
        for (Jugador jugador : partido.getJugadores()) {
            if (jugador.tieneEfecto(TipoEfecto.CONFUNDIDO)) {
                confundido = true;
                break;
            }
        }
        if (!confundido) {
            return;
        }

        double radioVisual = ParametrosJuego.RADIO_BALON_COLISION;
        double centroX = campoX + balon.getX() * escala;
        double centroY = campoY
                + (balon.getY() - (radioVisual - balon.getRadio())) * escala;
        double diametro = radioVisual * 2 * escala;

        double minX = campoX + radioVisual * escala;
        double maxX = campoX + (cancha.getAncho() - radioVisual) * escala;
        double minY = campoY + radioVisual * escala;
        double maxY = campoY + (cancha.getSuelo() - radioVisual) * escala;

        Graphics2D gFalso = (Graphics2D) g2.create();
        gFalso.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.55f));
        long tiempo = System.currentTimeMillis();

        for (int i = 0; i < 7; i++) {
            double angulo = Math.toRadians(i * 360.0 / 7.0)
                    + Math.toRadians(tiempo / 20.0);
            double radioOrbita = diametro * 1.4;
            int x = (int) (centroX + Math.cos(angulo) * radioOrbita);
            int y = (int) (centroY + Math.sin(angulo) * radioOrbita * 0.6);

            x = (int) Math.max(minX, Math.min(maxX, x));
            y = (int) Math.max(minY, Math.min(maxY, y));

            gFalso.drawImage(this.balon,
                    x - (int) (diametro / 2), y - (int) (diametro / 2),
                    (int) diametro, (int) diametro, null);
        }

        gFalso.dispose();
    }

    private void dibujarHud(Graphics2D g2) {
        int anchoPanel = Math.min(1240, Math.max(620, getWidth() - 260));
        int altoPanel = 112;
        int x = (getWidth() - anchoPanel) / 2;
        int y = 16;

        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRoundRect(x, y, anchoPanel, altoPanel, 24, 24);
        g2.setStroke(new BasicStroke(2f));
        g2.setColor(new Color(255, 255, 255, 130));
        g2.drawRoundRect(x, y, anchoPanel, altoPanel, 24, 24);

        Jugador jugador1 = partido.getJugadores().get(0);
        Jugador jugador2 = partido.getJugadores().get(1);
        int goles1 = partido.getMarcador().getGoles(1);
        int goles2 = partido.getMarcador().getGoles(2);

        int anchoCentro = 300;
        int anchoLateral = (anchoPanel - anchoCentro) / 2;
        int contenidoY = y + 12;

        // Separadores entre las zonas laterales y el marcador central.
        g2.setStroke(new BasicStroke(1f));
        g2.setColor(new Color(255, 255, 255, 45));
        g2.drawLine(x + anchoLateral, y + 14,
                x + anchoLateral, y + altoPanel - 14);
        g2.drawLine(x + anchoLateral + anchoCentro, y + 14,
                x + anchoLateral + anchoCentro, y + altoPanel - 14);

        dibujarBarrasJugador(g2, jugador1,
                x + 18, contenidoY, anchoLateral - 36, false);
        dibujarBarrasJugador(g2, jugador2,
                x + anchoPanel - anchoLateral + 18, contenidoY,
                anchoLateral - 36, true);

        g2.setFont(new Font("SansSerif", Font.BOLD, 36));
        g2.setColor(Color.WHITE);
        String marcador = goles1 + "  -  " + goles2;
        FontMetrics fmMarcador = g2.getFontMetrics();
        g2.drawString(marcador,
                x + (anchoPanel - fmMarcador.stringWidth(marcador)) / 2, y + 54);

        String tiempo = formatearTiempo();
        g2.setFont(new Font("SansSerif", Font.BOLD, 22));
        FontMetrics fmTiempo = g2.getFontMetrics();
        g2.drawString(tiempo,
                x + (anchoPanel - fmTiempo.stringWidth(tiempo)) / 2, y + 88);
    }

    private String formatearTiempo() {
        if (partido.getTipo() == TipoPartido.GOLES) {
            return "GOLES";
        }

        int segundos = (int) Math.ceil(partido.getTiempoRestante());
        int minutos = segundos / 60;
        int resto = segundos % 60;
        return String.format("%02d:%02d", minutos, resto);
    }

    private void dibujarBarrasJugador(Graphics2D g2, Jugador jugador,
                                      int x, int y, int ancho, boolean derecha) {
        int alto = 12;

        g2.setFont(new Font("SansSerif", Font.BOLD, 16));
        g2.setColor(Color.WHITE);
        String nombre = jugador.getNombre();
        FontMetrics fm = g2.getFontMetrics();
        int xTexto = derecha ? x + ancho - fm.stringWidth(nombre) : x;
        g2.drawString(nombre, xTexto, y + 18);

        double vida = jugador.getVida().getActual() / jugador.getVida().getMaxima();
        double poder = jugador.getBarraPoder().getCarga() / jugador.getBarraPoder().getCapacidad();

        dibujarBarra(g2, x, y + 26, ancho, alto, vida,
                new Color(70, 200, 100), derecha);

        int yPoder = y + 44;
        dibujarBarra(g2, x, yPoder, ancho, alto, poder,
                new Color(70, 150, 240), derecha);

        boolean barraLlena = jugador.getBarraPoder().isfull();
        if (barraLlena) {
            g2.setStroke(new BasicStroke(2f));
            g2.setColor(new Color(255, 220, 90));
            g2.drawRoundRect(x - 2, yPoder - 2, ancho + 4, alto + 4,
                    alto + 4, alto + 4);
        }

        Poder poderJugador = jugador.getPersonaje().getPoder();
        String textoPoder;
        if (jugador.isAuraActiva()) {
            textoPoder = "AURA: " + poderJugador.getNombre();
        } else if (barraLlena) {
            textoPoder = "PODER LISTO: " + poderJugador.getNombre();
        } else {
            textoPoder = "Poder: " + poderJugador.getNombre();
        }

        g2.setFont(new Font("SansSerif", Font.BOLD, 12));
        FontMetrics fmPoder = g2.getFontMetrics();
        int xPoderTexto = derecha
                ? x + ancho - fmPoder.stringWidth(textoPoder)
                : x;
        g2.setColor(CatalogoPoderes.colorPoder(poderJugador));
        g2.drawString(textoPoder, xPoderTexto, y + 74);

        String estado = null;
        Color colorEstado = new Color(255, 205, 80);
        if (jugador.isAturdido()) {
            estado = "ATURDIDO "
                    + (int) Math.ceil(jugador.getTiempoAturdimientoRestante()) + " s";
        } else if (jugador.getEfectoActivo() != null) {
            Efecto efecto = jugador.getEfectoActivo();
            estado = nombreEfecto(efecto.getTipo()) + " "
                    + String.format("%.1f", efecto.getTiempoRestante()) + " s";
            colorEstado = CatalogoPoderes.colorEfecto(efecto.getTipo());
        }

        if (estado != null) {
            g2.setFont(new Font("SansSerif", Font.BOLD, 12));
            FontMetrics fmEstado = g2.getFontMetrics();
            int xEstado = derecha
                    ? x + ancho - fmEstado.stringWidth(estado)
                    : x;
            g2.setColor(colorEstado);
            g2.drawString(estado, xEstado, y + 90);
        }
    }

    private static void dibujarBarra(Graphics2D g2, int x, int y,
                                     int ancho, int alto, double valor,
                                     Color color, boolean derecha) {
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRoundRect(x, y, ancho, alto, alto, alto);

        int anchoLleno = (int) Math.round(ancho * Math.max(0, Math.min(1, valor)));
        if (anchoLleno <= 0) {
            return;
        }

        int xLleno = derecha ? x + ancho - anchoLleno : x;
        g2.setColor(color);
        g2.fillRoundRect(xLleno, y, anchoLleno, alto, alto, alto);
    }

    private static String nombreEfecto(TipoEfecto tipo) {
        switch (tipo) {
            case EMPUJE:
                return "EMPUJADO";
            case ELECTROCUTADO:
                return "ELECTROCUTADO";
            case INCONSCIENTE:
                return "INCONSCIENTE";
            case CONFUNDIDO:
                return "CONFUNDIDO";
            case CONTROLES_INVERTIDOS:
                return "TECLAS INVERTIDAS";
            case BARRIL:
                return "EN BARRIL";
            default:
                return "";
        }
    }


    private static void dibujarAjustado(Graphics2D g2, BufferedImage imagen,
                                        int x, int y, int ancho, int alto,
                                        boolean alinearIzquierda) {
        double escala = Math.min(
                (double) ancho / imagen.getWidth(),
                (double) alto / imagen.getHeight());
        int anchoImagen = (int) (imagen.getWidth() * escala);
        int altoImagen = (int) (imagen.getHeight() * escala);
        int xImagen;
        if (alinearIzquierda) {
            xImagen = x;
        } else {
            xImagen = x + ancho - anchoImagen;
        }
        int yImagen = y + (alto - altoImagen) / 2;

        g2.drawImage(imagen, xImagen, yImagen, anchoImagen, altoImagen, null);
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

    // ------------------------------------------------------------------
    // Menú de pausa.
    // ------------------------------------------------------------------
    private static final class PanelPausa extends JPanel {

        private final JButton botonContinuar;
        private final JButton botonReiniciar;
        private final JButton botonCambiarPersonaje;
        private final JButton botonConfiguracion;
        private final JButton botonSalir;

        private PanelPausa() {
            setOpaque(false);
            setLayout(new GridBagLayout());

            botonContinuar = crearBotonImagen(
                    RUTA_BOTONES + "ReaunadarPartida.png", 280);
            botonReiniciar = crearBotonImagen(
                    RUTA_BOTONES + "ReiniciarPartido.png", 280);
            botonCambiarPersonaje = crearBotonImagen(
                    RUTA_BOTONES + "CambiarPersonajeEnPartido.png", 280);
            botonConfiguracion = crearBotonImagen(
                    RUTA_BOTONES + "ConfiguracionEnPartido.png", 280);
            botonSalir = crearBotonImagen(
                    RUTA_BOTONES + "MenuPrincipal.png", 280);

            JLabel titulo = new JLabel("PAUSA");
            titulo.setFont(new Font("SansSerif", Font.BOLD, 42));
            titulo.setForeground(Color.WHITE);
            titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel ayuda = new JLabel("ESC para continuar");
            ayuda.setFont(new Font("SansSerif", Font.BOLD, 16));
            ayuda.setForeground(new Color(220, 235, 245));
            ayuda.setAlignmentX(Component.CENTER_ALIGNMENT);

            JPanel menu = new PanelMenu();
            menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
            menu.setBorder(new EmptyBorder(22, 30, 18, 30));

            menu.add(titulo);
            menu.add(Box.createVerticalStrut(14));
            agregarBoton(menu, botonContinuar);
            agregarBoton(menu, botonReiniciar);
            agregarBoton(menu, botonCambiarPersonaje);
            agregarBoton(menu, botonConfiguracion);
            agregarBoton(menu, botonSalir);
            menu.add(Box.createVerticalStrut(6));
            menu.add(ayuda);

            GridBagConstraints c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 0;
            c.anchor = GridBagConstraints.NORTH;
            // Deja libre el marcador superior antes de empezar el menú.
            c.insets = new Insets(118, 0, 0, 0);
            add(menu, c);
        }

        private static void agregarBoton(JPanel menu, JButton boton) {
            boton.setAlignmentX(Component.CENTER_ALIGNMENT);
            menu.add(boton);
            menu.add(Box.createVerticalStrut(4));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(new Color(0, 0, 0, 185));
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.dispose();
        }

        private JButton getBotonContinuar() {
            return botonContinuar;
        }

        private JButton getBotonReiniciar() {
            return botonReiniciar;
        }

        private JButton getBotonCambiarPersonaje() {
            return botonCambiarPersonaje;
        }

        private JButton getBotonConfiguracion() {
            return botonConfiguracion;
        }

        private JButton getBotonSalir() {
            return botonSalir;
        }
    }

    private static final class PanelResultado extends JPanel {

        private final JButton botonRevancha;
        private final JButton botonCambiarPersonaje;
        private final JButton botonMenu;
        private final JLabel marcador;
        private final JLabel ganador;
        private final JLabel puntos;

        private PanelResultado() {
            setOpaque(false);
            setLayout(new GridBagLayout());

            botonRevancha = crearBotonImagen(
                    RUTA_BOTONES + "Revancha.png", 280);
            botonCambiarPersonaje = crearBotonImagen(
                    RUTA_BOTONES + "CambiarPersonajeEnPartido.png", 280);
            botonMenu = crearBotonImagen(
                    RUTA_BOTONES + "MenuPrincipal.png", 280);

            JLabel titulo = new JLabel("FIN DEL PARTIDO");
            titulo.setFont(new Font("SansSerif", Font.BOLD, 36));
            titulo.setForeground(Color.WHITE);
            titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

            marcador = new JLabel("", SwingConstants.CENTER);
            marcador.setFont(new Font("SansSerif", Font.BOLD, 28));
            marcador.setForeground(Color.WHITE);
            marcador.setAlignmentX(Component.CENTER_ALIGNMENT);

            ganador = new JLabel("", SwingConstants.CENTER);
            ganador.setFont(new Font("SansSerif", Font.BOLD, 22));
            ganador.setForeground(new Color(255, 220, 120));
            ganador.setAlignmentX(Component.CENTER_ALIGNMENT);

            puntos = new JLabel("", SwingConstants.CENTER);
            puntos.setFont(new Font("SansSerif", Font.BOLD, 17));
            puntos.setForeground(new Color(220, 235, 245));
            puntos.setAlignmentX(Component.CENTER_ALIGNMENT);

            JPanel menu = new PanelMenu();
            menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
            menu.setBorder(new EmptyBorder(22, 30, 20, 30));

            menu.add(titulo);
            menu.add(Box.createVerticalStrut(10));
            menu.add(marcador);
            menu.add(Box.createVerticalStrut(4));
            menu.add(ganador);
            menu.add(Box.createVerticalStrut(8));
            menu.add(puntos);
            menu.add(Box.createVerticalStrut(16));
            agregarBoton(menu, botonRevancha);
            agregarBoton(menu, botonCambiarPersonaje);
            agregarBoton(menu, botonMenu);

            GridBagConstraints c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 0;
            c.anchor = GridBagConstraints.CENTER;
            add(menu, c);
        }

        private static void agregarBoton(JPanel menu, JButton boton) {
            boton.setAlignmentX(Component.CENTER_ALIGNMENT);
            menu.add(boton);
            menu.add(Box.createVerticalStrut(4));
        }

        private void mostrar(Partido partido) {
            ResultadoPartido resultado = partido.getResultado();
            if (resultado == null) {
                marcador.setText("");
                ganador.setText("");
                puntos.setText("");
                return;
            }

            String nombre1 = partido.getJugadores().get(0).getNombre();
            String nombre2 = partido.getJugadores().get(1).getNombre();

            marcador.setText(nombre1 + "  " + resultado.getGolesJugador1()
                    + " - " + resultado.getGolesJugador2() + "  " + nombre2);

            int ganadorPartido = resultado.getGanador();
            if (ganadorPartido == 1) {
                ganador.setText("Ganó " + nombre1);
            } else if (ganadorPartido == 2) {
                ganador.setText("Ganó " + nombre2);
            } else {
                ganador.setText("Empate");
            }

            puntos.setText("<html><div style='text-align:center'>"
                    + nombre1 + ": +" + partido.getPuntosJugador1() + " puntos"
                    + "&nbsp;&nbsp;|&nbsp;&nbsp;"
                    + nombre2 + ": +" + partido.getPuntosJugador2() + " puntos"
                    + "</div></html>");
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(new Color(0, 0, 0, 200));
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.dispose();
        }

        private JButton getBotonRevancha() {
            return botonRevancha;
        }

        private JButton getBotonCambiarPersonaje() {
            return botonCambiarPersonaje;
        }

        private JButton getBotonMenu() {
            return botonMenu;
        }
    }

    private static final class PanelMenu extends JPanel {

        private PanelMenu() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            int ancho = getWidth();
            int alto = getHeight();
            int arco = 34;

            g2.setPaint(new GradientPaint(
                    0, 0, new Color(30, 70, 100, 235),
                    0, alto, new Color(10, 30, 50, 235)));
            g2.fillRoundRect(0, 0, ancho - 1, alto - 1, arco, arco);

            g2.setStroke(new BasicStroke(2f));
            g2.setColor(new Color(255, 255, 255, 140));
            g2.drawRoundRect(1, 1, ancho - 3, alto - 3, arco, arco);

            g2.dispose();
        }
    }
}
