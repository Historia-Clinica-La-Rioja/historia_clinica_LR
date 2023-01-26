package ar.lamansys.sgh.shared.infrastructure.input.service.immunization;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class VaccineConsultationInfoDto {

	private Integer id;
	private Integer patientId;
	private Integer patientMedicalCoverageId;
	private Integer clinicalSpecialtyId;
	private Integer institutionId;
	private Integer doctorId;
	private LocalDate performedDate;
	private Boolean billable;

}
