package net.pladema.emergencycare.infrastructure.input.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.emergencycare.application.getallemergencycaresectorbyinstitution.GetAllEmergencyCareSectorByInstitution;
import net.pladema.emergencycare.infrastructure.input.rest.dto.EmergencyCareAttentionPlaceDto;

import net.pladema.emergencycare.infrastructure.input.rest.mapper.EmergencyCareAttentionPlaceMapper;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/institution/{institutionId}/emergency-care/attention-places/sector")
@RestController
public class EmergencyCareAttentionPlaceSectorController {

	private final GetAllEmergencyCareSectorByInstitution getAllEmergencyCareSectorByInstitution;
	private final EmergencyCareAttentionPlaceMapper emergencyCareAttentionPlaceMapper;

	@GetMapping
	@PreAuthorize("hasPermission(#institutionId, 'ENFERMERO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA')")
	public List<EmergencyCareAttentionPlaceDto> getAll(@PathVariable(name = "institutionId") Integer institutionId){
		log.debug("Input get all emergency care sectors parameters -> institutionId {}", institutionId);
		List<EmergencyCareAttentionPlaceDto> result = emergencyCareAttentionPlaceMapper.toDtoList(
				getAllEmergencyCareSectorByInstitution.run(institutionId));
		log.debug("Output -> {}", result);
		return result;
	}
}
