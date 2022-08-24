package net.pladema.snowstorm.configuration;

import ar.lamansys.sgx.shared.restclient.configuration.HttpClientConfiguration;
import ar.lamansys.sgx.shared.restclient.configuration.resttemplate.RestTemplateSSL;
import ar.lamansys.sgx.shared.restclient.services.AuthService;
import net.pladema.snowstorm.services.domain.SnowstormLoginResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class SnowstormAuthService extends AuthService<SnowstormLoginResponse> {

    public SnowstormAuthService(
			HttpClientConfiguration configuration,
			@Value("${ws.snowstorm.url.login:/}") String relUrl,
			SnowstormWSConfig wsConfig
	) throws Exception {
        super(relUrl, new RestTemplateSSL(configuration), wsConfig);
    }

    @Override
    protected ResponseEntity<SnowstormLoginResponse> callLogin() {
        return ResponseEntity.ok().body(new SnowstormLoginResponse("Valido"));
    }
}
