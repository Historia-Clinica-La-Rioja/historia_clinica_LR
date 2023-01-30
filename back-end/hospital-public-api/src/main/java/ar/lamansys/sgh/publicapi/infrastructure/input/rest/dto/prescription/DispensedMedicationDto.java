package ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto.prescription;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DispensedMedicationDto {
	Integer snomedId;
	String commercialName;
	String commercialPresentation;
	Integer soldUnits;
	String brand;
	Double price;
	Double affiliatePayment;
	Double medicalCoveragePayment;
}
