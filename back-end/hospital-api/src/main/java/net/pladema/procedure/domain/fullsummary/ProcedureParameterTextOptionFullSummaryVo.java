package net.pladema.procedure.domain.fullsummary;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ProcedureParameterTextOptionFullSummaryVo {
	private Integer procedureParameterId;
	private Integer procedureTextOptionId;
	private String description;
}
