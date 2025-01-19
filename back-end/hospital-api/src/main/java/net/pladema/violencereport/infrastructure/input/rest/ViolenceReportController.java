package net.pladema.violencereport.infrastructure.input.rest;

import javax.validation.Valid;

import ar.lamansys.sgx.shared.filestorage.infrastructure.input.rest.StoredFileBo;
import ar.lamansys.sgx.shared.filestorage.infrastructure.input.rest.StoredFileResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.pladema.violencereport.application.GeneratePDFDocument;
import net.pladema.violencereport.application.GetFilters;
import net.pladema.violencereport.application.GetHistoricList;
import net.pladema.violencereport.application.GetSituationEvolution;
import net.pladema.violencereport.application.SaveSituationEvolution;

import net.pladema.violencereport.domain.ViolenceReportFilterBo;
import net.pladema.violencereport.domain.ViolenceReportFilterOptionBo;
import net.pladema.violencereport.domain.ViolenceReportSituationEvolutionBo;
import net.pladema.violencereport.infrastructure.input.rest.dto.ViolenceReportFilterOptionDto;
import net.pladema.violencereport.infrastructure.input.rest.dto.ViolenceReportSituationEvolutionDto;

import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
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

import java.util.List;

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

	private SaveSituationEvolution saveSituationEvolution;

	private GetHistoricList getHistoricList;

	private GetSituationEvolution getSituationEvolution;

	private GetFilters getFilters;

	private ObjectMapper objectMapper;

	private GeneratePDFDocument generatePDFDocument;

	@PostMapping(value = "/patient/{patientId}")
	@PreAuthorize("hasPermission(#institutionId, 'ABORDAJE_VIOLENCIAS')")
	public Integer saveNewViolenceReport(@PathVariable("institutionId") Integer institutionId,
										 @PathVariable("patientId") Integer patientId,
										 @RequestBody @Valid ViolenceReportDto violenceReport) {
		log.debug("Input parameters -> institutionId {}, patientId {}, violenceReport {}", institutionId, patientId, violenceReport);
		ViolenceReportBo violenceReportBo = parseViolenceReportDtoWithoutSituationId(institutionId, patientId, violenceReport);
		Integer result = saveNewViolenceReport.run(violenceReportBo);
		log.debug("Output -> {}", result);
		return result;
	}

	//Se devuelve un PageDto en lugar de una List ya que el dato de la cantidad TOTAL de elementos en la DB resulta relevante para flujos en el FE.
	@GetMapping(value = "/patient/{patientId}/get-situations")
	@PreAuthorize("hasPermission(#institutionId, 'ABORDAJE_VIOLENCIAS')")
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
	@PreAuthorize("hasPermission(#institutionId, 'ABORDAJE_VIOLENCIAS')")
	public ViolenceReportDto getLastSituationEvolutionReport(@PathVariable("institutionId") Integer institutionId,
															 @PathVariable("patientId") Integer patientId,
															 @PathVariable("situationId") Integer situationId) {
		log.debug("Input parameters -> institutionId {}, patientId {}, situationId {}", institutionId, patientId, situationId);
		ViolenceReportBo violenceReportBo = getLastSituationEvolutionReport.run(patientId, situationId);
		ViolenceReportDto result = violenceReportMapper.toViolenceReportDto(violenceReportBo);
		log.debug("Output -> {}", result);
		return result;
	}

	@PostMapping(value = "/patient/{patientId}/situation/{situationId}/evolve")
	@PreAuthorize("hasPermission(#institutionId, 'ABORDAJE_VIOLENCIAS')")
	public Integer saveSituationEvolution(@PathVariable("institutionId") Integer institutionId,
										  @PathVariable("patientId") Integer patientId,
										  @PathVariable("situationId") Short situationId,
										  @RequestBody @Valid ViolenceReportDto violenceReport) {
		log.debug("Input parameters -> institutionId {}, patientId {}, situationId {}, violenceReport {}", institutionId, patientId, situationId, violenceReport);
		ViolenceReportBo violenceReportBo = parseViolenceReportDtoWithSituationId(institutionId, patientId, situationId, violenceReport);
		Integer result = saveSituationEvolution.run(violenceReportBo);
		log.debug("Output -> {}", result);
		return result;
	}

	@GetMapping(value = "/patient/{patientId}/historic")
	@PreAuthorize("hasPermission(#institutionId, 'ABORDAJE_VIOLENCIAS')")
	public List<ViolenceReportSituationEvolutionDto> getHistoric(@PathVariable("institutionId") Integer institutionId,
																 @PathVariable("patientId") Integer patientId,
																 @RequestParam("filterData") String filterData) throws JsonProcessingException {
		log.debug("Input parameters -> institutionId {}, patientId {}, filterData {}", institutionId, patientId, filterData);
		ViolenceReportFilterBo filter = initializeViolenceReportFilter(filterData);
		List<ViolenceReportSituationEvolutionBo> violenceReportSituationEvolutionBos = getHistoricList.run(patientId, filter);
		List<ViolenceReportSituationEvolutionDto> result = violenceReportMapper.toViolenceReportSituationEvolutionDtoList(violenceReportSituationEvolutionBos);
		log.debug("Output -> result {}", result);
		return result;
	}

	private ViolenceReportFilterBo initializeViolenceReportFilter(String filterData) throws JsonProcessingException {
		ViolenceReportFilterBo filter = objectMapper.readValue(filterData, ViolenceReportFilterBo.class);
		if (filter == null)
			filter = new ViolenceReportFilterBo();
		return filter;
	}

	@GetMapping(value = "/patient/{patientId}/situation/{situationId}/evolution/{evolutionId}")
	@PreAuthorize("hasPermission(#institutionId, 'ABORDAJE_VIOLENCIAS')")
	public ViolenceReportDto getSituationEvolutionData(@PathVariable("institutionId") Integer institutionId,
													   @PathVariable("patientId") Integer patientId,
													   @PathVariable("situationId") Short situationId,
													   @PathVariable("evolutionId") Short evolutionId) {
		log.debug("Input parameters -> institutionId {}, patientId {}, situationId {}, evolutionId {}", institutionId, patientId, situationId, evolutionId);
		ViolenceReportBo violenceReportBo = getSituationEvolution.run(patientId, situationId, evolutionId);
		ViolenceReportDto result = violenceReportMapper.toViolenceReportDto(violenceReportBo);
		log.debug("Output -> {}", result);
		return result;
	}

	@GetMapping(value = "/patient/{patientId}/get-filter")
	@PreAuthorize("hasPermission(#institutionId, 'ABORDAJE_VIOLENCIAS')")
	public ViolenceReportFilterOptionDto getFilters(@PathVariable("institutionId") Integer institutionId,
													@PathVariable("patientId") Integer patientId) {
		log.debug("Input parameters -> institutionId {}, patientId {}", institutionId, patientId);
		ViolenceReportFilterOptionBo filters = getFilters.run(patientId);
		ViolenceReportFilterOptionDto result = violenceReportMapper.toViolenceReportFilterOptionDto(filters);
		log.debug("Output -> {}", result);
		return result;
	}

	private ViolenceReportBo parseViolenceReportDtoWithSituationId(Integer institutionId, Integer patientId, Short situationId, ViolenceReportDto violenceReport) {
		ViolenceReportBo violenceReportBo = parseViolenceReportDtoWithoutSituationId(institutionId, patientId, violenceReport);
		violenceReportBo.setSituationId(situationId);
		return violenceReportBo;
	}

	private ViolenceReportBo parseViolenceReportDtoWithoutSituationId(Integer institutionId, Integer patientId, ViolenceReportDto violenceReport) {
		ViolenceReportBo violenceReportBo = violenceReportMapper.fromViolenceReportDto(violenceReport);
		violenceReportBo.setPatientId(patientId);
		violenceReportBo.setInstitutionId(institutionId);
		return violenceReportBo;
	}

	@GetMapping(value = "/patient/{patientId}/situation/{situationId}/evolution/{evolutionId}/download")
	@PreAuthorize("hasPermission(#institutionId, 'ABORDAJE_VIOLENCIAS')")
	public ResponseEntity<Resource> downloadDocument(@PathVariable("institutionId") Integer institutionId,
													 @PathVariable("patientId") Integer patientId,
													 @PathVariable("situationId") Short situationId,
													 @PathVariable("evolutionId") Short evolutionId) {
		log.debug("Input parameters -> institutionId {}, patientId {}, situationId {}, evolutionId {}", institutionId, patientId, situationId, evolutionId);
		StoredFileBo document = generatePDFDocument.run(patientId, situationId, evolutionId);
		ResponseEntity<Resource> result = StoredFileResponse.sendFile(document);
		log.debug("Output -> {}", result);
		return result;
	}

}
