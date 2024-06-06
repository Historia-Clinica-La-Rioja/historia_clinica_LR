package ar.lamansys.sgh.clinichistory.domain.anthropometricgraphic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnthropometricGraphicEnablementBo {

	private boolean hasValidAge;
	private boolean hasValidGender;
	private boolean hasAnthropometricData;

}
