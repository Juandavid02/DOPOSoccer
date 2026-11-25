package test;

import domain.ConfiguracionIA;
import domain.ConfiguracionJugador;
import domain.ConfiguracionPartido;
import domain.Estadisticas;
import domain.EstadoPartido;
import domain.ExcepcionDOPOSoccer;
import domain.Modalidad;
import domain.PartidaGuardada;
import domain.Partido;
import domain.PerfilIA;
import domain.PerfilJugador;
import domain.Personaje;
import domain.PoderConfigurado;
import domain.Progreso;
import domain.Puntos;
import domain.Resolucion;
import domain.TipoPartido;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pruebas de unidad de guardar y abrir el estado de una partida.
 */
public class PartidaGuardadaTest {

    @TempDir
    Path temporal;

    /**
     * Crea un perfil con los datos indicados.
     *
     * @param id identificador
     * @param nombre nombre
     * @param puntos puntos acumulados
     * @return perfil creado
     * @throws ExcepcionDOPOSoccer si algún dato no es válido
     */
    private PerfilJugador crearPerfil(String id, String nombre, int puntos)
            throws ExcepcionDOPOSoccer {
        Puntos puntosPerfil = new Puntos();
        puntosPerfil.sumar(puntos);
        ConfiguracionJugador configuracion = new ConfiguracionJugador(
                nombre, new Estadisticas(3, 4, 5, 6), puntosPerfil);
        return new PerfilJugador(id, nombre, configuracion);
    }

    /**
     * Crea un personaje de prueba.
     *
     * @param nombre nombre del personaje
     * @return personaje con un poder real
     */
    private Personaje crearPersonaje(String nombre) {
        return new Personaje(nombre, "País", PoderConfigurado.auraAzul());
    }

    /**
     * Crea una configuración de partido PvsP.
     *
     * @return configuración válida
     * @throws ExcepcionDOPOSoccer si algún dato no es válido
     */
    private ConfiguracionPartido crearConfiguracion()
            throws ExcepcionDOPOSoccer {
        return new ConfiguracionPartido(
                Modalidad.PvsP, TipoPartido.TIEMPO,
                Resolucion.R1280x720, 5, 3);
    }

    /**
     * Crea una partida con dos perfiles y la deja pausada.
     *
     * @param perfil1 perfil del jugador 1
     * @param perfil2 perfil del jugador 2
     * @return partida creada
     * @throws ExcepcionDOPOSoccer si algún dato no es válido
     */
    private Partido crearPartido(PerfilJugador perfil1, PerfilJugador perfil2)
            throws ExcepcionDOPOSoccer {
        Partido partido = new Partido(
                crearConfiguracion(),
                crearPersonaje("Ana"),
                crearPersonaje("Luis"),
                perfil1.getConfiguracion(),
                perfil2.getConfiguracion());
        partido.actualizar(0.016);
        return partido;
    }

    @Test
    public void guardarYabrirConservaElEstadoDelPartido() throws Exception {
        PerfilJugador perfil1 = crearPerfil("id-1", "Ana", 800);
        PerfilJugador perfil2 = crearPerfil("id-2", "Luis", 400);
        perfil1.desbloquearPersonaje("BTS");
        perfil2.desbloquearBalon("BTS");

        Partido partido = crearPartido(perfil1, perfil2);
        partido.getBalon().reiniciar(1300, 590);
        partido.actualizar(0.016);
        partido.pausar();

        PartidaGuardada original = new PartidaGuardada(
                partido, crearConfiguracion(),
                crearPersonaje("Ana"), crearPersonaje("Luis"),
                perfil1, perfil2, null,
                "resources/images/Escenarios/fondo.png",
                "resources/images/Balones/balon.png",
                true);

        File archivo = temporal.resolve("partida.dat").toFile();
        original.saveAs(archivo);
        assertTrue(archivo.isFile());

        PartidaGuardada abierta = PartidaGuardada.open(archivo);
        Partido recuperado = abierta.getPartido();

        assertEquals(original.getConfiguracion().getModalidad(),
                abierta.getConfiguracion().getModalidad());
        assertEquals(original.getConfiguracion().getTipo(),
                abierta.getConfiguracion().getTipo());
        assertEquals("Ana", abierta.getPerfil1().getNombre());
        assertEquals(800, abierta.getPerfil1().getPuntos().getTotal());
        assertTrue(abierta.getPerfil1().estaDesbloqueadoPersonaje("BTS"));
        assertTrue(abierta.getPerfil2().estaDesbloqueadoBalon("BTS"));
        assertEquals(EstadoPartido.PAUSADO, recuperado.getEstado());
        assertEquals(1, recuperado.getMarcador().getGoles(1));
        assertEquals(0, recuperado.getMarcador().getGoles(2));
        assertEquals(3, recuperado.getJugadores().get(0)
                .getEstadisticas().getSpeed());
        assertTrue(recuperado.getJugadores().get(0)
                .getVida().getActual() > 0);
        assertTrue(abierta.isOtorgarPuntos());
    }

