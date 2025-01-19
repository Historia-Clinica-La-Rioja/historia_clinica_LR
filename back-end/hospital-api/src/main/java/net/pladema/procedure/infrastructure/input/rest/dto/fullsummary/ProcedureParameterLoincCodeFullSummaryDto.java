package net.pladema.procedure.infrastructure.input.rest.dto.fullsummary;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ProcedureParameterLoincCodeFullSummaryDto {
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
