package domain;

import java.util.List;

public class Partido {
    private final Modalidad modalidad;
    private final TipoPartido tipo;
    private final List<Jugador> jugadores;
    private final Balon balon;
    private final Cancha cancha;
    private final Marcador marcador;
    private EstadoPartido estado;
    private double tiempoRestante;
    private ResultadoPartido resultado;

    public Partido(Modalidad modalidad, TipoPartido tipo, Personaje p1, Personaje p2, String nombreJugador1, String nombreJugador2) {
        if (modalidad == null) {
            throw new IllegalArgumentException("La modalidad no puede ser nula");
        }
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de partido no puede ser nulo");
        }
        if (p1 == null) {
            throw new IllegalArgumentException("El personaje 1 no puede ser nulo");
        }
        if (p2 == null) {
            throw new IllegalArgumentException("El personaje 2 no puede ser nulo");
        }
        if (nombreJugador1 == null || nombreJugador1.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del jugador 1 no puede ser nulo o vacío");
        }
        if (nombreJugador2 == null || nombreJugador2.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del jugador 2 no puede ser nulo o vacío");
        }

        this.modalidad = modalidad;
        this.tipo = tipo;
        this.cancha = new Cancha();
        this.jugadores = List.of(
                new Jugador(nombreJugador1, p1,
                        new BarraPoder(ParametrosJuego.CAPACIDAD_BARRA_PODER, ParametrosJuego.RECARGA_BARRA_PODER),
                        Lado.IZQUIERDO,
                        ParametrosJuego.ANCHO_CANCHA / 4.0,
                        ParametrosJuego.SUELO_Y - ParametrosJuego.RADIO_JUGADOR),
                new Jugador(nombreJugador2, p2,
                        new BarraPoder(ParametrosJuego.CAPACIDAD_BARRA_PODER, ParametrosJuego.RECARGA_BARRA_PODER),
                        Lado.DERECHO,
                        ParametrosJuego.ANCHO_CANCHA * 3.0 / 4.0,
                        ParametrosJuego.SUELO_Y - ParametrosJuego.RADIO_JUGADOR)
        );
        this.balon = new Balon(ParametrosJuego.ANCHO_CANCHA / 2.0, ParametrosJuego.SUELO_Y - ParametrosJuego.RADIO_BALON, ParametrosJuego.RADIO_BALON);
        this.marcador = new Marcador();
        this.estado = EstadoPartido.LISTO;
        this.tiempoRestante = ParametrosJuego.TIEMPO_PARTIDO;
    }

    public void actualizar(double dt) {
        if (dt <= 0) {
            return;
        }
        if (estado == EstadoPartido.PAUSADO || estado == EstadoPartido.TERMINADO) {
            return;
        }
        if (estado == EstadoPartido.LISTO) {
            estado = EstadoPartido.EN_CURSO;
        }
        if (tipo == TipoPartido.TIEMPO) {
            tiempoRestante -= dt;
            if (tiempoRestante <= 0) {
                tiempoRestante = 0;
                terminar();
                return;
            }
        }
        balon.actualizar(dt, cancha);
        for (Jugador jugador : jugadores) {
            jugador.actualizar(dt, cancha);
            jugador.getBarraPoder().actualizarBarra(dt);
        }
        if (cancha.getArcoIzquierdo().estaDentro(balon)) {
            marcador.sumarGol(2);
            reiniciarDespuesDeGol();
        } else if (cancha.getArcoDerecho().estaDentro(balon)) {
            marcador.sumarGol(1);
            reiniciarDespuesDeGol();
        }
    }

    public void mover(int jugador, double dx) {
        if (!puedeJugar()) {
            return;
        }
        jugador(jugador).mover(dx);
    }

    public void saltar(int jugador) {
        if (!puedeJugar()) {
            return;
        }
        jugador(jugador).saltar();
    }

    public void patear(int jugador) {
        if (!puedeJugar()) {
            return;
        }
        jugador(jugador).patear(balon);
    }

    public void sumarGol(int jugador) {
        marcador.sumarGol(jugador);
    }

    public void reiniciarDespuesDeGol() {
        balon.reiniciar(ParametrosJuego.ANCHO_CANCHA / 2.0, ParametrosJuego.ALTO_CANCHA / 2.0);
        jugador(1).reiniciar(ParametrosJuego.ANCHO_CANCHA / 4.0, ParametrosJuego.SUELO_Y - ParametrosJuego.RADIO_JUGADOR);
        jugador(2).reiniciar(ParametrosJuego.ANCHO_CANCHA * 3.0 / 4.0, ParametrosJuego.SUELO_Y - ParametrosJuego.RADIO_JUGADOR);
    }

    public void pausar() {
        if (estado == EstadoPartido.LISTO || estado == EstadoPartido.EN_CURSO) {
            estado = EstadoPartido.PAUSADO;
        }
    }

    public void reanudar() {
        if (estado == EstadoPartido.PAUSADO) {
            estado = EstadoPartido.EN_CURSO;
        }
    }

    public void terminar() {
        estado = EstadoPartido.TERMINADO;
        resultado = new ResultadoPartido(marcador.getGoles(1), marcador.getGoles(2), MotivoFin.TIEMPO, ParametrosJuego.TIEMPO_PARTIDO - tiempoRestante);
    }

    private boolean puedeJugar() {
        return estado == EstadoPartido.LISTO || estado == EstadoPartido.EN_CURSO;
    }

    private Jugador jugador(int numero) {
        if (numero != 1 && numero != 2) {
            throw new IllegalArgumentException("El número de jugador debe ser 1 o 2");
        }
        return jugadores.get(numero - 1);
    }

    public Marcador getMarcador() {
        return marcador;
    }

    public EstadoPartido getEstado() {
        return estado;
    }

    public Balon getBalon() {
        return balon;
    }

    public List<Jugador> getJugadores() {
        return jugadores;
    }

    public double getTiempoRestante() {
        return tiempoRestante;
    }

    public ResultadoPartido getResultado() {
        return resultado;
    }
}
