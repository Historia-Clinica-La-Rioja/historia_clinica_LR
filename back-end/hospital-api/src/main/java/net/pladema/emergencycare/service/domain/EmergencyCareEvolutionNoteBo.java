package net.pladema.emergencycare.service.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class EmergencyCareEvolutionNoteBo {

	private Integer id;

	private Integer patientId;

	private Integer clinicalSpecialtyId;

	private Integer institutionId;

	private LocalDate startDate;

	private Long documentId;

	private Integer doctorId;

	private Boolean billable;

	private Integer patientMedicalCoverageId;

}
