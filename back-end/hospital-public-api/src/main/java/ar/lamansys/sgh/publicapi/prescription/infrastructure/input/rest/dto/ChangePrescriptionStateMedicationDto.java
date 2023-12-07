package ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
public class ChangePrescriptionStateMedicationDto {
	@NotNull(message = "Este campo no puede ser nulo")
	private Integer prescriptionLine;
	@NotNull(message = "Este campo no puede ser nulo")
	private Short prescriptionStateId;
	@NotNull(message = "Este campo no puede ser nulo")
	@Valid
	private DispensedMedicationDto dispensedMedicationDto;
	private String observations;

	public ChangePrescriptionStateMedicationDto(Integer prescriptionLine, Short prescriptionStateId, @Valid DispensedMedicationDto dispensedMedicationDto, String observations) {
		this.prescriptionLine = prescriptionLine;
		this.prescriptionStateId = prescriptionStateId;
		this.dispensedMedicationDto = dispensedMedicationDto;
		this.observations = observations;
	}
}
