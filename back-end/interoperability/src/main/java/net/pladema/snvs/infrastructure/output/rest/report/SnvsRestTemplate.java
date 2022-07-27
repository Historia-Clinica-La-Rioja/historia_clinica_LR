package net.pladema.snvs.infrastructure.output.rest.report;

import ar.lamansys.sgx.shared.restclient.configuration.HttpClientConfiguration;
import ar.lamansys.sgx.shared.restclient.configuration.resttemplate.RestTemplateAuth;
import net.pladema.snvs.infrastructure.configuration.SnvsCondition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;

@Service
@Conditional(SnvsCondition.class)
public class SnvsRestTemplate extends RestTemplateAuth {

    public SnvsRestTemplate(
			SisaAuthInterceptor sisaAuthInterceptor,
			HttpClientConfiguration configuration,
			@Value("${ws.sisa.snvs.rest-client.config.trust-invalid-certificate:false}") Boolean trustInvalidCertificate
	) throws Exception {
        super(sisaAuthInterceptor, configuration.with(trustInvalidCertificate));
    }
}
