package net.pladema.security.service;

import java.util.Optional;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import net.pladema.security.service.enums.ETokenType;

public interface SecurityService {

	boolean validateCredentials(String token);

	Optional<UsernamePasswordAuthenticationToken> getAppAuthentication(String token);

	Integer getUserId(String token);

	Boolean isTokenExpired(String verificationToken);

	boolean validType(String verificationToken, ETokenType verification);

}
