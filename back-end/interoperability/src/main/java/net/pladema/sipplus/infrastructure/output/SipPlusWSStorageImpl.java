package net.pladema.sipplus.infrastructure.output;

import ar.lamansys.sgh.shared.infrastructure.input.service.BasicDataPersonDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.BasicPatientDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedPatientPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedPersonPort;
import ar.lamansys.sgx.shared.restclient.configuration.resttemplate.exception.RestTemplateApiException;
import ar.lamansys.sgx.shared.restclient.services.RestClient;
import ar.lamansys.sgx.shared.restclient.services.RestClientInterface;

import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import net.pladema.sipplus.application.port.SipPlusWSStorage;

import net.pladema.sipplus.domain.SipPlusPregnancieResponse;
import net.pladema.sipplus.domain.SipPlusPregnancyCode;
import net.pladema.sipplus.infrastructure.input.rest.exception.SipPlusApiException;
import net.pladema.sipplus.infrastructure.input.rest.exception.SipPlusApiExceptionEnum;
import net.pladema.sipplus.infrastructure.output.rest.SipPlusPregnancyPayload;
import net.pladema.sipplus.infrastructure.output.rest.SipPlusRestTemplate;

import net.pladema.sipplus.infrastructure.output.rest.SipPlusWSConfig;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class SipPlusWSStorageImpl implements SipPlusWSStorage {

	private final RestClientInterface restClientInterface;
	private final SipPlusWSConfig sipPlusWSConfig;

	private final SharedPatientPort sharedPatientPort;

	private final SharedPersonPort sharedPersonPort;

	public SipPlusWSStorageImpl(SipPlusRestTemplate sipPlusRestTemplate, 
								SipPlusWSConfig sipPlusWSConfig, 
								SharedPatientPort sharedPatientPort, 
								SharedPersonPort sharedPersonPort) {
		this.restClientInterface = new RestClient(sipPlusRestTemplate, sipPlusWSConfig);
		this.sipPlusWSConfig = sipPlusWSConfig;
		this.sharedPatientPort = sharedPatientPort;
		this.sharedPersonPort = sharedPersonPort;
	}

	@Override
	public List<Integer> getPregnancies(Integer patientId) throws RestTemplateApiException {
		BasicPatientDto patientData = sharedPatientPort.getBasicDataFromPatient(patientId);
		String countryIsoCode = sharedPersonPort.getCountryIsoCodeFromPerson(patientData.getPerson().getId());

		String urlWithParams = getUrlWithParams(patientData, countryIsoCode);
		ResponseEntity<SipPlusPregnancieResponse> response;
		List<Integer> pregnancies = new ArrayList<>();
		try {
			response = restClientInterface.exchangeGet(urlWithParams, SipPlusPregnancieResponse.class);
			if (response.getBody().getPregnancies() != null) {
				response.getBody().getPregnancies().keySet().stream().forEach(p -> {
					pregnancies.add(Integer.parseInt(p));
				});
			}
		} catch (RestTemplateApiException e) {
			throw mapException(e);
		}
		return pregnancies;
	}

	@Override
	public void createPregnancy(Integer patientId, Integer pregnancyNumber) {
		BasicPatientDto patientData = sharedPatientPort.getBasicDataFromPatient(patientId);
		String countryIsoCode = sharedPersonPort.getCountryIsoCodeFromPerson(patientData.getPerson().getId());

		String urlWithParams = getUrlWithParams(patientData, countryIsoCode);
		SipPlusPregnancyPayload body = createPregnancyBody(patientData.getPerson(), pregnancyNumber.toString());
		try {
			restClientInterface.exchangePost(urlWithParams, body, Object.class);
		} catch (RestTemplateApiException e) {
			throw mapException(e);
		}
	}

	private SipPlusPregnancyPayload createPregnancyBody(BasicDataPersonDto basicDataPerson, String pregnancyNumber) {
		Map<String, JSONObject> pregnancies = new HashMap<>();
		pregnancies.put(pregnancyNumber, getPregnancyData(basicDataPerson));
		return SipPlusPregnancyPayload.builder()
				.pregnancies(pregnancies)
				.build();
	}


	private JSONObject getPregnancyData(BasicDataPersonDto basicDataPerson) {
		log.debug("Get pregnancy data");
		Short patientAge = basicDataPerson.getAge();
		JSONObject pregnancyData = new JSONObject();
		pregnancyData.put(SipPlusPregnancyCode.MOTHER_AGE, patientAge);
		return pregnancyData;
	}
	
	private String getUrlWithParams(BasicPatientDto patientData, String countryIsoCode) {
		return Stream.of("/record",
						countryIsoCode,
						patientData.getIdentificationType(),
						patientData.getIdentificationNumber())
				.collect(Collectors.joining("/"));
	}

	private SipPlusApiException mapException(RestTemplateApiException apiException) {
		if (apiException.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR)
			return new SipPlusApiException(SipPlusApiExceptionEnum.SERVER_ERROR, "El servicio de Sip+ tiene un error interno.");
		if (apiException.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR) {
			if (apiException.getStatusCode() == HttpStatus.NOT_FOUND)
				return new SipPlusApiException(SipPlusApiExceptionEnum.NOT_FOUND, "La paciente no se encuentra cargada en sip plus");
		}
		return new SipPlusApiException(SipPlusApiExceptionEnum.UNKNOWN_ERROR, "Estado de error desconocido");
	}

}
