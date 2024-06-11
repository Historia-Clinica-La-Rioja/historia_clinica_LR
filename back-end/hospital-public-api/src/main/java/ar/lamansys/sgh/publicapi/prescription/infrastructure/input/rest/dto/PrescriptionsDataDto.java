package ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@AllArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class PrescriptionsDataDto {

	private String domain;
	private String prescriptionId;
	private LocalDateTime prescriptionDate;
	private LocalDateTime dueDate;
	private String link;
	private ProfessionalPrescriptionDto professionalData;
	private PrescriptionSpecialtyDto prescriptionSpecialty;

}
