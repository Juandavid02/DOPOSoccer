package test;

import domain.ConfiguracionJugador;
import domain.Estadisticas;
import domain.ExcepcionDOPOSoccer;
import domain.PerfilJugador;
import domain.Progreso;
import domain.Puntos;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pruebas de unidad de las cuatro operaciones de persistencia del progreso.
 */
public class ProgresoTest {

    @TempDir
    Path temporal;

    /**
     * Crea un perfil con los datos indicados.
     *
     * @param id identificador
     * @param nombre nombre
     * @param puntos puntos acumulados
     * @param velocidad velocidad
     * @param salto salto
     * @param patada patada
     * @param resistencia resistencia
     * @return perfil creado
     * @throws ExcepcionDOPOSoccer si algún dato no es válido
     */
    private PerfilJugador crearPerfil(String id, String nombre, int puntos,
                                      int velocidad, int salto, int patada,
                                      int resistencia)
            throws ExcepcionDOPOSoccer {
        Puntos puntosPerfil = new Puntos();
        puntosPerfil.sumar(puntos);
        Estadisticas estadisticas = new Estadisticas(
                velocidad, salto, patada, resistencia);
        ConfiguracionJugador configuracion = new ConfiguracionJugador(
                nombre, estadisticas, puntosPerfil);
        return new PerfilJugador(id, nombre, configuracion);
    }

    /**
     * Escribe un archivo de texto con las líneas indicadas.
     *
     * @param nombre nombre del archivo
     * @param lineas líneas del archivo
     * @return archivo creado
     * @throws Exception si falla la escritura
     */
    private File escribirTexto(String nombre, String... lineas)
            throws Exception {
        Path archivo = temporal.resolve(nombre);
        Files.write(archivo, List.of(lineas), StandardCharsets.UTF_8);
        return archivo.toFile();
    }

    @Test
    public void guardarYabrirConservaTodosLosDatos() throws Exception {
        PerfilJugador perfil = crearPerfil("id-1", "Ana", 1250, 3, 4, 5, 6);
        perfil.desbloquearPersonaje("BTS");
        perfil.desbloquearBalon("BTS");
        perfil.desbloquearEscenario("Reino Champiñón");

        Progreso original = new Progreso(List.of(perfil));
        File archivo = temporal.resolve("progreso.dat").toFile();
        original.saveAs(archivo);

        assertTrue(archivo.isFile());

        Progreso abierto = new Progreso().open(archivo);

        assertEquals(1, abierto.cantidadPerfiles());
        PerfilJugador copia = abierto.getPerfiles().get(0);
        assertEquals("id-1", copia.getId());
        assertEquals("Ana", copia.getNombre());
        assertEquals(1250, copia.getPuntos().getTotal());
        assertEquals(3, copia.getEstadisticas().getSpeed());
        assertEquals(4, copia.getEstadisticas().getJump());
        assertEquals(5, copia.getEstadisticas().getKick());
        assertEquals(6, copia.getEstadisticas().getSurvival());
        assertTrue(copia.estaDesbloqueadoPersonaje("BTS"));
        assertTrue(copia.estaDesbloqueadoBalon("BTS"));
        assertTrue(copia.estaDesbloqueadoEscenario("Reino Champiñón"));
    }

    @Test
    public void exportarEimportarConservaTodosLosDatos() throws Exception {
        PerfilJugador perfil = crearPerfil(
                "id-2", "Perfil; raro", 800, 7, 2, 9, 4);
        perfil.desbloquearPersonaje("BTS");
        perfil.desbloquearBalon("Mario");
        perfil.desbloquearEscenario("Reino Champiñón");

        Progreso original = new Progreso(List.of(perfil));
        File archivo = temporal.resolve("progreso.txt").toFile();
        original.exportAs(archivo);

        Progreso importado = new Progreso();
        importado.import_(archivo);

        assertEquals(1, importado.cantidadPerfiles());
        PerfilJugador copia = importado.getPerfiles().get(0);
        assertEquals("id-2", copia.getId());
        assertEquals("Perfil; raro", copia.getNombre());
        assertEquals(800, copia.getPuntos().getTotal());
        assertEquals(7, copia.getEstadisticas().getSpeed());
        assertEquals(2, copia.getEstadisticas().getJump());
        assertEquals(9, copia.getEstadisticas().getKick());
        assertEquals(4, copia.getEstadisticas().getSurvival());
        assertTrue(copia.estaDesbloqueadoPersonaje("BTS"));
        assertTrue(copia.estaDesbloqueadoBalon("Mario"));
        assertTrue(copia.estaDesbloqueadoEscenario("Reino Champiñón"));
    }

