package net.pladema.snowstorm.services;

import net.pladema.sgx.restclient.configuration.resttemplate.RestTemplateSSL;
import net.pladema.sgx.restclient.services.AuthService;
import net.pladema.sgx.restclient.services.domain.WSResponseException;
import net.pladema.snowstorm.configuration.SnowstormWSConfig;
import net.pladema.snowstorm.services.domain.SnowstormLoginResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class SnowstormAuthService extends AuthService<SnowstormLoginResponse> {

    public SnowstormAuthService(@Value("${ws.snowstorm.url.login:/}") String relUrl,
            @Qualifier("baseRestTemplate") RestTemplateSSL restTemplateSSL, SnowstormWSConfig wsConfig) {
        super(relUrl, restTemplateSSL, wsConfig);
    }

    @Override
    protected ResponseEntity<SnowstormLoginResponse> callLogin() throws WSResponseException {
        return ResponseEntity.ok().body(new SnowstormLoginResponse("Valido"));
    }
}
