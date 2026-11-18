package presentation;

import domain.Arco;
import domain.Balon;
import domain.BalonIlusion;
import domain.Cancha;
import domain.Efecto;
import domain.Jugador;
import domain.Modalidad;
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
import java.io.File;
import java.io.IOException;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class JuegoView extends JPanel {

    private static final String RUTA_BOTONES = "resources/images/Botones/";
    private static final String RUTA_MARCO_MARCADOR =
            "resources/images/Cancha/MarcoMarcador.png";
    private static final String RUTA_GOL =
            "resources/images/Cancha/gol.png";
    private static final String RUTA_FUENTE_DIGITAL =
            "resources/fuentes/ds_digital/DS-DIGI.TTF";
    private static final long DURACION_GOL_MS = 1400;

    // Zona de la imagen del marcador que se dibuja sobre la cancha.
    private static final int MARCO_Y = 178;
    private static final int MARCO_ANCHO = 1920;
    private static final int MARCO_ALTO = 278;

    // Coordenadas de cada elemento dentro de esa zona de la imagen.
    private static final int TIEMPO_X = 958;
    private static final int TIEMPO_Y = 214;
    private static final int GOL_IZQUIERDA_X = 800;
    private static final int GOL_DERECHA_X = 1099;
    private static final int GOL_Y = 297;
    private static final int NOMBRE_IZQUIERDA_CENTRO_X = 383;
    private static final int NOMBRE_DERECHA_CENTRO_X = 1534;
    private static final int NOMBRE_Y = 309;
    private static final int NOMBRE_ANCHO = 540;
    private static final int VIDA_IZQUIERDA_X = 158;
    private static final int VIDA_IZQUIERDA_ANCHO = 506;
    private static final int VIDA_DERECHA_X = 1396;
    private static final int VIDA_DERECHA_ANCHO = 506;
    private static final int VIDA_Y = 369;
    private static final int VIDA_ALTO = 32;
    private static final int PODER_IZQUIERDA_X = 154;
    private static final int PODER_IZQUIERDA_ANCHO = 511;
    private static final int PODER_DERECHA_X = 1390;
    private static final int PODER_DERECHA_ANCHO = 511;
    private static final int PODER_Y = 416;
    private static final int PODER_ALTO = 32;
    private static final int CIRCULO_IZQUIERDA_X = 81;
    private static final int CIRCULO_DERECHA_X = 1324;
    private static final int CIRCULO_Y = 428;
    private static final int CIRCULO_ANCHO = 132;
    private static final int CIRCULO_ALTO = 54;

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
    private BufferedImage emojiJugador1;
    private BufferedImage emojiJugador2;
    private BufferedImage marcoMarcador;
    private BufferedImage imagenGol;
    private long inicioGol;
    private long finGol;
    private final Font fuenteDigital;
    private final Map<TipoEfecto, BufferedImage> efectosPoder =
            new EnumMap<>(TipoEfecto.class);
    private long finPatadaJugador1;
    private long finPatadaJugador2;

    private final JButton botonAtras;
    private final BotonSonido botonSonido;
    private final JPanel barraSuperior;
    private final PanelPausa panelPausa;
    private final PanelResultado panelResultado;

    /**
     * Construye la vista del partido con sus paneles y botones.
     */
    public JuegoView() {
        setOpaque(true);
        setBackground(Color.BLACK);
        setFocusable(true);
        setLayout(null);

        fuenteDigital = cargarFuenteDigital();

        botonAtras = crearBotonImagen(RUTA_BOTONES + "Retroceder.png",
                VistaBase.ANCHO_BOTON_ATRAS);
        botonSonido = new BotonSonido(VistaBase.ANCHO_BOTON_SONIDO);

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

    /**
     * Ubica los paneles de la vista dentro de la ventana.
     */
    @Override
    public void doLayout() {
        barraSuperior.setBounds(0, 0, getWidth(), 84);
        panelPausa.setBounds(0, 0, getWidth(), getHeight());
        panelResultado.setBounds(0, 0, getWidth(), getHeight());
    }

    /**
     * Prepara la vista con el partido y las imágenes del escenario y el balón.
     *
     * @param partido partido que se va a mostrar
     * @param rutaEscenario ruta de la imagen del escenario
     * @param rutaBalon ruta de la imagen del balón
     */
    public void iniciar(Partido partido, String rutaEscenario, String rutaBalon) {
        this.partido = partido;
        escenario = RecursosImagen.cargar(rutaEscenario);
        balon = RecursosImagen.recortarTransparente(RecursosImagen.cargar(rutaBalon));
        arcoIzquierdo = RecursosImagen.recortarTransparente(RecursosImagen.cargar(
                "resources/images/Cancha/RedDelaCanchaIzquierda.png"));
        arcoDerecho = RecursosImagen.recortarTransparente(RecursosImagen.cargar(
                "resources/images/Cancha/RedDelaCanchaDerecha.png"));
        efectoAturdimiento = RecursosImagen.recortarTransparente(RecursosImagen.cargar(
                "resources/images/Efectos/EfectoAturdimiento.png"));

        BufferedImage marco = RecursosImagen.cargar(RUTA_MARCO_MARCADOR);
        if (MARCO_ANCHO > marco.getWidth()
                || MARCO_Y + MARCO_ALTO > marco.getHeight()) {
            throw new IllegalStateException(
                    "La imagen del marcador no tiene el tamaño esperado: "
                            + RUTA_MARCO_MARCADOR);
        }
        marcoMarcador = marco.getSubimage(0, MARCO_Y, MARCO_ANCHO, MARCO_ALTO);
        imagenGol = RecursosImagen.recortarTransparente(RecursosImagen.cargar(RUTA_GOL));

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
        emojiJugador1 = recortarOpcional(CatalogoPoderes.rutaEmoji(poder1));
        emojiJugador2 = recortarOpcional(CatalogoPoderes.rutaEmoji(poder2));

        efectosPoder.clear();
        for (TipoEfecto tipo : TipoEfecto.values()) {
            BufferedImage imagen = cargarOpcional(CatalogoPoderes.rutaEfecto(tipo));
            if (imagen != null) {
                efectosPoder.put(tipo, RecursosImagen.recortarTransparente(imagen));
            }
        }

        finPatadaJugador1 = 0;
        finPatadaJugador2 = 0;
        inicioGol = 0;
        finGol = 0;

        panelPausa.setVisible(false);
        panelResultado.setVisible(false);
        revalidate();
        repaint();
    }

    /**
     * Muestra u oculta el menú de pausa.
     *
     * @param visible true para mostrar el menú de pausa
     */
    public void setPausaVisible(boolean visible) {
        panelPausa.setVisible(visible);
        revalidate();
        repaint();
    }

    /**
     * Ajusta la interfaz cuando el partido pertenece a un torneo: no se
     * permite cambiar de personaje ni salir por el botón superior.
     *
     * @param modoTorneo true si es un partido de torneo
     */
    public void setModoTorneo(boolean modoTorneo) {
        panelPausa.setCambiarPersonajeVisible(!modoTorneo);
        panelPausa.setReiniciarVisible(!modoTorneo);
        botonAtras.setVisible(!modoTorneo);
        revalidate();
        repaint();
    }

    /**
     * Muestra el panel con el resultado final del partido.
     *
     * @param partido partido terminado
     * @param modoTorneo true si el partido pertenece a un torneo
     */
    public void mostrarResultado(Partido partido, boolean modoTorneo) {
        panelPausa.setVisible(false);
        panelResultado.mostrar(partido, modoTorneo);
        panelResultado.setVisible(true);
        revalidate();
        repaint();
    }

    /**
     * Devuelve el botón para continuar en el torneo.
     *
     * @return el botón del torneo
     */
    public JButton getBotonResultadoTorneo() {
        return panelResultado.getBotonTorneo();
    }

    /**
     * Devuelve el botón de revancha del resultado.
     *
     * @return el botón de revancha
     */
    public JButton getBotonResultadoRevancha() {
        return panelResultado.getBotonRevancha();
    }

    /**
     * Devuelve el botón para cambiar de personaje desde el resultado.
     *
     * @return el botón de cambiar personaje
     */
    public JButton getBotonResultadoCambiarPersonaje() {
        return panelResultado.getBotonCambiarPersonaje();
    }

    /**
     * Devuelve el botón para volver al menú principal desde el resultado.
     *
     * @return el botón de menú principal
     */
    public JButton getBotonResultadoMenu() {
        return panelResultado.getBotonMenu();
    }

    /**
     * Muestra la imagen de gol durante unos segundos.
     */
    public void mostrarGol() {
        inicioGol = System.currentTimeMillis();
        finGol = inicioGol + DURACION_GOL_MS;
        repaint();
    }

    /**
     * Indica si la imagen de gol sigue visible; mientras lo esté, el
     * partido debe quedar congelado.
     *
     * @return true si la imagen de gol sigue visible
     */
    public boolean isGolVisible() {
        return imagenGol != null && inicioGol > 0
                && System.currentTimeMillis() < finGol;
    }

    /**
     * Activa la animación de patada de un jugador.
     *
     * @param jugador número del jugador que patea (1 o 2)
     */
    public void mostrarPatada(int jugador) {
        long fin = System.currentTimeMillis() + 150;
        if (jugador == 1) {
            finPatadaJugador1 = fin;
        } else {
            finPatadaJugador2 = fin;
        }
        repaint();
    }

    /**
     * Devuelve el botón para volver atrás.
     *
     * @return el botón de retroceso
     */
    public JButton getBotonAtras() {
        return botonAtras;
    }

    /**
     * Devuelve el botón de sonido.
     *
     * @return el botón de sonido
     */
    public BotonSonido getBotonSonido() {
        return botonSonido;
    }

    /**
     * Devuelve el botón de continuar del menú de pausa.
     *
     * @return el botón de continuar
     */
    public JButton getBotonPausaContinuar() {
        return panelPausa.getBotonContinuar();
    }

    /**
     * Devuelve el botón de reiniciar del menú de pausa.
     *
     * @return el botón de reiniciar
     */
    public JButton getBotonPausaReiniciar() {
        return panelPausa.getBotonReiniciar();
    }

    /**
     * Devuelve el botón de cambiar personaje del menú de pausa.
     *
     * @return el botón de cambiar personaje
     */
    public JButton getBotonPausaCambiarPersonaje() {
        return panelPausa.getBotonCambiarPersonaje();
    }

    /**
     * Devuelve el botón de configuración del menú de pausa.
     *
     * @return el botón de configuración
     */
    public JButton getBotonPausaConfiguracion() {
        return panelPausa.getBotonConfiguracion();
    }

    /**
     * Devuelve el botón de salir del menú de pausa.
     *
     * @return el botón de salir
     */
    public JButton getBotonPausaSalir() {
        return panelPausa.getBotonSalir();
    }

    /**
     * Crea un botón de imagen a partir de una ruta.
     *
     * @param ruta ruta de la imagen del botón
     * @param ancho ancho normal del botón
     * @return el botón creado
     */
    private static JButton crearBotonImagen(String ruta, int ancho) {
        return BotonImagen.crear(ruta, ancho);
    }

    /**
     * Carga una imagen y devuelve null si falla.
     *
     * @param ruta ruta de la imagen
     * @return la imagen cargada o null
     */
    private static BufferedImage cargarOpcional(String ruta) {
        if (ruta == null) {
            return null;
        }
        try {
            return RecursosImagen.cargar(ruta);
        } catch (RuntimeException e) {
            System.err.println("No se pudo cargar la imagen de poder: " + ruta);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Carga una imagen y le quita el fondo transparente si es posible.
     *
     * @param ruta ruta de la imagen
     * @return la imagen recortada o null
     */
    private static BufferedImage recortarOpcional(String ruta) {
        BufferedImage imagen = cargarOpcional(ruta);
        if (imagen == null) {
            return null;
        }
        return RecursosImagen.recortarTransparente(imagen);
    }

    /**
     * Carga la fuente digital del marcador o una fuente por defecto.
     *
     * @return la fuente del marcador
     */
    private static Font cargarFuenteDigital() {
        try {
            return Font.createFont(Font.TRUETYPE_FONT, new File(RUTA_FUENTE_DIGITAL));
        } catch (FontFormatException | IOException e) {
            System.err.println("No se pudo cargar la fuente: " + RUTA_FUENTE_DIGITAL);
            e.printStackTrace();
            return new Font("SansSerif", Font.BOLD, 12);
        }
    }

    /**
     * Carga el sprite del jugador según su lado y si está pateando.
     *
     * @param jugador jugador del que se carga el sprite
     * @param jugador1 true si es el jugador de la izquierda
     * @param patada true para usar el sprite de patada
     * @return el sprite cargado o null
     */
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

        return RecursosImagen.recortarTransparente(RecursosImagen.cargar(ruta));
    }

    /**
     * Dibuja el estado actual de la vista.
     *
     * @param g contexto gráfico
     */
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

        dibujarGol(g2);

        dibujarHud(g2);

        g2.dispose();
    }

    /**
     * Dibuja el aura activa de un jugador.
     *
     * @param g2 contexto gráfico
     * @param jugador jugador dueño del aura
     * @param aura imagen del aura
     * @param escala escala de la cancha
     * @param campoX posición horizontal de la cancha
     * @param campoY posición vertical de la cancha
     */
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

    /**
     * Dibuja el disparo de poder cuando está activo.
     *
     * @param g2 contexto gráfico
     * @param cancha cancha del partido
     * @param escala escala de la cancha
     * @param campoX posición horizontal de la cancha
     * @param campoY posición vertical de la cancha
     */
    private void dibujarTiroPoder(Graphics2D g2, Cancha cancha,
                                  double escala, double campoX, double campoY) {
        TiroPoder tiro = partido.getTiroPoder();
        if (tiro == null || !tiro.isActivo()) {
            return;
        }

        BufferedImage imagen;
        if (tiro.getDueno() == partido.getJugadores().get(0)) {
            imagen = tiroJugador1;
        } else {
            imagen = tiroJugador2;
        }
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

    /**
     * Dibuja un arco con su red en la cancha.
     *
     * @param g2 contexto gráfico
     * @param arco arco que se dibuja
     * @param imagen imagen de la red del arco
     * @param escala escala de la cancha
     * @param campoX posición horizontal de la cancha
     * @param campoY posición vertical de la cancha
     * @param izquierdo true si el arco es el izquierdo
     */
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

    /**
     * Dibuja un jugador con su sprite y sus efectos.
     *
     * @param g2 contexto gráfico
     * @param jugador jugador que se dibuja
     * @param sprite imagen del jugador
     * @param escala escala de la cancha
     * @param campoX posición horizontal de la cancha
     * @param campoY posición vertical de la cancha
     */
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

    /**
     * Dibuja todos los efectos de poder activos de un jugador.
     *
     * @param g2 contexto gráfico
     * @param jugador jugador con los efectos
     * @param centroX centro horizontal del jugador
     * @param y posición vertical superior del jugador
     * @param anchoJugador ancho del jugador
     * @param altoJugador alto del jugador
     */
    private void dibujarEfectoPoder(Graphics2D g2, Jugador jugador,
                                    double centroX, double y,
                                    double anchoJugador, double altoJugador) {
        List<Efecto> efectos = jugador.getEfectosActivos();
        if (efectos.isEmpty()) {
            return;
        }

        for (int i = 0; i < efectos.size(); i++) {
            dibujarEfectoPoder(g2, efectos.get(i), i, efectos.size(),
                    centroX, y, anchoJugador, altoJugador);
        }
    }

    /**
     * Dibuja un efecto de poder concreto de un jugador.
     *
     * @param g2 contexto gráfico
     * @param efecto efecto que se dibuja
     * @param indice posición del efecto en la lista
     * @param total cantidad de efectos activos
     * @param centroX centro horizontal del jugador
     * @param y posición vertical superior del jugador
     * @param anchoJugador ancho del jugador
     * @param altoJugador alto del jugador
     */
    private void dibujarEfectoPoder(Graphics2D g2, Efecto efecto,
                                    int indice, int total,
                                    double centroX, double y,
                                    double anchoJugador, double altoJugador) {
        BufferedImage imagen = efectosPoder.get(efecto.getTipo());
        if (imagen == null) {
            return;
        }

        long tiempo = System.currentTimeMillis();
        double pulso = 1.0 + 0.07 * Math.sin(tiempo / 140.0 + indice);
        boolean barril = efecto.getTipo() == TipoEfecto.BARRIL;

        double factorAncho = 1.05;
        if (barril) {
            factorAncho = 1.45;
        }
        double anchoEfecto = anchoJugador * factorAncho * pulso;
        double altoEfecto = anchoEfecto * imagen.getHeight() / imagen.getWidth();

        // Los efectos se abren en abanico para que varios sean visibles a la vez.
        double separacion = anchoJugador * 0.30;
        double desplazamiento = (indice - (total - 1) / 2.0) * separacion;
        double centroEfectoX = centroX + desplazamiento;
        if (barril) {
            centroEfectoX = centroX;
        }

        int x = (int) (centroEfectoX - anchoEfecto / 2);
        int yEfecto;
        if (barril) {
            yEfecto = (int) (y + altoJugador * 0.20
                    - altoEfecto * 0.50);
        } else {
            yEfecto = (int) (y - altoEfecto * 0.75
                    + Math.sin(tiempo / 180.0 + indice) * 4);
        }

        Graphics2D gEfecto = (Graphics2D) g2.create();
        gEfecto.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        gEfecto.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        if (barril) {
            gEfecto.rotate(Math.toRadians(5 * Math.sin(tiempo / 190.0)),
                    centroEfectoX, yEfecto + altoEfecto / 2);
        } else if (total > 1) {
            gEfecto.rotate(Math.toRadians(6 * (indice - (total - 1) / 2.0)),
                    centroEfectoX, yEfecto + altoEfecto / 2);
        }
        gEfecto.drawImage(imagen, x, yEfecto,
                (int) anchoEfecto, (int) altoEfecto, null);
        gEfecto.dispose();
    }

    /**
     * Dibuja el balón con su sombra dentro de la cancha.
     *
     * @param g2 contexto gráfico
     * @param balon balón del partido
     * @param cancha cancha del partido
     * @param escala escala de la cancha
     * @param campoX posición horizontal de la cancha
     * @param campoY posición vertical de la cancha
     */
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
     *
     * @param g2 contexto gráfico
     * @param balon imagen del balón real
     * @param cancha cancha del partido
     * @param escala escala de dibujo del campo
     * @param campoX desplazamiento horizontal del campo
     * @param campoY desplazamiento vertical del campo
     */
    private void dibujarBalonesFalsos(Graphics2D g2, Balon balon, Cancha cancha,
                                      double escala, double campoX, double campoY) {
        List<BalonIlusion> ilusiones = partido.getBalonesIlusion();
        if (this.balon == null || ilusiones.isEmpty()) {
            return;
        }

        double radioVisual = ParametrosJuego.RADIO_BALON_COLISION;
        int diametro = (int) (radioVisual * 2 * escala);

        for (BalonIlusion ilusion : ilusiones) {
            int x = (int) (campoX + ilusion.getX() * escala);
            int y = (int) (campoY
                    + (ilusion.getY() - (radioVisual - balon.getRadio())) * escala);

            // Los balones falsos son idénticos al balón real, incluida su sombra.
            g2.setColor(new Color(0, 0, 0, 70));
            g2.fillOval(x - diametro / 2, y - diametro / 4,
                    diametro, Math.max(4, diametro / 2));

            g2.drawImage(this.balon,
                    x - diametro / 2, y - diametro / 2,
                    diametro, diametro, null);
        }
    }

    /**
     * Dibuja la imagen de gol con una pequeña animación de entrada y salida.
     *
     * @param g2 contexto gráfico
     */
    private void dibujarGol(Graphics2D g2) {
        long ahora = System.currentTimeMillis();
        if (imagenGol == null || inicioGol <= 0 || ahora >= finGol) {
            return;
        }

        long duracion = finGol - inicioGol;
        double progreso = (double) (ahora - inicioGol) / duracion;
        double aparicion = Math.min(1.0, progreso / 0.16);
        double escala = 0.55 + 0.45 * (1 - Math.pow(1 - aparicion, 3));
        double pulso = 1.0 + 0.035 * Math.sin((ahora - inicioGol) / 85.0);
        double alpha = 1;
        if (progreso > 0.82) {
            alpha = Math.max(0, (1 - progreso) / 0.18);
        }

        double anchoMaximo = getWidth() * 0.42;
        double escalaImagen = anchoMaximo / imagenGol.getWidth()
                * escala * pulso;
        int ancho = Math.max(1,
                (int) Math.round(imagenGol.getWidth() * escalaImagen));
        int alto = Math.max(1,
                (int) Math.round(imagenGol.getHeight() * escalaImagen));
        int x = (getWidth() - ancho) / 2;
        double centroY = getHeight() * 0.45;
        int y = (int) centroY - alto / 2;

        Graphics2D gGol = (Graphics2D) g2.create();
        gGol.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        gGol.setComposite(AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER,
                (float) Math.max(0, Math.min(1, alpha))));
        gGol.rotate(Math.toRadians(
                2.0 * Math.sin((ahora - inicioGol) / 120.0)),
                getWidth() / 2.0, centroY);
        gGol.drawImage(imagenGol, x, y, ancho, alto, null);
        gGol.dispose();
    }

    /**
     * Dibuja el marcador superior del partido.
     *
     * @param g2 contexto gráfico
     */
    private void dibujarHud(Graphics2D g2) {
        if (marcoMarcador == null) {
            return;
        }

        // El marcador ocupa la mayor parte del ancho sin llegar a los bordes.
        int anchoHud = Math.max(320, (int) Math.round(getWidth() * 0.78));
        int altoHud = (int) Math.round(
                anchoHud * (double) MARCO_ALTO / MARCO_ANCHO);
        if (altoHud > getHeight() * 0.40) {
            altoHud = (int) Math.round(getHeight() * 0.40);
            anchoHud = (int) Math.round(
                    altoHud * (double) MARCO_ANCHO / MARCO_ALTO);
        }

        int hudX = (getWidth() - anchoHud) / 2;
        int hudY = 0;
        double escala = (double) anchoHud / MARCO_ANCHO;

        g2.drawImage(marcoMarcador, hudX, hudY, anchoHud, altoHud, null);

        Jugador jugador1 = partido.getJugadores().get(0);
        Jugador jugador2 = partido.getJugadores().get(1);

        dibujarTiempo(g2, hudX, hudY, escala);
        dibujarGoles(g2, hudX, hudY, escala);
        dibujarNombre(g2, jugador1, hudX, hudY, escala, true);
        dibujarNombre(g2, jugador2, hudX, hudY, escala, false);
        dibujarBarrasJugador(g2, jugador1, hudX, hudY, escala, true);
        dibujarBarrasJugador(g2, jugador2, hudX, hudY, escala, false);
    }

    /**
     * Dibuja el tiempo restante o la etiqueta de goles en el marcador.
     *
     * @param g2 contexto gráfico
     * @param hudX posición horizontal del marcador
     * @param hudY posición vertical del marcador
     * @param escala escala del marcador
     */
    private void dibujarTiempo(Graphics2D g2, int hudX, int hudY, double escala) {
        String texto;
        Font fuente;
        if (partido.getTipo() == TipoPartido.GOLES) {
            texto = "GOLES";
            fuente = new Font("SansSerif", Font.BOLD,
                    Math.max(12, (int) Math.round(44 * escala)));
        } else {
            texto = formatearTiempo();
            fuente = fuenteDigital.deriveFont(Font.PLAIN,
                    Math.max(12f, (float) (54 * escala)));
        }

        dibujarTextoCentrado(g2, texto, fuente,
                xMarco(TIEMPO_X, hudX, escala),
                yMarco(TIEMPO_Y, hudY, escala),
                new Color(215, 240, 255));
    }

    /**
     * Dibuja los goles de cada jugador en el marcador.
     *
     * @param g2 contexto gráfico
     * @param hudX posición horizontal del marcador
     * @param hudY posición vertical del marcador
     * @param escala escala del marcador
     */
    private void dibujarGoles(Graphics2D g2, int hudX, int hudY, double escala) {
        Font fuente = fuenteDigital.deriveFont(Font.PLAIN,
                Math.max(16f, (float) (104 * escala)));
        int y = yMarco(GOL_Y, hudY, escala);

        dibujarTextoCentrado(g2, String.valueOf(partido.getMarcador().getGoles(1)),
                fuente, xMarco(GOL_IZQUIERDA_X, hudX, escala), y, Color.WHITE);
        dibujarTextoCentrado(g2, String.valueOf(partido.getMarcador().getGoles(2)),
                fuente, xMarco(GOL_DERECHA_X, hudX, escala), y, Color.WHITE);
    }

    /**
     * Dibuja el nombre del jugador en el marcador.
     *
     * @param g2 contexto gráfico
     * @param jugador jugador cuyo nombre se dibuja
     * @param hudX posición horizontal del marcador
     * @param hudY posición vertical del marcador
     * @param escala escala del marcador
     * @param izquierda true si el nombre va a la izquierda
     */
    private void dibujarNombre(Graphics2D g2, Jugador jugador, int hudX, int hudY,
                               double escala, boolean izquierda) {
        int nombreX;
        if (izquierda) {
            nombreX = NOMBRE_IZQUIERDA_CENTRO_X;
        } else {
            nombreX = NOMBRE_DERECHA_CENTRO_X;
        }
        int centroX = xMarco(nombreX, hudX, escala);
        int anchoMaximo = Math.max(30, (int) Math.round(NOMBRE_ANCHO * escala));
        int tamano = Math.max(12, (int) Math.round(46 * escala));

        Font fuente = new Font("SansSerif", Font.BOLD, tamano);
        TextoAjustado ajustado = ajustarTexto(g2, jugador.getNombre(), fuente,
                anchoMaximo, Math.max(10, tamano * 2 / 5));

        g2.setFont(ajustado.fuente);
        FontMetrics fm = g2.getFontMetrics();
        int lineaBase = yMarco(NOMBRE_Y, hudY, escala)
                + (fm.getAscent() - fm.getDescent()) / 2;
        int x = centroX - fm.stringWidth(ajustado.texto) / 2;

        g2.setColor(new Color(0, 0, 0, 160));
        g2.drawString(ajustado.texto, x + 1, lineaBase + 1);
        g2.setColor(Color.WHITE);
        g2.drawString(ajustado.texto, x, lineaBase);

        dibujarEstadoJugador(g2, jugador, hudX, hudY, escala, izquierda);
    }

    /**
     * Muestra los estados temporales (aturdido, efectos) en una etiqueta
     * debajo de las barras, sin mover el nombre centrado.
     *
     * @param g2 contexto gráfico
     * @param jugador jugador cuyo estado se muestra
     * @param hudX posición horizontal del marcador
     * @param hudY posición vertical del marcador
     * @param escala escala de dibujo
     * @param izquierda indica si el marcador está a la izquierda
     */
    private void dibujarEstadoJugador(Graphics2D g2, Jugador jugador,
                                      int hudX, int hudY, double escala,
                                      boolean izquierda) {
        String estado = estadoJugador(jugador);
        if (estado == null) {
            return;
        }

        Color colorEstado = new Color(255, 205, 80);
        List<Efecto> efectos = jugador.getEfectosActivos();
        if (!jugador.isAturdido() && !efectos.isEmpty()) {
            colorEstado = CatalogoPoderes.colorEfecto(efectos.get(0).getTipo());
        }

        int centroBarra;
        if (izquierda) {
            centroBarra = VIDA_IZQUIERDA_X + VIDA_IZQUIERDA_ANCHO / 2;
        } else {
            centroBarra = VIDA_DERECHA_X + VIDA_DERECHA_ANCHO / 2;
        }
        int centroX = xMarco(centroBarra, hudX, escala);
        int y = yMarco(PODER_Y + PODER_ALTO + 34, hudY, escala);
        int tamano = Math.max(11, (int) Math.round(30 * escala));

        Font fuente = new Font("SansSerif", Font.BOLD, tamano);
        TextoAjustado ajustado = ajustarTexto(g2, estado, fuente,
                Math.max(40, (int) Math.round(320 * escala)),
                Math.max(9, tamano / 2));

        g2.setFont(ajustado.fuente);
        FontMetrics fm = g2.getFontMetrics();
        int ancho = fm.stringWidth(ajustado.texto)
                + (int) Math.round(24 * escala);
        int alto = fm.getHeight() + (int) Math.round(8 * escala);
        int x = centroX - ancho / 2;
        int yCaja = y - alto / 2;

        Graphics2D gCaja = (Graphics2D) g2.create();
        gCaja.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        gCaja.setColor(new Color(0, 0, 0, 170));
        gCaja.fillRoundRect(x, yCaja, ancho, alto, alto, alto);
        gCaja.setStroke(new BasicStroke(
                Math.max(1f, (float) (2 * escala))));
        gCaja.setColor(new Color(colorEstado.getRed(), colorEstado.getGreen(),
                colorEstado.getBlue(), 220));
        gCaja.drawRoundRect(x, yCaja, ancho, alto, alto, alto);
        gCaja.setColor(colorEstado);
        gCaja.drawString(ajustado.texto,
                x + (ancho - fm.stringWidth(ajustado.texto)) / 2,
                yCaja + (alto - fm.getHeight()) / 2 + fm.getAscent());
        gCaja.dispose();
    }

    /**
     * Dibuja las barras de vida y poder de un jugador.
     *
     * @param g2 contexto gráfico
     * @param jugador jugador de las barras
     * @param hudX posición horizontal del marcador
     * @param hudY posición vertical del marcador
     * @param escala escala del marcador
     * @param izquierda true si el jugador está a la izquierda
     */
    private void dibujarBarrasJugador(Graphics2D g2, Jugador jugador,
                                      int hudX, int hudY, double escala,
                                      boolean izquierda) {
        int vidaX;
        int poderX;
        int vidaAncho;
        int poderAncho;
        if (izquierda) {
            vidaX = VIDA_IZQUIERDA_X;
            poderX = PODER_IZQUIERDA_X;
            vidaAncho = VIDA_IZQUIERDA_ANCHO;
            poderAncho = PODER_IZQUIERDA_ANCHO;
        } else {
            vidaX = VIDA_DERECHA_X;
            poderX = PODER_DERECHA_X;
            vidaAncho = VIDA_DERECHA_ANCHO;
            poderAncho = PODER_DERECHA_ANCHO;
        }
        int xVida = xMarco(vidaX, hudX, escala);
        int xPoder = xMarco(poderX, hudX, escala);
        int anchoVida = (int) Math.round(vidaAncho * escala);
        int anchoPoder = (int) Math.round(poderAncho * escala);
        int altoVida = (int) Math.round(VIDA_ALTO * escala);
        int altoPoder = (int) Math.round(PODER_ALTO * escala);

        double vida = jugador.getVida().getActual() / jugador.getVida().getMaxima();
        double poder = jugador.getBarraPoder().getCarga()
                / jugador.getBarraPoder().getCapacidad();

        dibujarRellenoBarra(g2, xVida, yMarco(VIDA_Y, hudY, escala),
                anchoVida, altoVida, vida, new Color(64, 205, 105));

        int yPoder = yMarco(PODER_Y, hudY, escala);
        dibujarRellenoBarra(g2, xPoder, yPoder,
                anchoPoder, altoPoder, poder, new Color(70, 155, 245));

        boolean barraLlena = jugador.getBarraPoder().isFull();
        if (barraLlena) {
            Graphics2D gGlow = (Graphics2D) g2.create();
            gGlow.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            float pulso = (float) (0.5 + 0.5 * Math.sin(
                    System.currentTimeMillis() / 180.0));
            gGlow.setStroke(new BasicStroke(
                    Math.max(1.5f, (float) (3 * escala))));
            gGlow.setColor(new Color(255, 220, 90, (int) (150 + 105 * pulso)));
            gGlow.drawRoundRect(xPoder - 2, yPoder - 2,
                    anchoPoder + 4, altoPoder + 4,
                    altoPoder + 4, altoPoder + 4);
            gGlow.dispose();
        }

        BufferedImage emoji;
        int circuloX;
        if (izquierda) {
            emoji = emojiJugador1;
            circuloX = CIRCULO_IZQUIERDA_X;
        } else {
            emoji = emojiJugador2;
            circuloX = CIRCULO_DERECHA_X;
        }
        dibujarEmoji(g2, emoji,
                xMarco(circuloX, hudX, escala),
                yMarco(CIRCULO_Y, hudY, escala),
                (int) Math.round(CIRCULO_ANCHO * escala),
                (int) Math.round(CIRCULO_ALTO * escala),
                barraLlena);
    }

    /**
     * Devuelve el tiempo restante con formato de minutos y segundos.
     *
     * @return el tiempo restante como texto
     */
    private String formatearTiempo() {
        int segundos = (int) Math.ceil(partido.getTiempoRestante());
        int minutos = segundos / 60;
        int resto = segundos % 60;
        return String.format("%02d:%02d", minutos, resto);
    }

    /**
     * Describe el estado temporal actual de un jugador.
     *
     * @param jugador jugador que se consulta
     * @return el texto del estado o null si no tiene ninguno
     */
    private static String estadoJugador(Jugador jugador) {
        StringBuilder estado = new StringBuilder();

        if (jugador.isAturdido()) {
            estado.append("ATURDIDO ")
                    .append((int) Math.ceil(jugador.getTiempoAturdimientoRestante()))
                    .append(" s");
        }

        for (Efecto efecto : jugador.getEfectosActivos()) {
            if (estado.length() > 0) {
                estado.append(" · ");
            }
            estado.append(nombreEfecto(efecto.getTipo()))
                    .append(' ')
                    .append(String.format("%.1f", efecto.getTiempoRestante()))
                    .append(" s");
        }

        if (estado.length() == 0) {
            return null;
        }
        return estado.toString();
    }

    /**
     * Rellena una barra con un color según su valor.
     *
     * @param g2 contexto gráfico
     * @param x posición horizontal de la barra
     * @param y posición vertical de la barra
     * @param ancho ancho de la barra
     * @param alto alto de la barra
     * @param valor cantidad que se debe rellenar
     * @param color color del relleno
     */
    private static void dibujarRellenoBarra(Graphics2D g2, int x, int y,
                                            int ancho, int alto, double valor,
                                            Color color) {
        double cantidad = Math.max(0, Math.min(1, valor));
        int anchoLleno = (int) Math.round(ancho * cantidad);
        if (anchoLleno <= 0 || alto <= 0) {
            return;
        }

        Graphics2D g = (Graphics2D) g2.create();
        g.setClip(new RoundRectangle2D.Double(x, y, ancho, alto, alto, alto));
        g.setPaint(new GradientPaint(x, y, color.brighter(),
                x, y + alto, color.darker()));
        g.fillRect(x, y, anchoLleno, alto);
        g.setColor(new Color(255, 255, 255, 70));
        g.fillRect(x, y + Math.max(1, alto / 8),
                anchoLleno, Math.max(1, alto / 3));
        g.dispose();
    }

    /**
     * Dibuja el emoji del personaje dentro del círculo del marcador.
     *
     * @param g2 contexto gráfico
     * @param emoji imagen del emoji
     * @param centroX centro horizontal del círculo
     * @param centroY centro vertical del círculo
     * @param anchoCirculo ancho del círculo
     * @param altoCirculo alto del círculo
     * @param lleno true si la barra de poder está llena
     */
    private static void dibujarEmoji(Graphics2D g2, BufferedImage emoji,
                                     int centroX, int centroY,
                                     int anchoCirculo, int altoCirculo,
                                     boolean lleno) {
        if (emoji == null || anchoCirculo <= 0 || altoCirculo <= 0) {
            return;
        }

        double pulso = 1.0;
        if (lleno) {
            pulso = 1.0 + 0.08 * Math.sin(
                    System.currentTimeMillis() / 160.0);
        }
        double escala = Math.min(
                anchoCirculo * 0.68 / emoji.getWidth(),
                altoCirculo * 0.80 / emoji.getHeight()) * pulso;
        int ancho = Math.max(1, (int) Math.round(emoji.getWidth() * escala));
        int alto = Math.max(1, (int) Math.round(emoji.getHeight() * escala));

        Graphics2D g = (Graphics2D) g2.create();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(emoji, centroX - ancho / 2, centroY - alto / 2,
                ancho, alto, null);
        g.dispose();
    }

    /**
     * Dibuja un texto centrado con una pequeña sombra.
     *
     * @param g2 contexto gráfico
     * @param texto texto que se dibuja
     * @param fuente fuente del texto
     * @param centroX centro horizontal del texto
     * @param centroY centro vertical del texto
     * @param color color del texto
     */
    private static void dibujarTextoCentrado(Graphics2D g2, String texto,
                                             Font fuente, int centroX,
                                             int centroY, Color color) {
        g2.setFont(fuente);
        FontMetrics fm = g2.getFontMetrics();
        int x = centroX - fm.stringWidth(texto) / 2;
        int y = centroY + (fm.getAscent() - fm.getDescent()) / 2;

        g2.setColor(new Color(0, 0, 0, 140));
        g2.drawString(texto, x + 1, y + 1);
        g2.setColor(color);
        g2.drawString(texto, x, y);
    }

    /**
     * Reduce la fuente hasta que el texto quepa y, si aun así no cabe,
     * lo recorta con puntos suspensivos para que nunca se salga del cuadro.
     *
     * @param g2 contexto gráfico
     * @param texto texto que se quiere ajustar
     * @param fuente fuente inicial
     * @param anchoMaximo ancho máximo disponible
     * @param tamanoMinimo tamaño mínimo permitido de la fuente
     * @return el texto ajustado con su fuente resultante
     */
    private static TextoAjustado ajustarTexto(Graphics2D g2, String texto,
                                              Font fuente, int anchoMaximo,
                                              int tamanoMinimo) {
        if (texto == null) {
            texto = "";
        }
        if (anchoMaximo <= 0) {
            return new TextoAjustado(texto, fuente);
        }

        Font resultado = fuente;
        FontMetrics fm = g2.getFontMetrics(resultado);
        while (fm.stringWidth(texto) > anchoMaximo
                && resultado.getSize() > tamanoMinimo) {
            resultado = resultado.deriveFont((float) (resultado.getSize() - 1));
            fm = g2.getFontMetrics(resultado);
        }

        String visible = texto;
        if (fm.stringWidth(visible) > anchoMaximo) {
            String puntos = "...";
            while (visible.length() > 1
                    && fm.stringWidth(visible + puntos) > anchoMaximo) {
                visible = visible.substring(0, visible.length() - 1);
            }
            visible = visible + puntos;
        }
        return new TextoAjustado(visible, resultado);
    }

    private static final class TextoAjustado {

        private final String texto;
        private final Font fuente;

        /**
         * Construye el texto ajustado con su fuente.
         *
         * @param texto texto ya ajustado
         * @param fuente fuente usada para el texto
         */
        private TextoAjustado(String texto, Font fuente) {
            this.texto = texto;
            this.fuente = fuente;
        }
    }

    /**
     * Convierte una coordenada horizontal del marcador a la pantalla.
     *
     * @param x coordenada dentro de la imagen del marco
     * @param hudX posición horizontal del marcador
     * @param escala escala del marcador
     * @return la coordenada horizontal en pantalla
     */
    private static int xMarco(int x, int hudX, double escala) {
        return hudX + (int) Math.round(x * escala);
    }

    /**
     * Convierte una coordenada vertical del marcador a la pantalla.
     *
     * @param y coordenada dentro de la imagen del marco
     * @param hudY posición vertical del marcador
     * @param escala escala del marcador
     * @return la coordenada vertical en pantalla
     */
    private static int yMarco(int y, int hudY, double escala) {
        return hudY + (int) Math.round((y - MARCO_Y) * escala);
    }

    /**
     * Devuelve el nombre visible de un tipo de efecto.
     *
     * @param tipo tipo de efecto
     * @return el nombre del efecto
     */
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


    /**
     * Dibuja una imagen ajustada dentro de un rectángulo.
     *
     * @param g2 contexto gráfico
     * @param imagen imagen que se dibuja
     * @param x posición horizontal del rectángulo
     * @param y posición vertical del rectángulo
     * @param ancho ancho del rectángulo
     * @param alto alto del rectángulo
     * @param alinearIzquierda true para alinear la imagen a la izquierda
     */
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

    // ------------------------------------------------------------------
    // Menú de pausa.
    // ------------------------------------------------------------------
    private static final class PanelPausa extends JPanel {

        private final JButton botonContinuar;
        private final JButton botonReiniciar;
        private final JButton botonCambiarPersonaje;
        private final JButton botonConfiguracion;
        private final JButton botonSalir;

        /**
         * Construye el menú de pausa con sus botones.
         */
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

        /**
         * Agrega un botón al menú de pausa con separación.
         *
         * @param menu panel del menú
         * @param boton botón que se agrega
         */
        private static void agregarBoton(JPanel menu, JButton boton) {
            boton.setAlignmentX(Component.CENTER_ALIGNMENT);
            menu.add(boton);
            menu.add(Box.createVerticalStrut(4));
        }

        /**
         * Dibuja el fondo oscuro del menú de pausa.
         *
         * @param g contexto gráfico
         */
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(new Color(0, 0, 0, 185));
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.dispose();
        }

        /**
         * Devuelve el botón de continuar.
         *
         * @return el botón de continuar
         */
        private JButton getBotonContinuar() {
            return botonContinuar;
        }

        /**
         * Devuelve el botón de reiniciar.
         *
         * @return el botón de reiniciar
         */
        private JButton getBotonReiniciar() {
            return botonReiniciar;
        }

        /**
         * Devuelve el botón de cambiar personaje.
         *
         * @return el botón de cambiar personaje
         */
        private JButton getBotonCambiarPersonaje() {
            return botonCambiarPersonaje;
        }

        /**
         * Muestra u oculta el botón de cambiar personaje.
         *
         * @param visible true para mostrar el botón
         */
        private void setCambiarPersonajeVisible(boolean visible) {
            botonCambiarPersonaje.setVisible(visible);
        }

        /**
         * Muestra u oculta el botón de reiniciar.
         *
         * @param visible true para mostrar el botón
         */
        private void setReiniciarVisible(boolean visible) {
            botonReiniciar.setVisible(visible);
            botonReiniciar.setEnabled(visible);
        }

        /**
         * Devuelve el botón de configuración.
         *
         * @return el botón de configuración
         */
        private JButton getBotonConfiguracion() {
            return botonConfiguracion;
        }

        /**
         * Devuelve el botón de salir.
         *
         * @return el botón de salir
         */
        private JButton getBotonSalir() {
            return botonSalir;
        }
    }

    private static final class PanelResultado extends JPanel {

        private static final int MAX_CARACTERES_NOMBRE = 22;

        private final JButton botonRevancha;
        private final JButton botonCambiarPersonaje;
        private final JButton botonTorneo;
        private final JButton botonMenu;
        private final JPanel marcador;
        private final JLabel marcadorNombre1;
        private final JLabel marcadorGoles;
        private final JLabel marcadorNombre2;
        private final JLabel ganador;
        private final JLabel puntos;

        /**
         * Construye el panel de resultado final del partido.
         */
        private PanelResultado() {
            setOpaque(false);
            setLayout(new GridBagLayout());

            botonRevancha = crearBotonImagen(
                    RUTA_BOTONES + "Revancha.png", 280);
            botonCambiarPersonaje = crearBotonImagen(
                    RUTA_BOTONES + "CambiarPersonajeEnPartido.png", 280);
            botonTorneo = crearBotonImagen(
                    RUTA_BOTONES + "ContinuarSeleccionPerfil.png", 280);
            botonTorneo.setVisible(false);
            botonMenu = crearBotonImagen(
                    RUTA_BOTONES + "MenuPrincipal.png", 280);

            JLabel titulo = new JLabel("FIN DEL PARTIDO");
            titulo.setFont(new Font("SansSerif", Font.BOLD, 36));
            titulo.setForeground(Color.WHITE);
            titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

            marcador = new JPanel(new GridBagLayout());
            marcador.setOpaque(false);
            marcador.setAlignmentX(Component.CENTER_ALIGNMENT);

            Font fuenteMarcador = new Font("SansSerif", Font.BOLD, 28);
            marcadorNombre1 = crearEtiquetaMarcador(
                    fuenteMarcador, SwingConstants.RIGHT);
            marcadorGoles = crearEtiquetaMarcador(
                    fuenteMarcador, SwingConstants.CENTER);
            marcadorNombre2 = crearEtiquetaMarcador(
                    fuenteMarcador, SwingConstants.LEFT);

            GridBagConstraints cm = new GridBagConstraints();
            cm.gridy = 0;
            cm.insets = new Insets(0, 12, 0, 12);
            cm.gridx = 0;
            cm.anchor = GridBagConstraints.EAST;
            marcador.add(marcadorNombre1, cm);

            cm.gridx = 1;
            cm.anchor = GridBagConstraints.CENTER;
            marcador.add(marcadorGoles, cm);

            cm.gridx = 2;
            cm.anchor = GridBagConstraints.WEST;
            marcador.add(marcadorNombre2, cm);

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
            agregarBoton(menu, botonTorneo);
            agregarBoton(menu, botonMenu);

            GridBagConstraints c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 0;
            c.anchor = GridBagConstraints.CENTER;
            add(menu, c);
        }

        /**
         * Crea una etiqueta para el marcador con su alineación.
         *
         * @param fuente fuente de la etiqueta
         * @param alineacion alineación horizontal del texto
         * @return la etiqueta creada
         */
        private static JLabel crearEtiquetaMarcador(Font fuente, int alineacion) {
            JLabel etiqueta = new JLabel("", alineacion);
            etiqueta.setFont(fuente);
            etiqueta.setForeground(Color.WHITE);
            etiqueta.setPreferredSize(new Dimension(120, 34));
            return etiqueta;
        }

        /**
         * Agrega un botón al menú de resultado con separación.
         *
         * @param menu panel del menú
         * @param boton botón que se agrega
         */
        private static void agregarBoton(JPanel menu, JButton boton) {
            boton.setAlignmentX(Component.CENTER_ALIGNMENT);
            menu.add(boton);
            menu.add(Box.createVerticalStrut(4));
        }

        /**
         * Rellena el panel con los datos del resultado del partido.
         *
         * @param partido partido terminado
         * @param modoTorneo true si el partido pertenece a un torneo
         */
        private void mostrar(Partido partido, boolean modoTorneo) {
            ResultadoPartido resultado = partido.getResultado();
            if (resultado == null) {
                marcadorNombre1.setText("");
                marcadorGoles.setText("");
                marcadorNombre2.setText("");
                ganador.setText("");
                puntos.setText("");
                return;
            }

            String nombre1 = limitarNombre(
                    partido.getJugadores().get(0).getNombre());
            String nombre2 = limitarNombre(
                    partido.getJugadores().get(1).getNombre());

            FontMetrics fm = marcadorGoles.getFontMetrics(
                    marcadorGoles.getFont());
            int anchoNombre1 = fm.stringWidth(nombre1);
            int anchoNombre2 = fm.stringWidth(nombre2);
            int anchoLado = Math.min(220,
                    Math.max(110, Math.max(anchoNombre1, anchoNombre2)));

            marcadorNombre1.setPreferredSize(new Dimension(anchoLado, 34));
            marcadorNombre2.setPreferredSize(new Dimension(anchoLado, 34));
            marcadorNombre1.setText(recortar(nombre1, fm, anchoLado));
            marcadorGoles.setText(resultado.getGolesJugador1()
                    + " - " + resultado.getGolesJugador2());
            marcadorNombre2.setText(recortar(nombre2, fm, anchoLado));
            marcador.revalidate();

            int ganadorPartido = resultado.getGanador();
            if (ganadorPartido == 1) {
                ganador.setText("Ganó " + nombre1);
            } else if (ganadorPartido == 2) {
                ganador.setText("Ganó " + nombre2);
            } else {
                ganador.setText("Empate");
            }

            if (modoTorneo) {
                puntos.setText("Partido de torneo · sin puntos por partido");
                botonRevancha.setVisible(false);
                botonCambiarPersonaje.setVisible(false);
                botonTorneo.setVisible(true);
                botonMenu.setVisible(false);
            } else {
                if (partido.getModalidad() == Modalidad.PvsM) {
                    puntos.setText(nombre1 + ": +"
                            + partido.getPuntosJugador1() + " puntos");
                } else {
                    puntos.setText("<html><div style='text-align:center'>"
                            + nombre1 + ": +" + partido.getPuntosJugador1() + " puntos"
                            + "&nbsp;&nbsp;|&nbsp;&nbsp;"
                            + nombre2 + ": +" + partido.getPuntosJugador2() + " puntos"
                            + "</div></html>");
                }
                botonRevancha.setVisible(true);
                botonCambiarPersonaje.setVisible(true);
                botonTorneo.setVisible(false);
                botonMenu.setVisible(true);
            }
            revalidate();
            repaint();
        }

        /**
         * Recorta un texto con puntos suspensivos si no cabe.
         *
         * @param texto texto original
         * @param fm métricas de la fuente
         * @param anchoMaximo ancho disponible
         * @return el texto recortado
         */
        private static String recortar(String texto, FontMetrics fm,
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

        /**
         * Limita el largo de un nombre con puntos suspensivos.
         *
         * @param nombre nombre original
         * @return el nombre limitado
         */
        private static String limitarNombre(String nombre) {
            if (nombre == null) {
                return "";
            }
            if (nombre.length() <= MAX_CARACTERES_NOMBRE) {
                return nombre;
            }
            return nombre.substring(0, MAX_CARACTERES_NOMBRE - 3) + "...";
        }

        /**
         * Dibuja el fondo oscuro del panel de resultado.
         *
         * @param g contexto gráfico
         */
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(new Color(0, 0, 0, 200));
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.dispose();
        }

        /**
         * Devuelve el botón de revancha.
         *
         * @return el botón de revancha
         */
        private JButton getBotonRevancha() {
            return botonRevancha;
        }

        /**
         * Devuelve el botón de torneo.
         *
         * @return el botón de torneo
         */
        private JButton getBotonTorneo() {
            return botonTorneo;
        }

        /**
         * Devuelve el botón de cambiar personaje del resultado.
         *
         * @return el botón de cambiar personaje
         */
        private JButton getBotonCambiarPersonaje() {
            return botonCambiarPersonaje;
        }

        /**
         * Devuelve el botón de menú principal.
         *
         * @return el botón de menú principal
         */
        private JButton getBotonMenu() {
            return botonMenu;
        }
    }

    private static final class PanelMenu extends JPanel {

        /**
         * Construye el panel del menú con fondo redondeado.
         */
        private PanelMenu() {
            setOpaque(false);
        }

        /**
         * Dibuja el fondo redondeado del menú.
         *
         * @param g contexto gráfico
         */
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
