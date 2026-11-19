package domain;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Partido implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Modalidad modalidad;
    private final TipoPartido tipo;
    private final List<Jugador> jugadores;
    private final Balon balon;
    private final Cancha cancha;
    private final Marcador marcador;
    private EstadoPartido estado;
    private double tiempoRestante;
    private ResultadoPartido resultado;
    private final Resolucion resolucion;
    private final ReglaPartido regla;
    private final PerfilIA perfilIA;
    private final boolean otorgarPuntos;
    private final double tiempoTotal;
    private double tiempoTranscurrido;
    private int puntosJugador1;
    private int puntosJugador2;
    private final GestorPoderes gestorPoderes = new GestorPoderes();
    private final List<Balon> balonesIlusion = new ArrayList<>();
    private final Random randomIlusion = new Random();
    private boolean patadaJugador1;
    private boolean patadaJugador2;

    /**
     * Crea un partido sin perfil de IA y otorgando puntos.
     *
     * @param configuracion configuración del partido
     * @param p1 personaje del jugador 1
     * @param p2 personaje del jugador 2
     * @param configuracion1 configuración del jugador 1
     * @param configuracion2 configuración del jugador 2
     */
    public Partido(ConfiguracionPartido configuracion, Personaje p1,
                    Personaje p2, ConfiguracionJugador configuracion1,
                    ConfiguracionJugador configuracion2) {
        this(configuracion, p1, p2, configuracion1, configuracion2, null, true);
    }

    /**
     * Crea un partido con un perfil de IA y otorgando puntos.
     *
     * @param configuracion configuración del partido
     * @param p1 personaje del jugador 1
     * @param p2 personaje del jugador 2
     * @param configuracion1 configuración del jugador 1
     * @param configuracion2 configuración del jugador 2
     * @param perfilIA perfil de comportamiento de la máquina
     */
    public Partido(ConfiguracionPartido configuracion, Personaje p1, Personaje p2,
                    ConfiguracionJugador configuracion1, ConfiguracionJugador configuracion2,
                    PerfilIA perfilIA) {
        this(configuracion, p1, p2, configuracion1, configuracion2, perfilIA, true);
    }

    /**
     * Crea un partido con todos sus datos.
     *
     * @param configuracion configuración del partido
     * @param p1 personaje del jugador 1
     * @param p2 personaje del jugador 2
     * @param configuracion1 configuración del jugador 1
     * @param configuracion2 configuración del jugador 2
     * @param perfilIA perfil de comportamiento de la máquina
     * @param otorgarPuntos indica si el partido reparte puntos al terminar
     */
    public Partido(ConfiguracionPartido configuracion, Personaje p1, Personaje p2,
                    ConfiguracionJugador configuracion1, ConfiguracionJugador configuracion2,
                    PerfilIA perfilIA, boolean otorgarPuntos) {
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
        // Si el perfil de la IA viene en la configuración, se usa como
        // respaldo para que el constructor corto también funcione en PvsM.
        if (perfilIA != null) {
            this.perfilIA = perfilIA;
        } else {
            this.perfilIA = configuracion.getPerfilIA();
        }
        if (modalidad == Modalidad.PvsM && this.perfilIA == null) {
            throw new IllegalArgumentException(
                    "En PvsM se necesita el perfil de la IA");
        }
        this.otorgarPuntos = otorgarPuntos;
        this.cancha = new Cancha(resolucion);
        this.jugadores = FabricaJugadores.crear(p1, p2, configuracion1,
                configuracion2, cancha, modalidad, this.perfilIA);
        this.balon = new Balon(
                cancha.getAncho() / 2.0,
                cancha.getSuelo() - ParametrosJuego.RADIO_BALON,
                ParametrosJuego.RADIO_BALON);
        this.marcador = new Marcador();
        this.estado = EstadoPartido.LISTO;
        this.tiempoTotal = configuracion.getTiempo() * 60.0;
        this.tiempoRestante = tiempoTotal;
        this.regla = ReglaFactory.crearRegla(configuracion);
        this.tiempoTranscurrido = 0;
    }

    /**
     * Actualiza el estado del partido durante un instante.
     *
     * @param dt tiempo transcurrido en segundos
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

        actualizarReloj(dt);
        actualizarEntidades(dt);
        resolverColisiones(dt);
        actualizarBalonesIlusion(dt);
        detectarGoles();

        if (regla.terminado(this)) {
            terminar(regla.getMotivo());
        }
    }

    /**
     * Avanza el tiempo del partido y descuenta el tiempo restante.
     */
    private void actualizarReloj(double dt) {
        tiempoTranscurrido += dt;
        if (tipo == TipoPartido.TIEMPO && tiempoRestante > 0) {
            tiempoRestante -= dt;
            if (tiempoRestante < 0) {
                tiempoRestante = 0;
            }
        }
    }

    /**
     * Actualiza el balón, los jugadores y el tiro de poder.
     */
    private void actualizarEntidades(double dt) {
        balon.actualizar(dt, cancha);
        for (Jugador jugador : jugadores) {
            jugador.actualizarControl(this, dt);
            jugador.actualizar(dt, cancha);
            jugador.getBarraPoder().actualizarBarra(dt);
        }
        gestorPoderes.actualizar(dt);
    }

    /**
     * Resuelve todas las colisiones del instante.
     */
    private void resolverColisiones(double dt) {
        FisicaPartido.resolverJugadores(jugadores, cancha);
        resolverBalonJugador();
        FisicaPartido.resolverBalonAtrapado(jugadores.get(0), jugadores.get(1),
                balon, cancha, dt);
        ColisionesArcos.resolver(balon, cancha);
    }

    /**
     * Hace que el balón rebote al chocar con la cabeza o la bota de un
     * personaje y avisa al gestor de poderes de cada contacto.
     */
    private void resolverBalonJugador() {
        for (int iteracion = 0; iteracion < 2; iteracion++) {
            for (Jugador jugador : jugadores) {
                // La cabeza solo choca con balones que estén a su altura;
                // un balón rodando por el suelo no debe subir a la cabeza.
                if (balon.getY() < jugador.getBotaY()
                        && FisicaPartido.resolverBalonCirculo(balon, jugador,
                                cancha, jugador.getCabezaX(), jugador.getCabezaY(),
                                ParametrosJuego.RADIO_CABEZA_JUGADOR)) {
                    gestorPoderes.alContactarBalon(jugador, balon);
                }

                if (FisicaPartido.resolverBalonCirculo(balon, jugador,
                        cancha, jugador.getBotaX(), jugador.getBotaY(),
                        ParametrosJuego.RADIO_BOTA_JUGADOR)) {
                    gestorPoderes.alContactarBalon(jugador, balon);
                }
            }
        }
    }

    /**
     * Detecta si el balón entró en algún arco y actualiza el marcador.
     */
    private void detectarGoles() {
        if (cancha.getArcoIzquierdo().estaDentro(balon)) {
            marcador.sumarGol(2);
            // El jugador 1 recibió el gol: su barra sube un cuarto.
            jugador(1).getBarraPoder().sumarFraccion(
                    ParametrosJuego.FRACCION_BARRA_POR_GOL);
            reiniciarDespuesDeGol();
        } else if (cancha.getArcoDerecho().estaDentro(balon)) {
            marcador.sumarGol(1);
            // El jugador 2 recibió el gol: su barra sube un cuarto.
            jugador(2).getBarraPoder().sumarFraccion(
                    ParametrosJuego.FRACCION_BARRA_POR_GOL);
            reiniciarDespuesDeGol();
        }
    }

    /**
     * Mientras algún jugador está confundido, mantiene siete balones falsos
     * que se mueven como el balón real. Son solo decorativos: no empujan ni
     * dañan a nadie.
     *
     * @param dt tiempo transcurrido desde la última actualización
     */
    private void actualizarBalonesIlusion(double dt) {
        boolean hayConfundido = false;
        for (Jugador jugador : jugadores) {
            if (jugador.estaConfundido()) {
                hayConfundido = true;
                break;
            }
        }

        if (!hayConfundido) {
            balonesIlusion.clear();
            return;
        }

        if (balonesIlusion.isEmpty()) {
            crearBalonesIlusion();
        }

        for (Balon ilusion : balonesIlusion) {
            ilusion.actualizar(dt, cancha);
        }
    }

    /**
     * Crea los siete balones falsos en posiciones aleatorias.
     */
    private void crearBalonesIlusion() {
        balonesIlusion.clear();
        double margen = ParametrosJuego.RADIO_BALON + 10;
        double ancho = cancha.getAncho();
        double alto = cancha.getSuelo();

        for (int i = 0; i < ParametrosJuego.CANTIDAD_BALONES_ILUSION; i++) {
            double x = margen + randomIlusion.nextDouble()
                    * Math.max(1, ancho - margen * 2);
            double y = margen + randomIlusion.nextDouble()
                    * Math.max(1, alto - margen * 2);
            Balon falso = new Balon(x, y, ParametrosJuego.RADIO_BALON);
            falso.establecerVelocidad(
                    (randomIlusion.nextDouble() - 0.5) * 240,
                    -120 - randomIlusion.nextDouble() * 180);
            balonesIlusion.add(falso);
        }
    }

    /**
     * Mueve a un jugador si el partido está en juego.
     *
     * @param jugador número del jugador: 1 o 2
     * @param dx desplazamiento horizontal
     */
    public void mover(int jugador, double dx) {
        if (!puedeJugar()) {
            return;
        }
        jugador(jugador).mover(dx, cancha);
    }

    /**
     * Hace saltar a un jugador si el partido está en juego.
     *
     * @param jugador número del jugador: 1 o 2
     */
    public void saltar(int jugador) {
        if (!puedeJugar()) {
            return;
        }
        jugador(jugador).saltar();
    }

    /**
     * Activa el aura de poder de un jugador si su barra está llena y no
     * tiene ya un aura activa.
     *
     * @param jugador número del jugador: 1 o 2
     * @return true si el aura se activó
     */
    public boolean activarPoder(int jugador) {
        if (!puedeJugar()) {
            return false;
        }

        Jugador jugadorActual = jugador(jugador);
        if (jugadorActual.isAuraActiva()) {
            return false;
        }
        if (!jugadorActual.getBarraPoder().consumir()) {
            return false;
        }

        jugadorActual.activarAura();
        return true;
    }

    /**
     * Indica si un jugador tiene sus controles invertidos por el Beso de la Fea.
     *
     * @param jugador número del jugador: 1 o 2
     * @return true si debe invertir sus teclas
     */
    public boolean tieneControlesInvertidos(int jugador) {
        return jugador(jugador).tieneControlesInvertidos();
    }

    /**
     * Devuelve el tiro de poder activo del partido.
     *
     * @return tiro de poder activo o null si no hay ninguno
     */
    public TiroPoder getTiroPoder() {
        return gestorPoderes.getTiroPoder();
    }

    /**
     * Devuelve los balones falsos activos de la Ilusión.
     *
     * @return lista de balones falsos
     */
    public List<Balon> getBalonesIlusion() {
        return balonesIlusion;
    }

    /**
     * Ejecuta la patada de un jugador e impacta al rival si está al alcance.
     *
     * @param jugador número del jugador: 1 o 2
     */
    public void patear(int jugador) {
        if (!puedeJugar()) {
            return;
        }
        Jugador pateador = jugador(jugador);
        if (!pateador.puedePatear()) {
            return;
        }
        if (jugador == 1) {
            patadaJugador1 = true;
        } else {
            patadaJugador2 = true;
        }

        // Si la patada toca el balón, solo se impulsa el balón.
        if (pateador.patear(balon)) {
            return;
        }

        Jugador rival;
        if (jugador == 1) {
            rival = jugador(2);
        } else {
            rival = jugador(1);
        }
        if (rivalEstaAlAlcanceDePatada(pateador, rival)) {
            rival.recibirDanio(pateador.getEstadisticas().getDanoPatada());
        }
    }

    /**
     * Indica si hubo una patada desde la última consulta y reinicia el aviso.
     * Lo usa la presentación para animar la patada de la máquina.
     *
     * @param jugador número del jugador: 1 o 2
     * @return true si el jugador pateó desde la última consulta
     */
    public boolean consumirPatada(int jugador) {
        if (jugador == 1) {
            boolean hubo = patadaJugador1;
            patadaJugador1 = false;
            return hubo;
        }
        boolean hubo = patadaJugador2;
        patadaJugador2 = false;
        return hubo;
    }

    /**
     * Indica si el rival está al alcance de la bota del pateador.
     *
     * @param pateador jugador que patea
     * @param rival jugador rival
     * @return true si la patada alcanza al rival
     */
    private boolean rivalEstaAlAlcanceDePatada(Jugador pateador, Jugador rival) {
        double distanciaCabeza = Math.hypot(
                pateador.getBotaX() - rival.getCabezaX(),
                pateador.getBotaY() - rival.getCabezaY());
        double distanciaBota = Math.hypot(
                pateador.getBotaX() - rival.getBotaX(),
                pateador.getBotaY() - rival.getBotaY());

        double alcanceCabeza = ParametrosJuego.RADIO_BOTA_JUGADOR
                + ParametrosJuego.RADIO_CABEZA_JUGADOR
                + ParametrosJuego.TOLERANCIA_GOLPE_PATADA;
        double alcanceBota = ParametrosJuego.RADIO_BOTA_JUGADOR * 2
                + ParametrosJuego.TOLERANCIA_GOLPE_PATADA * 2;

        return distanciaCabeza <= alcanceCabeza || distanciaBota <= alcanceBota;
    }

    /**
     * Suma un gol al marcador del jugador indicado.
     *
     * @param jugador número del jugador: 1 o 2
     */
    public void sumarGol(int jugador) {
        marcador.sumarGol(jugador);
    }

    /**
     * Reinicia el partido después de un gol.
     */
    public void reiniciarDespuesDeGol() {
        gestorPoderes.limpiar();
        balonesIlusion.clear();
        balon.reiniciar(cancha.getAncho() / 2.0, cancha.getAlto() / 2.0);
        jugador(1).limpiarEfectos();
        jugador(2).limpiarEfectos();
        jugador(1).reiniciar(cancha.getAncho() / 4.0, cancha.getSuelo() - ParametrosJuego.RADIO_JUGADOR);
        jugador(2).reiniciar(cancha.getAncho() * 3.0 / 4.0, cancha.getSuelo() - ParametrosJuego.RADIO_JUGADOR);
    }

    /**
     * Pausa el partido si está listo o en curso.
     */
    public void pausar() {
        if (estado == EstadoPartido.LISTO || estado == EstadoPartido.EN_CURSO) {
            estado = EstadoPartido.PAUSADO;
        }
    }

    /**
     * Reanuda el partido si estaba pausado.
     */
    public void reanudar() {
        if (estado == EstadoPartido.PAUSADO) {
            estado = EstadoPartido.EN_CURSO;
        }
    }

    /**
     * Termina el partido por fin de tiempo.
     */
    public void terminar() {
        terminar(MotivoFin.TIEMPO);
    }

    /**
     * Termina el partido con el motivo indicado.
     *
     * @param motivo motivo de finalización
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
        if (otorgarPuntos) {
            otorgarPuntos();
        }
    }

    /**
     * Entrega los puntos de la partida a los dos perfiles. El ganador recibe
     * cien puntos y el perdedor veinticinco. Si hubiera empate, ambos reciben
     * la cantidad del perdedor.
     */
    private void otorgarPuntos() {
        int ganador = resultado.getGanador();

        if (ganador == 1) {
            puntosJugador1 = ParametrosJuego.PUNTOS_GANADOR;
            puntosJugador2 = ParametrosJuego.PUNTOS_PERDEDOR;
        } else if (ganador == 2) {
            puntosJugador1 = ParametrosJuego.PUNTOS_PERDEDOR;
            puntosJugador2 = ParametrosJuego.PUNTOS_GANADOR;
        } else {
            puntosJugador1 = ParametrosJuego.PUNTOS_PERDEDOR;
            puntosJugador2 = ParametrosJuego.PUNTOS_PERDEDOR;
        }

        jugadores.get(0).getPuntos().sumar(puntosJugador1);
        jugadores.get(1).getPuntos().sumar(puntosJugador2);
    }

    /**
     * Indica si el partido permite jugar en este momento.
     *
     * @return true si el partido está listo o en curso
     */
    private boolean puedeJugar() {
        return estado == EstadoPartido.LISTO || estado == EstadoPartido.EN_CURSO;
    }

    /**
     * Devuelve el jugador con el número indicado.
     *
     * @param numero número del jugador: 1 o 2
     * @return jugador correspondiente
     */
    private Jugador jugador(int numero) {
        if (numero != 1 && numero != 2) {
            throw new IllegalArgumentException("El número de jugador debe ser 1 o 2");
        }
        return jugadores.get(numero - 1);
    }

    /**
     * Devuelve el marcador del partido.
     *
     * @return marcador del partido
     */
    public Marcador getMarcador() {
        return marcador;
    }

    /**
     * Devuelve el estado actual del partido.
     *
     * @return estado del partido
     */
    public EstadoPartido getEstado() {
        return estado;
    }

    /**
     * Devuelve el tipo de partido.
     *
     * @return tipo de partido
     */
    public TipoPartido getTipo() {
        return tipo;
    }

    /**
     * Devuelve la modalidad del partido.
     *
     * @return modalidad del partido
     */
    public Modalidad getModalidad() {
        return modalidad;
    }

    /**
     * Devuelve el balón del partido.
     *
     * @return balón del partido
     */
    public Balon getBalon() {
        return balon;
    }

    /**
     * Devuelve la lista de jugadores del partido.
     *
     * @return jugadores del partido
     */
    public List<Jugador> getJugadores() {
        return jugadores;
    }

    /**
     * Devuelve el tiempo restante del partido.
     *
     * @return segundos restantes
     */
    public double getTiempoRestante() {
        return tiempoRestante;
    }

    /**
     * Devuelve la regla de finalización del partido.
     *
     * @return regla del partido
     */
    public ReglaPartido getRegla() {
        return regla;
    }

    /**
     * Devuelve la cancha del partido.
     *
     * @return cancha del partido
     */
    public Cancha getCancha() {
        return cancha;
    }

    /**
     * Devuelve el resultado del partido.
     *
     * @return resultado o null si aún no terminó
     */
    public ResultadoPartido getResultado() {
        return resultado;
    }

    /**
     * Devuelve los puntos obtenidos por el jugador 1.
     *
     * @return puntos del jugador 1
     */
    public int getPuntosJugador1() {
        return puntosJugador1;
    }

    /**
     * Devuelve los puntos obtenidos por el jugador 2.
     *
     * @return puntos del jugador 2
     */
    public int getPuntosJugador2() {
        return puntosJugador2;
    }
}
