package net.pladema.nominatim.fetchglobalcoordinatesbyaddress.application.port.input;

import net.pladema.nominatim.fetchglobalcoordinatesbyaddress.infrastructure.input.dto.NominatimAddressDto;
import net.pladema.nominatim.fetchglobalcoordinatesbyaddress.infrastructure.input.dto.NominatimRequestResponseDto;

public interface FetchGlobalCoordinatesByAddressPort {

	NominatimRequestResponseDto run(NominatimAddressDto address);

}
