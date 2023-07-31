package ar.lamansys.sgh.clinichistory.domain.ips;


import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

import ar.lamansys.sgh.clinichistory.domain.hce.summary.HealthcareProfessionalBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.EProfessionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DocumentHealthcareProfessionalBo {

	@Nullable
	private Integer id;

	@NotNull
	private HealthcareProfessionalBo healthcareProfessional;

	@NotNull
	private EProfessionType type;

	@Nullable
	private String comments;

	@Nullable
	private Integer professionalLicenseNumberId;

	public DocumentHealthcareProfessionalBo(Integer id, Integer healthcareProfessionalId, EProfessionType type, String comments, Integer professionalLicenseNumberId){
		this.id = id;
		this.healthcareProfessional = new HealthcareProfessionalBo(healthcareProfessionalId);
		this.type = type;
		this.comments = comments;
		this.professionalLicenseNumberId = professionalLicenseNumberId;
	}

}
