package net.pladema.establishment.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import net.pladema.establishment.controller.dto.RoomDto;
import net.pladema.establishment.controller.mapper.RoomMapper;
import net.pladema.establishment.repository.RoomRepository;
import net.pladema.establishment.repository.SectorRepository;
import net.pladema.establishment.repository.entity.Room;
import net.pladema.establishment.repository.entity.Sector;

@RestController
@Api(value = "Sector", tags = { "Sector" })
@RequestMapping("/sector")
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

	@GetMapping()
	public ResponseEntity<List<Sector>> getAll(){
		List<Sector> sectors = sectorRepository.findAll();
		LOG.debug("Get all Sectors => {}", sectors);
		return ResponseEntity.ok(sectors);
	}
	
	@GetMapping("/{sectorId}/specialty/{specialtyId}/rooms")
	public ResponseEntity<List<RoomDto>> getAllRoomsBySectorAndSpecialty(@PathVariable(name = "sectorId") Integer sectorId,
			@PathVariable(name = "specialtyId") Integer specialtyId) {
		List<Room> rooms = roomRepository.getAllBySector(sectorId, specialtyId);
		LOG.debug("Get all Rooms => {}", rooms);
		return ResponseEntity.ok(roomMapper.toListRoomDto(rooms));
	}
}
