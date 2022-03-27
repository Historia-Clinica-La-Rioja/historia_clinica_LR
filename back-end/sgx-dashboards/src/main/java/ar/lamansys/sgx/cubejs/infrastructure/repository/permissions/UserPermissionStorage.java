package ar.lamansys.sgx.cubejs.infrastructure.repository.permissions;

import java.util.List;

public interface UserPermissionStorage {
	List<DashboardRoleInfoDto> fetchPermissionInfoByUserId(Integer userId);
}
