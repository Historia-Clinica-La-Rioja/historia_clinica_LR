package net.pladema.permissions.controller.external;

import ar.lamansys.sgh.shared.infrastructure.input.service.RoleInfoDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedPermissionPort;
import net.pladema.permissions.application.fetchrolesfromtoken.FetchRolesFromToken;
import net.pladema.permissions.service.UserAssignmentService;
import net.pladema.permissions.service.dto.RoleAssignment;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PermissionModuleService implements SharedPermissionPort {

    private final UserAssignmentService userAssignmentService;

	private final FetchRolesFromToken fetchRolesFromToken;

    public PermissionModuleService(UserAssignmentService userAssignmentService,
								   FetchRolesFromToken fetchRolesFromToken) {
        this.userAssignmentService = userAssignmentService;
		this.fetchRolesFromToken = fetchRolesFromToken;
	}

    @Override
    public List<RoleInfoDto> ferPermissionInfoByUserId(Integer userId) {
        return userAssignmentService.getRoleAssignment(userId)
                .stream()
                .map(this::mapTo)
                .collect(Collectors.toList());
    }

	@Override
	public List<RoleInfoDto> fetchAuthoritiesFromToken(String userToken) {
		return fetchRolesFromToken.execute(userToken)
				.stream()
				.map(this::mapTo)
				.collect(Collectors.toList());
	}

	private RoleInfoDto mapTo(RoleAssignment roleAssignment) {
        return new RoleInfoDto(roleAssignment.getRole().getId(),
                roleAssignment.getInstitutionId(),
                roleAssignment.getRole().getValue());
    }
}
