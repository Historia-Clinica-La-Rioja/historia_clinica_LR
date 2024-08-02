package net.pladema.sanitaryresponsibilityarea.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.nominatim.fetchglobalcoordinatesbyaddress.domain.GlobalCoordinatesBo;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class GetPatientCoordinatesByAddedInstitutionFilterBo {

	private Integer institutionId;

	private GlobalCoordinatesBo mapUpperCorner;

	private GlobalCoordinatesBo mapLowerCorner;

}