    @Test
    public void elArchivoExportadoTieneUnaLineaPorDato() throws Exception {
        PerfilJugador perfil = crearPerfil("id-3", "Luis", 100, 1, 1, 1, 1);
        perfil.desbloquearPersonaje("BTS");

        File archivo = temporal.resolve("lineas.txt").toFile();
        new Progreso(List.of(perfil)).exportAs(archivo);

        List<String> lineas = Files.readAllLines(
                archivo.toPath(), StandardCharsets.UTF_8);

        assertEquals("DOPOSOCCER;1", lineas.get(0));
        assertTrue(lineas.stream().anyMatch(
                linea -> linea.startsWith("PERFIL;")));
        assertTrue(lineas.stream().anyMatch(
                linea -> linea.startsWith("DESBLOQUEO;")));
    }

    @Test
    public void guardarRechazaArchivoNuloCarpetaYExtension() throws Exception {
        Progreso progreso = new Progreso();
        File carpeta = temporal.toFile();
        File texto = temporal.resolve("progreso.txt").toFile();

        ExcepcionDOPOSoccer sinArchivo = assertThrows(
                ExcepcionDOPOSoccer.class, () -> progreso.saveAs(null));
        assertEquals(ExcepcionDOPOSoccer.SAVE_ARCHIVO_NULO,
                sinArchivo.getMessage());

        ExcepcionDOPOSoccer esCarpeta = assertThrows(
                ExcepcionDOPOSoccer.class, () -> progreso.saveAs(carpeta));
        assertEquals(ExcepcionDOPOSoccer.SAVE_ES_CARPETA,
                esCarpeta.getMessage());

        ExcepcionDOPOSoccer extension = assertThrows(
                ExcepcionDOPOSoccer.class, () -> progreso.saveAs(texto));
        assertEquals(ExcepcionDOPOSoccer.SAVE_EXTENSION_INVALIDA,
                extension.getMessage());
    }

    @Test
    public void guardarRechazaCarpetaInexistente() {
        File archivo = temporal.resolve("noExiste").resolve("progreso.dat")
                .toFile();

        ExcepcionDOPOSoccer error = assertThrows(
                ExcepcionDOPOSoccer.class,
                () -> new Progreso().saveAs(archivo));

        assertEquals(ExcepcionDOPOSoccer.SAVE_CARPETA_NO_EXISTE,
                error.getMessage());
    }

    @Test
    public void abrirRechazaArchivoInexistenteYExtension() throws Exception {
        File inexistente = temporal.resolve("noExiste.dat").toFile();
        File texto = escribirTexto("datos.txt", "hola");

        ExcepcionDOPOSoccer noExiste = assertThrows(
                ExcepcionDOPOSoccer.class,
                () -> new Progreso().open(inexistente));
        assertEquals(ExcepcionDOPOSoccer.OPEN_NO_EXISTE,
                noExiste.getMessage());

        ExcepcionDOPOSoccer extension = assertThrows(
                ExcepcionDOPOSoccer.class,
                () -> new Progreso().open(texto));
        assertEquals(ExcepcionDOPOSoccer.OPEN_EXTENSION_INVALIDA,
                extension.getMessage());
    }

    @Test
    public void abrirRechazaArchivoQueNoEsProgreso() throws Exception {
        File archivo = temporal.resolve("otro.dat").toFile();
        try (ObjectOutputStream salida = new ObjectOutputStream(
                Files.newOutputStream(archivo.toPath()))) {
            salida.writeObject("esto no es un progreso");
        }

        ExcepcionDOPOSoccer error = assertThrows(
                ExcepcionDOPOSoccer.class,
                () -> new Progreso().open(archivo));

        assertEquals(ExcepcionDOPOSoccer.OPEN_NO_ES_PROGRESO,
                error.getMessage());
    }

    @Test
    public void exportarRechazaArchivoNuloYExtension() throws Exception {
        File texto = temporal.resolve("salida.dat").toFile();

        ExcepcionDOPOSoccer sinArchivo = assertThrows(
                ExcepcionDOPOSoccer.class,
                () -> new Progreso().exportAs(null));
        assertEquals(ExcepcionDOPOSoccer.EXPORT_ARCHIVO_NULO,
                sinArchivo.getMessage());

        ExcepcionDOPOSoccer extension = assertThrows(
                ExcepcionDOPOSoccer.class,
                () -> new Progreso().exportAs(texto));
        assertEquals(ExcepcionDOPOSoccer.EXPORT_EXTENSION_INVALIDA,
                extension.getMessage());
    }

