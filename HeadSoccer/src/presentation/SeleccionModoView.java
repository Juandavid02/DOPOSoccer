package presentation;

import javax.swing.*;
import java.awt.*;

/**
 * Pantalla del modo Arcade. Solo muestra los dos botones grandes para elegir
 * entre jugador contra jugador y jugador contra máquina.
 */
public class SeleccionModoView extends VistaBase {

    private static final String RUTA_BOTONES = "resources/images/Botones/";

    private final JButton botonPvsP;
    private final JButton botonPvsM;

    /**
     * Crea la vista del modo Arcade con sus botones de selección.
     */
    public SeleccionModoView() {
        super("resources/images/Ventanas/FondoMenu.jpg");
        setTitulo("SELECCIONAR MODO");

        botonPvsP = BotonImagen.crear(RUTA_BOTONES + "PvsP.png", 360);
        botonPvsM = BotonImagen.crear(RUTA_BOTONES + "PvsM.png", 360);

        JPanel botones = new JPanel(new GridLayout(1, 2, 40, 0));
        botones.setOpaque(false);
        botones.add(botonPvsP);
        botones.add(botonPvsM);

        JPanel centro = new JPanel(new GridBagLayout());
        centro.setOpaque(false);
        centro.add(botones);
        setCentroDirecto(centro);
    }

    /**
     * Devuelve el botón del modo jugador contra jugador.
     *
     * @return botón de PvsP
     */
    public JButton getBotonPvsP() {
        return botonPvsP;
    }

    /**
     * Devuelve el botón del modo jugador contra máquina.
     *
     * @return botón de PvsM
     */
    public JButton getBotonPvsM() {
        return botonPvsM;
    }
}
