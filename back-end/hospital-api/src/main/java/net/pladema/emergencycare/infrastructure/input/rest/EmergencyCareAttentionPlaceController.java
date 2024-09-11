package net.pladema.emergencycare.infrastructure.input.rest;


import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import net.pladema.emergencycare.application.getallemergencycareattentionplaces.GetAllEmergencyCareAttentionPlaces;

import net.pladema.emergencycare.application.getemergencycarebeddetail.GetEmergencyCareBedDetail;
import net.pladema.emergencycare.application.getemergencycareshockroomdetail.GetEmergencyCareShockRoomDetail;
import net.pladema.emergencycare.infrastructure.input.rest.dto.EmergencyCareAttentionPlaceDto;

import net.pladema.emergencycare.infrastructure.input.rest.dto.EmergencyCareBedDetailDto;
import net.pladema.emergencycare.infrastructure.input.rest.dto.EmergencyCareShockRoomDetailDto;
import net.pladema.emergencycare.infrastructure.input.rest.mapper.EmergencyCareAttentionPlaceMapper;

import net.pladema.person.controller.service.PersonExternalService;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/institution/{institutionId}/emergency-care/attention-places")
@RestController
public class EmergencyCareAttentionPlaceController {

	private final GetAllEmergencyCareAttentionPlaces getAllEmergencyCareAttentionPlaces;
	private final EmergencyCareAttentionPlaceMapper emergencyCareAttentionPlaceMapper;
	private final GetEmergencyCareBedDetail getEmergencyCareBedDetail;
	private final PersonExternalService personExternalService;
	private final GetEmergencyCareShockRoomDetail getEmergencyCareShockRoomDetail;

	@GetMapping
	@PreAuthorize("hasPermission(#institutionId, 'ENFERMERO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA')")
	public List<EmergencyCareAttentionPlaceDto> getAll(@PathVariable(name = "institutionId") Integer institutionId){
		log.debug("Input get all emergency care attention places parameters -> institutionId {}", institutionId);
		List<EmergencyCareAttentionPlaceDto> result = emergencyCareAttentionPlaceMapper.toDtoList(
				getAllEmergencyCareAttentionPlaces.run(institutionId));
		log.debug("Output -> {}", result);
		return result;
	}

	@GetMapping("/bed/{bedId}")
	@PreAuthorize("hasPermission(#institutionId, 'ENFERMERO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA')")
	public EmergencyCareBedDetailDto getBedDetail(@PathVariable(name = "institutionId") Integer institutionId, @PathVariable(name = "bedId") Integer bedId){
		log.debug("Input get emergency care bed detail parameters -> institutionId {}, bedId {}", institutionId, bedId);
		EmergencyCareBedDetailDto result = emergencyCareAttentionPlaceMapper.toEmergencyCareBedDetailDto(getEmergencyCareBedDetail.run(bedId));
		if (result.hasPersonId())
			result.setPersonPhoto(personExternalService.getPersonPhoto(result.getPersonId()));
		log.debug("Output -> {}", result);
		return result;
	}

	@GetMapping("/shockroom/{shockroomId}")
	@PreAuthorize("hasPermission(#institutionId, 'ENFERMERO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA')")
	public EmergencyCareShockRoomDetailDto getShockRoomDetail(@PathVariable(name = "institutionId") Integer institutionId, @PathVariable(name = "shockroomId") Integer shockroomId){
		log.debug("Input get emergency care shockroom detail parameters -> institutionId {}, shockroomId {}", institutionId, shockroomId);
		EmergencyCareShockRoomDetailDto result = emergencyCareAttentionPlaceMapper.toEmergencyCareShockRoomDetailDto(getEmergencyCareShockRoomDetail.run(shockroomId));
		if (result.hasPersonId())
			result.setPersonPhoto(personExternalService.getPersonPhoto(result.getPersonId()));
		log.debug("Output -> {}", result);
		return result;
	}
}
