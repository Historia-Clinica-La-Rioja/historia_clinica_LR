package net.pladema.user.controller.filters;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import net.pladema.permissions.repository.entity.Role;
import net.pladema.permissions.repository.entity.UserRole;
import net.pladema.permissions.repository.enums.ERole;

@Service
public class BackofficeRolesFilter {
	private static final List<ERole> ALWAYS_HIDE = List.of(
			ERole.ROOT,
			ERole.PARTIALLY_AUTHENTICATED,
			ERole.API_CONSUMER // no se debería asignar mas
	);

	public static final List<ERole> PUBLIC_API_ROLES = List.of(
			ERole.API_FACTURACION,
			ERole.API_TURNOS,
			ERole.API_PACIENTES,
			ERole.API_RECETAS,
			ERole.API_SIPPLUS,
			ERole.API_USERS,
			ERole.API_IMAGENES,
			ERole.API_ORQUESTADOR,
			ERole.API_ANEXO,
			ERole.API_REPORTES
	);

	private final List<ERole> rolesToHide;

	public BackofficeRolesFilter() {

		List<ERole> optApiPublicRoles = Collections.emptyList();

		this.rolesToHide = Stream.concat(
				ALWAYS_HIDE.stream(),
				optApiPublicRoles.stream()
			).collect(Collectors.toList());

	}

	public boolean filterRoles(UserRole userRole) {
		return rolesToHide.stream()
				.noneMatch(ERole.map(userRole.getRoleId())::equals);
	}

	public boolean filterRoles(Role role) {
		return rolesToHide.stream()
				.noneMatch(ERole.map(role.getId())::equals);
	}

}
