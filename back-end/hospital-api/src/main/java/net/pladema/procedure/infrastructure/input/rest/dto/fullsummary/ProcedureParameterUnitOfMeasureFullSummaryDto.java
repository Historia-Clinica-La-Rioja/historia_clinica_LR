package net.pladema.procedure.infrastructure.input.rest.dto.fullsummary;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProcedureParameterUnitOfMeasureFullSummaryDto {
	private Short unitOfMeasureId;
	private String description;
	private String code;
}
