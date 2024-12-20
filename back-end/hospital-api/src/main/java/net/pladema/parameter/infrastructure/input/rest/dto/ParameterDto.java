package net.pladema.parameter.infrastructure.input.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class ParameterDto {

	private Integer id;
	private Integer loincId;
	private String loincCode;
	private String description;
	private Short typeId;
	private Short inputCount;
	private Integer snomedGroupId;
	private List<Integer> unitsOfMeasureIds;
	private List<String> textOptions;

	public ParameterDto(Integer id, Integer loincId, String description, Short typeId, Short inputCount, Integer snomedGroupId) {
		this.id = id;
		this.loincId = loincId;
		this.description = description;
		this.typeId = typeId;
		this.inputCount = inputCount;
		this.snomedGroupId = snomedGroupId;
	}
}
