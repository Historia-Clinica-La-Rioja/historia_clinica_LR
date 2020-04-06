package net.pladema.permissions.repository;

import static net.pladema.TestUtils.assertCreateAuditableEntity;
import static net.pladema.permissions.RoleTestUtils.createLicense;
import static net.pladema.user.UserTestUtils.createUser;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import net.pladema.BaseRepositoryTest;
import net.pladema.permissions.repository.entity.Role;
import net.pladema.permissions.repository.entity.UserRole;
import net.pladema.user.repository.entity.User;

@RunWith(SpringRunner.class)
@DataJpaTest(showSql = false)
public class UserRoleRepositoryTest extends BaseRepositoryTest {

	@Autowired
	private UserRoleRepository userLicenseRepository;

	@Before
	public void setUp() throws Exception {
	}
	
	@Test
	public void saveCreateTest() {
		Role role = save(createLicense("ADMIN"));
		User user = save(createUser("username9@mail.com"));
		UserRole userRole = new UserRole(user.getId(),role.getId());
		UserRole createdUserRole = userLicenseRepository.saveAndFlush(userRole);
		
		assertThat(createdUserRole)
			.isNotNull();
	
		assertThat(createdUserRole.getUserRolePK())
			.isNotNull();
	
		assertCreateAuditableEntity(createdUserRole);
					
		assertThat(createdUserRole.getRoleId())
			.isNotNull()
			.isEqualTo(role.getId());
		
		assertThat(createdUserRole.getUserId())
			.isNotNull()
			.isEqualTo(user.getId());
			
	}
}
