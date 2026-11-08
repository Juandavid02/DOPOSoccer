package presentation;

import domain.ConfiguracionPartido;
import domain.EstadoPartido;
import domain.ParametrosJuego;
import domain.Partido;
import domain.PerfilJugador;
import domain.Personaje;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

public class JuegoController {

    private static final int INTERVALO = 16;
    private static final double VELOCIDAD = 260;

    private final DOPOSoccerGUI gui;
    private final JuegoView view;
    private final Set<Integer> teclas = new HashSet<>();

    private ConfiguracionPartido configuracion;
    private Personaje personaje1;
    private Personaje personaje2;
    private PerfilJugador perfil1;
    private PerfilJugador perfil2;
    private String rutaEscenario;
    private String rutaBalon;

    private Partido partido;
    private Timer timer;
    private long ultimoTiempo;
    private KeyAdapter escuchaTeclas;

    public JuegoController(DOPOSoccerGUI gui, JuegoView view) {
        this.gui = gui;
        this.view = view;
        prepareController();
    }

    private void prepareController() {
        view.getBotonAtras().addActionListener(e -> volverASeleccionVisual());
        view.getBotonSonido().addActionListener(e -> gui.alternarSonido());

        view.getBotonPausaContinuar().addActionListener(e -> reanudarPartido());
        view.getBotonPausaReiniciar().addActionListener(e -> reiniciarPartido());
        view.getBotonPausaCambiarPersonaje().addActionListener(e -> cambiarPersonaje());
        view.getBotonPausaConfiguracion().addActionListener(
                e -> gui.mostrarConfiguracionDesdePausa());
        view.getBotonPausaSalir().addActionListener(e -> salirAlMenu());

        view.getBotonResultadoRevancha().addActionListener(e -> revancha());
        view.getBotonResultadoCambiarPersonaje().addActionListener(
                e -> cambiarPersonajeDesdeResultado());
        view.getBotonResultadoMenu().addActionListener(
                e -> volverAlMenuDesdeResultado());
    }

