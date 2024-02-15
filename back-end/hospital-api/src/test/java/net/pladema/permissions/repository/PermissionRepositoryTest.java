package net.pladema.permissions.repository;

import static net.pladema.TestUtils.assertCreateAuditableEntity;
import static net.pladema.permissions.RoleTestUtils.createPermission;
import static org.assertj.core.api.Assertions.assertThat;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentFileRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import net.pladema.UnitRepository;
import net.pladema.permissions.repository.entity.Permission;

import org.springframework.boot.test.mock.mockito.MockBean;

@ExtendWith(MockitoExtension.class)
class PermissionRepositoryTest extends UnitRepository {

	@Autowired
	private PermissionRepository permissionRepository;

	@MockBean
	private DocumentFileRepository documentFileRepository;

	@BeforeEach
	public void setUp() {
	}

	@Test
	void saveCreateTest() {
		Permission newPermission = createPermission();
		Permission createdPermission = permissionRepository.saveAndFlush(newPermission);
		
		assertThat(createdPermission)
			.isNotNull();
	
		assertThat(createdPermission.getId())
			.isNotNull();
	
		assertCreateAuditableEntity(createdPermission);
	}
}
