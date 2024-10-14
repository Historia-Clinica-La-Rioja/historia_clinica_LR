package net.pladema.establishment.sanitaryresponsibilityarea.institutionresponsibilityarea.application.port.output;

import net.pladema.nominatim.fetchglobalcoordinatesbyaddress.domain.GlobalCoordinatesBo;

import java.util.List;

public interface GetInstitutionResponsibilityAreaPort {

	List<GlobalCoordinatesBo> run(Integer institutionId);

}
