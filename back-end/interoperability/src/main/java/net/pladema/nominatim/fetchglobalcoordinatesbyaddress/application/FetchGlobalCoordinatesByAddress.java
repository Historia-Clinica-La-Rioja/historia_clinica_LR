package net.pladema.nominatim.fetchglobalcoordinatesbyaddress.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.nominatim.fetchglobalcoordinatesbyaddress.application.port.output.FetchNominatimGlobalCoordinatesByAddressPort;
import net.pladema.nominatim.fetchglobalcoordinatesbyaddress.domain.GlobalCoordinatesBo;

import net.pladema.nominatim.fetchglobalcoordinatesbyaddress.domain.NominatimAddressBo;

import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class FetchGlobalCoordinatesByAddress {

	private final FetchNominatimGlobalCoordinatesByAddressPort fetchNominatimGlobalCoordinatesByAddressPort;

	public GlobalCoordinatesBo run(NominatimAddressBo address) {
		log.debug("Input parameter -> address {}", address);
		GlobalCoordinatesBo result = fetchNominatimGlobalCoordinatesByAddressPort.run(address);
		log.debug("Output -> {}", result);
		return result;
	}

}
