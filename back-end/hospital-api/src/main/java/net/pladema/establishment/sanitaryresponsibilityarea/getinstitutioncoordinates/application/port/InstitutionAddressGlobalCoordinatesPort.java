package net.pladema.establishment.sanitaryresponsibilityarea.getinstitutioncoordinates.application.port;

import net.pladema.nominatim.fetchglobalcoordinatesbyaddress.domain.GlobalCoordinatesBo;

public interface InstitutionAddressGlobalCoordinatesPort {

	GlobalCoordinatesBo fetchInstitutionGlobalCoordinates(Integer institutionId);

}
