package net.pladema.person.service.impl;

import ar.lamansys.sgh.shared.domain.general.ContactInfoBo;
import ar.lamansys.sgh.shared.infrastructure.output.CompletePersonNameVo;
import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.pladema.patient.controller.dto.AuditPatientSearch;
import net.pladema.person.repository.domain.CompletePersonNameBo;
import net.pladema.person.repository.domain.DuplicatePersonVo;

import net.pladema.person.repository.domain.PersonSearchResultVo;

import net.pladema.user.domain.PersonBo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.pladema.person.repository.PersonExtendedRepository;
import net.pladema.person.repository.PersonHistoryRepository;
import net.pladema.person.repository.PersonRepository;
import net.pladema.person.repository.domain.CompletePersonVo;
import net.pladema.person.repository.domain.PersonalInformation;
import net.pladema.person.repository.entity.Person;
import net.pladema.person.repository.entity.PersonExtended;
import net.pladema.person.repository.entity.PersonHistory;
import net.pladema.person.service.PersonService;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    private static final Logger LOG = LoggerFactory.getLogger(PersonServiceImpl.class);
    public static final String OUTPUT = "Output -> {}";

    private final PersonRepository personRepository;
    private final PersonExtendedRepository personExtendedRepository;
	private final PersonHistoryRepository personHistoryRepository;
	private final FeatureFlagsService featureFlagsService;


    @Override
    public Person addPerson(Person person) {
        LOG.debug("Going to save -> {}", person);
        Person personSaved = personRepository.save(person);
		personHistoryRepository.save(new PersonHistory(personSaved));
        LOG.debug("Person saved -> {}", personSaved);
        return personSaved;
    }

    @Override
    public Person getPerson(Integer id) {
        return findPerson(id).orElseThrow(this.personNotFound(id));
    }

    @Override
    public Optional<Person> findPerson(Integer id) {
        LOG.debug("Going to get person -> {}", id);
        Optional<Person> result = personRepository.findById(id);
        LOG.debug("Person gotten-> {}", result);
        return result;
    }

    @Override
    public List<Person> getPeople(Set<Integer> personIds) {
        LOG.debug("Going to get person -> {}", personIds.size());
        LOG.trace("Going to get person -> {}", personIds);
        List<Person> result = personRepository.findAllById(personIds);
        LOG.debug("Person gotten-> {}", result);
        return result;
    }

    @Override
	public PersonExtended getPersonExtended(Integer personId) {
		LOG.debug("Going to get PersonExtended -> {}", personId);
		PersonExtended personExtFound = personExtendedRepository.findById(personId)
				.orElseThrow(this.personNotFound(personId));
		   LOG.debug("PersonExtended found-> {}", personExtFound);
		return personExtFound;
	}

    @Override
    public PersonExtended addPersonExtended(PersonExtended person) {
        LOG.debug("Going to save -> {}", person);
        PersonExtended personSaved = personExtendedRepository.save(person);
        LOG.debug("Person extended saved -> {}", personSaved);
        return personSaved;
    }

    @Override
    public List<Integer> getPersonByDniAndGender(Short identificationTypeId, String identificationNumber, Short genderId) {
        LOG.debug("Input data -> identificationTypeId {}, identificationNumber {}, genderId {}", identificationTypeId,
                identificationNumber, genderId);
        List<Integer> result = personRepository.findByDniAndGender(identificationTypeId, identificationNumber, genderId);
        LOG.debug(OUTPUT, result);
        return result;
    }

    @Override
    public Optional<PersonalInformation> getPersonalInformation(Integer personId) {
        LOG.debug("Input parameters -> {}", personId);
        Optional<PersonalInformation> result = personRepository.getPersonalInformation(personId);
        LOG.debug(OUTPUT, result);
        return result;
    }

	@Override
	public Optional<CompletePersonVo> getCompletePerson(Integer personId) {
		LOG.debug("Input parameters -> {}", personId);
        Optional<CompletePersonVo> result = personRepository.getCompletePerson(personId);
        LOG.debug(OUTPUT, result);
        return result;
	}

	@Override
	public String getCountryIsoCodeFromPerson(Integer personId) {
		LOG.debug("Input parameter -> personId {} ", personId);
		return personRepository.getCountryIsoCodeFromPerson(personId);
	}

	@Override
	public List<DuplicatePersonVo> getDuplicatePersonsByFilter(AuditPatientSearch auditPatientSearch) {
		LOG.debug("Input parameters -> AuditPatientSearch", auditPatientSearch);
		List<DuplicatePersonVo> result = personRepository.getAllByFilter(auditPatientSearch);
		LOG.debug(OUTPUT, result);
		return result;
	}

	@Override
	public List<PersonSearchResultVo> getPatientsPersonalInfo(DuplicatePersonVo duplicatePersonVo) {
		LOG.debug("Input parameters -> DuplicatePersonVo", duplicatePersonVo);
		List<PersonSearchResultVo> result = personRepository.getPersonSearchResultByAttributes(duplicatePersonVo);
		LOG.debug(OUTPUT, result);
		return result;
	}

	@Override
	public Optional<CompletePersonNameBo> findByHealthcareProfessionalPersonDataByDiaryId(Integer diaryId) {
		LOG.debug("Input parameters -> diaryId {}", diaryId);
		Optional<CompletePersonNameBo> result = personRepository.findProfessionalNameByDiaryId(diaryId);
		LOG.debug(OUTPUT, result);
		return result;
	}

	@Override
	public CompletePersonNameBo findByHealthcareProfessionalId(Integer healthcareProfessionalId) {
		LOG.debug("Input parameters -> healthcareProfessionalId {}", healthcareProfessionalId);
		CompletePersonNameBo result = personRepository.findByHealthcareProfessionalId(healthcareProfessionalId);
		LOG.debug(OUTPUT, result);
		return result;
	}

	@Override
	public Optional<PersonBo> getPersonData(Integer patientId) {
		return personRepository.findPersonExtendedByPatientId(patientId);
	}

	@Override
	public String getCompletePersonNameByUserId(Integer userId) {
		LOG.debug("Input parameter -> userId {}", userId);
		CompletePersonNameVo personName = personRepository.getCompletePersonNameByUserId(userId);
		return parseCompletePersonName(personName.getFirstName(), personName.getMiddleNames(), personName.getLastName(), personName.getOtherLastNames(), personName.getSelfDeterminateName());
	}

	@Override
	public String getCompletePersonNameById(Integer personId) {
		LOG.debug("Input parameters -> personId {}", personId);
		CompletePersonNameVo personName = personRepository.getCompletePersonNameByIds(List.of(personId)).get(0);
		return parseCompletePersonName(personName.getFirstName(), personName.getMiddleNames(), personName.getLastName(), personName.getOtherLastNames(), personName.getSelfDeterminateName());
	}

	@Override
	public String getFormalPersonNameById(Integer personId) {
		LOG.debug("Input parameters -> personId {}", personId);
		CompletePersonNameVo personName = personRepository.getCompletePersonNameByIds(List.of(personId)).get(0);
		return parseFormalPersonName(personName.getFirstName(), personName.getMiddleNames(), personName.getLastName(), personName.getOtherLastNames(), personName.getSelfDeterminateName());
	}

	@Override
	public String parseCompletePersonName(String firstName, String middleNames, String lastName, String otherLastNames, String selfDeterminateName) {
		String finalFirstName = this.getFinalFirstName(firstName, middleNames, selfDeterminateName);
		String finalLastName = this.getFinalLastName(lastName, otherLastNames);
		return String.join(" ", finalFirstName, finalLastName);
	}

	@Override
	public String parseCompletePersonName(String givenName, String familyNames, String selfDeterminateName) {
		String finalFirstName = getFinalFirstName(givenName, selfDeterminateName);
		return Stream.of(finalFirstName, familyNames)
				.filter(Objects::nonNull)
				.collect(Collectors.joining(" "));
	}

	@Override
	public String parseFormalPersonName(String firstName, String middleNames, String lastName, String otherLastNames, String selfDeterminateName) {
		String finalFirstName = this.getFinalFirstName(firstName, middleNames, selfDeterminateName);
		String finalLastName = this.getFinalLastName(lastName, otherLastNames);
		return String.join(" ", finalLastName, finalFirstName);
	}

	@Override
	public ContactInfoBo getContactInfoById(Integer personId) {
		LOG.debug("Input parameters -> personId {}", personId);
		ContactInfoBo result = personExtendedRepository.getContactInfoById(personId);
		LOG.debug(OUTPUT, result);
		return result;
	}

	@Override
	public List<String> getCompletePersonNameByIds(List<Integer> personIds) {
		LOG.debug("Input parameters -> personIds {}", personIds);
		List<CompletePersonNameVo> queryResult = personRepository.getCompletePersonNameByIds(personIds);
		List<String> result = queryResult.stream().map(personName -> parseCompletePersonName(personName.getFirstName(), personName.getMiddleNames(), personName.getLastName(), personName.getOtherLastNames(), personName.getSelfDeterminateName())).collect(Collectors.toList());
		LOG.debug("Output -> {}", result);
		return result;
	}

	private Supplier<NotFoundException> personNotFound(Integer personId) {
        return () -> new NotFoundException("person-not-exists", String.format("La persona %s no existe", personId));
    }
    
    @Override
	public Optional<Person> findByPatientId(Integer patientId) {
		LOG.debug("Input parameter -> patientId {}", patientId);
		Optional<Person> result = personRepository.findPersonByPatientId(patientId);
		LOG.debug("Output result -> {}", result);
		return result;
	}

	private String getFinalFirstName(String firstName, String middleNames, String selfDeterminateName) {
		return featureFlagsService.isOn(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS) && selfDeterminateName != null ? selfDeterminateName : middleNames != null ? String.join(" ", firstName != null ? firstName : "", middleNames != null ? middleNames : "") : firstName != null ? firstName : "";
	}

	private String getFinalFirstName(String givenName, String selfDeterminateName) {
		return featureFlagsService.isOn(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS) && selfDeterminateName != null ? selfDeterminateName :  givenName;
	}

	private String getFinalLastName(String lastName, String otherLastNames) {
		return otherLastNames != null ? String.join(" ", lastName != null ? lastName : "", otherLastNames != null ? otherLastNames : "") : lastName != null ? lastName : "";
	}

}
