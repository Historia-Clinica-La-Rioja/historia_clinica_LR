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
	private Integer snomedId;
	private String commercialName;
	private String commercialPresentation;
	private Integer soldUnits;
	private String brand;
	private Double price;
	private Double affiliatePayment;
	private Double medicalCoveragePayment;
	private String pharmacyName;
	private String pharmacistName;
	private String observations;
}
