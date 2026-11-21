package domain;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;

/**
 * Partida guardada de DOPOSoccer. Incluye el estado completo del partido y
 * los dos perfiles participantes. No guarda los demás perfiles ni el estado
 * de un torneo.
 */
public class PartidaGuardada implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final String EXTENSION = ".dat";

    private final Partido partido;
    private final ConfiguracionPartido configuracion;
    private final Personaje personaje1;
    private final Personaje personaje2;
    private final PerfilJugador perfil1;
    private final PerfilJugador perfil2;
    private final ConfiguracionIA configuracionIA;
    private final String rutaEscenario;
    private final String rutaBalon;
    private final boolean otorgarPuntos;

    /**
     * Crea una partida guardada con todos sus datos.
     *
     * @param partido partido en el momento de guardar
     * @param configuracion configuración del partido
     * @param personaje1 personaje del jugador 1
     * @param personaje2 personaje del jugador 2
     * @param perfil1 perfil del jugador 1
     * @param perfil2 perfil del jugador 2, o null si juega la máquina
     * @param configuracionIA configuración de la máquina, o null en PvsP
     * @param rutaEscenario ruta de la imagen del escenario
     * @param rutaBalon ruta de la imagen del balón
     * @param otorgarPuntos true si el partido reparte puntos al terminar
     */
    public PartidaGuardada(Partido partido,
                           ConfiguracionPartido configuracion,
                           Personaje personaje1,
                           Personaje personaje2,
                           PerfilJugador perfil1,
                           PerfilJugador perfil2,
                           ConfiguracionIA configuracionIA,
                           String rutaEscenario,
                           String rutaBalon,
                           boolean otorgarPuntos) {
        if (partido == null || configuracion == null
                || personaje1 == null || personaje2 == null
                || perfil1 == null || rutaEscenario == null
                || rutaBalon == null) {
            throw new IllegalArgumentException(
                    ExcepcionDOPOSoccer.PARTIDA_INVALIDA);
        }
        if (configuracion.getModalidad() == Modalidad.PvsP && perfil2 == null) {
            throw new IllegalArgumentException(
                    ExcepcionDOPOSoccer.PARTIDA_INVALIDA);
        }
        if (configuracion.getModalidad() == Modalidad.PvsM
                && configuracionIA == null) {
            throw new IllegalArgumentException(
                    ExcepcionDOPOSoccer.PARTIDA_INVALIDA);
        }

        this.partido = partido;
        this.configuracion = configuracion;
        this.personaje1 = personaje1;
        this.personaje2 = personaje2;
        this.perfil1 = perfil1;
        this.perfil2 = perfil2;
        this.configuracionIA = configuracionIA;
        this.rutaEscenario = rutaEscenario;
        this.rutaBalon = rutaBalon;
        this.otorgarPuntos = otorgarPuntos;
    }

    /**
     * Devuelve el partido guardado.
     *
     * @return partido en el estado en que se guardó
     */
    public Partido getPartido() {
        return partido;
    }

    /**
     * Devuelve la configuración del partido.
     *
     * @return configuración del partido
     */
    public ConfiguracionPartido getConfiguracion() {
        return configuracion;
    }

    /**
     * Devuelve el personaje del jugador 1.
     *
     * @return personaje del jugador 1
     */
    public Personaje getPersonaje1() {
        return personaje1;
    }

    /**
     * Devuelve el personaje del jugador 2.
     *
     * @return personaje del jugador 2
     */
    public Personaje getPersonaje2() {
        return personaje2;
    }

    /**
     * Devuelve el perfil guardado del jugador 1.
     *
     * @return perfil del jugador 1
     */
    public PerfilJugador getPerfil1() {
        return perfil1;
    }

    /**
     * Devuelve el perfil guardado del jugador 2.
     *
     * @return perfil del jugador 2, o null si juega la máquina
     */
    public PerfilJugador getPerfil2() {
        return perfil2;
    }

    /**
     * Devuelve la configuración de la máquina.
     *
     * @return configuración de la máquina, o null en PvsP
     */
    public ConfiguracionIA getConfiguracionIA() {
        return configuracionIA;
    }

    /**
     * Devuelve la ruta del escenario.
     *
     * @return ruta de la imagen del escenario
     */
    public String getRutaEscenario() {
        return rutaEscenario;
    }

    /**
     * Devuelve la ruta del balón.
     *
     * @return ruta de la imagen del balón
     */
    public String getRutaBalon() {
        return rutaBalon;
    }

    /**
     * Indica si el partido reparte puntos al terminar.
     *
     * @return true si reparte puntos
     */
    public boolean isOtorgarPuntos() {
        return otorgarPuntos;
    }

    /**
     * Guarda la partida completa en un archivo .dat.
     *
     * @param archivo archivo donde se guarda
     * @throws ExcepcionDOPOSoccer si el archivo es inválido o falla la escritura
     */
    public void saveAs(File archivo) throws ExcepcionDOPOSoccer {
        if (archivo == null) {
            throw new ExcepcionDOPOSoccer(
                    ExcepcionDOPOSoccer.PARTIDA_SAVE_ARCHIVO_NULO);
        }
        if (archivo.isDirectory()) {
            throw new ExcepcionDOPOSoccer(
                    ExcepcionDOPOSoccer.PARTIDA_SAVE_ES_CARPETA);
        }
        if (!tieneExtension(archivo)) {
            throw new ExcepcionDOPOSoccer(
                    ExcepcionDOPOSoccer.PARTIDA_SAVE_EXTENSION_INVALIDA);
        }
        File carpeta = archivo.getAbsoluteFile().getParentFile();
        if (carpeta == null || !carpeta.isDirectory()) {
            throw new ExcepcionDOPOSoccer(
                    ExcepcionDOPOSoccer.PARTIDA_SAVE_CARPETA_NO_EXISTE);
        }
        if (archivo.exists() ? !archivo.canWrite() : !carpeta.canWrite()) {
            throw new ExcepcionDOPOSoccer(
                    ExcepcionDOPOSoccer.PARTIDA_SAVE_SIN_PERMISO);
        }
        try (ObjectOutputStream salida =
                     new ObjectOutputStream(new FileOutputStream(archivo))) {
            salida.writeObject(this);
        } catch (NotSerializableException e) {
            throw new ExcepcionDOPOSoccer(
                    ExcepcionDOPOSoccer.PARTIDA_SAVE_NO_SERIALIZABLE, e);
        } catch (IOException e) {
            throw new ExcepcionDOPOSoccer(
                    ExcepcionDOPOSoccer.PARTIDA_SAVE_ERROR, e);
        }
    }

    /**
     * Abre una partida guardada en un archivo .dat.
     *
     * @param archivo archivo que se quiere leer
     * @return partida leída del archivo
     * @throws ExcepcionDOPOSoccer si el archivo es inválido o falla la lectura
     */
    public static PartidaGuardada open(File archivo) throws ExcepcionDOPOSoccer {
        if (archivo == null) {
            throw new ExcepcionDOPOSoccer(
                    ExcepcionDOPOSoccer.PARTIDA_OPEN_ARCHIVO_NULO);
        }
        if (archivo.isDirectory()) {
            throw new ExcepcionDOPOSoccer(
                    ExcepcionDOPOSoccer.PARTIDA_OPEN_ES_CARPETA);
        }
        if (!archivo.exists()) {
            throw new ExcepcionDOPOSoccer(
                    ExcepcionDOPOSoccer.PARTIDA_OPEN_NO_EXISTE);
        }
        if (!tieneExtension(archivo)) {
            throw new ExcepcionDOPOSoccer(
                    ExcepcionDOPOSoccer.PARTIDA_OPEN_EXTENSION_INVALIDA);
        }
        if (!archivo.canRead()) {
            throw new ExcepcionDOPOSoccer(
                    ExcepcionDOPOSoccer.PARTIDA_OPEN_SIN_PERMISO);
        }
        try (ObjectInputStream entrada =
                     new ObjectInputStream(new FileInputStream(archivo))) {
            Object leido = entrada.readObject();
            if (!(leido instanceof PartidaGuardada)) {
                throw new ExcepcionDOPOSoccer(
                        ExcepcionDOPOSoccer.PARTIDA_OPEN_NO_ES_PARTIDA);
            }
            return (PartidaGuardada) leido;
        } catch (InvalidClassException e) {
            throw new ExcepcionDOPOSoccer(
                    ExcepcionDOPOSoccer.PARTIDA_OPEN_VERSION_INCOMPATIBLE, e);
        } catch (StreamCorruptedException | EOFException e) {
            throw new ExcepcionDOPOSoccer(
                    ExcepcionDOPOSoccer.PARTIDA_OPEN_NO_ES_PARTIDA, e);
        } catch (ClassNotFoundException e) {
            throw new ExcepcionDOPOSoccer(
                    ExcepcionDOPOSoccer.PARTIDA_OPEN_CLASE_DESCONOCIDA, e);
        } catch (IOException e) {
            throw new ExcepcionDOPOSoccer(
                    ExcepcionDOPOSoccer.PARTIDA_OPEN_ERROR, e);
        }
    }

    /**
     * Indica si el archivo tiene la extensión .dat.
     *
     * @param archivo archivo que se revisa
     * @return true si el nombre termina en .dat
     */
    private static boolean tieneExtension(File archivo) {
        return archivo.getName().toLowerCase().endsWith(EXTENSION);
    }
}
