package net.pladema.establishment.sanitaryresponsibilityarea.getcoordinatesfromaddress.infrastructure.output;

import lombok.RequiredArgsConstructor;
import net.pladema.establishment.sanitaryresponsibilityarea.getcoordinatesfromaddress.application.port.output.GetCoordinatesFromAddressPort;
import net.pladema.nominatim.fetchglobalcoordinatesbyaddress.application.port.input.FetchGlobalCoordinatesByAddressPort;
import net.pladema.nominatim.fetchglobalcoordinatesbyaddress.domain.GlobalCoordinatesBo;
import net.pladema.nominatim.fetchglobalcoordinatesbyaddress.domain.NominatimAddressBo;

import net.pladema.nominatim.fetchglobalcoordinatesbyaddress.infrastructure.input.dto.GlobalCoordinatesDto;
import net.pladema.nominatim.fetchglobalcoordinatesbyaddress.infrastructure.input.dto.NominatimAddressDto;
import net.pladema.nominatim.fetchglobalcoordinatesbyaddress.infrastructure.input.mapper.GlobalCoordinatesMapper;

import net.pladema.nominatim.fetchglobalcoordinatesbyaddress.infrastructure.input.mapper.NominatimAddressMapper;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GetCoordinatesFromAddressPortImpl implements GetCoordinatesFromAddressPort {

	private final GlobalCoordinatesMapper globalCoordinatesMapper;

	private final NominatimAddressMapper nominatimAddressMapper;

	private final FetchGlobalCoordinatesByAddressPort fetchGlobalCoordinatesByAddressPort;

	@Override
	public GlobalCoordinatesBo run(NominatimAddressBo address) {
		NominatimAddressDto addressDto = nominatimAddressMapper.toNominatimAddressDto(address);
		GlobalCoordinatesDto result = fetchGlobalCoordinatesByAddressPort.run(addressDto);
		return globalCoordinatesMapper.fromGlobalCoordinatesDto(result);
	}

}
