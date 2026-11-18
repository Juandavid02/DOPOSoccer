package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    private final Resolucion resolucion;
    private final ReglaPartido regla;
    private final PerfilIA perfilIA;
    private final boolean otorgarPuntos;
    private final double tiempoTotal;
    private double tiempoTranscurrido;
    private int puntosJugador1;
    private int puntosJugador2;
    private TiroPoder tiroPoder;
    private final List<BalonIlusion> balonesIlusion = new ArrayList<>();
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
        this.jugadores = List.of(
                new JugadorHumano(configuracion1, p1,
                        new BarraPoder(ParametrosJuego.CAPACIDAD_BARRA_PODER, ParametrosJuego.RECARGA_BARRA_PODER),
                        Lado.IZQUIERDO,
                        cancha.getAncho() / 4.0,
                        cancha.getSuelo() - ParametrosJuego.RADIO_JUGADOR),
                crearJugadorDerecho(configuracion2, p2)
        );
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
     * Crea el jugador que ocupa el lado derecho de la cancha.
     *
     * @param configuracion configuración del jugador
     * @param personaje personaje elegido
     * @return jugador humano o máquina según la modalidad
     */
    private Jugador crearJugadorDerecho(ConfiguracionJugador configuracion,
                                        Personaje personaje) {
        BarraPoder barra = new BarraPoder(
                ParametrosJuego.CAPACIDAD_BARRA_PODER,
                ParametrosJuego.RECARGA_BARRA_PODER);
        if (modalidad == Modalidad.PvsM && perfilIA != null) {
            return new JugadorMaquina(configuracion, personaje, barra,
                    Lado.DERECHO,
                    cancha.getAncho() * 3.0 / 4.0,
                    cancha.getSuelo() - ParametrosJuego.RADIO_JUGADOR,
                    perfilIA);
        }
        return new JugadorHumano(configuracion, personaje, barra,
                Lado.DERECHO,
                cancha.getAncho() * 3.0 / 4.0,
                cancha.getSuelo() - ParametrosJuego.RADIO_JUGADOR);
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
        tiempoTranscurrido += dt;
        if (tipo == TipoPartido.TIEMPO && tiempoRestante > 0) {
            tiempoRestante -= dt;
            if (tiempoRestante < 0) {
                tiempoRestante = 0;
            }
        }
        balon.actualizar(dt, cancha);
        for (Jugador jugador : jugadores) {
            jugador.actualizarControl(this, dt);
            jugador.actualizar(dt, cancha);
            jugador.getBarraPoder().actualizarBarra(dt);
        }

        if (tiroPoder != null) {
            tiroPoder.actualizar(dt);
        }

        resolverColisionJugadores();
        resolverColisionBalonJugador();
        resolverColisionArcos();
        actualizarBalonesIlusion(dt);

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
        if (regla.terminado(this)) {
            terminar(regla.getMotivo());
        }
    }

    /**
     * Resuelve los choques entre los dos personajes usando sus dos
     * círculos: cabeza y bota.
     */
    private void resolverColisionJugadores() {
        Jugador jugador1 = jugadores.get(0);
        Jugador jugador2 = jugadores.get(1);

        for (int iteracion = 0; iteracion < 3; iteracion++) {
            double[] cabeza1 = centroCabeza(jugador1);
            double[] cabeza2 = centroCabeza(jugador2);
            double[] bota1 = centroBota(jugador1);
            double[] bota2 = centroBota(jugador2);

            resolverCirculosJugadores(jugador1, jugador2,
                    cabeza1, ParametrosJuego.RADIO_CABEZA_JUGADOR,
                    cabeza2, ParametrosJuego.RADIO_CABEZA_JUGADOR);

            cabeza1 = centroCabeza(jugador1);
            bota2 = centroBota(jugador2);
            resolverCirculosJugadores(jugador1, jugador2,
                    cabeza1, ParametrosJuego.RADIO_CABEZA_JUGADOR,
                    bota2, ParametrosJuego.RADIO_BOTA_JUGADOR);

            bota1 = centroBota(jugador1);
            cabeza2 = centroCabeza(jugador2);
            resolverCirculosJugadores(jugador1, jugador2,
                    bota1, ParametrosJuego.RADIO_BOTA_JUGADOR,
                    cabeza2, ParametrosJuego.RADIO_CABEZA_JUGADOR);

            bota1 = centroBota(jugador1);
            bota2 = centroBota(jugador2);
            resolverCirculosJugadores(jugador1, jugador2,
                    bota1, ParametrosJuego.RADIO_BOTA_JUGADOR,
                    bota2, ParametrosJuego.RADIO_BOTA_JUGADOR);
        }
    }

    /**
     * Resuelve la separación entre dos círculos de dos jugadores.
     *
     * @param jugador1 primer jugador
     * @param jugador2 segundo jugador
     * @param circulo1 centro del círculo del primer jugador
     * @param radio1 radio del círculo del primer jugador
     * @param circulo2 centro del círculo del segundo jugador
     * @param radio2 radio del círculo del segundo jugador
     */
    private void resolverCirculosJugadores(Jugador jugador1, Jugador jugador2,
                                           double[] circulo1, double radio1,
                                           double[] circulo2, double radio2) {
        double dx = circulo2[0] - circulo1[0];
        double dy = circulo2[1] - circulo1[1];
        double distancia = Math.sqrt(dx * dx + dy * dy);
        double distanciaMinima = radio1 + radio2;

        if (distancia >= distanciaMinima) {
            return;
        }

        double normalX;
        double normalY;
        if (distancia < 0.0001) {
            normalX = 1;
            normalY = 0;
        } else {
            normalX = dx / distancia;
            normalY = dy / distancia;
        }

        double separacion = distanciaMinima - distancia;
        jugador1.desplazar(-normalX * separacion / 2,
                -normalY * separacion / 2, cancha);
        jugador2.desplazar(normalX * separacion / 2,
                normalY * separacion / 2, cancha);
    }

    /**
     * Hace que el balón rebote al chocar con la cabeza o la bota de un
     * personaje.
     */
    private void resolverColisionBalonJugador() {
        for (int iteracion = 0; iteracion < 2; iteracion++) {
            for (Jugador jugador : jugadores) {
                // La cabeza solo choca con balones que estén a su altura;
                // un balón rodando por el suelo no debe subir a la cabeza.
                if (balon.getY() < jugador.getPosicionY() + ParametrosJuego.OFFSET_BOTA_Y_JUGADOR) {
                    double[] cabeza = centroCabeza(jugador);
                    resolverColisionBalonCirculo(jugador, cabeza[0], cabeza[1],
                            ParametrosJuego.RADIO_CABEZA_JUGADOR);
                }

                double[] bota = centroBota(jugador);
                resolverColisionBalonCirculo(jugador, bota[0], bota[1],
                        ParametrosJuego.RADIO_BOTA_JUGADOR);
            }
        }
    }

    /**
     * Resuelve el choque entre el balón y un círculo del jugador.
     *
     * @param jugador jugador que choca con el balón
     * @param centroX centro del círculo en X
     * @param centroY centro del círculo en Y
     * @param radio radio del círculo
     */
    private void resolverColisionBalonCirculo(Jugador jugador,
                                              double centroX, double centroY,
                                              double radio) {
        double dx = balon.getX() - centroX;
        double dy = balon.getY() - centroY;
        double distancia = Math.sqrt(dx * dx + dy * dy);
        double distanciaMinima = ParametrosJuego.RADIO_BALON_COLISION + radio;

        if (distancia >= distanciaMinima) {
            return;
        }

        double normalX;
        double normalY;
        if (distancia < 0.0001) {
            normalX = 1;
            normalY = 0;
        } else {
            normalX = dx / distancia;
            normalY = dy / distancia;
        }

        double nuevaX = centroX + normalX * distanciaMinima;
        double nuevaY = centroY + normalY * distanciaMinima;

        // El balón no debe salir por debajo del suelo ni por encima del
        // techo. Si el choque lo empujaría fuera, se resuelve en horizontal.
        double yMinimo = ParametrosJuego.RADIO_BALON;
        double yMaximo = cancha.getSuelo() - ParametrosJuego.RADIO_BALON;
        if (nuevaY < yMinimo || nuevaY > yMaximo) {
            nuevaY = Math.max(yMinimo, Math.min(yMaximo, nuevaY));

            double separacionY = nuevaY - centroY;
            double cuadradoHorizontal = distanciaMinima * distanciaMinima
                    - separacionY * separacionY;
            if (cuadradoHorizontal > 0) {
                double separacionX = Math.sqrt(cuadradoHorizontal);
                double direccion = -1;
                if (dx >= 0) {
                    direccion = 1;
                }
                nuevaX = centroX + direccion * separacionX;
            }
        }

        balon.reubicar(nuevaX, nuevaY);

        double normalFinalX = (nuevaX - centroX) / distanciaMinima;
        double normalFinalY = (nuevaY - centroY) / distanciaMinima;

        // Si el balón quedó apoyado en el suelo o el techo, el impulso se
        // aplica solo en horizontal para que no se entierre ni suba.
        if (nuevaY <= yMinimo + 0.001 || nuevaY >= yMaximo - 0.001) {
            double longitud = Math.abs(normalFinalX);
            if (longitud < 0.0001) {
                normalFinalX = 1;
                longitud = 1;
            }
            normalFinalX = normalFinalX / longitud;
            normalFinalY = 0;
        }

        double velocidadRelativaX = balon.getVelocidadX() - jugador.getVelocidadX();
        double velocidadRelativaY = balon.getVelocidadY() - jugador.getVelocidadY();
        double productoNormal = velocidadRelativaX * normalFinalX
                + velocidadRelativaY * normalFinalY;

        if (productoNormal < 0) {
            double impulso = -(1 + ParametrosJuego.RESTITUCION_COLISION)
                    * productoNormal;
            balon.aplicarImpulso(impulso * normalFinalX, impulso * normalFinalY);
        } else {
            balon.aplicarImpulso(normalFinalX * 30, normalFinalY * 30);
        }

        // Si el jugador tenía un aura, este contacto con el balón ejecuta
        // su tiro de poder.
        if (jugador.isAuraActiva()) {
            dispararPoder(jugador);
        }

        // Un tiro de poder activo golpea al rival (nunca a su dueño).
        if (tiroPoder != null && tiroPoder.isActivo()
                && tiroPoder.getDueno() != jugador) {
            impactarPoder(jugador);
        }
    }

    /**
     * Consume el aura del jugador y lanza su tiro de poder con la trayectoria
     * definida por el poder.
     *
     * @param jugador jugador que dispara el poder
     */
    private void dispararPoder(Jugador jugador) {
        Poder poder = jugador.getPersonaje().getPoder();
        jugador.consumirAura();

        double direccion = -1;
        if (jugador.getLado() == Lado.IZQUIERDO) {
            direccion = 1;
        }
        balon.establecerVelocidad(direccion * poder.getImpulsoX(), poder.getImpulsoY());

        tiroPoder = new TiroPoder(jugador, poder);
    }

    /**
     * Aplica al rival el daño y el efecto del tiro de poder activo.
     *
     * @param rival jugador que recibe el impacto
     */
    private void impactarPoder(Jugador rival) {
        Jugador dueno = tiroPoder.getDueno();
        Poder poder = tiroPoder.getPoder();

        double dano = poder.getDanoTiro(dueno)
                * dueno.getEstadisticas().getMultiplicadorKick();
        rival.recibirDanio(dano);
        rival.aplicarEfecto(poder.crearEfecto());

        tiroPoder.desactivar();
    }

    /**
     * Crea, mueve y resuelve los empujes de los balones falsos de la
     * Ilusión. Solo existen mientras algún jugador está confundido.
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

        for (BalonIlusion ilusion : balonesIlusion) {
            ilusion.actualizar(dt, cancha);
            if (!ilusion.puedeEmpujar()) {
                continue;
            }

            for (Jugador jugador : jugadores) {
                if (jugador.estaConfundido() && ilusion.tocaA(jugador)) {
                    empujarPorIlusion(jugador, ilusion);
                    ilusion.reiniciarEmpuje();
                    break;
                }
            }
        }
    }

    /**
     * Crea los balones falsos de la Ilusión en posiciones aleatorias.
     */
    private void crearBalonesIlusion() {
        balonesIlusion.clear();
        double margen = ParametrosJuego.RADIO_BALON_COLISION + 10;
        double ancho = cancha.getAncho();
        double alto = cancha.getSuelo();

        for (int i = 0; i < ParametrosJuego.CANTIDAD_BALONES_ILUSION; i++) {
            double x = margen + randomIlusion.nextDouble()
                    * Math.max(1, ancho - margen * 2);
            double y = margen + randomIlusion.nextDouble()
                    * Math.max(1, alto - margen * 2);
            balonesIlusion.add(new BalonIlusion(x, y, randomIlusion));
        }
    }

    /**
     * Empuja a un jugador cuando lo toca un balón falso.
     *
     * @param jugador jugador empujado
     * @param ilusion balón falso que produce el empuje
     */
    private void empujarPorIlusion(Jugador jugador, BalonIlusion ilusion) {
        double direccion = -1;
        if (jugador.getPosicionX() >= ilusion.getX()) {
            direccion = 1;
        }
        jugador.desplazar(
                direccion * ParametrosJuego.DESPLAZAMIENTO_BALON_ILUSION,
                -ParametrosJuego.ALTURA_EMPUJE_BALON_ILUSION,
                cancha);
    }

    /**
     * Hace rebotar el balón contra el travesaño superior de los arcos.
     */
    private void resolverColisionArcos() {
        resolverColisionArco(cancha.getArcoIzquierdo());
        resolverColisionArco(cancha.getArcoDerecho());
    }

    /**
     * Resuelve el choque del balón con el travesaño de un arco.
     *
     * @param arco arco con el que puede chocar el balón
     */
    private void resolverColisionArco(Arco arco) {
        double radio = ParametrosJuego.RADIO_BALON_COLISION;
        double centroVisualY = balon.getY()
                - (ParametrosJuego.RADIO_BALON_COLISION - ParametrosJuego.RADIO_BALON);

        boolean dentroX = balon.getX() + radio > arco.getX()
                && balon.getX() - radio < arco.getX() + arco.getAncho();

        if (!dentroX) {
            return;
        }

        double bordeSuperior = arco.getY();

        // Balón cayendo sobre la parte superior de la red.
        if (balon.getVelocidadY() > 0
                && centroVisualY + radio >= bordeSuperior
                && centroVisualY - radio < bordeSuperior) {
            double nuevoCentroVisual = bordeSuperior - radio;
            balon.reubicar(balon.getX(),
                    nuevoCentroVisual
                            + (ParametrosJuego.RADIO_BALON_COLISION
                            - ParametrosJuego.RADIO_BALON));
            balon.aplicarImpulso(0,
                    -balon.getVelocidadY() * (1 + ParametrosJuego.REBOTE));
            return;
        }

        // Balón subiendo desde dentro del arco.
        if (balon.getVelocidadY() < 0
                && centroVisualY - radio <= bordeSuperior
                && centroVisualY + radio > bordeSuperior) {
            double nuevoCentroVisual = bordeSuperior + radio;
            balon.reubicar(balon.getX(),
                    nuevoCentroVisual
                            + (ParametrosJuego.RADIO_BALON_COLISION
                            - ParametrosJuego.RADIO_BALON));
            balon.aplicarImpulso(0,
                    -balon.getVelocidadY() * (1 + ParametrosJuego.REBOTE));
        }
    }

    /**
     * Devuelve el centro del círculo de la cabeza de un jugador.
     *
     * @param jugador jugador consultado
     * @return coordenadas X e Y del centro de la cabeza
     */
    private double[] centroCabeza(Jugador jugador) {
        return new double[]{
                jugador.getPosicionX(),
                jugador.getPosicionY() + ParametrosJuego.OFFSET_CABEZA_JUGADOR
        };
    }

    /**
     * Devuelve el centro del círculo de la bota de un jugador.
     *
     * @param jugador jugador consultado
     * @return coordenadas X e Y del centro de la bota
     */
    private double[] centroBota(Jugador jugador) {
        double direccion = -1;
        if (jugador.getLado() == Lado.IZQUIERDO) {
            direccion = 1;
        }
        return new double[]{
                jugador.getPosicionX()
                        + direccion * ParametrosJuego.OFFSET_BOTA_X_JUGADOR,
                jugador.getPosicionY() + ParametrosJuego.OFFSET_BOTA_Y_JUGADOR
        };
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
        return tiroPoder;
    }

    /**
     * Devuelve los balones falsos activos de la Ilusión.
     *
     * @return lista de balones falsos
     */
    public List<BalonIlusion> getBalonesIlusion() {
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
        double[] bota = centroBota(pateador);
        double[] cabezaRival = centroCabeza(rival);
        double[] botaRival = centroBota(rival);

        double distanciaCabeza = distancia(bota, cabezaRival);
        double distanciaBota = distancia(bota, botaRival);

        double alcanceCabeza = ParametrosJuego.RADIO_BOTA_JUGADOR
                + ParametrosJuego.RADIO_CABEZA_JUGADOR
                + ParametrosJuego.TOLERANCIA_GOLPE_PATADA;
        double alcanceBota = ParametrosJuego.RADIO_BOTA_JUGADOR * 2
                + ParametrosJuego.TOLERANCIA_GOLPE_PATADA * 2;

        return distanciaCabeza <= alcanceCabeza || distanciaBota <= alcanceBota;
    }

    /**
     * Calcula la distancia entre dos puntos.
     *
     * @param punto1 primer punto
     * @param punto2 segundo punto
     * @return distancia entre los puntos
     */
    private double distancia(double[] punto1, double[] punto2) {
        double dx = punto2[0] - punto1[0];
        double dy = punto2[1] - punto1[1];
        return Math.sqrt(dx * dx + dy * dy);
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
        tiroPoder = null;
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
