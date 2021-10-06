package net.pladema.permissions.repository;

import static net.pladema.TestUtils.assertCreateAuditableEntity;
import static org.assertj.core.api.Assertions.assertThat;

import net.pladema.UnitRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import net.pladema.permissions.repository.entity.RolePermission;

@RunWith(SpringRunner.class)
public class RolePermissionRepositoryTest extends UnitRepository {
	private final static Short ROLE_ID = (short) 8;
	private final static Short PERMISSION_ID = (short) 108;

	@Autowired
	private RolePermissionRepository rolePermissionRepository;

	@Before
	public void setUp() throws Exception {
	}
	
	@Test
	public void saveCreateTest() {
		RolePermission rolePermission = new RolePermission(ROLE_ID, PERMISSION_ID);
		RolePermission createdRolePermission = rolePermissionRepository.saveAndFlush(rolePermission);
		
		assertThat(createdRolePermission)
			.isNotNull();
	
		assertThat(createdRolePermission.getRolePermissionPK())
			.isNotNull();
	
		assertCreateAuditableEntity(createdRolePermission);
		
		assertThat(createdRolePermission.getRoleId())
			.isNotNull()
			.isEqualTo(ROLE_ID);
		
		assertThat(createdRolePermission.getPermissionId())
			.isNotNull()
			.isEqualTo(PERMISSION_ID);
			
	}
}
