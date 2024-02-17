package net.pladema.procedure.infrastructure.input.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProcedureParameterDto {

	private Integer id;
	private Integer procedureTemplateId;
	private Integer loincId;
	private Short typeId;
	private Short orderNumber;
	private List<Integer> unitsOfMeasureIds;
	private Short inputCount;
	private List<String> textOptions;
	private Integer snomedGroupId;

	public ProcedureParameterDto(Integer id, Integer procedureTemplateId, Integer loincId, Short typeId, Short orderNumber, Short inputCount, Integer snomedGroupId) {
		this.id = id;
		this.procedureTemplateId = procedureTemplateId;
		this.loincId = loincId;
		this.typeId = typeId;
		this.orderNumber = orderNumber;
		this.inputCount = inputCount;
		this.snomedGroupId = snomedGroupId;
	}
}
