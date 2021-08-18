package net.pladema.nomivac.infrastructure.output.immunization.rest;

import ar.lamansys.sgx.shared.restclient.configuration.WSConfig;
import lombok.Getter;
import lombok.Setter;
import net.pladema.nomivac.infrastructure.configuration.NomivacCondition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="ws.nomivac.synchronization")
@Getter
@Setter
@Conditional(NomivacCondition.class)
public class NomivacWSConfig extends WSConfig {

	private final String immunizationUrl = "/immunization";

	public NomivacWSConfig(@Value("${ws.nomivac.synchronization.url.base:localhost}") String baseUrl,
						   @Value("${ws.nomivac.synchronization.mocked:false}") Boolean mocked) {
		super(baseUrl, mocked);
	}

}
