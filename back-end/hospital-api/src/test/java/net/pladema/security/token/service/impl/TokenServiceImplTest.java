package net.pladema.security.token.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import net.pladema.permissions.service.RoleService;
import net.pladema.security.service.SecurityService;
import net.pladema.security.token.service.domain.JWToken;
import net.pladema.security.token.service.domain.Login;
import net.pladema.user.service.UserService;

@RunWith(SpringRunner.class)
public class TokenServiceImplTest {

	private static final String TOKEN = "TOKEN";

	@MockBean
	private RoleService licenceService;

	@MockBean
	private UserService userService;

	@MockBean
	private SecurityService securityService;

	private TokenServiceImpl tokenServiceImpl;

	@Before
	public void setUp() {
		tokenServiceImpl = new TokenServiceImpl(securityService, licenceService, userService);
	}

	@Test
	public void generateToken() {
		Login login = new Login("USERNAME", "PASSWORD");
		TokenServiceImpl mockTokenService = Mockito.spy(tokenServiceImpl);
		Date expiration = new Date();
		doReturn(expiration).when(mockTokenService).generateExpirationDate(any());
		doReturn(TOKEN).when(mockTokenService).tokenToString(any(), any());

		when(userService.getUserId(any())).thenReturn(1);

		List<String> authorities = new ArrayList<>();
		authorities.add("ADMIN");
		when(licenceService.getAuthoritiesClaims(any())).thenReturn(authorities);

		JWToken token = mockTokenService.generateToken(login);
		assertThat(token).isNotNull().hasFieldOrPropertyWithValue("token", TOKEN)
				.hasFieldOrPropertyWithValue("refreshToken", TOKEN);

	}

}
