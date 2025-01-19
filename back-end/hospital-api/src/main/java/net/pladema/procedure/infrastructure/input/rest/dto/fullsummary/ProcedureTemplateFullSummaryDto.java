package net.pladema.procedure.infrastructure.input.rest.dto.fullsummary;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class ProcedureTemplateFullSummaryDto {
	private Integer id;
	private String description;
	private List<ProcedureParameterFullSummaryDto> parameters;
}
