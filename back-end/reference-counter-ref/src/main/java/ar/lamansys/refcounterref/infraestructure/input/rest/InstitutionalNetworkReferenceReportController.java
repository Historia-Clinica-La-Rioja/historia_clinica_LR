package ar.lamansys.refcounterref.infraestructure.input.rest;

import ar.lamansys.refcounterref.application.getreferencecompletedata.GetReferenceCompleteData;
import ar.lamansys.refcounterref.application.getreferencesbymanagerrole.GetReferencesByManagerRole;
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
@RequestMapping("/references-report")
@Tag(name = "Institutional Network Reference Report", description = "Institutional Network Reference Report")
@RestController
public class InstitutionalNetworkReferenceReportController {

	private final GetReferenceCompleteData getReferenceCompleteData;

	private final GetReferencesByManagerRole getReferencesByManagerRole;

	private final GetReferenceMapper getReferenceMapper;

	private final ObjectMapper objectMapper;

	@GetMapping("/manager")
	@PreAuthorize("hasAnyAuthority('GESTOR_DE_ACCESO_DE_DOMINIO')")
	public ResponseEntity<PageDto<ReferenceReportDto>> getReferencesByManagerRole(@RequestParam(name = "filter") String filter,
																				  @RequestParam(name = "pageNumber") Integer pageNumber,
																				  @RequestParam(name = "pageSize") Integer pageSize) {
		log.debug("Input parameters -> filter {}, pageNumber {}, pageSize {}", filter, pageNumber, pageSize);
		var reportFilter = ReferenceReportFilterUtils.parseFilter(filter, objectMapper);
		var result = getReferencesByManagerRole.run(reportFilter, PageRequest.of(pageNumber, pageSize))
				.map(getReferenceMapper::toReferenceReportDto);
		return ResponseEntity.ok(PageDto.fromPage(result));
	}

	@GetMapping("/reference-detail/{referenceId}")
	@PreAuthorize("hasAnyAuthority('GESTOR_DE_ACCESO_DE_DOMINIO', 'GESTOR_DE_ACCESO_REGIONAL', 'GESTOR_DE_ACCESO_LOCAL')")
	public ResponseEntity<ReferenceCompleteDataDto> getReferenceDetail(@PathVariable(name = "referenceId") Integer referenceId) {
		log.debug("Input parameters -> referenceId {} ", referenceId);
		ReferenceCompleteDataBo reference = getReferenceCompleteData.run(referenceId);
		ReferenceCompleteDataDto result = getReferenceMapper.toReferenceCompleteDataDto(reference);
		log.debug("Output -> result {}", result);
		return ResponseEntity.ok(result);
	}

}
