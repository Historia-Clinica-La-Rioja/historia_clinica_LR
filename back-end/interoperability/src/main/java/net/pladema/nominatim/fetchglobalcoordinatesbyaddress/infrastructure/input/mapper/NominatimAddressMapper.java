package net.pladema.nominatim.fetchglobalcoordinatesbyaddress.infrastructure.input.mapper;

import net.pladema.nominatim.fetchglobalcoordinatesbyaddress.domain.NominatimAddressBo;

import net.pladema.nominatim.fetchglobalcoordinatesbyaddress.infrastructure.input.dto.NominatimAddressDto;

import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper
public interface NominatimAddressMapper {

	@Named("fromGlobalCoordinatesDto")
	NominatimAddressBo fromNominatimAddressDto(NominatimAddressDto nominatimAddressDto);

	@Named("toNominatimAddressDto")
	NominatimAddressDto toNominatimAddressDto(NominatimAddressBo nominatimAddressBo);

}
