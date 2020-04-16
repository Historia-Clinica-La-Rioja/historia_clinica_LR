package net.pladema.person.controller.mapper;

import net.pladema.address.repository.entity.Address;
import net.pladema.person.controller.dto.APersonDto;
import net.pladema.person.controller.dto.BMPersonDto;
import net.pladema.person.repository.domain.CompleteDataPerson;
import net.pladema.person.repository.entity.Person;
import net.pladema.person.repository.entity.PersonExtended;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface PersonMapper {

    public BMPersonDto fromPerson(Person person);

    public Person fromPersonDto(APersonDto person);

    public PersonExtended updatePersonExtended(APersonDto person, Integer addressId);

    public Address updatePersonAddress(APersonDto person);

    @Mapping(target = "id", source = "person.id")
    @Mapping(target = "firstName", source = "person.firstName")
    @Mapping(target = "middleNames", source = "person.middleNames")
    @Mapping(target = "lastName", source = "person.lastName")
    @Mapping(target = "otherLastNames", source = "person.otherLastNames")
    @Mapping(target = "identificationTypeId", source = "person.identificationTypeId")
    @Mapping(target = "identificationNumber", source = "person.identificationNumber")
    @Mapping(target = "genderId", source = "person.genderId")
    @Mapping(target = "birthDate", source = "person.birthDate")

    @Mapping(target = "cuil", source = "personExtended.cuil")
    @Mapping(target = "mothersLastName", source = "personExtended.mothersLastName")
    @Mapping(target = "phoneNumber", source = "personExtended.phoneNumber")
    @Mapping(target = "email", source = "personExtended.email")
    @Mapping(target = "ethnic", source = "personExtended.ethnic")
    @Mapping(target = "religion", source = "personExtended.religion")
    @Mapping(target = "nameSelfDetermination", source = "personExtended.nameSelfDetermination")
    @Mapping(target = "genderSelfDeterminationId", source = "personExtended.genderSelfDeterminationId")

    @Mapping(target = "street", source = "address.street")
    @Mapping(target = "number", source = "address.number")
    @Mapping(target = "floor", source = "address.floor")
    @Mapping(target = "apartment", source = "address.apartment")
    @Mapping(target = "quarter", source = "address.quarter")
    @Mapping(target = "cityId", source = "address.cityId")
    @Mapping(target = "postcode", source = "address.postcode")
    public BMPersonDto fromCompleteDataPerson(CompleteDataPerson completeDataPerson);

    public List<BMPersonDto> fromfromCompleteDataPersonList(List<CompleteDataPerson> completeDataPersonList);
}
