package ar.lamansys.sgh.publicapi;

import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Clase con métodos utilitarios generales para testing
 */
public class TestUtils {

	public static <T extends Throwable> void shouldThrow(Class<T> exceptionClass, Executable executable){
		assertThrows(exceptionClass, executable);
	}

}
