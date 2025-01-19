package net.pladema.establishment.sanitaryresponsibilityarea.saveinstitutionglobalcoordinates.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class SaveInstitutionGlobalCoordinatesBo {

	private Integer institutionId;

	private Double latitude;

	private Double longitude;

}
