package net.pladema.federar.configuration;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import net.pladema.federar.services.FederarAuthService;
import net.pladema.federar.services.domain.FederarLoginResponse;
import net.pladema.sgx.restclient.configuration.TokenHolder;
import net.pladema.sgx.restclient.configuration.interceptors.AuthInterceptor;

@Component
public class FederarAuthInterceptor extends AuthInterceptor<FederarLoginResponse,FederarAuthService> {
		
	public FederarAuthInterceptor(FederarAuthService authService, FederarWSConfig federarWSConfig) {
		super(authService, new TokenHolder(federarWSConfig.getTokenExpiration()));
	}

	@Override
	protected void addAuthHeaders(HttpHeaders headers) {
		headers.setBearerAuth(token.get());
	}

}
