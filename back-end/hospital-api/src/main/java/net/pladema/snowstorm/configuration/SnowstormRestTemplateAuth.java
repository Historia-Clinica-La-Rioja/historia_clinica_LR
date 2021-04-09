package net.pladema.snowstorm.configuration;

import org.springframework.stereotype.Service;

import ar.lamansys.sgx.restclient.configuration.interceptors.LoggingRequestInterceptor;
import ar.lamansys.sgx.restclient.configuration.resttemplate.RestTemplateAuth;

@Service
public class SnowstormRestTemplateAuth extends RestTemplateAuth<SnowstormAuthInterceptor> {

    public SnowstormRestTemplateAuth(SnowstormAuthInterceptor authInterceptor) throws Exception {
        super(authInterceptor, new LoggingRequestInterceptor());
    }
}
