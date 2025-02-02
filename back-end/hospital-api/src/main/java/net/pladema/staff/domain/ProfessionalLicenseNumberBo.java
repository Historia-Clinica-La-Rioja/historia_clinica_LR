package net.pladema.staff.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ar.lamansys.sgh.shared.domain.ELicenseNumberTypeBo;

import java.util.Objects;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class ProfessionalLicenseNumberBo {

	private Integer id;

	private String licenseNumber;

	private ELicenseNumberTypeBo type;

	private Integer professionalProfessionId;

	private Integer healthcareProfessionalSpecialtyId;

	public boolean equals(ProfessionalLicenseNumberBo bo){
		return Objects.equals(this.id, bo.getId());
	}

	public boolean hasSameProfessionAndType(ProfessionalLicenseNumberBo bo){
		return this.type.equals(bo.getType())
				&& Objects.equals(this.professionalProfessionId,bo.getProfessionalProfessionId())
				&& Objects.equals(this.healthcareProfessionalSpecialtyId, bo.getHealthcareProfessionalSpecialtyId());
	}

	public String getCompleteTypeLicenseNumber() {
		return String.join("-", type.getAcronym(), licenseNumber);
	}
}
