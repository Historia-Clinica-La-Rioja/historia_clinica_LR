package net.pladema.emergencycare.service.domain;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.EDocumentType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class EmergencyCareEvolutionNoteBo {

	private Integer id;

	private Integer patientId;

	private Integer clinicalSpecialtyId;

	private Integer institutionId;

	private LocalDateTime performedDate;

	private Long documentId;

	private Integer doctorId;

	private Boolean billable;

	private Integer patientMedicalCoverageId;

	private EDocumentType documentType;

}
