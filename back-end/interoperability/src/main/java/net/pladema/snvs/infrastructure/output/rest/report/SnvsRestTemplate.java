package net.pladema.snvs.infrastructure.output.rest.report;

import ar.lamansys.sgx.shared.restclient.configuration.interceptors.LoggingRequestInterceptor;
import ar.lamansys.sgx.shared.restclient.configuration.resttemplate.RestTemplateAuth;
import net.pladema.snvs.infrastructure.configuration.SnvsCondition;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;

@Service
@Conditional(SnvsCondition.class)
public class SnvsRestTemplate extends RestTemplateAuth {

    public SnvsRestTemplate(SisaAuthInterceptor sisaAuthInterceptor) throws Exception {
        super(sisaAuthInterceptor, new LoggingRequestInterceptor());
    }
}
