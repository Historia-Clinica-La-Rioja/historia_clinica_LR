package net.pladema.establishment.sanitaryresponsibilityarea.getcoordinatesfromaddress.application.port.output;

import net.pladema.nominatim.fetchglobalcoordinatesbyaddress.domain.GlobalCoordinatesBo;
import net.pladema.nominatim.fetchglobalcoordinatesbyaddress.domain.NominatimAddressBo;

public interface GetCoordinatesFromAddressPort {

	GlobalCoordinatesBo run(NominatimAddressBo address);

}
