package ar.lamansys.virtualConsultation.domain;

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

}
