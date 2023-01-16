package net.pladema.establishment.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import net.pladema.establishment.controller.mapper.RoomMapper;
import net.pladema.establishment.repository.RoomRepository;
import net.pladema.establishment.repository.SectorRepository;
import net.pladema.establishment.repository.entity.Sector;

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
@Tag(name = "Sector", description = "Sector")
@RequestMapping("/institution/{institutionId}/sectoroftype/{sectorTypeId}")
public class SectorOfTypeController {

	private static final Logger LOG = LoggerFactory.getLogger(SectorOfTypeController.class);

	private SectorRepository sectorRepository;

	public SectorOfTypeController(SectorRepository sectorRepository) {
		this.sectorRepository = sectorRepository;
	}


	@GetMapping
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ADMINISTRADOR_AGENDA')")
	public ResponseEntity<List<Sector>>  getAllOfType(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "sectorTypeId") Short sectorTypeId) {
		List<Sector> sectors = sectorRepository.getSectorsOfTypeByInstitution(institutionId,sectorTypeId );
		LOG.debug("Get all Sectors of Type => {}", sectors);
		return ResponseEntity.ok(sectors);
	}
	

}
