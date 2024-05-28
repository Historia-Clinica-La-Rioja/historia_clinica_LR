package net.pladema.procedure.infrastructure.input.rest.mapper;

import net.pladema.procedure.domain.fullsummary.ProcedureParameterFullSummaryVo;
import net.pladema.procedure.domain.fullsummary.ProcedureParameterLoincCodeFullSummaryVo;
import net.pladema.procedure.domain.fullsummary.ProcedureParameterTextOptionFullSummaryVo;
import net.pladema.procedure.domain.fullsummary.ProcedureParameterUnitOfMeasureFullSummaryVo;
import net.pladema.procedure.domain.fullsummary.ProcedureTemplateFullSummaryVo;

import net.pladema.procedure.infrastructure.input.rest.dto.fullsummary.ProcedureParameterFullSummaryDto;
import net.pladema.procedure.infrastructure.input.rest.dto.fullsummary.ProcedureParameterLoincCodeFullSummaryDto;
import net.pladema.procedure.infrastructure.input.rest.dto.fullsummary.ProcedureParameterTextOptionFullSummaryDto;
import net.pladema.procedure.infrastructure.input.rest.dto.fullsummary.ProcedureParameterUnitOfMeasureFullSummaryDto;
import net.pladema.procedure.infrastructure.input.rest.dto.fullsummary.ProcedureTemplateFullSummaryDto;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mapper
public interface ProcedureTemplateSummaryMapper {

	public ProcedureTemplateFullSummaryDto toProcedureTemplateFullSummaryDto(ProcedureTemplateFullSummaryVo source);

	@AfterMapping
	default void multiplyMultiValueInputs(@MappingTarget ProcedureTemplateFullSummaryDto target) {
		var params =
		target.getParameters().stream()
		.flatMap(p -> {
			if (p.getInputCount() != null && p.getInputCount() > 1)
				return Collections.nCopies(p.getInputCount(), p).stream();
			else
				return Stream.of(p);
		}).collect(Collectors.toList());
		target.setParameters(params);
	}

	public ProcedureParameterFullSummaryDto toFullSummaryDto(ProcedureParameterFullSummaryVo source);
	public ProcedureParameterLoincCodeFullSummaryDto toFullSummaryDto(ProcedureParameterLoincCodeFullSummaryVo source);
	public ProcedureParameterTextOptionFullSummaryDto toFullSummaryDto(ProcedureParameterTextOptionFullSummaryVo source);
	public ProcedureParameterUnitOfMeasureFullSummaryDto toFullSummaryDto(ProcedureParameterUnitOfMeasureFullSummaryVo source);
}
