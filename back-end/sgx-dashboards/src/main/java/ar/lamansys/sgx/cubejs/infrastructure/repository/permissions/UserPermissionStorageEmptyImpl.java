package ar.lamansys.sgx.cubejs.infrastructure.repository.permissions;


import java.util.Collections;
import java.util.List;

public class UserPermissionStorageEmptyImpl implements UserPermissionStorage {
	@Override
	public List<DashboardRoleInfoDto> fetchPermissionInfoByUserId(Integer userId) {
		return Collections.emptyList();
	}
}
