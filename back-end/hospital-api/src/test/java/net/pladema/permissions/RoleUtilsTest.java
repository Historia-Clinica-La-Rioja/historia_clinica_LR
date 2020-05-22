package net.pladema.permissions;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

/**
 * Tests para evaluar cuando un rol tiene mas rango que otro.
 *
 */
public class RoleUtilsTest {

	@Test
	public void loggedUserHasHigherRank_sameClaims() {
		assertFalse("Same ROOT claims ain't higher", RoleUtils.loggedUserHasHigherRank(
				Arrays.asList("ROOT"),
				Arrays.asList("ROOT")
		));

		assertFalse("Same ADMINISTRADOR claims ain't higher", RoleUtils.loggedUserHasHigherRank(
				Arrays.asList("ADMINISTRADOR"),
				Arrays.asList("ADMINISTRADOR")
		));

		assertFalse("Same claims ain't higher", RoleUtils.loggedUserHasHigherRank(
				Arrays.asList("ADMINISTRADOR", "ROOT"),
				Arrays.asList("ROOT", "ADMINISTRADOR")
		));
	}

	@Test
	public void loggedUserHasHigherRank_unrankedClaims() {
		assertTrue("Unranked claims are lowest", RoleUtils.loggedUserHasHigherRank(
				Arrays.asList("ROOT"),
				Arrays.asList("ESPECIALISTA_MEDICO", "PROFESIONAL_DE_SALUD", "ADMINISTRATIVO")
		));

		assertFalse("Unranked claims are lowest", RoleUtils.loggedUserHasHigherRank(
				Arrays.asList("ESPECIALISTA_MEDICO", "PROFESIONAL_DE_SALUD", "ADMINISTRATIVO"),
				Arrays.asList("ROOT")
		));

		assertTrue("Unranked claims are lowest", RoleUtils.loggedUserHasHigherRank(
				Arrays.asList("ADMINISTRADOR"),
				Arrays.asList("ESPECIALISTA_MEDICO", "PROFESIONAL_DE_SALUD", "ADMINISTRATIVO")
		));

		assertTrue("Unranked claims are lowest", RoleUtils.loggedUserHasHigherRank(
				Arrays.asList("ADMINISTRADOR"),
				Arrays.asList("CUALQUIER OTRO")
		));
	}

	@Test
	public void loggedUserHasHigherRank_emptyClaims() {
		assertFalse("Empty claims ain't higher", RoleUtils.loggedUserHasHigherRank(
				Collections.emptyList(),
				Collections.emptyList()
		));
	}

	@Test
	public void loggedUserHasHigherRank_rootClaimRank() {
		assertTrue("ROOT should be higher", RoleUtils.loggedUserHasHigherRank(
				Arrays.asList("ROOT"),
				Arrays.asList("ADMINISTRADOR", "ESPECIALISTA_MEDICO", "PROFESIONAL_DE_SALUD", "ADMINISTRATIVO")
		));

		assertFalse("ROOT should be higher", RoleUtils.loggedUserHasHigherRank(
				Arrays.asList("ADMINISTRADOR", "ESPECIALISTA_MEDICO", "PROFESIONAL_DE_SALUD", "ADMINISTRATIVO"),
				Arrays.asList("ROOT")
		));
	}

	@Test
	public void loggedUserHasHigherRank_administradorClaimRank() {
		assertTrue("ADMINISTRADOR should be higher", RoleUtils.loggedUserHasHigherRank(
				Arrays.asList("ADMINISTRADOR"),
				Arrays.asList("ESPECIALISTA_MEDICO", "PROFESIONAL_DE_SALUD", "ADMINISTRATIVO")
		));

		assertFalse("ADMINISTRADOR should be higher", RoleUtils.loggedUserHasHigherRank(
				Arrays.asList("ESPECIALISTA_MEDICO", "PROFESIONAL_DE_SALUD", "ADMINISTRATIVO"),
				Arrays.asList("ADMINISTRADOR")
		));
	}

}