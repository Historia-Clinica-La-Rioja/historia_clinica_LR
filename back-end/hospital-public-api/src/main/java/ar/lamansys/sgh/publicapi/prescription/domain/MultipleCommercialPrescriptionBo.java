package ar.lamansys.sgh.publicapi.prescription.domain;

import ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.dto.PrescriptionSpecialtyDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MultipleCommercialPrescriptionBo {

	private String domain;

	private String prescriptionId;

	private LocalDateTime prescriptionDate;

	private LocalDateTime dueDate;

	private String link;

	private Boolean isArchived;

	private PatientPrescriptionBo patientPrescription;

	private InstitutionPrescriptionBo institutionPrescription;

	private ProfessionalPrescriptionBo professionalPrescription;

	private List<MultipleCommercialPrescriptionLineBo> prescriptionLines;

	private PrescriptionSpecialtyBo prescriptionSpecialty;


}
