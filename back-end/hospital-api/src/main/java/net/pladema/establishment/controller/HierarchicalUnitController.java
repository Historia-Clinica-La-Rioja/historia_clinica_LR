package net.pladema.establishment.controller;

import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.establishment.application.hierarchicalunits.FetchHierarchicalUnitsByUserIdAndInstitutionId;
import net.pladema.establishment.controller.dto.HierarchicalUnitDto;

import net.pladema.establishment.service.HierarchicalUnitService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestController
@Tag(name = "Hierarchical Unit", description = "Hierarchical Unit")
@RequestMapping("/institutions/{institutionId}/hierarchicalunit")
public class HierarchicalUnitController {

	private final HierarchicalUnitService hierarchicalUnitService;

	private final FetchHierarchicalUnitsByUserIdAndInstitutionId fetchHierarchicalUnitsByUserIdAndInstitutionId;

	@GetMapping()
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, " +
			"ENFERMERO, ADMINISTRATIVO, ADMINISTRADOR_DE_CAMAS, ADMINISTRADOR_INSTITUCIONAL_PRESCRIPTOR, PERSONAL_DE_ESTADISTICA')")
	public ResponseEntity<List<HierarchicalUnitDto>> getAllByInstitution(@PathVariable(name = "institutionId") Integer institutionId) {
		log.debug("Input institutionId {} ", institutionId);
		List<HierarchicalUnitDto> hierarchicalUnitsDto = hierarchicalUnitService.getByInstitution(institutionId).stream()
				.map(bo -> new HierarchicalUnitDto(bo.getId(), bo.getName(), bo.getTypeId()))
				.collect(Collectors.toList());
		log.debug("Output {} ", hierarchicalUnitsDto);
		return ResponseEntity.ok(hierarchicalUnitsDto);
	}

	@GetMapping("/user/{userId}")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_AGENDA, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO')")
	public ResponseEntity<List<HierarchicalUnitDto>> fetchAllByUserIdAndInstitutionId(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "userId") Integer userId) {
		log.debug("Input institutionId {}, userId {} ", institutionId, userId);
		List<HierarchicalUnitDto> result = fetchHierarchicalUnitsByUserIdAndInstitutionId.run(userId, institutionId)
				.stream()
				.map(bo -> new HierarchicalUnitDto(bo.getId(), bo.getName()))
				.collect(Collectors.toList());
		log.debug("Output {} ", result);
		return ResponseEntity.ok(result);
	}
	
}
