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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class RoleServiceImplTest {
	private final static Integer USER_ID = 1008;

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

		licenseServiceImpl.createUserRole(USER_ID, ERole.ADVANCED_USER);
	}

}
