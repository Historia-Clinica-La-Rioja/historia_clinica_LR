package net.pladema.person.controller.mapper;

import ar.lamansys.sgh.shared.infrastructure.input.service.BasicDataPersonDto;
import net.pladema.person.controller.dto.BasicPersonalDataDto;
import net.pladema.person.repository.entity.IdentificationType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import net.pladema.address.controller.dto.AddressDto;
import net.pladema.address.controller.mapper.AddressMapper;
import net.pladema.address.controller.mapper.DepartmentMapper;
import net.pladema.address.controller.mapper.ProvinceMapper;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import net.pladema.patient.controller.dto.APatientDto;
import net.pladema.person.controller.dto.APersonDto;
import net.pladema.person.controller.dto.BMPersonDto;
import net.pladema.person.controller.dto.PersonalInformationDto;
import net.pladema.person.repository.domain.CompletePersonVo;
import net.pladema.person.repository.domain.PersonalInformation;
import net.pladema.person.repository.entity.Gender;
import net.pladema.person.repository.entity.Person;
import net.pladema.person.repository.entity.PersonExtended;


@Mapper(uses = {AddressMapper.class, IdentificationTypeMapper.class, GenderMapper.class, SelfPerceivedGenderMapper.class, LocalDateMapper.class, ProvinceMapper.class, DepartmentMapper.class})
public interface PersonMapper {


    @Named("fromPerson")
    BMPersonDto fromPerson(Person person);

    @Named("fromPersonDto")
    Person fromPersonDto(APersonDto person);

    @Named("updatePersonExtended")
    PersonExtended updatePersonExtended(APersonDto person, Integer addressId);

    @Named("updatePersonExtendedPatient")
    PersonExtended toPersonExtended(APatientDto patient, Integer addressId);

    @Named("updatePersonExtendedPatient")
    PersonExtended updatePersonExtendedPatient(@MappingTarget PersonExtended personExtendedToUpdate, APatientDto patient);

    @Named("getAddressDto")
    AddressDto getAddressDto(APersonDto person);

    @Named("fromAPatientDto")
    Person fromAPatientDto(APatientDto patient);

    @Named("toBasicDataPersonDto")
    @Mapping(target = "id", source = "person.id")
    @Mapping(target = "gender", source = "gender", qualifiedByName = "fromGender")
    @Mapping(target = "identificationType", source = "identificationType.description")
	BasicDataPersonDto basicDataFromPerson(Person person, Gender gender, String selfPerceivedGender, IdentificationType identificationType);


    @Named("fromPersonalInformation")
    @Mapping(target = "identificationType", source = "identificationType", qualifiedByName = "fromIdentificationType")
    @Mapping(target = "address", source="personalInformation", qualifiedByName = "toAddressComplete")
    PersonalInformationDto fromPersonalInformation(PersonalInformation personalInformation);

    @Named("fromCompletePersonVo")
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
	@Mapping(target = "phonePrefix", source = "personExtended.phonePrefix")
	@Mapping(target = "phoneNumber", source = "personExtended.phoneNumber")
    @Mapping(target = "email", source = "personExtended.email")
    @Mapping(target = "religion", source = "personExtended.religion")
    @Mapping(target = "ethnicityId", source = "personExtended.ethnicityId")
    @Mapping(target = "educationLevelId", source = "personExtended.educationLevelId")
    @Mapping(target = "occupationId", source = "personExtended.occupationId")
    @Mapping(target = "nameSelfDetermination", source = "personExtended.nameSelfDetermination")
    @Mapping(target = "genderSelfDeterminationId", source = "personExtended.genderSelfDeterminationId")
    @Mapping(target = "otherGenderSelfDetermination", source = "personExtended.otherGenderSelfDetermination")
    @Mapping(target = "street", source = "address.street")
    @Mapping(target = "number", source = "address.number")
    @Mapping(target = "floor", source = "address.floor")
    @Mapping(target = "apartment", source = "address.apartment")
    @Mapping(target = "quarter", source = "address.quarter")
    @Mapping(target = "postcode", source = "address.postcode")
    @Mapping(target = "cityId", source = "city.id")
    @Mapping(target = "provinceId", source = "address.provinceId")
    @Mapping(target = "departmentId", source = "address.departmentId")
	@Mapping(target = "countryId", source = "address.countryId")
    BMPersonDto fromCompletePersonVo(CompletePersonVo completePersonVo);

    @Named("basicPersonalDataDto")
    BasicPersonalDataDto basicPersonalDataDto(Person person);
    
}
