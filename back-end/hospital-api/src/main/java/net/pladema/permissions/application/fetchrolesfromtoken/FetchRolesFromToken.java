package net.pladema.permissions.application.fetchrolesfromtoken;

import lombok.extern.slf4j.Slf4j;

import net.pladema.permissions.application.ports.PermissionStorage;
import net.pladema.permissions.service.UserAssignmentService;
import net.pladema.permissions.service.dto.RoleAssignment;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class FetchRolesFromToken {

	private final UserAssignmentService userAssignmentService;

	private final PermissionStorage permissionStorage;

	public FetchRolesFromToken(UserAssignmentService userAssignmentService,
							   PermissionStorage permissionStorage) {
		this.userAssignmentService = userAssignmentService;
		this.permissionStorage = permissionStorage;
	}

	public List<RoleAssignment> execute(String token) {
		return permissionStorage.fetchUserIdFromToken(token)
				.map(userAssignmentService::getRoleAssignment)
				.orElseGet(ArrayList::new);
	}
}
