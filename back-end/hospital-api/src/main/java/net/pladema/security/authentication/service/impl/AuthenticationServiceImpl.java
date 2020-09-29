package net.pladema.security.authentication.service.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import net.pladema.security.authentication.service.AuthenticationService;
import net.pladema.security.token.service.TokenService;
import net.pladema.security.token.service.domain.JWToken;
import net.pladema.security.token.service.domain.Login;
import net.pladema.user.repository.entity.User;
import net.pladema.user.service.UserPasswordService;
import net.pladema.user.service.UserService;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

	private final Logger logger;

	private final TokenService tokenService;

	private final UserService userService;

	private final UserPasswordService userPasswordService;

	public AuthenticationServiceImpl(
			TokenService tokenService,
			UserService userService,
			UserPasswordService userPasswordService
	) {
		this.logger = LoggerFactory.getLogger(this.getClass());
		this.tokenService = tokenService;
		this.userService = userService;
		this.userPasswordService = userPasswordService;
	}

	@Override
	public JWToken login(Login login) {
		Optional<User> optUser = userService.getUser(login.getUsername());
		User user = optUser.orElseThrow(
				() -> new BadCredentialsException("invalid.credentials")
		);
		if (Boolean.FALSE.equals(user.getEnable())) {
			throw new BadCredentialsException("disabled.username");
		}
		if (!userPasswordService.validCredentials(login.getPassword(), user.getId())) {
			throw new BadCredentialsException("invalid.credentials");
		}

		logger.debug("Valid credentials");
		userService.updateLoginDate(user.getId());
		return tokenService.generateToken(login.getUsername());
	}

	@Override
	public JWToken refreshToken(String refreshToken) {
		return tokenService.refreshTokens(refreshToken);
	}


}
