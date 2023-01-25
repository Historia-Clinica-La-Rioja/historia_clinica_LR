package ar.lamansys.sgh.shared.infrastructure.input.service.nursing;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class NursingConsultationInfoDto {

	private Integer id;
	private Integer patientId;
	private Integer patientMedicalCoverageId;
	private Integer clinicalSpecialtyId;
	private Integer institutionId;
	private Integer doctorId;
	private Boolean billable;
	private LocalDate performedDate;

}
