package net.pladema.procedure.infrastructure.input.rest.dto.fullsummary;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import net.pladema.procedure.domain.fullsummary.ProcedureParameterLoincCodeFullSummaryVo;

@Builder
@Getter
public class ProcedureParameterFullSummaryDto {
	private Integer id;
	private ProcedureParameterLoincCodeFullSummaryVo loincCode;
	private Short typeId;
	private String typeDescription;
	private Short orderNumber;
	private List<ProcedureParameterUnitOfMeasureFullSummaryDto> unitsOfMeasure;
	private Short inputCount;
	private List<ProcedureParameterTextOptionFullSummaryDto> textOptions;
	private Integer snomedGroupId;
	private String snomedGroupDescription;
}
