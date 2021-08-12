package net.pladema.federar.services.impl;

import ar.lamansys.sgx.shared.restclient.services.RestClient;
import net.pladema.federar.configuration.FederarRestTemplateAuth;
import net.pladema.federar.configuration.FederarWSConfig;
import net.pladema.federar.services.FederarService;
import net.pladema.federar.services.domain.FederarErrorResponse;
import net.pladema.federar.services.domain.FederarResourceAttributes;
import net.pladema.federar.services.domain.FederarResourcePayload;
import net.pladema.federar.services.domain.LocalIdSearchResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@ConditionalOnProperty(
		value="ws.federar.enabled",
		havingValue = "true",
		matchIfMissing = false)
public class FederarServiceImpl extends RestClient implements FederarService {

	private final Logger log = LoggerFactory.getLogger(FederarServiceImpl.class);

	private final FederarWSConfig federarWSConfig;

	public FederarServiceImpl(FederarRestTemplateAuth restTemplateAuth,
							  FederarWSConfig wsConfig) {
		super(restTemplateAuth, wsConfig);
		this.federarWSConfig = wsConfig;
	}

	@Override
	public Optional<Integer> federatePatient(FederarResourceAttributes attributes, Integer patientId) {
		attributes.setId(patientId);
		FederarResourcePayload requestBody = new FederarResourcePayload(attributes);
		requestBody.setIdentifier(federarWSConfig.getDomain(), attributes);
		ResponseEntity<FederarErrorResponse> response = callFederateWS(requestBody);
		if (federateSucceded(response)) {
			Optional<LocalIdSearchResponse> searchResponse = searchByLocalId(patientId);
			if(searchResponse.isPresent())
				return searchResponse.get().getNationalId();
		}
		if(response.hasBody()) {
			log.debug("Federate patient with id {}, statusCode {}, Cause: {}",
					patientId, response.getStatusCode(), response.getBody().getDiagnostics());
		}
		return Optional.empty();
	}

	private boolean federateSucceded(ResponseEntity<FederarErrorResponse> response) {
		return response.getStatusCode() == HttpStatus.CREATED;
	}

	private Optional<LocalIdSearchResponse> searchByLocalId(Integer localId) {
		String urlWithParams = federarWSConfig.getPatientService() + "?identifier=" + federarWSConfig.getIss() + "|"
				+ localId;
		ResponseEntity<LocalIdSearchResponse> response = exchangeGet(urlWithParams, LocalIdSearchResponse.class);
		return Optional.ofNullable(response.getBody());
	}

	private ResponseEntity<FederarErrorResponse> callFederateWS(FederarResourcePayload requestBody) {
		String urlWithParams = federarWSConfig.getPatientService();
		return exchangePost(urlWithParams, requestBody, FederarErrorResponse.class);
	}

}
