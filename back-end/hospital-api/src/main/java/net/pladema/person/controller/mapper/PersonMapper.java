package net.pladema.person.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import net.pladema.address.controller.dto.AddressDto;
import net.pladema.address.controller.mapper.AddressMapper;
import net.pladema.dates.configuration.LocalDateMapper;
import net.pladema.patient.controller.dto.APatientDto;
import net.pladema.person.controller.dto.APersonDto;
import net.pladema.person.controller.dto.BMPersonDto;
import net.pladema.person.controller.dto.BasicDataPersonDto;
import net.pladema.person.controller.dto.PersonalInformationDto;
import net.pladema.person.repository.domain.PersonalInformation;
import net.pladema.person.repository.entity.Gender;
import net.pladema.person.repository.entity.Person;
import net.pladema.person.repository.entity.PersonExtended;


@Mapper(uses = {AddressMapper.class, IdentificationTypeMapper.class, GenderMapper.class, LocalDateMapper.class})
public interface PersonMapper {

    public BMPersonDto fromPerson(Person person);

    public Person fromPersonDto(APersonDto person);

    public PersonExtended updatePersonExtended(APersonDto person, Integer addressId);

    public PersonExtended updatePersonExtendedPatient(APatientDto patient, Integer addresId);

    public AddressDto updatePersonAddress(APersonDto person);

    public Person fromAPatientDto(APatientDto patient);

    @Mapping(target = "id", source = "person.id")
    @Mapping(target = "gender", source = "gender", qualifiedByName = "fromGender")
    @Named("toBasicDataPersonDto")
    BasicDataPersonDto basicDatafromPerson(Person person, Gender gender);


    @Mapping(target = "identificationType", source = "identificationType", qualifiedByName = "fromIdentificationType")
    @Mapping(target = "address", source="personalInformation", qualifiedByName = "toAddressComplete")
    PersonalInformationDto fromPersonalInformation(PersonalInformation personalInformation);
}
