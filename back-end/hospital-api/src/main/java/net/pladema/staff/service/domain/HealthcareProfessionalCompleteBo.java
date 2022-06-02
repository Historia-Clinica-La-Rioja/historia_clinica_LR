package net.pladema.staff.service.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class HealthcareProfessionalCompleteBo {

    private Integer id;

    private Integer personId;

    private String licenseNumber;

	public HealthcareProfessionalCompleteBo(Integer id, Integer personId, String licenseNumber) {
		this.id = id;
		this.personId = personId;
		this.licenseNumber = licenseNumber;
	}
}
