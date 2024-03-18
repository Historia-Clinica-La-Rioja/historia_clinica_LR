package net.pladema.cipres.infrastructure.output.rest;

import ar.lamansys.sgh.shared.infrastructure.input.service.SharedAddressDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.institution.SharedInstitutionPort;
import ar.lamansys.sgx.shared.restclient.configuration.resttemplate.exception.RestTemplateApiException;
import lombok.extern.slf4j.Slf4j;
import net.pladema.cipres.application.port.CipresPatientStorage;
import net.pladema.cipres.application.port.CipresPersonStorage;
import net.pladema.cipres.application.port.CipresStorage;
import net.pladema.cipres.domain.BasicDataPatientBo;
import net.pladema.cipres.domain.BasicDataPersonBo;
import net.pladema.cipres.domain.PersonDataBo;
import net.pladema.cipres.infrastructure.output.repository.CipresEncounterRepository;
import net.pladema.cipres.infrastructure.output.repository.CipresPatient;
import net.pladema.cipres.infrastructure.output.repository.CipresPatientPk;
import net.pladema.cipres.infrastructure.output.repository.CipresPatientRepository;
import net.pladema.cipres.infrastructure.output.rest.domain.CipresCityResponse;
import net.pladema.cipres.infrastructure.output.rest.domain.CipresPatientResponse;
import net.pladema.cipres.infrastructure.output.rest.domain.CipresMasterData;
import net.pladema.cipres.infrastructure.output.rest.domain.CipresPatientPayload;


import net.pladema.cipres.infrastructure.output.rest.domain.CipresRegisterResponse;
import net.pladema.cipres.infrastructure.output.rest.domain.consultation.CipresEntityResponse;

