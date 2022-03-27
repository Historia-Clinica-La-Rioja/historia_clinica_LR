package ar.lamansys.sgx.cubejs.infrastructure.repository.permissions;


import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@Order
public class UserPermissionStorageEmptyImpl implements UserPermissionStorage {
	@Override
	public List<DashboardRoleInfoDto> fetchPermissionInfoByUserId(Integer userId) {
		return Collections.emptyList();
	}
}
