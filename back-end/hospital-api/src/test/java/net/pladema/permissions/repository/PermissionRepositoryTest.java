package net.pladema.permissions.repository;

import static net.pladema.TestUtils.assertCreateAuditableEntity;
import static net.pladema.permissions.RoleTestUtils.createPermission;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import net.pladema.BaseRepositoryTest;
import net.pladema.permissions.repository.entity.Permission;

@RunWith(SpringRunner.class)
@DataJpaTest(showSql = false)
public class PermissionRepositoryTest extends BaseRepositoryTest {

	@Autowired
	private PermissionRepository permissionRepository;

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void saveCreateTest() {
		Permission newPermission = createPermission();
		Permission createdPermission = permissionRepository.saveAndFlush(newPermission);
		
		assertThat(createdPermission)
			.isNotNull();
	
		assertThat(createdPermission.getId())
			.isNotNull();
	
		assertCreateAuditableEntity(createdPermission);
	}
}
