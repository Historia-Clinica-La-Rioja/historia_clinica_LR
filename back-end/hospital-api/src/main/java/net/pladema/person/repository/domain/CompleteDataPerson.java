package net.pladema.person.repository.domain;

import lombok.Getter;
import lombok.Setter;
import net.pladema.address.repository.entity.Address;
import net.pladema.person.repository.entity.Person;
import net.pladema.person.repository.entity.PersonExtended;

@Getter
@Setter
public class CompleteDataPerson {

    private Person person;
    private PersonExtended personExtended;
    private Address address;

    public CompleteDataPerson(Person person, PersonExtended personExtended, Address address){
        this.person = person;
        this.personExtended = personExtended;
        this.address = address;
    }
}
