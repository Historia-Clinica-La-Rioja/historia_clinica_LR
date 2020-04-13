package net.pladema.person.service;
import net.pladema.person.entity.Person;
import net.pladema.person.entity.PersonExtended;

import java.util.Optional;

public interface PersonService {

    Person addPerson(Person person);

    Person getPerson(Integer id);

    PersonExtended addPersonExtended(PersonExtended person);
}
