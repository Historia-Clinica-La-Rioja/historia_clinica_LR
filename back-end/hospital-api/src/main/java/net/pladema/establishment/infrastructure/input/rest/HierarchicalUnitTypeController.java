package net.pladema.establishment.infrastructure.input.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.establishment.application.hierarchicalunittypes.FetchHierarchicalUnitTypesByInstitutionId;

import net.pladema.establishment.infrastructure.input.rest.dto.HierarchicalUnitTypeDto;

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
@Tag(name = "Hierarchical Unit Type", description = "Hierarchical Unit Type")
@RequestMapping("/institutions/{institutionId}/hierarchical-unit-type")
public class HierarchicalUnitTypeController {

	private final FetchHierarchicalUnitTypesByInstitutionId fetchHierarchicalUnitTypesByInstitutionId;

	@GetMapping()
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, ESPECIALISTA_MEDICO, ENFERMERO, PROFESIONAL_DE_SALUD, " +
			"ESPECIALISTA_EN_ODONTOLOGIA, ADMINISTRADOR_INSTITUCIONAL_PRESCRIPTOR, PERSONAL_DE_ESTADISTICA')")
	public ResponseEntity<List<HierarchicalUnitTypeDto>> getAllByInstitution(@PathVariable(name = "institutionId") Integer institutionId) {
		log.debug("Input institutionId {} ", institutionId);
		List<HierarchicalUnitTypeDto> result = fetchHierarchicalUnitTypesByInstitutionId.run(institutionId)
				.stream()
				.map(hut -> new HierarchicalUnitTypeDto(hut.getId(), hut.getDescription()))
				.collect(Collectors.toList());
		log.debug("Output {} ", result);
		return ResponseEntity.ok(result);
	}
}
