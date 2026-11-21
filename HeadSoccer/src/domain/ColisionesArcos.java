package domain;

/**
 * Resuelve los rebotes del balón contra los arcos: la rampa invisible sobre
 * cada arco y el travesaño.
 *
 * No guarda estado; recibe el balón y la cancha en cada llamada.
 */
public final class ColisionesArcos {

    private ColisionesArcos() {
    }

    /**
     * Hace rebotar el balón contra las rampas y los travesaños de los arcos.
     *
     * @param balon balón del partido
     * @param cancha cancha con los arcos
     */
    public static void resolver(Balon balon, Cancha cancha) {
        resolverRampa(balon, cancha.getArcoIzquierdo(), true);
        resolverRampa(balon, cancha.getArcoDerecho(), false);
        resolverTravesano(balon, cancha.getArcoIzquierdo());
        resolverTravesano(balon, cancha.getArcoDerecho());
    }

    /**
     * Maneja el choque del balón con la rampa invisible encima del arco.
     * Si cae fuerte rebota; si cae despacio se resbala hacia la cancha.
     */
    private static void resolverRampa(Balon balon, Arco arco, boolean arcoIzquierdo) {
        double radio = ParametrosJuego.RADIO_BALON_COLISION;
        double desfase = ParametrosJuego.RADIO_BALON_COLISION - ParametrosJuego.RADIO_BALON;
        double centroX = balon.getX();
        double centroY = balon.getY() - desfase;

        // Extremo alto (contra la pared) y extremo bajo (hacia la cancha).
        double altoX;
        double bajoX;
        if (arcoIzquierdo) {
            altoX = arco.getX();
            bajoX = arco.getX() + arco.getAncho();
        } else {
            altoX = arco.getX() + arco.getAncho();
            bajoX = arco.getX();
        }
        double altoY = arco.getY() - ParametrosJuego.ALTO_RAMPA_ARCO;
        double bajoY = arco.getY();

        double ladoX = bajoX - altoX;
        double ladoY = bajoY - altoY;
        double longitud = Math.sqrt(ladoX * ladoX + ladoY * ladoY);
        double ux = ladoX / longitud;
        double uy = ladoY / longitud;

        // Normal de la rampa apuntando hacia arriba.
        double normalRampaX = uy;
        double normalRampaY = -ux;
        if (normalRampaY > 0) {
            normalRampaX = -normalRampaX;
            normalRampaY = -normalRampaY;
        }

        // Solo se choca por encima de la rampa; desde dentro del arco manda
        // el travesaño.
        double lado = (centroX - altoX) * normalRampaX + (centroY - altoY) * normalRampaY;
        if (lado <= 0) {
            return;
        }

        double t = ((centroX - altoX) * ux + (centroY - altoY) * uy) / longitud;
        t = Math.max(0, Math.min(1, t));
        double puntoX = altoX + t * ladoX;
        double puntoY = altoY + t * ladoY;

        double dx = centroX - puntoX;
        double dy = centroY - puntoY;
        double distancia = Math.sqrt(dx * dx + dy * dy);
        if (distancia >= radio) {
            return;
        }

        double normalX = normalRampaX;
        double normalY = normalRampaY;
        if (distancia > 0.0001) {
            normalX = dx / distancia;
            normalY = dy / distancia;
        }

        balon.reubicar(puntoX + normalX * radio,
                puntoY + normalY * radio + desfase);

        double velocidadNormal = balon.getVelocidadX() * normalX
                + balon.getVelocidadY() * normalY;
        if (velocidadNormal >= 0) {
            return;
        }

        double restitucion = ParametrosJuego.REBOTE;
        if (-velocidadNormal < ParametrosJuego.UMBRAL_REBOTE_RAMPA) {
            // Sin rebote: solo se quita la parte que entra en la rampa y el
            // resto de la velocidad lo hace resbalar.
            restitucion = 0;
        }
        double impulso = -(1 + restitucion) * velocidadNormal;
        balon.aplicarImpulso(impulso * normalX, impulso * normalY);
    }

    /**
     * Resuelve el choque del balón con el travesaño de un arco.
     */
    private static void resolverTravesano(Balon balon, Arco arco) {
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
                && centroVisualY >= bordeSuperior
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
}
