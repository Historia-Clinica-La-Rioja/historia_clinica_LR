package net.pladema.person.controller.mapper;

import net.pladema.person.controller.dto.APersonDto;
import net.pladema.person.entity.Person;
import net.pladema.person.entity.PersonExtended;
import org.mapstruct.Mapper;

@Mapper
public interface PersonMapper {

    public APersonDto fromPerson(Person person);

    public Person fromPersonDto(APersonDto person);

    public PersonExtended updatePersonExtended(APersonDto person);
}
