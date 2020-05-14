package net.pladema.renaper.services;

import net.pladema.person.controller.service.PersonExternalServiceImpl;
import net.pladema.renaper.configuration.RenaperWSConfig;
import net.pladema.renaper.services.domain.RenaperLoginPayload;
import net.pladema.renaper.services.domain.RenaperLoginResponse;
import net.pladema.sgx.restclient.configuration.resttemplate.RestTemplateSSL;
import net.pladema.sgx.restclient.services.AuthService;
import net.pladema.sgx.restclient.services.domain.WSResponseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class RenaperAuthService extends AuthService<RenaperLoginResponse> {

	private static final Logger LOG = LoggerFactory.getLogger(RenaperAuthService.class);

	public static final String OUTPUT = "Output -> {}";

	private RenaperWSConfig renaperWSConfig;

	public RenaperAuthService(
			@Value("${ws.renaper.url.login:/usuarios/aplicacion/login}") String relUrl,
			RestTemplateSSL restTemplateSSL, RenaperWSConfig wsConfig) {
		super(relUrl, restTemplateSSL, wsConfig);
		renaperWSConfig = wsConfig;
	}

	@Override
	protected ResponseEntity<RenaperLoginResponse> callLogin()  throws WSResponseException {
		ResponseEntity<RenaperLoginResponse> result = exchangePost(relUrl, new RenaperLoginPayload(renaperWSConfig.getNombre(),
				renaperWSConfig.getClave(), renaperWSConfig.getDominio()), RenaperLoginResponse.class);
		LOG.debug(OUTPUT, result);
		return result;
	}

}
