package net.pladema.procedure.domain.fullsummary;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProcedureParameterUnitOfMeasureFullSummaryVo {

	private Integer procedureParameterId;
	private Short unitOfMeasureId;
	private String description;
	private String code;
}
