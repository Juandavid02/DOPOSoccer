package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import domain.Arco;
import domain.Balon;
import domain.Cancha;
import domain.ColisionesArcos;
import domain.ParametrosJuego;
import domain.Resolucion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Pruebas de los rebotes del balón contra las rampas y los travesaños.
 */
class ColisionesArcosTest {

    private Cancha cancha;
    private Arco izquierdo;
    private Arco derecho;

    /** Prepara una cancha con sus dos arcos. */
    @BeforeEach
    void setUp() {
        cancha = new Cancha(Resolucion.R1280x720);
        izquierdo = cancha.getArcoIzquierdo();
        derecho = cancha.getArcoDerecho();
    }

    /**
     * Crea un balón con velocidad inicial.
     *
     * @param x posición horizontal
     * @param y posición vertical
     * @param vx velocidad horizontal
     * @param vy velocidad vertical
     * @return balón listo para la prueba
     */
    private static Balon balon(double x, double y, double vx, double vy) {
        Balon balon = new Balon(x, y, ParametrosJuego.RADIO_BALON);
        balon.establecerVelocidad(vx, vy);
        return balon;
    }

    /**
     * Coloca el balón justo encima de la rampa de un arco.
     *
     * @param balon balón que se posiciona
     * @param arco arco de referencia
     * @param esIzquierdo true si es el arco izquierdo
     * @param separacion distancia a la rampa
     */
    private static void colocarSobreRampa(Balon balon, Arco arco,
            boolean esIzquierdo, double separacion) {
        double altoX = esIzquierdo ? arco.getX() : arco.getX() + arco.getAncho();
        double bajoX = esIzquierdo ? arco.getX() + arco.getAncho() : arco.getX();
        double altoY = arco.getY() - ParametrosJuego.ALTO_RAMPA_ARCO;
        double bajoY = arco.getY();

        double ladoX = bajoX - altoX;
        double ladoY = bajoY - altoY;
        double longitud = Math.hypot(ladoX, ladoY);
        double ux = ladoX / longitud;
        double uy = ladoY / longitud;

        double normalX = uy;
        double normalY = -ux;
        if (normalY > 0) {
            normalX = -normalX;
            normalY = -normalY;
        }

        double medioX = (altoX + bajoX) / 2.0;
        double medioY = (altoY + bajoY) / 2.0;
        double desfase = ParametrosJuego.RADIO_BALON_COLISION
                - ParametrosJuego.RADIO_BALON;

        balon.reubicar(medioX + normalX * separacion,
                medioY + normalY * separacion + desfase);
    }

    /** Un balón que cae fuerte sobre la rampa izquierda rebota. */
    @Test
    void rebotaAlCaerFuerteSobreLaRampaIzquierda() {
        Balon balon = balon(0, 0, 0, 300);
        colocarSobreRampa(balon, izquierdo, true, 5);
        double yAntes = balon.getY();

        ColisionesArcos.resolver(balon, cancha);

        assertTrue(balon.getY() < yAntes);
        assertTrue(balon.getVelocidadX() > 0);
        assertTrue(balon.getVelocidadY() < 0);
    }

    /** Un balón que cae fuerte sobre la rampa derecha rebota al otro lado. */
    @Test
    void rebotaAlCaerFuerteSobreLaRampaDerecha() {
        Balon balon = balon(0, 0, 0, 300);
        colocarSobreRampa(balon, derecho, false, 5);
        double yAntes = balon.getY();

        ColisionesArcos.resolver(balon, cancha);

        assertTrue(balon.getY() < yAntes);
        assertTrue(balon.getVelocidadX() < 0);
        assertTrue(balon.getVelocidadY() < 0);
    }

    /** Una caída lenta se resbala sin rebotar. */
    @Test
    void unaCaidaLentaNoRebota() {
        Balon balon = balon(0, 0, 0, 50);
        colocarSobreRampa(balon, izquierdo, true, 5);

        ColisionesArcos.resolver(balon, cancha);

        assertTrue(balon.getVelocidadY() > 0);
    }

