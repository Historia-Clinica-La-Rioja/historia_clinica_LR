package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.anthropometricgraphic.dto;

import ar.lamansys.sgh.clinichistory.domain.anthropometricgraphic.enums.EAnthropometricGraphicType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AnthropometricGraphicDto {

	private EAnthropometricGraphicOption anthropometricGraphicOption;
	private EAnthropometricGraphicType anthropometricGraphicType;

}
