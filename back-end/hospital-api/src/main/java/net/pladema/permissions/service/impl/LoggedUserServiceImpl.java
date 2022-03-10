package net.pladema.permissions.service.impl;

import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ar.lamansys.sgx.shared.auth.user.SecurityContextUtils;
import net.pladema.permissions.controller.external.LoggedUserExternalServiceImpl;
import net.pladema.permissions.service.LoggedUserService;
import net.pladema.permissions.service.RoleAssignmentAuthority;
import net.pladema.permissions.service.dto.RoleAssignment;

@Service
public class LoggedUserServiceImpl implements LoggedUserService {

	private static final Logger LOG = LoggerFactory.getLogger(LoggedUserExternalServiceImpl.class);

	@Override
	public Integer getUserId() {
		return SecurityContextUtils.getUserDetails().userId;
	}

	@Override
	public Stream<RoleAssignment> currentAssignments() {
		return SecurityContextUtils.getAuthentication().getAuthorities().stream()
				.map(grantedAuthority -> (RoleAssignmentAuthority)grantedAuthority)
				.map(RoleAssignmentAuthority::assignment);
	}

}
