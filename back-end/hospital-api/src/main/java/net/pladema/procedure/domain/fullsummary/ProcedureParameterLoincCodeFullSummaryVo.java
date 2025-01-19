package net.pladema.procedure.domain.fullsummary;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ProcedureParameterLoincCodeFullSummaryVo {
	private Integer procedureParameterId;
	private Integer loincCodeId;
	private Short statusId;
	private String statusDescription;
	private Short systemId;
	private String systemDescription;
	private String description;
	private String code;
	private String displayName;
	private String customDisplayName;

}
