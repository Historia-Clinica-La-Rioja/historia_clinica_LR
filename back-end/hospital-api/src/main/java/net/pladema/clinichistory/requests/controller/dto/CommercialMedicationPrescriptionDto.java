package net.pladema.clinichistory.requests.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class CommercialMedicationPrescriptionDto {

	@NotNull
	private Short presentationUnitQuantity;

	@NotNull
	private Short medicationPackQuantity;

}
