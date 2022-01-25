package net.pladema.permissions;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

/**
 * Tests para evaluar cuando un rol tiene mas rango que otro.
 *
 */
class RoleUtilsTest {

	@Test
	void loggedUserHasHigherRank_sameClaims() {
		Assertions.assertFalse(RoleUtils.loggedUserHasHigherRank(
				Arrays.asList("ROOT"),
				Arrays.asList("ROOT")
		), "Same ROOT claims ain't higher");

		Assertions.assertFalse(RoleUtils.loggedUserHasHigherRank(
				Arrays.asList("ADMINISTRADOR"),
				Arrays.asList("ADMINISTRADOR")
		), "Same ADMINISTRADOR claims ain't higher");

		Assertions.assertFalse(RoleUtils.loggedUserHasHigherRank(
				Arrays.asList("ADMINISTRADOR", "ROOT"),
				Arrays.asList("ROOT", "ADMINISTRADOR")
		), "Same claims ain't higher");
	}

	@Test
	void loggedUserHasHigherRank_unrankedClaims() {
		Assertions.assertTrue(RoleUtils.loggedUserHasHigherRank(
				Arrays.asList("ROOT"),
				Arrays.asList("ESPECIALISTA_MEDICO", "PROFESIONAL_DE_SALUD", "ADMINISTRATIVO", "ENFERMERO_ADULTO_MAYOR")),
				"Unranked claims are lowest"
				);

		Assertions.assertFalse(RoleUtils.loggedUserHasHigherRank(
				Arrays.asList("ESPECIALISTA_MEDICO", "PROFESIONAL_DE_SALUD", "ADMINISTRATIVO", "ENFERMERO_ADULTO_MAYOR"),
				Arrays.asList("ROOT")
		), "Unranked claims are lowest");

		Assertions.assertTrue(RoleUtils.loggedUserHasHigherRank(
				Arrays.asList("ADMINISTRADOR"),
				Arrays.asList("ESPECIALISTA_MEDICO", "PROFESIONAL_DE_SALUD", "ADMINISTRATIVO", "ENFERMERO_ADULTO_MAYOR")
		),"Unranked claims are lowest");

		Assertions.assertTrue(RoleUtils.loggedUserHasHigherRank(
				Arrays.asList("ADMINISTRADOR"),
				Arrays.asList("CUALQUIER OTRO")
		), "Unranked claims are lowest");
	}

	@Test
	void loggedUserHasHigherRank_emptyClaims() {
		Assertions.assertFalse(RoleUtils.loggedUserHasHigherRank(
				Collections.emptyList(),
				Collections.emptyList()
		), "Empty claims ain't higher");
	}

	@Test
	void loggedUserHasHigherRank_rootClaimRank() {
		Assertions.assertTrue(RoleUtils.loggedUserHasHigherRank(
				Arrays.asList("ROOT"),
				Arrays.asList("ADMINISTRADOR", "ESPECIALISTA_MEDICO", "PROFESIONAL_DE_SALUD", "ADMINISTRATIVO", "ENFERMERO_ADULTO_MAYOR")
		), "ROOT should be higher");

		Assertions.assertFalse(RoleUtils.loggedUserHasHigherRank(
				Arrays.asList("ADMINISTRADOR", "ESPECIALISTA_MEDICO", "PROFESIONAL_DE_SALUD", "ADMINISTRATIVO", "ENFERMERO_ADULTO_MAYOR"),
				Arrays.asList("ROOT")
		), "ROOT should be higher");
	}

	@Test
	void loggedUserHasHigherRank_administradorClaimRank() {
		Assertions.assertTrue(RoleUtils.loggedUserHasHigherRank(
				Arrays.asList("ADMINISTRADOR"),
				Arrays.asList("ESPECIALISTA_MEDICO", "PROFESIONAL_DE_SALUD", "ADMINISTRATIVO", "ENFERMERO_ADULTO_MAYOR")
		), "ADMINISTRADOR should be higher");

		Assertions.assertFalse(RoleUtils.loggedUserHasHigherRank(
				Arrays.asList("ESPECIALISTA_MEDICO", "PROFESIONAL_DE_SALUD", "ADMINISTRATIVO", "ENFERMERO_ADULTO_MAYOR"),
				Arrays.asList("ADMINISTRADOR")
		), "ADMINISTRADOR should be higher");
	}

}