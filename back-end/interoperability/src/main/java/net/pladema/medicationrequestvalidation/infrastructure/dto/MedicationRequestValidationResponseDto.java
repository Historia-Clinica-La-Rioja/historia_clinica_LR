package net.pladema.medicationrequestvalidation.infrastructure.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicationRequestValidationResponseDto {

	@JsonAlias("recetas")
	private List<ValidatedMedicationRequestResponseDto> prescriptions;

}
