package ar.lamansys.refcounterref.infraestructure.input.rest;

import ar.lamansys.refcounterref.application.getreceivedreferences.GetReceivedReferences;
import ar.lamansys.refcounterref.application.getreferencecompletedata.GetReferenceCompleteData;
import ar.lamansys.refcounterref.application.getrequestedreferences.GetRequestedReferences;
import ar.lamansys.refcounterref.domain.reference.ReferenceCompleteDataBo;
import ar.lamansys.refcounterref.infraestructure.input.ReferenceReportFilterUtils;
import ar.lamansys.refcounterref.infraestructure.input.rest.dto.ReferenceReportDto;
import ar.lamansys.refcounterref.infraestructure.input.rest.dto.reference.ReferenceCompleteDataDto;
import ar.lamansys.refcounterref.infraestructure.input.rest.mapper.GetReferenceMapper;
import ar.lamansys.sgh.shared.infrastructure.input.service.datastructures.PageDto;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@Validated
@RequestMapping("/institutions/{institutionId}/references-report")
@Tag(name = "Institutional Reference Report", description = "Institutional Reference Report")
@RestController
public class InstitutionalReferenceReportController {

	private final GetReceivedReferences getReceivedReferences;

	private final GetRequestedReferences getRequestedReferences;

	private final GetReferenceCompleteData getReferenceCompleteData;

	private final GetReferenceMapper getReferenceMapper;

	private final ObjectMapper objectMapper;

	@GetMapping("/received")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ADMINISTRATIVO_RED_DE_IMAGENES')")
	public ResponseEntity<PageDto<ReferenceReportDto>> getAllReceivedReferences(@PathVariable(name = "institutionId") Integer institutionId,
																				 @RequestParam(name = "filter") String filter,
																				 @RequestParam(name = "pageNumber") Integer pageNumber,
																				 @RequestParam(name = "pageSize") Integer pageSize) {
		log.debug("Input parameters -> institutionId {}, filter {}, pageNumber {}, pageSize {} ", institutionId, filter, pageNumber, pageSize);
		var reportFilter = ReferenceReportFilterUtils.parseFilter(filter, objectMapper);
		reportFilter.setDestinationInstitutionId(institutionId);
		var result = getReceivedReferences.run(reportFilter, PageRequest.of(pageNumber, pageSize))
				.map(getReferenceMapper::toReferenceReportDto);
		return ResponseEntity.ok(PageDto.fromPage(result));
	}

	@GetMapping("/requested")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ADMINISTRATIVO_RED_DE_IMAGENES, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA')")
	public ResponseEntity<PageDto<ReferenceReportDto>> getAllRequestedReferences(@PathVariable(name = "institutionId") Integer institutionId,
																				 @RequestParam(name = "filter") String filter,
																				 @RequestParam(name = "pageNumber") Integer pageNumber,
																				 @RequestParam(name = "pageSize") Integer pageSize) {
		log.debug("Input parameters -> institutionId {}, filter {}, pageNumber {}, pageSize {} ", institutionId, filter, pageNumber, pageSize);
		var reportFilter = ReferenceReportFilterUtils.parseFilter(filter, objectMapper);
		reportFilter.setOriginInstitutionId(institutionId);
		var result = getRequestedReferences.run(institutionId, reportFilter, PageRequest.of(pageNumber, pageSize))
				.map(getReferenceMapper::toReferenceReportDto);
		return ResponseEntity.ok(PageDto.fromPage(result));
	}

	@GetMapping("/reference-detail/{referenceId}")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA')")
	public ResponseEntity<ReferenceCompleteDataDto> getReferenceDetail(@PathVariable(name = "institutionId") Integer institutionId,
																	   @PathVariable(name = "referenceId") Integer referenceId) {
		log.debug("Input parameters -> institutionId {}, referenceId {} ", institutionId, referenceId);
		ReferenceCompleteDataBo reference = getReferenceCompleteData.run(referenceId);
		ReferenceCompleteDataDto result = getReferenceMapper.toReferenceCompleteDataDto(reference);
		log.debug("Output -> result {}", result);
		return ResponseEntity.ok(result);
	}

}
