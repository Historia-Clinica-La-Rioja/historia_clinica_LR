package net.pladema.person.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import net.pladema.person.repository.PersonExtendedRepository;
import net.pladema.person.repository.PersonRepository;
import net.pladema.person.repository.domain.CompletePersonVo;
import net.pladema.person.repository.domain.PersonalInformation;
import net.pladema.person.repository.entity.Person;
import net.pladema.person.repository.entity.PersonExtended;
import net.pladema.person.service.PersonService;
import ar.lamansys.sgx.shared.exceptions.NotFoundException;

@Service
public class PersonServiceImpl implements PersonService {

    private static final Logger LOG = LoggerFactory.getLogger(PersonServiceImpl.class);
    public static final String OUTPUT = "Output -> {}";

    private final PersonRepository personRepository;
    private final PersonExtendedRepository personExtendedRepository;

    public PersonServiceImpl(PersonRepository personRepository, PersonExtendedRepository personExtendedRepository) {
        super();
        this.personRepository = personRepository;
        this.personExtendedRepository = personExtendedRepository;
    }

    @Override
    public Person addPerson(Person person) {
        LOG.debug("Going to save -> {}", person);
        Person personSaved = personRepository.save(person);
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

    private Supplier<NotFoundException> personNotFound(Integer personId) {
        return () -> new NotFoundException("person-not-exists", String.format("La persona %s no existe", personId));
    }

}
