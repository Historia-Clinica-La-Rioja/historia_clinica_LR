package net.pladema.person.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

import ar.lamansys.sgh.shared.domain.general.ContactInfoBo;
import ar.lamansys.sgh.shared.infrastructure.output.CompletePersonNameVo;
import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import net.pladema.address.repository.entity.Address;
import net.pladema.patient.controller.dto.AuditPatientSearch;
import net.pladema.person.repository.domain.CompletePersonNameBo;
import net.pladema.person.repository.domain.DuplicatePersonVo;

import net.pladema.person.repository.domain.PersonSearchResultVo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
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
	public String getCompletePersonNameById(Integer personId) {
		LOG.debug("Input parameters -> personId {}", personId);
		CompletePersonNameVo personName = personRepository.getCompletePersonNameById(personId);
		return parseCompletePersonName(personName.getFirstName(), personName.getMiddleNames(), personName.getLastName(), personName.getOtherLastNames(), personName.getSelfDeterminateName());
	}

	@Override
	public String parseCompletePersonName(String firstName, String middleNames, String lastName, String otherLastNames, String selfDeterminateName) {
		String finalFirstName = featureFlagsService.isOn(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS) && selfDeterminateName != null ? selfDeterminateName : middleNames != null ? String.join(" ", firstName != null ? firstName : "", middleNames != null ? middleNames : "") : firstName != null ? firstName : "";
		String finalLastName = otherLastNames != null ? String.join(" ", lastName != null ? lastName : "", otherLastNames != null ? otherLastNames : "") : lastName != null ? lastName : "";
		return String.join(" ", finalFirstName, finalLastName);
	}

	@Override
	public ContactInfoBo getContactInfoById(Integer personId) {
		LOG.debug("Input parameters -> personId {}", personId);
		ContactInfoBo result = personExtendedRepository.getContactInfoById(personId);
		LOG.debug(OUTPUT, result);
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

}
