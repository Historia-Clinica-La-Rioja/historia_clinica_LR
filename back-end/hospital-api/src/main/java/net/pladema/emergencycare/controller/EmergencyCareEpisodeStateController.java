package net.pladema.emergencycare.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import net.pladema.emergencycare.service.EmergencyCareEpisodeStateService;
import net.pladema.emergencycare.service.domain.enums.EEmergencyCareState;
import ar.lamansys.sgx.shared.masterdata.infrastructure.input.rest.dto.MasterDataDto;
import ar.lamansys.sgx.shared.masterdata.domain.EnumWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/institution/{institutionId}/emergency-care/episodes/{episodeId}/state")
@Tag(name = "Emergency care episodes state", description = "Emergency care episodes state")
public class EmergencyCareEpisodeStateController {

	private static final Logger LOG = LoggerFactory.getLogger(EmergencyCareEpisodeStateController.class);

	private final EmergencyCareEpisodeStateService emergencyCareEpisodeStateService;

	public EmergencyCareEpisodeStateController(EmergencyCareEpisodeStateService emergencyCareEpisodeStateService){
		this.emergencyCareEpisodeStateService = emergencyCareEpisodeStateService;
	}

	@GetMapping
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ADMINISTRATIVO_RED_DE_IMAGENES, ENFERMERO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, PRESCRIPTOR, ESPECIALISTA_EN_ODONTOLOGIA, ABORDAJE_VIOLENCIAS')")
	public ResponseEntity<MasterDataDto> getState(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "episodeId") Integer episodeId) {
		LOG.debug("Input parameters -> institutionId {}, episodeId {}", institutionId, episodeId);
		EEmergencyCareState result = emergencyCareEpisodeStateService.getState(episodeId, institutionId);
		LOG.debug("Output -> {}", result);
		return ResponseEntity.ok().body(EnumWriter.write(result));
	}

	@PostMapping
	@PreAuthorize("hasPermission(#institutionId, 'ENFERMERO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA')")
	public ResponseEntity<Boolean> changeState(
			@PathVariable(name = "episodeId") Integer episodeId,
			@PathVariable(name = "institutionId") Integer institutionId,
			@RequestParam(name = "emergencyCareStateId") Short emergencyCareStateId,
			@RequestParam(name = "doctorsOfficeId", required = false) Integer doctorsOfficeId,
			@RequestParam(name = "shockroomId", required = false) Integer shockroomId,
			@RequestParam(name = "bedId", required = false) Integer bedId) {
		LOG.debug("Change emergency care state -> episodeId {}, institutionId {}, emergencyCareStateId {}, doctorsOfficeId {}, shockroomId {}, bedId {}",
				episodeId, institutionId, emergencyCareStateId, doctorsOfficeId, shockroomId, bedId);
		Boolean result = emergencyCareEpisodeStateService.changeState(episodeId, institutionId, emergencyCareStateId, doctorsOfficeId, shockroomId, bedId);
		LOG.debug("Output -> {}", result);
		return ResponseEntity.ok().body(result);
	}
}
