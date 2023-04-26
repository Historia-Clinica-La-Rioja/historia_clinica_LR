package net.pladema.medicalconsultation.shockroom.infrastructure.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.medicalconsultation.shockroom.application.FetchShockrooms;

import net.pladema.medicalconsultation.shockroom.infrastructure.controller.dto.ShockroomDto;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/institutions/{institutionId}/shockroom")
@AllArgsConstructor
@Slf4j
public class ShockroomController {

	private final FetchShockrooms fetchShockrooms;
	public static final String OUTPUT = "Output -> {}";

	@GetMapping()
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ADMINISTRATIVO_RED_DE_IMAGENES, ENFERMERO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA')")
	public ResponseEntity<List<ShockroomDto>> getShockrooms(@PathVariable(name = "institutionId") Integer institutionId) {
		log.debug("Input parameters -> institutionId {}", institutionId);
		List<ShockroomDto> result = fetchShockrooms.execute(institutionId)
				.stream()
				.map(bo -> new ShockroomDto(bo.getId(), bo.getDescription()))
				.collect(Collectors.toList());
		log.debug(OUTPUT, result);
		return ResponseEntity.ok().body(result);

	}
}
