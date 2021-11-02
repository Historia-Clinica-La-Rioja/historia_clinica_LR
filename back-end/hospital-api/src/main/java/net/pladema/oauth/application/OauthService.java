package net.pladema.oauth.application;

import ar.lamansys.sgx.auth.jwt.application.login.exceptions.BadLoginException;
import ar.lamansys.sgx.auth.jwt.infrastructure.input.rest.dto.JWTokenDto;

import java.net.URISyntaxException;

public interface OauthService {
	JWTokenDto login(String code) throws URISyntaxException, BadLoginException;
	
	String getLoginUrl();
	
	Boolean getOauthEnabled();
}
