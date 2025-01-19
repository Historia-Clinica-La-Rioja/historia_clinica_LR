package ar.lamansys.refcounterref.infraestructure.input.rest;

import ar.lamansys.refcounterref.application.createreferenceobservation.CreateReferenceObservation;
import ar.lamansys.refcounterref.application.referenceforwarding.ReferenceForwarding;
import ar.lamansys.refcounterref.application.getreferencecompletedata.GetReferenceCompleteData;
import ar.lamansys.refcounterref.application.getreferencesbymanagerrole.GetReferencesByManagerRole;
import ar.lamansys.refcounterref.application.updatereferenceadministrativestate.UpdateReferenceAdministrativeState;
import ar.lamansys.refcounterref.application.updatereferenceregulationstate.UpdateReferenceRegulationState;
import ar.lamansys.refcounterref.application.updatereferenceforwarding.UpdateReferenceForwarding;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

	private final CreateReferenceObservation createReferenceObservation;

	private final ReferenceForwarding referenceForwarding;

	private final UpdateReferenceForwarding updateReferenceForwarding;

	private final GetReferenceMapper getReferenceMapper;

	private final ObjectMapper objectMapper;

	private final UpdateReferenceRegulationState updateReferenceRegulationState;

	private final UpdateReferenceAdministrativeState updateReferenceAdministrativeState;

	@GetMapping("/manager")
	@PreAuthorize("hasAnyAuthority('GESTOR_DE_ACCESO_DE_DOMINIO', 'GESTOR_DE_ACCESO_REGIONAL', 'GESTOR_DE_ACCESO_LOCAL')")
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

	@PostMapping("/{referenceId}/change-state")
	@PreAuthorize("hasAnyAuthority('GESTOR_DE_ACCESO_DE_DOMINIO', 'GESTOR_DE_ACCESO_LOCAL', 'GESTOR_DE_ACCESO_REGIONAL')")
	public Boolean changeReferenceRegulationState(@PathVariable(name = "referenceId") Integer referenceId,
														  @RequestParam(name = "stateId") Short stateId,
														  @RequestParam(name = "reason", required = false) String reason) {
		log.debug("Input parameters -> referenceId {}, stateId {}, reason {}", referenceId, stateId, reason);
		this.updateReferenceRegulationState.run(referenceId, stateId, reason);
		return Boolean.TRUE;
	}

	@PostMapping(value = "/{referenceId}/add-observation")
	@PreAuthorize("hasAnyAuthority('GESTOR_DE_ACCESO_DE_DOMINIO', 'GESTOR_DE_ACCESO_REGIONAL', 'GESTOR_DE_ACCESO_LOCAL')")
	public ResponseEntity<Boolean> addObservation(@PathVariable(name = "referenceId") Integer referenceId,
												  @RequestParam(name = "observation") String observation) {
		log.debug("Input parameters -> referenceId {}, observation {}", referenceId, observation);
		createReferenceObservation.run(referenceId, observation);
		return ResponseEntity.ok().body(Boolean.TRUE);
	}

	@PostMapping("/forwarding/{referenceId}")
	@PreAuthorize("hasAnyAuthority('GESTOR_DE_ACCESO_REGIONAL', 'GESTOR_DE_ACCESO_LOCAL')")
	public ResponseEntity<Boolean> addReferenceForwarding(@PathVariable(name = "referenceId") Integer referenceId,
														  @RequestParam(name = "observation") String observation) {
		log.debug("Input parameters -> referenceId {}, observation {} ", referenceId, observation);
		referenceForwarding.run(referenceId, observation);
		return ResponseEntity.ok(Boolean.TRUE);
	}

	@PutMapping("/update-forwarding/{forwardingId}")
	@PreAuthorize("hasAnyAuthority('GESTOR_DE_ACCESO_REGIONAL', 'GESTOR_DE_ACCESO_LOCAL')")
	public ResponseEntity<Boolean> updateReferenceForwarding(@PathVariable(name = "forwardingId") Integer forwardingId,
															 @RequestParam(name = "observation") String observation) {
		log.debug("Input parameters -> forwardingId {}, observation {} ", forwardingId, observation);
		updateReferenceForwarding.run(forwardingId, observation);
		return ResponseEntity.ok(Boolean.TRUE);
	}

	@PutMapping("/{referenceId}/change-administrative-state")
	@PreAuthorize("hasAnyAuthority('GESTOR_DE_ACCESO_DE_DOMINIO', 'GESTOR_DE_ACCESO_LOCAL', 'GESTOR_DE_ACCESO_REGIONAL')")
	public Boolean changeReferenceAdministrativeState(@PathVariable(name = "referenceId") Integer referenceId,
												  @RequestParam(name = "stateId") Short stateId,
												  @RequestParam(name = "reason", required = false) String reason) {
		log.debug("Input parameters -> referenceId {}, stateId {}, reason {}", referenceId, stateId, reason);
		this.updateReferenceAdministrativeState.run(referenceId, stateId, reason);
		return Boolean.TRUE;
	}

}
