package net.pladema.establishment.sanitaryresponsibilityarea.saveinstitutionaddress.infrastructure.input.rest.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class SaveInstitutionAddressDto {

	private Short stateId;

	private Short departmentId;

	private Integer cityId;

	private String streetName;

	private String houseNumber;

}
