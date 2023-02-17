package ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto.prescription;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Builder
@Getter
public class PrescriptionDto {
	String domain;
	String prescriptionId;
	LocalDateTime prescriptionDate;
	LocalDateTime dueDate;
	PatientPrescriptionDto patientPrescriptionDto;
	InstitutionPrescriptionDto institutionPrescriptionDto;
	ProfessionalPrescriptionDto professionalPrescriptionDto;
	List<PrescriptionLineDto> prescriptionsLineDto;
}
