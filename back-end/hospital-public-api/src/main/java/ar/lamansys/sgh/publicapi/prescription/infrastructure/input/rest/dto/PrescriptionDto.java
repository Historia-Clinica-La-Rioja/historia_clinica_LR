package ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
public class PrescriptionDto {
	String domain;
	String prescriptionId;
	LocalDateTime prescriptionDate;
	LocalDateTime dueDate;
	String link;
	Boolean isArchived;
	PatientPrescriptionDto patientPrescriptionDto;
	InstitutionPrescriptionDto institutionPrescriptionDto;
	ProfessionalPrescriptionDto professionalPrescriptionDto;
	List<PrescriptionLineDto> prescriptionsLineDto;
}
