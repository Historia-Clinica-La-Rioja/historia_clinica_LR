package ar.lamansys.sgx.auth.user.infrastructure.output.oauthuser.configuration;

import ar.lamansys.sgx.auth.oauth.infrastructure.output.config.OAuthWSConfig;
import ar.lamansys.sgx.shared.restclient.configuration.HttpClientConfiguration;
import ar.lamansys.sgx.shared.restclient.configuration.resttemplate.RestTemplateSSL;
import ar.lamansys.sgx.shared.restclient.services.AuthService;
import ar.lamansys.sgx.shared.restclient.services.domain.WSResponseException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class OAuthAuthService extends AuthService<OAuthLoginResponse> {

	private OAuthWSConfig oAuthWSConfig;

	public OAuthAuthService(
			HttpClientConfiguration configuration,
			OAuthWSConfig wsConfig
	) throws Exception {
		super(wsConfig.getFetchAccessToken(), getRestTemplateSSL(configuration), wsConfig);
		oAuthWSConfig = wsConfig;
	}

	private static RestTemplateSSL getRestTemplateSSL(
			HttpClientConfiguration configuration
	) throws Exception {
		var restTemplate = new RestTemplateSSL(
				configuration
		);
		restTemplate.getInterceptors().add(0, new OAuthLoginInterceptor()); // adds the interceptor in the first position
		return restTemplate;
	}

	@Override
	protected ResponseEntity<OAuthLoginResponse> callLogin() throws WSResponseException {
		ResponseEntity<OAuthLoginResponse> result = null;
		String loginPayload = buildLoginPayload();
		try {
			result = exchangePost(relUrl, loginPayload, OAuthLoginResponse.class);
		} catch (Exception e) {
			throw new WSResponseException("Error en login de administrador de usuarios en servidor de OAuth -> " + e.getMessage() ) ;
		}
		return result;
	}

	private String buildLoginPayload() {
		return "username=" + oAuthWSConfig.getUserAdminUsername() +
				"&password=" + oAuthWSConfig.getUserAdminPassword() +
				"&client_id=" + oAuthWSConfig.getClientId() +
				"&grant_type=password";
	}

}
