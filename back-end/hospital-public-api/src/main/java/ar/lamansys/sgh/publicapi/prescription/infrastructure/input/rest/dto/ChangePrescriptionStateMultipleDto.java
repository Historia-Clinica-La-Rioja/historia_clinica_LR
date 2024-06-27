package ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import java.util.List;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ChangePrescriptionStateMultipleDto {

	@NotNull(message = "Este campo no puede ser nulo")
	private String prescriptionId;

	@NotNull(message = "Este campo no puede ser nulo")
	@NotBlank(message = "Este campo no puede ser vacío")
	private	String pharmacyName;

	@NotNull(message = "Este campo no puede ser nulo")
	@NotBlank(message = "Este campo no puede ser vacío")
	private String pharmacistName;

	@NotNull(message = "Este campo no puede ser nulo")
	private String pharmacistRegistration;

	@NotNull(message = "Este campo no puede ser nulo")
	private String changeDate;

	@NotNull(message = "Este campo no puede ser nulo")
	@NotEmpty
	@Valid
	private List<ChangePrescriptionStateMultipleMedicationDto> changePrescriptionStateLineMedicationList;

}
