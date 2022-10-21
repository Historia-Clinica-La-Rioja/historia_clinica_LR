package net.pladema.establishment.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import net.pladema.establishment.controller.dto.RoomDto;
import net.pladema.establishment.controller.mapper.RoomMapper;
import net.pladema.establishment.repository.RoomRepository;
import net.pladema.establishment.repository.SectorRepository;
import net.pladema.establishment.repository.entity.Room;
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
@RequestMapping("/institution/{institutionId}/sector")
public class SectorController  {

	private static final Logger LOG = LoggerFactory.getLogger(SectorController.class);
	
	private SectorRepository sectorRepository;
	
	private RoomRepository roomRepository;
	
	private RoomMapper roomMapper;

	public SectorController(SectorRepository sectorRepository, RoomRepository roomRepository, RoomMapper roomMapper) {
		this.sectorRepository = sectorRepository;
		this.roomRepository = roomRepository;
		this.roomMapper = roomMapper;
	}

	@GetMapping
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ADMINISTRADOR_AGENDA, ESPECIALISTA_MEDICO, ENFERMERO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA')")
	public ResponseEntity<List<Sector>> getAll(@PathVariable(name = "institutionId") Integer institutionId) {
		List<Sector> sectors = sectorRepository.getSectorsByInstitution(institutionId);
		LOG.debug("Get all Sectors => {}", sectors);
		return ResponseEntity.ok(sectors);
	}
	
	@GetMapping("/{sectorId}/specialty/{specialtyId}/rooms")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO')")
	public ResponseEntity<List<RoomDto>> getAllRoomsBySectorAndSpecialty(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "sectorId") Integer sectorId,
			@PathVariable(name = "specialtyId") Integer specialtyId) {
		List<Room> rooms = roomRepository.getAllBySectorAndInstitution(sectorId, specialtyId, institutionId);
		LOG.debug("Get all Rooms => {}", rooms);
		return ResponseEntity.ok(roomMapper.toListRoomDto(rooms));
	}
}
