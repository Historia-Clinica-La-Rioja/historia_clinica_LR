package net.pladema.staff.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.pladema.staff.service.domain.ELicenseNumberTypeBo;

@Getter
@Setter
@AllArgsConstructor
public class ProfessionalLicenseNumberBo {

	private Integer id;

	private String licenseNumber;

	private ELicenseNumberTypeBo type;

	private Integer professionalProfessionId;

	private Integer healthcareProfessionalSpecialtyId;
}
