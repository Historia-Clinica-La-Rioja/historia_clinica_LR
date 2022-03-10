package net.pladema.sgx.session.infrastructure.input.rest;

import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import net.pladema.authorization.application.port.exceptions.InvalidUserException;
import net.pladema.authorization.infrastructure.input.rest.mapper.RoleNameMapper;
import net.pladema.sgx.session.application.fetchloggeduserinfo.FetchLoggedUserInfo;
import net.pladema.sgx.session.infrastructure.input.rest.dto.LoggedUserDto;
import net.pladema.sgx.session.infrastructure.input.rest.dto.PermissionsDto;
import net.pladema.sgx.session.infrastructure.input.rest.dto.RoleAssignmentDto;
import net.pladema.sgx.session.application.fetchloggeduserroles.FetchLoggedUserRoles;

@Slf4j
@RestController
@RequestMapping("/account")
public class LoggedUserController {

	private final FetchLoggedUserRoles getLoggedUserRoles;
	private final FetchLoggedUserInfo getLoggedUserInfo;
	private final RoleNameMapper roleNameMapper;

	public LoggedUserController(
			FetchLoggedUserRoles getLoggedUserRoles,
			FetchLoggedUserInfo getLoggedUserInfo,
			RoleNameMapper roleNameMapper
	) {
		this.getLoggedUserRoles = getLoggedUserRoles;
		this.getLoggedUserInfo = getLoggedUserInfo;
		this.roleNameMapper = roleNameMapper;
	}
	@GetMapping(value = "/permissions")
	public PermissionsDto getPermissions() {

		var roleAssignments = getLoggedUserRoles.execute().map(
				userRoleBo -> new RoleAssignmentDto(
						userRoleBo.role,	//
						roleNameMapper.getRoleDescription(userRoleBo.role), //
						userRoleBo.institutionId //
				)
		).collect(Collectors.toList());

		log.debug("Output -> {}", roleAssignments);
		return new PermissionsDto(roleAssignments);
	}

	@GetMapping(value = "/info")
	public LoggedUserDto getInfo() throws InvalidUserException {
		var loggedUserBo = getLoggedUserInfo.execute();
		return new LoggedUserDto(loggedUserBo, loggedUserBo.avatar.get());
	}

}
