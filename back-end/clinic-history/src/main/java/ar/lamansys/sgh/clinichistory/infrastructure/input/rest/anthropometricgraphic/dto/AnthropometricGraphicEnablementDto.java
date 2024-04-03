package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.anthropometricgraphic.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class AnthropometricGraphicEnablementDto {

	private boolean hasValidAge;
	private boolean hasValidGender;
	private boolean hasAnthropometricData;

}
