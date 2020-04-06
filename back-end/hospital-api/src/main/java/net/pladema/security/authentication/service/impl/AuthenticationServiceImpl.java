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

	private static final Logger LOG = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

	private final TokenService tokenService;

	private final UserService userService;

	private final UserPasswordService userPasswordService;

	public AuthenticationServiceImpl(TokenService tokenService, UserService userService,
			UserPasswordService userPasswordService) {
		super();
		this.tokenService = tokenService;
		this.userService = userService;
		this.userPasswordService = userPasswordService;
	}

	@Override
	public JWToken login(Login login) {
		Optional<User> opUser = Optional
				.ofNullable(userService.getUser(login.getUsername()));
		return opUser.map(u -> {
			if (validLogin(login, u.getId())) {
				LOG.debug("{}", "Credenciales validas");
				userService.updateLoginDate(opUser.get().getId());
				return tokenService.generateToken(login);
			}
			throw new BadCredentialsException("invalid.username");
		}).orElseThrow(() -> new BadCredentialsException("invalid.username"));
	}

	protected boolean validLogin(Login login, Integer userId) {
		return userPasswordService.validCredentials(login.getPassword(), userId);
	}

}
