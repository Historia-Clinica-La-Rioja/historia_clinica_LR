package ar.lamansys.virtualConsultation.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VirtualConsultationResponsibleProfessionalAvailabilityBo {

	private Integer healthcareProfessionalId;

	private Integer institutionId;

	private Boolean available;

	public VirtualConsultationResponsibleProfessionalAvailabilityBo(Integer healthcareProfessionalId, Integer institutionId) {
		this.healthcareProfessionalId = healthcareProfessionalId;
		this.institutionId = institutionId;
	}

}
