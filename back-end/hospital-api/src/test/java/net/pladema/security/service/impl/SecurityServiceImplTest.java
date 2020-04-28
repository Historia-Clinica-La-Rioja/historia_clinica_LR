package net.pladema.security.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

import java.util.Calendar;
import java.util.Optional;

import net.pladema.permissions.service.UserAssignmentService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import net.pladema.permissions.repository.RoleRepository;
import net.pladema.permissions.repository.UserRoleRepository;
import net.pladema.permissions.service.impl.RoleServiceImpl;
import net.pladema.security.service.impl.SecurityServiceImpl;

@RunWith(SpringRunner.class)
public class SecurityServiceImplTest {

	private static final String TOKEN = "TOKEN";

	private SecurityServiceImpl securityServiceImpl;

	@MockBean
	private UserAssignmentService userAssignmentService;

	@Before
	public void setUp() {
		securityServiceImpl = new SecurityServiceImpl(userAssignmentService);
	}

	@Test
	public void isTokenExpired_true() {
		SecurityServiceImpl mockSecurityService = Mockito.spy(securityServiceImpl);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -10);
		doReturn(Optional.of(cal.getTime())).when(mockSecurityService).getExpirationDateFromToken(any());
		assertThat(mockSecurityService.isTokenExpired(TOKEN)).isTrue();
	}

	@Test
	public void isTokenExpired_notDate() {
		SecurityServiceImpl mockSecurityService = Mockito.spy(securityServiceImpl);
		doReturn(Optional.empty()).when(mockSecurityService).getExpirationDateFromToken(any());
		assertThat(mockSecurityService.isTokenExpired(TOKEN)).isFalse();
	}

	@Test
	public void isTokenExpired_false() {
		SecurityServiceImpl mockSecurityService = Mockito.spy(securityServiceImpl);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, 10);
		doReturn(Optional.of(cal.getTime())).when(mockSecurityService).getExpirationDateFromToken(any());
		assertThat(mockSecurityService.isTokenExpired(TOKEN)).isFalse();
	}

}
