package net.pladema.medicalconsultation.virtualConsultation.domain;

import net.pladema.medicalconsultation.virtualConsultation.infrastructure.input.rest.dto.VirtualConsultationRequestDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VirtualConsultationRequestBo {

	private Integer patientId;

	private Integer attendantHealthcareProfessionalId;

	private Integer clinicalSpecialtyId;

	private Integer careLineId;

	private Integer problemSnomedId;

	private Short priorityId;

	private Integer motiveSnomedId;

	private Integer responsibleHealthcareProfessionalId;

	private Short statusId;

	private Integer institutionId;

	public VirtualConsultationRequestBo(VirtualConsultationRequestDto virtualConsultation) {
		this.patientId = virtualConsultation.getPatientId();
		this.clinicalSpecialtyId = virtualConsultation.getClinicalSpecialtyId();
		this.careLineId = virtualConsultation.getCareLineId();
		this.priorityId = virtualConsultation.getPriority().getId();
	}

}
