package ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class PrescriptionLineV2Dto {

	private Integer prescriptionLineNumber;

	private String prescriptionLineStatus;

	private PrescriptionProblemDto prescriptionProblem;

	private GenericMedicationDto genericMedication;

	private SuggestedCommercialMedicationDto suggestedCommercialMedication;

	private List<CommercialMedicationDto> commercialMedications;

	private PrescriptionDosageDto prescriptionDosage;

	private String observation;

}
