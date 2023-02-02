package ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto.prescription;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DispensedMedicationDto {
	@NotNull(message = "Este campo no puede ser nulo")
	private Integer snomedId;
	@NotNull(message = "Este campo no puede ser nulo")
	private String commercialName;
	@NotNull(message = "Este campo no puede ser nulo")
	private String commercialPresentation;
	@NotNull(message = "Este campo no puede ser nulo")
	private Integer soldUnits;
	@NotNull(message = "Este campo no puede ser nulo")
	private String brand;
	@NotNull(message = "Este campo no puede ser nulo")
	private Double price;
	@NotNull(message = "Este campo no puede ser nulo")
	private Double affiliatePayment;
	@NotNull(message = "Este campo no puede ser nulo")
	private Double medicalCoveragePayment;
	private String pharmacyName;
	private String pharmacistName;
	private String observations;
}
