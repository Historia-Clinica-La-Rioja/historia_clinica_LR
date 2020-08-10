package net.pladema.address.controller.mapper;

import net.pladema.address.controller.dto.AddressDto;
import net.pladema.address.controller.service.domain.AddressBo;
import net.pladema.address.repository.entity.Address;
import net.pladema.person.repository.domain.PersonalInformation;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(uses = {CityMapper.class, ProvinceMapper.class})
public interface AddressMapper {

    @Named("fromAddressDto")
    Address fromAddressDto(AddressDto addressDto);

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
    AddressDto toAddressDtoComplete(PersonalInformation personalInformation);

    @Named("toAddress")
    AddressDto toAddressDto(Address address);

    @Named("fromAddressBo")
    AddressDto fromAddressBo(AddressBo addressBo);

    @Named("fromAddressBoList")
    @IterableMapping(qualifiedByName = "fromAddressBo")
    List<AddressDto> fromAddressBoList(List<AddressBo> addressBoList);
}
