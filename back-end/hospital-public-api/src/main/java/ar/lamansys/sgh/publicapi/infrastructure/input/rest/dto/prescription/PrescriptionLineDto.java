package ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto.prescription;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class PrescriptionLineDto {
	Integer prescriptionLineNumber;
	String prescriptionLineStatus;
	PrescriptionProblemDto prescriptionProblemDto;
	GenericMedicationDto genericMedicationDto;
	CommercialMedicationDto commercialMedicationDto;
	Integer unitDosis;
	Integer dayDosis;
	Double duration;
	String presentation;
	Integer presentationQuantity;
}
