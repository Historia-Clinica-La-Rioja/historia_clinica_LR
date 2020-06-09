package net.pladema.security.authentication.service;

import net.pladema.security.authentication.business.OauthUser;
import net.pladema.security.token.service.domain.JWToken;

import java.net.URISyntaxException;

public interface OauthService {
	String getOauthToken(String code) throws URISyntaxException;

	OauthUser getApiDataChaco(String token) throws URISyntaxException;

	JWToken loginChaco(String code) throws Exception;
	
	String getLoginUrl();
	
	Boolean getOauthEnabled();
}
