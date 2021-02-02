package net.pladema.person.service;

import net.pladema.person.repository.domain.CompletePersonVo;
import net.pladema.person.repository.domain.PersonalInformation;
import net.pladema.person.repository.entity.Person;
import net.pladema.person.repository.entity.PersonExtended;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PersonService {

    Person addPerson(Person person);

    Person getPerson(Integer id);

    Optional<Person> findPerson(Integer id);

    List<Person> getPeople(Set<Integer> personIds);

    PersonExtended addPersonExtended(PersonExtended person);
    
    PersonExtended getPersonExtended(Integer personId);

    List<Integer> getPersonByDniAndGender(Short identificationTypeId, String identificationNumber, Short genderId);

    Optional<PersonalInformation> getPersonalInformation(Integer personId);
    
    Optional<CompletePersonVo> getCompletePerson(Integer personId);
}