    @Test
    public void importarRechazaArchivoNuloInexistenteYExtension()
            throws Exception {
        File texto = escribirTexto("datos.txt", "DOPOSOCCER;1");
        File binario = escribirTexto("otro.dat", "contenido");

        ExcepcionDOPOSoccer sinArchivo = assertThrows(
                ExcepcionDOPOSoccer.class,
                () -> new Progreso().import_(null));
        assertEquals(ExcepcionDOPOSoccer.IMPORT_ARCHIVO_NULO,
                sinArchivo.getMessage());

        ExcepcionDOPOSoccer noExiste = assertThrows(
                ExcepcionDOPOSoccer.class,
                () -> new Progreso().import_(
                        temporal.resolve("noExiste.txt").toFile()));
        assertEquals(ExcepcionDOPOSoccer.IMPORT_NO_EXISTE,
                noExiste.getMessage());

        ExcepcionDOPOSoccer extension = assertThrows(
                ExcepcionDOPOSoccer.class,
                () -> new Progreso().import_(binario));
        assertEquals(ExcepcionDOPOSoccer.IMPORT_EXTENSION_INVALIDA,
                extension.getMessage());

        assertEquals(1, Files.readAllLines(texto.toPath()).size());
    }

    @Test
    public void importarInformaLineaPalabraYCausaDeCadaError()
            throws Exception {
        File archivo = escribirTexto("conErrores.txt",
                "DOPOSOCCER;1",
                "PERFIL;id-1;Ana;100;1;1;1;1",
                "PERFIL;id-2;Luis;muchos;1;1;1;1",
                "PERFIL;id-1;Pedro;100;1;1;1;1",
                "DESBLOQUEO;id-9;PERSONAJE;Goku",
                "DESBLOQUEO;id-1;OTRO;Goku");

        Progreso progreso = new Progreso();
        ExcepcionDOPOSoccer error = assertThrows(
                ExcepcionDOPOSoccer.class, () -> progreso.import_(archivo));

        String mensaje = error.getMessage();
        assertTrue(mensaje.contains("4 errores"), mensaje);
        assertTrue(mensaje.contains("Línea 3"), mensaje);
        assertTrue(mensaje.contains("\"puntos\""), mensaje);
        assertTrue(mensaje.contains("Línea 4"), mensaje);
        assertTrue(mensaje.contains("repetido"), mensaje);
        assertTrue(mensaje.contains("Línea 5"), mensaje);
        assertTrue(mensaje.contains("no existe un perfil"), mensaje);
        assertTrue(mensaje.contains("Línea 6"), mensaje);
        assertTrue(mensaje.contains("PERSONAJE, BALON o ESCENARIO"), mensaje);
    }

    @Test
    public void importarConErroresNoModificaElProgresoActual()
            throws Exception {
        PerfilJugador existente = crearPerfil(
                "id-actual", "Actual", 50, 1, 1, 1, 1);
        Progreso progreso = new Progreso(List.of(existente));
        File archivo = escribirTexto("malo.txt",
                "DOPOSOCCER;1",
                "PERFIL;id-1;Ana;100;99;1;1;1");

        assertThrows(ExcepcionDOPOSoccer.class,
                () -> progreso.import_(archivo));

        assertEquals(1, progreso.cantidadPerfiles());
        assertEquals("Actual", progreso.getPerfiles().get(0).getNombre());
    }

    @Test
    public void importarRechazaCabeceraIncorrecta() throws Exception {
        File archivo = escribirTexto("sinCabecera.txt",
                "OTRA;COSA",
                "PERFIL;id-1;Ana;100;1;1;1;1");

        ExcepcionDOPOSoccer error = assertThrows(
                ExcepcionDOPOSoccer.class,
                () -> new Progreso().import_(archivo));

        assertTrue(error.getMessage().contains("cabecera"),
                error.getMessage());
    }

    @Test
    public void importarInformaCamposFaltantesVaciosYFueraDeRango()
            throws Exception {
        File archivo = escribirTexto("campos.txt",
                "DOPOSOCCER;1",
                "PERFIL;id-1;Ana;100;1;1;1",
                "PERFIL;;Luis;100;1;1;1;1",
                "PERFIL;id-3;;100;1;1;1;1",
                "PERFIL;id-4;Pedro;100;0;11;1;1");

        ExcepcionDOPOSoccer error = assertThrows(
                ExcepcionDOPOSoccer.class,
                () -> new Progreso().import_(archivo));

        String mensaje = error.getMessage();
        assertTrue(mensaje.contains("8 campos"), mensaje);
        assertTrue(mensaje.contains("\"id\""), mensaje);
        assertTrue(mensaje.contains("\"nombre\""), mensaje);
        assertTrue(mensaje.contains("speed"), mensaje);
        assertTrue(mensaje.contains("debe estar entre 1 y 10"), mensaje);
    }
}
