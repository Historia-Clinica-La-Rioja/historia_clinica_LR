package net.pladema.establishment.sanitaryresponsibilityarea.getcoordinatesfromaddress.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.establishment.sanitaryresponsibilityarea.getcoordinatesfromaddress.application.port.output.GetCoordinatesFromAddressPort;
import net.pladema.nominatim.fetchglobalcoordinatesbyaddress.domain.GlobalCoordinatesBo;

import net.pladema.nominatim.fetchglobalcoordinatesbyaddress.domain.NominatimAddressBo;

import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetCoordinatesFromAddress {

	private final GetCoordinatesFromAddressPort getCoordinatesFromAddressPort;

	public GlobalCoordinatesBo run(NominatimAddressBo address) {
		log.debug("Input parameter -> address {}", address);
		GlobalCoordinatesBo result = getCoordinatesFromAddressPort.run(address);
		log.debug("Output -> {}", result);
		return result;
	}

}
