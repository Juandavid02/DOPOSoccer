package presentation;

import domain.ExcepcionDOPOSoccer;
import domain.PerfilJugador;
import domain.Progreso;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.util.List;

/**
 * Controlador de la pantalla de gestión de progreso. Conecta los botones con
 * las operaciones de archivo del dominio y muestra los errores al usuario.
 */
public class GestionProgresoController {

    private static final String TITULO = "Gestionar progreso";
    private static final String EXTENSION_DAT = ".dat";
    private static final String EXTENSION_TXT = ".txt";

    private final DOPOSoccerGUI gui;
    private final GestionProgresoView view;
    private final List<PerfilJugador> perfiles;
    private File ultimaCarpeta;

    /**
     * Crea el controlador y conecta los botones de la vista.
     *
     * @param gui ventana principal
     * @param view vista de gestión de progreso
     * @param perfiles perfiles en memoria
     */
    public GestionProgresoController(DOPOSoccerGUI gui,
                                     GestionProgresoView view,
                                     List<PerfilJugador> perfiles) {
        this.gui = gui;
        this.view = view;
        this.perfiles = perfiles;
        prepareController();
    }

    /**
     * Conecta cada botón con su operación.
     */
    private void prepareController() {
        view.getBotonAtras().addActionListener(e -> gui.mostrarMenu());
        view.getBotonSonido().addActionListener(e -> gui.alternarSonido());
        view.getBotonGuardar().addActionListener(e -> guardarComo());
        view.getBotonAbrir().addActionListener(e -> abrir());
        view.getBotonExportar().addActionListener(e -> exportarComo());
        view.getBotonImportar().addActionListener(e -> importar());
    }

    /**
     * Refresca la pantalla con la cantidad actual de perfiles.
     */
    public void abrirPantalla() {
        view.actualizar(perfiles.size(),
                "Selecciona una operación para administrar el progreso.");
    }

    /**
     * Guarda el progreso completo en un archivo .dat.
     */
    private void guardarComo() {
        File archivo = elegirArchivo(true,
                "Guardar progreso",
                "Progreso de DOPOSoccer (*.dat)",
                EXTENSION_DAT);
        if (archivo == null) {
            return;
        }
        try {
            Progreso progreso = new Progreso(perfiles);
            progreso.saveAs(archivo);
            view.actualizar(perfiles.size(),
                    "Progreso guardado en " + archivo.getName());
            mostrarInformacion("Progreso guardado en:\n"
                    + archivo.getAbsolutePath());
        } catch (ExcepcionDOPOSoccer e) {
            mostrarError("Error al guardar el progreso", e);
        }
    }

    /**
     * Abre un progreso guardado en un archivo .dat y reemplaza los perfiles
     * que están en memoria.
     */
    private void abrir() {
        File archivo = elegirArchivo(false,
                "Abrir progreso",
                "Progreso de DOPOSoccer (*.dat)",
                EXTENSION_DAT);
        if (archivo == null) {
            return;
        }
        try {
            Progreso actual = new Progreso(perfiles);
            Progreso abierto = actual.open(archivo);
            reemplazarPerfiles(abierto.getPerfiles());
            view.actualizar(perfiles.size(),
                    "Progreso abierto desde " + archivo.getName());
            mostrarInformacion("Se abrieron "
                    + abierto.cantidadPerfiles() + " perfiles.");
        } catch (ExcepcionDOPOSoccer e) {
            mostrarError("Error al abrir el progreso", e);
        }
    }

    /**
     * Exporta los perfiles a un archivo de texto .txt.
     */
    private void exportarComo() {
        File archivo = elegirArchivo(true,
                "Exportar progreso",
                "Archivo de texto (*.txt)",
                EXTENSION_TXT);
        if (archivo == null) {
            return;
        }
        try {
            Progreso progreso = new Progreso(perfiles);
            progreso.exportAs(archivo);
            view.actualizar(perfiles.size(),
                    "Progreso exportado en " + archivo.getName());
            mostrarInformacion("Progreso exportado en:\n"
                    + archivo.getAbsolutePath());
        } catch (ExcepcionDOPOSoccer e) {
            mostrarError("Error al exportar el progreso", e);
        }
    }

    /**
     * Importa perfiles desde un archivo de texto .txt y reemplaza los
     * perfiles que están en memoria.
     */
    private void importar() {
        File archivo = elegirArchivo(false,
                "Importar progreso",
                "Archivo de texto (*.txt)",
                EXTENSION_TXT);
        if (archivo == null) {
            return;
        }
        try {
            Progreso importado = new Progreso(perfiles);
            importado.import_(archivo);
            reemplazarPerfiles(importado.getPerfiles());
            view.actualizar(perfiles.size(),
                    "Progreso importado desde " + archivo.getName());
            mostrarInformacion("Se importaron "
                    + importado.cantidadPerfiles() + " perfiles.");
        } catch (ExcepcionDOPOSoccer e) {
            mostrarError("Error al importar el progreso", e);
        }
    }

    /**
     * Reemplaza los perfiles en memoria conservando la misma lista para que
     * los demás controladores vean los cambios.
     *
     * @param nuevos perfiles que se cargan
     */
    private void reemplazarPerfiles(List<PerfilJugador> nuevos) {
        perfiles.clear();
        perfiles.addAll(nuevos);
    }

    /**
     * Abre un selector de archivos con el filtro indicado.
     *
     * @param guardar true para guardar, false para abrir
     * @param titulo título del diálogo
     * @param descripcion descripción del filtro
     * @param extension extensión permitida, con punto
     * @return archivo elegido con la extensión correcta, o null
     */
    private File elegirArchivo(boolean guardar, String titulo,
                               String descripcion, String extension) {
        JFileChooser selector = new JFileChooser();
        selector.setDialogTitle(titulo);
        selector.setFileFilter(new FileNameExtensionFilter(
                descripcion, extension.substring(1)));
        if (ultimaCarpeta != null) {
            selector.setCurrentDirectory(ultimaCarpeta);
        }

        int opcion = guardar
                ? selector.showSaveDialog(view)
                : selector.showOpenDialog(view);
        if (opcion != JFileChooser.APPROVE_OPTION) {
            return null;
        }

        File archivo = selector.getSelectedFile();
        if (archivo == null) {
            return null;
        }
        File carpeta = archivo.getAbsoluteFile().getParentFile();
        if (carpeta != null) {
            ultimaCarpeta = carpeta;
        }
        if (guardar) {
            archivo = conExtension(archivo, extension);
        }
        return archivo;
    }

    /**
     * Agrega la extensión al archivo si el usuario no la escribió.
     *
     * @param archivo archivo elegido
     * @param extension extensión esperada, con punto
     * @return archivo con la extensión correcta
     */
    private File conExtension(File archivo, String extension) {
        if (archivo.getName().toLowerCase().endsWith(extension)) {
            return archivo;
        }
        return new File(archivo.getParentFile(), archivo.getName() + extension);
    }

    /**
     * Muestra un error al usuario y guarda el detalle técnico en el log.
     *
     * @param contexto operación que falló
     * @param error error original
     */
    private void mostrarError(String contexto, ExcepcionDOPOSoccer error) {
        RegistroErrores.registrar(contexto, error);
        Dialogos.error(view, TITULO,
                error.getMessage()
                        + "\n\nDetalle técnico en " + RegistroErrores.getRutaArchivo());
    }

    /**
     * Muestra un mensaje informativo.
     *
     * @param mensaje texto que se muestra
     */
    private void mostrarInformacion(String mensaje) {
        Dialogos.informacion(view, TITULO, mensaje);
    }
}
