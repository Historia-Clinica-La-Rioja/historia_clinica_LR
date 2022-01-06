package net.pladema.hsi.extensions.configuration;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

class ExtensionAuthorizationBuilderTest {

	@Test
	void getSystemPage_() {
		ExtensionAuthorization auth = ExtensionAuthorizationBuilder
			.buildExtensionAuthorization()
				.systemMenu("tablerito", () -> true)
				.systemMenu("secret", () -> false)
			.build();

		assertTrue(auth.isSystemMenuAllowed("tablerito"));
		assertFalse(auth.isSystemMenuAllowed("secret"));
		assertFalse(auth.isSystemMenuAllowed("89889980"));
	}

	enum ExtRole {
		ADM,
		ADM2
	}
}