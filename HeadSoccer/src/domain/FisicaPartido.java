package domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Resuelve las colisiones entre jugadores y entre el balón y los jugadores.
 *
 * La clase no guarda estado: recibe las entidades y la cancha en cada
 * llamada. Así la física se puede leer y probar sin depender del estado
 * completo de un partido.
 */
public final class FisicaPartido {

    private static final double VELOCIDAD_SEPARACION_DISPUTA = 700;
    private static final double TOLERANCIA_CONTACTO = 2;

    private FisicaPartido() {
    }

    /**
     * Resuelve los choques entre los dos personajes usando sus dos círculos:
     * cabeza y bota.
     *
     * @param jugadores jugadores del partido
     * @param cancha cancha donde se mueven
     */
    public static void resolverJugadores(List<Jugador> jugadores, Cancha cancha) {
        Jugador jugador1 = jugadores.get(0);
        Jugador jugador2 = jugadores.get(1);

        // Si uno saltó por encima del otro, se permite el paso.
        if (pasaPorEncima(jugador1, jugador2) || pasaPorEncima(jugador2, jugador1)) {
            return;
        }

        for (int iteracion = 0; iteracion < 3; iteracion++) {
            resolverCirculosJugadores(jugador1, jugador2, centroCabeza(jugador1), ParametrosJuego.RADIO_CABEZA_JUGADOR, centroCabeza(jugador2), ParametrosJuego.RADIO_CABEZA_JUGADOR, cancha);

            resolverCirculosJugadores(jugador1, jugador2,
                    centroCabeza(jugador1), ParametrosJuego.RADIO_CABEZA_JUGADOR,
                    centroBota(jugador2), ParametrosJuego.RADIO_BOTA_JUGADOR,
                    cancha);

            resolverCirculosJugadores(jugador1, jugador2,
                    centroBota(jugador1), ParametrosJuego.RADIO_BOTA_JUGADOR,
                    centroCabeza(jugador2), ParametrosJuego.RADIO_CABEZA_JUGADOR,
                    cancha);

            resolverCirculosJugadores(jugador1, jugador2,
                    centroBota(jugador1), ParametrosJuego.RADIO_BOTA_JUGADOR,
                    centroBota(jugador2), ParametrosJuego.RADIO_BOTA_JUGADOR,
                    cancha);
        }
    }

    /**
     * Indica si un jugador está lo bastante alto como para pasar por encima
     * del otro sin chocar.
     *
     * @param arriba jugador que podría estar saltando por encima
     * @param abajo jugador que quedaría debajo
     * @return true si la bota del primero está por encima de la cabeza del segundo
     */
    public static boolean pasaPorEncima(Jugador arriba, Jugador abajo) {
        return arriba.getBordeInferiorBota() <= abajo.getBordeSuperiorCabeza();
    }

