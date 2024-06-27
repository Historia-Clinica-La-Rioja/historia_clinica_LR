package ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.dto;

import ar.lamansys.sgh.publicapi.prescription.domain.PrescriptionValidStatesEnum;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import java.util.List;

@Getter
public class ChangePrescriptionStateMultipleMedicationDto {

	@NotNull(message = "Este campo no puede ser nulo")
	private final Integer prescriptionLine;

	@NotNull(message = "Este campo no puede ser nulo")
	private final Short prescriptionStateId;

	@NotNull(message = "Este campo no puede ser nulo")
	@NotEmpty
	@Valid
	private final List<DispensedMedicationDto> dispensedMedicationDtos;

	private final String observations;

	public ChangePrescriptionStateMultipleMedicationDto(@JsonProperty("prescriptionLine") Integer prescriptionLine,
														@JsonProperty("prescriptionStateId") Short prescriptionStateId,
														@JsonProperty("dispensedMedicationDtos") List<DispensedMedicationDto> dispensedMedicationDtos,
														@JsonProperty("observations") String observations) {
		this.prescriptionLine = prescriptionLine;
		this.prescriptionStateId = prescriptionStateId;
		this.dispensedMedicationDtos = dispensedMedicationDtos;
		this.observations = observations;
		validate();
	}

	private void validate() {
		if (isCancelledWithNoComment())
			throw new RuntimeException("El renglón " + prescriptionLine + " tiene estado CANCELADO y no tiene observaciones");
	}

	private boolean isCancelledWithNoComment() {
		return observations == null && PrescriptionValidStatesEnum.CANCELADO_DISPENSA.equals(PrescriptionValidStatesEnum.map(prescriptionStateId));
	}

}
