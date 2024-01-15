package net.pladema.violencereport.infrastructure.input.rest;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.sgh.shared.infrastructure.input.service.datastructures.PageDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.violencereport.application.GetLastSituationEvolutionReport;
import net.pladema.violencereport.application.GetPatientSituations;
import net.pladema.violencereport.application.SaveNewViolenceEpisode;
import net.pladema.violencereport.domain.ViolenceReportBo;
import net.pladema.violencereport.domain.ViolenceReportSituationBo;
import net.pladema.violencereport.infrastructure.input.rest.dto.ViolenceReportDto;
import net.pladema.violencereport.infrastructure.input.rest.dto.ViolenceReportSituationDto;
import net.pladema.violencereport.infrastructure.input.rest.mapper.ViolenceReportMapper;

@Slf4j
@Tag(name = "Violence report", description = "Controller used to handle violence report related operations")
@RestController
@Validated
@PreAuthorize("hasPermission(#institutionId, 'ABORDAJE_VIOLENCIAS')")
@AllArgsConstructor
@RequestMapping(value = "/institution/{institutionId}/violence-report")
public class ViolenceReportController {

	private ViolenceReportMapper violenceReportMapper;

	private SaveNewViolenceEpisode saveNewViolenceReport;

	private GetPatientSituations getPatientSituations;

	private GetLastSituationEvolutionReport getLastSituationEvolutionReport;

	@PostMapping(value = "/patient/{patientId}")
	public Integer saveNewViolenceReport(@PathVariable("institutionId") Integer institutionId,
										 @PathVariable("patientId") Integer patientId,
										 @RequestBody @Valid ViolenceReportDto violenceReport) {
		log.debug("Input parameters -> institutionId {}, patientId {}, violenceReport {}", institutionId, patientId, violenceReport);
		ViolenceReportBo violenceReportBo = parseViolenceReportDto(patientId, violenceReport);
		Integer result = saveNewViolenceReport.run(violenceReportBo);
		log.debug("Output -> {}", result);
		return result;
	}

	//Se devuelve un PageDto en lugar de una List ya que el dato de la cantidad TOTAL de elementos en la DB resulta relevante para flujos en el FE.
	@GetMapping(value = "/patient/{patientId}/get-situations")
	public PageDto<ViolenceReportSituationDto> getAllPatientViolenceSituations(@PathVariable("institutionId") Integer institutionId,
																			   @PathVariable("patientId") Integer patientId,
																			   @RequestParam("mustBeLimited") boolean mustBeLimited) {
		log.debug("Input parameters -> institutionId {}, patientId {}, mustBeLimited {}", institutionId, patientId, mustBeLimited);
		final int AUX_PAGEABLE_PAGE_NUMBER = 0;
		final int AUX_PAGEABLE_PAGE_SIZE = 1;
		Pageable pageable = PageRequest.of(AUX_PAGEABLE_PAGE_NUMBER, AUX_PAGEABLE_PAGE_SIZE);
		Page<ViolenceReportSituationBo> patientSituations = getPatientSituations.run(patientId, mustBeLimited, pageable);
		Page<ViolenceReportSituationDto> violenceReportSituationDtos = patientSituations.map(violenceReportMapper::toViolenceReportSituationDto);
		PageDto<ViolenceReportSituationDto> result = PageDto.fromPage(violenceReportSituationDtos);
		log.debug("Output -> {}", result);
		return result;
	}

	@GetMapping(value = "/patient/{patientId}/situation/{situationId}")
	public ViolenceReportDto getLastSituationEvolutionReport(@PathVariable("institutionId") Integer institutionId,
															 @PathVariable("patientId") Integer patientId,
															 @PathVariable("situationId") Integer situationId) {
		log.debug("Input parameters -> institutionId {}, patientId {}, situationId {}", institutionId, patientId, situationId);
		ViolenceReportBo violenceReportBo = getLastSituationEvolutionReport.run(patientId, situationId);
		ViolenceReportDto result = violenceReportMapper.toViolenceReportDto(violenceReportBo);
		log.debug("Output -> {}", result);
		return result;
	}

	private ViolenceReportBo parseViolenceReportDto(Integer patientId, ViolenceReportDto violenceReport) {
		ViolenceReportBo violenceReportBo = violenceReportMapper.fromViolenceReportDto(violenceReport);
		violenceReportBo.setPatientId(patientId);
		return violenceReportBo;
	}

}
