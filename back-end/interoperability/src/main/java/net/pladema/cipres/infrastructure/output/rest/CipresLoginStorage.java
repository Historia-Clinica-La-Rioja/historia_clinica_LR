package net.pladema.cipres.infrastructure.output.rest;

import ar.lamansys.sgx.shared.restclient.configuration.HttpClientConfiguration;
import ar.lamansys.sgx.shared.restclient.configuration.resttemplate.RestTemplateSSL;
import ar.lamansys.sgx.shared.restclient.services.AuthService;
import ar.lamansys.sgx.shared.restclient.services.domain.WSResponseException;

import net.pladema.cipres.infrastructure.output.rest.domain.CipresLoginPayload;


import net.pladema.cipres.infrastructure.output.rest.domain.CipresLoginResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CipresLoginStorage extends AuthService<CipresLoginResponse> {

	private final CipresWSConfig cipresWSConfig;

	public CipresLoginStorage(CipresWSConfig cipresWSConfig, HttpClientConfiguration configuration) throws Exception {
		super(cipresWSConfig.getLoginUrl(), new RestTemplateSSL(configuration), cipresWSConfig);
		this.cipresWSConfig = cipresWSConfig;
	}

	@Override
	public  ResponseEntity<CipresLoginResponse> callLogin() throws WSResponseException {
		ResponseEntity<CipresLoginResponse> result = null;
		try {
			result = exchangePost(relUrl, getLoginBody(), CipresLoginResponse.class);
		} catch (Exception e) {
			throw new WSResponseException("Error al intentar autenticarse en la api de salud -> " + e.getMessage() ) ;
		}
		return result;
	}

	private CipresLoginPayload getLoginBody() {
		return CipresLoginPayload.builder()
				.username(cipresWSConfig.getUsername())
				.password(cipresWSConfig.getPassword())
				.realusername(cipresWSConfig.getRealUserName())
				.build();
	}

}
