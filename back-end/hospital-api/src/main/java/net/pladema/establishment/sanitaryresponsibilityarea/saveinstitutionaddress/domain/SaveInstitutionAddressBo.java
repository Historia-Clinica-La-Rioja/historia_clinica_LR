package net.pladema.establishment.sanitaryresponsibilityarea.saveinstitutionaddress.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class SaveInstitutionAddressBo {

	private Integer institutionId;

	private Short stateId;

	private Short departmentId;

	private Integer cityId;

	private String streetName;

	private String houseNumber;

}
