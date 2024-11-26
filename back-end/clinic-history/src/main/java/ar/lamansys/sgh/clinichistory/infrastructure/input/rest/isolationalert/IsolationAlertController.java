package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.isolationalert;

import java.util.List;

import ar.lamansys.sgh.clinichistory.application.isolationalerts.EditIsolationAlert;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.isolationalert.dto.UpdateIsolationAlertDto;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.sgh.clinichistory.application.isolationalerts.FetchPatientIsolationAlerts;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.isolationalert.dto.PatientCurrentIsolationAlertDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.isolationalert.mapper.IsolationAlertMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Isolation alerts controller", description = "Isolation alerts controller")
@RequestMapping("/institution/{institutionId}/isolation-alerts")
@RequiredArgsConstructor
@Slf4j
@RestController
public class IsolationAlertController {

	private final FetchPatientIsolationAlerts fetchPatientIsolationAlerts;
	private final IsolationAlertMapper isolationAlertMapper;
	private final EditIsolationAlert editIsolationAlert;

	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ENFERMERO, ESPECIALISTA_EN_ODONTOLOGIA')")
	@GetMapping("/{alertId}")
	public PatientCurrentIsolationAlertDto getAlertDetail(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "alertId") Integer alertId
	) {
		log.debug("Parameters -> institutionId {} alertId {} ", institutionId, alertId);
		var bo = fetchPatientIsolationAlerts.findByAlertId(alertId);
		var result = isolationAlertMapper.map(bo);
		log.debug("Output -> result {}", result);
		return result;
	}

	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ENFERMERO, ESPECIALISTA_EN_ODONTOLOGIA')")
	@GetMapping("/patient/{patientId}")
	public List<PatientCurrentIsolationAlertDto> getPatientCurrentAlerts(
		@PathVariable(name = "institutionId") Integer institutionId,
		@PathVariable(name = "patientId") Integer patientId
	) {
		log.debug("Parameters -> institutionId {} patientId {}", institutionId, patientId);
		var bo = fetchPatientIsolationAlerts.findByPatientId(patientId);
		var result = isolationAlertMapper.map(bo);
		log.debug("Output -> result {}", result);
		return result;
	}

	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ENFERMERO, ESPECIALISTA_EN_ODONTOLOGIA')")
	@PutMapping("/{alertId}/cancel")
	public PatientCurrentIsolationAlertDto cancel(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "alertId") Integer alertId
	) {
		log.debug("Parameters -> institutionId {} alertId {} ", institutionId, alertId);
		var edited = editIsolationAlert.cancel(alertId);
		var result = isolationAlertMapper.map(edited);
		log.debug("Output -> result {}", result);
		return result;
	}

	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ENFERMERO, ESPECIALISTA_EN_ODONTOLOGIA')")
	@PutMapping("/{alertId}")
	public PatientCurrentIsolationAlertDto update(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "alertId") Integer alertId,
			@RequestBody UpdateIsolationAlertDto update
			) {
		log.debug("Parameters -> institutionId {} alertId {} ", institutionId, alertId);
		var updatedValues = isolationAlertMapper.map(update);
		var edited = editIsolationAlert.update(alertId, updatedValues);
		var result = isolationAlertMapper.map(edited);
		log.debug("Output -> result {}", result);
		return result;
	}

}
