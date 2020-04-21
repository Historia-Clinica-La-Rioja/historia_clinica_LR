package net.pladema.person.service;

import net.pladema.person.repository.domain.PersonalInformation;
import net.pladema.person.repository.entity.Person;
import net.pladema.person.repository.entity.PersonExtended;

import java.util.List;
import java.util.Optional;

public interface PersonService {

    Person addPerson(Person person);

    Person getPerson(Integer id);

    PersonExtended addPersonExtended(PersonExtended person);

    List<Integer> getPersonByDniAndGender(Short identificationTypeId, String identificationNumber, Short genderId);

    Optional<PersonalInformation> getPersonalInformation(Integer personId);
}
