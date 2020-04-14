package net.pladema.person.service;
import net.pladema.person.repository.entity.Person;
import net.pladema.person.repository.entity.PersonExtended;

public interface PersonService {

    Person addPerson(Person person);

    Person getPerson(Integer id);

    PersonExtended addPersonExtended(PersonExtended person);
}
