package net.pladema.snowstorm.services;

import ar.lamansys.sgx.restclient.configuration.interceptors.LoggingRequestInterceptor;
import ar.lamansys.sgx.restclient.configuration.resttemplate.RestTemplateSSL;
import ar.lamansys.sgx.restclient.services.AuthService;
import ar.lamansys.sgx.restclient.services.domain.WSResponseException;
import net.pladema.snowstorm.configuration.SnowstormWSConfig;
import net.pladema.snowstorm.services.domain.SnowstormLoginResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class SnowstormAuthService extends AuthService<SnowstormLoginResponse> {

    public SnowstormAuthService(@Value("${ws.snowstorm.url.login:/}") String relUrl,
                                SnowstormWSConfig wsConfig) throws Exception {
        super(relUrl, new RestTemplateSSL(new LoggingRequestInterceptor()), wsConfig);
    }

    @Override
    protected ResponseEntity<SnowstormLoginResponse> callLogin() throws WSResponseException {
        return ResponseEntity.ok().body(new SnowstormLoginResponse("Valido"));
    }
}
