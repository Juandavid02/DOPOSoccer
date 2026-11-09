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
    private final Resolucion resolucion;
    private final ReglaPartido regla;
    private final double tiempoTotal;
    private double tiempoTranscurrido;
    private int puntosJugador1;
    private int puntosJugador2;
    private TiroPoder tiroPoder;

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
                new JugadorHumano(configuracion1, p1,
                        new BarraPoder(ParametrosJuego.CAPACIDAD_BARRA_PODER, ParametrosJuego.RECARGA_BARRA_PODER),
                        Lado.IZQUIERDO,
                        cancha.getAncho() / 4.0,
                        cancha.getSuelo() - ParametrosJuego.RADIO_JUGADOR),
                new JugadorHumano(configuracion2, p2,
                        new BarraPoder(ParametrosJuego.CAPACIDAD_BARRA_PODER, ParametrosJuego.RECARGA_BARRA_PODER),
                        Lado.DERECHO,
                        cancha.getAncho() * 3.0 / 4.0,
                        cancha.getSuelo() - ParametrosJuego.RADIO_JUGADOR)
        );
        this.balon = new Balon(cancha.getAncho() / 2.0, cancha.getSuelo() - ParametrosJuego.RADIO_BALON, ParametrosJuego.RADIO_BALON);
        this.marcador = new Marcador();
        this.estado = EstadoPartido.LISTO;
        this.tiempoTotal = configuracion.getTiempo() * 60.0;
        this.tiempoRestante = tiempoTotal;
        this.regla = ReglaFactory.crearRegla(configuracion);
        this.tiempoTranscurrido = 0;
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
        tiempoTranscurrido += dt;
        if (tipo == TipoPartido.TIEMPO && tiempoRestante > 0) {
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

        if (tiroPoder != null) {
            tiroPoder.actualizar(dt);
        }

        resolverColisionJugadores();
        resolverColisionBalonJugador();
        resolverColisionArcos();

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
                if (balon.getY() < jugador.getPosicionY()
                        + ParametrosJuego.OFFSET_BOTA_Y_JUGADOR) {
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
                double direccion = dx >= 0 ? 1 : -1;
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
     */
    private void dispararPoder(Jugador jugador) {
        Poder poder = jugador.getPersonaje().getPoder();
        jugador.consumirAura();

        double direccion = jugador.getLado() == Lado.IZQUIERDO ? 1 : -1;
        balon.establecerVelocidad(
                direccion * poder.getImpulsoX(),
                poder.getImpulsoY());

        tiroPoder = new TiroPoder(jugador, poder);
    }

    /**
     * Aplica al rival el daño y el efecto del tiro de poder activo.
     */
    private void impactarPoder(Jugador rival) {
        Jugador dueno = tiroPoder.getDueno();
        Poder poder = tiroPoder.getPoder();

        double dano = poder.getDanoTiro(dueno)
                * dueno.getEstadisticas().getMultiplicadorKick();
        rival.recibirDanio(dano);
        rival.aplicarEfecto(poder.getTipoEfecto(), poder.getDuracion());

        tiroPoder.desactivar();
    }

    /**
     * Hace rebotar el balón contra el travesaño superior de los arcos.
     */
    private void resolverColisionArcos() {
        resolverColisionArco(cancha.getArcoIzquierdo());
        resolverColisionArco(cancha.getArcoDerecho());
    }

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

    private double[] centroCabeza(Jugador jugador) {
        return new double[]{
                jugador.getPosicionX(),
                jugador.getPosicionY() + ParametrosJuego.OFFSET_CABEZA_JUGADOR
        };
    }

    private double[] centroBota(Jugador jugador) {
        double direccion = jugador.getLado() == Lado.IZQUIERDO ? 1 : -1;
        return new double[]{
                jugador.getPosicionX()
                        + direccion * ParametrosJuego.OFFSET_BOTA_X_JUGADOR,
                jugador.getPosicionY() + ParametrosJuego.OFFSET_BOTA_Y_JUGADOR
        };
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
     * Indica si un jugador tiene sus controles invertidos por el Flechazo.
     *
     * @param jugador número del jugador: 1 o 2
     * @return true si debe invertir sus teclas
     */
    public boolean tieneControlesInvertidos(int jugador) {
        return jugador(jugador).tieneControlesInvertidos();
    }

    public TiroPoder getTiroPoder() {
        return tiroPoder;
    }

    public void patear(int jugador) {
        if (!puedeJugar()) {
            return;
        }
        Jugador pateador = jugador(jugador);
        if (!pateador.puedePatear()) {
            return;
        }

        // Si la patada toca el balón, solo se impulsa el balón.
        if (pateador.patear(balon)) {
            return;
        }

        Jugador rival = jugador(jugador == 1 ? 2 : 1);
        if (rivalEstaAlAlcanceDePatada(pateador, rival)) {
            rival.recibirDanio(pateador.getEstadisticas().getDanoPatada());
        }
    }

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

    private double distancia(double[] punto1, double[] punto2) {
        double dx = punto2[0] - punto1[0];
        double dy = punto2[1] - punto1[1];
        return Math.sqrt(dx * dx + dy * dy);
    }

    public void sumarGol(int jugador) {
        marcador.sumarGol(jugador);
    }

    public void reiniciarDespuesDeGol() {
        tiroPoder = null;
        balon.reiniciar(cancha.getAncho() / 2.0, cancha.getAlto() / 2.0);
        jugador(1).reiniciar(cancha.getAncho() / 4.0, cancha.getSuelo() - ParametrosJuego.RADIO_JUGADOR);
        jugador(2).reiniciar(cancha.getAncho() * 3.0 / 4.0, cancha.getSuelo() - ParametrosJuego.RADIO_JUGADOR);
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
        terminar(MotivoFin.TIEMPO);
    }

    public void terminar(MotivoFin motivo) {
        if (motivo == null) {
            throw new IllegalArgumentException("El motivo de finalización no puede ser nulo");
        }
        if (estado == EstadoPartido.TERMINADO) {
            return;
        }
        estado = EstadoPartido.TERMINADO;
        resultado = new ResultadoPartido(marcador.getGoles(1), marcador.getGoles(2), motivo, tiempoTranscurrido);
        otorgarPuntos();
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

    public TipoPartido getTipo() {
        return tipo;
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

    public double getTiempoTotal() {
        return tiempoTotal;
    }

    public double getTiempoTranscurrido() {
        return tiempoTranscurrido;
    }

    public ReglaPartido getRegla() {
        return regla;
    }

    public Resolucion getResolucion() {
        return resolucion;
    }

    public Cancha getCancha() {
        return cancha;
    }

    public ResultadoPartido getResultado() {
        return resultado;
    }

    public int getPuntosJugador1() {
        return puntosJugador1;
    }

    public int getPuntosJugador2() {
        return puntosJugador2;
    }
}
