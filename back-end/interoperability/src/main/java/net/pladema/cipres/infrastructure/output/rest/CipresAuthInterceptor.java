package net.pladema.cipres.infrastructure.output.rest;

import ar.lamansys.sgx.shared.restclient.configuration.TokenHolder;
import ar.lamansys.sgx.shared.restclient.configuration.interceptors.AuthInterceptor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.cipres.infrastructure.output.rest.domain.CipresLoginResponse;

import org.springframework.http.HttpHeaders;

import org.springframework.stereotype.Component;
@Component
@Slf4j
public class CipresAuthInterceptor extends AuthInterceptor<CipresLoginResponse, CipresLoginStorage> {

	public CipresAuthInterceptor(CipresLoginStorage cipresLoginStorage, CipresWSConfig cipresWSConfig) {
		super(cipresLoginStorage, new TokenHolder(cipresWSConfig.getTokenExpiration()));
	}

	@Override
	protected void addAuthHeaders(HttpHeaders headers) {
		headers.setBearerAuth(token.get());
	}

}