package net.pladema.permissions.service.impl;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ar.lamansys.sgx.shared.auth.user.SecurityContextUtils;
import net.pladema.permissions.controller.external.LoggedUserExternalServiceImpl;
import net.pladema.permissions.repository.enums.ERole;
import net.pladema.permissions.service.LoggedUserService;
import net.pladema.permissions.service.LoggedUserStorage;
import net.pladema.permissions.service.RoleAssignmentAuthority;
import net.pladema.permissions.service.UserAssignmentService;
import net.pladema.permissions.service.domain.LoggedUserBo;
import net.pladema.permissions.service.dto.RoleAssignment;
import net.pladema.permissions.service.exceptions.LoggedUserException;
import net.pladema.permissions.service.exceptions.LoggedUserExceptionEnum;

@Service
public class LoggedUserServiceImpl implements LoggedUserService {

	private static final Logger LOG = LoggerFactory.getLogger(LoggedUserExternalServiceImpl.class);

	private final LoggedUserStorage loggedUserStorage;

	public LoggedUserServiceImpl(LoggedUserStorage loggedUserStorage) {
		this.loggedUserStorage = loggedUserStorage;
	}

	@Override
	public Integer getUserId() {
		return SecurityContextUtils.getUserDetails().userId;
	}

	@Override
	public List<RoleAssignment> getPermissionAssignment() {
		return currentAssignments().collect(Collectors.toList());
	}

	@Override
	public LoggedUserBo getInfo() {
		return loggedUserStorage.getUserInfo(this.getUserId())
				.orElseThrow(() -> new LoggedUserException(LoggedUserExceptionEnum.INVALID_USER, "Usuario invalido"));
	}

}
