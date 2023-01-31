package ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto.prescription;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangePrescriptionStateMedicationDto {
	Integer prescriptionLine;
	Short prescriptionStateId;
	DispensedMedicationDto dispensedMedicationDto;
	String observations;
}
