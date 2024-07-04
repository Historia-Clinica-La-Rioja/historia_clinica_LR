package net.pladema.staff.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class HealthcareProfessionalSearchBo {


	private Integer departmentId;

	private Integer institutionId;

	private Integer clinicalSpecialtyId;

	private Integer practiceId;

}
