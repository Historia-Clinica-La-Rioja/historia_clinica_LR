package net.pladema.establishment.sanitaryresponsibilityarea.institutionresponsibilityarea.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.nominatim.fetchglobalcoordinatesbyaddress.domain.GlobalCoordinatesBo;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SaveInstitutionResponsibilityAreaBo {

	private Integer institutionId;

	private List<GlobalCoordinatesBo> responsibilityAreaPolygon;

}
