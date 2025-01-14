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

	public final static String VALIDATE_PATH = "/prescription-validation";

	private final String username;

	private final String password;

	public MedicationRequestValidationWSConfig(@Value("${ws.medication.request.validation.baseUrl:}") String baseUrl,
											   @Value("${ws.medication.request.validation.username:}") String username,
											   @Value("${ws.medication.request.validation.password:}") String password) {
		super(baseUrl, false);
		this.username = username;
		this.password = password;
	}

}
