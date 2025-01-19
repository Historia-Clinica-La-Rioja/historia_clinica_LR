package net.pladema.person.controller.service;

import ar.lamansys.sgh.shared.infrastructure.input.service.BasicDataPersonDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.PersonFileDto;
import lombok.extern.slf4j.Slf4j;
import net.pladema.address.controller.service.AddressExternalService;
import net.pladema.address.repository.CityRepository;
import net.pladema.address.service.AddressMasterDataService;
import net.pladema.patient.controller.dto.APatientDto;
import net.pladema.patient.controller.dto.AuditPatientSearch;
import net.pladema.patient.controller.dto.DuplicatePatientDto;
import net.pladema.patient.controller.dto.PatientPersonalInfoDto;
import net.pladema.patient.repository.entity.EducationLevel;
import net.pladema.patient.repository.entity.Occupation;
import net.pladema.person.controller.dto.BMPersonDto;
import net.pladema.person.controller.dto.BasicPersonalDataDto;
import net.pladema.person.controller.dto.PersonPhotoDto;
import net.pladema.person.controller.dto.PersonalInformationDto;
import net.pladema.person.controller.mapper.PersonMapper;
import net.pladema.person.controller.service.exceptions.CreatePersonEnumException;
import net.pladema.person.controller.service.exceptions.CreatePersonException;
import net.pladema.person.repository.domain.DuplicatePersonVo;
import net.pladema.person.repository.domain.PersonalInformation;
import net.pladema.person.repository.entity.Ethnicity;
import net.pladema.person.repository.entity.Gender;
import net.pladema.person.repository.entity.IdentificationType;
import net.pladema.person.repository.entity.Person;
import net.pladema.person.repository.entity.PersonExtended;
import net.pladema.person.repository.entity.SelfPerceivedGender;
import net.pladema.person.service.PersonMasterDataService;
import net.pladema.person.service.PersonPhotoService;
import net.pladema.person.service.PersonService;
import net.pladema.user.controller.dto.UserPersonDto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PersonExternalServiceImpl implements PersonExternalService {

	private static final String ONE_INPUT_PARAMETER = "Input parameters -> {}";

	private static final Logger LOG = LoggerFactory.getLogger(PersonExternalServiceImpl.class);

	public static final String OUTPUT = "Output -> {}";

	private final PersonService personService;

	private final PersonMasterDataService personMasterDataService;

	private final PersonPhotoService personPhotoService;

	private final PersonMapper personMapper;

	private final AddressMasterDataService addressMasterDataService;

	private final PersonFileExternalService personFileExternalService;

	public PersonExternalServiceImpl(PersonService personService, PersonMasterDataService personMasterDataService, PersonMapper personMapper, PersonPhotoService personPhotoService, AddressExternalService addressExternalService, CityRepository cityRepository, AddressMasterDataService addressMasterDataService, PersonFileExternalService personFileExternalService) {
		super();
		this.personService = personService;
		this.personMasterDataService = personMasterDataService;
		this.personMapper = personMapper;
		this.personPhotoService = personPhotoService;
		this.addressMasterDataService = addressMasterDataService;
		this.personFileExternalService = personFileExternalService;
		LOG.debug("{}", "created service");
	}

	@Override
	public BMPersonDto addPerson(APatientDto patient) {
		LOG.debug(ONE_INPUT_PARAMETER, patient);
		Person newPerson = personMapper.fromAPatientDto(patient);
		return persistPerson(newPerson);
	}

	@Override
	public void addPersonExtended(APatientDto patient, Integer personId, Integer addressId) {
		LOG.debug("Input parameters -> {}, {}, {}", patient, personId, addressId);
		validatePersonAddress(patient);
		PersonExtended personExtendedToAdd = personMapper.toPersonExtended(patient, addressId);
		LOG.debug("Mapped result updatePersonExtendedPatient -> {}", personExtendedToAdd);
		personExtendedToAdd.setId(personId);
		personExtendedToAdd.setAddressId(addressId);
		LOG.debug("Going to add person extended -> {}", personExtendedToAdd);
		PersonExtended personExtendedSaved = personService.addPersonExtended(personExtendedToAdd);
		LOG.debug("PersonExtended added -> {}", personExtendedSaved);
	}

	private void validatePersonAddress(APatientDto patient) {
		if (patient.getCountryId() != null && patient.getProvinceId() !=  null && !addressMasterDataService.existProvinceInCountry(patient.getCountryId(), patient.getProvinceId()))
			throw new CreatePersonException(CreatePersonEnumException.INVALID_ENTRY_ADDRESS, "La provincia no pertenece al pais seleccionado");
		if (patient.getProvinceId() != null && patient.getDepartmentId() !=  null && !addressMasterDataService.existDepartmentInProvince(patient.getProvinceId(), patient.getDepartmentId()))
			throw new CreatePersonException(CreatePersonEnumException.INVALID_ENTRY_ADDRESS, "El departamento no pertenece a la provincia seleccionada");
		if (patient.getDepartmentId() != null && patient.getCityId() !=  null && !addressMasterDataService.existCityInDepartment(patient.getDepartmentId(), patient.getCityId()))
			throw new CreatePersonException(CreatePersonEnumException.INVALID_ENTRY_ADDRESS, "La ciudad no pertenece al departamento seleccionado");
	}

	@Override
	public PersonExtended updatePersonExtended(APatientDto patient, Integer personId) {
		LOG.debug("Input parameters -> {}, {}", patient, personId);
		PersonExtended personExtendedToUpdate = personService.getPersonExtended(personId);
		personExtendedToUpdate = personMapper.updatePersonExtendedPatient(personExtendedToUpdate, patient);
		LOG.debug("Going to add person extended -> {}", personExtendedToUpdate);
		PersonExtended personExtendedUpdated = personService.addPersonExtended(personExtendedToUpdate);
		LOG.debug("PersonExtended added -> {}", personExtendedUpdated);
		return personExtendedUpdated;
	}
	
	@Override
	public List<Integer> getPersonByDniAndGender(Short identificationTypeId, String identificationNumber,
			Short genderId) {
		List<Integer> result = personService.getPersonByDniAndGender(identificationTypeId, identificationNumber,
				genderId);
		LOG.debug(OUTPUT, result);
		return result;
	}

	@Override
	public PersonalInformationDto getPersonalInformation(Integer personId) {
		LOG.debug(ONE_INPUT_PARAMETER, personId);
		PersonalInformation serviceResult = personService.getPersonalInformation(personId).orElse(null);
		PersonalInformationDto result = personMapper.fromPersonalInformation(serviceResult);
		LOG.debug(OUTPUT, result);
		return result;
	}

	@Override
	public BasicDataPersonDto getBasicDataPerson(Integer personId) {
		LOG.debug(ONE_INPUT_PARAMETER, personId);
		if (personId != null) {
			Person person = personService.getPerson(personId);
			PersonExtended personExtended = personService.getPersonExtended(personId);
			List<PersonFileDto> personFileDtoList = personFileExternalService.getFiles(personId);
			BasicDataPersonDto result = personMapper.basicDataFromPerson(
					person,
					getGender(person.getGenderId()),
					getSelfPerceivedGender(personExtended.getGenderSelfDeterminationId(), personExtended.getId()),
					getIdentificationType(person.getIdentificationTypeId()),
					getOccupation(personExtended.getOccupationId()),
					getEducationLevel(personExtended.getEducationLevelId()),
					personExtended.getReligion(),
					getEthnicity(personExtended.getEthnicityId())
			);
			result.setEmail(personExtended.getEmail());
			result.setNameSelfDetermination(personExtended.getNameSelfDetermination());
			if (!personFileDtoList.isEmpty()) {
				result.setFiles(personFileDtoList);
			}
			LOG.debug(OUTPUT, result);
			return result;
		}
		else
			return new BasicDataPersonDto();
	}


	private String getOccupation(Integer occupationId) {
		return personMasterDataService.getOccupationById(occupationId).orElse(new Occupation()).getDescription();
	}

	private String getEducationLevel(Integer educationLevelId) {
		return personMasterDataService.getEducationLevelById(educationLevelId).orElse(new EducationLevel()).getDescription();
	}

	private String getEthnicity(Integer ethnicityId) {
		return personMasterDataService.getEthnicityById(ethnicityId).orElse(new Ethnicity()).getPt();
	}

	@Override
	public Optional<BasicDataPersonDto> findBasicDataPerson(Integer personId) {
		LOG.debug(ONE_INPUT_PARAMETER, personId);
		PersonExtended personExtended = personService.getPersonExtended(personId);
		Optional<BasicDataPersonDto> basicDataPerson = personService.findPerson(personId)
			.map(person -> personMapper.basicDataFromPerson(
					person,
					getGender(person.getGenderId()),
					getSelfPerceivedGender(personExtended.getGenderSelfDeterminationId(), personExtended.getId()),
					getIdentificationType(person.getIdentificationTypeId())
			));
		LOG.debug(OUTPUT, basicDataPerson);
		return basicDataPerson;
	}

	private IdentificationType getIdentificationType(Short identificationTypeId) {
		return personMasterDataService.getIdentificationType(identificationTypeId).orElse(new IdentificationType());
	}

	@Override
	public List<BasicDataPersonDto> getBasicDataPerson(Set<Integer> personIds) {
		LOG.debug(ONE_INPUT_PARAMETER, personIds);
		List<Person> people = personService.getPeople(personIds);
		List<Gender> genders = personMasterDataService.getGenders();
		List<SelfPerceivedGender> selfPerceivedGenders = personMasterDataService.getSelfPerceivedGender();
		List<IdentificationType> identificationTypes = personMasterDataService.getIdentificationTypes();

		List<BasicDataPersonDto> result = people.parallelStream()
				.map(p -> mapToBasicDataDto(p, genders, selfPerceivedGenders, identificationTypes))
				.collect(Collectors.toList());
		
		LOG.debug("Result size {}", result.size());
		LOG.trace(OUTPUT, result);
		return result;
	}

	private BasicDataPersonDto mapToBasicDataDto(Person person, List<Gender> genders, List<SelfPerceivedGender> selfPerceivedGenders, List<IdentificationType> identificationTypes) {
		LOG.debug("Input parameters -> person {}, genders {}, identificationTypes {} ", person, genders, identificationTypes);
		PersonExtended personExtended = personService.getPersonExtended(person.getId());
		Gender gender = genders.stream()
				.filter(g -> g.getId().equals(person.getGenderId())).findAny()
				.orElse(new Gender());

		String selfPerceivedGender = selfPerceivedGenders.stream()
				.filter(spg -> spg.getId().equals(personExtended.getGenderSelfDeterminationId())).findAny()
				.map(SelfPerceivedGender::getDescription)
				.orElse(null);

		IdentificationType identificationType = identificationTypes.stream()
				.filter(i -> i.getId().equals(person.getIdentificationTypeId())).findAny()
				.orElse(new IdentificationType());

		BasicDataPersonDto result = personMapper.basicDataFromPerson(person, gender, selfPerceivedGender, identificationType);
		result.setNameSelfDetermination(personExtended.getNameSelfDetermination());
		LOG.debug("BasicDataPersonDto id result {}", result.getId());
		return result;
	}

	@Override
	public BasicPersonalDataDto getBasicPersonalDataDto(Integer personId) {
		LOG.debug(ONE_INPUT_PARAMETER, personId);
		Person person = personService.getPerson(personId);
		PersonExtended personExtended = personService.getPersonExtended(personId);
		BasicPersonalDataDto result = personMapper.basicPersonalDataDto(person);
		result.setPhonePrefix(personExtended.getPhonePrefix());
		result.setPhoneNumber(personExtended.getPhoneNumber());
		result.setNameSelfDetermination(personExtended.getNameSelfDetermination());
		result.setCuil(personExtended.getCuil());
		LOG.debug(OUTPUT, result);
		return result;
	}

	@Override
	public BMPersonDto updatePerson(APatientDto patient, Integer personId) {
		LOG.debug(ONE_INPUT_PARAMETER, patient);
		Person personToUpdate = personMapper.fromAPatientDto(patient);
		personToUpdate.setId(personId);
		return persistPerson(personToUpdate);
	}

	private BMPersonDto persistPerson(Person personToUpdate) {
		LOG.debug("Mapped result fromAPatientDto -> {}", personToUpdate);
		personToUpdate = personService.addPerson(personToUpdate);
		BMPersonDto result = personMapper.fromPerson(personToUpdate);
		LOG.debug("Mapped result fromPerson -> {}", result);
		LOG.debug(OUTPUT, result);
		return result;
	}

	@Override
	public PersonPhotoDto getPersonPhoto(Integer personId) {
		LOG.debug("Input parameter -> personId {}", personId);
		PersonPhotoDto personPhotoDto = personPhotoService.get(personId);
		LOG.debug(OUTPUT, personPhotoDto);
		return personPhotoDto;
	}

	@Override
	public List<PersonPhotoDto> getPersonsPhotos(List<Integer> personIds) {
		LOG.debug("Input parameter -> personIds {}", personIds);
		List<PersonPhotoDto> personsPhotos = personPhotoService.get(personIds);
		LOG.debug(OUTPUT, personsPhotos);
		return personsPhotos;
	}

	@Override
	public boolean savePersonPhoto(Integer personId, String imageData) {
		LOG.debug("Input parameters -> personId {}, imageData {}", personId, imageData);
		boolean result = personPhotoService.save(personId, imageData);
		LOG.debug(OUTPUT, result);
		return result;
	}

	@Override
	public List<DuplicatePatientDto> getDuplicatePersonsByFilter(AuditPatientSearch auditPatientSearch) {
		LOG.debug("Input parameters -> AuditPatientSearch", auditPatientSearch);
		List<DuplicatePatientDto> result = personService.getDuplicatePersonsByFilter(auditPatientSearch).stream().map(DuplicatePatientDto::new).collect(Collectors.toList());
		LOG.debug(OUTPUT, result);
		return result;
	}
	
	public boolean saveEmail(Integer personId, String email) {
		PersonExtended entity = personService.getPersonExtended(personId);
		entity.setEmail(email);
		personService.addPersonExtended(entity);
		return Boolean.TRUE;
	}

	@Override
	public List<PatientPersonalInfoDto> getPatientsPersonalInfo(DuplicatePatientDto duplicatePatientDto) {
		LOG.debug("Input parameters -> DuplicatePatientDto", duplicatePatientDto);
		List<PatientPersonalInfoDto> result = personService.getPatientsPersonalInfo(new DuplicatePersonVo(duplicatePatientDto)).stream().map(PatientPersonalInfoDto::new).collect(Collectors.toList());
		LOG.debug(OUTPUT, result);
		return result;
	}

	@Override
	public UserPersonDto getUserPersonInformation(Integer personId) {
		log.debug("Input parameters -> personId {} ", personId);
		Person person = personService.getPerson(personId);
		PersonExtended personExtended = personService.getPersonExtended(personId);
		UserPersonDto result = new UserPersonDto(person.getFirstName(), person.getLastName(), person.getMiddleNames(), person.getOtherLastNames(), personExtended.getNameSelfDetermination());
		log.debug("Output", result);
		return result;
	}

	private Gender getGender(Short genderId) {
		return personMasterDataService.getGender(genderId).orElse(new Gender());
	}

	private String getSelfPerceivedGender(Short selfPerceivedGenderId, Integer personId){
		if(selfPerceivedGenderId != null && selfPerceivedGenderId == SelfPerceivedGender.NINGUNA_DE_LAS_ANTERIORES)
			return personService.getPersonExtended(personId).getOtherGenderSelfDetermination();
		return personMasterDataService.getSelfPerceivedGenderById(selfPerceivedGenderId).orElse(new String());
	}
}
