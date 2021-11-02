package net.pladema.permissions.service.impl;


import ar.lamansys.sgx.shared.auth.user.SecurityContextUtils;
import net.pladema.permissions.controller.external.LoggedUserExternalServiceImpl;
import net.pladema.permissions.repository.enums.ERole;
import net.pladema.permissions.service.LoggedUserService;
import net.pladema.permissions.service.LoggedUserStorage;
import net.pladema.permissions.service.UserAssignmentService;
import net.pladema.permissions.service.domain.LoggedUserBo;
import net.pladema.permissions.service.dto.RoleAssignment;
import net.pladema.permissions.service.exceptions.LoggedUserException;
import net.pladema.permissions.service.exceptions.LoggedUserExceptionEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Predicate;

@Service
public class LoggedUserServiceImpl implements LoggedUserService {


	private static final Logger LOG = LoggerFactory.getLogger(LoggedUserExternalServiceImpl.class);
	public static final String OUTPUT = "Output -> {}";

	private final UserAssignmentService userAssignmentService;

	private final LoggedUserStorage loggedUserStorage;

	public LoggedUserServiceImpl(UserAssignmentService userAssignmentService,
								 LoggedUserStorage loggedUserStorage) {
		this.userAssignmentService = userAssignmentService;
		this.loggedUserStorage = loggedUserStorage;
	}


	@Override
	public Integer getUserId() {
		return SecurityContextUtils.getUserDetails().userId;
	}

	@Override
	public List<RoleAssignment> getPermissionAssignment() {
		return userAssignmentService.getRoleAssignment(getUserId());
	}

	@Override
	public LoggedUserBo getInfo() {
		return loggedUserStorage.getUserInfo(this.getUserId())
				.orElseThrow(() -> new LoggedUserException(LoggedUserExceptionEnum.INVALID_USER, "Usuario invalido"));
	}

	@Override
	public boolean hasAnyRoleInstitution(Integer institutionId, List<ERole> roles) {
		LOG.debug("Input parameters -> institutionId {}, roles {}", institutionId, roles);
		Predicate<RoleAssignment> filter = (ra) -> (ra.getInstitutionId().equals(institutionId) && roles.contains(ra.getRole()));
		boolean result = getPermissionAssignment().stream().anyMatch(filter);
		LOG.debug(OUTPUT, result);
		return result;
	}
}
