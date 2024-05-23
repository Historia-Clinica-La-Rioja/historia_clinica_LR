package net.pladema.establishment.sanitaryresponsibilityarea.getinstitutionaddress.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetSanitaryResponsibilityAreaInstitutionAddressBo {

	private Short stateId;

	private String stateName;

	private Short departmentId;

	private String departmentName;

	private Integer cityId;

	private String cityName;

	private String streetName;

	private String houseNumber;

}
