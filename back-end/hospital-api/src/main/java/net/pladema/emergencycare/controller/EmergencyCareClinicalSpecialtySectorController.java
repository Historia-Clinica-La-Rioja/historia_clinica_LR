package net.pladema.emergencycare.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.emergencycare.application.getallclinicalspecialtysectorbyinstitution.GetAllClinicalSpecialtySectorByInstitution;
import net.pladema.emergencycare.application.getlastclinicalspecialtysectorbyepisode.GetLastClinicalSpecialtySectorByEpisode;
import net.pladema.emergencycare.controller.dto.EmergencyCareClinicalSpecialtySectorDto;
import net.pladema.emergencycare.controller.mapper.EmergencyCareClinicalSpecialtySectorMapper;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/institution/{institutionId}/emergency-care/episodes/specialty-sector")
@RestController
public class EmergencyCareClinicalSpecialtySectorController {

	private final GetAllClinicalSpecialtySectorByInstitution getAllClinicalSpecialtySectorByInstitution;
	private final EmergencyCareClinicalSpecialtySectorMapper emergencyCareClinicalSpecialtySectorMapper;
	private final GetLastClinicalSpecialtySectorByEpisode getLastClinicalSpecialtySectorByEpisode;

	@GetMapping
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ADMINISTRATIVO_RED_DE_IMAGENES, ENFERMERO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA')")
	public List<EmergencyCareClinicalSpecialtySectorDto> getAll(
			@PathVariable(name = "institutionId") Integer institutionId){
		log.debug("Input parameters getAll emergency care clinical specialty sector -> institutionId {}", institutionId);
		List<EmergencyCareClinicalSpecialtySectorDto> result = emergencyCareClinicalSpecialtySectorMapper.toDtoList(
				getAllClinicalSpecialtySectorByInstitution.run(institutionId));
		log.debug("Output -> {}", result);
		return result;
	}

	@GetMapping("/episode/{episodeId}/last")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ADMINISTRATIVO_RED_DE_IMAGENES, ENFERMERO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA')")
	public EmergencyCareClinicalSpecialtySectorDto getLastByEpisode(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "episodeId") Integer episodeId){
		log.debug("Input parameters get last emergency care clinical specialty sector used by episode id -> institutionId {}, episodeId {}", institutionId, episodeId);
		EmergencyCareClinicalSpecialtySectorDto result = emergencyCareClinicalSpecialtySectorMapper.toDto(
				getLastClinicalSpecialtySectorByEpisode.run(episodeId));
		log.debug("Output -> {}", result);
		return result;
	}

}
