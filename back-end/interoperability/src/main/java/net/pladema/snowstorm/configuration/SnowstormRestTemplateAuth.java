package net.pladema.snowstorm.configuration;

import ar.lamansys.sgx.shared.restclient.configuration.interceptors.LoggingRequestInterceptor;
import ar.lamansys.sgx.shared.restclient.configuration.resttemplate.RestTemplateAuth;
import org.springframework.stereotype.Service;

@Service
public class SnowstormRestTemplateAuth extends RestTemplateAuth<SnowstormAuthInterceptor> {

    public SnowstormRestTemplateAuth(SnowstormAuthInterceptor authInterceptor) throws Exception {
        super(authInterceptor, new LoggingRequestInterceptor());
    }
}
