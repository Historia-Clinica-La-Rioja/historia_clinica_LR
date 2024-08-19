package ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.dto;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Valid
public class ChangePrescriptionStateDto {
	@NotNull(message = "Este campo no puede ser nulo")
	private String prescriptionId;
	@NotNull(message = "Este campo no puede ser nulo")
	private	String pharmacyName;
	@NotNull(message = "Este campo no puede ser nulo")
	private String pharmacistName;
	@NotNull(message = "Este campo no puede ser nulo")
	private String pharmacistRegistration;
	@NotNull(message = "Este campo no puede ser nulo")
	private String changeDate;
	@NotNull(message = "Este campo no puede ser nulo")
	@Valid
	private	List<@Valid ChangePrescriptionStateMedicationDto> changePrescriptionStateLineMedicationList;

}
