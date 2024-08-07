package net.pladema.parameter.domain;

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
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ParameterCompleteDataBo {

	private Integer id;
	private Integer loincId;
	private String description;
	private EParameterType type;
	private Short inputCount;
	private SnomedECL ecl;
	private List<ParameterTextOptionBo> textOptions;
	private ParameterUnitOfMeasureBo unitOfMeasure;

}
