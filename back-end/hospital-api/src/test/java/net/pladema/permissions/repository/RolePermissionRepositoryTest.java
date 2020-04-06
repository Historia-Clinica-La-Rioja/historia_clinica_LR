package net.pladema.permissions.repository;

import static net.pladema.TestUtils.assertCreateAuditableEntity;
import static net.pladema.permissions.RoleTestUtils.createLicense;
import static net.pladema.permissions.RoleTestUtils.createPermission;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import net.pladema.BaseRepositoryTest;
import net.pladema.permissions.repository.entity.Role;
import net.pladema.permissions.repository.entity.RolePermission;
import net.pladema.permissions.repository.entity.Permission;

@RunWith(SpringRunner.class)
@DataJpaTest(showSql = false)
public class RolePermissionRepositoryTest extends BaseRepositoryTest {

	@Autowired
	private RolePermissionRepository rolePermissionRepository;

	@Before
	public void setUp() throws Exception {
	}
	
	@Test
	public void saveCreateTest() {
		Role role = save(createLicense("ADMIN"));
		Permission permission = save(createPermission());
		RolePermission rolePermission = new RolePermission(role.getId(), permission.getId());
		RolePermission createdRolePermission = rolePermissionRepository.saveAndFlush(rolePermission);
		
		assertThat(createdRolePermission)
			.isNotNull();
	
		assertThat(createdRolePermission.getRolePermissionPK())
			.isNotNull();
	
		assertCreateAuditableEntity(createdRolePermission);
		
		assertThat(createdRolePermission.getRoleId())
			.isNotNull()
			.isEqualTo(role.getId());
		
		assertThat(createdRolePermission.getPermissionId())
			.isNotNull()
			.isEqualTo(permission.getId());
			
	}
}
