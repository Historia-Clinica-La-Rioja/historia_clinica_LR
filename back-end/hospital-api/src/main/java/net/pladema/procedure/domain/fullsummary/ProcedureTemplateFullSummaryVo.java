package net.pladema.procedure.domain.fullsummary;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ProcedureTemplateFullSummaryVo {
	private Integer id;
	private String description;
	private List<ProcedureParameterFullSummaryVo> parameters;

	public Optional<ProcedureParameterFullSummaryVo> findParameter(Integer parameterId) {
		if (parameters == null) return Optional.empty();
		return getParameters()
			.stream()
			.filter(x -> Objects.equals(x.getId(), parameterId))
			.findFirst();
	}
}
