package net.pladema.oauth.domain;

import java.net.URISyntaxException;

public interface OauthDataService {

	OauthUser getUserData(String token) throws URISyntaxException;

}
