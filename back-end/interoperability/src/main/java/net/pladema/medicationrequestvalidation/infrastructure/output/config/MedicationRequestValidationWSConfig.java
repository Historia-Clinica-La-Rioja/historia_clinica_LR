package net.pladema.medicationrequestvalidation.infrastructure.output.config;

import ar.lamansys.sgx.shared.restclient.configuration.WSConfig;

import lombok.Getter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "ws.medication.request.validation")
@Getter
@Component
public class MedicationRequestValidationWSConfig extends WSConfig {

	public final static String VALIDATE_PATH = "/apirecipe/Receta";

	private final String clientId;

	private final String token;

	public MedicationRequestValidationWSConfig(@Value("${ws.medication.request.validation.baseUrl:}") String baseUrl,
											   @Value("${ws.medication.request.validation.clientId:}") String clientId,
											   @Value("${ws.medication.request.validation.token:}") String token) {
		super(baseUrl, false);
		this.clientId = clientId;
		this.token = token;
	}

}
