package net.pladema.permissions.service.impl;


import ar.lamansys.sgx.shared.auth.user.SecurityContextUtils;
import net.pladema.permissions.controller.external.LoggedUserExternalServiceImpl;
import net.pladema.permissions.repository.UserRoleRepository;
import net.pladema.permissions.repository.enums.ERole;
import net.pladema.permissions.service.LoggedUserService;
import net.pladema.permissions.service.domain.UserBo;
import net.pladema.permissions.service.dto.RoleAssignment;
import net.pladema.user.repository.UserRepository;
import net.pladema.user.repository.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Predicate;

@Service
public class LoggedUserServiceImpl implements LoggedUserService {


	private static final Logger LOG = LoggerFactory.getLogger(LoggedUserExternalServiceImpl.class);
	public static final String OUTPUT = "Output -> {}";

	private final UserRoleRepository userRoleRepository;

	private final UserRepository userRepository;

	public LoggedUserServiceImpl(UserRoleRepository userRoleRepository, UserRepository userRepository) {
		this.userRoleRepository = userRoleRepository;
		this.userRepository = userRepository;
	}

	@Override
	public Integer getUserId() {
		return SecurityContextUtils.getUserDetails().userId;
	}

	@Override
	public List<RoleAssignment> getPermissionAssignment() {
		return userRoleRepository.getRoleAssignments(getUserId());
	}

	@Override
	public UserBo getInfo() {
		User user = userRepository.findById(this.getUserId()).orElse(new User());
		return new UserBo(user);
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
