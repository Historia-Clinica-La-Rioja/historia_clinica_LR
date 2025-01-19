package net.pladema.procedure.infrastructure.input.rest.dto;

import lombok.Value;
import net.pladema.procedure.domain.ProcedureTemplateVo;

@Value
public class ProcedureTemplateShortSummaryDto {
	private Integer id;
	private String description;
	public static ProcedureTemplateShortSummaryDto fromVo(ProcedureTemplateVo pt) {
		return new ProcedureTemplateShortSummaryDto(pt.getId(), pt.getDescription());
	}
}
