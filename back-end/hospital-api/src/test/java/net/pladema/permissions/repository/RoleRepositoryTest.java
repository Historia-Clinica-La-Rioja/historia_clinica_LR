package net.pladema.permissions.repository;

import static net.pladema.TestUtils.assertCreateAuditableEntity;
import static net.pladema.permissions.RoleTestUtils.createLicense;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import net.pladema.BaseRepositoryTest;
import net.pladema.permissions.repository.entity.Role;

@RunWith(SpringRunner.class)
@DataJpaTest(showSql = false)
public class RoleRepositoryTest extends BaseRepositoryTest {

	@Autowired
	private RoleRepository roleRepository;

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void saveCreateTest() {
		Role newRole = createLicense("ADMIN");
		Role createdRole = roleRepository.saveAndFlush(newRole);
		
		assertThat(createdRole)
			.isNotNull();
	
		assertThat(createdRole.getId())
			.isNotNull();
	
		assertCreateAuditableEntity(createdRole);
	}
}
