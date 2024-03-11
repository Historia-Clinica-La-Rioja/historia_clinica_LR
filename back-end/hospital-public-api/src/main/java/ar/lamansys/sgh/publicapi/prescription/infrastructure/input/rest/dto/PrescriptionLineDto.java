package ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
public class PrescriptionLineDto {
	Integer prescriptionLineNumber;
	String prescriptionLineStatus;
	PrescriptionProblemDto prescriptionProblemDto;
	GenericMedicationDto genericMedicationDto;
	CommercialMedicationDto commercialMedicationDto;
	Double unitDosis;
	Double dayDosis;
	Double duration;
	String presentation;
	Integer presentationQuantity;
	Double quantity;
}
