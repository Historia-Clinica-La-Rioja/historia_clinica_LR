package net.pladema.medicationrequestvalidation.infrastructure.output.config;

import ar.lamansys.sgx.shared.restclient.services.RestClient;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Service
public class MedicationRequestValidationRestClient extends RestClient {

	private final MedicationRequestValidationWSConfig medicationRequestValidationWSConfig;

	public MedicationRequestValidationRestClient(RestTemplateBuilder restTemplateBuilder, MedicationRequestValidationWSConfig wsConfig) {
		super(restTemplateBuilder.build(), wsConfig);
		this.medicationRequestValidationWSConfig = wsConfig;
	}

	@Override
	public HttpHeaders getHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(APPLICATION_JSON);
		headers.setBearerAuth(medicationRequestValidationWSConfig.getToken());
		return headers;
	}

}
