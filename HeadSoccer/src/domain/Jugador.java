package domain;

public class Jugador {
    private final String nombre;

    private final Personaje personaje;
    private final Estadisticas estadisticas;
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

    private final Vida vida;

    /**
     * Crea un jugador a partir de su configuración (nombre, estadísticas y
     * puntos) y del personaje elegido. Las estadísticas son del jugador, no
     * del personaje.
     */
    public Jugador(ConfiguracionJugador configuracion, Personaje personaje, BarraPoder barraPoder, Lado lado, double posicionX, double posicionY) {
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
        this.estadisticas = configuracion.getEstadisticas();
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

    public String getNombre() {
        return nombre;
    }

    public Personaje getPersonaje() {
        return personaje;
    }

    public Estadisticas getEstadisticas() {
        return estadisticas;
    }

    public Puntos getPuntos() {
        return puntos;
    }

    public BarraPoder getBarraPoder() {
        return barraPoder;
    }

    public Lado getLado() {
        return lado;
    }

    public Vida getVida() {
        return vida;
    }

    public double getPosicionX() {
        return posicionX;
    }

    public double getPosicionY() {
        return posicionY;
    }

    public double getVelocidadX() {
        return velocidadX;
    }

    public double getVelocidadY() {
        return velocidadY;
    }

    public boolean isEnSuelo() {
        return enSuelo;
    }

    public boolean isAturdido() {
        return aturdido;
    }

    public double getTiempoAturdimientoRestante() {
        return tiempoAturdimiento;
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

    public void setVelocidadX(double velocidadX) {
        this.velocidadX = velocidadX;
    }

    public void setVelocidadY(double velocidadY) {
        this.velocidadY = velocidadY;
    }

    public void actualizar(double dt, Cancha cancha) {
        if (dt <= 0) {
            return;
        }

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

    public void mover(double dx, Cancha cancha) {
        if (aturdido) {
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

    public void saltar() {
        if (aturdido || !enSuelo) {
            return;
        }
        int salto = estadisticas.getJump();
        velocidadY = -(ParametrosJuego.IMPULSO_SALTO_BASE + salto * ParametrosJuego.IMPULSO_SALTO_POR_NIVEL);
        enSuelo = false;
    }

    public boolean patear(Balon balon) {
        if (balon == null || aturdido) {
            return false;
        }
        double direccion = lado == Lado.IZQUIERDO ? 1 : -1;
        double botaX = posicionX
                + direccion * ParametrosJuego.OFFSET_BOTA_X_JUGADOR;
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

    public void reiniciar(double posicionX, double posicionY) {
        velocidadX = 0;
        velocidadY = 0;
        enSuelo = true;
        this.posicionX = posicionX;
        this.posicionY = posicionY;
    }
}
