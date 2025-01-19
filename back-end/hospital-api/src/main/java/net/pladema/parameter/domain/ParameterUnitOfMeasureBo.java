package net.pladema.parameter.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ParameterUnitOfMeasureBo {

	private Integer parameterId;
	private Integer unitOfMeasureId;
	private String unitOfMeasureCode;
	private String unitOfMeasureDescription;

}
