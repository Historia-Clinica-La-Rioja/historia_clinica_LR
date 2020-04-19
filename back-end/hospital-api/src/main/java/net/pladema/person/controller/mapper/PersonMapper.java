package net.pladema.person.controller.mapper;

import net.pladema.address.controller.dto.AddressDto;
import net.pladema.address.controller.mapper.AddressMapper;
import net.pladema.patient.controller.dto.APatientDto;
import net.pladema.person.controller.dto.APersonDto;
import net.pladema.person.controller.dto.BMPersonDto;
import net.pladema.person.controller.dto.BasicDataPersonDto;
import net.pladema.person.controller.dto.PersonalInformationDto;
import net.pladema.person.repository.domain.CompleteDataPerson;
import net.pladema.person.repository.domain.PersonalInformation;
import net.pladema.person.repository.entity.Gender;
import net.pladema.person.repository.entity.Person;
import net.pladema.person.repository.entity.PersonExtended;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;


@Mapper(uses = {AddressMapper.class, HealthInsuranceMapper.class, IdentificationTypeMapper.class, GenderMapper.class})
public interface PersonMapper {

    public BMPersonDto fromPerson(Person person);

    public Person fromPersonDto(APersonDto person);

    public PersonExtended updatePersonExtended(APersonDto person, Integer addressId);

    public PersonExtended updatePersonExtendedPatient(APatientDto patient, Integer addresId);

    public AddressDto updatePersonAddress(APersonDto person);

    public Person fromAPatientDto(APatientDto patient);

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


    @Mapping(target = "id", source = "person.id")
    @Mapping(target = "gender", source = "gender", qualifiedByName = "fromGender")
    BasicDataPersonDto basicDatafromPerson(Person person, Gender gender);


    @Mapping(target = "identificationType", source = "identificationType", qualifiedByName = "fromIdentificationType")
    @Mapping(target = "healthInsurance", source = "healthInsurance", qualifiedByName = "fromHealthInsurance")
    @Mapping(target = "address", source="personalInformation", qualifiedByName = "toAddressComplete")
    PersonalInformationDto fromPersonalInformation(PersonalInformation personalInformation);
}