    @Test
    public void guardarYabrirConservaElTiempoYLosPuntosDelPartido()
            throws Exception {
        PerfilJugador perfil1 = crearPerfil("id-1", "Ana", 800);
        PerfilJugador perfil2 = crearPerfil("id-2", "Luis", 400);
        Partido partido = crearPartido(perfil1, perfil2);
        partido.actualizar(1.5);
        partido.pausar();

        PartidaGuardada original = new PartidaGuardada(
                partido, crearConfiguracion(),
                crearPersonaje("Ana"), crearPersonaje("Luis"),
                perfil1, perfil2, null,
                "escenario.png", "balon.png", true);

        File archivo = temporal.resolve("tiempo.dat").toFile();
        original.saveAs(archivo);
        PartidaGuardada abierta = PartidaGuardada.open(archivo);

        assertEquals(partido.getTiempoRestante(),
                abierta.getPartido().getTiempoRestante(), 0.0001);
    }

    @Test
    public void guardarYabrirConservaUnaPartidaContraLaMaquina()
            throws Exception {
        PerfilJugador perfil1 = crearPerfil("id-1", "Ana", 800);
        Partido partido = new Partido(
                crearConfiguracionIa(),
                crearPersonaje("Ana"),
                crearPersonaje("Luis"),
                perfil1.getConfiguracion(),
                new ConfiguracionIA(PerfilIA.TORPE,
                        new Estadisticas(2, 2, 2, 2)).crearConfiguracionJugador(),
                PerfilIA.TORPE,
                true);
        partido.actualizar(0.5);
        partido.pausar();

        PartidaGuardada original = new PartidaGuardada(
                partido, crearConfiguracionIa(),
                crearPersonaje("Ana"), crearPersonaje("Luis"),
                perfil1, null,
                new ConfiguracionIA(PerfilIA.TORPE,
                        new Estadisticas(2, 2, 2, 2)),
                "escenario.png", "balon.png", true);

        File archivo = temporal.resolve("maquina.dat").toFile();
        original.saveAs(archivo);
        PartidaGuardada abierta = PartidaGuardada.open(archivo);

        assertTrue(abierta.getConfiguracionIA() != null);
        assertEquals(PerfilIA.TORPE,
                abierta.getConfiguracionIA().getPerfil());
        assertEquals(Modalidad.PvsM,
                abierta.getConfiguracion().getModalidad());
        assertEquals(EstadoPartido.PAUSADO,
                abierta.getPartido().getEstado());
    }

    /**
     * Crea una configuración de partido contra la máquina.
     *
     * @return configuración válida
     * @throws ExcepcionDOPOSoccer si algún dato no es válido
     */
    private ConfiguracionPartido crearConfiguracionIa()
            throws ExcepcionDOPOSoccer {
        return new ConfiguracionPartido(
                Modalidad.PvsM, TipoPartido.TIEMPO,
                Resolucion.R1280x720, 5, 3, PerfilIA.TORPE);
    }

    @Test
    public void abrirRechazaArchivoNuloCarpetaYExtension() throws Exception {
        File carpeta = temporal.toFile();
        File texto = temporal.resolve("partida.txt").toFile();
        Files.writeString(texto.toPath(), "contenido");

        ExcepcionDOPOSoccer nulo = assertThrows(
                ExcepcionDOPOSoccer.class,
                () -> PartidaGuardada.open(null));
        assertEquals(ExcepcionDOPOSoccer.PARTIDA_OPEN_ARCHIVO_NULO,
                nulo.getMessage());

        ExcepcionDOPOSoccer directorio = assertThrows(
                ExcepcionDOPOSoccer.class,
                () -> PartidaGuardada.open(carpeta));
        assertEquals(ExcepcionDOPOSoccer.PARTIDA_OPEN_ES_CARPETA,
                directorio.getMessage());

        ExcepcionDOPOSoccer extension = assertThrows(
                ExcepcionDOPOSoccer.class,
                () -> PartidaGuardada.open(texto));
        assertEquals(ExcepcionDOPOSoccer.PARTIDA_OPEN_EXTENSION_INVALIDA,
                extension.getMessage());
    }

