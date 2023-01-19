package net.pladema.establishment.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import net.pladema.establishment.controller.dto.EquipmentDto;
import net.pladema.establishment.controller.dto.SectorDto;
import net.pladema.establishment.repository.EquipmentRepository;
import net.pladema.establishment.repository.SectorRepository;
import net.pladema.establishment.repository.entity.Equipment;
import net.pladema.establishment.repository.entity.Sector;
import net.pladema.establishment.service.EquipmentBOMapper;
import net.pladema.establishment.service.EquipmentService;
import net.pladema.establishment.service.domain.EquipmentBO;
import net.pladema.establishment.service.domain.SectorBO;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import java.util.List;

@RestController
@Tag(name = "Equipment", description = "Equipment")
@RequestMapping("/institutions/{institutionId}/equipment")
public class EquipmentController {

	private static final Logger LOG = LoggerFactory.getLogger(EquipmentController.class);

	private EquipmentService equipmentService;

	private EquipmentBOMapper equipmentBOMapper;

	public EquipmentController(EquipmentService equipmentService, EquipmentBOMapper equipmentBOMapper) {
		this.equipmentService = equipmentService;
		this.equipmentBOMapper = equipmentBOMapper;
	}



	@GetMapping("/sector/{sectorId}")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ADMINISTRADOR_AGENDA')")
	public ResponseEntity<List<EquipmentDto>>  getAllBySector(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "sectorId") Integer sectorId) {
		List<EquipmentBO> equipments = equipmentService.getEquipmentBySector(sectorId);
		List<EquipmentDto> result = equipmentBOMapper.toListEquipmentDto(equipments);
		LOG.debug("Get all Sectors of Type => {}", equipments);
		return ResponseEntity.ok(result);
	}
}

