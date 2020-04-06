package net.pladema.security.authentication.service;

import net.pladema.security.token.service.domain.JWToken;
import net.pladema.security.token.service.domain.Login;

public interface AuthenticationService {

	public JWToken login(Login login);
}
