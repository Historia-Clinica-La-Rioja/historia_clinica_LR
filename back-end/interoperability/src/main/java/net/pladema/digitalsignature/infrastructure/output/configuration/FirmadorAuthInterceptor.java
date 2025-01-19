package net.pladema.digitalsignature.infrastructure.output.configuration;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import ar.lamansys.sgx.shared.restclient.configuration.TokenHolder;
import ar.lamansys.sgx.shared.restclient.configuration.interceptors.AuthInterceptor;
import net.pladema.digitalsignature.domain.FirmadorLoginResponse;

@Component
public class FirmadorAuthInterceptor extends AuthInterceptor<FirmadorLoginResponse, FirmadorAuthService> {

	private static final String AUTHORIZATION = "Authorization";

	public FirmadorAuthInterceptor(FirmadorAuthService authService, FirmadorWSConfig firmadorWSConfig) {
		super(authService, new TokenHolder(firmadorWSConfig.getTokenExpiration()));
	}

	@Override
	protected void addAuthHeaders(HttpHeaders headers) {
		headers.add(AUTHORIZATION, "Bearer " + token.get());

	}

}
