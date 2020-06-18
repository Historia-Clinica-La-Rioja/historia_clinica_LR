package net.pladema.security.authentication.service;

import java.net.URISyntaxException;

import net.pladema.security.authentication.business.OauthUser;

public interface OauthDataService {

	OauthUser getUserData(String token) throws URISyntaxException;

}
