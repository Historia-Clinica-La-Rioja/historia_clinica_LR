package net.pladema.renaper.configuration;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import net.pladema.renaper.services.RenaperAuthService;
import net.pladema.sgx.restclient.configuration.TokenHolder;
import net.pladema.sgx.restclient.configuration.interceptors.AuthInterceptor;

@Component
public class RenaperAuthInterceptor extends AuthInterceptor<RenaperAuthService> {
	
	private static final String COD_DOMINIO = "codDominio";
	private static final String TOKEN = "token";
	
	private RenaperWSConfig renaperWSConfig;
	
	public RenaperAuthInterceptor(RenaperAuthService authService, RenaperWSConfig renaperWSConfig) {
		super(authService, new TokenHolder(renaperWSConfig.getTokenExpiration()));
		this.renaperWSConfig = renaperWSConfig;
	}

	@Override
	protected void addAuthHeaders(HttpHeaders headers) {
		headers.add(TOKEN, token.get());
		headers.add(COD_DOMINIO, renaperWSConfig.getDominio());
	}

}
