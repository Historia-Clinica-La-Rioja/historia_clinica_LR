package net.pladema.permissions.repository;

import static net.pladema.TestUtils.assertCreateAuditableEntity;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import net.pladema.permissions.repository.entity.UserRole;
import net.pladema.permissions.repository.enums.ERole;

@RunWith(SpringRunner.class)
@DataJpaTest(showSql = false)
public class UserRoleRepositoryTest {
	private final static Integer USER_ID = 1008;
	private final static Short ROLE_ID = ERole.ESPECIALISTA_MEDICO.getId();

	@Autowired
	private UserRoleRepository userLicenseRepository;

	@Before
	public void setUp() throws Exception {
	}
	
	@Test
	public void save_ok() {
		UserRole userRole = new UserRole(USER_ID, ROLE_ID);
		UserRole createdUserRole = userLicenseRepository.saveAndFlush(userRole);
		
		assertThat(createdUserRole)
			.isNotNull();
	
		assertThat(createdUserRole.getUserRolePK())
			.isNotNull();
	
		assertCreateAuditableEntity(createdUserRole);
					
		assertThat(createdUserRole.getRoleId())
			.isNotNull()
			.isEqualTo(ROLE_ID);
		
		assertThat(createdUserRole.getUserId())
			.isNotNull()
			.isEqualTo(USER_ID);
			
	}
}
