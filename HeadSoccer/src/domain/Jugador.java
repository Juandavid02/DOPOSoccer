package domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public abstract class Jugador {
    private final String nombre;

    private final Personaje personaje;
    private final Estadisticas estadisticas;
    private final Estadisticas estadisticasBase;
    private final Puntos puntos;
    private final BarraPoder barraPoder;
    private final Lado lado;

    private double posicionX;
    private double posicionY;
    private double velocidadX;
    private double velocidadY;
    private boolean enSuelo;
    private boolean aturdido;
    private double tiempoAturdimiento;

    private boolean auraActiva;
    private final List<Efecto> efectosActivos = new ArrayList<>();

    private final Vida vida;

    /**
     * Crea un jugador a partir de su configuración (nombre, estadísticas y
     * puntos) y del personaje elegido. Las estadísticas son del jugador, no
     * del personaje.
     *
     * @param configuracion configuración con nombre, estadísticas y puntos
     * @param personaje personaje elegido para el jugador
     * @param barraPoder barra de poder del jugador
     * @param lado lado de la cancha en el que juega
     * @param posicionX posición horizontal inicial
     * @param posicionY posición vertical inicial
     */
    public Jugador(ConfiguracionJugador configuracion, Personaje personaje,
                   BarraPoder barraPoder, Lado lado,
                   double posicionX, double posicionY) {
        if (configuracion == null) {
            throw new IllegalArgumentException("La configuración del jugador no puede ser nula");
        }
        if (personaje == null) {
            throw new IllegalArgumentException("El personaje no puede ser nulo");
        }
        if (barraPoder == null) {
            throw new IllegalArgumentException("La barra de poder no puede ser nula");
        }
        if (lado == null) {
            throw new IllegalArgumentException("El lado no puede ser nulo");
        }

        this.nombre = configuracion.getNombre();
        this.personaje = personaje;
        // Se trabaja con una copia para que los efectos del partido (por
        // ejemplo el Barril) no modifiquen las mejoras guardadas del perfil.
        this.estadisticas = configuracion.getEstadisticas().copiar();
        this.estadisticasBase = this.estadisticas.copiar();
        this.vida = new Vida(this.estadisticas.getVidaMax());
        this.puntos = configuracion.getPuntos();
        this.barraPoder = barraPoder;
        this.lado = lado;
        this.posicionX = posicionX;
        this.posicionY = posicionY;
        this.velocidadX = 0;
        this.velocidadY = 0;
        this.enSuelo = true;
    }

    /**
     * Ejecuta la lógica de control de este jugador.
     *
     * Las subclases deciden de dónde salen las acciones. En el jugador
     * humano la entrada se aplica desde la capa de presentación mediante
     * Partido; en la IA será la propia IA la que decida qué hacer.
     *
     * @param partido partido en el que se está jugando
     * @param dt tiempo transcurrido desde la última actualización
     */
    public abstract void actualizarControl(Partido partido, double dt);

    /**
     * Devuelve el nombre del jugador.
     *
     * @return nombre del jugador
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Devuelve el personaje del jugador.
     *
     * @return personaje elegido
     */
    public Personaje getPersonaje() {
        return personaje;
    }

    /**
     * Devuelve las estadísticas actuales del jugador.
     *
     * @return estadísticas del jugador
     */
    public Estadisticas getEstadisticas() {
        return estadisticas;
    }

    /**
     * Devuelve los puntos del jugador.
     *
     * @return puntos acumulados
     */
    public Puntos getPuntos() {
        return puntos;
    }

    /**
     * Devuelve la barra de poder del jugador.
     *
     * @return barra de poder
     */
    public BarraPoder getBarraPoder() {
        return barraPoder;
    }

    /**
     * Devuelve el lado de la cancha del jugador.
     *
     * @return lado del jugador
     */
    public Lado getLado() {
        return lado;
    }

    /**
     * Devuelve la vida del jugador.
     *
     * @return vida del jugador
     */
    public Vida getVida() {
        return vida;
    }

    /**
     * Devuelve la posición horizontal del jugador.
     *
     * @return posición en X
     */
    public double getPosicionX() {
        return posicionX;
    }

    /**
     * Devuelve la posición vertical del jugador.
     *
     * @return posición en Y
     */
    public double getPosicionY() {
        return posicionY;
    }

    /**
     * Devuelve la velocidad horizontal del jugador.
     *
     * @return velocidad en X
     */
    public double getVelocidadX() {
        return velocidadX;
    }

    /**
     * Devuelve la velocidad vertical del jugador.
     *
     * @return velocidad en Y
     */
    public double getVelocidadY() {
        return velocidadY;
    }

    /**
     * Indica si el jugador está apoyado en el suelo.
     *
     * @return true si está en el suelo
     */
    public boolean isEnSuelo() {
        return enSuelo;
    }

    /**
     * Indica si el jugador está aturdido.
     *
     * @return true si está aturdido
     */
    public boolean isAturdido() {
        return aturdido;
    }

    /**
     * Devuelve el tiempo de aturdimiento que le queda al jugador.
     *
     * @return segundos restantes de aturdimiento
     */
    public double getTiempoAturdimientoRestante() {
        return tiempoAturdimiento;
    }

    /**
     * Indica si el jugador tiene un aura activa.
     *
     * @return true si el aura está activa
     */
    public boolean isAuraActiva() {
        return auraActiva;
    }

    /**
     * Marca que el jugador tiene un aura de poder lista para ejecutarse.
     */
    public void activarAura() {
        auraActiva = true;
    }

    /**
     * Consume el aura porque el jugador ya tocó el balón.
     */
    public void consumirAura() {
        auraActiva = false;
    }

    /**
     * Devuelve el primer efecto activo. Se mantiene por compatibilidad; para
     * conocer todos los efectos simultáneos usa getEfectosActivos().
     *
     * @return primer efecto activo o null si no hay ninguno
     */
    public Efecto getEfectoActivo() {
        if (efectosActivos.isEmpty()) {
            return null;
        }
        return efectosActivos.get(0);
    }

    /**
     * Devuelve todos los efectos que sufre el jugador en este momento.
     *
     * @return lista de solo lectura con los efectos activos
     */
    public List<Efecto> getEfectosActivos() {
        return Collections.unmodifiableList(efectosActivos);
    }

    /**
     * Indica si el jugador tiene activo un efecto del tipo indicado.
     *
     * @param tipo tipo de efecto consultado
     * @return true si el efecto está activo
     */
    public boolean tieneEfecto(TipoEfecto tipo) {
        for (Efecto efecto : efectosActivos) {
            if (efecto.getTipo() == tipo) {
                return true;
            }
        }
        return false;
    }

    /**
     * Indica si el jugador tiene los controles invertidos.
     *
     * @return true si los controles están invertidos
     */
    public boolean tieneControlesInvertidos() {
        for (Efecto efecto : efectosActivos) {
            if (efecto.invierteControles()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Indica el multiplicador de velocidad actual del jugador. Cada efecto
     * activo puede frenarlo (por ejemplo, el Rayo lo deja a la mitad) y los
     * factores se multiplican entre sí.
     *
     * @return factor que multiplica la velocidad normal
     */
    public double getMultiplicadorVelocidad() {
        double multiplicador = 1.0;
        for (Efecto efecto : efectosActivos) {
            multiplicador *= efecto.getMultiplicadorVelocidad();
        }
        return multiplicador;
    }

    /**
     * Aplica un efecto de poder a partir de su tipo y su duración. Crea el
     * efecto correspondiente y lo aplica.
     *
     * @param tipo efecto que se aplica
     * @param duracion tiempo que dura el efecto
     */
    public void aplicarEfecto(TipoEfecto tipo, double duracion) {
        if (tipo == null || duracion <= 0) {
            return;
        }
        aplicarEfecto(tipo.crear(duracion));
    }

    /**
     * Aplica un efecto de poder. Los efectos se acumulan: si el jugador ya
     * tenía otro efecto, ambos permanecen activos hasta que termine su
     * tiempo. Si se vuelve a aplicar el mismo tipo, solo se renueva su
     * duración.
     *
     * @param nuevo efecto que se aplica
     */
    public void aplicarEfecto(Efecto nuevo) {
        if (nuevo == null) {
            return;
        }

        Efecto activo = null;
        for (Efecto efecto : efectosActivos) {
            if (efecto.getTipo() == nuevo.getTipo()) {
                activo = efecto;
                break;
            }
        }

        if (activo != null) {
            activo.renovar(nuevo.getTiempoRestante());
            activo.alAplicar(this);
        } else {
            efectosActivos.add(nuevo);
            nuevo.alAplicar(this);
        }
    }

    /**
     * Quita de inmediato todos los efectos activos y el aturdimiento del
     * jugador. Se usa al marcarse un gol para que nadie quede afectado en el
     * reinicio de la jugada.
     */
    public void limpiarEfectos() {
        for (Efecto efecto : efectosActivos) {
            efecto.alTerminar(this);
        }
        efectosActivos.clear();

        if (aturdido) {
            aturdido = false;
            tiempoAturdimiento = 0;
            vida.reiniciar();
        }
    }

    /**
     * Actualiza el temporizador de todos los efectos y retira los que
     * terminan.
     *
     * @param dt tiempo transcurrido desde la última actualización
     */
    private void actualizarEfecto(double dt) {
        Iterator<Efecto> iterador = efectosActivos.iterator();
        while (iterador.hasNext()) {
            Efecto efecto = iterador.next();
            efecto.actualizar(dt);
            if (efecto.terminado()) {
                iterador.remove();
                efecto.alTerminar(this);
            }
        }
    }

    /**
     * Le da una velocidad al jugador y lo deja en el aire. Lo usan los
     * efectos que lo lanzan (Empuje, Inconsciente).
     *
     * @param velocidadHorizontal velocidad horizontal nueva
     * @param velocidadVertical velocidad vertical nueva
     */
    void impulsar(double velocidadHorizontal, double velocidadVertical) {
        velocidadX = velocidadHorizontal;
        velocidadY = velocidadVertical;
        enSuelo = false;
    }

    /**
     * Frena el movimiento horizontal del jugador.
     */
    void detenerHorizontal() {
        velocidadX = 0;
    }

    /**
     * Deja al jugador como lo pide el efecto Barril: estadísticas en cero,
     * barra de poder reiniciada y vida máxima ajustada.
     */
    void reiniciarPorBarril() {
        estadisticas.restablecer();
        barraPoder.reiniciar();
        actualizarVidaMaxima();
    }

    /**
     * Restaura las estadísticas base del jugador.
     */
    void restaurarEstadisticas() {
        estadisticas.copiarDesde(estadisticasBase);
        actualizarVidaMaxima();
    }

    /**
     * Ajusta la vida máxima según las estadísticas actuales.
     */
    private void actualizarVidaMaxima() {
        vida.ajustarMaximaManteniendoProporcion(estadisticas.getVidaMax());
    }

    /**
     * Indica si el jugador puede desplazarse.
     *
     * @return false si está aturdido o algún efecto se lo impide
     */
    public boolean puedeMoverse() {
        if (aturdido) {
            return false;
        }
        for (Efecto efecto : efectosActivos) {
            if (efecto.bloqueaMovimiento()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Indica si el jugador puede saltar.
     *
     * @return false si está aturdido o algún efecto se lo impide
     */
    public boolean puedeSaltar() {
        if (aturdido) {
            return false;
        }
        for (Efecto efecto : efectosActivos) {
            if (efecto.bloqueaSalto()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Indica si el jugador puede patear.
     *
     * @return false si está aturdido o algún efecto se lo impide
     */
    public boolean puedePatear() {
        if (aturdido) {
            return false;
        }
        for (Efecto efecto : efectosActivos) {
            if (efecto.bloqueaPatada()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Aplica daño al jugador. Si su vida llega a cero queda aturdido y no
     * puede moverse, saltar ni patear durante unos segundos.
     *
     * @param danio cantidad de daño recibida
     */
    public void recibirDanio(double danio) {
        if (danio <= 0 || aturdido) {
            return;
        }

        vida.recibirDanio(danio);
        if (vida.estaEnCero()) {
            aturdir();
        }
    }

    /**
     * Deja al jugador aturdido durante el tiempo configurado.
     */
    public void aturdir() {
        aturdido = true;
        tiempoAturdimiento = ParametrosJuego.TIEMPO_ATURDIMIENTO;
        velocidadX = 0;
    }

    /**
     * Establece la velocidad horizontal del jugador.
     *
     * @param velocidadX nueva velocidad en X
     */
    public void setVelocidadX(double velocidadX) {
        this.velocidadX = velocidadX;
    }

    /**
     * Establece la velocidad vertical del jugador.
     *
     * @param velocidadY nueva velocidad en Y
     */
    public void setVelocidadY(double velocidadY) {
        this.velocidadY = velocidadY;
    }

    /**
     * Actualiza la física y los efectos del jugador durante un instante.
     *
     * @param dt tiempo transcurrido en segundos
     * @param cancha cancha donde se mueve el jugador
     */
    public void actualizar(double dt, Cancha cancha) {
        if (dt <= 0) {
            return;
        }

        actualizarEfecto(dt);

        if (aturdido) {
            tiempoAturdimiento -= dt;
            if (tiempoAturdimiento <= 0) {
                tiempoAturdimiento = 0;
                aturdido = false;
                vida.reiniciar();
            }
        }

        velocidadY += ParametrosJuego.GRAVEDAD * dt;
        posicionX += velocidadX * dt;
        posicionY += velocidadY * dt;
        double suelo = cancha.getSuelo();
        double ancho = cancha.getAncho();

        if (posicionY + ParametrosJuego.RADIO_JUGADOR >= suelo) {
            posicionY = suelo - ParametrosJuego.RADIO_JUGADOR;
            velocidadY = 0;
            enSuelo = true;
        } else {
            enSuelo = false;
        }

        if (posicionY - ParametrosJuego.RADIO_JUGADOR < 0) {
            posicionY = ParametrosJuego.RADIO_JUGADOR;
            if (velocidadY < 0) {
                velocidadY = 0;
            }
        }

        if (posicionX - ParametrosJuego.RADIO_JUGADOR < 0) {
            posicionX = ParametrosJuego.RADIO_JUGADOR;
            velocidadX = 0;
        } else if (posicionX + ParametrosJuego.RADIO_JUGADOR > ancho) {
            posicionX = ancho - ParametrosJuego.RADIO_JUGADOR;
            velocidadX = 0;
        }
    }

    /**
     * Mueve al jugador horizontalmente respetando los límites de la cancha.
     *
     * @param dx desplazamiento horizontal
     * @param cancha cancha donde se mueve el jugador
     */
    public void mover(double dx, Cancha cancha) {
        if (!puedeMoverse()) {
            return;
        }
        posicionX += dx;
        double ancho = cancha.getAncho();
        if (posicionX < ParametrosJuego.RADIO_JUGADOR) {
            posicionX = ParametrosJuego.RADIO_JUGADOR;
        } else if (posicionX > ancho - ParametrosJuego.RADIO_JUGADOR) {
            posicionX = ancho - ParametrosJuego.RADIO_JUGADOR;
        }
    }

    /**
     * Desplaza al jugador para resolver una colisión, respetando los
     * límites de la cancha.
     *
     * @param dx desplazamiento horizontal
     * @param dy desplazamiento vertical
     * @param cancha cancha donde se mueve
     */
    public void desplazar(double dx, double dy, Cancha cancha) {
        posicionX += dx;
        posicionY += dy;

        double ancho = cancha.getAncho();
        double suelo = cancha.getSuelo();

        if (posicionX < ParametrosJuego.RADIO_JUGADOR) {
            posicionX = ParametrosJuego.RADIO_JUGADOR;
        } else if (posicionX > ancho - ParametrosJuego.RADIO_JUGADOR) {
            posicionX = ancho - ParametrosJuego.RADIO_JUGADOR;
        }

        if (posicionY + ParametrosJuego.RADIO_JUGADOR >= suelo) {
            posicionY = suelo - ParametrosJuego.RADIO_JUGADOR;
            if (velocidadY > 0) {
                velocidadY = 0;
            }
            enSuelo = true;
        } else {
            enSuelo = false;
        }

        if (posicionY - ParametrosJuego.RADIO_JUGADOR < 0) {
            posicionY = ParametrosJuego.RADIO_JUGADOR;
            if (velocidadY < 0) {
                velocidadY = 0;
            }
        }
    }

    /**
     * Hace saltar al jugador si está en el suelo y puede saltar.
     */
    public void saltar() {
        if (!puedeSaltar() || !enSuelo) {
            return;
        }
        int salto = estadisticas.getJump();
        velocidadY = -(ParametrosJuego.IMPULSO_SALTO_BASE + salto * ParametrosJuego.IMPULSO_SALTO_POR_NIVEL);
        enSuelo = false;
    }

    /**
     * Intenta patear el balón si está al alcance.
     *
     * @param balon balón que se intenta patear
     * @return true si la patada tocó el balón
     */
    public boolean patear(Balon balon) {
        if (balon == null || !puedePatear()) {
            return false;
        }
        double direccion = -1;
        if (lado == Lado.IZQUIERDO) {
            direccion = 1;
        }
        double botaX = posicionX + direccion * ParametrosJuego.OFFSET_BOTA_X_JUGADOR;
        double botaY = posicionY + ParametrosJuego.OFFSET_BOTA_Y_JUGADOR;

        double distanciaX = balon.getX() - botaX;
        double distanciaY = balon.getY() - botaY;
        double distancia = Math.sqrt(distanciaX * distanciaX + distanciaY * distanciaY);

        if (distancia > ParametrosJuego.ALCANCE_PATADA) {
            return false;
        }

        balon.aplicarImpulso(
                direccion * ParametrosJuego.POTENCIA_PATADA_BASE,
                -ParametrosJuego.POTENCIA_LEVANTE_PATADA);
        return true;
    }

    /**
     * Reinicia la posición y la velocidad del jugador.
     *
     * @param posicionX nueva posición en X
     * @param posicionY nueva posición en Y
     */
    public void reiniciar(double posicionX, double posicionY) {
        velocidadX = 0;
        velocidadY = 0;
        enSuelo = true;
        this.posicionX = posicionX;
        this.posicionY = posicionY;
    }
}
