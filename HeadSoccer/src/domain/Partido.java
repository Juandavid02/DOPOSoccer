package domain;

import java.util.List;

/**
 * Partida de DOPOSoccer.
 *
 * Es la única frontera entre el dominio y la presentación. Coordina a los
 * jugadores, el balón, el marcador y la regla de victoria del tipo de partido
 * elegido.</p>
 */
public class Partido {
    private final Modalidad modalidad;
    private final TipoPartido tipo;
    private final List<Jugador> jugadores;
    private final Balon balon;
    private final Cancha cancha;
    private final Marcador marcador;
    private final Resolucion resolucion;
    private final ReglaPartido regla;

    private EstadoPartido estado;
    private double tiempoRestante;
    private double tiempoTranscurrido;
    private ResultadoPartido resultado;

    public Partido(ConfiguracionPartido configuracion, Personaje p1, Personaje p2, ConfiguracionJugador configuracion1, ConfiguracionJugador configuracion2) {
        if (configuracion == null) {
            throw new IllegalArgumentException("La configuración del partido no puede ser nula");
        }
        if (p1 == null) {
            throw new IllegalArgumentException("El personaje 1 no puede ser nulo");
        }
        if (p2 == null) {
            throw new IllegalArgumentException("El personaje 2 no puede ser nulo");
        }
        if (configuracion1 == null) {
            throw new IllegalArgumentException("La configuración del jugador 1 no puede ser nula");
        }
        if (configuracion2 == null) {
            throw new IllegalArgumentException("La configuración del jugador 2 no puede ser nula");
        }

        this.modalidad = configuracion.getModalidad();
        this.tipo = configuracion.getTipo();
        this.resolucion = configuracion.getResolucion();
        this.cancha = new Cancha(resolucion);
        this.jugadores = List.of(
                new Jugador(configuracion1, p1,
                        new BarraPoder(ParametrosJuego.CAPACIDAD_BARRA_PODER, ParametrosJuego.RECARGA_BARRA_PODER),
                        Lado.IZQUIERDO,
                        cancha.getAncho() / 4.0,
                        cancha.getSuelo() - ParametrosJuego.RADIO_JUGADOR),
                new Jugador(configuracion2, p2,
                        new BarraPoder(ParametrosJuego.CAPACIDAD_BARRA_PODER, ParametrosJuego.RECARGA_BARRA_PODER),
                        Lado.DERECHO,
                        cancha.getAncho() * 3.0 / 4.0,
                        cancha.getSuelo() - ParametrosJuego.RADIO_JUGADOR)
        );
        this.balon = new Balon(cancha.getAncho() / 2.0,
                cancha.getSuelo() - ParametrosJuego.RADIO_BALON,
                ParametrosJuego.RADIO_BALON);
        this.marcador = new Marcador();
        this.estado = EstadoPartido.LISTO;
        this.tiempoRestante = configuracion.getTiempo() * 60.0;
        this.tiempoTranscurrido = 0;
        this.regla = ReglaFactory.crearRegla(configuracion);
    }

    /**
     * Avanza el partido un paso de tiempo.
     *
     * @param dt segundos transcurridos desde el frame anterior
     */
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

        tiempoTranscurrido += dt;

        if (tipo == TipoPartido.TIEMPO) {
            tiempoRestante -= dt;
            if (tiempoRestante < 0) {
                tiempoRestante = 0;
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

        if (regla.terminado(this)) {
            terminar(regla.getMotivo());
        }
    }

    public void mover(int jugador, double dx) {
        if (!puedeJugar()) {
            return;
        }
        jugador(jugador).mover(dx, cancha);
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
        balon.reiniciar(cancha.getAncho() / 2.0, cancha.getAlto() / 2.0);
        jugador(1).reiniciar(cancha.getAncho() / 4.0,
                cancha.getSuelo() - ParametrosJuego.RADIO_JUGADOR);
        jugador(2).reiniciar(cancha.getAncho() * 3.0 / 4.0,
                cancha.getSuelo() - ParametrosJuego.RADIO_JUGADOR);
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

    /**
     * Termina el partido con el motivo indicado y construye el resultado.
     *
     * @param motivo motivo del final del partido
     */
    public void terminar(MotivoFin motivo) {
        if (motivo == null) {
            throw new IllegalArgumentException("El motivo de finalización no puede ser nulo");
        }
        if (estado == EstadoPartido.TERMINADO) {
            return;
        }
        estado = EstadoPartido.TERMINADO;
        resultado = new ResultadoPartido(marcador.getGoles(1), marcador.getGoles(2), motivo, tiempoTranscurrido);
    }

    /**
     * Finaliza el partido manualmente (por ejemplo, desde el menú de pausa).
     */
    public void terminar() {
        terminar(MotivoFin.TIEMPO);
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

    public Modalidad getModalidad() {
        return modalidad;
    }

    public TipoPartido getTipo() {
        return tipo;
    }

    public Resolucion getResolucion() {
        return resolucion;
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

    public double getTiempoTranscurrido() {
        return tiempoTranscurrido;
    }

    public ResultadoPartido getResultado() {
        return resultado;
    }
}
