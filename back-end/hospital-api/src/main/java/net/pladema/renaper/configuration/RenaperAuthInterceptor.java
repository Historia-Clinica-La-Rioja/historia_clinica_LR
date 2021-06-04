package net.pladema.renaper.configuration;

import org.springframework.context.annotation.Conditional;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import net.pladema.renaper.services.RenaperAuthService;
import net.pladema.renaper.services.domain.RenaperLoginResponse;
import ar.lamansys.sgx.shared.restclient.configuration.TokenHolder;
import ar.lamansys.sgx.shared.restclient.configuration.interceptors.AuthInterceptor;

@Component
@Conditional(RenaperCondition.class)
public class RenaperAuthInterceptor extends AuthInterceptor<RenaperLoginResponse,RenaperAuthService> {
	
	private static final String COD_DOMINIO = "codDominio";
	private static final String TOKEN_INTERCEPTOR = "token";
	
	private RenaperWSConfig renaperWSConfig;
	
	public RenaperAuthInterceptor(RenaperAuthService authService, RenaperWSConfig renaperWSConfig) {
		super(authService, new TokenHolder(renaperWSConfig.getTokenExpiration()));
		this.renaperWSConfig = renaperWSConfig;
	}

	@Override
	protected void addAuthHeaders(HttpHeaders headers) {
		headers.add(TOKEN_INTERCEPTOR, token.get());
		headers.add(COD_DOMINIO, renaperWSConfig.getDominio());
	}

}
