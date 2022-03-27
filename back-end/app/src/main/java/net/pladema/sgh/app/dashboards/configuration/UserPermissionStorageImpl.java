package net.pladema.sgh.app.dashboards.configuration;

import ar.lamansys.sgh.shared.infrastructure.input.service.SharedPermissionPort;
import ar.lamansys.sgx.cubejs.infrastructure.repository.permissions.DashboardRoleInfoDto;
import ar.lamansys.sgx.cubejs.infrastructure.repository.permissions.UserPermissionStorage;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Primary
public class UserPermissionStorageImpl implements UserPermissionStorage {

	private final SharedPermissionPort permissionPort;

	public UserPermissionStorageImpl(SharedPermissionPort permissionPort) {
		this.permissionPort = permissionPort;
	}

	@Override
	public List<DashboardRoleInfoDto> fetchPermissionInfoByUserId(Integer userId) {
		return permissionPort.ferPermissionInfoByUserId(userId)
				.stream()
				.map(roleInfoDto -> new DashboardRoleInfoDto(roleInfoDto.getId(), roleInfoDto.getInstitution(), roleInfoDto.getValue()))
				.collect(Collectors.toList());
	}
}
