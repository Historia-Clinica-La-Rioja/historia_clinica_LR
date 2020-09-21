package net.pladema.snowstorm.configuration;

import org.springframework.stereotype.Service;

import net.pladema.sgx.restclient.configuration.interceptors.LoggingRequestInterceptor;
import net.pladema.sgx.restclient.configuration.resttemplate.RestTemplateAuth;

@Service
public class SnowstormRestTemplateAuth extends RestTemplateAuth<SnowstormAuthInterceptor> {

    public SnowstormRestTemplateAuth(SnowstormAuthInterceptor authInterceptor,
                                     LoggingRequestInterceptor loggingRequestInterceptor) throws Exception {
        super(authInterceptor, loggingRequestInterceptor);
    }
}
