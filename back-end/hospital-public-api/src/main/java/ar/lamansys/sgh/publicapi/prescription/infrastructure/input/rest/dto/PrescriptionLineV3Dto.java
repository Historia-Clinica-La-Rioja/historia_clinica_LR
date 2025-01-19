package ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class PrescriptionLineV3Dto {

	private Integer prescriptionLineNumber;

	private String prescriptionLineStatus;

	private PrescriptionProblemDto prescriptionProblemDto;

	private GenericMedicationDto genericMedicationDto;

	private SuggestedCommercialMedicationDto suggestedCommercialMedicationDto;

	private List<CommercialMedicationDto> commercialMedicationsDto;

	private PrescriptionDosageDto prescriptionDosageDto;

	private String observation;

}
