package net.pladema.federar.services;

import ar.lamansys.sgx.shared.restclient.configuration.resttemplate.RestTemplateSSL;
import ar.lamansys.sgx.shared.restclient.services.AuthService;
import ar.lamansys.sgx.shared.restclient.services.domain.WSResponseException;
import net.pladema.federar.configuration.FederarCondition;
import net.pladema.federar.configuration.FederarWSConfig;
import net.pladema.federar.services.domain.FederarLoginPayload;
import net.pladema.federar.services.domain.FederarLoginResponse;
import net.pladema.federar.services.domain.FederarValidateTokenPayload;
import net.pladema.federar.services.domain.FederarValidateTokenResponse;
import org.springframework.context.annotation.Conditional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Conditional(FederarCondition.class)
public class FederarAuthService extends AuthService<FederarLoginResponse> {

	private FederarWSConfig federarWSConfig;

	public FederarAuthService(FederarWSConfig wsConfig) throws Exception {
		super(wsConfig.getAuthenticationPath(), new RestTemplateSSL(), wsConfig);
		federarWSConfig = wsConfig;
	}

	@Override
	protected ResponseEntity<FederarLoginResponse> callLogin() throws WSResponseException {
		ResponseEntity<FederarLoginResponse> result = null;
		try {
			result = exchangePost(relUrl,
					new FederarLoginPayload(federarWSConfig.getGrantType(), federarWSConfig.getScope(),
							federarWSConfig.getClientAssertionType(), generateClientAssertion()),
					FederarLoginResponse.class);
		} catch (Exception e) {
			throw new WSResponseException("Error al codificar SignKey -> " + e.getMessage() ) ;
		}
		return result;
	}

	private String generateClientAssertion() {
		return JWTUtils.generateJWT(federarWSConfig.getClaims(), federarWSConfig.getSignKey(), (int) federarWSConfig.getTokenExpiration());
	}

	@Override
	protected void assertValidResponse(FederarLoginResponse loginResponse) throws WSResponseException {
		super.assertValidResponse(loginResponse);
		ResponseEntity<FederarValidateTokenResponse> assertTokenResponse = exchangePost(federarWSConfig.getAuthorizationPath(),
				new FederarValidateTokenPayload(loginResponse.getAccessToken()),
				FederarValidateTokenResponse.class);
		super.assertValidStatusCode(assertTokenResponse);
	}

}
