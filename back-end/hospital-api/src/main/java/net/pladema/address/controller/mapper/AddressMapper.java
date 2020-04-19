package net.pladema.address.controller.mapper;

import net.pladema.address.controller.dto.AddressDto;
import net.pladema.address.repository.entity.Address;
import net.pladema.address.repository.entity.City;
import net.pladema.address.repository.entity.Province;
import net.pladema.patient.controller.dto.APatientDto;
import net.pladema.patient.controller.dto.BMPatientDto;
import net.pladema.patient.controller.dto.PatientSearchDto;
import net.pladema.patient.service.domain.PatientSearch;
import net.pladema.person.controller.mapper.PersonMapper;
import net.pladema.person.repository.domain.PersonalInformation;
import net.pladema.person.repository.entity.Person;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(uses = {CityMapper.class, ProvinceMapper.class})
public interface AddressMapper {

    @Named("fromAddressDto")
    public Address fromAddressDto(AddressDto addressDto);

    @Named("toAddressComplete")
    @Mapping(target = "id", source = "address.id")
    @Mapping(target = "street", source = "address.street")
    @Mapping(target = "number", source = "address.number")
    @Mapping(target = "floor", source = "address.floor")
    @Mapping(target = "apartment", source = "address.apartment")
    @Mapping(target = "quarter", source = "address.quarter")
    @Mapping(target = "postcode", source = "address.postcode")
    @Mapping(target = "city", source = "city", qualifiedByName = "fromCity")
    @Mapping(target = "province", source = "province", qualifiedByName = "fromProvince")
    public AddressDto toAddressDtoComplete(PersonalInformation personalInformation);

    @Named("toAddress")
    public AddressDto toAddressDto(Address address);
}
