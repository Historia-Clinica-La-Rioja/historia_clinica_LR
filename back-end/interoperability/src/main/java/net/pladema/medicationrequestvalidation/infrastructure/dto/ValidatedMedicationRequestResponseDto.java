package net.pladema.medicationrequestvalidation.infrastructure.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ValidatedMedicationRequestResponseDto {

	@JsonAlias("idReceta")
	private String prescriptionId;

}
