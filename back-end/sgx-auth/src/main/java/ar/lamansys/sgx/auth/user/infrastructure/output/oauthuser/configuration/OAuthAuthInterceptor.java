package ar.lamansys.sgx.auth.user.infrastructure.output.oauthuser.configuration;

import ar.lamansys.sgx.auth.oauth.infrastructure.output.config.OAuthWSConfig;
import ar.lamansys.sgx.shared.restclient.configuration.TokenHolder;
import ar.lamansys.sgx.shared.restclient.configuration.interceptors.AuthInterceptor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class OAuthAuthInterceptor extends AuthInterceptor<OAuthLoginResponse, OAuthAuthService> {

	public OAuthAuthInterceptor(OAuthAuthService authService, OAuthWSConfig oAuthWSConfig) {
		super(authService, new TokenHolder(oAuthWSConfig.getTokenExpiration()));
	}

	@Override
	protected void addAuthHeaders(HttpHeaders headers) {
		headers.setBearerAuth(token.get());
	}

}
