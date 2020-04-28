package net.pladema.security.authentication.service.impl;

import net.pladema.security.token.service.TokenService;
import net.pladema.security.token.service.domain.JWToken;
import net.pladema.security.token.service.domain.JWTokenBean;
import net.pladema.security.token.service.domain.Login;
import net.pladema.user.repository.entity.User;
import net.pladema.user.repository.entity.UserBean;
import net.pladema.user.service.UserPasswordService;
import net.pladema.user.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class AuthenticationServiceImplTest {

	private AuthenticationServiceImpl authenticationService;

	@MockBean
	private TokenService tokenService;

	@MockBean
	private UserService userService;

	@MockBean
	private UserPasswordService userPasswordService;

	private Login invalidUsernameLogin = new Login("user1", "1234");

	private Login disabledUsernameLogin = new Login("user2", "1234");
	private User disabledUsernameUser = UserBean.newUser(1, "user2", false);

	private Login invalidPasswordLogin = new Login("user3", "1234");
	private User invalidPasswordUser = UserBean.newUser(2, "user3", true);

	private Login rightLogin = new Login("user4", "1234");
	private User rightUser = UserBean.newUser(3, "user4", true);

	@Before
	public void setUp() {
		authenticationService = new AuthenticationServiceImpl(
				tokenService,
				userService,
				userPasswordService
		);

		when(userService.getUser(eq(invalidUsernameLogin.getUsername())))
				.thenReturn(Optional.empty());

		when(userService.getUser(eq(disabledUsernameLogin.getUsername())))
				.thenReturn(Optional.of(disabledUsernameUser));

		when(userService.getUser(eq(invalidPasswordLogin.getUsername())))
				.thenReturn(Optional.of(invalidPasswordUser));
		when(userPasswordService.validCredentials(any(), eq(invalidPasswordUser.getId())))
				.thenReturn(false);

		when(userService.getUser(eq(rightLogin.getUsername())))
				.thenReturn(Optional.of(rightUser));
		when(userPasswordService.validCredentials(any(), eq(rightUser.getId())))
				.thenReturn(true);

		when(tokenService.generateToken(any()))
				.thenReturn(JWTokenBean.newJWToken("TOKEN", "REFRESHTOKEN"));

	}

	@Test(expected = BadCredentialsException.class)
	public void login_invalidUsername() {
		authenticationService.login(invalidUsernameLogin);
	}

	@Test(expected = BadCredentialsException.class)
	public void login_disabledUsername() {
		authenticationService.login(disabledUsernameLogin);
	}

	@Test(expected = BadCredentialsException.class)
	public void login_invalidPassword() {
		authenticationService.login(invalidPasswordLogin);
	}

	@Test
	public void login_right() {
		JWToken token = authenticationService.login(rightLogin);
		assertThat(token)
				.hasFieldOrPropertyWithValue("token", "TOKEN")
				.hasFieldOrPropertyWithValue("refreshToken", "REFRESHTOKEN");
	}
}