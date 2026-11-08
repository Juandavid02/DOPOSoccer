package presentation;

import domain.ConfiguracionPartido;
import domain.EstadoPartido;
import domain.Partido;
import domain.PerfilJugador;
import domain.Personaje;
import domain.ResultadoPartido;

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
                e -> gui.mostrarProximamente("Configuración"));
        view.getBotonPausaSalir().addActionListener(e -> salirAlMenu());
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
                    if (codigo == KeyEvent.VK_W) {
                        partido.saltar(1);
                    } else if (codigo == KeyEvent.VK_S) {
                        partido.patear(1);
                        view.mostrarPatada(1);
                    } else if (codigo == KeyEvent.VK_UP) {
                        partido.saltar(2);
                    } else if (codigo == KeyEvent.VK_DOWN) {
                        partido.patear(2);
                        view.mostrarPatada(2);
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
            double paso = VELOCIDAD * dt;

            if (teclas.contains(KeyEvent.VK_A)) {
                partido.mover(1, -paso);
            }
            if (teclas.contains(KeyEvent.VK_D)) {
                partido.mover(1, paso);
            }
            if (teclas.contains(KeyEvent.VK_LEFT)) {
                partido.mover(2, -paso);
            }
            if (teclas.contains(KeyEvent.VK_RIGHT)) {
                partido.mover(2, paso);
            }
        }

        partido.actualizar(dt);
        view.repaint();

        if (partido.getEstado() == EstadoPartido.TERMINADO) {
            timer.stop();
            mostrarResultado();
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
        ResultadoPartido resultado = partido.getResultado();
        String mensaje;

        if (resultado == null) {
            mensaje = "El partido terminó.";
        } else {
            String nombre1 = partido.getJugadores().get(0).getNombre();
            String nombre2 = partido.getJugadores().get(1).getNombre();
            int ganador = resultado.getGanador();

            mensaje = nombre1 + ": " + resultado.getGolesJugador1() + "\n"
                    + nombre2 + ": " + resultado.getGolesJugador2();

            if (ganador == 1) {
                mensaje += "\n\nGanó " + nombre1;
            } else if (ganador == 2) {
                mensaje += "\n\nGanó " + nombre2;
            } else {
                mensaje += "\n\nEmpate";
            }
        }

        JOptionPane.showMessageDialog(
                view,
                mensaje,
                "Fin del partido",
                JOptionPane.INFORMATION_MESSAGE);

        gui.volverAlMenuDesdeJuego();
    }
}
