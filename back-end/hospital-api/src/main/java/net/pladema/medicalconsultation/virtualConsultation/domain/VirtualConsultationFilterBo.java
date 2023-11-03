package net.pladema.medicalconsultation.virtualConsultation.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VirtualConsultationFilterBo {

	private Integer careLineId;

	private Integer clinicalSpecialtyId;

	private Integer priorityId;

	private Boolean availability;

	private Integer responsibleHealthcareProfessionalId;

	private Integer statusId;

	private Integer institutionId;

}