    /** Si el balón está lejos de la rampa no se modifica. */
    @Test
    void noColisionaSiEstaLejosDeLaRampa() {
        Balon balon = balon(0, 0, 0, 300);
        colocarSobreRampa(balon, izquierdo, true,
                ParametrosJuego.RADIO_BALON_COLISION + 5);
        double x = balon.getX();
        double y = balon.getY();
        double vx = balon.getVelocidadX();
        double vy = balon.getVelocidadY();

        ColisionesArcos.resolver(balon, cancha);

        assertEquals(x, balon.getX(), 0.0001);
        assertEquals(y, balon.getY(), 0.0001);
        assertEquals(vx, balon.getVelocidadX(), 0.0001);
        assertEquals(vy, balon.getVelocidadY(), 0.0001);
    }

    /** Desde dentro del arco la rampa no actúa. */
    @Test
    void noColisionaDesdeAbajoDeLaRampa() {
        Balon balon = balon(izquierdo.getX() + 60,
                izquierdo.getY() + 100, 0, 300);
        double x = balon.getX();
        double y = balon.getY();
        double vx = balon.getVelocidadX();
        double vy = balon.getVelocidadY();

        ColisionesArcos.resolver(balon, cancha);

        assertEquals(x, balon.getX(), 0.0001);
        assertEquals(y, balon.getY(), 0.0001);
        assertEquals(vx, balon.getVelocidadX(), 0.0001);
        assertEquals(vy, balon.getVelocidadY(), 0.0001);
    }

    /** Si el balón se aleja de la rampa solo se corrige la posición. */
    @Test
    void siSeAlejaNoCambiaLaVelocidad() {
        Balon balon = balon(0, 0, 0, -300);
        colocarSobreRampa(balon, izquierdo, true, 5);
        double yAntes = balon.getY();

        ColisionesArcos.resolver(balon, cancha);

        assertTrue(balon.getY() < yAntes);
        assertEquals(0, balon.getVelocidadX(), 0.0001);
        assertEquals(-300, balon.getVelocidadY(), 0.0001);
    }

    /** Un balón que cae sobre el travesaño rebota hacia arriba. */
    @Test
    void rebotaAlCaerSobreElTravesano() {
        double borde = izquierdo.getY();
        Balon balon = balon(60, borde + 18, 0, 100);

        ColisionesArcos.resolver(balon, cancha);

        assertEquals(borde - 12, balon.getY(), 0.0001);
        assertTrue(balon.getVelocidadY() < 0);
    }

    /** Un balón que sube desde dentro del arco rebota hacia abajo. */
    @Test
    void rebotaAlSubirDesdeDentroDelArco() {
        double borde = izquierdo.getY();
        Balon balon = balon(60, borde + 18, 0, -100);

        ColisionesArcos.resolver(balon, cancha);

        assertEquals(borde + 48, balon.getY(), 0.0001);
        assertTrue(balon.getVelocidadY() > 0);
    }

    /** Fuera del ancho del arco el travesaño no interviene. */
    @Test
    void elTravesanoNoAfectaFueraDelArco() {
        double borde = izquierdo.getY();
        Balon balon = balon(640, borde + 18, 0, 100);

        ColisionesArcos.resolver(balon, cancha);

        assertEquals(640, balon.getX(), 0.0001);
        assertEquals(borde + 18, balon.getY(), 0.0001);
        assertEquals(100, balon.getVelocidadY(), 0.0001);
    }

    /** Un balón quieto sobre el travesaño no se mueve. */
    @Test
    void elTravesanoNoAfectaUnBalonQuieto() {
        double borde = izquierdo.getY();
        Balon balon = balon(60, borde + 18, 0, 0);

        ColisionesArcos.resolver(balon, cancha);

        assertEquals(borde + 18, balon.getY(), 0.0001);
        assertEquals(0, balon.getVelocidadY(), 0.0001);
    }
}
