package presentation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Pantalla para guardar, abrir, exportar e importar el progreso de los
 * perfiles. Sigue la estructura del laboratorio: botones que delegan en el
 * controlador, que a su vez usa las operaciones de archivo del dominio.
 */
public class GestionProgresoView extends VistaBase {

    private static final Color TEXTO = new Color(22, 48, 68);
    private static final Color TEXTO_SUAVE = new Color(45, 78, 102);
    private static final Color AZUL = new Color(38, 108, 148);

    private final BotonTexto botonGuardar;
    private final BotonTexto botonAbrir;
    private final BotonTexto botonExportar;
    private final BotonTexto botonImportar;
    private final JLabel etiquetaPerfiles;
    private final JLabel etiquetaMensaje;

    /**
     * Crea la pantalla de gestión de progreso.
     */
    public GestionProgresoView() {
        super("resources/images/Ventanas/FondoPerfil.png");
        setTitulo("GESTIONAR PROGRESO");

        botonGuardar = new BotonTexto("GUARDAR COMO (.dat)", AZUL);
        botonAbrir = new BotonTexto("ABRIR (.dat)", new Color(38, 132, 96));
        botonExportar = new BotonTexto("EXPORTAR COMO (.txt)",
                new Color(176, 118, 30));
        botonImportar = new BotonTexto("IMPORTAR (.txt)",
                new Color(140, 70, 160));

        etiquetaPerfiles = new JLabel("Perfiles en memoria: 0",
                SwingConstants.CENTER);
        etiquetaPerfiles.setFont(new Font("SansSerif", Font.BOLD, 20));
        etiquetaPerfiles.setForeground(TEXTO);

        etiquetaMensaje = new JLabel(
                "Selecciona una operación para administrar el progreso.",
                SwingConstants.CENTER);
        etiquetaMensaje.setFont(new Font("SansSerif", Font.PLAIN, 16));
        etiquetaMensaje.setForeground(TEXTO_SUAVE);

        prepareViewCentro();
    }

    /**
     * Construye la tarjeta central con las cuatro operaciones.
     */
    private void prepareViewCentro() {
        TarjetaPanel tarjeta = new TarjetaPanel();
        tarjeta.setLayout(new BorderLayout(0, 18));
        tarjeta.setBorder(new EmptyBorder(24, 36, 22, 36));
        tarjeta.setPreferredSize(new Dimension(820, 540));
        tarjeta.setMinimumSize(new Dimension(720, 480));

        JLabel titulo = new JLabel("ARCHIVOS DE PROGRESO",
                SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 26));
        titulo.setForeground(TEXTO);

        JLabel descripcion = new JLabel(
                "<html><div style='text-align:center; width:640px;'>"
                        + "Guardar y abrir conservan el progreso completo "
                        + "(.dat). Exportar e importar usan un archivo de "
                        + "texto (.txt) con una línea por dato.</div></html>",
                SwingConstants.CENTER);
        descripcion.setFont(new Font("SansSerif", Font.PLAIN, 17));
        descripcion.setForeground(TEXTO_SUAVE);

        JPanel encabezado = new JPanel(new BorderLayout(0, 8));
        encabezado.setOpaque(false);
        encabezado.add(titulo, BorderLayout.NORTH);
        encabezado.add(descripcion, BorderLayout.CENTER);

        JPanel botones = new JPanel(new GridLayout(2, 2, 18, 18));
        botones.setOpaque(false);
        botones.add(botonGuardar);
        botones.add(botonAbrir);
        botones.add(botonExportar);
        botones.add(botonImportar);

        JPanel pie = new JPanel(new BorderLayout(0, 6));
        pie.setOpaque(false);
        pie.add(etiquetaPerfiles, BorderLayout.NORTH);
        pie.add(etiquetaMensaje, BorderLayout.CENTER);

        tarjeta.add(encabezado, BorderLayout.NORTH);
        tarjeta.add(botones, BorderLayout.CENTER);
        tarjeta.add(pie, BorderLayout.SOUTH);

        setCentro(tarjeta, 20, 34);
    }

    /**
     * Actualiza el estado que ve el usuario.
     *
     * @param cantidadPerfiles cantidad de perfiles en memoria
     * @param mensaje resultado de la última operación
     */
    public void actualizar(int cantidadPerfiles, String mensaje) {
        etiquetaPerfiles.setText("Perfiles en memoria: " + cantidadPerfiles);
        etiquetaMensaje.setText(mensaje);
    }

    /**
     * Devuelve el botón para guardar el progreso.
     *
     * @return botón de guardar como
     */
    public JButton getBotonGuardar() {
        return botonGuardar;
    }

    /**
     * Devuelve el botón para abrir un progreso.
     *
     * @return botón de abrir
     */
    public JButton getBotonAbrir() {
        return botonAbrir;
    }

    /**
     * Devuelve el botón para exportar el progreso.
     *
     * @return botón de exportar como
     */
    public JButton getBotonExportar() {
        return botonExportar;
    }

    /**
     * Devuelve el botón para importar un progreso.
     *
     * @return botón de importar
     */
    public JButton getBotonImportar() {
        return botonImportar;
    }
}
