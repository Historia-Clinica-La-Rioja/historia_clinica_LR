package net.pladema.permissions;

import java.util.ArrayList;
import java.util.Arrays;
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

	public static List<Short> getProfessionalERoleIds() {
		List<Short> professionalRolIds = new ArrayList<>();
		for(ERole e : ERole.values()) {
			String rol = e.getValue();
			if (rol.equals(ERole.PROFESIONAL_DE_SALUD.getValue()) || rol.equals(ERole.ENFERMERO.getValue()) || rol.equals(ERole.ESPECIALISTA_MEDICO.getValue()) || rol.equals(ERole.ESPECIALISTA_EN_ODONTOLOGIA.getValue()))
				professionalRolIds.add(e.getId());
		}
		return professionalRolIds;
	}
}
