package net.pladema.federar.services.impl;

import java.util.Optional;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ar.lamansys.sgx.shared.restclient.configuration.resttemplate.exception.RestTemplateApiException;
import ar.lamansys.sgx.shared.restclient.services.RestClient;
import ar.lamansys.sgx.shared.restclient.services.RestClientInterface;
import lombok.extern.slf4j.Slf4j;
import net.pladema.federar.configuration.FederarRestTemplateAuth;
import net.pladema.federar.configuration.FederarWSConfig;
import net.pladema.federar.services.FederarService;
import net.pladema.federar.services.domain.FederarErrorResponse;
import net.pladema.federar.services.domain.FederarResourceAttributes;
import net.pladema.federar.services.domain.FederarResourcePayload;
import net.pladema.federar.services.domain.LocalIdSearchResponse;
import net.pladema.federar.services.exceptions.FederarApiException;
import net.pladema.federar.services.exceptions.FederarEnumException;

@Service
@ConditionalOnProperty(
		value="ws.federar.enabled",
		havingValue = "true")
@Slf4j
public class FederarServiceImpl implements FederarService {

	private final FederarWSConfig federarWSConfig;

	private final RestClientInterface restClientInterface;

	public FederarServiceImpl(FederarRestTemplateAuth restTemplateAuth,
							  FederarWSConfig wsConfig) {
		this.federarWSConfig = wsConfig;
		this.restClientInterface = new RestClient(restTemplateAuth, wsConfig);
	}

	@Override
	public Optional<Integer> federatePatient(FederarResourceAttributes attributes, Integer patientId) throws FederarApiException {
		attributes.setId(patientId);
		FederarResourcePayload requestBody = new FederarResourcePayload(attributes);
		requestBody.setIdentifier(federarWSConfig.getDomain(), attributes);
		ResponseEntity<FederarErrorResponse> response = callFederateWS(requestBody);
		if (federateSucceed(response)) {
			Optional<LocalIdSearchResponse> searchResponse = searchByLocalId(patientId);
			if(searchResponse.isPresent())
				return searchResponse.get().getNationalId();
		}
		if(response.hasBody()) {
			log.error("Federate patient with id {}, statusCode {}, Cause: {}",
					patientId, response.getStatusCode(), response.getBody());
		}
		return Optional.empty();
	}

	private boolean federateSucceed(ResponseEntity<FederarErrorResponse> response) {
		return response.getStatusCode().is2xxSuccessful();
	}

	private Optional<LocalIdSearchResponse> searchByLocalId(Integer localId) throws FederarApiException {
		String urlWithParams = federarWSConfig.getPatientService() + "?identifier=" + federarWSConfig.getIss() + "|"
				+ localId;
		try {
			ResponseEntity<LocalIdSearchResponse> response = restClientInterface.exchangeGet(urlWithParams, LocalIdSearchResponse.class);
			return Optional.ofNullable(response.getBody());
		} catch (RestTemplateApiException ex) {
			throw new FederarApiException(FederarEnumException.UNKNOWN_ERROR, ex.getStatusCode(),
					String.format("La busqueda del paciente federar tuvo el siguiente error %s", ex.getBody()));
		}

	}

	private ResponseEntity<FederarErrorResponse> callFederateWS(FederarResourcePayload requestBody) throws FederarApiException {
		String urlWithParams = federarWSConfig.getPatientService();
		try {
			return restClientInterface.exchangePost(urlWithParams, requestBody, FederarErrorResponse.class);
		} catch (RestTemplateApiException ex) {
			return processRestTemplateApiException(ex, requestBody);
		}
	}

	private ResponseEntity<FederarErrorResponse> processRestTemplateApiException(RestTemplateApiException ex, FederarResourcePayload requestBody) throws FederarApiException {
		if (ex.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR)
			throw new FederarApiException(FederarEnumException.SERVER_ERROR, ex.getStatusCode(), "El servicio de Federar tiene un error interno.");
		if (ex.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR) {
			if (ex.getStatusCode() == HttpStatus.REQUEST_TIMEOUT)
				throw new FederarApiException(FederarEnumException.API_TIMEOUT, ex.getStatusCode(), "El servicio de Federar esta tardando en responder.");
			if (ex.getStatusCode() == HttpStatus.NOT_FOUND)
				throw new FederarApiException(FederarEnumException.NOT_FOUND, ex.getStatusCode(), "El servicio consultado no existe.");
			if ((ex.getStatusCode() == HttpStatus.BAD_REQUEST) || (ex.getStatusCode().equals(HttpStatus.UNPROCESSABLE_ENTITY))) {
				try {
					ObjectMapper mapper = new ObjectMapper();
					FederarErrorResponse message = mapper.readValue(ex.getBody(), FederarErrorResponse.class);
					return processIssues(ex.getStatusCode(), message, requestBody);
				} catch (JsonProcessingException ignored) {
					throw new FederarApiException(FederarEnumException.UNPARSEABLE_RESPONSE, ex.getStatusCode(),
							String.format("El siguiente body %s no se puede parsear en la clase %s", ex.getBody(), FederarErrorResponse.class));
				}
			}
		}
		throw new FederarApiException(FederarEnumException.UNKNOWN_ERROR, ex.getStatusCode(), ex.getBody());
	}

	private ResponseEntity<FederarErrorResponse> validatePreexistedPatient(HttpStatus statusCode, FederarErrorResponse message,
																		   FederarResourcePayload requestBody) throws FederarApiException {
		return searchByLocalId(Integer.parseInt(requestBody.getId()))
				.filter(localIdSearchResponse -> {
					if (this.isValidPatient(localIdSearchResponse, requestBody))
						return true;
					log.error("Federate patient {} already exists, but the preexisted patient is not equal {}", requestBody, localIdSearchResponse);
					return false;
				})
				.map(localIdSearchResponse-> new ResponseEntity<FederarErrorResponse>(HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<>(message, statusCode));

	}

	private boolean isValidPatient(LocalIdSearchResponse localIdSearchResponse, FederarResourcePayload requestBody) {
		return localIdSearchResponse.getEntry().stream().anyMatch(entry -> entry.isSamePatient(requestBody));
	}

	private ResponseEntity<FederarErrorResponse> processIssues(HttpStatus statusCode, FederarErrorResponse message, FederarResourcePayload requestBody) throws FederarApiException {
		String alreadyExistsdiagnostic = String.format("Identifier %s|%s already exists", federarWSConfig.getIss(), requestBody.getId());
		if (!message.getIssue().isEmpty() ) {
			if (message.containDiagnostic(alreadyExistsdiagnostic))
				return validatePreexistedPatient(statusCode, message, requestBody);
		}
		return new ResponseEntity<>(message, statusCode);
	}
}
