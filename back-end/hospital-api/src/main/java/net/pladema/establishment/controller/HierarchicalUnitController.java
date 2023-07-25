package net.pladema.establishment.controller;

import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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

	@GetMapping()
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE')")
	public ResponseEntity<List<HierarchicalUnitDto>> getAllByInstitution(@PathVariable(name = "institutionId") Integer institutionId) {
		log.debug("Input institutionId {} ", institutionId);
		List<HierarchicalUnitDto> hierarchicalUnitsDto = hierarchicalUnitService.getByInstitution(institutionId).stream()
				.map(bo -> new HierarchicalUnitDto(bo.getId(), bo.getName()))
				.collect(Collectors.toList());
		log.debug("Output {} ", hierarchicalUnitsDto);
		return ResponseEntity.ok(hierarchicalUnitsDto);
	}
}
