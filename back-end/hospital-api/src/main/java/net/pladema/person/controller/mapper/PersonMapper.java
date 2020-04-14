package net.pladema.person.controller.mapper;

import net.pladema.address.repository.entity.Address;
import net.pladema.person.controller.dto.APersonDto;
import net.pladema.person.controller.dto.BMPersonDto;
import net.pladema.person.repository.entity.Person;
import net.pladema.person.repository.entity.PersonExtended;
import org.mapstruct.Mapper;

@Mapper
public interface PersonMapper {

    public BMPersonDto fromPerson(Person person);

    public Person fromPersonDto(APersonDto person);

    public PersonExtended updatePersonExtended(BMPersonDto person, Integer addressId);

    public Address updatePersonAddress(APersonDto person);
}
