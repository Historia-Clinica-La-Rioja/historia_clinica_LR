package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.anthropometricgraphic.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class AnthropometricGraphicEnablementDto {

	@NotNull
	private Boolean hasValidAge;

	@Nullable
	private Boolean hasValidGender;

	@Nullable
	private Boolean hasAnthropometricData;

}
