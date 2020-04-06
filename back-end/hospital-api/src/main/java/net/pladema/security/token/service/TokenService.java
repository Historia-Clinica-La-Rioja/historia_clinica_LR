package net.pladema.security.token.service;

import net.pladema.security.token.service.domain.JWToken;
import net.pladema.security.token.service.domain.Login;

public interface TokenService {

	JWToken generateToken(Login login);

	String generateVerificationToken(Integer userId);

	boolean validVerificationToken(String verificationToken);

}