    /**
     * Resuelve la separación entre dos círculos de dos jugadores.
     */
    private static void resolverCirculosJugadores(Jugador jugador1, Jugador jugador2,
                                                  double[] circulo1, double radio1,
                                                  double[] circulo2, double radio2,
                                                  Cancha cancha) {
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
     * Resuelve el choque entre el balón y un círculo del jugador.
     *
     * @return true si el balón tocó el círculo
     */
    public static boolean resolverBalonCirculo(Balon balon, Jugador jugador,
                                               Cancha cancha, double centroX,
                                               double centroY, double radio) {
        double dx = balon.getX() - centroX;
        double dy = balon.getY() - centroY;
        double distancia = Math.sqrt(dx * dx + dy * dy);
        double distanciaMinima = ParametrosJuego.RADIO_BALON_COLISION + radio;

        if (distancia >= distanciaMinima) {
            return false;
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

        return true;
    }

    /**
     * Hace que el balón rebote al chocar con la cabeza o la bota de los
     * jugadores.
     *
     * @param jugadores jugadores del partido
     * @param balon balón del partido
     * @param cancha cancha donde se mueve el balón
     * @return jugadores que tocaron el balón durante el instante
     */
    public static List<Jugador> resolverBalonJugadores(List<Jugador> jugadores,
                                                       Balon balon,
                                                       Cancha cancha) {
        List<Jugador> contactos = new ArrayList<>();

        for (int iteracion = 0; iteracion < 2; iteracion++) {
            for (Jugador jugador : jugadores) {
                // La cabeza solo choca con balones que estén a su altura;
                // un balón rodando por el suelo no debe subir a la cabeza.
                if (balon.getY() < jugador.getBotaY()
                        && resolverBalonCirculo(balon, jugador, cancha,
                                jugador.getCabezaX(), jugador.getCabezaY(),
                                ParametrosJuego.RADIO_CABEZA_JUGADOR)) {
                    contactos.add(jugador);
                }

                if (resolverBalonCirculo(balon, jugador, cancha,
                        jugador.getBotaX(), jugador.getBotaY(),
                        ParametrosJuego.RADIO_BOTA_JUGADOR)) {
                    contactos.add(jugador);
                }
            }
        }

        return contactos;
    }

    /**
     * Cuando el balón queda apretado entre los dos jugadores, los separa un
     * poco y lo sube para que no se quede pegado.
     */
    public static void resolverBalonAtrapado(Jugador jugador1, Jugador jugador2,
                                             Balon balon, Cancha cancha,
                                             double dt) {
        Jugador izquierdo = jugador1;
        Jugador derecho = jugador2;
        if (izquierdo.getPosicionX() > derecho.getPosicionX()) {
            izquierdo = jugador2;
            derecho = jugador1;
        }

        double xIzquierdo = izquierdo.getPosicionX();
        double xDerecho = derecho.getPosicionX();
        boolean entre = balon.getX() > xIzquierdo && balon.getX() < xDerecho;
        if (!entre || !balonToca(izquierdo, balon) || !balonToca(derecho, balon)) {
            return;
        }

        // Los personajes se abren hasta que el balón pueda pasar entre las
        // cabezas.
        double separacionNecesaria = 2 * (ParametrosJuego.RADIO_CABEZA_JUGADOR
                + ParametrosJuego.RADIO_BALON_COLISION);
        double faltante = separacionNecesaria - (xDerecho - xIzquierdo);
        if (faltante > 0) {
            double paso = Math.min(faltante / 2, VELOCIDAD_SEPARACION_DISPUTA * dt);
            izquierdo.desplazar(-paso, 0, cancha);
            derecho.desplazar(paso, 0, cancha);
        }

        // El balón sale hacia arriba.
        if (balon.getVelocidadY() > -ParametrosJuego.POTENCIA_LEVANTE_PATADA) {
            balon.establecerVelocidad(balon.getVelocidadX(),
                    -ParametrosJuego.POTENCIA_LEVANTE_PATADA);
        }
    }

    /**
     * Mira si el balón está tocando la bota o la cabeza del jugador.
     */
    private static boolean balonToca(Jugador jugador, Balon balon) {
        double distanciaBota = Math.hypot(balon.getX() - jugador.getBotaX(),
                balon.getY() - jugador.getBotaY());
        if (distanciaBota <= ParametrosJuego.RADIO_BALON_COLISION
                + ParametrosJuego.RADIO_BOTA_JUGADOR + TOLERANCIA_CONTACTO) {
            return true;
        }
        if (balon.getY() < jugador.getBotaY()) {
            double distanciaCabeza = Math.hypot(balon.getX() - jugador.getCabezaX(),
                    balon.getY() - jugador.getCabezaY());
            return distanciaCabeza <= ParametrosJuego.RADIO_BALON_COLISION
                    + ParametrosJuego.RADIO_CABEZA_JUGADOR + TOLERANCIA_CONTACTO;
        }
        return false;
    }

    private static double[] centroCabeza(Jugador jugador) {
        return new double[]{jugador.getCabezaX(), jugador.getCabezaY()};
    }

    private static double[] centroBota(Jugador jugador) {
        return new double[]{jugador.getBotaX(), jugador.getBotaY()};
    }
}
