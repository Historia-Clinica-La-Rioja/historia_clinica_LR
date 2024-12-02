package ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
public class PrescriptionV2Dto {

	private String domain;

	private String prescriptionId;

	private LocalDateTime prescriptionDate;

	private LocalDateTime dueDate;

	private String link;

	private Boolean isArchived;

	private PatientPrescriptionDto patientPrescription;

	private InstitutionPrescriptionDto institutionPrescription;

	private ProfessionalPrescriptionDto professionalPrescription;

	private List<PrescriptionLineV2Dto> prescriptionLines;

	private PrescriptionSpecialtyDto prescriptionSpecialty;

}
