package net.pladema.establishment.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.establishment.controller.dto.HierarchicalUnitStaffDto;
import net.pladema.establishment.service.HierarchicalUnitStaffService;

import net.pladema.establishment.service.domain.HierarchicalUnitStaffBo;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestController
@Tag(name = "Hierarchical Unit Staff", description = "Hierarchical Unit Staff")
@RequestMapping("/institutions/{institutionId}/hierarchicalunitstaff/user/{userId}")
@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE')")
public class HierarchicalUnitStaffController {

	private final HierarchicalUnitStaffService hierarchicalUnitStaffService;

	@GetMapping()
	public ResponseEntity<List<HierarchicalUnitStaffDto>> getByUserId(@PathVariable(name = "institutionId") Integer institutionId,
																	  @PathVariable(name = "userId") Integer userId) {
		log.debug("Input institutionId {}, userId {}", institutionId, userId);
		List<HierarchicalUnitStaffDto> hierarchicalUnitsStaffDto = hierarchicalUnitStaffService.getByUserId(userId, institutionId).stream()
				.map(bo -> new HierarchicalUnitStaffDto(bo.getId(),
						bo.isResponsible(),
						bo.getHierarchicalUnitId(),
						bo.getHierarchicalUnitAlias()))
				.collect(Collectors.toList());
		log.debug("Output {}", hierarchicalUnitsStaffDto);
		return ResponseEntity.ok(hierarchicalUnitsStaffDto);
	}

	@PutMapping()
	public ResponseEntity<Boolean> updateHierarchicalUnits(@PathVariable(name = "institutionId") Integer institutionId,
														   @PathVariable (name = "userId") Integer userId,
														   @RequestBody List<HierarchicalUnitStaffDto> staff) {
		log.debug("Input parameters institutionId {}, userId {}, hierarchicalUnitStaffDtos {}", institutionId, userId ,staff);
		List<HierarchicalUnitStaffBo> bos = staff.stream()
				.map(dto -> new HierarchicalUnitStaffBo(dto.getId(),
						dto.isResponsible(),
						dto.getHierarchicalUnitId(),
						dto.getHierarchicalUnitAlias()))
				.collect(Collectors.toList());
		Boolean result = hierarchicalUnitStaffService.updateHierarchicalUnits(institutionId, userId, bos);
		log.debug("Output {}", result);
		return ResponseEntity.ok(result);
	}
}
