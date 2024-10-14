package net.pladema.emergencycare.controller;

import ar.lamansys.sgh.clinichistory.application.fetchEmergencyCareEpisodeState.FetchEmergencyCareEpisodeStateDiagnoses;
import ar.lamansys.sgh.clinichistory.domain.ips.DiagnosisBo;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.DiagnosesGeneralStateDto;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.AllArgsConstructor;

import net.pladema.clinichistory.hospitalization.controller.mapper.InternmentStateMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/institution/{institutionId}/emergency-care/episode/{episodeId}")
@Tag(name = "Emergency care states", description = "Emergency care states")
@Validated
@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, ENFERMERO_ADULTO_MAYOR, ENFERMERO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, PERSONAL_DE_IMAGENES, PERSONAL_DE_LABORATORIO, PERSONAL_DE_FARMACIA')")
@AllArgsConstructor
public class EmergencyCareStateController {

	private static final Logger LOG = LoggerFactory.getLogger(EmergencyCareStateController.class);

	private final FetchEmergencyCareEpisodeStateDiagnoses fetchEmergencyCareEpisodeStateDiagnoses;

	private final InternmentStateMapper internmentStateMapper;

	@GetMapping("/diagnoses")
	public ResponseEntity<List<DiagnosesGeneralStateDto>> getEmergencyCareEpisodeDiagnoses(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "episodeId") Integer episodeId) {
		LOG.debug("Input parameters -> institutionId {}, episodeId {}", institutionId, episodeId);
		List<DiagnosisBo> diagnoses = fetchEmergencyCareEpisodeStateDiagnoses.getDiagnosesGeneralState(episodeId);
		List<DiagnosesGeneralStateDto> result = internmentStateMapper.toListDiagnosesGeneralStateDtoFromDiagnosisBoList(diagnoses);
		LOG.debug("Output -> result {}", result);
		return  ResponseEntity.ok().body(result);
	}

}
