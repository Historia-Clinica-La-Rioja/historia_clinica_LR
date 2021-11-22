package ar.lamansys.sgx.auth;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;

public class SgxAsserts {

	private SgxAsserts() {
		
	}

	public static <T> T assertPresent(final Optional<T> optional) {
		Assertions.assertNotNull(optional);
		assertTrue(optional.isPresent());
		return optional.get();
	}
}
