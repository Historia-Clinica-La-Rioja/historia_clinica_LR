package net.pladema.sgx.restclient.services;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import net.pladema.sgx.restclient.configuration.WSConfig;
import net.pladema.sgx.restclient.configuration.resttemplate.RestTemplateSSL;
import net.pladema.sgx.restclient.services.domain.LoginResponse;
import net.pladema.sgx.restclient.services.domain.WSResponseException;

public abstract class AuthService extends RestClient {

	protected final String relUrl;

	public AuthService(String relUrl, RestTemplateSSL restTemplateSSL, WSConfig tawsConfig) {
		super(restTemplateSSL, tawsConfig);
		this.relUrl = relUrl;
	}

	protected abstract ResponseEntity<LoginResponse> callLogin() throws WSResponseException;

	public LoginResponse login() throws WSResponseException {
		return getValidResponse(callLogin());
	}

	private static LoginResponse getValidResponse(ResponseEntity<LoginResponse> result) throws WSResponseException {
		assertValidStatusCode(result);
		LoginResponse loginResponse = result.getBody();
		assertValidResponse(loginResponse);
		return loginResponse;
	}

	private static void assertValidResponse(LoginResponse loginResponse) throws WSResponseException {
		if (loginResponse == null) {
			throw new WSResponseException("WS Response is null");
		}
		if (loginResponse.getToken() == null) {
			throw new WSResponseException("WS Response token is null");
		}
	}

	private static void assertValidStatusCode(ResponseEntity<?> responseEntity) throws WSResponseException {
		if (responseEntity.getStatusCode() != HttpStatus.OK) {
			throw new WSResponseException(
					String.format("WS Response status code is %s", responseEntity.getStatusCode()));
		}
	}
}
