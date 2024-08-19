package net.pladema.procedure.domain.fullsummary;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import lombok.Builder;
import lombok.Getter;
import net.pladema.procedure.domain.ProcedureParameterTypeBo;

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
