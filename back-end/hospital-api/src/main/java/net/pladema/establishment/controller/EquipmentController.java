package net.pladema.establishment.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import net.pladema.establishment.controller.dto.EquipmentDto;
import net.pladema.establishment.service.EquipmentBOMapper;
import net.pladema.establishment.service.EquipmentService;
import net.pladema.establishment.service.domain.EquipmentBO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

@RestController
@Tag(name = "Equipment", description = "Equipment")
@RequestMapping("/institutions/{institutionId}/equipment")
public class EquipmentController {

	private static final Logger LOG = LoggerFactory.getLogger(EquipmentController.class);

	private final EquipmentService equipmentService;

	private final EquipmentBOMapper equipmentBOMapper;

	public EquipmentController(EquipmentService equipmentService, EquipmentBOMapper equipmentBOMapper) {
		this.equipmentService = equipmentService;
		this.equipmentBOMapper = equipmentBOMapper;
	}

	@GetMapping("/sector/{sectorId}")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ADMINISTRATIVO_RED_DE_IMAGENES, ADMINISTRADOR_AGENDA')")
	public ResponseEntity<List<EquipmentDto>>  getAllBySector(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "sectorId") Integer sectorId) {
		List<EquipmentBO> equipments = equipmentService.getEquipmentBySector(sectorId);
		List<EquipmentDto> result = equipmentBOMapper.toListEquipmentDto(equipments);
		LOG.debug("Get all equipment by sector => {}", equipments);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/modality/{modalityId}")
	@PreAuthorize("hasPermission(#institutionId, 'TECNICO')")
	public ResponseEntity<List<EquipmentDto>>  getAllByModalityInInstitution(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "modalityId") Integer modalityId) {
		LOG.debug("Input -> institutionId {}, modalityId {}", institutionId, modalityId);
		List<EquipmentBO> equipments = equipmentService.getEquipmentByModalityInInstitution(modalityId, institutionId);
		List<EquipmentDto> result = equipmentBOMapper.toListEquipmentDto(equipments);
		LOG.debug("Output -> {}", result);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/equipmentbyinstitution")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO_RED_DE_IMAGENES, ADMINISTRADOR_AGENDA, TECNICO, INDEXADOR')")
	public ResponseEntity<List<EquipmentDto>>  getAllByInstitution(
			@PathVariable(name = "institutionId") Integer institutionId) {
		List<EquipmentBO> equipments = equipmentService.getEquipmentByInstitution(institutionId);
		List<EquipmentDto> result = equipmentBOMapper.toListEquipmentDto(equipments);
		LOG.debug("Get all equipment by institution => {}", equipments);
		return ResponseEntity.ok(result);
	}
}

