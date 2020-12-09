package net.pladema.security.token.service;

import net.pladema.security.token.service.domain.JWToken;

public interface TokenService {

	JWToken generateToken(String username);

	JWToken refreshTokens(String refreshToken);

	String generateVerificationToken(Integer userId);

	boolean validVerificationToken(String verificationToken);

}
