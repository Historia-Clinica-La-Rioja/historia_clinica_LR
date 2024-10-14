package ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PrescriptionLineDto {
	Integer prescriptionLineNumber;
	String prescriptionLineStatus;
	PrescriptionProblemDto prescriptionProblemDto;
	GenericMedicationDto genericMedicationDto;
	SuggestedCommercialMedicationDto suggestedCommercialMedicationDto;
	CommercialMedicationDto commercialMedicationDto;
	Double unitDosis;
	Double dayDosis;
	Double duration;
	String presentation;
	Short presentationQuantity;
	Double quantity;
	Short presentationPackageQuantity;
}