    public boolean iniciar(ConfiguracionPartido configuracion,
                           Personaje personaje1, Personaje personaje2,
                           PerfilJugador perfil1, PerfilJugador perfil2,
                           String rutaEscenario, String rutaBalon) {
        if (configuracion == null || personaje1 == null || personaje2 == null
                || perfil1 == null || perfil2 == null
                || rutaEscenario == null || rutaBalon == null) {
            JOptionPane.showMessageDialog(
                    view,
                    "Faltan datos para iniciar el partido.",
                    "DOPOSoccer",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        this.configuracion = configuracion;
        this.personaje1 = personaje1;
        this.personaje2 = personaje2;
        this.perfil1 = perfil1;
        this.perfil2 = perfil2;
        this.rutaEscenario = rutaEscenario;
        this.rutaBalon = rutaBalon;

        crearPartido();
        prepararTeclas();

        ultimoTiempo = System.nanoTime();
        timer = new Timer(INTERVALO, e -> actualizar());
        timer.start();
        view.requestFocusInWindow();

        return true;
    }

    private void crearPartido() {
        partido = new Partido(
                configuracion,
                personaje1,
                personaje2,
                perfil1.getConfiguracion(),
                perfil2.getConfiguracion());
        view.iniciar(partido, rutaEscenario, rutaBalon);
    }

    public void detener() {
        if (timer != null) {
            timer.stop();
            timer = null;
        }
        if (escuchaTeclas != null) {
            view.removeKeyListener(escuchaTeclas);
            escuchaTeclas = null;
        }
        teclas.clear();
        gui.restaurarMusicaMenu();
    }

    private void prepararTeclas() {
        escuchaTeclas = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int codigo = e.getKeyCode();

                if (codigo == KeyEvent.VK_ESCAPE) {
                    alternarPausa();
                    return;
                }

                if (teclas.add(codigo)) {
                    if (codigo == tecla(1, ConfiguracionControles.Accion.SALTAR)) {
                        partido.saltar(1);
                    } else if (codigo == tecla(1, ConfiguracionControles.Accion.PATEAR)) {
                        partido.patear(1);
                        view.mostrarPatada(1);
                    } else if (codigo == tecla(2, ConfiguracionControles.Accion.SALTAR)) {
                        partido.saltar(2);
                    } else if (codigo == tecla(2, ConfiguracionControles.Accion.PATEAR)) {
                        partido.patear(2);
                        view.mostrarPatada(2);
                    } else if (codigo == teclaPoder(1)) {
                        partido.activarPoder(1);
                    } else if (codigo == teclaPoder(2)) {
                        partido.activarPoder(2);
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                teclas.remove(e.getKeyCode());
            }
        };

        view.addKeyListener(escuchaTeclas);
    }

    private void actualizar() {
        if (partido == null) {
            return;
        }

        long ahora = System.nanoTime();
        double dt = (ahora - ultimoTiempo) / 1_000_000_000.0;
        ultimoTiempo = ahora;

        if (dt <= 0) {
            return;
        }
        if (dt > 0.05) {
            dt = 0.05;
        }

        if (partido.getEstado() != EstadoPartido.PAUSADO) {
            double paso1 = velocidadJugador(1) * dt;
            double paso2 = velocidadJugador(2) * dt;

            if (teclas.contains(tecla(1, ConfiguracionControles.Accion.IZQUIERDA))) {
                partido.mover(1, -paso1);
            }
            if (teclas.contains(tecla(1, ConfiguracionControles.Accion.DERECHA))) {
                partido.mover(1, paso1);
            }
            if (teclas.contains(tecla(2, ConfiguracionControles.Accion.IZQUIERDA))) {
                partido.mover(2, -paso2);
            }
            if (teclas.contains(tecla(2, ConfiguracionControles.Accion.DERECHA))) {
                partido.mover(2, paso2);
            }
        }

        partido.actualizar(dt);
        view.repaint();

        if (partido.getEstado() == EstadoPartido.TERMINADO) {
            timer.stop();
            mostrarResultado();
        }
    }

    private double velocidadJugador(int jugador) {
        int speed = partido.getJugadores().get(jugador - 1)
                .getEstadisticas().getSpeed();
        double factor = 1 + ParametrosJuego.INCREMENTO_POR_PUNTO * (speed - 5);
        double multiplicadorEfecto = partido.getJugadores().get(jugador - 1)
                .getMultiplicadorVelocidad();
        return VELOCIDAD * Math.max(0.2, factor) * multiplicadorEfecto;
    }

    /**
     * Devuelve la tecla asignada a una acción, invirtiéndola si el jugador
     * está bajo el efecto del Flechazo de Amor.
     */
    private int tecla(int jugador, ConfiguracionControles.Accion accion) {
        ConfiguracionControles controles = gui.getConfiguracionControles();
        if (partido != null && partido.tieneControlesInvertidos(jugador)) {
            accion = invertirAccion(accion);
        }
        return controles.getTecla(jugador, accion);
    }

    private int teclaPoder(int jugador) {
        return gui.getConfiguracionControles().getTecla(
                jugador, ConfiguracionControles.Accion.PODER);
    }

    private static ConfiguracionControles.Accion invertirAccion(
            ConfiguracionControles.Accion accion) {
        switch (accion) {
            case IZQUIERDA:
                return ConfiguracionControles.Accion.DERECHA;
            case DERECHA:
                return ConfiguracionControles.Accion.IZQUIERDA;
            case SALTAR:
                return ConfiguracionControles.Accion.PATEAR;
            case PATEAR:
                return ConfiguracionControles.Accion.SALTAR;
            default:
                return accion;
        }
    }

    private void alternarPausa() {
        if (partido.getEstado() == EstadoPartido.PAUSADO) {
            reanudarPartido();
        } else if (partido.getEstado() == EstadoPartido.EN_CURSO
                || partido.getEstado() == EstadoPartido.LISTO) {
            pausarPartido();
        }
    }

    private void pausarPartido() {
        partido.pausar();
        view.setPausaVisible(true);
        view.repaint();
    }

    private void reanudarPartido() {
        partido.reanudar();
        view.setPausaVisible(false);
        view.requestFocusInWindow();
        view.repaint();
    }

    private void reiniciarPartido() {
        if (!confirmar("¿Reiniciar el partido?")) {
            return;
        }

        iniciarPartidoNuevo();
    }

    private void revancha() {
        iniciarPartidoNuevo();
    }

    private void iniciarPartidoNuevo() {
        crearPartido();
        teclas.clear();
        ultimoTiempo = System.nanoTime();
        if (timer != null) {
            timer.restart();
        }
        view.setPausaVisible(false);
        view.requestFocusInWindow();
        view.repaint();
    }

    private void cambiarPersonajeDesdeResultado() {
        detener();
        gui.cambiarPersonajeDesdeJuego();
    }

    private void volverAlMenuDesdeResultado() {
        detener();
        gui.volverAlMenuDesdeJuego();
    }

    private void cambiarPersonaje() {
        if (!confirmar("¿Cambiar de personaje? La partida actual se reiniciará.")) {
            return;
        }

        detener();
        gui.cambiarPersonajeDesdeJuego();
    }

    private void salirAlMenu() {
        if (!confirmar("¿Salir al menú principal?")) {
            return;
        }

        detener();
        gui.volverAlMenuDesdeJuego();
    }

    private void volverASeleccionVisual() {
        detener();
        gui.mostrarConfiguracionVisual();
    }

    private boolean confirmar(String mensaje) {
        int opcion = JOptionPane.showConfirmDialog(
                view,
                mensaje,
                "Pausa",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        return opcion == JOptionPane.YES_OPTION;
    }

    private void mostrarResultado() {
        view.mostrarResultado(partido);
    }
}
