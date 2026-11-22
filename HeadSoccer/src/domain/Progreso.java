package domain;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InvalidClassException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Progreso de DOPOSoccer: los perfiles con sus puntos, estadísticas y
 * desbloqueos. Ofrece las cuatro operaciones básicas de archivos del
 * laboratorio: guardar como, abrir, exportar como e importar.
 *
 * Guardar y abrir usan un archivo binario .dat (serialización del objeto).
 * Exportar e importar usan un archivo de texto .txt con una línea por ítem,
 * igual que el laboratorio: el tipo del dato y sus campos separados por ";".
 */
public class Progreso implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final String EXTENSION_DAT = ".dat";
    private static final String EXTENSION_TXT = ".txt";
    private static final String CABECERA = "DOPOSOCCER;1";

    private static final String PERFIL = "PERFIL";
    private static final String DESBLOQUEO = "DESBLOQUEO";
    private static final String PERSONAJE = "PERSONAJE";
    private static final String BALON = "BALON";
    private static final String ESCENARIO = "ESCENARIO";

    private final List<PerfilJugador> perfiles;

    /**
     * Crea un progreso vacío.
     */
    public Progreso() {
        this.perfiles = new ArrayList<>();
    }

    /**
     * Crea un progreso con los perfiles indicados.
     *
     * @param perfiles perfiles que forman el progreso
     * @throws IllegalArgumentException si la lista es nula
     */
    public Progreso(List<PerfilJugador> perfiles) {
        if (perfiles == null) {
            throw new IllegalArgumentException("La lista de perfiles no puede ser nula");
        }
        this.perfiles = new ArrayList<>();
        for (PerfilJugador perfil : perfiles) {
            if (perfil != null) {
                this.perfiles.add(perfil);
            }
        }
    }

    /**
     * Devuelve los perfiles del progreso.
     *
     * @return lista de perfiles sin posibilidad de modificación
     */
    public List<PerfilJugador> getPerfiles() {
        return Collections.unmodifiableList(perfiles);
    }

    /**
     * Devuelve cuántos perfiles tiene el progreso.
     *
     * @return cantidad de perfiles
     */
    public int cantidadPerfiles() {
        return perfiles.size();
    }

    /**
     * Guarda el progreso completo en un archivo binario .dat.
     *
     * @param archivo archivo donde se guarda
     * @throws ExcepcionDOPOSoccer si el archivo es inválido o falla la escritura
     */
    public void saveAs(File archivo) throws ExcepcionDOPOSoccer {
        if (archivo == null) {
            throw new ExcepcionDOPOSoccer(ExcepcionDOPOSoccer.SAVE_ARCHIVO_NULO);
        }
        if (archivo.isDirectory()) {
            throw new ExcepcionDOPOSoccer(ExcepcionDOPOSoccer.SAVE_ES_CARPETA);
        }
        if (!tieneExtension(archivo, EXTENSION_DAT)) {
            throw new ExcepcionDOPOSoccer(ExcepcionDOPOSoccer.SAVE_EXTENSION_INVALIDA);
        }
        File carpeta = archivo.getAbsoluteFile().getParentFile();
        if (carpeta == null || !carpeta.isDirectory()) {
            throw new ExcepcionDOPOSoccer(ExcepcionDOPOSoccer.SAVE_CARPETA_NO_EXISTE);
        }
        if (archivo.exists() ? !archivo.canWrite() : !carpeta.canWrite()) {
            throw new ExcepcionDOPOSoccer(ExcepcionDOPOSoccer.SAVE_SIN_PERMISO);
        }
        try (ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream(archivo))) {
            salida.writeObject(this);
        } catch (NotSerializableException e) {
            throw new ExcepcionDOPOSoccer(
                    ExcepcionDOPOSoccer.SAVE_NO_SERIALIZABLE, e);
        } catch (IOException e) {
            throw new ExcepcionDOPOSoccer(ExcepcionDOPOSoccer.SAVE_ERROR, e);
        }
    }

    /**
     * Abre un progreso guardado en un archivo binario .dat.
     *
     * @param archivo archivo que se quiere leer
     * @return progreso leído del archivo
     * @throws ExcepcionDOPOSoccer si el archivo es inválido o falla la lectura
     */
    public Progreso open(File archivo) throws ExcepcionDOPOSoccer {
        if (archivo == null) {
            throw new ExcepcionDOPOSoccer(ExcepcionDOPOSoccer.OPEN_ARCHIVO_NULO);
        }
        if (archivo.isDirectory()) {
            throw new ExcepcionDOPOSoccer(ExcepcionDOPOSoccer.OPEN_ES_CARPETA);
        }
        if (!archivo.exists()) {
            throw new ExcepcionDOPOSoccer(ExcepcionDOPOSoccer.OPEN_NO_EXISTE);
        }
        if (!tieneExtension(archivo, EXTENSION_DAT)) {
            throw new ExcepcionDOPOSoccer(ExcepcionDOPOSoccer.OPEN_EXTENSION_INVALIDA);
        }
        if (!archivo.canRead()) {
            throw new ExcepcionDOPOSoccer(ExcepcionDOPOSoccer.OPEN_SIN_PERMISO);
        }
        try (ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(archivo))) {
            Object leido = entrada.readObject();
            if (!(leido instanceof Progreso)) {
                throw new ExcepcionDOPOSoccer(
                        ExcepcionDOPOSoccer.OPEN_NO_ES_PROGRESO);
            }
            return (Progreso) leido;
        } catch (InvalidClassException e) {
            throw new ExcepcionDOPOSoccer(
                    ExcepcionDOPOSoccer.OPEN_VERSION_INCOMPATIBLE, e);
        } catch (StreamCorruptedException | EOFException e) {
            throw new ExcepcionDOPOSoccer(
                    ExcepcionDOPOSoccer.OPEN_NO_ES_PROGRESO, e);
        } catch (ClassNotFoundException e) {
            throw new ExcepcionDOPOSoccer(
                    ExcepcionDOPOSoccer.OPEN_CLASE_DESCONOCIDA, e);
        } catch (IOException e) {
            throw new ExcepcionDOPOSoccer(ExcepcionDOPOSoccer.OPEN_ERROR, e);
        }
    }

    /**
     * Exporta el progreso a un archivo de texto .txt.
     * Escribe una línea por perfil y una línea por cada desbloqueo.
     *
     * @param archivo archivo donde se exporta
     * @throws ExcepcionDOPOSoccer si el archivo es inválido o falla la escritura
     */
    public void exportAs(File archivo) throws ExcepcionDOPOSoccer {
        if (archivo == null) {
            throw new ExcepcionDOPOSoccer(ExcepcionDOPOSoccer.EXPORT_ARCHIVO_NULO);
        }
        if (archivo.isDirectory()) {
            throw new ExcepcionDOPOSoccer(ExcepcionDOPOSoccer.EXPORT_ES_CARPETA);
        }
        if (!tieneExtension(archivo, EXTENSION_TXT)) {
            throw new ExcepcionDOPOSoccer(
                    ExcepcionDOPOSoccer.EXPORT_EXTENSION_INVALIDA);
        }
        File carpeta = archivo.getAbsoluteFile().getParentFile();
        if (carpeta == null || !carpeta.isDirectory()) {
            throw new ExcepcionDOPOSoccer(
                    ExcepcionDOPOSoccer.EXPORT_CARPETA_NO_EXISTE);
        }
        if (archivo.exists() ? !archivo.canWrite() : !carpeta.canWrite()) {
            throw new ExcepcionDOPOSoccer(ExcepcionDOPOSoccer.EXPORT_SIN_PERMISO);
        }
        try (BufferedWriter escritor = new BufferedWriter(
                new OutputStreamWriter(
                        new FileOutputStream(archivo), StandardCharsets.UTF_8))) {
            escritor.write(CABECERA);
            escritor.newLine();
            for (PerfilJugador perfil : perfiles) {
                escribirPerfil(escritor, perfil);
                escribirDesbloqueos(escritor, perfil);
            }
        } catch (IOException e) {
            throw new ExcepcionDOPOSoccer(ExcepcionDOPOSoccer.EXPORT_ERROR, e);
        }
    }

    /**
     * Importa perfiles desde un archivo de texto .txt.
     * Si alguna línea tiene errores, informa la línea, la palabra y la causa,
     * y no modifica el progreso actual.
     *
     * @param archivo archivo que se quiere importar
     * @throws ExcepcionDOPOSoccer si el archivo es inválido o alguna línea falla
     */
    public void import_(File archivo) throws ExcepcionDOPOSoccer {
        if (archivo == null) {
            throw new ExcepcionDOPOSoccer(ExcepcionDOPOSoccer.IMPORT_ARCHIVO_NULO);
        }
        if (archivo.isDirectory()) {
            throw new ExcepcionDOPOSoccer(ExcepcionDOPOSoccer.IMPORT_ES_CARPETA);
        }
        if (!archivo.exists()) {
            throw new ExcepcionDOPOSoccer(ExcepcionDOPOSoccer.IMPORT_NO_EXISTE);
        }
        if (!tieneExtension(archivo, EXTENSION_TXT)) {
            throw new ExcepcionDOPOSoccer(
                    ExcepcionDOPOSoccer.IMPORT_EXTENSION_INVALIDA);
        }
        if (!archivo.canRead()) {
            throw new ExcepcionDOPOSoccer(ExcepcionDOPOSoccer.IMPORT_SIN_PERMISO);
        }

        List<PerfilJugador> nuevos = new ArrayList<>();
        Map<String, PerfilJugador> porId = new LinkedHashMap<>();
        List<String[]> desbloqueos = new ArrayList<>();
        List<String> errores = new ArrayList<>();
        boolean cabeceraLeida = false;

        try (BufferedReader lector = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(archivo), StandardCharsets.UTF_8))) {
            String linea;
            int numero = 0;
            while ((linea = lector.readLine()) != null) {
                numero++;
                if (linea.trim().isEmpty()) {
                    continue;
                }
                String[] campos = dividir(linea);
                if (!cabeceraLeida) {
                    cabeceraLeida = true;
                    if (campos.length != 2
                            || !"DOPOSOCCER".equals(campos[0])
                            || !"1".equals(campos[1])) {
                        errores.add(error(numero, campos[0],
                                "se esperaba la cabecera " + CABECERA));
                        continue;
                    }
                    continue;
                }
                String tipo = campos[0];
                if (PERFIL.equals(tipo)) {
                    agregarPerfil(campos, numero, nuevos, porId, errores);
                } else if (DESBLOQUEO.equals(tipo)) {
                    if (campos.length != 4) {
                        errores.add(error(numero, tipo,
                                "se esperaban 4 campos y se encontraron "
                                        + campos.length));
                    } else {
                        desbloqueos.add(new String[] {
                                String.valueOf(numero), campos[1],
                                campos[2], campos[3] });
                    }
                } else {
                    errores.add(error(numero, tipo,
                            "tipo de línea desconocido"));
                }
            }
        } catch (IOException e) {
            throw new ExcepcionDOPOSoccer(ExcepcionDOPOSoccer.IMPORT_ERROR, e);
        }

        if (!cabeceraLeida) {
            errores.add("Línea 1: \"\" - el archivo está vacío o no tiene la cabecera "
                    + CABECERA);
        }

        for (String[] desbloqueo : desbloqueos) {
            agregarDesbloqueo(desbloqueo, porId, errores);
        }

        if (!errores.isEmpty()) {
            throw new ExcepcionDOPOSoccer(mensajeErrores(errores));
        }

        perfiles.clear();
        perfiles.addAll(nuevos);
    }

    /**
     * Escribe un perfil en el archivo de texto.
     *
     * @param escritor archivo de salida
     * @param perfil perfil que se escribe
     * @throws IOException si falla la escritura
     */
    private void escribirPerfil(BufferedWriter escritor, PerfilJugador perfil)
            throws IOException {
        Estadisticas estadisticas = perfil.getEstadisticas();
        escritor.write(PERFIL
                + ";" + escapar(perfil.getId())
                + ";" + escapar(perfil.getNombre())
                + ";" + perfil.getPuntos().getTotal()
                + ";" + estadisticas.getSpeed()
                + ";" + estadisticas.getJump()
                + ";" + estadisticas.getKick()
                + ";" + estadisticas.getSurvival());
        escritor.newLine();
    }

    /**
     * Escribe los desbloqueos de un perfil en el archivo de texto.
     *
     * @param escritor archivo de salida
     * @param perfil perfil del que se escriben los desbloqueos
     * @throws IOException si falla la escritura
     */
    private void escribirDesbloqueos(BufferedWriter escritor,
                                     PerfilJugador perfil) throws IOException {
        escribirDesbloqueos(escritor, perfil, PERSONAJE,
                perfil.getPersonajesDesbloqueados());
        escribirDesbloqueos(escritor, perfil, BALON,
                perfil.getBalonesDesbloqueados());
        escribirDesbloqueos(escritor, perfil, ESCENARIO,
                perfil.getEscenariosDesbloqueados());
    }

    /**
     * Escribe un conjunto de desbloqueos del perfil.
     *
     * @param escritor archivo de salida
     * @param perfil perfil dueño de los desbloqueos
     * @param tipo tipo de desbloqueo
     * @param nombres nombres desbloqueados
     * @throws IOException si falla la escritura
     */
    private void escribirDesbloqueos(BufferedWriter escritor, PerfilJugador perfil,
                                     String tipo, Set<String> nombres)
            throws IOException {
        List<String> ordenados = new ArrayList<>(nombres);
        Collections.sort(ordenados);
        for (String nombre : ordenados) {
            escritor.write(DESBLOQUEO
                    + ";" + escapar(perfil.getId())
                    + ";" + tipo
                    + ";" + escapar(nombre));
            escritor.newLine();
        }
    }

    /**
     * Crea un perfil a partir de una línea del archivo y lo agrega a la lista
     * temporal. Si la línea tiene errores, los acumula en la lista de errores.
     *
     * @param campos campos de la línea
     * @param numero número de línea del archivo
     * @param nuevos perfiles que se van a importar
     * @param porId perfiles temporales indexados por id
     * @param errores errores encontrados
     */
    private void agregarPerfil(String[] campos, int numero,
                               List<PerfilJugador> nuevos,
                               Map<String, PerfilJugador> porId,
                               List<String> errores) {
        if (campos.length != 8) {
            errores.add(error(numero, PERFIL,
                    "se esperaban 8 campos y se encontraron " + campos.length));
            return;
        }

        String id = campos[1].trim();
        String nombre = campos[2].trim();
        if (id.isEmpty()) {
            errores.add(error(numero, "id", "no puede estar vacío"));
            return;
        }
        if (nombre.isEmpty()) {
            errores.add(error(numero, "nombre", "no puede estar vacío"));
            return;
        }
        if (porId.containsKey(id)) {
            errores.add(error(numero, id, "el id del perfil está repetido"));
            return;
        }

        Integer puntos = entero(campos[3], "puntos", numero, errores);
        Integer speed = entero(campos[4], "speed", numero, errores);
        Integer jump = entero(campos[5], "jump", numero, errores);
        Integer kick = entero(campos[6], "kick", numero, errores);
        Integer survival = entero(campos[7], "survival", numero, errores);
        if (puntos == null || speed == null || jump == null
                || kick == null || survival == null) {
            return;
        }
        if (puntos < 0) {
            errores.add(error(numero, "puntos", "no puede ser negativo"));
            return;
        }

        try {
            Estadisticas estadisticas =
                    new Estadisticas(speed, jump, kick, survival);
            Puntos puntosPerfil = new Puntos();
            puntosPerfil.sumar(puntos);
            ConfiguracionJugador configuracion =
                    new ConfiguracionJugador(nombre, estadisticas, puntosPerfil);
            PerfilJugador perfil =
                    new PerfilJugador(id, nombre, configuracion);
            nuevos.add(perfil);
            porId.put(id, perfil);
        } catch (IllegalArgumentException e) {
            errores.add(error(numero, "estadísticas", e.getMessage()));
        } catch (ExcepcionDOPOSoccer e) {
            errores.add(error(numero, "perfil", e.getMessage()));
        }
    }

    /**
     * Aplica un desbloqueo a un perfil ya leído. Si hay un error, lo acumula.
     *
     * @param desbloqueo datos {número de línea, id del perfil, tipo, nombre}
     * @param porId perfiles temporales indexados por id
     * @param errores errores encontrados
     */
    private void agregarDesbloqueo(String[] desbloqueo,
                                   Map<String, PerfilJugador> porId,
                                   List<String> errores) {
        int numero = Integer.parseInt(desbloqueo[0]);
        String idPerfil = desbloqueo[1].trim();
        String tipo = desbloqueo[2].trim();
        String nombre = desbloqueo[3].trim();

        PerfilJugador perfil = porId.get(idPerfil);
        if (perfil == null) {
            errores.add(error(numero, idPerfil,
                    "no existe un perfil con ese id"));
            return;
        }
        if (nombre.isEmpty()) {
            errores.add(error(numero, "nombre", "no puede estar vacío"));
            return;
        }

        switch (tipo) {
            case PERSONAJE:
                perfil.desbloquearPersonaje(nombre);
                break;
            case BALON:
                perfil.desbloquearBalon(nombre);
                break;
            case ESCENARIO:
                perfil.desbloquearEscenario(nombre);
                break;
            default:
                errores.add(error(numero, tipo,
                        "el tipo debe ser PERSONAJE, BALON o ESCENARIO"));
        }
    }

    /**
     * Convierte un campo de texto en un entero. Si no es un número, acumula
     * el error y devuelve null.
     *
     * @param valor texto que se convierte
     * @param etiqueta nombre del campo
     * @param numero número de línea
     * @param errores errores encontrados
     * @return el número, o null si el campo no era válido
     */
    private Integer entero(String valor, String etiqueta, int numero,
                           List<String> errores) {
        try {
            return Integer.valueOf(valor.trim());
        } catch (NumberFormatException e) {
            errores.add(error(numero, etiqueta,
                    "se esperaba un número entero"));
            return null;
        }
    }

    /**
     * Construye el mensaje que reúne todos los errores de la importación.
     *
     * @param errores errores encontrados
     * @return mensaje con el detalle de cada error
     */
    private String mensajeErrores(List<String> errores) {
        String titulo = errores.size() == 1
                ? "Se encontró 1 error en el archivo:"
                : "Se encontraron " + errores.size() + " errores en el archivo:";
        StringBuilder mensaje = new StringBuilder(titulo);
        for (String error : errores) {
            mensaje.append('\n').append(error);
        }
        return mensaje.toString();
    }

    /**
     * Construye el detalle de un error con línea, palabra y causa.
     *
     * @param numero número de línea
     * @param palabra palabra o campo con el error
     * @param causa explicación del error
     * @return detalle del error
     */
    private String error(int numero, String palabra, String causa) {
        return "Línea " + numero + ": \"" + palabra + "\" - " + causa;
    }

    /**
     * Indica si un archivo tiene la extensión indicada.
     *
     * @param archivo archivo que se revisa
     * @param extension extensión esperada, con punto
     * @return true si el nombre termina con la extensión
     */
    private boolean tieneExtension(File archivo, String extension) {
        return archivo.getName().toLowerCase().endsWith(extension);
    }

    /**
     * Escapa un texto para que pueda guardarse en una línea separada por ";".
     *
     * @param valor texto original
     * @return texto escapado
     */
    private String escapar(String valor) {
        if (valor == null) {
            return "";
        }
        return valor.replace("\\", "\\\\")
                .replace(";", "\\;")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }

    /**
     * Divide una línea por ";" respetando los caracteres escapados.
     *
     * @param linea línea del archivo
     * @return campos de la línea
     */
    private String[] dividir(String linea) {
        List<String> campos = new ArrayList<>();
        StringBuilder actual = new StringBuilder();
        boolean escapado = false;
        for (int i = 0; i < linea.length(); i++) {
            char caracter = linea.charAt(i);
            if (escapado) {
                if (caracter == 'n') {
                    actual.append('\n');
                } else if (caracter == 'r') {
                    actual.append('\r');
                } else {
                    actual.append(caracter);
                }
                escapado = false;
            } else if (caracter == '\\') {
                escapado = true;
            } else if (caracter == ';') {
                campos.add(actual.toString());
                actual.setLength(0);
            } else {
                actual.append(caracter);
            }
        }
        if (escapado) {
            actual.append('\\');
        }
        campos.add(actual.toString());
        return campos.toArray(new String[0]);
    }
}
