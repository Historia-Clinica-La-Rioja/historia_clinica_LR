package net.pladema.emergencycare.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.emergencycare.controller.dto.EmergencyCareEpisodeInProgressDto;
import net.pladema.emergencycare.controller.dto.EmergencyCareListDto;
import net.pladema.emergencycare.controller.mapper.EmergencyCareMapper;
import net.pladema.emergencycare.service.EmergencyCareEpisodeService;
import net.pladema.emergencycare.service.domain.EmergencyCareBo;
import net.pladema.emergencycare.service.domain.EmergencyCareEpisodeInProgressBo;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/institution/{institutionId}/emergency-care/episode")
@Tag(name = "Emergency care episode summary", description = "Emergency care episode summary")
public class EmergencyCareEpisodeSummaryController {

	private final EmergencyCareEpisodeService emergencyCareEpisodeService;
	private final EmergencyCareMapper emergencyCareMapper;

	@GetMapping("/in-progress/patient/{patientId}")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ENFERMERO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, PERSONAL_DE_IMAGENES, PERSONAL_DE_LABORATORIO, PERSONAL_DE_FARMACIA')")
	public ResponseEntity<EmergencyCareEpisodeInProgressDto> emergencyCareEpisodeInProgress(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "patientId") Integer patientId) {
		log.debug("Input parameters -> institutionId {}, patientId {}", institutionId, patientId);
		EmergencyCareEpisodeInProgressBo resultQuery = emergencyCareEpisodeService.emergencyCareEpisodeInProgress(institutionId, patientId);
		EmergencyCareEpisodeInProgressDto result = emergencyCareMapper.toEmergencyCareEpisodeInProgressDto(resultQuery);
		log.debug("Output -> {}", result);
		return ResponseEntity.ok().body(result);
	}

	@GetMapping("/{episodeId}/summary")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ENFERMERO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD')")
	public ResponseEntity<EmergencyCareListDto> getEpisodeSummary(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name= "episodeId") Integer episodeId) {
		log.debug("Input parameters -> institutionId {}, episodeId {}", institutionId, episodeId);
		EmergencyCareBo episode = emergencyCareEpisodeService.getEpisodeSummary(institutionId, episodeId);
		EmergencyCareListDto result = emergencyCareMapper.toEmergencyCareListDto(episode);
		log.debug("Output -> {}", result);
		return ResponseEntity.ok().body(result);
	}

}
