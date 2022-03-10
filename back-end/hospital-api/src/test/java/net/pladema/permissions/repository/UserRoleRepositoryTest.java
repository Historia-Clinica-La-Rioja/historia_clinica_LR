package net.pladema.permissions.repository;

import net.pladema.UnitRepository;
import net.pladema.permissions.repository.entity.UserRole;
import net.pladema.permissions.repository.enums.ERole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static net.pladema.TestUtils.assertCreateAuditableEntity;
import static org.assertj.core.api.Assertions.assertThat;

class UserRoleRepositoryTest extends UnitRepository {
	private final static Integer USER_ID = 1008;
	private final static Short ROLE_ID = ERole.ESPECIALISTA_MEDICO.getId();

	@Autowired
	private UserRoleRepository userLicenseRepository;

	@BeforeEach
	void setUp() throws Exception {
	}
	
	@Test
	void save_ok() {
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
