package net.pladema.federar.services.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import net.pladema.federar.configuration.FederarRestTemplateAuth;
import net.pladema.federar.configuration.FederarWSConfig;
import net.pladema.federar.services.FederarService;
import net.pladema.federar.services.domain.FederarErrorResponse;
import net.pladema.federar.services.domain.FederarExtensionPayload;
import net.pladema.federar.services.domain.FederarFamilyPayload;
import net.pladema.federar.services.domain.FederarIdentifierPayload;
import net.pladema.federar.services.domain.FederarNamePayload;
import net.pladema.federar.services.domain.FederarResourcePayload;
import net.pladema.federar.services.domain.LocalIdSearchResponse;
import net.pladema.patient.repository.entity.Patient;
import net.pladema.person.repository.entity.Gender;
import net.pladema.person.repository.entity.Person;
import net.pladema.sgx.restclient.services.RestClient;

@Service
@Profile("prod")
public class FederarServiceImpl extends RestClient implements FederarService {

	private static final String FEMALE = "female";
	private static final String MALE = "male";
	private static final String ALREADY_PATIENT = "already a patient";
	private static final String USE_OFFICIAL = "official";
	private static final String FAMILY_URL = "http://hl7.org/fhir/StructureDefinition/humanname-fathers-family";
	private static final String TYPE_PATIENT = "Patient";
	private static final String DNI_URL = "http://www.renaper.gob.ar/dni";

	private FederarWSConfig federarWSConfig;

	public FederarServiceImpl(FederarRestTemplateAuth restTemplateAuth, FederarWSConfig wsConfig) {
		super(restTemplateAuth, wsConfig);
		this.federarWSConfig = wsConfig;
	}

	@Override
	public Optional<LocalIdSearchResponse> searchByLocalId(Integer localId) {
		String urlWithParams = federarWSConfig.getLocalSearchIdUrl() + "?identifier=" + federarWSConfig.getIss() + "|"
				+ localId;
		ResponseEntity<LocalIdSearchResponse> response = exchangeGet(urlWithParams, LocalIdSearchResponse.class);
		return Optional.of(response.getBody());
	}

	@Override
	public Optional<LocalIdSearchResponse> federatePatient(Person person, Patient patient) {
		ResponseEntity<FederarErrorResponse> response = callFederateWS(person, patient);
		if (federateSucceded(response)) {
			return searchByLocalId(patient.getId());
		}
		return Optional.empty();
	}

	private boolean federateSucceded(ResponseEntity<FederarErrorResponse> response) {
		if (response.getStatusCode() == HttpStatus.CREATED) {
			return true;
		}
		if (response.getStatusCode() == HttpStatus.BAD_REQUEST && response.getBody() != null
				&& response.getBody().getIssue() != null) {
			return response.getBody().getIssue().stream()
					.anyMatch(issue -> issue.getDiagnostics().contains(ALREADY_PATIENT));
		}
		return false;
	}

	private ResponseEntity<FederarErrorResponse> callFederateWS(Person person, Patient patient) {
		String urlWithParams = federarWSConfig.getFederateUrl();
		FederarResourcePayload body = generateResourcePayload(person, patient);
		return exchangePost(urlWithParams, body, FederarErrorResponse.class);
	}

	private FederarResourcePayload generateResourcePayload(Person person, Patient patient) {
		FederarResourcePayload resource = new FederarResourcePayload();
		List<FederarNamePayload> names = Arrays.asList(generateNamePayload(person));
		resource.setResourceType(TYPE_PATIENT);
		resource.setBirthDate(person.getBirthDate());
		resource.setGender(person.getGenderId() == Gender.MALE ? MALE : FEMALE);
		resource.setIdentifier(generateIdentifiers(person, patient));
		resource.setName(names);
		return resource;
	}

	private FederarNamePayload generateNamePayload(Person person) {
		FederarNamePayload namePayload = new FederarNamePayload();
		namePayload.setUse(USE_OFFICIAL);
		Stream<String> personNames = Stream.of(person.getFirstName(), person.getMiddleNames(), person.getLastName(),
				person.getOtherLastNames());
		namePayload.setText(personNames.filter(Objects::nonNull).collect(Collectors.joining(" ")));
		namePayload.setFamily(person.getLastName());
		namePayload.setGiven(Stream.of(person.getFirstName(), person.getMiddleNames()).filter(Objects::nonNull)
				.collect(Collectors.toList()));
		generateFamilyExtension(person, namePayload);
		return namePayload;
	}

	private void generateFamilyExtension(Person person, FederarNamePayload namePayload) {
		FederarExtensionPayload familyExtension = new FederarExtensionPayload(FAMILY_URL, person.getLastName());
		namePayload.setFamilyExtension(new FederarFamilyPayload(Arrays.asList(familyExtension)));
	}

	private List<FederarIdentifierPayload> generateIdentifiers(Person person, Patient patient) {
		List<FederarIdentifierPayload> identifiers = new ArrayList<>();
		identifiers.add(new FederarIdentifierPayload(federarWSConfig.getDomain(), patient.getId().toString()));
		identifiers.add(new FederarIdentifierPayload(DNI_URL, person.getIdentificationNumber()));
		return identifiers;
	}

}
