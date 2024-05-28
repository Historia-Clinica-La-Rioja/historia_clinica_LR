package net.pladema.procedure.infrastructure.input.rest;

import net.pladema.procedure.application.FindProcedureTemplatesAvailableForDiagnosticReport;

import net.pladema.procedure.application.FindProcedureTempleteFullSummary;
import net.pladema.procedure.application.exceptions.ProcedureTemplateNotFoundException;
import net.pladema.procedure.infrastructure.input.rest.dto.fullsummary.ProcedureTemplateFullSummaryDto;
import net.pladema.procedure.infrastructure.input.rest.dto.ProcedureTemplateShortSummaryDto;

import net.pladema.procedure.infrastructure.input.rest.mapper.ProcedureTemplateSummaryMapper;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@Tag(name = "Procedure template", description = "Procedure template")
@RequiredArgsConstructor
@RequestMapping("/procedure-templates")
public class ProcedureTemplateController {

	private final FindProcedureTemplatesAvailableForDiagnosticReport findProcedureTemplatesAvailableForDiagnosticReport;
	private final FindProcedureTempleteFullSummary findProcedureTempleteFullSummary;
	private final ProcedureTemplateSummaryMapper procedureTemplateSummaryMapper;

	@GetMapping("/diagnostic-report/{diagnosticReportId}")
	public List<ProcedureTemplateShortSummaryDto> findAvailableForDiagnosticReportId(@PathVariable(name = "diagnosticReportId") Integer diagnosticReportId) {
		log.debug("Input parameter -> diagnosticReportId {}", diagnosticReportId);
		var templates = findProcedureTemplatesAvailableForDiagnosticReport.run(diagnosticReportId);
		var result = templates.stream().map(pt -> ProcedureTemplateShortSummaryDto.fromVo(pt)).collect(Collectors.toList());
		log.debug("Output -> {}", result);
		return result;
	}

	@GetMapping("{procedureTemplateId}")
	public ProcedureTemplateFullSummaryDto findById(
		@PathVariable("procedureTemplateId") Integer procedureTemplateId
	) throws ProcedureTemplateNotFoundException {
		log.debug("Input parameter -> procedureTemplateId {}", procedureTemplateId);
		var result = procedureTemplateSummaryMapper.toProcedureTemplateFullSummaryDto(
				findProcedureTempleteFullSummary.run(procedureTemplateId)
		);
		log.debug("Output -> {}", result);
		return result;
	}

}
