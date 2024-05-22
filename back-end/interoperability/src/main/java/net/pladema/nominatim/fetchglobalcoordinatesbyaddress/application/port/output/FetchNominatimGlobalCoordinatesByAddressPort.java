package net.pladema.nominatim.fetchglobalcoordinatesbyaddress.application.port.output;

import net.pladema.nominatim.fetchglobalcoordinatesbyaddress.domain.GlobalCoordinatesBo;
import net.pladema.nominatim.fetchglobalcoordinatesbyaddress.domain.NominatimAddressBo;

public interface FetchNominatimGlobalCoordinatesByAddressPort {

	GlobalCoordinatesBo run(NominatimAddressBo address);

}
