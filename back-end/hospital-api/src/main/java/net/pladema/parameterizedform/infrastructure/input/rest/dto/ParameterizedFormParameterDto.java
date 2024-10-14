package net.pladema.parameterizedform.infrastructure.input.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ParameterizedFormParameterDto {

	private Integer id;
	private Integer parameterizedFormId;
	private Integer parameterId;
	private Short orderNumber;
	private List<Integer> unitsOfMeasureIds;

	public ParameterizedFormParameterDto(Integer id, Integer parameterizedFormId, Integer parameterId, Short orderNumber) {
		this.id = id;
		this.parameterizedFormId = parameterizedFormId;
		this.parameterId = parameterId;
		this.orderNumber = orderNumber;
	}
}
