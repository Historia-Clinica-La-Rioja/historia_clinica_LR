package net.pladema.parameter.infrastructure.input.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.parameter.domain.enums.EParameterType;
import net.pladema.snowstorm.services.domain.semantics.SnomedECL;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ParameterCompleteDataDto {

	private Integer id;
	private Integer loincId;
	private String description;
	private EParameterType type;
	private Short inputCount;
	private SnomedECL ecl;
	private List<ParameterTextOptionDto> textOptions;
	private ParameterUnitOfMeasureDto unitOfMeasure;

}
