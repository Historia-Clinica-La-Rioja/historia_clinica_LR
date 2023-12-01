package net.pladema.cipres.infrastructure.output.rest;

import ar.lamansys.sgx.shared.restclient.configuration.resttemplate.exception.RestTemplateApiException;
import lombok.extern.slf4j.Slf4j;
import net.pladema.cipres.application.port.CipresPatientStorage;
import net.pladema.cipres.application.port.CipresPersonStorage;
import net.pladema.cipres.application.port.CipresStorage;
import net.pladema.cipres.domain.BasicDataPatientBo;
import net.pladema.cipres.domain.BasicDataPersonBo;
import net.pladema.cipres.domain.PersonDataBo;
import net.pladema.cipres.infrastructure.output.repository.CipresPatient;
import net.pladema.cipres.infrastructure.output.repository.CipresPatientPk;
import net.pladema.cipres.infrastructure.output.repository.CipresPatientRepository;
import net.pladema.cipres.infrastructure.output.rest.domain.CipresCityResponse;
import net.pladema.cipres.infrastructure.output.rest.domain.CipresPatientResponse;
import net.pladema.cipres.infrastructure.output.rest.domain.CipresMasterData;
import net.pladema.cipres.infrastructure.output.rest.domain.CipresPatientPayload;


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

	private static final String ADDRESS = "domicilio";

	private static final String ID = "id";

	private static final String DEPARTMENT_DESCRIPTION = "descripcionPartido";

	private static final String ESTABLISHMENT_URL = "?establecimiento=";

	private static final String GET_PATIENT_URL = "%s?esDocumentoArgentino=%s&numeroDocumento=%s&sexo=%s&esDocumentoPropio=true&incluirRelaciones=false&incluirDomicilio=true&establecimiento=%s";

	private static final List<Short> nationalDocumentTypes = List.of(IDENTIFICATION_TYPE_DNI, IDENTIFICATION_TYPE_CI, IDENTIFICATION_TYPE_LC, IDENTIFICATION_TYPE_LE, IDENTIFICATION_TYPE_CUIT);

	private static final List<Short> foreignDocumentTypes = List.of(IDENTIFICATION_TYPE_FOREIGN_PASSPORT, IDENTIFICATION_TYPE_FOREIGN_IDENTITY_CARD, IDENTIFICATION_TYPE_OTHER_FOREIGN_DOCUMENT, IDENTIFICATION_TYPE_CEDULA_MERCOSUR);

	private final CipresPersonStorage cipresPersonStorage;

	private final CipresPatientRepository cipresPatientRepository;

	public CipresPatientStorageImpl (CipresRestTemplate cipresRestTemplate,
									 CipresWSConfig cipresWSConfig,
									 CipresPersonStorage cipresPersonStorage,
									 CipresPatientRepository cipresPatientRepository) {
		super(cipresRestTemplate, cipresWSConfig);
		this.cipresPersonStorage = cipresPersonStorage;
		this.cipresPatientRepository = cipresPatientRepository;
	}

	@Override
	public Optional<Long> getPatientId(BasicDataPatientBo patientData, String establishmentId) {
		var cipresPatientId = this.cipresPatientRepository.getCipresPatientId(patientData.getId());
		return cipresPatientId.or(() -> foundPatientId(patientData, establishmentId));
	}

	private Optional<Long> foundPatientId(BasicDataPatientBo patientData, String establishmentId) {
		String url = this.buildPatientUrl(patientData.getPerson(), establishmentId);
		Optional<Long> result = Optional.empty();
		try {
			ResponseEntity<CipresPatientResponse[]> response = restClient.exchangeGet(url, CipresPatientResponse[].class);
			if (isSuccessfulResponse(response))
				result = processPatientSuccessfulResponse(response.getBody(), establishmentId, patientData);
		} catch (RestTemplateApiException e) {
			result = handlePatientRestTemplateApiException(e, patientData, establishmentId);
		} catch (ResourceAccessException e) {
			handleResourceAccessException(e);
		}
        result.ifPresent(cipresPatientId -> createCipresPatient(patientData.getId(), cipresPatientId));
		return result;
	}

	private Optional<Long> handlePatientRestTemplateApiException(RestTemplateApiException e, BasicDataPatientBo patientData,
																	String establishmentId) {
		var personData = patientData.getPerson();
		if (e.getStatusCode().equals(HttpStatus.NOT_FOUND) && validIdentificationData(personData)) {
			PersonDataBo person = cipresPersonStorage.getPersonData(personData.getId());
			if (validPatientMinimalData(person))
				return createPatient(person, establishmentId);
		} else
			log.debug("Error al intentar obtener un paciente");
		return Optional.empty();
	}

	private Optional<Long> processPatientSuccessfulResponse(CipresPatientResponse[] patients, String establishmentId,
															   BasicDataPatientBo patientData) {
		var patient = patients[0].getPaciente();
		var patientId = patient.get(ID).toString();

		if (patient.get(ADDRESS) != null)
			return Optional.of(Long.parseLong(patientId));

		PersonDataBo person = cipresPersonStorage.getPersonData(patientData.getPerson().getId());
		if (validAddressData(person)) {
			var addressId = savePatientAddress(person, patientId, establishmentId);
			if (addressId.isPresent())
				return Optional.of(Long.parseLong(patientId));
		}

		return Optional.empty();
	}

	public Optional<Integer> savePatientAddress(PersonDataBo person, String patientId, String establishmentId) {
		String url = cipresWSConfig.getAddressUrl().concat(ESTABLISHMENT_URL+ establishmentId);
		var body = mapToCipresPatientAddressPayload(person);
		if (body.getLocalidad() != null && body.getNacionalidad() != null) {
			body.setPaciente("api/paciente/" + patientId);
			try {
				var response = restClient.exchangePost(url, body, CipresEntityResponse.class);
				if (response != null && response.getBody() != null)
					return Optional.ofNullable(response.getBody().getId() != null ? Integer.parseInt(response.getBody().getId()) : null);
			} catch (RestTemplateApiException e) {
				log.warn("Error al intentar actualizar el domicilio del paciente en la api", e);
			} catch (ResourceAccessException e) {
				handleResourceAccessException(e);
			}
		}
		return Optional.empty();
	}

	@Override
	public Optional<Long> createPatient(PersonDataBo person, String establishmentId) {
		String url = cipresWSConfig.getPatientUrl().concat(ESTABLISHMENT_URL + establishmentId);
		CipresPatientAddressPayload address = mapToCipresPatientAddressPayload(person);
		if (address.getLocalidad() != null && address.getNacionalidad() != null) {
			CipresPatientPayload body = mapToPatientPayload(person, address);
			try {
				var response = restClient.exchangePost(url, body, CipresEntityResponse.class);
				if (response != null && response.getBody() != null && response.getBody().getId() != null)
					return Optional.of(Long.parseLong(response.getBody().getId()));
			} catch (RestTemplateApiException e) {
				log.warn("Error al intentar insertar un paciente en la api");
			} catch (ResourceAccessException e) {
				handleResourceAccessException(e);
			}
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

	private CipresPatientAddressPayload mapToCipresPatientAddressPayload(PersonDataBo personInfo) {
		String personContact = personInfo.getPhonePrefix() != null &&  personInfo.getPhoneNumber() != null ?
				personInfo.getPhonePrefix().concat(personInfo.getPhoneNumber()) : null;
		var city = personInfo.getCityBahraCode() != null
				? getCityIRI(personInfo.getCityBahraCode(), personInfo.getCity(), personInfo.getDepartment()).orElse(null)
				: getCityIRIByCityName(personInfo.getCity(), personInfo.getDepartment()).orElse(null);
		var nationality = getNationalityIRI(personInfo.getCountry()).orElse(null);
		return CipresPatientAddressPayload.builder()
				.telefono(personContact)
				.celular(personContact)
				.email(personInfo.getEmail())
				.calle(personInfo.getStreet())
				.nro(personInfo.getNumber())
				.piso(personInfo.getFloor() != null && personInfo.getFloor().matches("\\d+") ? Integer.parseInt(personInfo.getFloor()) : null)
				.departamento(personInfo.getApartment())
				.barrio((personInfo.getQuarter()))
				.cp(personInfo.getPostcode())
				.localidad(city)
				.nacionalidad(nationality)
				.build();
	}

	public Optional<String> getNationalityIRI(String country) {
		String url = String.format("%s?descripcionPais=%s", cipresWSConfig.getCountryUrl(), country.toUpperCase());
		try {
			ResponseEntity<CipresEntityResponse[]>  response = restClient.exchangeGet(url, CipresEntityResponse[].class);
			if (isSuccessfulResponse(response)) {
				var cipresCountry = Arrays.asList(response.getBody()).get(0);
				return Optional.of(cipresWSConfig.getNacionalityUrl().concat("/").concat(cipresCountry.getId()));
			}
		} catch (RestTemplateApiException e) {
			log.debug("Error al intentar obtener la nacionalidad");
		} catch (ResourceAccessException e) {
			handleResourceAccessException(e);
		}
		return Optional.empty();
	}

	public Optional<String> getCityIRI(String bahraCode, String city, String department) {
		String url = String.format("%s?codigoBahra=%s", cipresWSConfig.getCitiesUrl(), bahraCode);
		try {
			ResponseEntity<CipresCityResponse[]> response = restClient.exchangeGet(url, CipresCityResponse[].class);
			if (response != null ) {
				if (response.getBody() != null && response.getBody().length > 0) {
					var cipresCityResponse = Arrays.asList(response.getBody());
					if (cipresCityResponse.isEmpty())
						return getCityIRIByCityName(city, department);
					else {
						if (cipresCityResponse.size() > 1)
							return cipresCityResponse.stream().filter(c -> matchesCityAndDepartment(c, city, department))
									.findFirst()
									.map(c -> cipresWSConfig.getCitiesUrl() + "/" + c.getId());
						return Optional.of(cipresWSConfig.getCitiesUrl() + "/" + cipresCityResponse.get(0).getId());
					}

				} else
					return getCityIRIByCityName(city, department);
			}
		} catch (RestTemplateApiException e) {
			log.debug("Error al intentar obtener la localidad");
		} catch (ResourceAccessException e) {
			handleResourceAccessException(e);
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
				return cipresCityResponse.stream().filter(c -> matchesCityAndDepartment(c, city, department))
						.findFirst()
						.map(c -> cipresWSConfig.getCitiesUrl() + "/" + c.getId());
			}
		} catch (RestTemplateApiException e) {
			log.debug("Error al intentar obtener la localidad");
		} catch (ResourceAccessException e) {
			handleResourceAccessException(e);
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

	private boolean validPatientMinimalData(PersonDataBo person) {
		return person.getBirthDate() != null && person.getFirstName() != null && person.getLastName() != null && validAddressData(person);
	}

	private boolean validIdentificationData(BasicDataPersonBo basicDataPersonBo) {
		boolean isCUITIdentificationType = basicDataPersonBo.getIdentificationTypeId().equals(IDENTIFICATION_TYPE_CUIT);
		return !isCUITIdentificationType || basicDataPersonBo.getIdentificationNumber().length() == CUIT_LENGTH;
	}

	private boolean validAddressData (PersonDataBo person) {
		return person.getCountry() != null && (person.getCityBahraCode() != null || (person.getCity() != null && person.getDepartment() != null));
	}
	
	private void handleResourceAccessException(ResourceAccessException e) {
		log.debug("Fallo en la comunicación - API SALUD", e);
	}

	private <T> boolean isSuccessfulResponse(ResponseEntity<T[]> response) {
		return response != null && response.getBody() != null && response.getBody().length > 0;
	}

}