    @Test
    public void abrirRechazaUnProgresoComoPartida() throws Exception {
        PerfilJugador perfil = crearPerfil("id-1", "Ana", 100);
        Progreso progreso = new Progreso(List.of(perfil));
        File archivo = temporal.resolve("progreso.dat").toFile();
        progreso.saveAs(archivo);

        ExcepcionDOPOSoccer error = assertThrows(
                ExcepcionDOPOSoccer.class,
                () -> PartidaGuardada.open(archivo));

        assertEquals(ExcepcionDOPOSoccer.PARTIDA_OPEN_NO_ES_PARTIDA,
                error.getMessage());
    }

    @Test
    public void guardarRechazaDatosIncompletos() throws Exception {
        PerfilJugador perfil = crearPerfil("id-1", "Ana", 100);
        Partido partido = crearPartido(perfil, perfil);

        assertThrows(IllegalArgumentException.class,
                () -> new PartidaGuardada(
                        partido, crearConfiguracion(),
                        crearPersonaje("Ana"), crearPersonaje("Luis"),
                        perfil, null, null,
                        "escenario.png", "balon.png", true));
    }

    @Test
    public void guardarRechazaArchivoSinExtensionDat() throws Exception {
        PerfilJugador perfil = crearPerfil("id-1", "Ana", 100);
        Partido partido = crearPartido(perfil, perfil);
        PartidaGuardada guardada = new PartidaGuardada(
                partido, crearConfiguracion(),
                crearPersonaje("Ana"), crearPersonaje("Luis"),
                perfil, perfil, null,
                "escenario.png", "balon.png", true);

        File archivo = temporal.resolve("partida.txt").toFile();
        ExcepcionDOPOSoccer error = assertThrows(
                ExcepcionDOPOSoccer.class,
                () -> guardada.saveAs(archivo));

        assertEquals(ExcepcionDOPOSoccer.PARTIDA_SAVE_EXTENSION_INVALIDA,
                error.getMessage());
        assertFalse(archivo.exists());
    }

    @Test
    public void guardarRechazaArchivoNuloCarpetaYDestinoInexistente()
            throws Exception {
        PerfilJugador perfil = crearPerfil("id-1", "Ana", 100);
        Partido partido = crearPartido(perfil, perfil);
        PartidaGuardada guardada = new PartidaGuardada(
                partido, crearConfiguracion(),
                crearPersonaje("Ana"), crearPersonaje("Luis"),
                perfil, perfil, null,
                "escenario.png", "balon.png", true);

        ExcepcionDOPOSoccer nulo = assertThrows(
                ExcepcionDOPOSoccer.class,
                () -> guardada.saveAs(null));
        assertEquals(ExcepcionDOPOSoccer.PARTIDA_SAVE_ARCHIVO_NULO,
                nulo.getMessage());

        ExcepcionDOPOSoccer carpeta = assertThrows(
                ExcepcionDOPOSoccer.class,
                () -> guardada.saveAs(temporal.toFile()));
        assertEquals(ExcepcionDOPOSoccer.PARTIDA_SAVE_ES_CARPETA,
                carpeta.getMessage());

        File sinCarpeta = temporal.resolve("noexiste")
                .resolve("partida.dat").toFile();
        ExcepcionDOPOSoccer destino = assertThrows(
                ExcepcionDOPOSoccer.class,
                () -> guardada.saveAs(sinCarpeta));
        assertEquals(ExcepcionDOPOSoccer.PARTIDA_SAVE_CARPETA_NO_EXISTE,
                destino.getMessage());
    }

    @Test
    public void abrirRechazaUnArchivoQueNoExiste() {
        File archivo = temporal.resolve("noexiste.dat").toFile();

        ExcepcionDOPOSoccer error = assertThrows(
                ExcepcionDOPOSoccer.class,
                () -> PartidaGuardada.open(archivo));

        assertEquals(ExcepcionDOPOSoccer.PARTIDA_OPEN_NO_EXISTE,
                error.getMessage());
    }
}
