package net.pladema.permissions.service.impl;

import net.pladema.permissions.repository.RoleRepository;
import net.pladema.permissions.repository.UserRoleRepository;
import net.pladema.permissions.repository.entity.Role;
import net.pladema.permissions.repository.entity.UserRole;
import net.pladema.permissions.repository.enums.ERole;
import net.pladema.user.repository.entity.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static net.pladema.user.UserTestUtils.createUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class RoleServiceImplTest {

	@MockBean
	private UserRoleRepository userLicenseRepository;

	@MockBean
	private RoleRepository licenseRepository;

	private RoleServiceImpl licenseServiceImpl;

	@Before
	public void setUp() {
		licenseServiceImpl = new RoleServiceImpl(userLicenseRepository, licenseRepository);
	}

	@Test(expected = EntityNotFoundException.class)
	public void createUserLicense_notExistLicense() {
		when(licenseRepository.findByDescription(any())).thenReturn(Optional.empty());

		User user = createUser("username9@mail.com");
		licenseServiceImpl.createUserRole(user.getId(), ERole.PATIENT_USER);
	}

	@Test
	public void createUserLicence() {
		Short roleId = 1;
		Role role = new Role();
		role.setId(roleId);
		role.setDescription("PRUEBA");

		User user = createUser("username9@mail.com");

		when(licenseRepository.findById(roleId)).thenReturn(Optional.of(role));
		when(userLicenseRepository.save(any())).thenReturn(new UserRole(user.getId(), role.getId()));

		assertThat(licenseServiceImpl.createUserRole(user.getId(), roleId)).isNotNull();

	}
}
