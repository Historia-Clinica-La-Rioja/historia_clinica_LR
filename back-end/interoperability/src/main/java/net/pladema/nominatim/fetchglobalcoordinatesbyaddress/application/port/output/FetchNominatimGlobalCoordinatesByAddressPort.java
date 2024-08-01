package net.pladema.nominatim.fetchglobalcoordinatesbyaddress.application.port.output;

import net.pladema.nominatim.fetchglobalcoordinatesbyaddress.domain.GlobalCoordinatesBo;
import net.pladema.nominatim.fetchglobalcoordinatesbyaddress.domain.NominatimAddressBo;
import net.pladema.nominatim.fetchglobalcoordinatesbyaddress.domain.NominatimRequestResponseBo;

public interface FetchNominatimGlobalCoordinatesByAddressPort {

	NominatimRequestResponseBo run(NominatimAddressBo address);

}