import net.pladema.cipres.infrastructure.output.rest.domain.patient.CipresPatientAddressPayload;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class CipresPatientStorageImpl extends CipresStorage implements CipresPatientStorage {

	public static final short GENDER_FEMALE = 1;

	public static final short GENDER_MALE = 2;
	public static final short IDENTIFICATION_TYPE_DNI = 1;
	public static final short IDENTIFICATION_TYPE_CI = 2;
	public static final short IDENTIFICATION_TYPE_LC = 3;
	public static final short IDENTIFICATION_TYPE_LE = 4;
	public static final short IDENTIFICATION_TYPE_CEDULA_MERCOSUR = 5;
	public static final short IDENTIFICATION_TYPE_CUIT = 6;
	public static final short IDENTIFICATION_TYPE_FOREIGN_PASSPORT = 8;
	public static final short IDENTIFICATION_TYPE_FOREIGN_IDENTITY_CARD = 9;
	public static final short IDENTIFICATION_TYPE_OTHER_FOREIGN_DOCUMENT = 10;

	public static final Integer CUIT_LENGTH = 11;

	public static final Integer MIN_PERSON_CONTACT_LENGTH = 10;

	public static final Integer MAX_PERSON_CONTACT_LENGTH = 13;

	private static final String ADDRESS = "domicilio";

	private static final String ID = "id";

	private static final String DEPARTMENT_DESCRIPTION = "descripcionPartido";

	private static final String ESTABLISHMENT_URL = "?establecimiento=";

	private static final String GET_PATIENT_URL = "%s?esDocumentoArgentino=%s&numeroDocumento=%s&sexo=%s&esDocumentoPropio=true&incluirRelaciones=false&incluirDomicilio=true&establecimiento=%s";

	private static final String MINIMAL_PATIENT_DATA = "No se cuenta con alguno de los datos mínimos requeridos para dar de alta al paciente (fecha de nacimiento, nombres y apellidos)";

	private static final String NO_NATIONALITY_MATCHING_DATA = "No se encontraron datos en la api para conformar la nacionalidad del paciente";

	private static final String NO_ADDRESS_MATCHING_DATA = "No se encontraron datos en la api para conformar la localidad del paciente";

	private static final String NO_IDENTIFICATION_PATIENT_DATA = "No se cuenta con la información necesaria para identificar al paciente. Verificar tipo de documento, número de documento y género";

	private static final String INVALID_CUIT = "Error en la cantidad de caracteres que componen el CUIT del paciente";

	private static final List<Short> nationalDocumentTypes = List.of(IDENTIFICATION_TYPE_DNI, IDENTIFICATION_TYPE_CI, IDENTIFICATION_TYPE_LC, IDENTIFICATION_TYPE_LE, IDENTIFICATION_TYPE_CUIT);

	private static final List<Short> foreignDocumentTypes = List.of(IDENTIFICATION_TYPE_FOREIGN_PASSPORT, IDENTIFICATION_TYPE_FOREIGN_IDENTITY_CARD, IDENTIFICATION_TYPE_OTHER_FOREIGN_DOCUMENT, IDENTIFICATION_TYPE_CEDULA_MERCOSUR);

	private final CipresPersonStorage cipresPersonStorage;

	private final CipresPatientRepository cipresPatientRepository;

	private final SharedInstitutionPort sharedInstitutionPort;

	private Integer encounterId;

	private Integer cipresEncounterId;

	private Integer institutionId;

	public CipresPatientStorageImpl(CipresRestTemplate cipresRestTemplate,
									 CipresWSConfig cipresWSConfig,
									 CipresPersonStorage cipresPersonStorage,
									 CipresPatientRepository cipresPatientRepository,
									 CipresEncounterRepository cipresEncounterRepository,
									 SharedInstitutionPort sharedInstitutionPort) {
		super(cipresRestTemplate, cipresWSConfig, cipresEncounterRepository);
		this.cipresPersonStorage = cipresPersonStorage;
		this.cipresPatientRepository = cipresPatientRepository;
		this.sharedInstitutionPort = sharedInstitutionPort;
	}

	@Override
	public Optional<Long> getPatientId(BasicDataPatientBo patientData, String establishmentId,
									   Integer encounterId, Integer cipresEncounterId,
									   Integer institutionId) {
		this.encounterId = encounterId;
		this.cipresEncounterId = cipresEncounterId;
		this.institutionId = institutionId;
		var cipresPatientId = this.cipresPatientRepository.getCipresPatientId(patientData.getId());
		return cipresPatientId.or(() -> foundPatientId(patientData, establishmentId));
	}

	private Optional<Long> foundPatientId(BasicDataPatientBo patientData, String establishmentId) {
		if (validIdentificationPatientData(patientData.getPerson())) {
			String url = this.buildPatientUrl(patientData.getPerson(), establishmentId);
			Optional<Long> result = Optional.empty();
			try {
				ResponseEntity<CipresPatientResponse[]> response = restClient.exchangeGet(url, CipresPatientResponse[].class);
				if (isSuccessfulResponse(response))
					result = processPatientSuccessfulResponse(response.getBody(), establishmentId, patientData);
			} catch (RestTemplateApiException e) {
				result = handlePatientRestTemplateApiException(e, patientData, establishmentId);
			} catch (ResourceAccessException e) {
				handleResourceAccessException(e, this.encounterId, this.cipresEncounterId);
			}
			result.ifPresent(cipresPatientId -> createCipresPatient(patientData.getId(), cipresPatientId));
			return result;
		} else
			saveStatusError(this.cipresEncounterId, this.encounterId, NO_IDENTIFICATION_PATIENT_DATA, HttpStatus.BAD_REQUEST.value());
		return Optional.empty();
	}

	private Optional<Long> handlePatientRestTemplateApiException(RestTemplateApiException e, BasicDataPatientBo patientData,
																 String establishmentId) {
		var personData = patientData.getPerson();
		if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
			if (validCuitIdentificationTypeData(personData)) {
				PersonDataBo person = cipresPersonStorage.getPersonData(personData.getId());
				if (validPatientMinimalData(person)) {
					CipresPatientAddressPayload addressBody = patientHasAddressData(person) ? mapFromPersonAddress(person) : mapFromInstitutionAddress();
					if (addressBody.getLocalidad() != null && addressBody.getNacionalidad() != null)
						return createPatient(person, establishmentId, addressBody);
				} else
					saveStatusError(this.cipresEncounterId, this.encounterId, MINIMAL_PATIENT_DATA, HttpStatus.BAD_REQUEST.value());
			} else
				saveStatusError(this.cipresEncounterId, this.encounterId, INVALID_CUIT, HttpStatus.BAD_REQUEST.value());
		} else
			saveStatusError(this.cipresEncounterId, this.encounterId, e.mapErrorBody(CipresRegisterResponse.class).getDetail(), e.getStatusCode().value());
		return Optional.empty();
	}

	private Optional<Long> processPatientSuccessfulResponse(CipresPatientResponse[] patients, String establishmentId,
															BasicDataPatientBo patientData) {
		var patient = patients[0].getPaciente();
		var patientId = patient.get(ID).toString();
		if (patient.get(ADDRESS) != null)
			return Optional.of(Long.parseLong(patientId));

		var addressId = createPatientAddress(patientData.getPerson().getId(), patientId, establishmentId);
		if (addressId.isPresent())
			return Optional.of(Long.parseLong(patientId));

		return Optional.empty();
	}

	private Optional<Integer> createPatientAddress(Integer personId, String patientId,
												   String establishmentId) {
		PersonDataBo person = cipresPersonStorage.getPersonData(personId);
		CipresPatientAddressPayload body = patientHasAddressData(person) ? mapFromPersonAddress(person) : mapFromInstitutionAddress();
		if (body.getNacionalidad() != null && body.getLocalidad() != null)
			return savePatientAddress(body, patientId, establishmentId);
		return Optional.empty();
	}

	public Optional<Integer> savePatientAddress(CipresPatientAddressPayload body, String patientId, String establishmentId) {
		String url = cipresWSConfig.getAddressUrl().concat(ESTABLISHMENT_URL+ establishmentId);
		body.setPaciente("api/paciente/" + patientId);
		try {
			var response = restClient.exchangePost(url, body, CipresEntityResponse.class);
			if (response != null && response.getBody() != null)
				return Optional.ofNullable(response.getBody().getId() != null ? Integer.parseInt(response.getBody().getId()) : null);
		} catch (RestTemplateApiException e) {
			log.debug("Error al intentar actualizar el domicilio del paciente con id: ", patientId);
			saveStatusError(this.cipresEncounterId, this.encounterId, e.mapErrorBody(CipresRegisterResponse.class).getDetail(), e.getStatusCode().value());
		} catch (ResourceAccessException e) {
			handleResourceAccessException(e, this.encounterId, this.cipresEncounterId);
		}
		return Optional.empty();
	}

	@Override
	public Optional<Long> createPatient(PersonDataBo person, String establishmentId, CipresPatientAddressPayload address) {
		String url = cipresWSConfig.getPatientUrl().concat(ESTABLISHMENT_URL + establishmentId);
		CipresPatientPayload body = mapToPatientPayload(person, address);
		try {
			var response = restClient.exchangePost(url, body, CipresEntityResponse.class);
			if (response != null && response.getBody() != null && response.getBody().getId() != null)
				return Optional.of(Long.parseLong(response.getBody().getId()));
		} catch (RestTemplateApiException e) {
			log.debug("Error al intentar insertar un paciente en la api");
			saveStatusError(this.cipresEncounterId, this.encounterId, e.mapErrorBody(CipresRegisterResponse.class).getDetail(), e.getStatusCode().value());
		} catch (ResourceAccessException e) {
			handleResourceAccessException(e, this.encounterId, this.cipresEncounterId);
		}
		return Optional.empty();
	}

	public CipresPatientPayload mapToPatientPayload(PersonDataBo person, CipresPatientAddressPayload address) {
		String firstName = Stream.of(person.getFirstName(),  person.getMiddleNames())
				.filter(Objects::nonNull)
				.collect(Collectors.joining(" "));
		String lastName = Stream.of(person.getOtherLastNames(), person.getLastName())
				.filter(Objects::nonNull)
				.collect(Collectors.joining(" "));
		return CipresPatientPayload.builder()
				.nombre(firstName)
				.apellido(lastName)
				.tipoDocumento(getCipresDocumentType(person.getIdentificationTypeId()))
				.numeroDocumento(!person.getIdentificationTypeId().equals(IDENTIFICATION_TYPE_CUIT) ? person.getIdentificationNumber() :
						person.getIdentificationNumber().substring(2, 10))
				.sexo(getCipresGenderMasterData(person.getGenderId()))
				.fechaNacimiento(person.getBirthDate().toString())
				.domicilio(address)
				.nacionalidad(address.getNacionalidad())
				.build();
	}

	private CipresPatientAddressPayload mapToAddressPayload(String contact, String cityBahraCode, String cityName, String departmentName,
															String email, String street, String number, Integer floor, String apartment,
															String quarter, String postcode, String countryName) {
		String city = cityBahraCode != null ? getCityIRI(cityBahraCode, cityName, departmentName).orElse(null) : getCityIRIByCityName(cityName, departmentName).orElse(null);
		String nationality = city != null ? getNationalityIRI(countryName).orElse(null) : null;
		return CipresPatientAddressPayload.builder()
				.telefono(contact)
				.celular(contact)
				.email(email)
				.calle(street)
				.nro(number)
				.piso(floor)
				.departamento(apartment)
				.barrio(quarter)
				.cp(postcode)
				.localidad(city)
				.nacionalidad(nationality)
				.build();
	}

	private CipresPatientAddressPayload mapFromPersonAddress(PersonDataBo personInfo) {
		String personContact = getPersonContact(personInfo.getPhonePrefix(), personInfo.getPhoneNumber());
		String cityBahraCode = personInfo.getCityBahraCode();
		String cityName = personInfo.getCity();
		String departmentName = personInfo.getDepartment();
		String email = personInfo.getEmail();
		String street = personInfo.getStreet();
		String number = personInfo.getNumber();
		String floor = personInfo.getFloor();
		Integer parsedFloor = floor != null && floor.matches("\\d+") ? Integer.parseInt(floor) : null;
		String apartment = personInfo.getApartment();
		String quarter = personInfo.getQuarter();
		String postcode = personInfo.getPostcode();
		String countryName = personInfo.getCountry();

		return mapToAddressPayload(personContact, cityBahraCode, cityName, departmentName, email, street, number, parsedFloor, apartment, quarter, postcode, countryName);
	}

	private CipresPatientAddressPayload mapFromInstitutionAddress() {
		SharedAddressDto address = sharedInstitutionPort.fetchAddress(this.institutionId);
		String cityBahraCode = address.getBahraCode();
		String cityName = address.getCityName();
		String departmentName = address.getDepartmentName();
		String street = address.getStreet();
		String number = address.getNumber();
		String floor = address.getFloor();
		Integer parsedFloor = floor != null && floor.matches("\\d+") ? Integer.parseInt(floor) : null;
		String apartment = address.getApartment();
		String postcode = address.getPostCode();
		String countryName = address.getCountryName();

		return mapToAddressPayload(null, cityBahraCode, cityName, departmentName, null, street, number, parsedFloor, apartment, null, postcode, countryName);
	}


	private String getPersonContact(String phonePrefix, String phoneNumber) {
		if (phonePrefix != null && phoneNumber != null) {
			String personContact = phonePrefix.concat(phoneNumber);
			if (personContact.length() >= MIN_PERSON_CONTACT_LENGTH && personContact.length() <= MAX_PERSON_CONTACT_LENGTH) {
				StringBuilder finalPersonContact = new StringBuilder(phonePrefix.concat(phoneNumber));
				finalPersonContact.insert(3, "-");
				return finalPersonContact.toString();
			}
		}
		return null;
	}

	public Optional<String> getNationalityIRI(String country) {
		String url = String.format("%s?descripcionPais=%s", cipresWSConfig.getCountryUrl(), country.toUpperCase());
		try {
			ResponseEntity<CipresEntityResponse[]>  response = restClient.exchangeGet(url, CipresEntityResponse[].class);
			if (isSuccessfulResponse(response)) {
				var cipresCountry = Arrays.asList(response.getBody()).get(0);
				return Optional.of(cipresWSConfig.getNationalityUrl().concat("/").concat(cipresCountry.getId()));
			} else
				saveStatusError(this.cipresEncounterId, this.encounterId, NO_NATIONALITY_MATCHING_DATA, HttpStatus.BAD_REQUEST.value());
		} catch (RestTemplateApiException e) {
			log.debug("Error al intentar obtener la nacionalidad");
			saveStatusError(this.cipresEncounterId, this.encounterId, e.getMessage(), e.getStatusCode().value());
		} catch (ResourceAccessException e) {
			handleResourceAccessException(e, this.encounterId, this.cipresEncounterId);
		}
		return Optional.empty();
	}

	public Optional<String> getCityIRI(String bahraCode, String city, String department) {
		String url = String.format("%s?codigoBahra=%s", cipresWSConfig.getCitiesUrl(), bahraCode);
		try {
			ResponseEntity<CipresCityResponse[]> response = restClient.exchangeGet(url, CipresCityResponse[].class);
			if (isSuccessfulResponse(response)) {
				var cipresCityResponse = Arrays.asList(response.getBody());
				if (cipresCityResponse.size() > 1)
					return cipresCityResponse.stream().filter(c -> matchesCityAndDepartment(c, city, department))
							.findFirst()
							.map(c -> cipresWSConfig.getCitiesUrl() + "/" + c.getId());
				return Optional.of(cipresWSConfig.getCitiesUrl() + "/" + cipresCityResponse.get(0).getId());
			} else
				return getCityIRIByCityName(city, department);
		} catch (RestTemplateApiException e) {
			saveStatusError(this.cipresEncounterId, this.encounterId, e.getMessage(), e.getStatusCode().value());
			log.debug("Error al intentar obtener la localidad");
		} catch (ResourceAccessException e) {
			handleResourceAccessException(e, this.encounterId, this.cipresEncounterId);
		}
		return Optional.empty();
	}

	private boolean matchesCityAndDepartment(CipresCityResponse city, String cityName, String departmentName) {
		return (city.getDescripcionLocalidad()).toLowerCase().replaceAll("[^a-zA-Z0-9]", "")
				.equals(cityName.toLowerCase().replaceAll("[^a-zA-Z0-9]", "")) &&
				((String) city.getPartido().get(DEPARTMENT_DESCRIPTION)).toLowerCase().replaceAll("[^a-zA-Z0-9]", "")
						.equals(departmentName.toLowerCase().replaceAll("[^a-zA-Z0-9]", ""));
	}

	public Optional<String> getCityIRIByCityName(String city, String department) {
		String url = String.format("%s?descripcionLocalidad=%s", cipresWSConfig.getCitiesUrl(), city.toUpperCase());
		try {
			ResponseEntity<CipresCityResponse[]>  response = restClient.exchangeGet(url, CipresCityResponse[].class);
			if (isSuccessfulResponse(response)) {
				var cipresCityResponse = Arrays.asList(response.getBody());
				var cities = cipresCityResponse.stream().filter(c -> matchesCityAndDepartment(c, city, department)).collect(Collectors.toList());
				if (cities.isEmpty())
					saveStatusError(this.cipresEncounterId, this.encounterId, NO_ADDRESS_MATCHING_DATA, HttpStatus.BAD_REQUEST.value());
				else
					return cities.stream().findFirst().map(c -> cipresWSConfig.getCitiesUrl() + "/" + c.getId());
			} else
				saveStatusError(this.cipresEncounterId, this.encounterId, NO_ADDRESS_MATCHING_DATA, HttpStatus.BAD_REQUEST.value());
		} catch (RestTemplateApiException e) {
			log.debug("Error al intentar obtener la localidad");
			saveStatusError(this.cipresEncounterId, this.encounterId, e.getMessage(), e.getStatusCode().value());
		} catch (ResourceAccessException e) {
			handleResourceAccessException(e, this.encounterId, this.cipresEncounterId);
		}
		return Optional.empty();
	}

	private String buildPatientUrl(BasicDataPersonBo basicDataPersonBo, String establishmentId) {
		return String.format(
				GET_PATIENT_URL,
				cipresWSConfig.getPatientUrl(),
				!foreignDocumentTypes.contains(basicDataPersonBo.getIdentificationTypeId()),
				basicDataPersonBo.getIdentificationNumber(),
				getCipresGenderInitial(basicDataPersonBo.getGenderId()),
				establishmentId
		);
	}

	private String getCipresDocumentType(Short documentType) {
		if (nationalDocumentTypes.contains(documentType))
			return CipresMasterData.DNI_DOCUMENT_TYPE;
		if (foreignDocumentTypes.contains(documentType))
			return CipresMasterData.FOREIGN_DOCUMENT_TYPE;
		return CipresMasterData.NOT_SPECIFIED_DOCUMENT_TYPE;
	}

	private String getCipresGenderMasterData(Short genderId) {
		switch (genderId) {
			case GENDER_FEMALE:
				return CipresMasterData.GENDER_FEMALE;
			case GENDER_MALE:
				return CipresMasterData.GENDER_MALE;
			default:
				return CipresMasterData.GENDER_NONBINARY;
		}
	}

	private String getCipresGenderInitial(Short genderId) {
		switch (genderId) {
			case GENDER_FEMALE:
				return CipresMasterData.GENDER_FEMALE_INITIAL;
			case GENDER_MALE:
				return CipresMasterData.GENDER_MALE_INITIAL;
			default:
				return CipresMasterData.GENDER_NONBINARY_INITIAL;
		}
	}

	private void createCipresPatient(Integer patientId, Long cipresPatientId) {
		cipresPatientRepository.save(new CipresPatient(new CipresPatientPk(patientId, cipresPatientId), LocalDate.now()));
	}

	private boolean validIdentificationPatientData(BasicDataPersonBo person) {
		return person.getGenderId() != null && person.getIdentificationTypeId() != null && person.getIdentificationNumber() != null;
	}

	private boolean validPatientMinimalData(PersonDataBo person) {
		return person.getBirthDate() != null && person.getFirstName() != null && person.getLastName() != null;
	}

	private boolean validCuitIdentificationTypeData(BasicDataPersonBo basicDataPersonBo) {
		boolean isCUITIdentificationType = basicDataPersonBo.getIdentificationTypeId().equals(IDENTIFICATION_TYPE_CUIT);
		return !isCUITIdentificationType || basicDataPersonBo.getIdentificationNumber().length() == CUIT_LENGTH;
	}

	private boolean patientHasAddressData(PersonDataBo person) {
		return person.getCountry() != null && (person.getCityBahraCode() != null || (person.getCity() != null && person.getDepartment() != null));
	}

}
