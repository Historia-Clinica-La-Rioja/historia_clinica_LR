package net.pladema.user.controller;

import net.pladema.permissions.RoleUtils;
import net.pladema.permissions.repository.UserRoleRepository;
import net.pladema.permissions.repository.enums.ERole;
import net.pladema.permissions.service.LoggedUserService;
import net.pladema.permissions.service.dto.RoleAssignment;
import net.pladema.sgx.exceptions.PermissionDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
public final class BackofficeAuthoritiesValidator {

	private final LoggedUserService loggedUserService;

	private final Supplier<List<String>> loggedUserClaims;
	private final Function<Integer, List<String>> entityUserClaims;

	public BackofficeAuthoritiesValidator(
			LoggedUserService loggedUserService,
			UserRoleRepository userRoleRepository
	) {
		this.loggedUserService = loggedUserService;
		this.loggedUserClaims = () -> toRoleName(loggedUserService.getPermissionAssignment());
		this.entityUserClaims = (Integer userId) -> toRoleName(userRoleRepository.getRoleAssignments(userId));
	}

	protected void assertLoggedUserOutrank(Integer entityUserId) {
		if (entityUserId == null) {
			throw new PermissionDeniedException("Operación no permitida");
		}

		// validar que loggedUserId tiene mas rango que entityUserId
		assertLoggedUserOutrank(entityUserClaims.apply(entityUserId));
	}

	protected void assertLoggedUserOutrank(List<String> roleNames) {
		if (!RoleUtils.loggedUserHasHigherRank(loggedUserClaims.get(), roleNames)) {
			throw new PermissionDeniedException("No cuenta con suficientes privilegios");
		}
	}

	public boolean isLoggedUserId(Integer id) {
		return loggedUserService.getUserId().equals(id);
	}

	public boolean hasRole(ERole role) {
		return loggedUserService.getPermissionAssignment()
				.stream()
				.anyMatch(roleAssignment -> roleAssignment.role == role);
	}

	private static final List<String> toRoleName(List<RoleAssignment> assignments) {
		return assignments.stream()
				.map(assignment -> assignment.role.getValue())
				.collect(Collectors.toList());
	}

	public List<Integer> allowedInstitutionIds(List<ERole> permissions) {
		return loggedUserService.getPermissionAssignment().stream()
				.filter(roleAssignment -> permissions.contains(roleAssignment.role))
				.map(assignment -> assignment.institutionId)
				.collect(Collectors.toList());
	}
}
