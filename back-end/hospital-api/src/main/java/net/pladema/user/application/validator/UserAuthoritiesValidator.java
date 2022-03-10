package net.pladema.user.application.validator;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import net.pladema.permissions.RoleUtils;
import net.pladema.permissions.repository.UserRoleRepository;
import net.pladema.permissions.service.dto.RoleAssignment;
import net.pladema.sgx.exceptions.PermissionDeniedException;
import net.pladema.sgx.session.application.port.UserSessionStorage;

@Service
public final class UserAuthoritiesValidator {

	private final UserSessionStorage userSessionStorage;

	private final Supplier<List<String>> loggedUserClaims;
	private final Function<Integer, List<String>> entityUserClaims;

	public UserAuthoritiesValidator(
			UserSessionStorage userSessionStorage,
			UserRoleRepository userRoleRepository
	) {
		this.userSessionStorage = userSessionStorage;
		this.loggedUserClaims = () -> toRoleName(userSessionStorage.getRolesAssigned());
		this.entityUserClaims = (Integer userId) -> toRoleName(userRoleRepository.getRoleAssignments(userId));
	}

	protected void assertLoggedUserOutrank(Integer entityUserId) {
		if (entityUserId == null) {
			throw new PermissionDeniedException("Operación no permitida");
		}
		assertLoggedUserOutrank(entityUserClaims.apply(entityUserId));
	}

	protected void assertLoggedUserOutrank(List<String> roleNames) {
		if (!RoleUtils.loggedUserHasHigherRank(loggedUserClaims.get(), roleNames)) {
			throw new PermissionDeniedException("No cuenta con suficientes privilegios");
		}
	}

	public boolean isLoggedUserId(Integer id) {
		return userSessionStorage.getUserId().equals(id);
	}

	private static List<String> toRoleName(Stream<RoleAssignment> assignments) {
		return assignments
				.map(assignment -> assignment.role.getValue())
				.collect(Collectors.toList());
	}

	private static List<String> toRoleName(List<RoleAssignment> assignments) {
		return toRoleName(assignments.stream());
	}
}
