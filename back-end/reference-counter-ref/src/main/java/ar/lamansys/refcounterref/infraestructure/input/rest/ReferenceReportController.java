package ar.lamansys.refcounterref.infraestructure.input.rest;

import ar.lamansys.refcounterref.application.getreceivedreferences.GetReceivedReferences;
import ar.lamansys.refcounterref.application.getreferencecompletedata.GetReferenceCompleteData;
import ar.lamansys.refcounterref.application.getrequestedreferences.GetRequestedReferences;
import ar.lamansys.refcounterref.domain.ReferenceReportBo;
import ar.lamansys.refcounterref.domain.reference.ReferenceCompleteDataBo;
import ar.lamansys.refcounterref.infraestructure.input.rest.dto.ReferenceReportDto;
import ar.lamansys.refcounterref.infraestructure.input.rest.dto.reference.ReferenceCompleteDataDto;
import ar.lamansys.refcounterref.infraestructure.input.rest.mapper.GetReferenceMapper;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Validated
@RequestMapping("/institutions/{institutionId}/references-report")
@Tag(name = "Reference Report", description = "Reference Report")
@RestController
public class ReferenceReportController {

	private final GetReceivedReferences getReceivedReferences;

	private final GetRequestedReferences getRequestedReferences;

	private final GetReferenceCompleteData getReferenceCompleteData;

	private final GetReferenceMapper getReferenceMapper;

	private final LocalDateMapper localDateMapper;

	@GetMapping("/received")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ADMINISTRATIVO_RED_DE_IMAGENES')")
	public ResponseEntity<List<ReferenceReportDto>> getAllReceivedReferences(@PathVariable(name = "institutionId") Integer institutionId,
																			 @RequestParam(name = "from") String from,
																			 @RequestParam(name = "to") String to) {
		log.debug("Input parameters -> institutionId {}, from {}, to {}", institutionId, from, to);
		LocalDate startDate = localDateMapper.fromStringToLocalDate(from);
		LocalDate endDate = localDateMapper.fromStringToLocalDate(to);
		List<ReferenceReportBo> references = getReceivedReferences.run(institutionId, startDate, endDate);
		List<ReferenceReportDto> result = getReferenceMapper.toReferenceReportDtoList(references);
		log.debug("Output -> result {}", result.size());
		return ResponseEntity.ok(result);
	}

	@GetMapping("/requested")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ADMINISTRATIVO_RED_DE_IMAGENES, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA')")
	public ResponseEntity<List<ReferenceReportDto>> getAllRequestedReferences(@PathVariable(name = "institutionId") Integer institutionId,
																			  @RequestParam(name = "from") String from,
																			  @RequestParam(name = "to") String to) {
		log.debug("Input parameters -> institutionId {}, from {}, to {}", institutionId, from, to);
		LocalDate startDate = localDateMapper.fromStringToLocalDate(from);
		LocalDate endDate = localDateMapper.fromStringToLocalDate(to);
		List<ReferenceReportBo> references = getRequestedReferences.run(institutionId, startDate, endDate);
		List<ReferenceReportDto> result = getReferenceMapper.toReferenceReportDtoList(references);
		log.debug("Output -> result {}", result.size());
		return ResponseEntity.ok(result);
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
