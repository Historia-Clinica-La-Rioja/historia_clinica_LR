package net.pladema.permissions.repository;

import static net.pladema.TestUtils.assertCreateAuditableEntity;
import static org.assertj.core.api.Assertions.assertThat;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentFileRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import net.pladema.UnitRepository;
import net.pladema.permissions.repository.entity.UserRole;
import net.pladema.permissions.repository.enums.ERole;

import org.springframework.boot.test.mock.mockito.MockBean;

class UserRoleRepositoryTest extends UnitRepository {
	private final static Integer USER_ID = 1008;
	private final static Short ROLE_ID = ERole.ESPECIALISTA_MEDICO.getId();

	@Autowired
	private UserRoleRepository userLicenseRepository;

	@MockBean
	private DocumentFileRepository documentFileRepository;

	@BeforeEach
	void setUp() throws Exception {
	}
	
	@Test
	void save_ok() {
		UserRole userRole = new UserRole(USER_ID, ROLE_ID);
		UserRole createdUserRole = userLicenseRepository.saveAndFlush(userRole);
		
		assertThat(createdUserRole)
			.isNotNull();
	
		assertThat(createdUserRole.getUserId())
			.isNotNull();

		assertThat(createdUserRole.getRoleId())
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
