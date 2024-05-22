package net.pladema.nominatim.fetchglobalcoordinatesbyaddress.application.port.input;

import net.pladema.nominatim.fetchglobalcoordinatesbyaddress.infrastructure.input.dto.GlobalCoordinatesDto;
import net.pladema.nominatim.fetchglobalcoordinatesbyaddress.infrastructure.input.dto.NominatimAddressDto;

public interface FetchGlobalCoordinatesByAddressPort {

	GlobalCoordinatesDto run(NominatimAddressDto address);

}
