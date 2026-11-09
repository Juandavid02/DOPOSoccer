package presentation;

import domain.ExcepcionDOPOSoccer;
import domain.PerfilJugador;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class PerfilController {

    private final DOPOSoccerGUI gui;
    private final PerfilView view;
    private final List<PerfilJugador> perfiles;

    /**
     * Crea el controlador de la pantalla de perfiles.
     *
     * @param gui la ventana principal
     * @param view la vista de perfiles
     * @param perfiles la lista de perfiles en memoria
     */
    public PerfilController(DOPOSoccerGUI gui, PerfilView view, List<PerfilJugador> perfiles) {
        this.gui = gui;
        this.view = view;
        this.perfiles = perfiles;
        prepareController();
    }

    /**
     * Conecta los botones y la lista de perfiles con sus acciones.
     */
    private void prepareController() {
        view.getBotonAtras().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gui.volverDePerfil();
            }
        });
        view.getBotonCrear().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                crearPerfil();
            }
        });
        view.getBotonGuardar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarPerfil();
            }
        });
        view.getBotonEliminar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarPerfil();
            }
        });
        view.getBotonSonido().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gui.alternarSonido();
            }
        });
        view.getListaPerfiles().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                actualizarSeleccion();
            }
        });
    }

    /**
     * Vuelve a cargar la lista de perfiles en la vista.
     */
    public void refrescar() {
        view.mostrarPerfiles(perfiles);
        actualizarSeleccion();
    }

    /**
     * Muestra los datos del perfil que está seleccionado en la lista.
     */
    private void actualizarSeleccion() {
        int indice = view.getListaPerfiles().getSelectedIndex();
        if (indice >= 0 && indice < perfiles.size()) {
            view.mostrarPerfil(perfiles.get(indice));
        } else {
            view.limpiarFormulario();
        }
    }

    /**
     * Crea un perfil nuevo con el nombre escrito. Si el campo está vacío,
     * genera un nombre como "Perfil 1".
     */
    private void crearPerfil() {
        String nombre = view.getNombre();
        if (nombre.isEmpty()) {
            nombre = siguienteNombre();
        }

        if (nombreEnUso(nombre, null)) {
            mostrarError("Ya existe un perfil con el nombre \"" + nombre + "\".");
            return;
        }

        try {
            PerfilJugador perfil = new PerfilJugador(nombre);
            perfiles.add(perfil);

            view.mostrarPerfiles(perfiles);
            view.getListaPerfiles().setSelectedIndex(perfiles.size() - 1);
        } catch (ExcepcionDOPOSoccer e) {
            mostrarError(e.getMessage());
        }
    }

    /**
     * Guarda el nuevo nombre del perfil seleccionado.
     */
    private void guardarPerfil() {
        int indice = view.getListaPerfiles().getSelectedIndex();
        if (indice < 0 || indice >= perfiles.size()) {
            mostrarError("Selecciona un perfil para guardar.");
            return;
        }

        PerfilJugador perfil = perfiles.get(indice);
        String nombre = view.getNombre();

        if (nombre.isEmpty()) {
            mostrarError("El nombre del perfil no puede estar vacío.");
            return;
        }

        if (nombreEnUso(nombre, perfil)) {
            mostrarError("Ya existe un perfil con el nombre \"" + nombre + "\".");
            return;
        }

        try {
            perfil.setNombre(nombre);
        } catch (ExcepcionDOPOSoccer e) {
            mostrarError(e.getMessage());
            return;
        }

        view.mostrarPerfiles(perfiles);
        view.getListaPerfiles().setSelectedIndex(indice);

        JOptionPane.showMessageDialog(
                view,
                "Perfil guardado.",
                "Perfil",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Elimina el perfil seleccionado después de pedir confirmación.
     */
    private void eliminarPerfil() {
        int indice = view.getListaPerfiles().getSelectedIndex();
        if (indice < 0 || indice >= perfiles.size()) {
            mostrarError("Selecciona un perfil para eliminar.");
            return;
        }

        PerfilJugador perfil = perfiles.get(indice);
        int opcion = JOptionPane.showConfirmDialog(
                view,
                "¿Eliminar el perfil \"" + perfil.getNombre() + "\"?",
                "Eliminar perfil",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (opcion == JOptionPane.YES_OPTION) {
            perfiles.remove(indice);
            view.mostrarPerfiles(perfiles);
            actualizarSeleccion();
        }
    }

    /**
     * Busca un nombre libre de la forma "Perfil N".
     */
    private String siguienteNombre() {
        int numero = 1;
        while (nombreEnUso("Perfil " + numero, null)) {
            numero++;
        }
        return "Perfil " + numero;
    }

    /**
     * Indica si un nombre ya lo usa otro perfil.
     *
     * @param nombre el nombre a comprobar
     * @param excepto el perfil que se puede ignorar, o null
     */
    private boolean nombreEnUso(String nombre, PerfilJugador excepto) {
        for (PerfilJugador perfil : perfiles) {
            if (perfil != excepto && perfil.getNombre().equalsIgnoreCase(nombre)) {
                return true;
            }
        }
        return false;
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(
                view,
                mensaje,
                "Perfil",
                JOptionPane.WARNING_MESSAGE);
    }
}
