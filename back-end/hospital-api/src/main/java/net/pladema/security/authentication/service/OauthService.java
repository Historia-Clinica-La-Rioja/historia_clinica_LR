package net.pladema.security.authentication.service;

import java.net.URISyntaxException;

import net.pladema.security.token.service.domain.JWToken;

public interface OauthService {
	String getOauthToken(String code) throws URISyntaxException;

	JWToken login(String code) throws Exception;
	
	String getLoginUrl();
	
	Boolean getOauthEnabled();
}
