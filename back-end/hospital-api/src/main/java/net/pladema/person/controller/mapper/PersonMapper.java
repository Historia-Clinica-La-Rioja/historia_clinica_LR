package net.pladema.person.controller.mapper;

import net.pladema.address.controller.dto.AddressDto;
import net.pladema.address.controller.mapper.AddressMapper;
import net.pladema.patient.controller.dto.APatientDto;
import net.pladema.person.controller.dto.APersonDto;
import net.pladema.person.controller.dto.BMPersonDto;
import net.pladema.person.controller.dto.BasicDataPersonDto;
import net.pladema.person.controller.dto.PersonalInformationDto;
import net.pladema.person.repository.domain.PersonalInformation;
import net.pladema.person.repository.entity.Gender;
import net.pladema.person.repository.entity.Person;
import net.pladema.person.repository.entity.PersonExtended;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;



@Mapper(uses = {AddressMapper.class, HealthInsuranceMapper.class, IdentificationTypeMapper.class, GenderMapper.class},
        imports = {java.time.LocalDateTime.class,java.time.LocalTime.class})
public interface PersonMapper {

    @Mapping( target="birthDate",  expression = "java(LocalDateTime.of(person.getBirthDate(), LocalTime.now()))")
    public BMPersonDto fromPerson(Person person);

    @Mapping( target="birthDate",  expression = "java(person.getBirthDate().toLocalDate())")
    public Person fromPersonDto(APersonDto person);

    public PersonExtended updatePersonExtended(APersonDto person, Integer addressId);

    public PersonExtended updatePersonExtendedPatient(APatientDto patient, Integer addresId);

    public AddressDto updatePersonAddress(APersonDto person);

    @Mapping( target="birthDate",  expression = "java(patient.getBirthDate().toLocalDate())")
    public Person fromAPatientDto(APatientDto patient);

    @Mapping(target = "id", source = "person.id")
    @Mapping(target = "gender", source = "gender", qualifiedByName = "fromGender")
    BasicDataPersonDto basicDatafromPerson(Person person, Gender gender);


    @Mapping(target = "identificationType", source = "identificationType", qualifiedByName = "fromIdentificationType")
    @Mapping(target = "healthInsurance", source = "healthInsurance", qualifiedByName = "fromHealthInsurance")
    @Mapping(target = "address", source="personalInformation", qualifiedByName = "toAddressComplete")
    PersonalInformationDto fromPersonalInformation(PersonalInformation personalInformation);
}
