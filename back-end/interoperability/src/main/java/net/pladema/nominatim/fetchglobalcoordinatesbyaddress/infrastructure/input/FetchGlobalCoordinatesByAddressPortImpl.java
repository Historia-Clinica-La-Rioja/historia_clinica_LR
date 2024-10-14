package net.pladema.nominatim.fetchglobalcoordinatesbyaddress.infrastructure.input;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.nominatim.fetchglobalcoordinatesbyaddress.application.FetchGlobalCoordinatesByAddress;
import net.pladema.nominatim.fetchglobalcoordinatesbyaddress.application.port.input.FetchGlobalCoordinatesByAddressPort;

import net.pladema.nominatim.fetchglobalcoordinatesbyaddress.domain.NominatimAddressBo;
import net.pladema.nominatim.fetchglobalcoordinatesbyaddress.domain.NominatimRequestResponseBo;

import net.pladema.nominatim.fetchglobalcoordinatesbyaddress.infrastructure.input.dto.NominatimAddressDto;

import net.pladema.nominatim.fetchglobalcoordinatesbyaddress.infrastructure.input.dto.NominatimRequestResponseDto;
import net.pladema.nominatim.fetchglobalcoordinatesbyaddress.infrastructure.input.mapper.GlobalCoordinatesMapper;

import net.pladema.nominatim.fetchglobalcoordinatesbyaddress.infrastructure.input.mapper.NominatimAddressMapper;

import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class FetchGlobalCoordinatesByAddressPortImpl implements FetchGlobalCoordinatesByAddressPort {

	private final FetchGlobalCoordinatesByAddress fetchGlobalCoordinatesByAddress;

	private final GlobalCoordinatesMapper globalCoordinatesMapper;

	private final NominatimAddressMapper nominatimAddressMapper;

	@Override
	public NominatimRequestResponseDto run(NominatimAddressDto address) {
		log.debug("Input parameter -> address {}", address);
		NominatimAddressBo addressBo = nominatimAddressMapper.fromNominatimAddressDto(address);
		NominatimRequestResponseBo resultCoordinates = fetchGlobalCoordinatesByAddress.run(addressBo);
		NominatimRequestResponseDto result = globalCoordinatesMapper.toNominatimRequestResponseDto(resultCoordinates);
		log.debug("Output -> {}", result);
		return result;
	}

}
