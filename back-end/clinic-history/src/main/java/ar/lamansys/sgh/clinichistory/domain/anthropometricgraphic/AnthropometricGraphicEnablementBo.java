package ar.lamansys.sgh.clinichistory.domain.anthropometricgraphic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnthropometricGraphicEnablementBo {

	@NotNull
	private Boolean hasValidAge;

	@Nullable
	private Boolean hasValidGender;

	@Nullable
	private Boolean hasAnthropometricData;

	public AnthropometricGraphicEnablementBo(boolean hasValidAge) {
		this.hasValidAge = hasValidAge;
	}
}
