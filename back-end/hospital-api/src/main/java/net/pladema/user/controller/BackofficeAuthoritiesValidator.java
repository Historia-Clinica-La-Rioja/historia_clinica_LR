package net.pladema.user.controller;

import net.pladema.permissions.repository.enums.ERole;
import net.pladema.permissions.service.impl.RoleServiceImpl;
import net.pladema.sgx.exceptions.PermissionDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public final class BackofficeAuthoritiesValidator {
//	private static final List<String> CLAIMS_RANK = Stream.of(
//			ERole.ADMIN, // mayor rango, index 0
//			ERole.ADMIN_APP,
//			ERole.ADVANCED_USER,
//			ERole.BASIC_USER // menor rango, index 3
//	).map(ERole::getValue).collect(Collectors.toList());

	private final RoleServiceImpl roleService;

	public BackofficeAuthoritiesValidator(RoleServiceImpl roleService) {
		this.roleService = roleService;
	}

	private Integer loggedUserId() {
		return (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	protected void assertAllowed(Integer entityUserId) {
		if (entityUserId == null) {
			throw new PermissionDeniedException("Operación no permitida");
		}
		Integer loggedUserId = loggedUserId();
		if (entityUserId.equals(loggedUserId)) {
			return; // mismo usuario
		}
		// validar que loggedUserId tiene permisos para modificar entityUserId

//		List<String> loggedUserClaims = roleService.getAuthoritiesClaims(loggedUserId);
//		List<String> entityUserClaims = roleService.getAuthoritiesClaims(entityUserId);
//		if (!loggedUserHasHigherRank(loggedUserClaims, entityUserClaims)) {
//			throw new PermissionDeniedException("No cuenta con suficientes privilegios");
//		}
	}

//	protected void assertRolesOfLowerRank(List<Short> roleIds) {
//		List<String> loggedUserClaims = roleService.getAuthoritiesClaims(loggedUserId());
//		List<String> entityUserClaims = roleService.getAuthoritiesClaims(roleIds);
//		if (!loggedUserHasHigherRank(loggedUserClaims, entityUserClaims)) {
//			throw new PermissionDeniedException("Roles con demasiados privilegios");
//		}
//	}
//
// 	protected static boolean loggedUserHasHigherRank(List<String> loggedUserClaims, List<String> entityUserClaims) {
//		int loggedUserRank = loggedUserClaims.stream()
//				.filter(CLAIMS_RANK::contains)
//				.map(CLAIMS_RANK::indexOf)
//				.min(Integer::compareTo)
//				.orElse(CLAIMS_RANK.size());
//		int entityUserRank = entityUserClaims.stream()
//				.filter(CLAIMS_RANK::contains)
//				.map(CLAIMS_RANK::indexOf)
//				.min(Integer::compareTo)
//				.orElse(CLAIMS_RANK.size());
//		return loggedUserRank < entityUserRank;
//	}

	public boolean isLoggedUserId(Integer id) {
		return loggedUserId() == id;
	}
}
