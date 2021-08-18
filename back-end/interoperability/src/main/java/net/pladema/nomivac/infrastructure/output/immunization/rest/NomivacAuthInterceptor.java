package net.pladema.nomivac.infrastructure.output.immunization.rest;

import ar.lamansys.sgx.shared.restclient.configuration.TokenHolder;
import ar.lamansys.sgx.shared.restclient.configuration.interceptors.AuthInterceptor;
import net.pladema.federar.configuration.FederarWSConfig;
import net.pladema.federar.services.FederarAuthService;
import net.pladema.federar.services.domain.FederarLoginResponse;
import net.pladema.nomivac.infrastructure.configuration.NomivacCondition;
import org.springframework.context.annotation.Conditional;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
@Conditional(NomivacCondition.class)
public class NomivacAuthInterceptor extends AuthInterceptor<FederarLoginResponse,FederarAuthService> {

	public NomivacAuthInterceptor(FederarAuthService authService, FederarWSConfig federarWSConfig) {
		super(authService, new TokenHolder(federarWSConfig.getTokenExpiration()));
	}

	@Override
	protected void addAuthHeaders(HttpHeaders headers) {
		headers.setBearerAuth(token.get());
	}

}
