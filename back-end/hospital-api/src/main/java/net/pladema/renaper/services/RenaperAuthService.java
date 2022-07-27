package net.pladema.renaper.services;

import ar.lamansys.sgx.shared.restclient.configuration.HttpClientConfiguration;
import ar.lamansys.sgx.shared.restclient.configuration.resttemplate.RestTemplateSSL;
import ar.lamansys.sgx.shared.restclient.services.AuthService;
import ar.lamansys.sgx.shared.restclient.services.domain.WSResponseException;
import net.pladema.renaper.configuration.RenaperCondition;
import net.pladema.renaper.configuration.RenaperWSConfig;
import net.pladema.renaper.services.domain.RenaperLoginPayload;
import net.pladema.renaper.services.domain.RenaperLoginResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Conditional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Conditional(RenaperCondition.class)
public class RenaperAuthService extends AuthService<RenaperLoginResponse> {

	private static final Logger LOG = LoggerFactory.getLogger(RenaperAuthService.class);

	public static final String OUTPUT = "Output -> {}";

	private RenaperWSConfig renaperWSConfig;

	public RenaperAuthService(
			HttpClientConfiguration configuration,
			RenaperWSConfig wsConfig
	) throws Exception {
		super(wsConfig.getLoginPath(), new RestTemplateSSL(configuration), wsConfig);
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
