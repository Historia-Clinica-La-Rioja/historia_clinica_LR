package net.pladema.procedure.domain.fullsummary;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ProcedureParameterFullSummaryVo {
	private Integer id;
	private Integer procedureTemplateId;
	private ProcedureParameterLoincCodeFullSummaryVo loincCode;
	private Short typeId;
	private String typeDescription;
	private Short orderNumber;
	private List<ProcedureParameterUnitOfMeasureFullSummaryVo> unitsOfMeasure;
	private Short inputCount;
	private List<ProcedureParameterTextOptionFullSummaryVo> textOptions;
	private Integer snomedGroupId;
	private String snomedGroupDescription;

	public String getLoincDescription() {
		return loincCode.getDescription();
	}

}
