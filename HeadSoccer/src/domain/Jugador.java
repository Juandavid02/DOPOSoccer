package domain;

public class Jugador {
    private final String nombre;

    private final Personaje personaje;
    private final BarraPoder barraPoder;
    private final Lado lado;

    private double posicionX;
    private double posicionY;
    private double velocidadX;
    private double velocidadY;
    private boolean enSuelo;

    public Jugador(String nombre, Personaje personaje, BarraPoder barraPoder, Lado lado, double posicionX, double posicionY) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede ser nulo o vacío");
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

        this.nombre = nombre;
        this.personaje = personaje;
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

    public BarraPoder getBarraPoder() {
        return barraPoder;
    }

    public Lado getLado() {
        return lado;
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

    public void mover(double dx) {
        posicionX += dx;
        if (posicionX < ParametrosJuego.RADIO_JUGADOR) {
            posicionX = ParametrosJuego.RADIO_JUGADOR;
        } else if (posicionX > ParametrosJuego.ANCHO_CANCHA - ParametrosJuego.RADIO_JUGADOR) {
            posicionX = ParametrosJuego.ANCHO_CANCHA - ParametrosJuego.RADIO_JUGADOR;
        }
    }

    public void saltar() {
        if (!enSuelo) {
            return;
        }
        int salto = personaje.getEstadisticas().getSalto();
        velocidadY = -(ParametrosJuego.IMPULSO_SALTO_BASE + salto * ParametrosJuego.IMPULSO_SALTO_POR_NIVEL);
        enSuelo = false;
    }

    public void patear(Balon balon) {
        if (balon == null) {
            return;
        }

        double distanciaX = balon.getX() - posicionX;
        double distanciaY = balon.getY() - posicionY;
        double distancia = Math.sqrt(distanciaX * distanciaX + distanciaY * distanciaY);
        double alcance = ParametrosJuego.RADIO_BALON + ParametrosJuego.RADIO_JUGADOR;
        if (distancia > alcance || distancia == 0) {
            return;
        }
        int fuerza = personaje.getEstadisticas().getFuerza();
        double potencia = ParametrosJuego.POTENCIA_PATADA_BASE + fuerza * ParametrosJuego.POTENCIA_PATADA_POR_NIVEL;
        // IA generativo ayuda a calcular el ángulo de la patada para que sea más realista
        double angulo = Math.atan2(distanciaY, distanciaX);
        balon.aplicarImpulso(potencia * Math.cos(angulo),potencia * Math.sin(angulo)
        );        
    }

    public void reiniciar(double posicionX, double posicionY) {
        velocidadX = 0;
        velocidadY = 0;
        enSuelo = true;
        this.posicionX = posicionX;
        this.posicionY = posicionY;
    }
}
