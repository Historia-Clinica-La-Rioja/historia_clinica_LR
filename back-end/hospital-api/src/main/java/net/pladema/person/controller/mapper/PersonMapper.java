package net.pladema.person.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import net.pladema.address.controller.dto.AddressDto;
import net.pladema.address.controller.mapper.AddressMapper;
import net.pladema.address.controller.mapper.DepartmentMapper;
import net.pladema.address.controller.mapper.ProvinceMapper;
import net.pladema.dates.configuration.LocalDateMapper;
import net.pladema.patient.controller.dto.APatientDto;
import net.pladema.person.controller.dto.APersonDto;
import net.pladema.person.controller.dto.BMPersonDto;
import net.pladema.person.controller.dto.BasicDataPersonDto;
import net.pladema.person.controller.dto.PersonalInformationDto;
import net.pladema.person.repository.domain.CompletePersonVo;
import net.pladema.person.repository.domain.PersonalInformation;
import net.pladema.person.repository.entity.Gender;
import net.pladema.person.repository.entity.Person;
import net.pladema.person.repository.entity.PersonExtended;


@Mapper(uses = {AddressMapper.class, IdentificationTypeMapper.class, GenderMapper.class, LocalDateMapper.class, ProvinceMapper.class, DepartmentMapper.class})
public interface PersonMapper {

    public BMPersonDto fromPerson(Person person);

    public Person fromPersonDto(APersonDto person);

    public PersonExtended updatePersonExtended(APersonDto person, Integer addressId);

    public PersonExtended updatePersonExtendedPatient(APatientDto patient, Integer addresId);
    
    public PersonExtended updatePersonExtendedPatient(@MappingTarget PersonExtended personExtendedToUpdate, APatientDto patient);

    public AddressDto updatePersonAddress(APersonDto person);

    public Person fromAPatientDto(APatientDto patient);

    @Mapping(target = "id", source = "person.id")
    @Mapping(target = "gender", source = "gender", qualifiedByName = "fromGender")
    @Named("toBasicDataPersonDto")
    BasicDataPersonDto basicDatafromPerson(Person person, Gender gender);


    @Mapping(target = "identificationType", source = "identificationType", qualifiedByName = "fromIdentificationType")
    @Mapping(target = "address", source="personalInformation", qualifiedByName = "toAddressComplete")
    PersonalInformationDto fromPersonalInformation(PersonalInformation personalInformation);
    
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
    @Mapping(target = "postcode", source = "address.postcode")
    @Mapping(target = "cityId", source = "city.id")
    @Mapping(target = "province", source = "province", qualifiedByName = "fromProvince")
    @Mapping(target = "department", source = "department", qualifiedByName = "fromDepartment")
    BMPersonDto fromCompletePersonVo(CompletePersonVo completePersonVo);
    //TODO: mejorar jerarquia DTO's mediante composicion
    
}
