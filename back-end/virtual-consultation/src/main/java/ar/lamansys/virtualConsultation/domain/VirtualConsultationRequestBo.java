package ar.lamansys.virtualConsultation.domain;

import ar.lamansys.virtualConsultation.infrastructure.input.rest.dto.VirtualConsultationRequestDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VirtualConsultationRequestBo {

	private Integer patientId;

	private Integer attendantHealthcareProfessionalId;

	private Integer clinicalSpecialtyId;

	private Integer careLineId;

	private Integer problemId;

	private Short priorityId;

	private Integer motiveId;

	private Integer responsibleHealthcareProfessionalId;

	private Short statusId;

	private Integer institutionId;

	public VirtualConsultationRequestBo(VirtualConsultationRequestDto virtualConsultation) {
		this.patientId = virtualConsultation.getPatientId();
		this.clinicalSpecialtyId = virtualConsultation.getClinicalSpecialtyId();
		this.careLineId = virtualConsultation.getCareLineId();
		this.problemId = virtualConsultation.getProblemId();
		this.priorityId = virtualConsultation.getPriority().getId();
		this.motiveId = virtualConsultation.getMotiveId();
	}

}
