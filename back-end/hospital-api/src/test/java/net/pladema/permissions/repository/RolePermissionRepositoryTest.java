package net.pladema.permissions.repository;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentFileRepository;
import net.pladema.UnitRepository;
import net.pladema.permissions.repository.entity.RolePermission;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import static net.pladema.TestUtils.assertCreateAuditableEntity;
import static org.assertj.core.api.Assertions.assertThat;

class RolePermissionRepositoryTest extends UnitRepository {
	private final static Short ROLE_ID = (short) 8;
	private final static Short PERMISSION_ID = (short) 108;

	@Autowired
	private RolePermissionRepository rolePermissionRepository;

	@MockBean
	private DocumentFileRepository documentFileRepository;

	@BeforeEach
	void setUp() {
	}
	
	@Test
	void saveCreateTest() {
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
