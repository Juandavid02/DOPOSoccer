package test;

import static org.junit.jupiter.api.Assertions.*;

import domain.ConfiguracionJugador;
import domain.ConfiguracionPartido;
import domain.Estadisticas;
import domain.ExcepcionDOPOSoccer;
import domain.Modalidad;
import domain.PerfilJugador;
import domain.Resolucion;
import domain.TipoPartido;
import org.junit.jupiter.api.Test;

/**
 * Pruebas de la excepción propia de DOPOSoccer.
 */
public class ExcepcionDOPOSoccerTest {

    @Test
    public void shouldThrowCustomExceptionForInvalidMatchConfiguration() {
        ExcepcionDOPOSoccer error = assertThrows(
                ExcepcionDOPOSoccer.class,
                () -> new ConfiguracionPartido(
                        null,
                        TipoPartido.TIEMPO,
                        Resolucion.R1280x720,
                        1,
                        5));

        assertEquals(ExcepcionDOPOSoccer.MODALIDAD_NULA, error.getMessage());
    }

    @Test
    public void shouldThrowCustomExceptionForInvalidPlayerConfiguration() {
        ExcepcionDOPOSoccer error = assertThrows(
                ExcepcionDOPOSoccer.class,
                () -> new ConfiguracionJugador(
                        "",
                        new Estadisticas(1, 1, 1, 1)));

        assertEquals(ExcepcionDOPOSoccer.NOMBRE_INVALIDO, error.getMessage());
    }

    @Test
    public void shouldThrowCustomExceptionForInvalidProfile() {
        ExcepcionDOPOSoccer error = assertThrows(
                ExcepcionDOPOSoccer.class,
                () -> new PerfilJugador(""));

        assertEquals(ExcepcionDOPOSoccer.NOMBRE_INVALIDO, error.getMessage());
    }
}
