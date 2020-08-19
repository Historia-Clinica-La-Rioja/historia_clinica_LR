package net.pladema.permissions.controller.external;

import net.pladema.permissions.repository.enums.ERole;
import net.pladema.permissions.service.dto.RoleAssignment;

import java.util.List;

public interface LoggedUserExternalService {

	List<RoleAssignment> getPermissionAssignment();

	boolean hasAnyRoleInstitution(Integer institutionId, List<ERole> role);
}
