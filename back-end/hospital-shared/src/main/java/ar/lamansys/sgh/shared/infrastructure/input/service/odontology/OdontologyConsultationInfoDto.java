package ar.lamansys.sgh.shared.infrastructure.input.service.odontology;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class OdontologyConsultationInfoDto {

	private Integer id;
	private Integer patientId;
	private Integer clinicalSpecialtyId;
	private Integer institutionId;
	private Integer patientMedicalCoverageId;
	private Integer doctorId;
	private LocalDate performedDate;
	private Boolean billable;
}
