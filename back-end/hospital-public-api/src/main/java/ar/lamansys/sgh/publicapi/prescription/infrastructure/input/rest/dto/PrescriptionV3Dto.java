package ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
public class PrescriptionV3Dto {

	private String domain;

	private String prescriptionId;

	private LocalDateTime prescriptionDate;

	private LocalDateTime dueDate;

	private String link;

	private Boolean isArchived;

	private PatientPrescriptionDto patientPrescriptionDto;

	private InstitutionPrescriptionDto institutionPrescriptionDto;

	private ProfessionalPrescriptionDto professionalPrescriptionDto;

	private List<PrescriptionLineV3Dto> prescriptionsLineDto;

	private PrescriptionSpecialtyDto prescriptionSpecialtyDto;

}
