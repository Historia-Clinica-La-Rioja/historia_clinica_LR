package ar.lamansys.sgx.auth.user.infrastructure.output.oauthuser.configuration;

import ar.lamansys.sgx.shared.restclient.configuration.resttemplate.RestTemplateAuth;
import org.springframework.stereotype.Service;

@Service
public class OAuthRestTemplateAuth extends RestTemplateAuth<OAuthAuthInterceptor> {

	public OAuthRestTemplateAuth(OAuthAuthInterceptor authInterceptor) throws Exception {
		super(authInterceptor);
	}

}
