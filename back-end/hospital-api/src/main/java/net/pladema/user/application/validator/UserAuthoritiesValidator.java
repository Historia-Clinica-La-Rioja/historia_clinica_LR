package net.pladema.user.application.validator;

import net.pladema.permissions.RoleUtils;
import net.pladema.permissions.repository.UserRoleRepository;
import net.pladema.permissions.service.LoggedUserService;
import net.pladema.permissions.service.dto.RoleAssignment;
import net.pladema.sgx.exceptions.PermissionDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
public final class UserAuthoritiesValidator {

	private final LoggedUserService loggedUserService;

	private final Supplier<List<String>> loggedUserClaims;
	private final Function<Integer, List<String>> entityUserClaims;

	public UserAuthoritiesValidator(
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

	private static final List<String> toRoleName(List<RoleAssignment> assignments) {
		return assignments.stream()
				.map(assignment -> assignment.role.getValue())
				.collect(Collectors.toList());
	}
}
