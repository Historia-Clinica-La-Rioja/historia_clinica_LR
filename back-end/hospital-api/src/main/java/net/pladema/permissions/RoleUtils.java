package net.pladema.permissions;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.pladema.permissions.repository.enums.ERole;

public class RoleUtils {

	private RoleUtils() { }

	private static final List<String> CLAIMS_RANK = Stream.of(
			ERole.ROOT, // mayor rango, index 0
			ERole.ADMINISTRADOR,
			ERole.ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE
	).map(ERole::getValue).collect(Collectors.toList());

	public static boolean loggedUserHasHigherRank(List<String> loggedUserClaims, List<String> entityUserClaims) {
		int loggedUserRank = loggedUserClaims.stream()
				.filter(CLAIMS_RANK::contains)
				.map(CLAIMS_RANK::indexOf)
				.min(Integer::compareTo)
				.orElse(CLAIMS_RANK.size());
		int entityUserRank = entityUserClaims.stream()
				.filter(CLAIMS_RANK::contains)
				.map(CLAIMS_RANK::indexOf)
				.min(Integer::compareTo)
				.orElse(CLAIMS_RANK.size());
		return loggedUserRank < entityUserRank;
	}

	public static boolean hasProfessionalRole(List<ERole> roles){
		return !Collections.disjoint(roles, List.of(ERole.ESPECIALISTA_MEDICO, ERole.ENFERMERO, ERole.ESPECIALISTA_EN_ODONTOLOGIA, ERole.PROFESIONAL_DE_SALUD));
	}
}
